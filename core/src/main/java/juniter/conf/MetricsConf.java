package juniter.conf;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
public class MetricsConf extends MetricsConfigurerAdapter {

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        // registerReporter allows the MetricsConfigurerAdapter to
        // shut down the reporter when the Spring context is closed


        registerReporter(ConsoleReporter
                .forRegistry(metricRegistry)
                .outputTo(System.out)
                .filter(MetricFilter.ALL)
                .build())
                .start(300, TimeUnit.SECONDS);

    }


//    private AtlasConfig atlasConfig = new AtlasConfig() {
//        @Override
//        public Duration step() {
//            return Duration.ofSeconds(10);
//        }
//
//        @Override
//        public String get(String k) {
//            return null; // accept the rest of the defaults
//        }
//
//        @Override
//        public Map<String, String> commonTags() {
//            return Collections.singletonMap("juniter", "juniter");
//        }
//
//        public boolean autoStart() {
//            return true;
//        }
//
//    };

    private PrometheusConfig promeheusConfig = new PrometheusConfig() {
        @Override
        public String get(String key) {
            return null;
        }
    };

    @Bean
    JvmThreadMetrics threadMetrics() {
        return new JvmThreadMetrics();
    }

    @Bean
    public MeterRegistry metricsRegistry() {
        var compositeMeterRegistry = new CompositeMeterRegistry();
        var simpleMeterRegistry = new SimpleMeterRegistry();
        var collectorRegistry = new CollectorRegistry();

        var prometheusRegistry = new PrometheusMeterRegistry(promeheusConfig);//, collectorRegistry, Clock.SYSTEM);
       // var atlasMeterRegistry = new AtlasMeterRegistry(atlasConfig, Clock.SYSTEM);

        compositeMeterRegistry.add(simpleMeterRegistry);
       // compositeMeterRegistry.add(atlasMeterRegistry);
        compositeMeterRegistry.add(prometheusRegistry);

//        Timer timer = compositeMeterRegistry.timer("blockchain.currentChained");
//        timer.record(() -> {
//            try {
//                MILLISECONDS.sleep(1500);
//            } catch (InterruptedException ignored) {
//            }
//        });
//
//        timer.record(3000, MILLISECONDS);

        //compositeMeterRegistry.timer("index.trimGlobal");



        return compositeMeterRegistry;

    }

}