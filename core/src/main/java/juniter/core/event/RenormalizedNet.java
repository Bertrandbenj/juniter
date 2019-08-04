package juniter.core.event;

import juniter.core.event.CoreEvent;
import juniter.service.bma.PeerService;

import java.util.List;


public class RenormalizedNet extends CoreEvent<List<PeerService.NetStats>> {

    public RenormalizedNet(List<PeerService.NetStats> list ) {
        super(list, "RenormalizedNet : ");
        name = getClass().getSimpleName();
    }
}
