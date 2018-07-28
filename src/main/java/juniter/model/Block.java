package juniter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.persistence.Hash;
import juniter.model.persistence.PubKey;
import juniter.model.persistence.Signature;
import juniter.model.persistence.tx.Transaction;
import juniter.model.persistence.wot.Active;
import juniter.model.persistence.wot.Certification;
import juniter.model.persistence.wot.Excluded;
import juniter.model.persistence.wot.Identity;
import juniter.model.persistence.wot.Joiner;
import juniter.model.persistence.wot.Leaver;
import juniter.model.persistence.wot.Revoked;
import juniter.utils.Constants;

/**
 * The top / main class of the model is the Block class 
 * 
 * Here are some note regarding annotations : 
 * <pre>
 * 
 * 	&#64;Valid  -> is useful to enforce recursive validation 
 * 
	&#64;AttributeOverride(name = "pubkey", column = @Column(name = "issuer"))  -> 
	
	private PubKey issuer = new PubKey(); -> the constructor at initialization is needed by the json parser
 * 
 * </pre>
 * 
 * 
 * @author ben
 *
 */
@Entity
@Table(name = "block", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block implements Serializable {

	private static final long serialVersionUID = -4464417074968456696L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Short version;

	private Long nonce;

	private Integer number;

	private Integer powMin;

	@Temporal(TemporalType.TIME)
	private Date time;

	@Temporal(TemporalType.TIMESTAMP)
	private Date medianTime;

	private Integer membersCount;

	private Long monetaryMass;

	@Min(0) @Max(0)
	private Integer unitbase;

	
	private Integer issuersCount;

	private Integer issuersFrame;

	private Integer issuersFrameVar;

	@Pattern(regexp = Constants.Regex.G1)
	private String currency;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "issuer"))
	private PubKey issuer = new PubKey();

	@Valid
	@AttributeOverride(name = "signature", column = @Column(name = "signature"))
	private Signature signature = new Signature();

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "block_hash"))
	private Hash hash = new Hash();

	// @Pattern(regexp = Constants.Regex.EMPTY_STRING)
	private String parameters;

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "previous_hash"))
	private Hash previousHash = new Hash();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "previous_issuer"))
	private PubKey previousIssuer = new PubKey();

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "inner_hash"))
	private Hash inner_hash = new Hash();

	/**
	 * Quantitative representation of the daily UD in ḡ1 cents <br>
	 * ex : UD=1, g1=10.02, dividend=1002
	 */
	private Integer dividend;

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_identities", joinColumns = @JoinColumn(name = "container_block"))
	private List<Identity> identities = new ArrayList<Identity>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_joiners", joinColumns = @JoinColumn(name = "container_block"))
	private List<Joiner> joiners = new ArrayList<Joiner>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_actives", joinColumns = @JoinColumn(name = "container_block"))
	private List<Active> actives = new ArrayList<Active>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_leavers", joinColumns = @JoinColumn(name = "container_block"))
	private List<Leaver> leavers = new ArrayList<Leaver>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_revoked", joinColumns = @JoinColumn(name = "container_block"))
	private List<Revoked> revoked = new ArrayList<Revoked>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "wot_excluded", joinColumns = @JoinColumn(name = "container_block"))
	private List<Excluded> excluded = new ArrayList<Excluded>();

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	public List<Certification> certifications = new ArrayList<Certification>();

	@Valid
	@OneToMany(cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<Transaction>();

	/**
	 * Empty constructor
	 */
	public Block() {
	}

	public Long id() {
		return id;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash.getHash();
	}

	/**
	 * Format as follow Block [id=... , buid=... , hash=... , ... ]
	 * 
	 * return a string representation of the object
	 */
	@Override
	public String toString() {
		return "Block [" + version + ", " + number + ", " + currency + "]";
	}

	/**
	 * Method returning block as a Raw format
	 * 
	 * @return
	 */
	public String getRaw() {
		return "Version: " + version + "\nType: Block" + "\nCurrency: " + currency + "\nNumber: " + number
				+ "\nPoWMin: " + powMin + "\nTime: " + getTime() + "\nMedianTime: " + getMedianTime() + "\nUnitBase: "
				+ unitbase + "\nIssuer: " + issuer + "\nIssuersFrame: " + issuersFrame + "\nIssuersFrameVar: "
				+ issuersFrameVar + "\nDifferentIssuersCount: " + issuersCount + "\nPreviousHash: " + previousHash
				+ "\nPreviousIssuer: " + previousIssuer + "\nMembersCount: " + membersCount + "\nIdentities:\n"
				+ identities.stream().map(Identity::toRaw).collect(Collectors.joining("\n"))
				+ (identities.size() > 0 ? "\n" : "") + "Joiners:\n"
				+ joiners.stream().map(Joiner::toRaw).collect(Collectors.joining("\n"))
				+ (joiners.size() > 0 ? "\n" : "") + "Actives:\n"
				+ actives.stream().map(Active::toRaw).collect(Collectors.joining("\n"))
				+ (actives.size() > 0 ? "\n" : "") + "Leavers:\n"
				+ leavers.stream().map(Leaver::toRaw).collect(Collectors.joining("\n"))
				+ (leavers.size() > 0 ? "\n" : "") + "Revoked:\n"
				+ revoked.stream().map(Revoked::toRaw).collect(Collectors.joining("\n"))
				+ (revoked.size() > 0 ? "\n" : "") + "Excluded:\n"
				+ excluded.stream().map(Excluded::toRaw).collect(Collectors.joining("\n"))
				+ (excluded.size() > 0 ? "\n" : "") + "Certifications:\n"
				+ certifications.stream().map(Certification::toRaw).collect(Collectors.joining("\n"))
				+ (certifications.size() > 0 ? "\n" : "") + "Transactions:\n"
				+ transactions.stream().map(Transaction::toRaw).collect(Collectors.joining("\n"))
				+ (transactions.size() > 0 ? "\n" : "") + "InnerHash: " + inner_hash + "\nNonce: " + nonce + "\n";
	}

	/**
	 * @return the id
	 */
	protected Long getId() {
		return id;
	}

	/**
	 * Here we hack the getter to keep Timestamp display as a number
	 * 
	 * @return the time
	 */
	public Long getTime() {
		return time.getTime();
	}

	/**
	 * Here we hack the getter to keep Timestamp display as a number
	 * 
	 * @return the medianTime
	 */
	public Long getMedianTime() {
		return medianTime.getTime();
	}

	/**
	 * @return the membersCount
	 */
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
	 * @return the unitbase
	 */
	public Integer getUnitbase() {
		return unitbase;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Here we hack the getter to avoid Nesting the Json struct
	 * 
	 * @return the issuer's pubKey
	 */
	public String getIssuer() {
		return issuer.getPubkey();
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature.getSignature();
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * The only occasion previousBlock may be Null is if we are dealing with the first block 
	 * then Here we hack the getter to avoid Nesting the Json struct
	 * @return the previousHash
	 */
	public String getPreviousHash() {
		if(number==0) //  
			return null;
		return previousHash.getHash();
	}

	/**
	 * The only occasion previousBlock may be Null is if we are dealing with the first block 

	 * then Here we hack the getter to avoid Nesting the Json struct
	 * 
	 * @return the previousIssuer
	 */
	public String getPreviousIssuer() {
		if (number==0)
			return null;
		return previousIssuer.getPubkey();
	}

	/**
	 * @return the inner_hash
	 */
	public String getInner_hash() {
		return inner_hash.getHash();
	}

	/**
	 * @return the dividend
	 */
	public Integer getDividend() {
		return dividend;
	}

	/**
	 * @return the version
	 */
	public Short getVersion() {
		return version;
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
	 * @return the powMin
	 */
	public Integer getPowMin() {
		return powMin;
	}

	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * @return the identities
	 */
	public List<String> getIdentities() {
		return identities.stream().map(id -> id.getIdentity()).collect(Collectors.toList());
	}

	/**
	 * @return the joiners
	 */
	public List<String> getJoiners() {
		return joiners.stream().map(Joiner::toString).collect(Collectors.toList());
	}

	/**
	 * @return the active
	 */
	public List<String> getActives() {
		return actives.stream().map(Active::toString).collect(Collectors.toList());
	}

	/**
	 * @return the leavers
	 */
	public List<String> getLeavers() {
		return leavers.stream().map(Leaver::toString).collect(Collectors.toList());
	}

	/**
	 * @return the revoked
	 */
	public List<String> getRevoked() {
		return revoked.stream().map(Revoked::toString).collect(Collectors.toList());
	}

	/**
	 * @return the excluded
	 */
	public List<String> getExcluded() {
		return excluded.stream().map(Excluded::toString).collect(Collectors.toList());
	}

	/**
	 * @return the certifications
	 */
	public List<String> getCertifications() {
		return certifications.stream().map(c -> c.getCertif()).collect(Collectors.toList());
	}

}
