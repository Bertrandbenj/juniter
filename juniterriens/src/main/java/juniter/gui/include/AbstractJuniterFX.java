package juniter.gui.include;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import juniter.GUIApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * This helper allows @Autowired  in GUI's
 */
@Component
public abstract class AbstractJuniterFX extends Application {

    private static ConfigurableApplicationContext applicationContext;

    private static final Logger LOG = LogManager.getLogger(GUIApplication.class);


    @Override
    public void init() throws Exception {
        super.init();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }



    @Override
    public void stop() throws Exception {
        super.stop();
        LOG.info("Stopping JavaFX & Java");
        applicationContext.close();
        Platform.exit();
        System.exit(0);
    }

    public static void launchGUI(Class<? extends AbstractJuniterFX> appClass, ConfigurableApplicationContext context) {
        applicationContext = context;
        Application.launch(appClass);
    }


    protected Object load(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url),
                    ResourceBundle.getBundle("Internationalization", I18N.getLocale()));
            loader.setControllerFactory(aClass -> applicationContext.getBean(aClass));


            return loader.load();
        } catch (Exception e) {

            throw new RuntimeException(String.format("Failed to load FXML file '%s' ", url) + getClass().getResource(url), e);
        }
    }


}