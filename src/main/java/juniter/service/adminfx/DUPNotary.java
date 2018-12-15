package juniter.service.adminfx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
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
public class DUPNotary extends AbstractJuniterFX implements Initializable {

	private static final Logger LOG = LogManager.getLogger();

	@FXML BorderPane content;



	@Override
	public void start(Stage primaryStage) {
		LOG.info("Starting DUPNotary");

		primaryStage.setTitle("Juniter - DUP Documents Form  ");
		primaryStage.show();
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
