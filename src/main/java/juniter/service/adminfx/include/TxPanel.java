package juniter.service.adminfx.include;

import com.google.common.collect.Lists;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.Hash;
import juniter.core.model.Pubkey;
import juniter.core.model.Signature;
import juniter.core.model.tx.*;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import juniter.service.adminfx.DUPNotary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class TxPanel  implements Initializable {

    private static final Logger LOG = LogManager.getLogger();
    @FXML
    private TextField pkDest;
    @FXML
    private TextField amountDest;
    @FXML
    private ComboBox issUnit;
    @FXML
    private TextField issSalt;
    @FXML
    private TextField issAmount;
    @FXML
    private TextField issPass;
    @FXML
    private TextField fieldVersion;
    @FXML
    private TextField fieldCurrency;
    @FXML
    private TextField fieldBlockstamp;
    @FXML
    private TextField fieldComment;
    @FXML
    private TextField fieldLocktime;
    @FXML
    private VBox issuerContainer;
    @FXML
    private VBox inputContainer;
    @FXML
    private VBox outputContainer;
    @FXML
    private VBox unlockContainer;
    @FXML
    private VBox signatureContainer;

    private List<TxInput> inputs = Lists.newArrayList();
    private List<TxOutput> outputs = Lists.newArrayList();
    private List<TxUnlock> unlocks = Lists.newArrayList();
    private Map<SecretBox, Integer> issuersInputs = new HashMap<>();


    private Transaction tx;

    @Autowired BlockRepository blockRepo;

    @Autowired SINDEXRepository txRepo;


    public TxPanel() {
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
       blockRepo.current().ifPresent(b-> fieldBlockstamp.setText(b.bstamp()));
    }

    @FXML
    public void addIssuers(ActionEvent event) {
        try {
            issuersInputs.put(
                    new SecretBox(issSalt.getText(), issPass.getText()),
                    Integer.parseInt(issAmount.getText()));
            issSalt.setText("");
            issPass.setText("");
            issAmount.setText("");

        } catch (Exception e) {
            LOG.warn("", e);
        }
        refresh();
    }




    @FXML
    public void refresh() {

        blockRepo.current().ifPresent(b-> {

            tx = new Transaction(
                    Integer.parseInt(fieldVersion.getText()),
                    fieldCurrency.getText(),
                    Integer.parseInt(fieldLocktime.getText()),
                    "",
                    b.bStamp(),
                    b.getTime().intValue(),
                    issuersInputs.keySet().stream().map(SecretBox::getPublicKey).map(Pubkey::new).collect(Collectors.toList()),
                    inputs,
                    outputs,
                    unlocks,
                    null,
                    fieldComment.getText());


            tx.setSignatures(issuersInputs.entrySet().stream()
                    .map(sb -> sb.getKey().sign(tx.toDUPdoc(false)))
                    .map(Signature::new)
                    .collect(Collectors.toList()));
        });




        issuerContainer.getChildren().clear();
        signatureContainer.getChildren().clear();
        issuersInputs.forEach((sb, amount) -> {


            var rem = new Button("-");
            rem.setOnAction(e -> {
                issuersInputs.remove(sb);
                refresh();
            });
            var pk = new Label(sb.getPublicKey());
            pk.setStyle("-fx-font: 16 monospaced;");
            var amountL = new Label("" + amount);
            var issCtrl = new HBox();
            issCtrl.setSpacing(20);
            issCtrl.getChildren().addAll(pk, amountL, rem);
            issuerContainer.getChildren().add(issCtrl);

            var sign = new Label(sb.sign(tx.toDUPdoc(false)));
            sign.setStyle("-fx-font: 10 monospaced;");
            signatureContainer.getChildren().add(sign);
        });

        outputContainer.getChildren().clear();
        outputContainer.getChildren().addAll(
                outputs.stream()
                        .map(o -> {
                            var rem = new Button("-");
                            rem.setOnAction(e -> {
                                outputs.remove(o);
                                refresh();
                            });
                            var lab = new Label(o.toDUP());
                            var outCtrl = new HBox();
                            outCtrl.setSpacing(20);
                            outCtrl.getChildren().addAll(lab, rem);
                            return outCtrl;
                        })
                        .collect(Collectors.toList())
        );


        DUPNotary.raw.setValue(tx.toDUPdoc(true));

    }


    @FXML
    public void addOutputs(ActionEvent actionEvent) {
        var out = new TxOutput();
        out.setAmount(Integer.parseInt(amountDest.getText()));
        out.setBase(0);
        out.setCondition("SIG(" + pkDest.getText() + ")");
        outputs.add(out);
        refresh();
    }

    @FXML
    public void refreshUnlocks(ActionEvent actionEvent) {
        unlockContainer.getChildren().clear();
    }


    @FXML
    public void refreshInputs(ActionEvent actionEvent) {
        inputContainer.getChildren().clear();

        issuersInputs.forEach((sb, amount) -> {
            var sources = txRepo.sourcesOfPubkey(sb.getPublicKey())
                    .map(s -> new TxInput(amount, 0, TxType.D, new Pubkey(sb.getPublicKey()), 0, new Hash(""), 0))
                    //.collect(Collectors.toList())
                    ;
            inputContainer.getChildren().addAll(
                    sources.map(s -> new Label(s.toDUP())).collect(Collectors.toList()));
        });


    }
}
