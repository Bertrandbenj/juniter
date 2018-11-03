package juniter.service.front;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;


public class GraphPanel extends Application {

	private static final Logger LOG = LogManager.getLogger();

	@FXML
	WebView  SVGAnchor;

	@FXML
	TextArea  DotAnchor;

	@FXML
	HBox svg ;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(GraphPanel.class.getResource("/adminfx/GraphPanel.fxml"));
		loader.setRoot(svg);
		HBox page =  (HBox) loader.load();


		WebEngine webEngine = SVGAnchor.getEngine(); 	// Get WebEngine via WebView
		webEngine.load("http://eclipse.com"); 	// Load page

		File file = new File("/home/ben/ws/juniter/src/main/resources/static/dot/3BE66EE5F40F8E93441D63E1274F8780FB330003B779C374B247DD45B737799E.svg");
		URL url = file.toURI().toURL();
		webEngine.load(url.toString()); 	// Load page



		DotAnchor.isWrapText();


		primaryStage.setTitle("Juniter - Dot visualisation  ");
		primaryStage.show();

	}



}
