package juniter.service.adminfx;

import javafx.fxml.Initializable;
import javafx.stage.Stage;
import juniter.service.adminfx.include.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Database extends AbstractJuniterFX implements Initializable {

	private static final Logger LOG = LogManager.getLogger();


	@Override
	public void start(Stage primaryStage) {
		LOG.info("Starting Database");

		primaryStage.setTitle("Juniter - Database  ");
		primaryStage.show();
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
