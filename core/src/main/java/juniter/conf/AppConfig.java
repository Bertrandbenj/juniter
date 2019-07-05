package juniter.conf;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.stream.Stream;


@Configuration
@ComponentScan("juniter")
public class AppConfig {

    private static final Logger LOG = LogManager.getLogger(AppConfig.class);


    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;

    @Bean
    public IPFS ipfs() {
        var ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));

        LOG.info(" ==== IPFS INIT =====");
        try {
            ipfs.config.show().forEach((k, v) -> LOG.info("  --  kv: " + k + " : " + v));
//            ipfs.pin.add(Multihash.fromBase58("QmUhVpSmXnTTnpyRivjYADjBEG5MYtr4eP4JEE2qxfVjMd"));
//            ipfs.pin.add(Multihash.fromBase58("QmRBFKnivhKQxy3kZ4vZUCEGMtrckdu9GMeNjkcM497P9z"));
//            ipfs.pin.add(Multihash.fromBase58("QmNqToxUD8nUh476UsFyMUiSTTSgH2WAAnrSR3qL95iHXK"));
//            ipfs.pin.add(Multihash.fromBase58("QmUNLLsPACCz1vLxQVkXqqLX5R1X345qqfHbsf67hvA3Nn"));

        } catch (IOException e) {
            LOG.error("Initializing IPFS ", e);
        }

        return ipfs;
    }


    @Bean
    public Path workingDir() {

        LOG.info("Setting workingDir to " + dataPath);

        var res = Path.of(dataPath);

        if (res.toFile().mkdirs()) {
            LOG.info("The data directory didn't exists, just created it ");
        }
        Stream.of("bindex", "blockchain", "blocks", "cindex", "dot", "dump", "duniter", "iindex", "json", "mindex", "parquets", "sindex")
                .map(dir -> res.resolve(dir).toFile())
                .forEach(File::mkdir);
        return res;
    }

    @Bean
    public ModelMapper modelMapper() {
        final var res = new ModelMapper();
        res.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        return res;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(5 * 1000)
                .setReadTimeout(5 * 1000)
                .build();
    }


    @Bean(name = "AsyncJuniterPool")
    public Executor asyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncJuniter-");
        executor.initialize();
        return executor;
    }

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

//    @Bean
//    public JdbcTemplate getJdbcTemplate() {
//        return new JdbcTemplate(dataSource());
//    }


//    @Primary
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource dataSource() {
//
//        // no need shutdown, EmbeddedDatabaseFactoryBean will take care of this
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        EmbeddedDatabase db = builder
//                .setType(EmbeddedDatabaseType.HSQL) //.H2 or .DERBY
////                .addScript("db/sql/create-db.sql")
////                .addScript("db/sql/insert-data.sql")
//                .build();
//        return db;
//    }


    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {

        return builder.dataSource(dataSource)
                .packages("juniter")
                .persistenceUnit("juniter")
                .build();
    }


}
