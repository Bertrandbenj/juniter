package juniter.gui.business.panel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.index.IINDEX;
import juniter.grammar.*;
import juniter.repository.jpa.index.IINDEXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static juniter.gui.JuniterBindings.currenBlock;
import static juniter.gui.JuniterBindings.rawDocument;

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
    private TextField userid;
    @FXML
    private TextField version;
    @FXML
    private TextField currency;


    @FXML
    private TextField uniqueID;
    @FXML
    private TextField timestamp;
    @FXML
    private TextField signature;
    @FXML
    private TextField certTimestamp;
    @FXML
    private TextField idtySignature;
    @FXML
    private TextField idtyTimestamp;
    @FXML
    private TextField idtyUniqueID;
    @FXML
    private TextField idtyIssuer;
    @FXML
    private TextField certTS;
    @FXML
    private TextField block;
    @FXML
    private TextField idtySignatureRev;
    @FXML
    private TextField idtyTimestampRev;
    @FXML
    private TextField idtyUniqueIDRev;


    @Autowired
    private IINDEXRepository iRepo;

    private Document doc;


    public WotPanel() {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boxIdty.managedProperty().bind(boxIdty.visibleProperty());
        boxCertification.managedProperty().bind(boxCertification.visibleProperty());
        boxMembership.managedProperty().bind(boxMembership.visibleProperty());
        boxRevocation.managedProperty().bind(boxRevocation.visibleProperty());

        var b = currenBlock.get();
        timestamp.setText(b.bstamp());
        block.setText(b.bstamp());
        certTS.setText(b.bstamp());
        certTimestamp.setText(b.bstamp());
        idtyTimestamp.setText(b.bstamp());


        switchIdty();

        cbReceiver.getItems().setAll(iRepo.findAll().stream()
                .filter(i -> i.getUid() != null)
                .sorted(Comparator.comparing(IINDEX::getUid)).collect(Collectors.toList()));

        cbReceiver.setCellFactory(t -> new IdentityListCell());


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
            doc = new IdentityDoc(
                    version.getText(),
                    currency.getText(),
                    sb.getPublicKey(),
                    uniqueID.getText(),
                    timestamp.getText());
            var idty = (IdentityDoc) doc;

            var sign = sb.sign(idty.unsignedDoc());
            idty.setSignature(sign);
            signature.setText(sign);
        }

        if (swCertif.isSelected()) {
            doc = new CertificationDoc(
                    version.getText(),
                    currency.getText(),
                    sb.getPublicKey(),
                    idtyIssuer.getText(),
                    idtyUniqueID.getText(),
                    idtyTimestamp.getText(),
                    idtySignature.getText(),
                    certTimestamp.getText());
            var cert = (CertificationDoc) doc;

            var sign = sb.sign(cert.unsignedDoc());
            cert.setSignature(sign);
            signature.setText(sign);
        }

        if (swRevoc.isSelected()) {
            doc = new RevocationDoc(
                    version.getText(),
                    currency.getText(),
                    sb.getPublicKey(),
                    idtyUniqueIDRev.getText(),
                    idtyTimestampRev.getText(),
                    idtySignatureRev.getText());
            var rev = (RevocationDoc) doc;

            var sign = sb.sign(rev.unsignedDoc());
            rev.setSignature(sign);
            signature.setText(sign);
        }


        if (swMember.isSelected()) {
            doc = new MembershipDoc(
                    version.getText(),
                    currency.getText(),
                    sb.getPublicKey(),
                    block.getText(),
                    "IN",
                    userid.getText(),
                    certTimestamp.getText());
            var mem = (MembershipDoc) doc;

            var sign = sb.sign(mem.unsignedDoc());
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
