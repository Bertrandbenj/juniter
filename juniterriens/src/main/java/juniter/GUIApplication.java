package juniter;

import com.sun.javafx.tk.Toolkit;
import juniter.gui.business.page.FrontPage;
import juniter.gui.technical.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


@EnableJpaRepositories(value = "juniter.repository" )
@SpringBootApplication
@EnableAsync
@EnableScheduling
@EntityScan("juniter.core.model")
//https://www.youtube.com/watch?v=RifjriAxbw8 could be better like that
public class GUIApplication {

    private static final Logger LOG = LogManager.getLogger(GUIApplication.class);

    public static void main(String[] args) {
        //java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);


        //FirstPreloader.launch(FirstPreloader.class);
        //LauncherImpl.launchApplication(FrontPage.class, FirstPreloader.class, args);
        //FIXME https://github.com/thomasdarimont/spring-labs/blob/master/spring-boot-javafx/src/main/java/demo/App.java
        LOG.info("!!! Entering GUIApplication main !!!");
        Toolkit.getToolkit();

        var context = SpringApplication.run(GUIApplication.class, args);

        LOG.info("!!! FrontPage.launching GUI !!!");
        AbstractJuniterFX.launchGUI(FrontPage.class, context);




    }

    /**
     * Required because WebSockets initialize its own task scheduler in a different way
     *
     * @return taskScheduler
     */
    @Bean("taskExecutor")
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.initialize();
        return taskScheduler;
    }
}