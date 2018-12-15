package juniter.service.adminfx.include;

import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Block0Panel extends AbstractJuniterFX implements Initializable {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane page = (AnchorPane) load("/adminfx/include/Block0Panel.fxml");

        Scene scene = new Scene(page);

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
