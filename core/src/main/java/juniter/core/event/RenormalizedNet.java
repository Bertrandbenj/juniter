package juniter.core.event;

import juniter.core.model.dbo.NetStats;

import java.util.List;


public class RenormalizedNet extends CoreEvent<List<NetStats>> {

    public RenormalizedNet(List<NetStats> list) {
        super(list, "RenormalizedNet : ");
        name = getClass().getSimpleName();
    }
}
