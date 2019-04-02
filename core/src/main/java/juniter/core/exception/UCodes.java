package juniter.core.exception;

public interface UCodes {

    class UCode {
        int number;
        String message;

        UCode(int number, String message) {
            this.number = number;
            this.message = message;
        }
    }

    // Technical errors
    UCode UNKNOWN = new UCode(1001, "An unknown error occured");
    UCode UNHANDLED = new UCode(1002, "An unhandled error occured");
    UCode HTTP_LIMITATION = new UCode(1006, "This URI has reached its maximum usage quota. Please retry later.");

    UCode HTTP_PARAM_PUBKEY_REQUIRED = new UCode(1101, "Parameter `pubkey` is required");
    UCode HTTP_PARAM_IDENTITY_REQUIRED = new UCode(1102, "Parameter `identity` is required");
    UCode HTTP_PARAM_PEER_REQUIRED = new UCode(1103, "Requires a peer");
    UCode HTTP_PARAM_BLOCK_REQUIRED = new UCode(1104, "Requires a block");
    UCode HTTP_PARAM_MEMBERSHIP_REQUIRED = new UCode(1105, "Requires a membership");
    UCode HTTP_PARAM_TX_REQUIRED = new UCode(1106, "Requires a transaction");
    UCode HTTP_PARAM_SIG_REQUIRED = new UCode(1107, "Parameter `sig` is required");
    UCode HTTP_PARAM_CERT_REQUIRED = new UCode(1108, "Parameter `cert` is required");
    UCode HTTP_PARAM_REVOCATION_REQUIRED = new UCode(1109, "Parameter `revocation` is required");
    UCode HTTP_PARAM_CONF_REQUIRED = new UCode(1110, "Parameter `conf` is required");
    UCode HTTP_PARAM_CPU_REQUIRED = new UCode(1111, "Parameter `cpu` is required");

    // Business errors
    UCode NO_MATCHING_IDENTITY = new UCode(2001, "No matching identity");
    UCode SELF_PEER_NOT_FOUND = new UCode(2005, "Self peering was not found");
    UCode NOT_A_MEMBER = new UCode(2009, "Not a member");
    UCode NO_CURRENT_BLOCK = new UCode(2010, "No current block");
    UCode PEER_NOT_FOUND = new UCode(2012, "Peer not found");
    UCode NO_IDTY_MATCHING_PUB_OR_UID = new UCode(2021, "No identity matching this pubkey or userid");
    UCode TX_NOT_FOUND = new UCode(2034, "Transaction not found");
    UCode INCORRECT_PAGE_NUMBER = new UCode(2035, "Incorrect page number");


}
