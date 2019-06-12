package juniter.gui.include;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxType;
import juniter.gui.game.GameBindings;
import juniter.repository.jpa.block.TxRepository;
import juniter.repository.jpa.index.AccountRepository;
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
    private CINDEXRepository cRepo;


    @Autowired
    private TxRepository txRepo;


    @Autowired
    private SINDEXRepository sRepo;


    @Autowired
    private AccountRepository accountRepository;

    public LoginBox() {
    }


    @FXML
    public void login() {
        LOG.info("Login ... ");
        var sb = new SecretBox(salt.getText(), password.getText());
        pubkey.setText(sb.getPublicKey());
        JuniterBindings.secretBox.set(sb);

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() throws InterruptedException{

                updateMessage("Loading certificates ");
                var c1 = cRepo.receivedBy(sb.getPublicKey());
                var c2 = cRepo.issuedBy(sb.getPublicKey());
                JuniterBindings.certsRelated.addAll(c1);
                JuniterBindings.certsRelated.addAll(c2);


                updateMessage("Loading Transactions ");
                var t1 = txRepo.transactionsOfIssuer_(sb.getPublicKey());
                var t2 = txRepo.transactionsOfReceiver_(sb.getPublicKey());
                JuniterBindings.txRelated.addAll(t1);
                JuniterBindings.txRelated.addAll(t2);

                updateMessage("Loading Sources ");
                var ss = sRepo.sourcesOfPubkeyL(sb.getPublicKey()).stream()
                        //.peek(x -> LOG.info("found SINDEX " + x))
                        .sorted(Comparator.comparingInt(SINDEX::getAmount))
                        .map(s -> new TxInput(s.getAmount() + ":" + s.getBase() + ":" + s.type() + ":" + s.getIdentifier() + ":" + s.getPos()))
                        .collect(Collectors.toList());
                JuniterBindings.sources.addAll(ss);


                updateMessage("Loading account");
                JuniterBindings.playing.set(true);
                GameBindings.money.setValue(accountRepository.accountOf(sb.getPublicKey()).getBSum());

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
