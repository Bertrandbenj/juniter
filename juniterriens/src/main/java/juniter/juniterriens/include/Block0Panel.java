package juniter.juniterriens.include;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Joiner;
import juniter.juniterriens.Notary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Block0Panel implements Initializable {

    @FXML
    private TextField ud0;

    @FXML
    private TextField mSalt;

    @FXML
    private TextField udReevalTime;

    @FXML
    private TextField sigWindow;

    @FXML
    private VBox membersContainer;

    @FXML
    private TextField msWindow;

    @FXML
    private TextField dtReeval;

    @FXML
    private TextField dtDiffEval;

    @FXML
    private TextField dt;

    @FXML
    private TextField sigQty;

    @FXML
    private TextField number;

    @FXML
    private TextField mUid;

    @FXML
    private TextField msPeriod;

    @FXML
    private TextField medianTime;

    @FXML
    private TextField idtyWindow;

    @FXML
    private TextField xPercent;

    @FXML
    private TextField currency;

    @FXML
    private TextField stepMax;

    @FXML
    private TextField medianTimeBlocks;

    @FXML
    private TextField udTime0;

    @FXML
    private TextField msValidity;

    @FXML
    private TextField percentRot;

    @FXML
    private TextField c;

    @FXML
    private TextField sigValidity;

    @FXML
    private TextField sigStock;

    @FXML
    private TextField powMin;

    @FXML
    private VBox certsContainers;

    @FXML
    private TextField avgGenTime;

    @FXML
    private TextField version;

    @FXML
    private TextField mPassword;

    @FXML
    private TextField unitBase;

    @FXML
    private TextField sigPeriod;

    @FXML
    private TextField issuersFrame;

    @FXML
    private TextField issuersFrameVar;

    @FXML
    private TextField issuersCount;

    @FXML
    private TextField sigReplay;

    @FXML
    private TextField time;

    @FXML
    private ComboBox cReceiver;

    @FXML
    private ComboBox cCertifier;

    @FXML
    private ComboBox cIssuer;


    private DBBlock block = new DBBlock();
    private  Map<String, SecretBox> members = Maps.newHashMap();
    private  Map<String, Set<String>> certs = Maps.newHashMap();




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        version.setText(Notary.PROTOCOL_VERSION+"");
        time.setText(String.format("%d", new Date().getTime()));
        medianTime.setText(String.format("%d", new Date().getTime()));

    }



    private void refresh() {
        block.setNumber(Integer.valueOf(number.getText()));
        block.setVersion(Short.valueOf(version.getText()));
        block.setCurrency(currency.getText());
        block.setPowMin(Integer.valueOf(powMin.getText()));
        block.setTime(Long.valueOf(time.getText()));
        block.setMedianTime(Long.valueOf(medianTime.getText()));
        block.setUnitbase(Integer.valueOf(unitBase.getText()));
        block.setIssuersFrame(Integer.valueOf(issuersFrame.getText()));
        block.setIssuersFrameVar(Integer.valueOf(issuersFrameVar.getText()));
        block.setIssuersCount(Integer.valueOf(issuersCount.getText()));

        cIssuer.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    block.setIssuer(pubkeyOf(newValue.toString()));
                });


        block.setParameters(c.getText() + ":" +
                dt.getText() + ":" +
                ud0.getText() + ":" +
                sigPeriod.getText() + ":" +
                sigStock.getText() + ":" +
                sigWindow.getText() + ":" +
                sigValidity.getText() + ":" +
                sigQty.getText() + ":" +
                idtyWindow.getText() + ":" +
                msWindow.getText() + ":" +
                xPercent.getText() + ":" +
                msValidity.getText() + ":" +
                stepMax.getText() + ":" +
                medianTimeBlocks.getText() + ":" +
                avgGenTime.getText() + ":" +
                dtDiffEval.getText() + ":" +
                percentRot.getText() + ":" +
                udTime0.getText() + ":" +
                udReevalTime.getText() + ":" +
                dtReeval.getText() + ":" +
                msPeriod.getText() + ":" +
                sigReplay.getText()
        );

        block.getIdentities().clear();
        block.getIdentities().addAll(
                members.entrySet().stream()
                        .map(ent ->
                                new Identity(ent.getValue().getPublicKey()
                                        + ":" + "==" // signature
                                        + ":" + "0-XXX" //  TODO complete
                                        + ":" + ent.getKey()))
                        .collect(Collectors.toList())
        );

        block.getJoiners().clear();
        block.getJoiners().addAll(
                members.entrySet().stream()
                        .map(ent ->
                                new Joiner(ent.getValue().getPublicKey()
                                        + ":" + "==" // signature
                                        + ":" + "0-XXX"// bstamp TODO complete
                                        + ":" + "0-XXX"// bstamp duplicate on node 0
                                        + ":" + ent.getKey()))
                        .collect(Collectors.toList())
        );

        block.getCertifications().clear();
        block.getCertifications().addAll(
                certs.entrySet().stream()
                        .flatMap(ent -> ent.getValue().stream().map(receiver ->
                                new Certification(
                                        pubkeyOf(ent.getKey())
                                                + ":" + pubkeyOf(receiver)
                                                + ":0:" + "_" // signature
                                )))
                        .collect(Collectors.toList())
        );

        block.setMembersCount(members.size());
        certsContainers.getChildren().setAll(
                certs.entrySet().stream()
                        .flatMap(ent -> ent.getValue().stream().map(dest -> new Label(ent.getKey() + " -> " + dest)))
                        .collect(Collectors.toList())
        );

        membersContainer.getChildren().setAll(
                members.entrySet().stream()
                        .map(ent -> new Label(ent.getValue().getPublicKey() + " : " + ent.getKey()))
                        .collect(Collectors.toList())
        );


        JuniterBindings.rawDocument.setValue(block.toDUP(true, true));
    }

    private String pubkeyOf(String huhu) {
        return members.entrySet().stream()
                .filter(ent -> ent.getKey().equals(huhu))
                .map(ent -> ent.getValue().getPublicKey())
                .findAny()
                .orElse("NO_PUBKEY_FOUND");
    }



    @FXML
    public void addMember() {
        var sb = new SecretBox(mSalt.getText(), mPassword.getText());
        members.put(mUid.getText(), sb);
        certs.put(mUid.getText(), Sets.newHashSet());
        cCertifier.getItems().setAll(members.keySet());
        cIssuer.getItems().setAll(members.keySet());
        cReceiver.getItems().setAll(members.keySet());
        refresh();
    }


    @FXML
    public void addCert() {
        var iss = cCertifier.getSelectionModel().getSelectedItem() + "";
        var rec = cReceiver.getSelectionModel().getSelectedItem() + "";
        certs.get(iss).add(rec);
        cCertifier.getSelectionModel().clearSelection();
        cIssuer.getSelectionModel().clearSelection();
        cReceiver.getSelectionModel().clearSelection();
        refresh();
    }
}
