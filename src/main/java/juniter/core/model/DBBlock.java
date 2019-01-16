package juniter.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ryantenney.metrics.annotation.CachedGauge;
import com.ryantenney.metrics.annotation.Counted;
import juniter.core.model.tx.Transaction;
import juniter.core.model.wot.*;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@Table(name = "block", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(BStamp.class)
public class DBBlock implements Serializable, DUPComponent {

    private static final long serialVersionUID = -4464417074968456696L;

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
    //@Pattern(regexp = Constants.Regex.HASH)
    protected String hash;

    private String parameters;

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
    @ElementCollection
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Identity> identities = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_joiners", joinColumns = {@JoinColumn(name = "number"), @JoinColumn(name = "hash")})
    private List<Joiner> joiners = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_renewed", joinColumns = {@JoinColumn(name = "number"), @JoinColumn(name = "hash")})
    private List<Renew> renewed = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_leavers", joinColumns = {@JoinColumn(name = "number"), @JoinColumn(name = "hash")})
    private List<Leaver> leavers = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name = "wot_revoked", joinColumns = {@JoinColumn(name = "number"), @JoinColumn(name = "hash")})
    private List<Revoked> revoked = new ArrayList<>();

    @Valid
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "wot_excluded", joinColumns = {@JoinColumn(name = "number"), @JoinColumn(name = "hash")})
    private List<Excluded> excluded = new ArrayList<>();

    @Valid
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications = new ArrayList<>();

    @Valid
    @OrderColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns(value = {@JoinColumn(name = "Bnumber", referencedColumnName = "number"),
            @JoinColumn(name = "Bhash", referencedColumnName = "hash")})
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Empty constructors
     */
    public DBBlock() {
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
     * @return the certifications
     */
    public List<Certification> getCertifications() {
        return certifications;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return the dividend
     */
    public Integer getDividend() {
        return dividend;
    }

    /**
     * @return the excluded
     */
    public List<Excluded> getExcluded() {
        return excluded; // .stream().map(Excluded::toString).collect(Collectors.toList());
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the identities
     */
    public List<Identity> getIdentities() {
        return identities;
    }


    /**
     * @return the inner_hash
     */
    public String getInner_hash() {
        return inner_hash;
    }

    /**
     * Here we hack the getter to avoid Nesting the Json struct
     *
     * @return the issuer's pubKey
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * @return the issuersCount
     */
    public Integer getIssuersCount() {
        return issuersCount;
    }

    /**
     * @return the issuersFrame
     */
    public Integer getIssuersFrame() {
        return issuersFrame;
    }

    /**
     * @return the issuersFrameVar
     */
    public Integer getIssuersFrameVar() {
        return issuersFrameVar;
    }

    /**
     * @return the joiners
     */
    public List<Joiner> getJoiners() {
        return joiners;// .stream().map(Joiner::toString).collect(Collectors.toList());
    }

    /**
     * @return the leavers
     */
    public List<Leaver> getLeavers() {
        return leavers; // .stream().map(Leaver::toString).collect(Collectors.toList());
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
     * @return the monetaryMass
     */
    public Long getMonetaryMass() {
        return monetaryMass;
    }

    /**
     * @return the nonce
     */
    public Long getNonce() {
        return nonce;
    }

    /**
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return the parameters
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @return the powMin
     */
    public Integer getPowMin() {
        return powMin;
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

    /**
     * @return the revoked
     */
    public List<Revoked> getRevoked() {
        return revoked;
    }


    public List<Renew> getRenewed() {
        return renewed;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Here we hack the getter to keep Timestamp display as a number
     *
     * @return the time
     */
    public Long getTime() {
        return time;
    }

    /**
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * @return the unitbase
     */
    public Integer getUnitbase() {
        return unitbase;
    }

    /**
     * @return the version
     */
    public Short getVersion() {
        return version;
    }

    public void setActives(List<Renew> renewed) {
        this.renewed = renewed;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDividend(Integer dividend) {
        this.dividend = dividend;
    }

    public void setExcluded(List<Excluded> excluded) {
        this.excluded = excluded;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setIdentities(List<Identity> identities) {
        this.identities = identities;
    }

    public void setIssuersCount(Integer issuersCount) {
        this.issuersCount = issuersCount;
    }

    public void setIssuersFrame(Integer issuersFrame) {
        this.issuersFrame = issuersFrame;
    }

    public void setIssuersFrameVar(Integer issuersFrameVar) {
        this.issuersFrameVar = issuersFrameVar;
    }

    public void setJoiners(List<Joiner> joiners) {
        this.joiners = joiners;
    }

    public void setLeavers(List<Leaver> leavers) {
        this.leavers = leavers;
    }

    public void setMedianTime(Long medianTime) {
        this.medianTime = medianTime;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    public void setMonetaryMass(Long monetaryMass) {
        this.monetaryMass = monetaryMass;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public void setPowMin(Integer powMin) {
        this.powMin = powMin;
    }


    public void setRevoked(List<Revoked> revoked) {
        this.revoked = revoked;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setIssuer(String iss) {
        issuer = iss;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setUnitbase(Integer unitbase) {
        this.unitbase = unitbase;
    }

    public void setVersion(Short version) {
        this.version = version;
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
            paramOrPrevious += "\nParameters: " + parameters;// 0.0488:86400:1000:432000:100:5259600:63115200:5:5259600:5259600:0.8:31557600:5:24:300:12:0.67:1488970800:1490094000:15778800";
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
                + identities.stream().map(Identity::toString).collect(Collectors.joining("\n"))
                + (identities.size() > 0 ? "\n" : "") + "Joiners:\n"
                + joiners.stream().map(Joiner::toDUP).collect(Collectors.joining("\n"))
                + (joiners.size() > 0 ? "\n" : "") + "Actives:\n"
                + renewed.stream().map(Renew::toDUP).collect(Collectors.joining("\n"))
                + (renewed.size() > 0 ? "\n" : "") + "Leavers:\n"
                + leavers.stream().map(Leaver::toDUP).collect(Collectors.joining("\n"))
                + (leavers.size() > 0 ? "\n" : "") + "Revoked:\n"
                + revoked.stream().map(Revoked::toDUP).collect(Collectors.joining("\n"))
                + (revoked.size() > 0 ? "\n" : "") + "Excluded:\n"
                + excluded.stream().map(Excluded::toDUP).collect(Collectors.joining("\n"))
                + (excluded.size() > 0 ? "\n" : "") + "Certifications:\n"
                + certifications.stream().map(Certification::toDUP).collect(Collectors.joining("\n"))
                + (certifications.size() > 0 ? "\n" : "") + "Transactions:\n"
                + transactions.stream().map(tx -> tx.toDUPshort(true)).collect(Collectors.joining("\n"))
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
