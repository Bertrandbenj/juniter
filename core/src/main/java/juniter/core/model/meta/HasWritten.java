package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;

public interface HasWritten {

    BStamp getWritten();

    void setWritten(BStamp written);

    default String bstamp(){
        return getWritten().stamp();
    }


}
