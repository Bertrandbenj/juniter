package juniter.gui.business.page;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import juniter.core.crypto.SecretBox;
import juniter.core.event.ServerLogin;
import juniter.core.validation.BlockLocalValid;
import juniter.gui.JuniterBindings;
import juniter.gui.business.popup.AlertBox;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.I18N;
import juniter.gui.technical.Theme;
import juniter.service.core.BlockService;
import juniter.service.ipfs.Interplanetary;
import juniter.user.UnitDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.beans.EventHandler;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import static juniter.gui.JuniterBindings.*;
import static juniter.gui.technical.Theme.JMetroBase;


@Component
public class Settings extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Settings.class);
    public Label pk;
    @FXML
    private TextField delSome;
    @FXML
    private CheckBox delLowerThan;
    @FXML
    private CheckBox delHigherThan;
    @FXML
    private CheckBox testLowerThan;
    @FXML
    private TextField tstSome;
    @FXML
    private CheckBox testHigherThan;
    @FXML
    private ListView<String> bookmarkList;
    @FXML
    private ListView<String> walletList;

    @Value("${juniter.forkSize:100}")
    private Integer forkSize;


    @FXML
    private PasswordField salt;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField forksize;
    @FXML
    private ComboBox<Locale> langCB;
    @FXML
    private ComboBox<UnitDisplay> unitCB;
    @FXML
    private ComboBox<Theme> themeCB;


    @Autowired
    private BlockService blockService;

    @Autowired
    private Optional<Interplanetary> interplanetary;

    @Autowired
    private ApplicationEventPublisher coreEvents;


    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Settings");
        primaryStage.setTitle("Juniter - " + I18N.get("settings"));
        primaryStage.show();
    }

    public class LanguageListCell extends ListCell<Locale> {
        @Override
        protected void updateItem(Locale item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getDisplayLanguage());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forksize.setText(forkSize.toString());

        //secretBox.set(userSettings.getNodeKey());

        // ============ SET LANG COMBO =============
        langCB.setItems(langs);
        langCB.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayLanguage();
            }

            @Override
            public Locale fromString(String string) {
                return null;
            }
        });
        langCB.setCellFactory(param -> new LanguageListCell());
        langCB.getSelectionModel().select(Locale.getDefault());

        langCB.setOnAction(ev -> {
            LOG.info("LANG_COMBO.setOnAction " + langCB.getSelectionModel().getSelectedItem());

            I18N.setLocale(langCB.getSelectionModel().getSelectedItem());

            JuniterBindings.screenController.removeScreens();

        });


        // ============ SET THEME COMBO =============
        themeCB.setItems(themes);
//        themeCB.setConverter(new StringConverter<>() {
//            @Override
//            public String toString(String object) {
//                var split = object.split("/");
//                var file = split[split.length - 1];
//                return file.replaceAll(".css", "");
//            }
//
//            @Override
//            public String fromString(String string) {
//                return null;
//            }
//        });

        themeCB.getSelectionModel().select(selectedTheme.getValue());

        themeCB.setOnAction(event -> {
            var theme = themeCB.getSelectionModel().getSelectedItem();
            LOG.info("THEME_COMBO.setOnAction " + theme);
            selectedTheme.setValue(theme);
            screenController.getMain().getStylesheets().setAll(JMetroBase.getTheme(), theme.getTheme());
        });

        unitCB.setItems(FXCollections.observableArrayList(UnitDisplay.values()));


        walletList.setItems(FXCollections.observableArrayList("3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa", "TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS"));

        bookmarkList.setItems(FXCollections.observableArrayList(
                "https://duniter.normandie-libre.fr/wotmap/",
                "https://g1.le-sou.org/#/app/currency/lg",
                "https://g1-monit.librelois.fr/",
                "https://www.gchange.fr/#/app/market/lg"));


        salt.setOnAction(ev -> updateKey());
        pass.setOnAction(ev -> updateKey());

    }

    private void updateKey(){
        var sb = new SecretBox(salt.getText(),pass.getText());
        coreEvents.publishEvent(new ServerLogin(sb));
        pk.setText(sb.getPublicKey());
    }

    @FXML
    public void deleteSome() {
        String[] ids = delSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {
                    LOG.info("deleting Blocks # " + id);

                    blockService.blocks(id).forEach(block -> blockService.delete(block));
                });
    }


    @FXML
    public void ipfs() {

        Platform.runLater(() ->
                interplanetary.ifPresent(Interplanetary::dumpChain)
        );


    }


    @FXML
    public void testSome() {
        String[] ids = tstSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {

                    blockService.block(id).ifPresent(block -> {
                        boolean result = false;
                        try {
                            BlockLocalValid.Static.assertBlock(block);

                        } catch (AssertionError ea) {
                            LOG.warn("Assertion error ", ea);
                            result = AlertBox.display("testing Blocks #"+id, "AssertionError " + ea.getMessage());
                        }
                     });
                });

    }

}
