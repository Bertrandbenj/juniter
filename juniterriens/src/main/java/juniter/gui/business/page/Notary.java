package juniter.gui.business.page;

import antlr.generated.JuniterLexer;
import antlr.generated.JuniterParser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dto.raw.*;
import juniter.grammar.JuniterGrammar;
import juniter.gui.technical.AbstractJuniterFX;
import juniter.gui.technical.I18N;
import juniter.service.BlockService;
import juniter.service.bma.PeerService;
import org.antlr.v4.runtime.*;
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

import static juniter.gui.JuniterBindings.block_0;
import static juniter.gui.JuniterBindings.rawDocument;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Notary extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Notary.class);

    public Label isValidLabel;
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

    @FXML
    private Button sendButton;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peers;

    private BooleanProperty docIsValid = new SimpleBooleanProperty(false);

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
        rawDocTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            LOG.info("rawDocument Doc changed ");
            if (tabPane.getSelectionModel().getSelectedItem().equals(paneTX))// FIXME remove once fixed
                return;


            try {
                final var parser = juniterParser(CharStreams.fromString(newValue));
                var doc = parser.doc();
                assert doc != null : "doc is null";


                LOG.debug("parsed document \n {}", doc);

                JuniterGrammar visitor = new JuniterGrammar();
                var docObject = visitor.visitDoc(doc);
                LOG.info("Visited : " + docObject + "  ");

                logLocalValid.setWrapText(true);
                logLocalValid.setText(I18N.get("φ.1"));

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                logLocalValid.setWrapText(false);
                logLocalValid.setText(sw.toString());
                LOG.error("Parsing error " + e.getMessage());
            }

        });
    }

    @FXML
    public void sendDoc() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String dest = "wot/add";
        Wrapper reqBodyData = null;

        var selectedPane = tabPane.getSelectionModel().getSelectedItem();

        if (selectedPane.equals(paneBlock0)) {

            blockService.localSave(block_0);
            reqBodyData = new WrapperBlock(rawDocTextArea.getText());
            // TODO COMPLETE
        } else {
            if (selectedPane.equals(paneTX)) {
                dest = "tx/process";
                reqBodyData = new WrapperTransaction(rawDocTextArea.getText());

            }
            if (selectedPane.equals(panePeer)) {
                dest = "network/peering";
                reqBodyData = new WrapperPeer(rawDocTextArea.getText());

            } else if (selectedPane.equals(paneWOT)) {
                switch (rawDocTextArea.getText().lines().filter(l -> l.startsWith("Type")).findFirst().orElseThrow()) {
                    case "Type: Identity":
                        dest = "wot/add";
                        reqBodyData = new WrapperIdentity(rawDocTextArea.getText());
                        break;
                    case "Type: Membership":
                        dest = "blockchain/membership";
                        reqBodyData = new WrapperMembership(rawDocTextArea.getText());
                        break;
                    case "Type: Certification":
                        dest = "wot/certify";
                        reqBodyData = new WrapperCertification(rawDocTextArea.getText());
                        break;
                    case "Type: Revocation":
                        dest = "wot/revoke";
                        reqBodyData = new WrapperRevocation(rawDocTextArea.getText());
                        break;
                }
            }

            var reqURL = peers.nextHost(EndPointType.BMAS).orElseThrow().getHost();
            //var reqURL = "https://g1.presles.fr"; // FIXME remove when fixed
            reqURL += (reqURL.endsWith("/") ? "" : "/") + dest;

            LOG.info("posting doc to {}\n{}", reqURL, reqBodyData);

            var request = new HttpEntity<>(reqBodyData, headers);
            ResponseEntity response = null;

            try {

                response = restTemplate.postForEntity(reqURL, request, Object.class);

                if (response.getStatusCodeValue() != 200) {
                    throw new AssertionError("post doc error, code {} " + response);
                } else {
                    LOG.info("successfully sent doc, response : {}", response);
                    logLocalValid.setText("successfully sent doc, response : " + response);
                }

            } catch (HttpServerErrorException http) {
                LOG.warn("error sending doc response {} " + response, http);

                logLocalValid.setText("error sending doc, response : " + response);
            } catch (ResourceAccessException ignored) {
                LOG.warn("ignored ResourceAccessException (handled as duniter ucode )", ignored );
            } catch (Exception | AssertionError e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                logLocalValid.setWrapText(false);
                logLocalValid.setText(sw.toString());
                LOG.error("Notary.sendDoc ", e);
            }
        }

    }


    private JuniterParser juniterParser(CharStream file) {
        final JuniterLexer l = new JuniterLexer(file);
        final JuniterParser p = new JuniterParser(new CommonTokenStream(l));

        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });

        return p;
    }
}
