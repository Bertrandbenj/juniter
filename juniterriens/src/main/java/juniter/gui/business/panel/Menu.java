package juniter.gui.business.panel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static juniter.gui.JuniterBindings.*;
import static juniter.gui.technical.PageName.*;


/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Menu implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Menu.class);

    private Image DEFAULT_LOGO;
    private Image MAIN_LOGO;
    private Image GRAPH_LOGO;
    private Image NETWORK_LOGO;
    private Image DATABASE_LOGO;
    private Image SPARK_LOGO;
    private Image NOTARY_LOGO;
    private Image SETTINGS_LOGO;
    private Image CURRENCIES_LOGO;
    private Image USER_LOGO;


    @FXML
    private VBox vMenu;

    @FXML
    private ImageView logoMain;

    @FXML
    private ImageView logoNotary;

    @FXML
    private ImageView logoGraphs;

    @FXML
    private ImageView logoNetwork;

    @FXML
    private ImageView logoCurrencies;
    @FXML
    private ImageView logoUser;


    @FXML
    private ImageView logoDatabase;

    @FXML
    private ImageView logoSpark;

    @FXML
    private ImageView logoSettings;


    public Menu() {
    }


    @FXML
    public void viewMain(ActionEvent event) {
        screenController.viewGeneric(MAIN, event);
    }

    @FXML
    public void viewSVGGraph(ActionEvent event) {
        screenController.viewGeneric(GRAPHS, event);
    }

    @FXML
    public void viewNotary(ActionEvent event) {
        screenController.viewGeneric(NOTARY, event);
    }

    @FXML
    public void viewNetwork(ActionEvent event) {
        screenController.viewGeneric(NETWORK, event);
    }

    @FXML
    public void viewCurrencies(ActionEvent event) {
        screenController.viewGeneric(CURRENCIES, event);
    }

    @FXML
    public void viewDatabase(ActionEvent event) {
        screenController.viewGeneric(DATABASE, event);
    }

    @FXML
    public void viewSpark(ActionEvent event) {

        screenController.viewGeneric(SPARK, event);
    }


    public void viewUser(ActionEvent event) {
        screenController.viewGeneric(USER, event);
    }

    @FXML
    public void viewSettings(ActionEvent event) {
        screenController.viewGeneric(SETTINGS, event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (getClass().getResource("/gui/Spark.fxml") == null) {
            vMenu.getChildren().remove(8); // dirty hack
        }

        DEFAULT_LOGO = new Image("/gui/images/logo.png");
        MAIN_LOGO = new Image("/gui/images/whiterabbit.jpg");
        GRAPH_LOGO = new Image("/gui/images/graph.png");
        NETWORK_LOGO = new Image("/gui/images/network.png");
        DATABASE_LOGO = new Image("/gui/images/database.png");
        SPARK_LOGO = new Image("/gui/images/spark.png");
        NOTARY_LOGO = new Image("/gui/images/keep_calm_im_the_notary_puzzle.jpg");
        SETTINGS_LOGO = new Image("/gui/images/settings.png");
        CURRENCIES_LOGO = new Image("/gui/images/g1.png");
        USER_LOGO = new Image("/gui/images/user_default.png");

        currentPageName.addListener((observable, oldValue, newValue) -> {
            LOG.info("currentPageName " + newValue);

            logoMain.imageProperty().setValue(MAIN.equals(newValue) ? DEFAULT_LOGO: MAIN_LOGO  );
            logoGraphs.imageProperty().setValue(GRAPHS.equals(newValue) ? DEFAULT_LOGO: GRAPH_LOGO );
            logoNotary.imageProperty().setValue(NOTARY.equals(newValue) ? DEFAULT_LOGO: NOTARY_LOGO );
            logoNetwork.setImage(NETWORK.equals(newValue) ? DEFAULT_LOGO: NETWORK_LOGO  );
            logoNetwork.setImage(CURRENCIES.equals(newValue) ? DEFAULT_LOGO: CURRENCIES_LOGO  );
            logoNetwork.setImage(USER.equals(newValue) ? DEFAULT_LOGO: USER_LOGO  );
            logoDatabase.setImage(DATABASE.equals(newValue) ? DEFAULT_LOGO: DATABASE_LOGO );
            logoSpark.setImage(SPARK.equals(newValue) ? DEFAULT_LOGO: SPARK_LOGO );
            logoSettings.setImage(SETTINGS.equals(newValue) ? DEFAULT_LOGO: SETTINGS_LOGO );
        });


    }


}
