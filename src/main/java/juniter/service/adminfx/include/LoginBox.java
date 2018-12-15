package juniter.service.adminfx.include;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.crypto.SecretBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class LoginBox extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();



    //                                  LOGIN SECTION
    @FXML private PasswordField salt;

    @FXML private PasswordField password;

    @FXML
    private Label pubkey;



    public LoginBox() { }



    @FXML
    public void login() {
        LOG.info("Login ... ");
        var secretBox = new SecretBox(salt.getText(), password.getText());
        pubkey.setText(secretBox.getPublicKey());
    }


    @Override
    public void start(Stage primaryStage) {


        BorderPane page = (BorderPane) load("/adminfx/include/LoginBox.fxml");

        Scene scene = new Scene(page);
        scene.getStylesheets().add("/adminfx/css/search.css");


        primaryStage.setTitle("Juniter - Login ");
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(LoginBox.class.getResourceAsStream("/adminfx/images/logo.png")));
        primaryStage.show();
        primaryStage.setOnHidden(e -> Platform.exit());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
