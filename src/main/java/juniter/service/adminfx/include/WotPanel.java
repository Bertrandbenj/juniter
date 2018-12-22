package juniter.service.adminfx.include;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.BStamp;
import juniter.grammar.Document;
import juniter.grammar.IdentityDocument;
import juniter.repository.jpa.BlockRepository;
import juniter.service.adminfx.DUPNotary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class WotPanel  implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    @FXML private TextField salt;
    @FXML private TextField password;
    @FXML private VBox boxIdty;
    @FXML private VBox boxMembership;
    @FXML private VBox boxCertification;
    @FXML private Label pk;
    @FXML private TextField uniqueID;
    @FXML private TextField timestamp;
    @FXML private TextField signature;
    @FXML private TextField certTimestamp;
    @FXML private TextField idtySignature;
    @FXML private TextField idtyTimestamp;
    @FXML private TextField idtyUniqueID;
    @FXML private TextField idtyIssuer;
    @FXML private TextField certTS;
    @FXML private TextField block;
    @FXML private RadioButton swIdty;
    @FXML private RadioButton swMember;
    @FXML private RadioButton swCertif;
    @FXML private RadioButton swRevoc;
    @FXML private TextField userid;

    @Autowired
    BlockRepository blockRepo;


    public WotPanel() { }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boxMembership.setPrefWidth(0);
        boxCertification.setPrefWidth(0);
        boxIdty.setPrefWidth(500);

        blockRepo.current().ifPresent(b->{
            timestamp.setText(b.bstamp());
            block.setText(b.bstamp());
            certTS.setText(b.bstamp());
            certTimestamp.setText(b.bstamp());
            idtyTimestamp.setText(b.bstamp());
        });
    }

    Document doc;

    @FXML
    public void refresh(){

        var sb = new SecretBox(salt.getText(),password.getText());

        if(swIdty.isSelected()){
            doc = new IdentityDocument("10","g1",sb.getPublicKey(), uniqueID.getText(),new BStamp(timestamp.getText()),signature.getText() );
            var idty = (IdentityDocument) doc;
            idty.setSignature(sb.sign(idty.unsignedDoc()));
            signature.setText(idty.getSignature());
            DUPNotary.raw.setValue(idty.toString());
        }

        if(swCertif.isSelected()){
            DUPNotary.raw.setValue("");
        }

        if(swMember.isSelected()){
            DUPNotary.raw.setValue("");
        }

    }

    @FXML
    public void switchIdty(ActionEvent actionEvent) {
        boxMembership.setPrefWidth(0);
        boxCertification.setPrefWidth(0);
        boxIdty.setPrefWidth(500);
        boxIdty.setVisible(true);
        boxMembership.setVisible(false);
        boxCertification.setVisible(false);
    }

    @FXML
    public void switchMembership(ActionEvent actionEvent) {
        boxMembership.setPrefWidth(500);
        boxCertification.setPrefWidth(0);
        boxIdty.setPrefWidth(0);
        boxIdty.setVisible(false);
        boxMembership.setVisible(true);
        boxCertification.setVisible(false);

    }

    @FXML
    public void switchCertif(ActionEvent actionEvent) {
        boxMembership.setPrefWidth(0);
        boxCertification.setPrefWidth(500);
        boxIdty.setPrefWidth(0);
        boxIdty.setVisible(false);
        boxMembership.setVisible(false);
        boxCertification.setVisible(true);
    }

    @FXML
    public void switchRevoc(ActionEvent actionEvent) {
        boxMembership.setPrefWidth(0);
        boxCertification.setPrefWidth(0);
        boxIdty.setPrefWidth(0);
        boxIdty.setVisible(false);
        boxMembership.setVisible(false);
        boxCertification.setVisible(false);
    }
}
