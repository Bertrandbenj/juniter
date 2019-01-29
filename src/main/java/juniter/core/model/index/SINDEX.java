package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "SINDEX", schema = "public")//,uniqueConstraints={@UniqueConstraint(columnNames={"op","",""})}) // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
public class SINDEX implements Serializable {

    private static final long serialVersionUID = -6400219827778830671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Business variables

    private String op;
    private String tx;
    private String identifier;
    private Integer pos;
    private String created_on;
    private String written_on;
    private Long written_time;
    private int amount;
    private int base;
    private Long locktime;
    private boolean consumed;
    private String conditions;
    private Integer writtenOn;

    public juniter.service.gva.tx.Source asSourceGVA(){
        return new juniter.service.gva.tx.Source(
                tx == null ? "D":"T",
                pos,
                identifier,
                amount,
                base,
                conditions,
                consumed);
    }

    /**
     * return available sources
     * @return
     */
    public juniter.core.model.dto.Source asSourceBMA(){
        return new juniter.core.model.dto.Source(
                tx == null ? "D":"T",
                pos,
                identifier,
                amount,
                base,
                conditions);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getWritten_on() {
        return written_on;
    }

    public int getAmount() {
        return amount;
    }

    public int getBase() {
        return base;
    }

    public String getConditions() {
        return conditions;
    }

    public int getPos() {
        return pos;
    }
}