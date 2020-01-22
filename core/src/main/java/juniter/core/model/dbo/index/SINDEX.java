package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.meta.SourceType;
import juniter.core.model.gva.Source;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.function.BinaryOperator;

/**
 * <pre>
 * * Sources
 *
 * The local SINDEX has a unicity constraint on UPDATE, IDENTIFIER, POS
 * The local SINDEX ........................... CREATE, ...............
 *
 *
 * * Functionally:
 *
 *
 * a same getSource cannot be consumed twice by the block a same output cannot be
 * a same getSource cannot be consumed twice by the block a same output cannot be
 * produced twice by block
 *
 *
 * But a getSource can be both created and consumed in the same block, so a tree
 * of transactions can be stored at once.
 *
 * 	<h2>Double-spending control</h2>
 * Definitions:
 *
 * For each SINDEX unique tx:
 *  - inputs are the SINDEX row matching UPDATE, tx
 *  - outputs are the SINDEX row matching CREATE, tx
 *
 *
 * Functionally: we gather the sources for each transaction, in order to check them.
 *
 * CommonBase
 *
 * Each input has an InputBase, and each output has an OutputBase. These bases are to be called AmountBase.
 *
 * The CommonBase is the lowest getBase value among all AmountBase of the transaction.
 *
 * For any getAmount comparison, the respective amounts must be translated into CommonBase using the following rule:
 *
 * AMOUNT(CommonBase) = AMOUNT(AmountBase) x POW(10, AmountBase - CommonBase)
 *
 * So if a transaction only carries amounts with the same AmountBase, no conversion is required.
 * But if a transaction carries:
 *
 *   input_0 of value 45 with AmountBase = 5
 *   input_1 of value 75 with AmountBase = 5
 *   input_2 of value 3 with AmountBase = 6
 *   output_0 of value 15 with AmountBase = 6
 *
 * Then the output value has to be converted before being compared:
 *
 * CommonBase = 5
 *
 * output_0(5) = output_0(6) x POW(10, 6 - 5)
 * output_0(5) = output_0(6) x POW(10, 1)
 * output_0(5) = output_0(6) x 10
 * output_0(5) = 15 x 10
 * output_0(5) = 150
 * input_0(5) = input_0(5)
 * input_0(5) = 45
 * input_1(5) = input_1(5)
 * input_1(5) = 75
 * input_2(5) = input_2(6) x POW(10, 6 - 5)
 * input_2(5) = input_2(6) x POW(10, 1)
 * input_2(5) = input_2(6) x 10
 * input_2(5) = 3 x 10
 * input_2(5) = 30
 *
 * The equality of inputs and outputs is then verified because:
 *
 * output_0(5) = 150
 * input_0(5) = 45
 * input_1(5) = 75
 * input_2(5) = 30
 *
 * output_0(5) = input_0(5) + input_1(5) + input_2(5)
 * 150 = 45 + 75 + 30
 * TRUE
 *
 * Amounts
 *
 *
 *
 * Def.: InputBaseSum is the sum of amounts with the same InputBase.
 *
 * Def.: OutputBaseSum is the sum of amounts with the same OutputBase.
 *
 * Def.: BaseDelta = OutputBaseSum - InputBaseSum, expressed in CommonBase
 *
 *
 * Rule: For each OutputBase:
 *
 *
 * if BaseDelta > 0, then it must be inferior or equal to the sum of all preceding BaseDelta
 *
 *
 *
 * Rule: The sum of all inputs in CommonBase must equal the sum of all outputs in CommonBase
 *
 *
 * Functionally: we cannot create nor lose money through transactions. We can only transfer coins we own.
 * Functionally: also, we cannot convert a superiod unit getBase into a lower one.
 *
 *
 * </pre>
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "index_s", schema = "public", indexes = {
        @Index(columnList = "identifier"),
        @Index(columnList = "consumed"),
        @Index(columnList = "conditions"),
        @Index(columnList = "signed_number"),
        @Index(columnList = "written_number"),
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"op", "identifier", "pos"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SINDEX implements Comparable<SINDEX> {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String op;

    private String tx;

    private String identifier;

    private Integer pos;

    private BStamp signed;

    private BStamp written;

    private int amount;

    private int base;

    private Long locktime;

    private boolean consumed;

    public String conditions;


    // Local to the Block we are validating
    public transient long age;

    public transient boolean available;

    public transient boolean isLocked;

    public transient boolean isTimeLocked;


    public SINDEX(String op, String identifier, Integer pos, BStamp signed, BStamp written_on,
                  int amount, int base, Long locktime, String conditions, boolean consumed, String tx) {
        this.op = op;
        this.identifier = identifier;
        this.pos = pos;
        this.signed = signed;
        this.written = written_on;
        this.amount = amount;
        this.base = base;
        this.locktime = locktime;
        this.conditions = conditions;
        this.consumed = consumed;
        this.tx = tx;
    }

    @Override
    public int compareTo(@NonNull SINDEX o) {
        return (op + "-" + identifier + "-" + pos + "-" + written)
                .compareTo(o.op + "-" + o.identifier + "-" + o.pos + "-" + o.written);
    }

    /**
     * Be aware, that function is more essential than it looks, keep it public
     *
     * @return conditions
     */
    public String getConditions() {
        return conditions;
    }


    public SourceType type() {
        return tx == null ? SourceType.D : SourceType.T;

    }

    public Source asSourceGVA() {
        return new Source(
                tx == null ? "D" : "T",
                pos,
                identifier,
                amount,
                base,
                conditions,
                consumed);
    }

    /**
     * return available sources
     *
     * @return
     */
    public juniter.core.model.dto.tx.Source asSourceBMA() {
        return new juniter.core.model.dto.tx.Source(
                tx == null ? "D" : "T",
                pos,
                identifier,
                amount,
                base,
                conditions);
    }

    public static final BinaryOperator<SINDEX> reducer = (m1, m2) -> {

        // var top = m1.written.compareTo(m2.written) > 0 ? m2 : m1;
        //System.out.println("Reducing" + m1 + "\n" + m2);
        SINDEX bot, top;
        if (m1.written.getNumber() < m2.written.getNumber()) {
            top = m1;
            bot = m2;
        } else {
            top = m2;
            bot = m1;
        }


        if (top.getTx() == null)
            top.setTx(bot.getTx());

        if (top.getIdentifier() == null)
            top.setIdentifier(bot.getIdentifier());

        if (top.getPos() == null)
            top.setPos(bot.getPos());

        if (top.getSigned() == null)
            top.setSigned(bot.getSigned());

        if (top.getWritten() == null)
            top.setWritten(bot.getWritten());

        if (top.getAmount() == 0)
            top.setAmount(bot.getAmount());

        if (top.getBase() == 0)
            top.setBase(bot.getBase());

        if (top.getLocktime() == null)
            top.setLocktime(bot.getLocktime());

        if (top.getConditions() == null)
            top.setConditions(bot.getConditions());

        top.setConsumed(bot.consumed);

        return top;

    };
}