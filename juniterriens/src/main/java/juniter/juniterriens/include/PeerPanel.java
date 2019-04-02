package juniter.juniterriens.include;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dbo.net.Peer;
import juniter.repository.jpa.block.BlockRepository;
import juniter.service.bma.NetworkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class PeerPanel implements Initializable {

    private static final Logger LOG = LogManager.getLogger();


    @FXML
    private ComboBox typeCombo;

    @FXML
    private TextField salt;

    @FXML
    private TextField ip4;

    @FXML
    private TextField version;

    @FXML
    private TextField url;

    @FXML
    private TextField ip6;

    @FXML
    private TextField sessid;

    @FXML
    private TextField password;

    @FXML
    private TextField port;

    @FXML
    private VBox endpointsContainer;

    @FXML
    private TextField currency;

    @FXML
    private TextField block;

    @FXML
    private Label pubkey;


    @Autowired
    NetworkService netService;

    @Autowired
    BlockRepository blockRepository;

    private Peer peer;

    private String selectedEPType = "";


    @FXML
    public void addEndPoint() {

        var epType = selectedEPType;
        var sess = selectedEPType.equals("WS2P") ? sessid.getText() + " " : "";
        var url_ = url.getText().isEmpty() ? "" : (url.getText() + " ");
        var ip4_ = ip4.getText().isEmpty() ? "" : (ip4.getText() + " ");
        var ip6_ = ip6.getText().isEmpty() ? "" : (ip6.getText() + " ");

        var _ep = epType + " " + sess + url_ + ip4_ + ip6_ + port.getText();

        peer.endpoints().add(new EndPoint(_ep));
        refresh();
    }


    private void refresh() {

        presetPeer();

        endpointsContainer.getChildren().clear();

        endpointsContainer.getChildren().addAll(
                peer.endpoints().stream().map(EndPoint::getEndpoint)
                        .map(ep -> {
                            var remove = new Button("-");
                            remove.setOnAction(x -> {
                                LOG.info(" at ep x " + peer.endpoints().removeIf(ep1 -> ep1.getEndpoint().equals(ep)));
                                peer.endpoints().removeIf(ep1 -> ep1.getEndpoint().equals(ep));
                                refresh();
                            });
                            return new HBox(10, remove, new Label(ep));
                        })
                        .collect(Collectors.toList())
        );

        Bindings.rawDocument.setValue(peer.toDUP(true));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        typeCombo.getItems().setAll("WS2P", "BMAS", "BASIC_MERKLED_API");

        salt.setOnAction(e -> {
            var sb = new SecretBox(salt.getText(), password.getText());
            pubkey.setText(sb.getPublicKey());
        });
        password.setOnAction(e -> {
            var sb = new SecretBox(salt.getText(), password.getText());
            pubkey.setText(sb.getPublicKey());
        });

        typeCombo.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    selectedEPType = newValue.toString();
                    sessid.setVisible(selectedEPType.equals("WS2P"));
                });

        Platform.runLater(this::refresh);

    }


    void presetPeer() {
        peer = netService.endPointPeer(Bindings.currentBindex.intValue());
        sessid.setText(getRandomHexString(8));
        block.setText(peer.getBlock());
        pubkey.setText(peer.getPubkey());
        version.setText(peer.getVersion() + "");
        currency.setText(peer.getCurrency());
    }

    private String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
}
