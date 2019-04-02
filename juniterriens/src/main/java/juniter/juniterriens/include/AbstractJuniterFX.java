package juniter.juniterriens.include;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void init() throws Exception {
        super.init();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //applicationContext.close();
    }

    public static void launchGUI(Class<? extends AbstractJuniterFX> appClass, ConfigurableApplicationContext context) {
        applicationContext = context;
        Application.launch(appClass);
    }


    protected Object load(String url  ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

            loader.setResources(ResourceBundle.getBundle("Internationalization",I18N.getLocale()));

            return  loader.load();
        } catch (Exception e) {

            throw new RuntimeException(String.format("Failed to load FXML file '%s' ", url) + getClass().getResource(url), e);
        }
    }



}