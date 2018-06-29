package juniter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.utils.Constants;

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

	private Integer unitbase;

	private Integer issuersCount;

	private Integer issuersFrame;

	private Integer issuersFrameVar;

	private String currency;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "issuer", referencedColumnName= "pubkey")

	// @Pattern(regexp=Constants.Regex.PUBKEY) @Size(min=43, max=45)
	@AttributeOverride(name = "issuer", column = @Column(name = "issuer"))
	@Valid private PubKey issuer;

	@Pattern(regexp = Constants.Regex.SIGNATURE)
	@Size(max = 88)
	private String signature;//

	@Pattern(regexp = Constants.Regex.HASH)
	@Size(max = 64)
	private String hash;

	//@Pattern(regexp = Constants.Regex.EMPTY_STRING)
	private String parameters;

	@Pattern(regexp = Constants.Regex.HASH)
	@Size(max = 64)
	private String previousHash;

//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn(name = "previousIssuer", referencedColumnName= "pubkey")
	// @Pattern(regexp=Constants.Regex.PUBKEY) @Size(min=43, max=45)

	@AttributeOverride(name = "pubkey", column = @Column(name = "previousissuer"))
	@Valid private PubKey previousIssuer;

	@Pattern(regexp = Constants.Regex.HASH)
	@Size(max = 64)
	private String inner_hash;

	/**
	 * Quantitative representation of the daily UD in ḡ1 cents <br>
	 * ex : UD=1, g1=10.02, dividend=1002
	 */
	private Integer dividend;// 1002

	@OneToMany(cascade = { CascadeType.ALL })
	private List<Identity> identities = new ArrayList<Identity>();

	@OneToMany(cascade = { CascadeType.ALL })
	private List<Joiner> joiners = new ArrayList<Joiner>();

	@ElementCollection
	private List<String> actives = new ArrayList<String>();

	@ElementCollection
	private List<String> leavers = new ArrayList<String>();

	@ElementCollection
	private List<String> revoked = new ArrayList<String>();

	@ElementCollection
	private List<String> excluded = new ArrayList<String>();

	@OneToMany(cascade = { CascadeType.ALL })
	private List<Certification> certifications = new ArrayList<Certification>();

	@OneToMany(cascade = { CascadeType.ALL })
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
		return hash;
	}

	/**
	 * Format as follow Block [id=... , buid=... , hash=... , ... ]
	 * 
	 * return a string representation of the object
	 */
	@Override
	public String toString() {
		return "Block [" + version + ", " + number + ", " + currency +"]";
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
				+ (joiners.size() > 0 ? "\n" : "") + "Actives:\n" + actives.stream().collect(Collectors.joining("\n"))
				+ (actives.size() > 0 ? "\n" : "") + "Leavers:\n" + leavers.stream().collect(Collectors.joining("\n"))
				+ (leavers.size() > 0 ? "\n" : "") + "Revoked:\n" + revoked.stream().collect(Collectors.joining("\n"))
				+ (revoked.size() > 0 ? "\n" : "") + "Excluded:\n" + excluded.stream().collect(Collectors.joining("\n"))
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
		return signature;
	}

	/**
	 * @return the parameters
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * @return the previousHash
	 */
	public String getPreviousHash() {
		return previousHash;
	}

	/**
	 * Here we hack the getter to avoid Nesting the Json struct
	 * 
	 * @return the previousIssuer
	 */
	public String getPreviousIssuer() {
		return previousIssuer.getPubkey();
	}

	/**
	 * @return the inner_hash
	 */
	public String getInner_hash() {
		return inner_hash;
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
	public List<Identity> getIdentities() {
		return identities;
	}

	/**
	 * @return the joiners
	 */
	public List<Joiner> getJoiners() {
		return joiners;
	}

	/**
	 * @return the active
	 */
	public List<String> getActives() {
		return actives;
	}

	/**
	 * @return the leavers
	 */
	public List<String> getLeavers() {
		return leavers;
	}

	/**
	 * @return the revoked
	 */
	public List<String> getRevoked() {
		return revoked;
	}

	/**
	 * @return the excluded
	 */
	public List<String> getExcluded() {
		return excluded;
	}

	/**
	 * @return the certifications
	 */
	public List<String> getCertifications() {
		return certifications.stream().map(c -> c.getCertif()).collect(Collectors.toList());
	}

}
