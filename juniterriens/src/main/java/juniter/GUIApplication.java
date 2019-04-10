package juniter;

import com.sun.javafx.application.LauncherImpl;
import com.sun.javafx.tk.Toolkit;
import juniter.juniterriens.FrontPage;
import juniter.juniterriens.include.FirstPreloader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


//@Configuration
//@EnableAutoConfiguration//(exclude = { DataSourceAutoConfiguration.class })
//@ComponentScan
@EnableJpaRepositories("juniter.repository")
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan("juniter.core.model")
//@ImportResource(value="classpath:hsql_cfg.xml")
public class GUIApplication {

    private static final Logger LOG = LogManager.getLogger(GUIApplication.class);

    public static void main(String[] args) {
        //FirstPreloader.launch(FirstPreloader.class);
        //LauncherImpl.launchApplication(FrontPage.class, FirstPreloader.class, args);
        //FIXME https://github.com/thomasdarimont/spring-labs/blob/master/spring-boot-javafx/src/main/java/demo/App.java
        LOG.info("!!! Entering GUIApplication main !!!");
        Toolkit.getToolkit();

        var context = SpringApplication.run(GUIApplication.class, args);

        LOG.info("!!! FrontPage.launching GUI !!!");
        FrontPage.launchGUI(FrontPage.class, context);

    }

    /**
     * Required because WebSockets initialize its own task scheduler in a different way
     *
     * @return taskScheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}