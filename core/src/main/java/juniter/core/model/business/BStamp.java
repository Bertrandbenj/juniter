package juniter.core.model.business;

import juniter.core.utils.Constants;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 *
 * @author ben
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BStamp implements Serializable, Comparable<BStamp> {

    private static final long serialVersionUID = -165962007943111454L;
    private static final Logger LOG = LogManager.getLogger();

    @Min(0)
    @Column(name = "number")
    private Integer number;

    @Pattern(regexp = Constants.Regex.HASH)
    @Column(length = 64)
    @Size(max = 64)
    private  String hash;


    public BStamp(String string) {
        final String[] pat = string.split("-");
        number = Integer.valueOf(pat[0]);
        hash = pat[1];
    }

    @Override
    public String toString() {
        return number + "-" + hash;
    }


    @Override
    public int compareTo(BStamp o) {
        var cmpNum = number.compareTo(o.number);
        return cmpNum == 0 ? hash.compareTo(o.hash) : cmpNum;
    }
}