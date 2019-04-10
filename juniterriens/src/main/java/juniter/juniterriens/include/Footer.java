package juniter.juniterriens.include;

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

    private static final Logger LOG = LogManager.getLogger(Footer.class);

    @FXML private ProgressIndicator downloadIndic;
    @FXML private ProgressIndicator indexIndic;
    @FXML private Label indexLog;
    @FXML private Label peerLog;
    @FXML private Label memoryLog;
    @FXML private Label docPoolLog;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOG.debug("initialize Footer.fxml");
        // bind indicators
        indexIndic.progressProperty().bind(Bindings.currentBindex.divide(Bindings.maxBindex));
        downloadIndic.progressProperty().bind(Bindings.currentDBBlock.divide(Bindings.maxPeerBlock));

        // bind logger


        indexLog.textProperty().bind(new SimpleStringProperty("Index : ")
                .concat(javafx.beans.binding.Bindings.format("%,.2f", Bindings.currentBindex.multiply(100).divide(Bindings.currentDBBlock)))
                .concat("% - HEAD: ").concat(Bindings.currentBindex)
                .concat(" - DB: ").concat(Bindings.currentDBBlock)
                .concat(" - ").concat(Bindings.indexLogMessage));
        peerLog.textProperty().bind(new SimpleStringProperty("Peers : ")
                .concat(Bindings.maxPeerBlock)
                .concat(" - ").concat(Bindings.peerLogMessage));
        memoryLog.textProperty().bind(new SimpleStringProperty("Memory : ")
                .concat(Bindings.memoryLogMessage).concat(" - "));
        docPoolLog.textProperty().bind(new SimpleStringProperty("Pools : ")
                .concat(" - ").concat(Bindings.docPoolLogMessage));
    }

}
