package juniter.core.event;

import javafx.application.Platform;
import juniter.juniterriens.Network;
import juniter.juniterriens.include.Bindings;
import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JuniterriensEventListener implements CoreEventBus {


    Logger LOG = LogManager.getLogger(JuniterriensEventListener.class);


    @EventListener(condition = "#event.success")
    public void handleSuccessful(GenericSpringEvent<String> event) {
        LOG.info("Handling generic event (conditional).");
    }


    @Override
    public void sendEventCurrentBindex(final int i) {
        Platform.runLater(() -> Bindings.currentBindex.setValue(i));
    }

    @Override
    public void sendEventIndexLogMessage(String s) {
        Platform.runLater(() -> Bindings.indexLogMessage.setValue(s));

    }

    @Override
    public void sendEventPeerLogMessage(String s) {
        Platform.runLater(() -> Bindings.peerLogMessage.setValue(s));
    }

    @Override
    public void sendEventIsIndexing(boolean is) {
        Platform.runLater(() -> Bindings.isIndexing.setValue(is));
    }

    @Override
    public void sendEventCurrent(long x) {
        Platform.runLater(() -> {
            Bindings.currentDBBlock.setValue(x);
        });
    }

    @Override
    public void sendEventSetMaxDBBlock(int maxBlockDB) {
        Platform.runLater(() -> {
            Bindings.maxDBBlock.setValue(maxBlockDB);
        });
    }

    @Override
    public void sendEventSetMaxPeerBlock(long maxBlockDB) {
        Platform.runLater(() -> {
            Bindings.maxPeerBlock.setValue(maxBlockDB);
        });
    }

    @Override
    public void sendEventDecrementCurrentBlock() {
        Platform.runLater(() -> Bindings.currentDBBlock.subtract(1));
    }

    @Override
    public void sendEventRenormalizedPeer(List<PeerService.NetStats> list) {
        Platform.runLater(() -> Network.observableList.setAll(list));
    }

    @Override
    public void sendEventMemoryLog(String log) {
        Platform.runLater(() -> Bindings.memoryLogMessage.setValue(log));
    }

    @Override
    public boolean isIndexing() {
        return Bindings.isIndexing.get();
    }
}