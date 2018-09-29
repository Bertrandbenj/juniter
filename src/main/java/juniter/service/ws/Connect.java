package juniter.service.ws;

import java.io.Serializable;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.crypto.CryptoUtils;
import juniter.crypto.SecretBox;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Connect implements Serializable {

	public class ACK {

		String pub;
		String sig;

		private ACK(String pub) {
			this.pub = secretBox.getPublicKey();
			sig = secretBox.sign(toRaw());
		}

		String toRaw() {

			return "WS2P:ACK:g1:" + secretBox.getPublicKey() + ":";
		}

		@Override
		public String toString() {
			return "{" + "\"auth\":\"ACK\",\"pub\":\"" + pub + "\",\"sig\":\"" + sig + "\"}";
		}
	}

	private static final long serialVersionUID = 1443849093806421432L;

	final static SecretBox secretBox = new SecretBox("salt", "password");

	private static final Logger LOG = LogManager.getLogger();

	static Connect make() {
		final var res = new Connect();

		res.setAuth("CONNECT");
		final String challenge = UUID.randomUUID().toString();
		res.setChallenge(challenge);
		res.setPub(secretBox.getPublicKey());
		res.setSig(secretBox.sign(res.toRaw()));
		return res;
	}

	String auth;

	String pub;

	String challenge;

	String sig;

	public Connect() {

	}

	public String ackJson() {
		return "{\"auth\":\"ACK\",\"pub\":\"" + secretBox.getPublicKey() + "\",\"sig\":\"" + secretBox.sign(ackRaw())
				+ "\"}";
	}

	public String ackRaw() {
		return "WS2P:ACK:g1:" + secretBox.getPublicKey() + ":" + challenge;
	}

	/**
	 * JSON format
	 */
	public String connectJson() {
		return "{\"auth\":\"" + "CONNECT" + //
				"\",\"pub\":\"" + pub + //
				"\",\"challenge\":\"" + challenge + //
				"\",\"sig\":\"" + sig + "\"}";
	}

	public String getAuth() {
		return auth;
	}

	public String getChallenge() {
		return challenge;
	}

	public String getPub() {
		return pub;
	}

	public String getSig() {
		return sig;
	}

	public boolean isACK() {
		return "ACK".equals(auth);
	}

	public boolean isConnect() {
		return "CONNECT".equals(auth);
	}

	public boolean isOK() {
		return "OK".equals(auth);
	}

	public String okJson() {
		return "{" + "\"auth\":" + "OK" + "\",sig\":\"" + secretBox.sign(okRaw()) + "\"}";
	}

	private String okRaw() {
		return "WS2P:OK:g1:" + pub + ":" + challenge;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public void setPub(String pub) {
		this.pub = pub;
	}

	public void setSig(String sig) {
		this.sig = sig;
	}

	String toRaw() {
		return "WS2P:CONNECT:g1:" + pub + ":" + challenge;
	}

	@Override
	public String toString() {
		return auth + ":" + pub + ":" + challenge + ":" + sig;
	}

	public boolean verify() {
		LOG.info("connect? " + "CONNECT".equals(auth) + "  crypto? " + CryptoUtils.verify(challenge, sig, pub));
		return "CONNECT".equals(auth) && CryptoUtils.verify(challenge, sig, pub);
	}

}