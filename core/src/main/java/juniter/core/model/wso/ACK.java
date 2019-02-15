package juniter.core.model.wso;

import juniter.core.crypto.SecretBox;
import lombok.Data;

@Data
public class ACK {

    private String pub;
    private String sig;

    private ACK(SecretBox secretBox) {
        this.pub = secretBox.getPublicKey();
        sig = secretBox.sign(toRaw());
    }


   String toRaw() {
        return "WS2P:ACK:g1:" + pub + ":";
    }

    @Override
    public String toString() {
        return "{" + "\"auth\":\"ACK\",\"pub\":\"" + pub + "\",\"sig\":\"" + sig + "\"}";
    }
}
