package juniter.gui.include;

import com.google.common.collect.Maps;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Joiner;
import juniter.core.validation.GlobalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

import static juniter.gui.include.JuniterBindings.block_0;
import static juniter.gui.include.JuniterBindings.rawDocument;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Block0Panel implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Block0Panel.class);


    @FXML
    private VBox membersContainer;

    @FXML
    private VBox certsContainers;

    @FXML
    private TextField nonceField;

    @FXML
    private TextField ud0;

    @FXML
    private TextField mSalt;

    @FXML
    private TextField udReevalTime;

    @FXML
    private TextField sigWindow;

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
    private ComboBox<Map.Entry<String, SecretBox>> cReceiver;

    @FXML
    private ComboBox<Map.Entry<String, SecretBox>> cCertifier;

    @FXML
    private ComboBox<Map.Entry<String, SecretBox>> cIssuer;


    private Map<String, SecretBox> members = Maps.newHashMap();
    private Map<String, Set<String>> certs = Maps.newHashMap();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var t = String.format("%d", new Date().getTime());

        time.setText(t);
        medianTime.setText(t);
        cIssuer.setOnAction(e -> refresh());


        var converter = new StringConverter<Map.Entry<String, SecretBox>>() {


            @Override
            public String toString(Map.Entry<String, SecretBox> object) {
                return object.getKey();
            }

            @Override
            public Map.Entry<String, SecretBox> fromString(String string) {
                return members.entrySet().stream().filter(m -> m.getKey().equals(string)).findFirst().orElseThrow();
            }


        };

        cIssuer.setConverter(converter);
        cReceiver.setConverter(converter);
        cCertifier.setConverter(converter);

    }


    private void refresh() {

        String initHash = GlobalValid.INIT_HASH;

        block_0.setNumber(Integer.valueOf(number.getText()));
        block_0.setVersion(Short.valueOf(version.getText()));
        block_0.setCurrency(currency.getText());
        block_0.setPowMin(Integer.valueOf(powMin.getText()));
        block_0.setTime(Long.valueOf(time.getText()));
        block_0.setMedianTime(Long.valueOf(medianTime.getText()));
        block_0.setUnitbase(Integer.valueOf(unitBase.getText()));
        block_0.setIssuersFrame(Integer.valueOf(issuersFrame.getText()));
        block_0.setIssuersFrameVar(Integer.valueOf(issuersFrameVar.getText()));
        block_0.setIssuersCount(Integer.valueOf(issuersCount.getText()));


        block_0.setIssuer(cIssuer.getValue().getValue().getPublicKey());


        var params = new ChainParameters(currency.getText());
        params.setAvgGenTime(Long.parseLong(avgGenTime.getText()));
        params.setC(Double.parseDouble(c.getText()));
        params.setDt(Long.parseLong(dt.getText()));
        params.setDtDiffEval(Long.parseLong(dtDiffEval.getText()));
        params.setDtReeval(Long.parseLong(dtReeval.getText()));
        params.setIdtyWindow(Long.parseLong(idtyWindow.getText()));
        params.setMedianTimeBlocks(Long.parseLong(medianTimeBlocks.getText()));
        params.setMsPeriod(Long.parseLong(msPeriod.getText()));
        params.setMsValidity(Long.parseLong(msValidity.getText()));
        params.setPercentRot(Double.parseDouble(percentRot.getText()));
        params.setUd0(Long.parseLong(ud0.getText()));
        params.setUdReevalTime0(Long.parseLong(udReevalTime.getText()));
        params.setSigPeriod(Long.parseLong(sigPeriod.getText()));
        params.setSigQty(Long.parseLong(sigQty.getText()));
        params.setSigReplay(Long.parseLong(sigReplay.getText()));
        params.setSigStock(Long.parseLong(sigStock.getText()));
        params.setSigValidity(Long.parseLong(sigValidity.getText()));
        params.setStepMax(Long.parseLong(stepMax.getText()));
        params.setXpercent(Double.parseDouble(xPercent.getText()));
        params.setSigWindow(Long.parseLong(sigWindow.getText()));
        params.setMsWindow(Long.parseLong(msWindow.getText()));
        params.setUdTime0(Long.parseLong(udTime0.getText()));


        block_0.setParameters(params);
        block_0.getIdentities().clear();
        block_0.getIdentities().addAll(
                members.entrySet().stream()
                        .map(ent ->
                        {
                            var idty = new Identity(ent.getValue().getPublicKey()
                                    + ":" + "==" // signature
                                    + ":" + initHash //  TODO complete
                                    + ":" + ent.getKey());

                            idty.setSignature(ent.getValue().sign(idty.toDUPdoc(false)));
                            return idty;
                        })
                        .collect(Collectors.toList())
        );

        block_0.getJoiners().clear();
        block_0.getJoiners().addAll(
                members.entrySet().stream()
                        .map(ent ->
                        {
                            var joiner = new Joiner(ent.getValue().getPublicKey()
                                    + ":" + "==" // signature
                                    + ":" + initHash// bstamp TODO complete
                                    + ":" + initHash// bstamp duplicate on node 0
                                    + ":" + ent.getKey());

                            joiner.setSignature(ent.getValue().sign(joiner.toDUPdoc(false)));

                            return joiner;
                        })
                        .collect(Collectors.toList())
        );

        block_0.getCertifications().clear();
        block_0.getCertifications().addAll(
                certs.entrySet().stream()
                        .flatMap(ent -> ent.getValue().stream().map(receiver ->
                        {
                            var cert = new Certification(
                                    ent.getKey()
                                            + ":" + receiver
                                            + ":0:" + "==" // signature
                            );
                            var signature = members.values().stream()
                                    .filter(pk -> pk.getPublicKey().equals(ent.getKey()))
                                    .findFirst().orElseThrow()
                                    .sign(cert.toDUPdoc(false));
                            cert.setSignature(signature);
                            return cert;


                        }))
                        .collect(Collectors.toList())
        );

        block_0.setMembersCount(members.size());
        certsContainers.getChildren().setAll(
                certs.entrySet().stream()
                        .flatMap(ent -> ent.getValue().stream().map(dest -> new Label(issuerOf(ent.getKey()) + " -> " + issuerOf(dest))))
                        .collect(Collectors.toList())
        );

        membersContainer.getChildren().setAll(
                members.entrySet().stream()
                        .map(ent -> new Label(ent.getValue().getPublicKey() + " : " + ent.getKey()))
                        .collect(Collectors.toList())
        );


        block_0.setInner_hash(Crypto.hash(block_0.toDUP(false, false)));

        LOG.info("members " + members + " == " + block_0.getIssuer());


        block_0.setSignature(cIssuer.getValue().getValue().sign(block_0.signedPart()));
        block_0.setNonce(100000L);

        block_0.setHash(Crypto.hash(block_0.signedPartSigned()));

        rawDocument.setValue(block_0.toDUP(true, true));


    }

    private String pubkeyOf(String pseudo) {
        return members.entrySet().stream()
                .filter(ent -> ent.getKey().equals(pseudo))
                .map(ent -> ent.getValue().getPublicKey())
                .findAny()
                .orElse("NO_PUBKEY_FOUND");
    }


    private String issuerOf(String pubkey) {
        return members.entrySet().stream()
                .filter(ent -> ent.getValue().getPublicKey().equals(pubkey))
                .map(ent -> ent.getKey())
                .findAny()
                .orElse("NO_PSEUDO_FOUND");
    }


    @FXML
    public void addMember() {
        var sb = new SecretBox(mSalt.getText(), mPassword.getText());
        members.put(mUid.getText(), sb);

        // fill combos
        cCertifier.getItems().setAll(members.entrySet());
        cReceiver.getItems().setAll(members.entrySet());
        cIssuer.getItems().setAll(members.entrySet());

        var previous = cIssuer.getValue();
        if (previous == null) {
            cIssuer.setValue(new SimpleEntry(mUid.getText(), sb));
        } else {
            cIssuer.setValue(previous);
        }

        refresh();
    }


    @FXML
    public void addCert() {
        var iss = cCertifier.getValue().getValue().getPublicKey();
        var rec = cReceiver.getValue().getValue().getPublicKey();
        if (!certs.containsKey(iss)) {
            certs.put(iss, new HashSet<>());
        }

        certs.get(iss).add(rec);
        cCertifier.getSelectionModel().clearSelection();
        cReceiver.getSelectionModel().clearSelection();
        refresh();
    }
}
