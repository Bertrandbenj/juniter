package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.DenormalizeWrittenStamp;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wot_certification", schema = "public", indexes = {
        @Index(columnList = "certifier"),
        @Index(columnList = "certified"),
        @Index(columnList = "writtenOn")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Comparable<Certification>, DenormalizeWrittenStamp {

    private static final Logger LOG = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String certifier;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String certified;

    private Integer signedOn;

    @Size(max = 88)
    private String signature;

    private Integer writtenOn;

    private String writtenHash;

    private Long writtenTime;


    public Certification(String certif) {
        LOG.debug("Parsing certif ... " + certif);

        final var it = certif.split(":");
        certifier = it[0];
        certified = it[1];
        signedOn = Integer.valueOf(it[2]);
        signature = it[3];
    }

    @Override
    public int compareTo(Certification o) {
        return (certifier + " " + certified).compareTo(o.certifier + " " + o.certified);
    }

    public String toDUP() {
        return certifier + ":" + certified + ":" + signedOn + ":" + signature;
    }

    @Override
    public String toString() {
        return toDUP();
    }


}
