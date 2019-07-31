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


/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Menu extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Menu.class);


    public Image DEFAULT_LOGO, MAIN_LOGO, GRAPH_LOGO, NETWORK_LOGO, DATABASE_LOGO, SPARK_LOGO, NOTARY_LOGO;


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
    private ComboBox<Locale> LANG_COMBO;
    @FXML
    private ComboBox<String> THEME_COMBO;


    public Menu() {
    }


    @FXML
    public void viewMain(ActionEvent event) {
        viewGeneric("Main", "/gui/FrontPage.fxml", event);
    }

    @FXML
    public void viewSVGGraph(ActionEvent event) {
        viewGeneric("Graphs", "/gui/GraphPanel.fxml", event);
    }

    @FXML
    public void viewNotary(ActionEvent event) {
        viewGeneric("Notary", "/gui/Notary.fxml", event);
    }

    @FXML
    public void viewNetwork(ActionEvent event) {
        viewGeneric("Network", "/gui/Network.fxml", event);
    }

    @FXML
    public void viewDatabase(ActionEvent event) {
        viewGeneric("Database", "/gui/Database.fxml", event);
    }

    @FXML
    public void viewSpark(ActionEvent event) {
        viewGeneric("Spark", "/gui/Spark.fxml", event);
    }


    @Override
    public void start(Stage primaryStage) {
//        AnchorPane page = (AnchorPane) load("/gui/include/Menu.fxml");
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

        // ============ SET LANG COMBO =============
        LANG_COMBO.setItems(JuniterBindings.langs);
        LANG_COMBO.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayLanguage();
            }

            @Override
            public Locale fromString(String string) {
                return null;
            }
        });
        LANG_COMBO.setCellFactory(param -> new LanguageListCell());
        LANG_COMBO.getSelectionModel().select(Locale.getDefault());

        LANG_COMBO.setOnAction(ev -> {
            LOG.info("LANG_COMBO.setOnAction");

            I18N.setLocale(LANG_COMBO.getSelectionModel().getSelectedItem());

//            JuniterBindings.screenController.getScreenMap().values().forEach(x-> x.requestLayout());

            //JuniterBindings.screenController.removeScreens();

            //viewMain(ev);
            try {
                //LANG_COMBO.getScene().init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        // ============ SET THEME COMBO =============
        THEME_COMBO.setItems(JuniterBindings.themes);
        THEME_COMBO.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                var split = object.split("/");
                var file = split[split.length - 1];
                return file.replaceAll(".css", "");
            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });

        THEME_COMBO.getSelectionModel().select(JuniterBindings.selectedTheme.getValue());

        THEME_COMBO.setOnAction(event -> {
            LOG.info("THEME_COMBO.setOnAction");
            var theme = THEME_COMBO.getSelectionModel().getSelectedItem();
            JuniterBindings.selectedTheme.setValue(theme);
            JuniterBindings.screenController.getMain().getStylesheets().setAll(JuniterBindings.JMetroBase, theme);
            //new JMetro(JMetro.Style.LIGHT).applyTheme(JuniterBindings.screenController.getMain());
            LOG.info("THEME_COMBO.setOnAction " + theme);



        });

        preload();


        //viewGeneric("Main", "/gui/FrontPage.fxml", (Stage) THEME_COMBO.getScene().getWindow());


    }

    private void preload() {
        // Preload
        Platform.runLater(() -> {

            try {
                Thread.sleep(2000);
                Scene sc = THEME_COMBO.getScene();
                if (sc == null)
                    sc = JuniterBindings.screenController.getMain();
                Stage s = (Stage) sc.getWindow();

//            viewGeneric("Graphs", "/gui/GraphPanel.fxml", s);
//            viewGeneric("Notary", "/gui/Notary.fxml", s);
//            viewGeneric("Network", "/gui/Network.fxml", s);
//            viewGeneric("Database", "/gui/Database.fxml", s);
//            if (getClass().getResource("/gui/Spark.fxml") != null) {
//                viewGeneric("Spark", "/gui/Spark.fxml", s);
//            }

                viewGeneric("Main", "/gui/FrontPage.fxml", s);

            } catch (Exception e) {
                LOG.error("error", e);
            }
        });
    }

    private void viewGeneric(String name, String fxml, Stage current) {

        Scene scene;


        if (JuniterBindings.screenController.hasScreen(name)) {
            JuniterBindings.screenController.activate(name);
        } else {
            BorderPane page = (BorderPane) load(fxml);
            page.setPrefSize(current.getScene().getWidth(), current.getScene().getHeight());

            JuniterBindings.screenController.addScreen(name, page);
            JuniterBindings.screenController.setMain(current.getScene());
            JuniterBindings.screenController.activate(name);

        }

        scene = JuniterBindings.screenController.getMain();
        scene.getStylesheets().setAll(JuniterBindings.JMetroBase, JuniterBindings.selectedTheme.getValue());
        // new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

        logoMain.setImage("Main".equals(name) ? MAIN_LOGO : DEFAULT_LOGO);
        logoGraphs.setImage("Graphs".equals(name) ? GRAPH_LOGO : DEFAULT_LOGO);
        logoNotary.setImage("Notary".equals(name) ? NOTARY_LOGO : DEFAULT_LOGO);
        logoNetwork.setImage("Network".equals(name) ? NETWORK_LOGO : DEFAULT_LOGO);
        logoDatabase.setImage("Database".equals(name) ? DATABASE_LOGO : DEFAULT_LOGO);
        logoSpark.setImage("Spark".equals(name) ? SPARK_LOGO : DEFAULT_LOGO);


        current.setOnHidden(e -> Platform.exit());
        current.setOnCloseRequest(t -> System.exit(0));

        current.getIcons().add(new Image("/gui/images/logo.png"));
        current.setTitle("Juniter - " + name);
//        if(current.getScene()!=null)current.getScene().disposePeer();
        current.setScene(scene);
        current.show();
    }

    private void viewGeneric(String name, String fxml, ActionEvent event) {
        LOG.info(" view " + name + " - " + event.getEventType());
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        ((Node) event.getSource()).getScene().disposePeer();
        viewGeneric(name, fxml, current);
    }


    class LanguageListCell extends ListCell<Locale> {
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getDisplayLanguage());
            }
        }
    }


}
