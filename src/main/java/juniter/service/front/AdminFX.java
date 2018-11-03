package juniter.service.front;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.crypto.SecretBox;
import juniter.repository.jpa.BlockRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class AdminFX extends Application implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger();

    @FXML
    private BorderPane content;

    @FXML
    private TextField delOne;

    @FXML
    private TextField delSome;

    @FXML
    private PasswordField salt;

    @FXML
    private PasswordField password;

    @FXML
    private Button login;



    @Autowired
    BlockRepository repository;

    @Async
    @Override
    public void run(String... args) throws Exception {

        LOG.info("Launching FX application");
        launch(args);

        LOG.info("Closed FX application");
    }


    public AdminFX() {

    }

    @FXML
    private void initialize() {

    }

    @FXML
    public void deleteOne() {
        LOG.info("Couldn't delete Block, error parsing blockNumber " + delOne);

        Integer id = Integer.parseInt(delOne.getText());
        if(id !=null){

            LOG.info("deleting Block # " + id);

            repository.block(id).ifPresent(block -> {
                repository.delete(block);
            });
        }else{
            LOG.info("Couldn't delete Block, error parsing blockNumber " + delOne.getText());
        }

    }

    @FXML
    public void deleteSome() {
        String[] ids = delSome.getText().split(",");

        Stream.of(ids) //
                .map(id -> Integer.parseInt(id))//
                .forEach(id -> {
                    LOG.info("deleting Block # " + id);

                    repository.block(id).ifPresent(block -> {
                        repository.delete(block);
                    });
                });


    }

    @FXML
    public void login() {
        LOG.info("login "+ salt + " "+ login);

        boolean result = ConfirmBox.display("Login", "pk : "  );
        LOG.info(result);
        var secretBox = new SecretBox(salt.getText(), password.getText());
        LOG.info(secretBox.getPublicKey());

    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(AdminFX.class.getResource("/adminfx/AdminFX.fxml"));
        loader.setRoot(content);
        BorderPane page = (BorderPane) loader.load();

        login  = new Button("Login2");
//        salt = new PasswordField();
//        password = new PasswordField();

        login.setOnAction(e -> {

            LOG.info("login "+ salt + " "+ login);

            boolean result = ConfirmBox.display("Title of Window", "Are you sure you want to send that pic?");
            LOG.info(result);
            var secretBox = new SecretBox(salt.getText(), password.getText());
            LOG.info(secretBox.getPublicKey());
        });


        Scene scene = new Scene(page);
        scene.getStylesheets().add("/adminfx/search.css");


        primaryStage.setTitle("Juniter - Admin panel ");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(AdminFX.class.getResourceAsStream("/adminfx/logo.png")));
        primaryStage.show();

    }

    @FXML
    public void openSVGGraph() {
        LOG.info("openSVGGraph ");
        GraphPanel.launch();

    }

}
