package juniter.gui.technical;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static juniter.gui.technical.Theme.JMetroBase;
import static juniter.gui.JuniterBindings.screenController;
import static juniter.gui.JuniterBindings.selectedTheme;

@Getter
@Setter
public class ScreenController {
    private static final Logger LOG = LogManager.getLogger(ScreenController.class);

    public static ScreenController singleton = new ScreenController();

    public static final Map<PageName, String> map = Stream.of(new Object[][]{
            {PageName.MAIN, "/gui/page/FrontPage.fxml"},
            {PageName.GRAPHS, "/gui/page/GraphPanel.fxml"},
            {PageName.NOTARY, "/gui/page/Notary.fxml"},
            {PageName.NETWORK, "/gui/page/Network.fxml"},
            {PageName.CURRENCIES, "/gui/page/Currencies.fxml"},
            {PageName.SETTINGS, "/gui/page/Settings.fxml"},
            {PageName.SPARK, "/gui/Spark.fxml"},
            {PageName.DATABASE, "/gui/page/Database.fxml"},
            {PageName.USER, "/gui/page/User.fxml"}

    }).collect(Collectors.toMap(data -> (PageName) data[0], data -> (String) data[1]));


    private HashMap<PageName, Pane> screenMap = new HashMap<>();

    private Scene main;

    private ObjectProperty<PageName> currentPageName = new SimpleObjectProperty<>(PageName.MAIN);

    private ScreenController() {

    }

    public void addScreen(PageName name, Pane pane) {
        screenMap.put(name, pane);
        LOG.info("addScreen " + name + "  " + pane + " " + main);

    }

    public void activate(PageName name) {
        main.setRoot(screenMap.get(name));
    }

    private boolean hasScreen(PageName name) {
        return screenMap.containsKey(name) && getClass().getResource(map.get(name)) != null;
    }

    private Object load(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url),
                    ResourceBundle.getBundle("Internationalization", I18N.getLocale()));
            loader.setControllerFactory(aClass -> AbstractJuniterFX.applicationContext.getBean(aClass));


            return loader.load();
        } catch (Exception e) {

            throw new RuntimeException(String.format("Failed to reload FXML file '%s' ", url) + getClass().getResource(url), e);
        }
    }


    private void viewGeneric(PageName name, String fxml, Stage current) {

        Scene scene;
        currentPageName.set(name);

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


        current.setOnHidden(e -> Platform.exit());
        current.setOnCloseRequest(t -> System.exit(0));

        current.getIcons().add(new Image("/gui/images/logo.png"));
        current.setTitle("Juniter - " + name);
//        if(currentChained.getScene()!=null)currentChained.getScene().disposePeer();
        current.setScene(scene);
        current.show();
    }

    public void viewGeneric(PageName name, ActionEvent event) {
        LOG.info(" view " + name + " - " + event.getEventType());
        Stage current = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        ((Node) event.getSource()).getScene().disposePeer();
        viewGeneric(name, map.get(name), current);
    }


    private void preload() {

        LOG.info("preloading screens");
        // Preload
        Platform.runLater(() -> {

            try {
                Stream.of(PageName.values()).filter(p -> !hasScreen(p)).forEach(p -> {
                    screenMap.put(p, (BorderPane) load(map.get(p)));
                });

            } catch (Exception e) {
                LOG.error("error", e);
            }
        });
    }

    public void removeScreens() {
        screenMap.clear();
        preload();
    }
}