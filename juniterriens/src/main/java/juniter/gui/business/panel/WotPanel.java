package juniter.gui.business.panel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.IINDEX;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dbo.wot.Revoked;
import juniter.core.model.meta.DUPDocument;
import juniter.service.jpa.Index;
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
public class WotPanel implements Initializable {


    @FXML
    private ComboBox<IINDEX> cbReceiver;
    @FXML
    private VBox boxIdty;
    @FXML
    private VBox boxMembership;
    @FXML
    private VBox boxCertification;
    @FXML
    private VBox boxRevocation;

    @FXML
    private RadioButton swIdty;
    @FXML
    private RadioButton swMember;
    @FXML
    private RadioButton swCertif;
    @FXML
    private RadioButton swRevoc;

    @FXML
    private TextField salt;
    @FXML
    private TextField password;
    @FXML
    private Label pk;
    @FXML
    private TextField useridMem;
    @FXML
    private TextField version;
    @FXML
    private TextField currency;


    @FXML
    private TextField uniqueIDIdty;
    @FXML
    private TextField timestampIdty;
    @FXML
    private TextField signature;
    @FXML
    private TextField certTimestampCert;
    @FXML
    private TextField idtySignatureCert;
    @FXML
    private TextField idtyTimestampCert;
    @FXML
    private TextField idtyUniqueIDCert;
    @FXML
    private TextField idtyIssuerCert;
    @FXML
    private TextField certTSMem;
    @FXML
    private TextField blockMem;
    @FXML
    private TextField idtySignatureRev;
    @FXML
    private TextField idtyTimestampRev;
    @FXML
    private TextField idtyUniqueIDRev;


    @Autowired
    private Index index;


    private DUPDocument doc;


    public WotPanel() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        version.editableProperty().bind(advancedUser);
        currency.editableProperty().bind(advancedUser);
        signature.editableProperty().bind(advancedUser);
        timestampIdty.editableProperty().bind(advancedUser);
        certTimestampCert.editableProperty().bind(advancedUser);
        idtySignatureCert.editableProperty().bind(advancedUser);
        idtyUniqueIDCert.editableProperty().bind(advancedUser);
        idtyTimestampCert.editableProperty().bind(advancedUser);
        idtyIssuerCert.editableProperty().bind(advancedUser);
        idtySignatureRev.editableProperty().bind(advancedUser);
        idtyTimestampRev.editableProperty().bind(advancedUser);
        idtyUniqueIDRev.editableProperty().bind(advancedUser);


        boxIdty.managedProperty().bind(boxIdty.visibleProperty());
        boxCertification.managedProperty().bind(boxCertification.visibleProperty());
        boxMembership.managedProperty().bind(boxMembership.visibleProperty());
        boxRevocation.managedProperty().bind(boxRevocation.visibleProperty());
        pk.textProperty().addListener(c -> {
            var assocIdentity = index.reduceI(pk.getText()).get();
            var assocMembership = index.reduceM(pk.getText()).get();

            useridMem.setText(assocIdentity.getUid());
            certTSMem.setText(assocMembership.getSigned().toString());

            idtySignatureRev.setText(assocIdentity.getSig());
            idtyTimestampRev.setText(assocIdentity.getSigned().toString());
            idtyUniqueIDRev.setText(assocIdentity.getUid());

        });

        var b = currenBlock.get();
        timestampIdty.setText(b.bstamp());
        blockMem.setText(b.bstamp());
        certTimestampCert.setText(b.bstamp());


        switchIdty();

        cbReceiver.getItems().setAll(index.getIRepo().findAll().stream()
                .filter(i -> i.getUid() != null)
                .sorted(Comparator.comparing(IINDEX::getUid)).collect(Collectors.toList()));

        cbReceiver.setCellFactory(t -> new IdentityListCell());

        cbReceiver.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            var assocIdentity = index.getIRepo().byUidOrPubkey(null, newValue.getPub()).get(0);
            idtyIssuerCert.setText(assocIdentity.getPub());
            idtyUniqueIDCert.setText(assocIdentity.getUid());
            idtyTimestampCert.setText(assocIdentity.getSigned().toString());
            idtySignatureCert.setText(assocIdentity.getSig());
        });

    }

    class IdentityListCell extends ListCell<IINDEX> {
        @Override
        protected void updateItem(IINDEX item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getUid() + " / " + item.getPub());
            }
        }


    }

    @FXML
    public void refresh() {

        var sb = new SecretBox(salt.getText(), password.getText());

        if (swIdty.isSelected()) {
            doc = new Identity(
                    version.getText(),
                    currency.getText(),
                    sb.getPublicKey(),
                    uniqueIDIdty.getText(),
                    timestampIdty.getText());
            var idty = (Identity) doc;

            var sign = sb.sign(idty.toDUPdoc(false));
            idty.setSignature(sign);
            signature.setText(sign);
        }

        if (swCertif.isSelected()) {
            doc = new Certification();
            doc.setVersion(Short.valueOf(version.getText()));
            doc.setCurrency(currency.getText());

//                    sb.getPublicKey(),
//                    idtyIssuerCert.getText(),
//                    idtyUniqueIDCert.getText(),
//                    idtyTimestampCert.getText(),
//                    idtySignatureCert.getText(),
//                    certTimestampCert.getText());
            var cert = (Certification) doc;

            var sign = sb.sign(cert.toDUPdoc(false));
            cert.setSignature(sign);
            signature.setText(sign);
        }

        if (swRevoc.isSelected()) {
            doc = new Revoked();
            doc.setVersion(Short.valueOf(version.getText()));
            doc.setCurrency(currency.getText());
            ((Revoked) doc).setPubkey(sb.getPublicKey());
//
//            idtyUniqueIDRev.getText(),
//                    idtyTimestampRev.getText(),
//                    idtySignatureRev.getText());
            var rev = (Revoked) doc;

            var sign = sb.sign(rev.toDUPdoc(false));
            rev.setSignature(sign);
            signature.setText(sign);
        }


        if (swMember.isSelected()) {
            doc = new Member();
            ((Member) doc).setPubkey(sb.getPublicKey());
            doc.setCurrency(currency.getText());
            doc.setVersion(Short.valueOf(version.getText()));

            ((Member) doc).setUid(useridMem.getText());
//            ((Member) doc).setSigned(certTimestampCert.getText());


            var mem = (Member) doc;

            var sign = sb.sign(mem.toDUPdoc(false));
            mem.setSignature(sign);
            signature.setText(sign);
        }

        rawDocument.setValue(doc.toString());
        pk.setText(sb.getPublicKey());

    }

    @FXML
    public void switchIdty() {

        boxIdty.setVisible(true);
        boxMembership.setVisible(false);
        boxCertification.setVisible(false);
        boxRevocation.setVisible(false);
    }

    @FXML
    public void switchMembership() {


        boxIdty.setVisible(false);
        boxMembership.setVisible(true);
        boxCertification.setVisible(false);
        boxRevocation.setVisible(false);


    }

    @FXML
    public void switchCertif() {


        boxIdty.setVisible(false);
        boxMembership.setVisible(false);
        boxCertification.setVisible(true);
        boxRevocation.setVisible(false);

    }

    @FXML
    public void switchRevoc() {


        boxIdty.setVisible(false);
        boxMembership.setVisible(false);
        boxCertification.setVisible(false);
        boxRevocation.setVisible(true);

    }
}
