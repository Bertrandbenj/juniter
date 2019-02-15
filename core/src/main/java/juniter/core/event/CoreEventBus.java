package juniter.core.event;

import juniter.service.bma.PeerService;

import java.util.List;

public interface CoreEventBus {

     //Logger LOG = LogManager.getLogger();


    void sendEventCurrentBindex(int i);


    void sendEventIndexLogMessage(String s);



    void sendEventPeerLogMessage(String s );


    void sendEventIsIndexing(boolean is);



     void sendEventCurrentAndMax(long x, int y);




     void sendEventSetMaxDBBlock(int maxBlockDB );




     void sendEventSetMaxPeerBlock(long maxBlockDB );



     void sendEventDecrementCurrentBlock();


     void sendEventRenormalizedPeer(List<PeerService.NetStats> list);



     void sendEventMemoryLog(String log );



}
