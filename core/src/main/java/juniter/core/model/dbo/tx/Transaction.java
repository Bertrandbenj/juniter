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

    private Long locktime;

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
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "txId", referencedColumnName = "id")
    private List<SourceInput> inputs = new ArrayList<>();

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
    public List<String> issuers() {
        return issuers;
    }
}
