package juniter.service.bma;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.event.RenormalizedNet;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.NetStats;
import juniter.core.model.dto.node.NodeSummaryDTO;
import juniter.core.model.dto.raw.WrapperResponse;
import juniter.repository.jpa.net.EndPointsRepository;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private final Map<String, NetStats> BMAHosts = new ConcurrentHashMap<>();

    private final Map<String, NetStats> WS2PHosts = new ConcurrentHashMap<>();

    private BlockingQueue<NetStats> pingingQueue = new LinkedBlockingDeque<>(20);


    @Getter
    private AtomicBoolean pinging = new AtomicBoolean(true);

    @Autowired
    private RestTemplate restTemplate;

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;

    @PostConstruct
    public void initConsumers() {

        // buff the restTemplate
        restTemplate.setErrorHandler(new ResponseErrorHandler() {

            final Logger LOG = LogManager.getLogger(ResponseErrorHandler.class);


            final ObjectMapper jsonReader = new ObjectMapper();

            @Override
            public boolean hasError(ClientHttpResponse response) {

                try {
                    int code = response.getRawStatusCode();

                    if (response.getStatusCode().is2xxSuccessful()) {
                        return false;
                    }

                    if (response.getStatusCode().is3xxRedirection()) {
                        LOG.info("redirected, no big deal");
                        return false;
                    }


                    if (response.getStatusCode().is4xxClientError()) {
                        LOG.error("client error ");

                        try {
                            LOG.info("code : {}, message: {}", response.getRawStatusCode(), response.getStatusText());
                            LOG.info("Headers  : {}", response.getHeaders());
                            LOG.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
                        } catch (IOException e) {
                            LOG.error("IO during Error investigation  ", e);
                        }

                        return true;
                    }

                    if (response.getStatusCode().is5xxServerError()) {
                        LOG.error("server error");
                        switch (code) {
                            case 502:
                                return true;
                            case 500:
                                try {
                                    var duniterCode = jsonReader.readValue(response.getBody(), WrapperResponse.class);
                                    LOG.warn("Duniter Error : {}", duniterCode);
                                    return false;
                                } catch (Exception e) {
                                    LOG.error("not a duniter error investigation  ", e);
                                }
                            default:
                                return true;
                        }
                    }

                } catch (IOException e) {
                    LOG.error("IO while getting status code => pretty bad !! ", e);
                }
                return true;

            }

            @Override
            public void handleError(ClientHttpResponse response) {
                LOG.info("WE SHOULDNT ENDUP HERE .... EVER ");
            }

            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                var repURL = url.getScheme() + "://" + url.getAuthority() + "/";
                LOG.info("reporting error " + response.getRawStatusCode() + " on " + repURL + " after attempt on " + url.toString());

                if (response.getStatusCode().is5xxServerError()) {
                    reportError(EndPointType.BMAS, url.toString().substring(url.toString().indexOf(url.getPath())) + "/");

                } else if (response.getStatusCode().is4xxClientError()) {
                    LOG.error("WRITE NEW WAY TO HANDLE THIS ");
                } else {
                    LOG.error("WRITE NEW WAY TO HANDLE THIS ");
                }


            }

        });

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


    @Scheduled(initialDelay = 30 * 1000, fixedDelay = 1000 * 60 * 10)
    public void pings() {

        if (!pinging.get()) {
            return;
        }

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
                next.setLastResponseTime(System.nanoTime() - prevTime);
                reportSuccess(type, next.getHost());
            } else {
                reportError(type, next.getHost());
            }
        } catch (Exception e) {
            LOG.debug("pinging " + e);
        }

    }


    private List<String> getEndPoint(EndPointType type) {

        var res = new ArrayList<String>();
        if (type == EndPointType.BMAS || type == EndPointType.BASIC_MERKLED_API) {
            res.addAll(endPointRepo.getUrls(EndPointType.BMAS, EndPointType.BASIC_MERKLED_API));
            res.addAll(configuredNodes);
            LOG.info("loading nodes from db {} \n and from conf {}", res, configuredNodes);

        }

        if (type == EndPointType.WS2P || type == EndPointType.WS2PS) {
            res.addAll(endPointRepo.getUrls(EndPointType.WS2P, EndPointType.WS2PS));
            LOG.info("loading nodes from db {}", res);

        }
        return res;
    }

    private Map<String, NetStats> getQueue(EndPointType type) {
        var queue = type == EndPointType.BMAS || type == EndPointType.BASIC_MERKLED_API ? BMAHosts
                : type == EndPointType.WS2P || type == EndPointType.WS2PS ? WS2PHosts
                : null;


        if (queue.isEmpty()) {
            LOG.info("rebuild queue because its empty ");
            getEndPoint(type).forEach(url -> {
                queue.computeIfPresent(url, (x, y) -> {

                    LOG.info("compute if present ");
                    y.getCount().incrementAndGet();
                    return y;
                });
                queue.putIfAbsent(url, new NetStats(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(0), System.currentTimeMillis(), 1L, Math.random(), url));
            });

        }

        return queue;

    }

    public Optional<NetStats> nextHost(EndPointType type) {
        return Optional.of(nextHosts(type, 1).get(0));
    }

    @Transactional(readOnly = true)
    public List<NetStats> nextHosts(EndPointType type, int nb) {


        var queue = getQueue(type);

        List<NetStats> res = new ArrayList<>(nb);
        var rand = Math.random();

      //  synchronized (queue) {
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
       // }

        LOG.debug("next " + nb + " hosts of type " + type + " among [" + queue.size() + "] => " + res);
        return res;
    }

    @Scheduled(fixedRate = 1000 * 60*5, initialDelay = 60 * 1000)
    public void renormalize() {
        renormalize(EndPointType.BMAS);
    }


    @Transactional
    public void renormalize(EndPointType type) {

        var queue = getQueue(type);
        //synchronized (queue) {
            var sum = queue.values().stream().mapToDouble(NetStats::score).sum();
            var cntAll = queue.values().stream().mapToDouble(ns -> ns.getCount().doubleValue()).sum();
            var cntSucc = queue.values().stream().mapToDouble(ns -> ns.getSuccess().doubleValue()).sum();
            LOG.info("renormalize  " + type + " - " + cntSucc + "/" + cntAll + " of wich top 6 : \n" + queue.values().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(6)
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

       // }
    }

    public void reportSuccess(EndPointType type, String url) {

        var queue = getQueue(type);

       // synchronized (queue) {
            var h = queue.get(url);
            var x = h.getSuccess().incrementAndGet();
            if (x > 100) {
                LOG.debug("renormalize - reset: " + h.getHost());
                h.getSuccess().set(0);
                h.getCount().set(1);
                h.setLastNormalizedScore(Math.random() * 0.2);
            }
            LOG.debug(" " + url + " : " + x);
        //}
    }


    public void reportError(EndPointType type, String url) {

        var queue = getQueue(type);

        synchronized (queue) {
            var h = queue.get(url);
            var x = h.getError().incrementAndGet();
            if (x > 100) {
                LOG.debug("renormalize - reset: " + h.getHost());
                h.getSuccess().set(0);
                h.getCount().set(1);
                h.setLastNormalizedScore(Math.random() * 0.2);
            }
        }
    }

}
