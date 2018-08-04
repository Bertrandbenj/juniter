package juniter.model.bma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BlockDTO implements Serializable {

	private static final long serialVersionUID = -6400285529088830671L;

	private Short version;

	private Long nonce;

	private Integer number;

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

	private String hash;

	private String parameters;

	private String previousHash;

	private String previousIssuer;

	private String inner_hash;

	private Integer dividend;

	private List<String> identities = new ArrayList<>();

	private List<String> joiners = new ArrayList<>();

	private List<String> actives = new ArrayList<>();

	private List<String> leavers = new ArrayList<>();

	private List<String> revoked = new ArrayList<>();

	private List<String> excluded = new ArrayList<>();

	private List<String> certifications = new ArrayList<>();

	private List<TransactionDTO> transactions = new ArrayList<>();

	public List<String> getActives() {
		return actives;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public String getCurrency() {
		return currency;
	}

	public Integer getDividend() {
		return dividend;
	}

	public List<String> getExcluded() {
		return excluded;
	}

	public String getHash() {
		return hash;
	}

	public List<String> getIdentities() {
		return identities;
	}

	public String getInner_hash() {
		return inner_hash;
	}

	public String getIssuer() {
		return issuer;
	}

	public Integer getIssuersCount() {
		return issuersCount;
	}

	public Integer getIssuersFrame() {
		return issuersFrame;
	}

	public Integer getIssuersFrameVar() {
		return issuersFrameVar;
	}

	public List<String> getJoiners() {
		return joiners;
	}

	public List<String> getLeavers() {
		return leavers;
	}

	public Long getMedianTime() {
		return medianTime;
	}

	public Integer getMembersCount() {
		return membersCount;
	}

	public Long getMonetaryMass() {
		return monetaryMass;
	}

	public Long getNonce() {
		return nonce;
	}

	public Integer getNumber() {
		return number;
	}

	public String getParameters() {
		return parameters;
	}

	public Integer getPowMin() {
		return powMin;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public String getPreviousIssuer() {
		return previousIssuer;
	}

	public List<String> getRevoked() {
		return revoked;
	}

	public String getSignature() {
		return signature;
	}

	public Long getTime() {
		return time;
	}

	public List<TransactionDTO> getTransactions() {
		return transactions;
	}

	public Integer getUnitbase() {
		return unitbase;
	}

	public Short getVersion() {
		return version;
	}

	public void setActives(List<String> actives) {
		this.actives.addAll(actives);
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDividend(Integer dividend) {
		this.dividend = dividend;
	}

	public void setExcluded(List<String> excluded) {
		this.excluded = excluded;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setIdentities(List<String> identities) {
		this.identities = identities;
	}

	public void setInner_hash(String inner_hash) {
		this.inner_hash = inner_hash;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
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

	public void setJoiners(List<String> joiners) {
		this.joiners = joiners;
	}

	public void setLeavers(List<String> leavers) {
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

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public void setPreviousIssuer(String previousIssuer) {
		this.previousIssuer = previousIssuer;
	}

	public void setRevoked(List<String> revoked) {
		this.revoked = revoked;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public void setTransactions(List<TransactionDTO> transactions) {
		this.transactions = transactions;
	}

	public void setUnitbase(Integer unitbase) {
		this.unitbase = unitbase;
	}

	public void setVersion(Short version) {
		this.version = version;
	}

}
