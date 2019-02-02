package juniter.core.model.wot;

import juniter.core.model.BStamp;
import juniter.core.model.DUPComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Joiner implements DUPComponent, Serializable, Comparable<Joiner> {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 4413010134970991059L;


    private String pubkey;

    private String signature;

    private String createdOn;

    private String i_block_uid;

    private String uid;


    public Joiner(String joiner) {
        LOG.debug("Parsing Joiner... " + joiner);
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        createdOn = vals[2];
        i_block_uid = vals[3];
        uid = vals[4];
    }


    @Override
    public int compareTo(@NonNull Joiner o) {
        return pubkey.compareTo(o.pubkey);
    }



    public BStamp createdOn() {
        return new BStamp(createdOn);
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + signature + ":" + createdOn + ":" + i_block_uid + ":" + uid;
    }

    @Override
    public String toString() {
        return toDUP();
    }

}
