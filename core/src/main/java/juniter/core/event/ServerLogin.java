package juniter.core.event;

import juniter.core.crypto.SecretBox;

public class ServerLogin extends CoreEvent<SecretBox> {

    public ServerLogin(SecretBox secret) {
        super(secret, "");
        name = getClass().getSimpleName();
    }

}
