package juniter.core.event;

import javafx.application.Platform;
import javafx.geometry.Pos;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dbo.net.NetStats;
import juniter.gui.business.page.Network;
import juniter.gui.technical.ScreenController;
import juniter.user.UserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static juniter.gui.JuniterBindings.*;

/**
 * this class interface Core events onto the GUI
 */
@Service
public class GuiCoreEventListener implements ApplicationListener<CoreEvent> {

    private static final Logger LOG = LogManager.getLogger(GuiCoreEventListener.class);

    private final List<String> wallets = new UserSettings().getWallets();

    @Override
    public void onApplicationEvent(CoreEvent event) {

        var notifTitle = "";
        var notifText = "";

        switch (event.getName()) {
            case "NewBINDEX":
                var bindex = (BINDEX) event.getWhat();
                notifTitle = "A new " + bindex.getCurrency() + " Block as been indexed ";
                notifText = "# " + bindex.getNumber();
                Platform.runLater(() -> currentBindex.setValue(bindex));
                break;

            case "NewBlock":
                var block = (DBBlock) event.getWhat();
                notifTitle = "A new " + block.getCurrency() + " Block as been Saved ";
                notifText = "# " + block.getNumber();

                if (wallets.contains(block.getIssuer())) {
                    notifText += "congrats you forged this block";
                }

                Platform.runLater(() -> {
                    currenBlock.setValue(block);
                    currentDBBlockNum.setValue(block.getNumber());
                });
                break;

            case "CurrentBNUM":
                Platform.runLater(() -> currentDBBlockNum.setValue((Integer) event.getWhat()));
                break;

            case "DecrementCurrent":
                Platform.runLater(() -> currentDBBlockNum.subtract(1));
                break;

            case "MaxDBBlock":
                Platform.runLater(() -> highestDBBlock.setValue((Integer) event.getWhat()));
                break;

            case "MaxPeerBlock":
                Platform.runLater(() -> maxPeerBlock.setValue((Integer) event.getWhat()));
                break;

            case "RenormalizedNet":
                var list = (List<NetStats>) event.getWhat();
                list.sort(Comparator.comparing(NetStats::getLastNormalizedScore));
                list.subList(list.size() - 10, list.size() - 1);
                Platform.runLater(() -> Network.observableNetStats.setAll(list));
                break;

            case "LogNetwork":
                Platform.runLater(() -> peerLogMessage.setValue((String) event.getWhat()));
                break;

            case "LogMemory":
                Platform.runLater(() -> memoryLogMessage.setValue((String) event.getWhat()));
                break;

            case "Indexing":
                Platform.runLater(() -> isIndexing.setValue((Boolean) event.getWhat()));
                Platform.runLater(() -> indexLogMessage.setValue(event.getMessage()));
                break;

            default:
                break;

        }

        if (!notifTitle.isEmpty()) {
            String finalNotifTitle = notifTitle;
            String finalNotifText = notifText;
            Platform.runLater(() -> Notifications
                    .create()
                    .title(finalNotifTitle)
                    .text(finalNotifText)
                    .owner(ScreenController.singleton.getMain().getWindow())
                    .position(Pos.TOP_RIGHT)
                    .darkStyle()
                    .showInformation());
        }

    }
}