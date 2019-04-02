package juniter.service.bma;

import juniter.core.event.CoreEventBus;
import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dto.NodeSummaryDTO;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.net.EndPointsRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
import java.util.stream.IntStream;

@ConditionalOnExpression("${juniter.loader.useDefault:true}") // Must be up for dependencies
@Service
@Order(10)
public class PeerService {

    public static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CoreEventBus coreEventBus;

    @Autowired
    private EndPointsRepository endPointRepo;

    final Map<String, NetStats> hosts = Collections.synchronizedMap(new HashMap<>());

    public BlockingQueue<NetStats> pingingQueue = new LinkedBlockingDeque<>(20);

    @Autowired
    private BlockRepository blockRepo;
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
                    test(value);
                }
                Thread.sleep(2000);

            } catch (Exception e) {
                LOG.error("PingCOnsumer ERROR " + e.getMessage());
            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(cons, "consumer" + i).start();
        }

    }


    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 1000 * 60 * 60)
    public void pings() {

        while (!nextHost().isPresent()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        new Thread(() -> {
            IntStream.range(0, 200).forEach(n -> {
                nextHost().ifPresent(ns -> {
                    try {
                        pingingQueue.put(ns);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

            });
        }).start();

    }

    public void test(NetStats next) {
        var prevTime = System.nanoTime();
        try {
            if (restTemplate.getForEntity(next.getHost() + "/node/summary", NodeSummaryDTO.class).getStatusCodeValue() == 200) {

                reportSuccess(next.getHost());

                next.setLastResponseTime(System.nanoTime() - prevTime);
            }
        } catch (Exception e) {

        }

    }

    @Transactional(readOnly = true)
    public void load() {
        synchronized (hosts) {
            var urls = endPointRepo.endpointssBMAS().stream()
                    .filter(ep -> {
                        try{
                            var split = ep.getPeer().getBlock().split("-");
                            var parse = Integer.parseInt(split[0]);
                            return parse < blockRepo.currentBlockNumber() - 100;
                        }catch(Exception e ){
                            return false;
                        }

                    })
                    .map(EndPoint::url)
                    .collect(Collectors.toList());
            urls.addAll(configuredNodes);
            urls.forEach(url -> {
                var ns = new NetStats(new AtomicInteger(1), new AtomicInteger(1), System.currentTimeMillis(), 1L, Math.random(), url);
                hosts.putIfAbsent(url, ns);
            });
        }
        renormalize();
    }


    @Transactional(readOnly = true)
    public Optional<NetStats> nextHost() {

        var rand = Math.random();

        //hosts.values().stream().min(Comparator.naturalOrder()).ifPresent(top -> LOG.info("Min Found " + top));
        //
        synchronized (hosts) {
            var normalizeAggregate = 0.;

            for (Map.Entry<String, NetStats> h : hosts.entrySet()) {
                normalizeAggregate += h.getValue().getLastNormalizedScore();

                if (normalizeAggregate >= rand) {
                    //LOG.info("normalizeAggregate " + normalizeAggregate);

                    h.getValue().count.incrementAndGet();
                    return Optional.of(h.getValue());
                }
            }
        }

        load();
        LOG.error("no peers found in hosts[" + hosts.size() + "]");
        return Optional.empty();
    }

    @Transactional
    @Scheduled(fixedRate = 1000 * 10, initialDelay = 60 * 1000)
    public void renormalize() {
        synchronized (hosts) {
            var sum = hosts.values().stream().mapToDouble(NetStats::score).sum();
            var cntAll = hosts.values().stream().mapToDouble(ns -> ns.getCount().doubleValue()).sum();
            var cntSucc = hosts.values().stream().mapToDouble(ns -> ns.getSuccess().doubleValue()).sum();

            LOG.debug("renormalize - success: " + cntSucc + "/" + cntAll);
            LOG.debug("renormalize - top 6 : \n" + hosts.values().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(6)
                    .map(NetStats::getHost)
                    .collect(Collectors.joining(",")));

            coreEventBus.sendEventRenormalizedPeer(hosts.values().stream()
                    .sorted(Comparator.reverseOrder())
                    .filter(ns -> ns.lastNormalizedScore > 0.001 && ns.success.get() > 1)
                    .collect(Collectors.toList()));

            for (Map.Entry<String, NetStats> h : hosts.entrySet()) {
                h.getValue().normalizedScore(sum);
            }
        }
    }

    public void reportSuccess(String url) {
        synchronized (hosts) {
            var h = hosts.get(url);
            var x = h.success.incrementAndGet();
            if (x > 100) {
                LOG.debug("renormalize - reset: " + h.getHost());
                h.success.lazySet(1);
                h.count.lazySet(1);
                h.setLastNormalizedScore(Math.random() * 0.5);
            }
            LOG.debug(" " + url + " : " + x);
        }
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public class NetStats implements Comparable<NetStats> {

        AtomicInteger count;
        AtomicInteger success;
        Long lastUpdate;
        Long lastResponseTime;
        Double lastNormalizedScore;
        String host;


        Double score() {
            var ratio = success.doubleValue() / count.doubleValue();
            var age = System.currentTimeMillis() - lastUpdate;

            var x = ratio * age;

            return x * success.doubleValue();
        }

        Double normalizedScore(Double sum) {
            lastNormalizedScore = score() / sum;
            return lastNormalizedScore;
        }


        @Override
        public int compareTo(NetStats o) {
            return lastNormalizedScore.compareTo(o.lastNormalizedScore);
        }
    }


}
