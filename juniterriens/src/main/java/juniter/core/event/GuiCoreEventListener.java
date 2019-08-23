package juniter.core.event;

import javafx.application.Platform;
import juniter.core.model.dbo.NetStats;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.gui.Network;
import juniter.gui.include.JuniterBindings;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * this class interface Core events onto the GUI
 */
@Service
public class GuiCoreEventListener implements ApplicationListener<CoreEvent> {

    @Override
    public void onApplicationEvent(CoreEvent event) {

        switch (event.getName()) {
            case "NewBINDEX":
                Platform.runLater(() -> JuniterBindings.currentBindex.setValue(((BINDEX) event.getWhat()).getNumber()));
                break;

            case "NewBlock":
                Platform.runLater(() -> {
                    JuniterBindings.currenBlock.setValue(((DBBlock) event.getWhat()));
                    JuniterBindings.currentDBBlock.setValue(((DBBlock) event.getWhat()).getNumber());
                });
                break;

            case "CurrentBNUM":
                Platform.runLater(() -> JuniterBindings.currentDBBlock.setValue((Integer) event.getWhat()));
                break;

            case "DecrementCurrent":
                Platform.runLater(() -> JuniterBindings.currentDBBlock.subtract(1));
                break;

            case "MaxDBBlock":
                Platform.runLater(() -> JuniterBindings.maxDBBlock.setValue((Integer) event.getWhat()));
                break;

            case "MaxPeerBlock":
                Platform.runLater(() -> JuniterBindings.maxPeerBlock.setValue((Integer) event.getWhat()));
                break;

            case "RenormalizedNet":
                Platform.runLater(() -> Network.observableList.setAll((List<NetStats>) event.getWhat()));
                break;

            case "LogNetwork":
                Platform.runLater(() -> JuniterBindings.peerLogMessage.setValue((String) event.getWhat()));
                break;

            case "LogIndex":
                Platform.runLater(() -> JuniterBindings.indexLogMessage.setValue((String) event.getWhat()));
                break;

            case "LogMemory":
                Platform.runLater(() -> JuniterBindings.memoryLogMessage.setValue((String) event.getWhat()));
                break;

            case "Indexing":
                Platform.runLater(() -> JuniterBindings.isIndexing.setValue((Boolean) event.getWhat()));
                break;

            default:
                break;

        }

    }
}