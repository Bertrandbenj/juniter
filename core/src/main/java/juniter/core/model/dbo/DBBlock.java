package juniter.core.model.dbo;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Counted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.*;
import juniter.core.model.meta.DUPBlock;
import juniter.core.utils.Constants;
import juniter.core.validation.meta.BlockConstraint;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The top / main class of the model is the Block class
 * <p>
 * Here are some note regarding annotations :
 *
 * <pre>
 *
 * 	&#64;Valid  : is useful to enforce encapsulated object validation
 *
 * &#64;AttributeOverride(name = "pubkey", column = @Column(name = "issuer"))
 *
 * private PubKey issuer = new PubKey(); The constructor at initialization is needed by the json parser
 *
 * </pre>
 *
 * @author ben
 */
@Entity
@Data
@Table(name = "block", schema = "public", indexes = {
        @Index(columnList = "number"),
        @Index(columnList = "hash"),
        @Index(columnList = "dividend"),
        @Index(columnList = "size"),
        @Index(columnList = "medianTime")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"number", "hash"})
})
@BlockConstraint
@Valid
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBBlock implements DUPBlock, Serializable {

    private static final long serialVersionUID = -4464417074968456696L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @PositiveOrZero
    private Integer number;


    @Size(max = 64)
    @Column(length = 64)
    private String hash;

    @Min(10)
    @Max(100)
    private Short version;

    private Long nonce;

    @JsonIgnore
    private Integer size;

    private Integer powMin;

    @Positive
    private Long time;

    @Positive
    private Long medianTime;

    @Positive
    private Integer membersCount;

    private Long monetaryMass;

    @Min(0)
    @Max(1000)
    private Integer unitbase;

    private Integer issuersCount;

    private Integer issuersFrame;

    private Integer issuersFrameVar;

    @Size(max = 42)
    @Pattern(regexp = Constants.Regex.CURRENCY)
    private String currency;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String issuer;

    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Nullable
    private ChainParameters parameters;


    @Column(length = 64, name = "previous_hash")
    //@Pattern(regexp = Constants.Regex.HASH)
    private String previousHash;


    @Pattern(regexp = Constants.Regex.PUBKEY)
    @Column(length = 45, name = "previous_issuer")
    private String previousIssuer;


    @Column(length = 64)
    //@Pattern(regexp = Constants.Regex.HASH)
    private String inner_hash;

    /**
     * Quantitative representation of the daily UD in ḡ1 cents <br>
     * ex : UD=1, g1=10.02, dividend=1002
     */
    private Integer dividend;

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Identity> identities = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Joiner> joiners = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Renew> renewed = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Leaver> leavers = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Revoked> revoked = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Excluded> excluded = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Certification> certifications = new ArrayList<>();


    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Member> members = new ArrayList<>();


    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "writtenId", referencedColumnName = "id")
    private List<Transaction> transactions = new ArrayList<>();

    public void setParameters(String string) {
        if (string == null || string.equals(""))
            return;
        parameters = new ChainParameters();
        parameters.accept(string);
    }

    public void setParameters(ChainParameters string) {
        parameters = string;
    }

    public ChainParameters params() {
        return parameters;
    }

    public String getParameter () {
        return (getParameters() != null && getNumber() == 0) ? params().toDUP() : null;
    }


    /**
     * alias
     *
     * @return the Renewed alias Actives
     */
    public List<Renew> getActives() {
        return getRenewed();
    }


    /**
     * Here we hack the getter to keep Timestamp display as a number
     *
     * @return the medianTime
     */
    @CachedGauge(name = "getMedianTime", timeout = 30, timeoutUnit = TimeUnit.SECONDS)
    public Long getMedianTime() {
        return medianTime;
    }

    /**
     * @return the membersCount
     */
    @Counted
    @CachedGauge(name = "getMembersCount", timeout = 30, timeoutUnit = TimeUnit.SECONDS)
    public Integer getMembersCount() {
        return membersCount;
    }


    /**
     * The only occasion previousBlock may be Null is if we are dealing with the
     * first node then Here we hack the getter to avoid Nesting the Json struct
     *
     * @return the previousHash
     */
    public String getPreviousHash() {
        if (number == 0) //
            return null;
        return previousHash;
    }


    /**
     * The only occasion previousBlock may be Null is if we are dealing with the
     * first node
     * <p>
     * then Here we hack the getter to avoid Nesting the Json struct
     *
     * @return the previousIssuer
     */
    public String getPreviousIssuer() {
        if (number == 0)
            return null;
        return previousIssuer;
    }

    public String getRaw() {
        return toDUP(false, true);
    }


    /**
     * <pre>
     * BlockSize
     *
     * The node getSize is defined as the number of lines in multiline fields (Identities, Joiners, Actives, Leaver, Revoked, Certifications, Transactions) except Excluded field.
     *
     * For example:
     *
     *
     * 1 new identity + 1 joiner + 2 certifications = 4 lines sized node
     * 1 new identity + 1 joiner + 2 certifications + 5 lines transaction = 9 lines sized node
     * </pre>
     *
     * @return getSize of the DBBlock
     */
    public Integer getSize() {

        if (size == null) {
            size = identities.size() + joiners.size() + renewed.size() + leavers.size() + revoked.size()
                    + certifications.size() + transactions.size();
        }

        return size;
    }

    public String bstamp() {
        return getNumber() + "-" + getHash();
    }

    public BStamp bStamp() {
        return new BStamp(getNumber(), getHash(), getMedianTime());
    }

    @Override
    public String toDUP() {
        return toDUP(true, true);
    }

    @Override
    public String toDUPdoc(boolean signed) {
        return null;
    }




    /**
     * Format as follow Block [id=... , buid=... , hash=... , ... ]
     * <p>
     * return a string representation of the object
     */
    @Override
    public String toString() {
        return "Block [" + version + ", " + number + ", " + currency + "]";
    }
}
