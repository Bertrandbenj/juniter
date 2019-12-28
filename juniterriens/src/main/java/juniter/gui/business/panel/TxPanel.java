package juniter.gui.business.panel;

import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxOutput;
import juniter.core.model.dbo.tx.TxUnlock;
import juniter.service.core.Index;
import juniter.user.UnitDisplay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.*;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class TxPanel implements Initializable {

    private static final Logger LOG = LogManager.getLogger(TxPanel.class);
    public ScrollPane pane;
    public TitledPane sourcePane;

    @FXML
    private TextField pkDest;
    @FXML
    private TextField amountDest;
    @FXML
    private ComboBox<UnitDisplay> issUnit;

    private static ObservableList<UnitDisplay> unitsList = FXCollections.observableArrayList(UnitDisplay.values());

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

    @FXML
    private VBox sourcesActionCol;

    private List<TxInput> inputs = Lists.newArrayList();

    private List<TxOutput> outputs = Lists.newArrayList();

    private List<TxUnlock> unlocks = Lists.newArrayList();

    private List<Pair<SecretBox, Integer>> issuersInputs = Lists.newArrayList();
    private Transaction tx;

    @Autowired
    private Index index;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fieldVersion.editableProperty().bind(advancedUser);
        fieldCurrency.editableProperty().bind(advancedUser);
        fieldBlockstamp.editableProperty().bind(advancedUser);


        fieldBlockstamp.setText(currenBlock.get().bstamp());
        issUnit.setItems(unitsList);
        issUnit.getSelectionModel().selectLast();
        sourcePane.setExpanded(advancedUser.not().get());
    }

    @FXML
    public void addIssuers() {
        try {
            issuersInputs.add(new Pair(
                    new SecretBox(issSalt.getText(), issPass.getText()),
                    Integer.parseInt(issAmount.getText())));
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

        Platform.runLater(() -> {
            var b = currenBlock.get();

            tx = new Transaction(null,
                    Integer.parseInt(fieldVersion.getText()),
                    fieldCurrency.getText(),
                    Integer.parseInt(fieldLocktime.getText()),
                    "",
                    b.bStamp(),
                    // b,
                    //b.getTime().intValue(),
                    issuersInputs.stream().map(Pair::getKey).map(SecretBox::getPublicKey).collect(Collectors.toList()),
                    inputs,
                    outputs,
                    unlocks,
                    null,
                    "JT" + fieldComment.getText(),
                    b.bStamp());


            tx.setSignatures(issuersInputs.stream().map(Pair::getKey)
                    .map(sb -> sb.sign(tx.toDUPdoc(false)))
                    .collect(Collectors.toList()));


            issuerContainer.getChildren().clear();
            signatureContainer.getChildren().clear();
            inputContainer.getChildren().clear();
            unlockContainer.getChildren().clear();


            for (int i = 0; i < issuersInputs.size(); i++) {
                SecretBox sb = issuersInputs.get(i).getKey();
                Integer amount = issuersInputs.get(i).getValue();
                var rem = new Button("-");
                rem.setOnAction(e -> {
                    issuersInputs.removeIf(p -> p.getKey().equals(sb));
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
                inputs.clear();
                unlocks.clear();

                AtomicInteger ai = new AtomicInteger(0);

                inputs.addAll(

                        index.getSRepo().availableSourcesOfPub(sb.getPublicKey()).stream()
                                .sorted(Comparator.comparingInt(SINDEX::getAmount))
                                .limit(40)
                                .takeWhile(s -> ai.getAndAdd(s.getAmount()) < amount)
                                //.map(SINDEX::asSourceBMA)
                                .map(s -> new TxInput(s.getAmount() + ":" + s.getBase() + ":" + s.type() + ":" + s.getIdentifier() + ":" + s.getPos()))
                                .collect(Collectors.toList()));

                LOG.info("Sum keyboardInput " + sb.getPublicKey() + ": " + inputs.stream().mapToInt(TxInput::getAmount).sum());

                for (int inCnt = 0; inCnt < inputs.size(); inCnt++) {
                    var in = inputs.get(inCnt);
                    var txi = new Label(in.toDUP());

                    inputContainer.getChildren().add(txi);

                    var txu = new TxUnlock(inCnt, TxUnlock.UnlockFct.SIG, i + "");
                    var txulab = new Label(txu.toDUP());
                    unlocks.add(txu);
                    unlockContainer.getChildren().add(txulab);

                    var delButton = new Button("-");
                    delButton.setStyle("-fx-font: 10 monospaced;");
                    delButton.setOnAction(e -> {
                        inputContainer.getChildren().remove(txi);
                        unlocks.remove(txu);
                        unlockContainer.getChildren().remove(txulab);
                        sourcesActionCol.getChildren().remove(delButton);
                        inputs.remove(in);
                    });
                    sourcesActionCol.getChildren().add(delButton);
                }

            }


            outputContainer.getChildren().setAll(
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

            rawDocument.setValue(tx.toDUPdoc(true));

        });

    }


    @FXML
    public void addOutputs() {
        var out = new TxOutput();
        out.setAmount(Integer.parseInt(amountDest.getText()));
        out.setBase(0);
        out.setCondition("SIG(" + pkDest.getText() + ")");
        outputs.add(out);
        refresh();
    }

    @FXML
    public void refreshUnlocks() {
        unlockContainer.getChildren().clear();
    }


    @FXML
    public void refreshInputs() {


    }
}
