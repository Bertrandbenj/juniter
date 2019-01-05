package juniter.repository.jpa.index;

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
@Table(name = "SINDEX", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
public class SINDEX implements Serializable {

    private static final long serialVersionUID = -6400219827778830671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    String op;
    String tx;
    String identifier;
    Integer pos;
    String created_on;
    String written_on;
    Long written_time;
    int amount;
    int base;
    Long locktime;
    boolean consumed;
    String conditions;
    Integer writtenOn;

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
    public juniter.service.bma.dto.Source asSourceBMA(){
        return new juniter.service.bma.dto.Source(
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