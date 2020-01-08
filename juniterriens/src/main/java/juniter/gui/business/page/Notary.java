package juniter.gui.business.page;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import juniter.core.model.technical.DocumentType;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dto.raw.*;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.I18N;
import juniter.service.core.BlockService;
import juniter.service.core.PeerService;
import juniter.service.core.Sandboxes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import static juniter.core.model.technical.DocumentType.*;
import static juniter.gui.JuniterBindings.block_0;
import static juniter.gui.JuniterBindings.rawDocument;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Notary extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Notary.class);

    public Label isValidLabel;
    public Button sendButton;
    public Button sandboxButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab paneWOT;
    @FXML
    private Tab paneTX;
    @FXML
    private Tab paneBlock0;
    @FXML
    private Tab panePeer;

    @FXML
    private TextArea rawDocTextArea;

    @FXML
    private TextArea logLocalValid;

    @FXML
    private TextArea logGlobalValid;

    @Autowired
    private RestTemplate POST;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peers;

    @Autowired
    private Sandboxes sandbox;

    private BooleanProperty docIsValid = new SimpleBooleanProperty(false);

    private ObjectProperty<DocumentType> docType = new SimpleObjectProperty<>(IDENTITY);

    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Notary");

        primaryStage.setTitle("Juniter - " + I18N.get("notary"));
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // for the time being
        logGlobalValid.managedProperty().bind(logGlobalValid.visibleProperty());
        logGlobalValid.setVisible(false);

        isValidLabel.textProperty().bind(new SimpleStringProperty(I18N.get("notary.isValid") + " ").concat(docIsValid.asString()));

        //sendButton.disableProperty().bind(docIsValid.not()); // FIXME uncomment once fixed

        rawDocTextArea.textProperty().bind(rawDocument);

        tabPane.selectionModelProperty().addListener((obs, old, tab) -> {
            var selectedTab = tab.getSelectedItem();

            if (selectedTab.equals(paneTX)) {
                docType.set(TRANSACTION);
            } else if (selectedTab.equals(paneWOT)) {
                switch (rawDocTextArea.getText().lines().filter(l -> l.startsWith("Type")).findFirst().orElseThrow()) {
                    case "Type: Identity":
                        docType.set(IDENTITY);
                    case "Type: Membership":
                        docType.set(MEMBERSHIP);
                    case "Type: Certification":
                        docType.set(CERTIFICATION);
                    case "Type: Revocation":
                        docType.set(REVOCATION);
                }
            } else if (selectedTab.equals(panePeer)) {
                docType.set(PEER);
            } else if (selectedTab.equals(paneBlock0)) {
                docType.set(BLOCK0);
            }
        });

    }

    @FXML
    public void sendDoc() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        var dest = "wot/add";
        Wrapper reqBodyData = null;

        var doc = rawDocTextArea.getText();

        switch (docType.get()) {
            case PEER:
                dest = "network/peering";
                reqBodyData = new WrapperPeer(doc);
                break;
            case TRANSACTION:
                dest = "tx/process";
                reqBodyData = new WrapperTransaction(doc);
                break;
            case BLOCK0:
                blockService.safeSave(block_0);
                reqBodyData = new WrapperBlock(doc);
                // TODO COMPLETE
                break;
            case IDENTITY:
                dest = "wot/add";
                reqBodyData = new WrapperIdentity(doc);
                break;
            case MEMBERSHIP:
                dest = "blockchain/membership";
                reqBodyData = new WrapperMembership(doc);
                break;
            case REVOCATION:
                dest = "wot/revoke";
                reqBodyData = new WrapperRevocation(doc);
                break;
            case CERTIFICATION:
                dest = "wot/certify";
                reqBodyData = new WrapperCertification(doc);
                break;
        }


        var reqURL = peers.nextHost(EndPointType.BMAS).orElseThrow().getHost();
        //var reqURL = "https://g1.presles.fr"; // FIXME remove when fixed
        reqURL += (reqURL.endsWith("/") ? "" : "/") + dest;

        LOG.info("posting Doc to {}\n{}", reqURL, reqBodyData);

        var request = new HttpEntity<>(reqBodyData, headers);
        ResponseEntity response = null;

        try {

            response = POST.postForEntity(reqURL, request, Object.class);

            if (response.getStatusCodeValue() != 200) {
                throw new AssertionError("post Doc error, code {} " + response);
            } else {
                LOG.info("successfully sent Doc, response : {}", response);
                logLocalValid.setText("successfully sent Doc, response : " + response);
            }

        } catch (HttpServerErrorException http) {
            LOG.warn("error sending Doc response {} " + response, http);
            logLocalValid.setText("error sending Doc, response : " + response);
        } catch (ResourceAccessException ignored) {
            LOG.warn("ignored ResourceAccessException (handled as duniter ucode )", ignored);
        } catch (Exception | AssertionError e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logLocalValid.setWrapText(false);
            logLocalValid.setText(sw.toString());
            LOG.error("Notary.sendDoc ", e);
        }


    }

    @FXML
    private void sandboxIt() {
        var doc = rawDocTextArea.getText();

        try {
            sandbox.put(doc, docType.get());

            logLocalValid.setWrapText(true);
            logLocalValid.setText(I18N.get("φ.1"));

        } catch (AssertionError | Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logLocalValid.setWrapText(false);
            logLocalValid.setText(sw.toString());
            LOG.error("Parsing error " + e.getMessage());
        }


    }
}
