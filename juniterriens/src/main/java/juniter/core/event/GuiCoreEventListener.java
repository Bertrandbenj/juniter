package juniter.core.event;

import javafx.application.Platform;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.NetStats;
import juniter.core.model.dbo.index.BINDEX;
import juniter.gui.Network;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static juniter.gui.include.JuniterBindings.*;

/**
 * this class interface Core events onto the GUI
 */
@Service
public class GuiCoreEventListener implements ApplicationListener<CoreEvent> {

    @Override
    public void onApplicationEvent(CoreEvent event) {

        switch (event.getName()) {
            case "NewBINDEX":
                Platform.runLater(() -> currentBindex.setValue(((BINDEX) event.getWhat()).getNumber()));
                break;

            case "NewBlock":
                Platform.runLater(() -> {
                    currenBlock.setValue(((DBBlock) event.getWhat()));
                    currentDBBlock.setValue(((DBBlock) event.getWhat()).getNumber());
                });
                break;

            case "CurrentBNUM":
                Platform.runLater(() -> currentDBBlock.setValue((Integer) event.getWhat()));
                break;

            case "DecrementCurrent":
                Platform.runLater(() -> currentDBBlock.subtract(1));
                break;

            case "MaxDBBlock":
                Platform.runLater(() -> maxDBBlock.setValue((Integer) event.getWhat()));
                break;

            case "MaxPeerBlock":
                Platform.runLater(() -> maxPeerBlock.setValue((Integer) event.getWhat()));
                break;

            case "RenormalizedNet":
                Platform.runLater(() -> Network.observableList.setAll((List<NetStats>) event.getWhat()));
                break;

            case "LogNetwork":
                Platform.runLater(() -> peerLogMessage.setValue((String) event.getWhat()));
                break;

            case "LogIndex":
                Platform.runLater(() -> indexLogMessage.setValue((String) event.getWhat()));
                break;

            case "LogMemory":
                Platform.runLater(() -> memoryLogMessage.setValue((String) event.getWhat()));
                break;

            case "Indexing":
                Platform.runLater(() -> isIndexing.setValue((Boolean) event.getWhat()));
                break;

            default:
                break;

        }

    }
}