package juniter.service.front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class DUPDocumentsForm extends AbstractJavaFxApplicationSupport implements Initializable {

	private static final Logger LOG = LogManager.getLogger();

	@FXML BorderPane content;



	@Override
	public void start(Stage primaryStage) {
		LOG.info("Starting DUPDocumentsForm");

		primaryStage.setTitle("Juniter - DUP Documents Form  ");
		primaryStage.show();
	}


	@FXML
	public void viewSVGGraph(ActionEvent event) {
		LOG.info("view SVG Graph " + event.getEventType());

		BorderPane page = (BorderPane) load("/adminfx/GraphPanel.fxml");

		Scene scene = new Scene(page);


		Stage stage = (Stage) content.getScene().getWindow();
		stage.setScene(scene);
		stage.show();

	}

	@FXML
	public void viewMain(ActionEvent event) {
		LOG.info("view Main " + event.getEventType());

		BorderPane page =  (BorderPane) load("/adminfx/AdminFX.fxml");

		Scene scene = new Scene(page);


		Stage stage = (Stage) content.getScene().getWindow();

		stage.setScene(scene);
		stage.show();

	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
