package juniter.gui.include;

import javafx.application.Preloader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import juniter.gui.game.screens.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

 import java.net.URL;
import java.util.ResourceBundle;

@Component

public class Technology extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(Technology.class);
    public Button cancel;
    public Button ok;

    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

//        if (null == blockRepo) {
//            throw new IllegalStateException("BlockRepository was not injected properly");
//        }
        primaryStage.setOnCloseRequest(e-> Room.popupOpen = false);
        var page = (BorderPane) load("/gui/game/Technology.fxml");
        //JuniterBindings.screenController.addScreen("Main", page);

        Scene scene = new Scene(page);

//        JuniterBindings.screenController.setMain(scene);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/gui/game/img/techtree.png"));
        primaryStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancel.setCancelButton(true);
        cancel.setOnAction(e -> ((Stage) cancel.getScene().getWindow()).close());
        ok.setOnAction(e -> {
            LOG.info("ok");
        });
    }
}
