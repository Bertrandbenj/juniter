package juniter.juniterriens.include;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private static final Logger LOG = LogManager.getLogger();

    private final String CSS_FOLDER = "/juniterriens/css/";


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

    public Image DEFAULT_LOGO, MAIN_LOGO, GRAPH_LOGO, NETWORK_LOGO, DATABASE_LOGO, SPARK_LOGO, NOTARY_LOGO;

    public Menu() {
    }


    @FXML
    public void viewMain(ActionEvent event) {
        viewGeneric("Main", "/juniterriens/FrontPage.fxml", event);
    }

    @FXML
    public void viewSVGGraph(ActionEvent event) {
        viewGeneric("Graphs", "/juniterriens/GraphPanel.fxml", event);
    }

    @FXML
    public void viewNotary(ActionEvent event) {
        viewGeneric("Notary", "/juniterriens/Notary.fxml", event);
    }

    @FXML
    public void viewNetwork(ActionEvent event) {
        viewGeneric("Network", "/juniterriens/Network.fxml", event);
    }

    @FXML
    public void viewDatabase(ActionEvent event) {
        viewGeneric("Database", "/juniterriens/Database.fxml", event);
    }

    @FXML
    public void viewSpark(ActionEvent event) {
        viewGeneric("Spark", "/juniterriens/Spark.fxml", event);
    }





    @Override
    public void start(Stage primaryStage) {
//        AnchorPane page = (AnchorPane) load("/juniterriens/include/Menu.fxml");
//        Scene scene = new Scene(page);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // LOG.info(getClass().getResource("/juniterriens/Spark.fxml") + "  " +  vMenu.getChildren().size() );
        if (getClass().getResource("/juniterriens/Spark.fxml") == null) {
            var removed = vMenu.getChildren().remove(6);
        }
        vMenu.getChildren().addAll(translateCB(),themeCB());
        // vMenu.getChildren().forEach(LOG::info);
        DEFAULT_LOGO = new Image("/juniterriens/images/logo.png");

        MAIN_LOGO = new Image("/juniterriens/images/whiterabbit.jpg");
        GRAPH_LOGO = new Image("/juniterriens/images/dotex.png");
        NETWORK_LOGO = new Image("/juniterriens/images/network.png");
        DATABASE_LOGO = new Image("/juniterriens/images/database.png");
        SPARK_LOGO = new Image("/juniterriens/images/spark.png");
        NOTARY_LOGO = new Image("/juniterriens/images/keep_calm_im_the_notary_puzzle.jpg");


        LANG_COMBO.setOnAction(ev -> {
            I18N.setLocale(LANG_COMBO.getSelectionModel().getSelectedItem());
            viewGeneric("Main", "/juniterriens/FrontPage.fxml" ,  (Stage) vMenu.getScene().getWindow());
        });

     }

    private void viewGeneric(String name, String fxml, Stage current) {
        BorderPane page = (BorderPane) load(fxml );
        page.setPrefSize(current.getScene().getWidth(), current.getScene().getHeight());

        Scene scene = new Scene(page);
        current.setTitle("Juniter - " + name);
        scene.getStylesheets().setAll(Bindings.selectedTheme.getValue());

        logoMain.setImage("Main".equals(name) ? MAIN_LOGO : DEFAULT_LOGO);
        logoGraphs.setImage("Graphs".equals(name) ? GRAPH_LOGO : DEFAULT_LOGO);
        logoNotary.setImage("Notary".equals(name) ? NOTARY_LOGO : DEFAULT_LOGO);
        logoNetwork.setImage("Network".equals(name) ? NETWORK_LOGO : DEFAULT_LOGO);
        logoDatabase.setImage("Database".equals(name) ? DATABASE_LOGO : DEFAULT_LOGO);
        logoSpark.setImage("Spark".equals(name) ? SPARK_LOGO : DEFAULT_LOGO);



        current.setScene(scene);
        current.show();
    }

    private void viewGeneric(String name, String fxml, ActionEvent event) {
        LOG.info(" view " + name + " - " + event.getEventType());
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();

        viewGeneric(name,fxml,current);
    }


    private ComboBox<Locale> LANG_COMBO  ;

    private ComboBox<Locale> translateCB() {
        LANG_COMBO = new ComboBox<>();
        ObservableList<Locale> options = FXCollections.observableArrayList(I18N.getSupportedLocales());
        LANG_COMBO.setItems(options);
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

        return LANG_COMBO;
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



    private ComboBox<String> themeCB() {
        ComboBox<String> comboBox = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList(CSS_FOLDER + "dark-theme.css", CSS_FOLDER + "no-theme.css");
        comboBox.setItems(options);
        comboBox.setMaxWidth(100);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                var split = object.split("/");
                var file = split[split.length-1];
                return file.replaceAll(".css", "");
            }

            @Override
            public String fromString(String string) {
                return null;
            }
        });

        comboBox.getSelectionModel().select(Bindings.selectedTheme.getValue());

        comboBox.setOnAction(event ->  {
           var theme =  comboBox.getSelectionModel().getSelectedItem();
            Bindings.selectedTheme.setValue(theme);
            comboBox.getScene().getStylesheets().setAll(theme);
        });
        return comboBox;
    }



}
