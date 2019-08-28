package juniter.gui.include;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import static juniter.gui.include.JuniterBindings.*;
import static juniter.gui.include.JuniterBindings.Theme.JMetroBase;
import static juniter.gui.include.ScreenController.*;


/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Menu extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Menu.class);

    private Image DEFAULT_LOGO, MAIN_LOGO, GRAPH_LOGO, NETWORK_LOGO, DATABASE_LOGO, SPARK_LOGO, NOTARY_LOGO, SETTINGS_LOGO;


    @FXML
    public VBox vMenu;
    @FXML
    private ImageView logoMain;
    @FXML
    private ImageView logoNotary;
    @FXML
    private ImageView logoGraphs;
    @FXML
    private ImageView logoNetwork;
    @FXML
    private ImageView logoDatabase;
    @FXML
    private ImageView logoSpark;
    @FXML
    public ImageView logoSettings;


    public Menu() {
    }


    @FXML
    public void viewMain(ActionEvent event) {
        viewGeneric(PanelName.MAIN, "/gui/FrontPage.fxml", event);
    }

    @FXML
    public void viewSVGGraph(ActionEvent event) {
        viewGeneric(PanelName.GRAPHS, "/gui/GraphPanel.fxml", event);
    }

    @FXML
    public void viewNotary(ActionEvent event) {
        viewGeneric(PanelName.NOTARY, "/gui/Notary.fxml", event);
    }

    @FXML
    public void viewNetwork(ActionEvent event) {
        viewGeneric(PanelName.NETWORK, "/gui/Network.fxml", event);
    }

    @FXML
    public void viewDatabase(ActionEvent event) {
        viewGeneric(PanelName.DATABASE, "/gui/Database.fxml", event);
    }

    @FXML
    public void viewSpark(ActionEvent event) {
        viewGeneric(PanelName.SPARK, "/gui/Spark.fxml", event);
    }
    @FXML
    public void viewSettings(ActionEvent event) {
        viewGeneric(PanelName.SETTINGS, "/gui/Settings.fxml", event);
    }


    @Override
    public void start(Stage primaryStage) {
//        AnchorPane page = (AnchorPane) reload("/gui/include/Menu.fxml");
//        Scene scene = new Scene(page);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (getClass().getResource("/gui/Spark.fxml") == null) {
            vMenu.getChildren().remove(6); // dirty hack
        }

        DEFAULT_LOGO = new Image("/gui/images/logo.png");
        MAIN_LOGO = new Image("/gui/images/whiterabbit.jpg");
        GRAPH_LOGO = new Image("/gui/images/dotex.png");
        NETWORK_LOGO = new Image("/gui/images/network.png");
        DATABASE_LOGO = new Image("/gui/images/database.png");
        SPARK_LOGO = new Image("/gui/images/spark.png");
        NOTARY_LOGO = new Image("/gui/images/keep_calm_im_the_notary_puzzle.jpg");
        SETTINGS_LOGO=new Image("/gui/images/settings.png");

        preload();

    }

    private void preload() {
        // Preload
        Platform.runLater(() -> {

            try {
                Thread.sleep(2000);
                Scene sc = vMenu.getScene();
                if (sc == null)
                    sc = screenController.getMain();
                Stage s = (Stage) sc.getWindow();


                viewGeneric(PanelName.MAIN, "/gui/FrontPage.fxml", s);

            } catch (Exception e) {
                LOG.error("error", e);
            }
        });
    }

    private void viewGeneric(PanelName name, String fxml, Stage current) {

        Scene scene;


        if (screenController.hasScreen(name)) {
            screenController.activate(name);
        } else {
            BorderPane page = (BorderPane) load(fxml);
            page.setPrefSize(current.getScene().getWidth(), current.getScene().getHeight());

            screenController.addScreen(name, page);
            screenController.setMain(current.getScene());
            screenController.activate(name);

        }

        scene = screenController.getMain();
        scene.getStylesheets().setAll(JMetroBase.getTheme(), selectedTheme.getValue().getTheme());
        // new JMetro(JMetro.Style.LIGHT).applyTheme(scene);



        logoMain.setImage(PanelName.MAIN.equals(name) ? MAIN_LOGO : DEFAULT_LOGO);
        logoGraphs.setImage(PanelName.GRAPHS.equals(name) ? GRAPH_LOGO : DEFAULT_LOGO);
        logoNotary.setImage(PanelName.NOTARY.equals(name) ? NOTARY_LOGO : DEFAULT_LOGO);
        logoNetwork.setImage(PanelName.NETWORK.equals(name) ? NETWORK_LOGO : DEFAULT_LOGO);
        logoDatabase.setImage(PanelName.DATABASE.equals(name) ? DATABASE_LOGO : DEFAULT_LOGO);
        logoSpark.setImage(PanelName.SPARK.equals(name) ? SPARK_LOGO : DEFAULT_LOGO);
        logoSettings.setImage(PanelName.SETTINGS.equals(name) ? SETTINGS_LOGO : DEFAULT_LOGO);


        current.setOnHidden(e -> Platform.exit());
        current.setOnCloseRequest(t -> System.exit(0));

        current.getIcons().add(new Image("/gui/images/logo.png"));
        current.setTitle("Juniter - " + name);
//        if(current.getScene()!=null)current.getScene().disposePeer();
        current.setScene(scene);
        current.show();
    }

    private void viewGeneric(PanelName name, String fxml, ActionEvent event) {
        LOG.info(" view " + name + " - " + event.getEventType());
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        ((Node) event.getSource()).getScene().disposePeer();
        viewGeneric(name, fxml, current);
    }





}
