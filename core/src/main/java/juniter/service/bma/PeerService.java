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
                Thread.sleep(200);

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
            LOG.debug("pinging " + e);
        }

    }

    @Transactional(readOnly = true)
    public void reload(EndPointType type) {
        LOG.info("reload " + type);
        var queue = getQueue(type);

        synchronized (queue) {

            var urls = getEndPoint(type).stream()
                    .map(EndPoint::url)
                    .collect(Collectors.toList());
            urls.addAll(configuredNodes);
            LOG.info("added configures node " + urls + " from " + configuredNodes);
            urls.forEach(url -> {
                queue.computeIfPresent(url, (x, y) -> {

                    LOG.info("compute if present ");
                    y.getCount().incrementAndGet();
                    return y;
                });
                queue.putIfAbsent(url, new NetStats(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(0), System.currentTimeMillis(), 1L, Math.random(), url));
            });
        }

        renormalize(type);
    }

    private List<EndPoint> getEndPoint(EndPointType type) {
        return type == EndPointType.BMAS || type == EndPointType.BASIC_MERKLED_API ? endPointRepo.get(EndPointType.BMAS, EndPointType.BASIC_MERKLED_API)
                : type == EndPointType.WS2P || type == EndPointType.WS2PS ? endPointRepo.get(EndPointType.WS2P, EndPointType.WS2PS)
                : null;
    }

    private Map<String, NetStats> getQueue(EndPointType type) {
        return type == EndPointType.BMAS || type == EndPointType.BASIC_MERKLED_API ? BMAHosts
                : type == EndPointType.WS2P || type == EndPointType.WS2PS ? WS2PHosts
                : null;
    }

    public Optional<NetStats> nextHost(EndPointType type) {
        return Optional.of(nextHosts(type, 1).get(0));
    }

    @Transactional(readOnly = true)
    public List<NetStats> nextHosts(EndPointType type, int nb) {

        var queue = getQueue(type);
        List<NetStats> res = new ArrayList<>(nb);
        var rand = Math.random();

        synchronized (queue) {
            for (int x = nb; x > 0; x--) {

                var normalizeAggregate = 0.;

                for (Map.Entry<String, NetStats> h : queue.entrySet()) {
                    normalizeAggregate += h.getValue().getLastNormalizedScore();

                    if (normalizeAggregate >= rand) {
                        //LOG.info("normalizeAggregate " + normalizeAggregate);

                        h.getValue().getCount().incrementAndGet();
                        res.add(h.getValue());
                        if (res.size() == nb) break;
                    }
                }
            }
        }

        //reload(type);
        LOG.warn("next " + nb + " hosts of type " + type + " among [" + queue.size() + "] => " + res);
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
        LOG.info("renormalize " + type + " => " + queue);
        synchronized (queue) {
            var sum = queue.values().stream().mapToDouble(NetStats::score).sum();
            var cntAll = queue.values().stream().mapToDouble(ns -> ns.getCount().doubleValue()).sum();
            var cntSucc = queue.values().stream().mapToDouble(ns -> ns.getSuccess().doubleValue()).sum();

            LOG.debug("renormalize - success: " + cntSucc + "/" + cntAll);
            LOG.info("renormalize - top 6 : \n" + queue.values().stream()
                    .sorted(Comparator.reverseOrder())
                    //.filter(ns -> ns.getLastNormalizedScore() > 0.001)
                    .map(NetStats::getHost)
                    .collect(Collectors.joining(",")));

            if (queue.size() > 0) {

                for (Map.Entry<String, NetStats> h : queue.entrySet()) {
                    h.getValue().normalizedScore(sum);
                }
                coreEventBus.publishEvent(new RenormalizedNet(
                        queue.values().stream()
                                .sorted(Comparator.reverseOrder())
                                //.filter(ns -> ns.getLastNormalizedScore() > 0.001)
                                //.filter(ns -> ns.getSuccess().get() > 0)
                                // .limit(6)
                                .collect(Collectors.toList())));
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
                h.getSuccess().set(0);
                h.getCount().set(1);
                h.setLastNormalizedScore(Math.random() * 0.2);
            }
            LOG.debug(" " + url + " : " + x);
        }
    }

}
