package juniter.service.adminfx.include;

import javafx.beans.binding.Bindings;
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
    @FXML private Label memoryLog;
    @FXML private Label docPoolLog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.debug("initialize Footer.fxml");
        // bind indicators
        indexIndic.progressProperty().bind(Bus.currentBindex.divide(Bus.maxBindex));
        loadIndic.progressProperty().bind(Bus.currentDBBlock.divide(Bus.maxPeerBlock));

        // bind logger


        indexLog.textProperty().bind(new SimpleStringProperty("Index : ")
                .concat(Bindings.format("%,.2f",Bus.currentBindex.multiply(100).divide(Bus.currentDBBlock)))
                .concat("% - HEAD: ").concat(Bus.currentBindex)
                .concat(" - DB: ").concat(Bus.currentDBBlock)
                .concat(" - ").concat(Bus.indexLogMessage));
        peerLog.textProperty().bind(new SimpleStringProperty("Peers : ")
                .concat(Bus.maxPeerBlock)
                .concat(" - ").concat(Bus.peerLogMessage));
        memoryLog.textProperty().bind(new SimpleStringProperty("Memory : ")
                .concat(Bus.memoryLogMessage).concat(" - "));
        docPoolLog.textProperty().bind(new SimpleStringProperty("Pools : ")
                .concat(" - ").concat(Bus.docPoolLogMessage));
    }

}
