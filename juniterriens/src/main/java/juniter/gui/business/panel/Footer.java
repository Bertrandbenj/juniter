package juniter.gui.business.panel;

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

import static javafx.beans.binding.Bindings.*;
import static juniter.gui.JuniterBindings.*;


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
        LOG.debug("initialize Footer.fxml " );

        // bind indicators
        indexIndic.progressProperty().bind(indexRatio);
        downloadIndic.progressProperty().bind(dlRatio);

        // bind loggers
        indexLog.textProperty().bind(new SimpleStringProperty("Index : ")
                .concat(format("%,.2f", indexRatio.multiply(100)))
                .concat("% - HEAD: ").concat(currentBindexN)
                .concat(" - DB: ").concat(highestDBBlock)
                .concat(" - ").concat(indexLogMessage));
        peerLog.textProperty().bind(new SimpleStringProperty("Peers : ")
                .concat(maxPeerBlock)
                .concat(" - ").concat(peerLogMessage));
        memoryLog.textProperty().bind(new SimpleStringProperty("Memory : ")
                .concat(memoryLogMessage).concat(" - "));
        docPoolLog.textProperty().bind(new SimpleStringProperty("Pools : ")
                .concat(" - ").concat(docPoolLogMessage));
    }

}
