package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPInput;
import juniter.core.model.meta.SourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tx_sources", schema = "public" )
public class SourceInput implements DUPInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Positive
    private Integer amount;

    @PositiveOrZero
    private Integer base;

    private String reference;

    @PositiveOrZero
    private Integer index;

    @Enumerated(EnumType.ORDINAL)
    private SourceType type;

    @Override
    public String toString() {
        return toDUP();
    }

    @Override
    public SourceType type() {
        return type;
    }


    /**
     * Build input from String
     *
     * @param input as a DUPComponent String
     */
    public SourceInput (String input) {
        final var it = input.split(":");
        amount = Integer.valueOf(it[0]);
        base = Integer.valueOf(it[1]);
        var type = it[2];
        if (type.equals("D")){
            this.type=SourceType.D;
        }else{
            this.type=SourceType.T;
        }
        reference =  it[3] ;
        index = Integer.valueOf(it[4]);

    }
}
