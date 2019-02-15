package juniter.juniterriens;

import antlr.generated.JuniterLexer;
import antlr.generated.JuniterParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.grammar.JuniterGrammar;
import juniter.juniterriens.include.AbstractJuniterFX;
import juniter.juniterriens.include.Bindings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.io.Serializable;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Notary extends AbstractJuniterFX implements Initializable {

	private static final Logger LOG = LogManager.getLogger();

	@FXML private BorderPane content;

	@FXML private TextArea rawDoc;

	@FXML private TextArea logLocalValid;

	@FXML private TextArea logGlobalValid;

	@FXML private Button sendButton;


	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RestTemplate restTemplate;

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

			try{
				final var parser = juniterParser(CharStreams.fromString(newValue));
				var doc = parser.doc();
				assert doc != null : "doc is null" ;
				LOG.info(doc);

				JuniterGrammar visitor = new JuniterGrammar();
				var docObject = visitor.visitDoc(doc);
				LOG.info("Visited : "+ docObject + "  ");

				sendButton.setDisable(!docObject.isValid());

				logLocalValid.setWrapText(true);
				logLocalValid.setText("Prométhée dans sa révolte offrit le feu aux hommes, " +
						"et si l'homme dans sa sagesse l'avait découvert tout seul ? " +
						"et si c'était une femme? l'ordre de Zeus eut-il été différent ? ");
			} catch(Exception e ){
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logLocalValid.setWrapText(false);
				logLocalValid.setText(sw.toString());
				sendButton.setDisable(true);
			}

			logGlobalValid.setText(" some global validation log here  ");
		});
	}

	public void sendDoc(ActionEvent actionEvent) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		Map<String, String> bodyParamMap = new HashMap<>();

		WrapperIdentity reqBodyData = new WrapperIdentity(rawDoc.getText());
		try {
			objectMapper.writeValueAsString(reqBodyData);
			LOG.info("sendDoc posting {}",  reqBodyData);

			var request = new HttpEntity<>(reqBodyData, headers);

			var response = restTemplate.postForEntity( "https://duniter.moul.re/wot/add", request , String.class );
			if (response.getStatusCodeValue() != 200)
				throw new AssertionError("post Identity status code {} " + response);
			else
				LOG.info("sendDoc response : {}", response  );

		} catch (Exception | AssertionError e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logLocalValid.setWrapText(false);
			logLocalValid.setText(sw.toString());
		}


	}


	@Getter
	@Setter
	@NoArgsConstructor
	class WrapperIdentity implements Serializable {
		private static final long serialVersionUID = 1518741167475514278L;

		String identity;

		WrapperIdentity(String rawDoc) {
			this.identity = rawDoc;
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
