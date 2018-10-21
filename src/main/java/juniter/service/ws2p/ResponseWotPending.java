package juniter.service.ws2p;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.core.model.BStamp;
import juniter.core.model.Pubkey;
import juniter.core.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ResponseWotPending implements Serializable {
	@JsonIgnoreProperties(ignoreUnknown = true)

	class Identities {

		class Idty {

			class Certifs {
				String from;
				String to;
				String sig;
				Long timestamp;
				Long expiresIn;

				public Certifs() {

				}
			}

			class Meta {

				BStamp timestamp;

				public Meta() {

				}
			}

			class PendingCerts {
				String from;
				String to;
				String target;
				Long block_number;
				String block_hash;
				Long block;
				Boolean linked;
				Boolean written;
				Object written_block;
				Object written_hash;
				Long expires_on;
				Long expired;
				BStamp blockstamp;
				String sig;

				Long timestamp;
				Long expiresIn;

				public PendingCerts() {

				}

				public void setBlock(Long block) {
					this.block = block;
				}

				public void setBlock_hash(String block_hash) {
					this.block_hash = block_hash;
				}

				public void setBlock_number(Long block_number) {
					this.block_number = block_number;
				}

				public void setBlockstamp(BStamp blockstamp) {
					this.blockstamp = blockstamp;
				}

				public void setExpired(Long expired) {
					this.expired = expired;
				}

				public void setExpires_on(Long expires_on) {
					this.expires_on = expires_on;
				}

				public void setExpiresIn(Long expiresIn) {
					this.expiresIn = expiresIn;
				}

				public void setFrom(String from) {
					this.from = from;
				}

				public void setLinked(Boolean linked) {
					this.linked = linked;
				}

				public void setSig(String sig) {
					this.sig = sig;
				}

				public void setTarget(String target) {
					this.target = target;
				}

				public void setTimestamp(Long timestamp) {
					this.timestamp = timestamp;
				}

				public void setTo(String to) {
					this.to = to;
				}

				public void setWritten(Boolean written) {
					this.written = written;
				}

				public void setWritten_block(Object written_block) {
					this.written_block = written_block;
				}

				public void setWritten_hash(Object written_hash) {
					this.written_hash = written_hash;
				}
			}

			class PendingMembs {
				String membership;
				String issuer;
				Long number;
				Long blockNumber;
				String blockHash;
				String userid;
				String certts;
				String block;
				String fpr;
				String idtyHash;
				Boolean written;
				Object written_number;
				Long expires_on;
				String signature;
				Long expired;
				BStamp blockstamp;
				String sig;
				String type;

				public PendingMembs() {

				}

				public void setBlock(String block) {
					this.block = block;
				}

				public void setBlockHash(String blockHash) {
					this.blockHash = blockHash;
				}

				public void setBlockNumber(Long blockNumber) {
					this.blockNumber = blockNumber;
				}

				public void setBlockstamp(BStamp blockstamp) {
					this.blockstamp = blockstamp;
				}

				public void setCertts(String certts) {
					this.certts = certts;
				}

				public void setExpired(Long expired) {
					this.expired = expired;
				}

				public void setExpires_on(Long expires_on) {
					this.expires_on = expires_on;
				}

				public void setFpr(String fpr) {
					this.fpr = fpr;
				}

				public void setIdtyHash(String idtyHash) {
					this.idtyHash = idtyHash;
				}

				public void setIssuer(String issuer) {
					this.issuer = issuer;
				}

				public void setMembership(String membership) {
					this.membership = membership;
				}

				public void setNumber(Long number) {
					this.number = number;
				}

				public void setSig(String sig) {
					this.sig = sig;
				}

				public void setSignature(String signature) {
					this.signature = signature;
				}

				public void setType(String type) {
					this.type = type;
				}

				public void setUserid(String userid) {
					this.userid = userid;
				}

				public void setWritten(Boolean written) {
					this.written = written;
				}

				public void setWritten_number(Object written_number) {
					this.written_number = written_number;
				}

			}

			Pubkey pubkey;

			String uid;
			Signature sig;
			Meta meta;
			String revocation_sig;
			Boolean revoked;
			Object revoked_on;
			Boolean expired;
			Boolean outdistanced;
			Boolean isSentry;
			Boolean wasMember;
			List<Certifs> certifications = new ArrayList<>();;

			List<PendingCerts> pendingCerts = new ArrayList<>();;

			List<PendingMembs> pendingMemberships = new ArrayList<>();;

			Long membershipExpiresIn;

			public Idty() {

			}

			public void setCertifications(List<Certifs> certifications) {
				this.certifications = certifications;
			}

			public void setExpired(Boolean expired) {
				this.expired = expired;
			}

			public void setIsSentry(Boolean isSentry) {
				this.isSentry = isSentry;
			}

			public void setMembershipExpiresIn(Long membershipExpiresIn) {
				this.membershipExpiresIn = membershipExpiresIn;
			}

			public void setMeta(Meta meta) {
				this.meta = meta;
			}

			public void setOutdistanced(Boolean outdistanced) {
				this.outdistanced = outdistanced;
			}

			public void setPendingCerts(List<PendingCerts> pendingCerts) {
				this.pendingCerts = pendingCerts;
			}

			public void setPendingMemberships(List<PendingMembs> pendingMemberships) {
				this.pendingMemberships = pendingMemberships;
			}

			public void setPubkey(Pubkey pubkey) {
				this.pubkey = pubkey;
			}

			public void setRevocation_sig(String revocation_sig) {
				this.revocation_sig = revocation_sig;
			}

			public void setRevoked(Boolean revoked) {
				this.revoked = revoked;
			}

			public void setRevoked_on(Object revoked_on) {
				this.revoked_on = revoked_on;
			}

			public void setSig(Signature sig) {
				this.sig = sig;
			}

			public void setUid(String uid) {
				this.uid = uid;
			}

			public void setWasMember(Boolean wasMember) {
				this.wasMember = wasMember;
			}
		}

		List<Idty> identities = new ArrayList<>();

		public Identities() {

		}

		public List<Idty> getIdentities() {
			return identities;
		}

		public void setIdentities(List<Idty> identities) {
			this.identities = identities;
		}
	}

	private static final long serialVersionUID = 2497514270739293189L;

	private static final Logger LOG = LogManager.getLogger();

	String resId;

	Object body;

	public Object getBody() {
		return body;
	}

	public String getResId() {
		return resId;
	}

}
