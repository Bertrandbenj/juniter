package juniter.core.model.dbo.tx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.meta.DUPTransaction;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction", schema = "public", indexes = {
        @Index(columnList = "blockstamp_number"),
        @Index(columnList = "blockstamp_hash"),
        @Index(columnList = "hash"),
        @Index(columnList = "written_hash"),
        @Index(columnList = "written_number"),
        @Index(columnList = "comment")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements DUPTransaction, Serializable {

    private static final Logger LOG = LogManager.getLogger(Transaction.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(10)
    @Max(100)
    private Short version;


    @Size(max = 42)
    private String currency;

    private Integer locktime;

    @Valid
    @Size(max = 64)
    private String hash;

    @Valid
    private BStamp blockstamp;

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "tx_issuer", joinColumns = @JoinColumn(name = "tx_id"))
    private List<@Size(max = 45) @Pattern(regexp = Constants.Regex.PUBKEY)
            String> issuers = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "tx_input", joinColumns = @JoinColumn(name = "tx_id"))
    private List<TxInput> inputs = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "tx_output", joinColumns = @JoinColumn(name = "tx_id"))
    private List<TxOutput> outputs = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "tx_unlock", joinColumns = @JoinColumn(name = "tx_id"))
    private List<TxUnlock> unlocks = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "tx_signature", joinColumns = @JoinColumn(name = "tx_id"))
    private List<@Size(max = 88) @Pattern(regexp = Constants.Regex.SIGNATURE)
            String> signatures = new ArrayList<>();


    @Size(max = 255)
    private String comment;

    private BStamp written;


    public String getHash() {
        if (hash == null || "".equals(hash)) {
            hash = Crypto.hash(toDUPdoc(true));
        }
        return hash;
    }

    @Override
    public String toDUP() {
        return toDUPdoc(true);
    }

    /**
     * <pre>
     * ex :
     * Version: 10
     * Type: Transaction
     * Currency: g1
     * Blockstamp: 12345
     * Locktime: 98765
     * Issuers:
     * HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
     * GgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
     * Inputs:
     * 25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:0
     * 25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:65
     * Unlocks:
     * 0:SIG(1)
     * 0:XHX(1)
     * Outputs:
     * 50:2:(SIG(HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd) || (SIG(DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV) && XHX(309BC5E644F797F53E5A2065EAF38A173437F2E6)))
     * 50:2:XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)
     * Signatures:
     * DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
     * DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
     * Comment: huhuhaha
     */
    public String toDUPdoc(boolean signed) {
        return "Version: " + version +
                "\nType: Transaction" +
                "\nCurrency: g1" +
                "\nBlockstamp: " + this.blockstamp.stamp() +
                "\nLocktime: " + locktime +
                "\nIssuers:\n" + String.join("\n", issuers) +
                "\nInputs:\n" + inputs.stream().map(TxInput::toDUP).collect(Collectors.joining("\n")) +
                "\nUnlocks:\n" + unlocks.stream().map(TxUnlock::toDUP).collect(Collectors.joining("\n")) +
                "\nOutputs:\n" + outputs.stream().map(TxOutput::toDUP).collect(Collectors.joining("\n")) +
                "\nComment: " + comment + "\n" +
                (signed ? String.join("\n", signatures) + "\n" : "")

                ;
    }



    /**
     * <pre>
     * TX:VERSION:NB_ISSUERS:NB_INPUTS:NB_UNLOCKS:NB_OUTPUTS:HAS_COMMENT:LOCKTIME
     *
     * ex : Transactions:\nTX:10:1:1:1:2:0:0
     * 127129-00000232C91EF53648DA67D5DA32DA54C766238B48C512F66C7CC769585DFCBE
     * 8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e
     * 9506:0:T:97A239CA02FA2F97B859C2EA093FE68FEADF90A1FDE8EE69711C2048BD328128:1
     * 0:SIG(0)
     * 1000:0:SIG(CCdjH7Pd8GPe74ZbiD1DdZ1CXQ2ggYVehk2c7iVV6NwJ)
     * 8506:0:SIG(8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e)
     * EP9BhAMIbDSy9nfplSmmvp7yI6t79kO0/7/bdecGjayH+hrZxT2R4xkpEVyV3qo6Ztc1TwK+F2Hf2big5pVrCA==
     * </pre>
     */
    public String toDUPshort() {

        final var hasComment = comment != null && !comment.equals("");

        return "TX:" + version + ":" + issuers.size() + ":" + inputs.size() + ":" + unlocks.size() + ":"
                + outputs.size() + ":" + (hasComment ? 1 : 0) + ":" + locktime + "\n" + blockstamp.stamp() + "\n"
                + String.join("\n", issuers) + "\n"
                + inputs.stream().map(TxInput::toDUP).collect(Collectors.joining("\n")) + "\n" //
                + unlocks.stream().map(TxUnlock::toDUP).collect(Collectors.joining("\n")) + "\n"
                + outputs.stream().map(TxOutput::toDUP).collect(Collectors.joining("\n")) + "\n"
                + (hasComment ? comment + "\n" : "")
                + String.join("\n", signatures);
    }


    @Override
    public List<String> issuers() {
        return issuers;
    }
}
