package juniter;

import juniter.service.front.AdminFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@EnableAutoConfiguration//(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan
@EnableJpaRepositories("juniter.repository")
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

    private static final Logger LOG = LogManager.getLogger();


    public static void main(String[] args) {
        LOG.info("!!! Entering Application main !!!");

        var context = SpringApplication.run(Application.class, args);
        LOG.info("!!! SpringApplication.runPeerCheck !!!");


        var useJavaFX = context.getEnvironment().getProperty("juniter.useJavaFX", Boolean.class);
        if (useJavaFX) {
            AdminFX.launchGUI(AdminFX.class, context);
            LOG.info("!!! AdminFX.launchGUI !!!");
        }


    }

    @Bean
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

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        return builder.dataSource(dataSource) //
                .packages("juniter") //
                .persistenceUnit("juniter") //
                .build();
    }


}