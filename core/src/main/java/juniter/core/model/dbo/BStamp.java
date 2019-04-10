package juniter.core.model.dbo;

import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
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


    private Integer number;

    @Pattern(regexp = Constants.Regex.HASH)
    private String hash;

    private Long medianTime;


    public BStamp(String string) {
        final String[] pat = string.split("-");
        number = Integer.valueOf(pat[0]);
        hash = pat[1];
    }

    public BStamp(Integer n, String string) {
        number = n;
        hash = string;
    }

    @Override
    public String toString() {
        return number + "-" + hash;
    }

    public String stamp() {
        return number + "-" + hash;
    }


    @Override
    public int compareTo(BStamp o) {
        var cmpNum = number.compareTo(o.number);
        return cmpNum == 0 ? hash.compareTo(o.hash) : cmpNum;
    }
}