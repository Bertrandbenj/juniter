package juniter.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.tx.Transaction;
import juniter.core.model.wot.*;
import juniter.core.utils.Constants;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * The top / main class of the model is the Block class
 *
 * Here are some note regarding annotations :
 *
 * <pre>
 *
 * 	&#64;Valid  -> is useful to enforce encapsulated object validation
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
@Table(name = "block", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(BStamp.class)
public class Block implements Serializable, DUPComponent {

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

	@Pattern(regexp = Constants.Regex.G1)
	private String currency;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "issuer"))
	private final Pubkey issuer = new Pubkey();

	@Valid
	@AttributeOverride(name = "signature", column = @Column(name = "signature"))
	private Signature signature = new Signature();

	@Valid
	//	@AttributeOverride(name = "hash", column = @Column(name = "hash"))
	@Id
	protected String hash;// = new Hash();

	// @Pattern(regexp = Constants.Regex.EMPTY_STRING)

	private String parameters;

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "previous_hash"))
	private Hash previousHash = new Hash();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "previous_issuer"))
	private Pubkey previousIssuer = new Pubkey();

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "inner_hash"))
	private Hash inner_hash = new Hash();

	/**
	 * Quantitative representation of the daily UD in ḡ1 cents <br>
	 * ex : UD=1, g1=10.02, dividend=1002
	 */
	private Integer dividend;

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@OneToMany(cascade = CascadeType.ALL)
	private List<Identity> identities = new ArrayList<Identity>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "wot_joiners", joinColumns = { @JoinColumn(name = "number"), @JoinColumn(name = "hash") })
	private List<Joiner> joiners = new ArrayList<Joiner>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "wot_actives", joinColumns = { @JoinColumn(name = "number"), @JoinColumn(name = "hash") })
	private List<Active> actives = new ArrayList<Active>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "wot_leavers", joinColumns = { @JoinColumn(name = "number"), @JoinColumn(name = "hash") })
	private List<Leaver> leavers = new ArrayList<>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "wot_revoked", joinColumns = { @JoinColumn(name = "number"), @JoinColumn(name = "hash") })
	private List<Revoked> revoked = new ArrayList<>();

	@Valid
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable(name = "wot_excluded", joinColumns = { @JoinColumn(name = "number"), @JoinColumn(name = "hash") })
	private List<Excluded> excluded = new ArrayList<>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Certification> certifications = new ArrayList<>();

	@Valid
	@OrderColumn
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumns(value = { @JoinColumn(name = "Bnumber", referencedColumnName = "number"),
			@JoinColumn(name = "Bhash", referencedColumnName = "hash") })
	private List<Transaction> transactions = new ArrayList<>();

	/**
	 * Empty constructors
	 */
	public Block() {
	}

	/**
	 * @return the active
	 */
	public List<Active> getActives() {
		return actives;
	}

	public List<String> getActivesAsStrings() {
		return actives.stream().map(Active::toString).collect(toList());
	}

	/**
	 * @return the certifications
	 */
	public List<Certification> getCertifications() {
		return certifications; // .stream().map(c -> c.getCertif()).collect(Collectors.toList());
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
		return identities;// .stream().map(id -> id.getIdentity()).collect(Collectors.toList());
	}

	/**
	 * @return the id
	 */
	//	protected Long getId() {
	//		return id;
	//	}

	/**
	 * @return the inner_hash
	 */
	public String getInner_hash() {
		return inner_hash.toString();
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
	public Long getMedianTime() {
		return medianTime;
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
		return previousHash.toString();
	}

	/**
	 * The only occasion previousBlock may be Null is if we are dealing with the
	 * first block
	 *
	 * then Here we hack the getter to avoid Nesting the Json struct
	 *
	 * @return the previousIssuer
	 */
	public String getPreviousIssuer() {
		if (number == 0)
			return null;
		return previousIssuer.getPubkey();
	}

	public String getRaw() {
		return toDUP(false, true);
	}

	/**
	 * @return the revoked
	 */
	public List<Revoked> getRevoked() {
		return revoked; // .stream().map(Revoked::toString).collect(Collectors.toList());
	}

	/**
	 * @return the signature
	 */
	public Signature getSignature() {
		return signature;// .getSignature();
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

	public void setActives(List<Active> actives) {
		this.actives = actives;
	}

	public void setCertifications(List<Certification> certifications) {
		this.certifications = certifications;
	}

	//	public Long id() {
	//		return id;
	//	}

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

	//	public void setId(Long id) {
	//		this.id = id;
	//	}

	public void setInner_hash(Hash inner_hash) {
		this.inner_hash = inner_hash;
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

	public void setPreviousHash(Hash previousHash) {
		this.previousHash = previousHash;
	}

	public void setPreviousIssuer(Pubkey previousIssuer) {
		this.previousIssuer = previousIssuer;
	}

	public void setRevoked(List<Revoked> revoked) {
		this.revoked = revoked;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

	public void setTime(Long time) {
		this.time = time;
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
	BlockSize

	The block size is defined as the number of lines in multiline fields (Identities, Joiners, Actives, Leavers, Revoked, Certifications, Transactions) except Excluded field.

	For example:


	1 new identity + 1 joiner + 2 certifications = 4 lines sized block
	1 new identity + 1 joiner + 2 certifications + 5 lines transaction = 9 lines sized block
	 * </pre>
	 *
	 * @return
	 */
	public Integer size() {

		return identities.size() + joiners.size() + actives.size() + leavers.size() + revoked.size()
		+ certifications.size() + transactions.size();
	}


	@Override
	public String toDUP() {
		return toDUP(true, true);
	}

	/**
	 * Method returning block as a Raw format
	 *
	 * @return
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
				+ actives.stream().map(Active::toDUP).collect(Collectors.joining("\n"))
				+ (actives.size() > 0 ? "\n" : "") + "Leavers:\n"
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
	 *
	 * return a string representation of the object
	 */
	@Override
	public String toString() {
		return "Block [" + version + ", " + number + ", " + currency + "]";
	}
}
