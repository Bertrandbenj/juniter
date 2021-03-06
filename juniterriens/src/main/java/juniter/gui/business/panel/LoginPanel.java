package juniter.gui.business.panel;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.SourceInput;
import juniter.gui.game.GameBindings;
import juniter.service.jpa.Index;
import juniter.service.jpa.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.*;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class LoginPanel implements Initializable {

    private static final Logger LOG = LogManager.getLogger(LoginPanel.class);


    //                                  LOGIN SECTION
    @FXML
    private PasswordField salt;

    @FXML
    private PasswordField password;

    @FXML
    private Label pubkey;


    @Autowired
    private Index index;


    @Autowired
    private TransactionService txService;


    public LoginPanel() {
    }


    @FXML
    public void login() {
        LOG.info("Login ... ");
        var sb = new SecretBox(salt.getText(), password.getText());
        pubkey.setText(sb.getPublicKey());
        secretBox.set(sb);

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws InterruptedException {

                updateMessage("Loading certificates ");
                certsRelated.addAll(index.getC(sb.getPublicKey(), sb.getPublicKey()).collect(Collectors.toList()));


                updateMessage("Loading Transactions ");
                var t1 = txService.transactionsOfIssuer(sb.getPublicKey());
                var t2 = txService.transactionsOfReceiver(sb.getPublicKey());
                txRelated.addAll(t1);
                txRelated.addAll(t2);

                updateMessage("Loading Sources ");
                var ss = index.getSRepo().sourcesOfPubkeyL(sb.getPublicKey()).stream()
                        //.peek(x -> LOG.info("found SINDEX " + x))
                        .sorted(Comparator.comparingInt(SINDEX::getAmount))
                        .map(s -> new SourceInput(s.getAmount() + ":" + s.getBase() + ":" + s.type() + ":" + s.getIdentifier() + ":" + s.getPos()))
                        .collect(Collectors.toList());
                sources.addAll(ss);


                updateMessage("Loading account");
                playing.set(true);
                GameBindings.money.setValue(index.getAccountRepo().accountOf(sb.getPublicKey()).getBSum());

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> salt.requestFocus());
    }
}
