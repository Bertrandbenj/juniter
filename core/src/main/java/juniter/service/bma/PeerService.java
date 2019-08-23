package juniter.service.bma;

import juniter.core.event.RenormalizedNet;
import juniter.core.model.dbo.NetStats;
import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dto.node.NodeSummaryDTO;
import juniter.repository.jpa.net.EndPointsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ConditionalOnExpression("${juniter.loader.useDefault:true}") // Must be up for dependencies
@Service
@Order(10)
public class PeerService {


    public static final Logger LOG = LogManager.getLogger(PeerService.class);

    @Autowired
    private ApplicationEventPublisher coreEventBus;


    @Autowired
    private EndPointsRepository endPointRepo;

    private final Map<String, NetStats> BMAHosts = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, NetStats> WS2PHosts = Collections.synchronizedMap(new HashMap<>());


    private BlockingQueue<NetStats> pingingQueue = new LinkedBlockingDeque<>(20);


    @Autowired
    private RestTemplate restTemplate;

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;


    @PostConstruct
    public void initConsumers() {

        Runnable cons = () -> {
            try {
                NetStats value;
                while ((value = pingingQueue.take()) != null) {
                    test(EndPointType.BMAS, value);
                }
                Thread.sleep(2000);

            } catch (Exception e) {
                LOG.error("initConsumers ERROR " + e.getMessage());
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(cons, "consumer" + i).start();
        }

    }


    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 1000 * 10)
    public void pings() {

        while (nextHost(EndPointType.BMAS).isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOG.error(e);

            }
        }

        nextHost(EndPointType.BMAS).ifPresent(ns -> {
            try {
                pingingQueue.put(ns);
            } catch (InterruptedException e) {
                LOG.error(e);
            }
        });


    }

    public void test(EndPointType type, NetStats next) {
        var prevTime = System.nanoTime();
        try {
            if (restTemplate.getForEntity(next.getHost() + "/node/summary", NodeSummaryDTO.class).getStatusCodeValue() == 200) {

                reportSuccess(type, next.getHost());

                next.setLastResponseTime(System.nanoTime() - prevTime);
            }
        } catch (Exception e) {
            LOG.debug("pinging "+ e);
        }

    }

    @Transactional(readOnly = true)
    public void reload(EndPointType type) {

        var queue = getQueue(type);

        synchronized (queue) {

            var list = type == EndPointType.BMAS ? endPointRepo.endpointssBMAS() : type == EndPointType.WS2P ? endPointRepo.get(EndPointType.WS2P, EndPointType.WS2PS) : null;

            var urls = list.stream()
                    .map(EndPoint::url)
                    .collect(Collectors.toList());
            urls.addAll(configuredNodes);
            urls.forEach(url -> {
                var ns = new NetStats(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(0), System.currentTimeMillis(), 1L, Math.random(), url);
                queue.putIfAbsent(url, ns);
            });
        }

        renormalize(type);
    }


    private Map<String, NetStats> getQueue(EndPointType type) {
        return type == EndPointType.BMAS || type == EndPointType.BASIC_MERKLED_API ? BMAHosts : type == EndPointType.WS2P || type == EndPointType.WS2PS ? WS2PHosts : null;

    }

    @Transactional(readOnly = true)
    public Optional<NetStats> nextHost(EndPointType type) {

        var queue = getQueue(type);

        var rand = Math.random();

        //BMAHosts.values().stream().min(Comparator.naturalOrder()).ifPresent(top -> LOG.info("Min Found " + top));
        //
        synchronized (queue) {
            var normalizeAggregate = 0.;

            for (Map.Entry<String, NetStats> h : queue.entrySet()) {
                normalizeAggregate += h.getValue().getLastNormalizedScore();

                if (normalizeAggregate >= rand) {
                    //LOG.info("normalizeAggregate " + normalizeAggregate);

                    h.getValue().getCount().incrementAndGet();
                    return Optional.of(h.getValue());
                }
            }
        }

        reload(type);
        LOG.error("no peers found in BMAHosts[" + BMAHosts.size() + "]");
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public List<NetStats> nextHosts(EndPointType type, int nb) {

        var queue = getQueue(type);

        List<NetStats> res = new ArrayList<>(nb);

        var rand = Math.random();

        //BMAHosts.values().stream().min(Comparator.naturalOrder()).ifPresent(top -> LOG.info("Min Found " + top));
        //
        synchronized (queue) {
            for (int x = nb; x > 0; x--) {

                var normalizeAggregate = 0.;

                for (Map.Entry<String, NetStats> h : queue.entrySet()) {
                    normalizeAggregate += h.getValue().getLastNormalizedScore();

                    if (normalizeAggregate >= rand) {
                        //LOG.info("normalizeAggregate " + normalizeAggregate);

                        h.getValue().getCount().incrementAndGet();
                        res.add(h.getValue());
                    }
                }
            }
        }

        reload(type);
        LOG.error("no peers found in BMAHosts[" + queue.size() + "]");
        return res;
    }

    @Scheduled(fixedRate = 1000 * 10, initialDelay = 60 * 1000)
    public void renormalizer() {
        renormalize(EndPointType.BMAS);
        renormalize(EndPointType.WS2P);
    }


    @Transactional
    private void renormalize(EndPointType type) {

        var queue = getQueue(type);

        synchronized (queue) {
            var sum = queue.values().stream().mapToDouble(NetStats::score).sum();
            var cntAll = queue.values().stream().mapToDouble(ns -> ns.getCount().doubleValue()).sum();
            var cntSucc = queue.values().stream().mapToDouble(ns -> ns.getSuccess().doubleValue()).sum();

            LOG.debug("renormalize - success: " + cntSucc + "/" + cntAll);
            LOG.debug("renormalize - top 6 : \n" + queue.values().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(6)
                    .map(NetStats::getHost)
                    .collect(Collectors.joining(",")));


            coreEventBus.publishEvent(new RenormalizedNet(
                    queue.values().stream()
                            .sorted(Comparator.reverseOrder())
                            .filter(ns -> ns.getLastNormalizedScore() > 0.001 && ns.getSuccess().get() > 1)
                            .collect(Collectors.toList())));

            for (Map.Entry<String, NetStats> h : queue.entrySet()) {
                h.getValue().normalizedScore(sum);
            }
        }
    }

    public void reportSuccess(EndPointType type, String url) {

        var queue = getQueue(type);

        synchronized (queue) {
            var h = queue.get(url);
            var x = h.getSuccess().incrementAndGet();
            if (x > 100) {
                LOG.debug("renormalize - reset: " + h.getHost());
                h.getSuccess().lazySet(1);
                h.getCount().lazySet(1);
                h.setLastNormalizedScore(Math.random() * 0.5);
            }
            LOG.debug(" " + url + " : " + x);
        }
    }

    public void reportError(EndPointType type, String url) {

        var queue = getQueue(type);

        synchronized (queue) {
            var h = queue.get(url);
            var x = h.getSuccess().incrementAndGet();
            if (x > 100) {
                LOG.debug("renormalize - reset: " + h.getHost());
                h.getSuccess().lazySet(1);
                h.getCount().lazySet(1);
                h.setLastNormalizedScore(Math.random() * 0.5);
            }
            LOG.debug(" " + url + " : " + x);
        }
    }


}
