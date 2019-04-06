package juniter;

import juniter.core.event.CoreEventBus;
import juniter.core.event.GenericSpringEvent;
import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.naming.Binding;
import java.util.List;

@Component
public class JuniterEventListener implements CoreEventBus {


    Logger LOG = LogManager.getLogger();


    @EventListener(condition = "#event.success")
    public void handleSuccessful(GenericSpringEvent<String> event) {
        System.out.println("Handling generic event (conditional).");
    }

    @Override
    public void sendEventCurrentBindex(int i) {

    }

    @Override
    public void sendEventIndexLogMessage(String s  ) {

        LOG.info(s);
    }

    @Override
    public void sendEventPeerLogMessage(String s) {

    }

    @Override
    public void sendEventIsIndexing(boolean is) {

    }

    @Override
    public void sendEventCurrent(long x) {

    }

    @Override
    public void sendEventSetMaxDBBlock(int maxBlockDB) {

    }

    @Override
    public void sendEventSetMaxPeerBlock(long maxBlockDB) {

    }

    @Override
    public void sendEventDecrementCurrentBlock() {

    }

    @Override
    public void sendEventRenormalizedPeer(List<PeerService.NetStats> list) {

    }

    @Override
    public void sendEventMemoryLog(String log) {

    }

    @Override
    public boolean isIndexing(){
        return true;
    }
}