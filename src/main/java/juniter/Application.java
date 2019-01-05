package juniter;

import juniter.service.adminfx.FrontPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

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

        var useJavaFX = context.getEnvironment().getProperty("juniter.useJavaFX", Boolean.class);
        if (useJavaFX) {
            LOG.info("!!! FrontPage.launching GUI !!!");
            FrontPage.launchGUI(FrontPage.class, context);
        }


    }


}