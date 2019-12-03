package juniter.gui.business.panel;

import javafx.application.Preloader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import juniter.gui.game.screens.Room;
import juniter.gui.technical.AbstractJuniterFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static juniter.gui.JuniterBindings.overallTaxRate;

@Component
public class TaxPanel extends AbstractJuniterFX implements Initializable {

    private static final Logger LOG = LogManager.getLogger(TaxPanel.class);
    public Slider sliderTax;
    public Slider sliderRemuniter;
    public Slider sliderDevs;
    public Slider sliderSchool;
    public Slider sliderHealth;
    public Slider sliderJunidev;
    public Button ok;
    public Button cancel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.info("initialize Tax panel");
        sliderTax.valueProperty().bindBidirectional(overallTaxRate);
        cancel.setCancelButton(true);
        cancel.setOnAction(e -> ((Stage) cancel.getScene().getWindow()).close());
        ok.setOnAction(e -> {
        });
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));

        primaryStage.setOnCloseRequest(e-> Room.popupOpen = false);

        primaryStage.initModality(Modality.APPLICATION_MODAL);

        var page = (VBox) load("/gui/include/TaxPanel.fxml");
        //JuniterBindings.screenController.addScreen("Main", page);

        Scene scene = new Scene(page);

//        JuniterBindings.screenController.setMain(scene);

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/gui/game/img/pcent.png"));
        primaryStage.show();

    }
}
