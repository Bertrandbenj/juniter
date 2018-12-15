package juniter.service.adminfx.include;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * inspiration here https://github.com/buckyroberts/Source-Code-from-Tutorials
 */
@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class WotPanel extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger();


    public WotPanel() { }




    @Override
    public void start(Stage primaryStage) {

        AnchorPane page = (AnchorPane) load("/adminfx/include/Menu.fxml");

        Scene scene = new Scene(page);

        primaryStage.setScene(scene);

        primaryStage.show();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
