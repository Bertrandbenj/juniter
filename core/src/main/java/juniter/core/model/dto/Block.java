package juniter.core.model.dto;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Counted;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {


    private Integer number;


    private String hash;

    private Short version;

    private Long nonce;

    @JsonIgnore
    private Integer size;

    private Integer powMin;

    private Long time;

    private Long medianTime;

    private Integer membersCount;

    private Long monetaryMass;

    private Integer unitbase;

    private Integer issuersCount;

    private Integer issuersFrame;

    private Integer issuersFrameVar;

    private String currency;

    private String issuer;

    private String signature;


    @Nullable
    private String parameters;


    private String previousHash;

    private String previousIssuer;


    private String inner_hash;


    private Integer dividend;


    private List<Identity> identities = new ArrayList<>();


    private List<Joiner> joiners = new ArrayList<>();


    private List<Renew> renewed = new ArrayList<>();

    private List<Leaver> leavers = new ArrayList<>();


    private List<Revoked> revoked = new ArrayList<>();

    private List<Excluded> excluded = new ArrayList<>();

    private List<Certification> certifications = new ArrayList<>();


    private List<Member> members = new ArrayList<>();


    private List<Transaction> transactions = new ArrayList<>();

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

    /**
     * Method returning node as a Raw format
     *
     * @return the DUPComponent as text format
     */
    public String toDUP(boolean signed, boolean innerHash) {


        final String div = dividend != null && dividend >= 1000 ? "\nUniversalDividend: " + dividend : "";

        String paramOrPrevious = "";
        if (number.equals(0)) {
            paramOrPrevious += "\nParameters: " + parameters ;
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
                + joiners.stream().map(Joiner::toDUP).collect(Collectors.joining("\n"))
                + (joiners.size() > 0 ? "\n" : "") +
                "Actives:\n"
                + renewed.stream().map(Renew::toDUP).collect(Collectors.joining("\n"))
                + (renewed.size() > 0 ? "\n" : "") +
                "Leaver:\n"
                + leavers.stream().map(Leaver::toDUP).collect(Collectors.joining("\n"))
                + (leavers.size() > 0 ? "\n" : "") +
                "Revoked:\n"
                + revoked.stream().map(Revoked::toDUP).collect(Collectors.joining("\n"))
                + (revoked.size() > 0 ? "\n" : "") +
                "Excluded:\n"
                + excluded.stream().map(Excluded::toDUP).collect(Collectors.joining("\n"))
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
