package juniter.core;

import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public interface CoreEventBus {

     Logger LOG = LogManager.getLogger();


    default void sendEventCurrentBindex(int i){
        // Platform.runLater(() -> Bus.currentBindex.setValue(finali));
    }

    default void sendEventIndexLogMessage(String s ){
        LOG.info(s);
        //  Platform.runLater(() -> Bus.indexLogMessage.setValue(s));
    }

    default void sendEventPeerLogMessage(String s ){
        LOG.info(s);
        //  Platform.runLater(() -> Bus.indexLogMessage.setValue(s));
    }


    default void sendEventIsIndexing(boolean is){
        // Platform.runLater(() -> Bus.currentBindex.setValue(finali));
    }


    default void sendEventCurrentAndMax(long x, int y){
//        Platform.runLater(() -> {
//            Bus.currentDBBlock.setValue(x);
//            Bus.maxDBBlock.setValue(currentNumber);
//        });

    }

    default void sendEventSetMaxDBBlock(int maxBlockDB ){
        //us.maxDBBlock.setValue(maxBlockDB);

    }


    default void sendEventSetMaxPeerBlock(long maxBlockDB ){
        //us.maxDBBlock.setValue(maxBlockDB);

    }

    default void sendEventDecrementCurrentBlock(){
       // Platform.runLater(() -> Bus.currentDBBlock.subtract(1));
    }

    default void sendEventRenormalizedPeer(List<PeerService.NetStats> list){
        //     Network.observableList.setAll(
    }

    default void sendEventMemoryLog(String log ){
        //Platform.runLater(()->Bus.memoryLogMessage.setValue());
        LOG.info(log);
    }

}
