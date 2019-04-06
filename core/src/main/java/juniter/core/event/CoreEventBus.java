package juniter.core.event;

import juniter.service.bma.PeerService;

import java.util.List;

public interface CoreEventBus {


    void sendEventCurrentBindex(int i);


    void sendEventIndexLogMessage(String s);


    void sendEventPeerLogMessage(String s);


    void sendEventIsIndexing(boolean is);


    void sendEventCurrent(long x);


    void sendEventSetMaxDBBlock(int maxBlockDB);


    void sendEventSetMaxPeerBlock(long maxBlockDB);


    void sendEventDecrementCurrentBlock();


    void sendEventRenormalizedPeer(List<PeerService.NetStats> list);


    void sendEventMemoryLog(String log);


    boolean isIndexing();
}
