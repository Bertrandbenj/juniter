package juniter.service.adminfx.include;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class Footer implements Initializable {

    private static final Logger LOG = LogManager.getLogger();

    @FXML private ProgressIndicator loadIndic;
    @FXML private ProgressIndicator indexIndic;
    @FXML private Label indexLog;
    @FXML private Label peerLog;
    @FXML private Label sparkLog;
    @FXML private Label docPoolLog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.debug("initialize Footer.fxml");
        // bind indicators
        indexIndic.progressProperty().bind(Bus.currentBindex.divide(Bus.maxBindex));
        loadIndic.progressProperty().bind(Bus.maxDBBlock.divide(Bus.maxPeerBlock));

        // bind logger
        indexLog.textProperty().bind(new SimpleStringProperty("Index : ")
                .concat("Bidx: ").concat(Bus.currentBindex)
                .concat(" - DB: ").concat(Bus.maxDBBlock)
                .concat(" - ").concat(Bus.indexLogMessage));
        peerLog.textProperty().bind(new SimpleStringProperty("Peers : ")
                .concat("max : ").concat(Bus.maxPeerBlock)
                .concat(" - ").concat(Bus.peerLogMessage));
        sparkLog.textProperty().bind(new SimpleStringProperty("Spark : ")
                .concat(" - ").concat(Bus.sparkLogMessage));
        docPoolLog.textProperty().bind(new SimpleStringProperty("Pools : ")
                .concat(" - ").concat(Bus.docPoolLogMessage));
    }
}
