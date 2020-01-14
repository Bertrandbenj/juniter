package juniter.core.model.dbo.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxOutput;
import juniter.core.model.dbo.tx.TxUnlock;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "sb_transaction", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionSandboxed implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(10)
    @Max(100)
    private Integer version;


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


}
