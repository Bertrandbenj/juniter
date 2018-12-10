package juniter.service.front;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.crypto.SecretBox;
import juniter.core.validation.StaticValid;
import juniter.repository.jpa.BlockRepository;
import juniter.service.Indexer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class AdminFX extends AbstractJavaFxApplicationSupport implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    @FXML
    private BorderPane content;

    @FXML
    private TextField delOne;

    @FXML
    private TextField delSome;

    @FXML
    private MenuItem menuGraph;


    @FXML
    private TextField tstOne;

    @FXML
    private TextField tstSome;

    @FXML private ImageView logo;


    //                                  LOGIN SECTION
    @FXML private PasswordField salt;

    @FXML private PasswordField password;

    @FXML private Button login;


    //                                  LOADING  SECTION
    @FXML private ProgressBar indexBar;
    @FXML private ProgressIndicator indexIndic;
    public static DoubleProperty indexUpdater = new SimpleDoubleProperty(.0);

    @FXML private ProgressBar loadBar ;
    @FXML private ProgressIndicator loadIndic;
    public static DoubleProperty loadUpdater = new SimpleDoubleProperty(.0);




    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private Indexer indexer;

    @FXML
    private TextField indexTil;


    public AdminFX() { }

    @FXML
    public void indexUntil(){

        indexer.indexUntil(Integer.parseInt(indexTil.getText()));
    }


    @FXML
    public void deleteSome() {
        String[] ids = delSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {
                    LOG.info("deleting Blocks # " + id);

                    blockRepo.block(id).ifPresent(block -> {
                        blockRepo.delete(block);
                    });
                });


    }

    @FXML
    private Label pubkey;

    @FXML
    public void login() {
        var secretBox = new SecretBox(salt.getText(), password.getText());
        pubkey.setText(secretBox.getPublicKey());
    }



    @FXML
    public void testSome() {
        String[] ids = tstSome.getText().split(",");

        Stream.of(ids) //
                .map(Integer::parseInt)//
                .forEach(id -> {
                    LOG.info("testing Blocks # " + id);

                    blockRepo.block(id).ifPresent(block -> {
                        boolean result = false;
                        try {
                            StaticValid.assertBlock(block);

                        } catch (AssertionError ea) {
                            result = ConfirmBox.display("AssertionError", ea.getMessage());
                        }
                        LOG.info("testing Block # " + result);
                    });
                });
        ConfirmBox.display("All good", "repository node is local valid  ");

    }


    @FXML
    public void viewSVGGraph(ActionEvent event) {
        LOG.info("view SVG Graph " + event.getEventType());

        BorderPane page = (BorderPane) load("/adminfx/GraphPanel.fxml");

        Scene scene = new Scene(page);


        Stage stage = (Stage) login.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    public void viewDocs(ActionEvent event) {
        LOG.info("view Docs " + event.getEventType());

        BorderPane page = (BorderPane) load("/adminfx/DUPForm.fxml");

        Scene scene = new Scene(page);


        Stage stage = (Stage) login.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void start(Stage primaryStage) {

        if (null == blockRepo) {
            throw new IllegalStateException("BlockRepository was not injected properly");
        }


        BorderPane page = (BorderPane) load("/adminfx/AdminFX.fxml");

        Scene scene = new Scene(page);
        scene.getStylesheets().add("/adminfx/search.css");




        primaryStage.setTitle("Juniter - Admin panel ");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(AdminFX.class.getResourceAsStream("/adminfx/logo.png")));
        primaryStage.show();
        primaryStage.setOnHidden(e -> Platform.exit());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        indexBar.progressProperty().bind(indexUpdater);
        indexIndic.progressProperty().bind(indexUpdater);

        loadBar.progressProperty().bind(loadUpdater);
        loadIndic.progressProperty().bind(loadUpdater);
    }
}
