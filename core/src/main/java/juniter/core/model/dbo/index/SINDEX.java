package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.service.gva.Source;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@ToString
@Entity
@Table(name = "SINDEX", schema = "public", indexes = {
        @Index(name="ind_shash",columnList = "identifier"),
        @Index(name="ind_sconsumed",columnList = "consumed"),
        @Index(name="ind_sconditions",columnList = "conditions"),
        @Index(name="ind_swritten_on",columnList = "written_on"),
        @Index(name="ind_swrittenOn",columnList = "writtenOn"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SINDEX  {


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

    public Source asSourceGVA(){
        return new Source(
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