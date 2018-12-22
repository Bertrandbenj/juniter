package juniter.service.adminfx;

import antlr.main.JuniterLexer;
import antlr.main.JuniterParser;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import juniter.grammar.JuniterGrammar;
import juniter.service.adminfx.include.AbstractJuniterFX;
import org.antlr.v4.runtime.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class DUPNotary extends AbstractJuniterFX implements Initializable {

	private static final Logger LOG = LogManager.getLogger();


	public static SimpleStringProperty raw = new SimpleStringProperty("here comes the Document in DUP format");

	@FXML private TextArea rawDoc;
	@FXML private TextArea logLocalValid;
	@FXML private TextArea logGlobalValid;
	@FXML private Button sendButton;


	@Override
	public void start(Stage primaryStage) {
		LOG.info("Starting DUPNotary");

		primaryStage.setTitle("Juniter - DUP Documents Form  ");
		primaryStage.show();

	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rawDoc.textProperty().bind(raw );
		rawDoc.textProperty().addListener((observable, oldValue, newValue) -> {
			LOG.info("raw.addListener");

			try{
				final var parser = juniterParser(CharStreams.fromString(rawDoc.getText()));
				var doc = parser.doc();
				assert doc != null : "doc is null" ;

				JuniterGrammar visitor = new JuniterGrammar();
				var docObject = visitor.visitDoc(doc);

				sendButton.setDisable(!docObject.isValid());

				logLocalValid.setText("Promethée dans sa revolte offrit le feu aux hommes, " +
						"et si l'homme dans sa sagesse l'avait découvert tout seul ? " +
						"et si c'était une femme? l'ordre de Zeus eut-il été différent ? ");
			} catch(Exception e ){
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				logLocalValid.setText(sw.toString());
				sendButton.setDisable(true);
			}

			logGlobalValid.setText(" some global validation log here  ");
		});

	}

	public void sendDoc(ActionEvent actionEvent) {
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
