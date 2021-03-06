package juniter.core.event;

import juniter.core.model.dbo.net.NetStats;

import java.util.List;


public class RenormalizedNet extends CoreEvent<List<NetStats>> {

    public RenormalizedNet(List<NetStats> list) {
        super(list, "");
        name = getClass().getSimpleName();
    }
}
