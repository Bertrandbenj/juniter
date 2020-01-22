package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPComponent;
import juniter.core.model.meta.DUPUnlock;
import juniter.core.model.meta.LockType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Embeddable
public class TxUnlock implements DUPUnlock,  DUPComponent {

    private static final Logger LOG = LogManager.getLogger(TxUnlock.class);

    private Integer inputRef;

    @Enumerated(EnumType.ORDINAL)
    private LockType fct;

    @Size(max = 255)
    private String param;


    public TxUnlock(Integer inputRef, LockType fct, String fctParam){
        this.inputRef=inputRef;
        this.fct=fct;
        this.param=fctParam;
    }

    public TxUnlock(String unlock) {
        try {
            final var vals = unlock.split(":");
            inputRef = Integer.valueOf(vals[0]);
            final var function = vals[1];
            fct = LockType.valueOf(function.substring(0, 3));
            param = function.substring(4, function.length() - 1);
        } catch (Exception e) {
            LOG.error("parsing TxUnlock " + unlock, e);
        }

    }



    @Override
    public String toString() {
        return toDUP();
    }



}
