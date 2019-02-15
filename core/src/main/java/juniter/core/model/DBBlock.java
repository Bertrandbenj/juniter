package juniter.core.model;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Counted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.business.BStamp;
import juniter.core.model.business.ChainParameters;
import juniter.core.model.business.DUPComponent;
import juniter.core.model.business.tx.Transaction;
import juniter.core.model.wot.*;
import juniter.core.utils.Constants;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
@NoArgsConstructor
@Table(name = "block", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(BStamp.class)
public class DBBlock implements Serializable, DUPComponent {

    private static final long serialVersionUID = -4464417074968456696L;

    @Min(10)
    @Max(100)
    private Short version;

    private Long nonce;

    @Id
    private Integer number;

    private Integer powMin;

    private Long time;

    private Long medianTime;

    private Integer membersCount;

    private Long monetaryMass;

    @Min(0)
    @Max(0)
    private Integer unitbase;

    private Integer issuersCount;

    private Integer issuersFrame;

    private Integer issuersFrameVar;

    @Size(max = 42)
    @Pattern(regexp = Constants.Regex.G1)
    private String currency;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String issuer;

    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;

    @Id
    @Size(max = 64)
    @Column(length = 64)
    protected String hash;


    //@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)

    @JsonIgnore
    transient private ChainParameters parameters = new ChainParameters();

    @Size(max = 64)
    @Column(length = 64, name = "previous_hash")
    //@Pattern(regexp = Constants.Regex.HASH)
    private String previousHash;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    @Column(length = 45, name = "previous_issuer")
    private String previousIssuer;

    @Size(max = 64)
    @Column(length = 64)
    //@Pattern(regexp = Constants.Regex.HASH)
    private String inner_hash;

    /**
     * Quantitative representation of the daily UD in ḡ1 cents <br>
     * ex : UD=1, g1=10.02, dividend=1002
     */
    private Integer dividend;

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    //@ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns(value = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Identity> identities = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_joiners", joinColumns = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Join> joiners = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_renewed", joinColumns = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Activ> renewed = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_leavers", joinColumns = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Leavers> leavers = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_revoked", joinColumns = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Revok> revoked = new ArrayList<>();

    @Valid
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "wot_excluded", joinColumns = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Excl> excluded = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns(value = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Certification> certifications = new ArrayList<>();


    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns(value = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Member> members = new ArrayList<>();


    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns(value = {@JoinColumn(name = "writtenOn", referencedColumnName = "number"),
            @JoinColumn(name = "writtenHash", referencedColumnName = "hash")})
    private List<Transaction> transactions = new ArrayList<>();


//    public void setActives(List<String> actives) {
//        members.addAll(actives.stream()
//                .map(Activ::new)
//                .collect(Collectors.toList()));
//    }
//
//    public void setJoiners(List<String> actives) {
//        members.addAll(actives.stream()
//                .map(Join::new)
//                .collect(Collectors.toList()));
//    }
//
//    public void setLeavers(List<String> actives) {
//        members.addAll(actives.stream()
//                .map(Leavers::new)
//                .collect(Collectors.toList()));
//    }
//
//    public void setRevoked(List<String> actives) {
//        members.addAll(actives.stream()
//                .map(Revok::new)
//                .collect(Collectors.toList()));
//    }
//    public void setExcluded(List<String> actives) {
//        members.addAll(actives.stream()
//                .map(Revok::new)
//                .collect(Collectors.toList()));
//    }

    public void setParameters(String string) {
        parameters.accept(string);
    }

    public String getParameters(){
        return number==0?parameters.toDUP():null;
    }



    /**
     * alias
     *
     * @return the Renewed alias Actives
     */
    public List<Activ> getActives() {
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
     * first block then Here we hack the getter to avoid Nesting the Json struct
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
     * first block
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


    public String signedPart() {
        return "InnerHash: " + inner_hash + "\n" +
                "Nonce: " + nonce + "\n";
    }

    public String signedPartSigned() {
        return signedPart() + signature + "\n";
    }

    /**
     * <pre>
     * BlockSize
     *
     * The block size is defined as the number of lines in multiline fields (Identities, Joiners, Actives, Leavers, Revoked, Certifications, Transactions) except Excluded field.
     *
     * For example:
     *
     *
     * 1 new identity + 1 joiner + 2 certifications = 4 lines sized block
     * 1 new identity + 1 joiner + 2 certifications + 5 lines transaction = 9 lines sized block
     * </pre>
     *
     * @return size of the DBBlock
     */
    public Integer size() {

        return identities.size() + joiners.size() + renewed.size() + leavers.size() + revoked.size()
                + certifications.size() + transactions.size();
    }

    @CachedGauge(name = "queueSize", timeout = 30, timeoutUnit = TimeUnit.SECONDS)
    public String bstamp() {
        return getNumber() + "-" + getHash();
    }

    public BStamp bStamp() {
        return new BStamp(getNumber(), getHash());
    }

    @Override
    public String toDUP() {
        return toDUP(true, true);
    }

    /**
     * Method returning block as a Raw format
     *
     * @return the DUP as text format
     */
    public String toDUP(boolean signed, boolean innerHash) {


        final String div = dividend != null && dividend >= 1000 ? "\nUniversalDividend: " + dividend : "";

        String paramOrPrevious = "";
        if (number.equals(0)) {
            paramOrPrevious += "\nParameters: " + parameters.toDUP();
        } else {
            paramOrPrevious += "\nPreviousHash: " + previousHash;
            paramOrPrevious += "\nPreviousIssuer: " + previousIssuer;
        }

        return "Version: " + version +
                "\nType: Block" +
                "\nCurrency: " + currency +
                "\nNumber: " + number +
                "\nPoWMin: " + powMin +
                "\nTime: " + getTime() +
                "\nMedianTime: " + getMedianTime() +
                div +
                "\nUnitBase: " + unitbase +
                "\nIssuer: " + issuer +
                "\nIssuersFrame: " + issuersFrame +
                "\nIssuersFrameVar: " + issuersFrameVar +
                "\nDifferentIssuersCount: " + issuersCount +
                paramOrPrevious +
                "\nMembersCount: " + membersCount +
                "\nIdentities:\n"
                + identities.stream().map(Identity::toDUP).collect(Collectors.joining("\n"))
                + (identities.size() > 0 ? "\n" : "") +
                "Joiners:\n"
                + joiners.stream().map(Join::toDUP).collect(Collectors.joining("\n"))
                + (joiners.size() > 0 ? "\n" : "") +
                "Actives:\n"
                + renewed.stream().map(Activ::toDUP).collect(Collectors.joining("\n"))
                + (renewed.size() > 0 ? "\n" : "") +
                "Leavers:\n"
                + leavers.stream().map(Leavers::toDUP).collect(Collectors.joining("\n"))
                + (leavers.size() > 0 ? "\n" : "") +
                "Revoked:\n"
                + revoked.stream().map(Revok::toDUP).collect(Collectors.joining("\n"))
                + (revoked.size() > 0 ? "\n" : "") +
                "Excluded:\n"
                + excluded.stream().map(Excl::toDUP).collect(Collectors.joining("\n"))
                + (excluded.size() > 0 ? "\n" : "") +
                "Certifications:\n"
                + certifications.stream().map(Certification::toDUP).collect(Collectors.joining("\n"))
                + (certifications.size() > 0 ? "\n" : "") +
                "Transactions:\n"
                + transactions.stream().map(Transaction::toDUPshort).collect(Collectors.joining("\n"))
                + (transactions.size() > 0 ? "\n" : "") //
                + (innerHash ? "InnerHash: " + inner_hash : "")
                + (signed || innerHash ? "\nNonce: " + nonce + "\n" : "")
                + (signed ? signature : "");
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
