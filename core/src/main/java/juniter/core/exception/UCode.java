package juniter.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UCode {

    private int number;
    private String message;


    // Technical errors
    public static UCode UNKNOWN = new UCode(1001, "An unknown error occured");
    public static UCode UNHANDLED = new UCode(1002, "An unhandled error occured");
    public static UCode HTTP_LIMITATION = new UCode(1006, "This URI has reached its maximum usage quota. Please retry later.");

    public static UCode HTTP_PARAM_PUBKEY_REQUIRED = new UCode(1101, "Parameter `pubkey` is required");
    public static UCode HTTP_PARAM_IDENTITY_REQUIRED = new UCode(1102, "Parameter `identity` is required");
    public static UCode HTTP_PARAM_PEER_REQUIRED = new UCode(1103, "Requires a peer");
    public static UCode HTTP_PARAM_BLOCK_REQUIRED = new UCode(1104, "Requires a node");
    public static UCode HTTP_PARAM_MEMBERSHIP_REQUIRED = new UCode(1105, "Requires a membership");
    public static UCode HTTP_PARAM_TX_REQUIRED = new UCode(1106, "Requires a transaction");
    public static UCode HTTP_PARAM_SIG_REQUIRED = new UCode(1107, "Parameter `sig` is required");
    public static UCode HTTP_PARAM_CERT_REQUIRED = new UCode(1108, "Parameter `cert` is required");
    public static UCode HTTP_PARAM_REVOCATION_REQUIRED = new UCode(1109, "Parameter `revocation` is required");
    public static UCode HTTP_PARAM_CONF_REQUIRED = new UCode(1110, "Parameter `conf` is required");
    public static UCode HTTP_PARAM_CPU_REQUIRED = new UCode(1111, "Parameter `cpu` is required");

    // Business errors
    public static UCode NO_MATCHING_IDENTITY = new UCode(2001, "No matching identity");
    public static UCode SELF_PEER_NOT_FOUND = new UCode(2005, "Self peering was not found");
    public static UCode NOT_A_MEMBER = new UCode(2009, "Not a member");
    public static UCode NO_CURRENT_BLOCK = new UCode(2010, "No currentStrict node");
    public static UCode PEER_NOT_FOUND = new UCode(2012, "Peer not found");
    public static UCode NO_IDTY_MATCHING_PUB_OR_UID = new UCode(2021, "No identity matching this pubkey or userid");
    public static UCode TX_NOT_FOUND = new UCode(2034, "Transaction not found");
    public static UCode INCORRECT_PAGE_NUMBER = new UCode(2035, "Incorrect page number");

    //Juniter errors
    public static UCode HTTP_PARAM_CCY_REQUIRED = new UCode(5002, "Parameter `currency` is required [ex: g1]");
    public static UCode EXCEPTION = new UCode(5404, "...");


}