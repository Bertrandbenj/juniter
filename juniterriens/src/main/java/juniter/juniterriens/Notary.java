package juniter.juniterriens;

import antlr.generated.JuniterLexer;
import antlr.generated.JuniterParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import juniter.core.model.dto.raw.*;
import juniter.grammar.JuniterGrammar;
import juniter.juniterriens.include.AbstractJuniterFX;
import juniter.juniterriens.include.Bindings;
import juniter.service.bma.PeerService;
import org.antlr.v4.runtime.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Notary extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    @FXML
    private TextArea rawDoc;

    @FXML
    private TextArea logLocalValid;

    @FXML
    private TextArea logGlobalValid;

    @FXML
    private Button sendButton;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public static Integer PROTOCOL_VERSION = 10;

    @Override
    public void start(Stage primaryStage) {
        LOG.info("Starting Notary");

        primaryStage.setTitle("Juniter - DUP Documents Form  ");
        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rawDoc.textProperty().bind(Bindings.rawDocument);
        rawDoc.textProperty().addListener((observable, oldValue, newValue) -> {
            LOG.info("rawDocument Doc changed ");

            try {
                final var parser = juniterParser(CharStreams.fromString(newValue));
                var doc = parser.doc();
                assert doc != null : "doc is null";
                LOG.info(doc);

                JuniterGrammar visitor = new JuniterGrammar();
                var docObject = visitor.visitDoc(doc);
                LOG.info("Visited : " + docObject + "  ");

                //sendButton.setDisable(!docObject.isValid());


                logLocalValid.setWrapText(true);
                logLocalValid.setText("Prométhée dans sa révolte offrit le feu aux hommes, " +
                        "et si l'homme dans sa sagesse l'avait découvert tout seul ? " +
                        "et si c'était une femme? l'ordre de Zeus eut-il été différent ? ");
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                logLocalValid.setWrapText(false);
                logLocalValid.setText(sw.toString());
                sendButton.setDisable(true);
            }
            sendButton.setDisable(false); // FIXME
            logGlobalValid.setText(" some global validation log here  ");
        });
    }

    public void sendDoc() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        String dest = "wot/add";
        Wrapper reqBodyData = null;
        switch (rawDoc.getText().lines().filter(l -> l.startsWith("Type")).findFirst().get()) {
            case "Type: Transaction":
                dest = "tx/process";
                reqBodyData  = new WrapperTransaction(rawDoc.getText());
                break;
            case "Type: Identity":
                dest = "wot/add";
                reqBodyData  = new WrapperIdentity(rawDoc.getText());
                break;
            case "Type: Membership":
                dest = "blockchain/membership";
                reqBodyData  = new WrapperMembership(rawDoc.getText());
                break;
            case "Type: Certification":
                dest = "wot/certify";
                reqBodyData  = new WrapperCertification(rawDoc.getText());
                break;
            case "Type: Revocation":
                dest = "wot/revoke";
                reqBodyData  = new WrapperRevocation(rawDoc.getText());
                break;
        }


        try {
            //objectMapper.writeValueAsString(reqBodyData);
            var reqURL = peers.nextHost().get().getHost() ;
            reqURL += (reqURL.endsWith("/")?"":"/") + dest;

            LOG.info("sendDoc posting {} {}", reqURL,  reqBodyData);

            var request = new HttpEntity<>(reqBodyData, headers);

            var response = restTemplate.postForEntity(reqURL, request, Object.class);

            LOG.info("sendDoc response {}", response);

            if (response.getStatusCodeValue() != 200)
                throw new AssertionError("post doc status code {} " + response);
            else
                LOG.info("sendDoc response : {}", response);

        } catch (Exception | AssertionError e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logLocalValid.setWrapText(false);
            logLocalValid.setText(sw.toString());
            LOG.error("sendDoc Error ", e);
        }

    }

    @Autowired
    private PeerService peers;



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
