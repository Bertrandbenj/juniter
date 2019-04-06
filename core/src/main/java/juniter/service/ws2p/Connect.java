package juniter.service.ws2p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Connect  {

    private static final SecretBox secretBox = new SecretBox("salt", "password");

    private static final Logger LOG = LogManager.getLogger();


    private String auth;

    private String pub;

    private String challenge;

    private String sig;


    static Connect make() {
        final var res = new Connect();

        res.setAuth("CONNECT");
        final String challenge = UUID.randomUUID().toString();
        res.setChallenge(challenge);
        res.setPub(secretBox.getPublicKey());
        res.setSig(secretBox.sign(res.toRaw()));
        return res;
    }


    String ackJson() {
        return "{\"auth\":\"ACK\",\"pub\":\"" + secretBox.getPublicKey() + "\",\"sig\":\"" + secretBox.sign(ackRaw())
                + "\"}";
    }

    String ackRaw() {
        return "WS2P:ACK:g1:" + secretBox.getPublicKey() + ":" + challenge;
    }

    /**
     * @return the json Connect string
     */
    public String connectJson() {
        return "{\"auth\":\"" + "CONNECT" + //
                "\",\"pub\":\"" + pub + //
                "\",\"challenge\":\"" + challenge + //
                "\",\"sig\":\"" + sig + "\"}";
    }


    boolean isACK() {
        return "ACK".equals(auth);
    }

    boolean isConnect() {
        return "CONNECT".equals(auth);
    }

    boolean isOK() {
        return "OK".equals(auth);
    }

    String okJson() {
        return "{" + "\"auth\":" + "OK" + "\",sig\":\"" + secretBox.sign(okRaw()) + "\"}";
    }

    private String okRaw() {
        return "WS2P:OK:g1:" + pub + ":" + challenge;
    }

    String toRaw() {
        return "WS2P:CONNECT:g1:" + pub + ":" + challenge;
    }

    @Override
    public String toString() {
        return auth + ":" + pub + ":" + challenge + ":" + sig;
    }

    public boolean verify() {
        LOG.info("connect? " + "CONNECT".equals(auth) + "  crypto? " + Crypto.verify(challenge, sig, pub));
        return "CONNECT".equals(auth) && Crypto.verify(challenge, sig, pub);
    }

}