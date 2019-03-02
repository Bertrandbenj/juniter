package juniter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Configuration
//@EnableAutoConfiguration//(exclude = { DataSourceAutoConfiguration.class })
//@ComponentScan
@EnableJpaRepositories("juniter.repository")
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan("juniter.core.model")
public class Application {

    private static final Logger LOG = LogManager.getLogger();


    public static void main(String[] args) {
        LOG.info("!!! Entering juniter.Application main !!!");

        var context = SpringApplication.run(Application.class, args);


    }


}