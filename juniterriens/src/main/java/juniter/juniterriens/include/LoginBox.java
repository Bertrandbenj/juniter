package juniter.juniterriens.include;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxType;
import juniter.repository.jpa.block.TxRepository;
import juniter.repository.jpa.index.CINDEXRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class LoginBox implements Initializable {

    private static final Logger LOG = LogManager.getLogger(LoginBox.class);


    //                                  LOGIN SECTION
    @FXML
    private PasswordField salt;

    @FXML
    private PasswordField password;

    @FXML
    private Label pubkey;


    @Autowired
    CINDEXRepository cRepo;


    @Autowired
    TxRepository txRepo;


    @Autowired
    SINDEXRepository sRepo;

    public LoginBox() {
    }


    @FXML
    public void login() {
        LOG.info("Login ... ");
        JuniterBindings.secretBox.set(new SecretBox(salt.getText(), password.getText()));


        JuniterBindings.secretBox.addListener((observable, oldValue, newValue) -> {
            var c1 = cRepo.receivedBy(newValue.getPublicKey());
            var c2 = cRepo.issuedBy(newValue.getPublicKey());
            JuniterBindings.certsRelated.addAll(c1);
            JuniterBindings.certsRelated.addAll(c2);

            var t1 = txRepo.transactionsOfIssuer_(newValue.getPublicKey());
            var t2 = txRepo.transactionsOfReceiver_(newValue.getPublicKey());
            //JuniterBindings.txRelated.addAll(t1);
//            JuniterBindings.txRelated.addAll(t2);
            var ss = sRepo.sourcesOfPubkeyL(newValue.getPublicKey()).stream()
                    .sorted(Comparator.comparingInt(SINDEX::getAmount))
                    .map(s -> new TxInput(s.getAmount() + ":" + s.getBase() + ":" + TxType.D + ":" + s.getIdentifier() + ":" + s.getPos()))
                    .peek(x -> LOG.info("found TXInput " + x))
                    .collect(Collectors.toList());
            JuniterBindings.sources.addAll(ss);

        });


    }

//
//    @Override
//    public void start(Stage primaryStage) {
//
//
//        BorderPane page = (BorderPane) load("juniterriens/include/LoginBox.fxml");
//
//        Scene scene = new Scene(page);
//        scene.getStylesheets().add("juniterriens/css/search.css");
//
//
//        primaryStage.setTitle("Juniter - Login ");
//        primaryStage.setScene(scene);
//
//        primaryStage.getIcons().add(new Image(LoginBox.class.getResourceAsStream("juniterriens/images/logo.png")));
//        primaryStage.show();
//        primaryStage.setOnHidden(e -> Platform.exit());
//
//    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pubkey.textProperty().bind(Bindings.createObjectBinding(() ->
                JuniterBindings.secretBox.getValue().getPublicKey()));


    }
}
