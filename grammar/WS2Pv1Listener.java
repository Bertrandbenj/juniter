// Generated from WS2Pv1.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link WS2Pv1Parser}.
 */
public interface WS2Pv1Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(WS2Pv1Parser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(WS2Pv1Parser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#transaction}.
	 * @param ctx the parse tree
	 */
	void enterTransaction(WS2Pv1Parser.TransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#transaction}.
	 * @param ctx the parse tree
	 */
	void exitTransaction(WS2Pv1Parser.TransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#identity}.
	 * @param ctx the parse tree
	 */
	void enterIdentity(WS2Pv1Parser.IdentityContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#identity}.
	 * @param ctx the parse tree
	 */
	void exitIdentity(WS2Pv1Parser.IdentityContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#revocation}.
	 * @param ctx the parse tree
	 */
	void enterRevocation(WS2Pv1Parser.RevocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#revocation}.
	 * @param ctx the parse tree
	 */
	void exitRevocation(WS2Pv1Parser.RevocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#certification}.
	 * @param ctx the parse tree
	 */
	void enterCertification(WS2Pv1Parser.CertificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#certification}.
	 * @param ctx the parse tree
	 */
	void exitCertification(WS2Pv1Parser.CertificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#membership}.
	 * @param ctx the parse tree
	 */
	void enterMembership(WS2Pv1Parser.MembershipContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#membership}.
	 * @param ctx the parse tree
	 */
	void exitMembership(WS2Pv1Parser.MembershipContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#peer}.
	 * @param ctx the parse tree
	 */
	void enterPeer(WS2Pv1Parser.PeerContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#peer}.
	 * @param ctx the parse tree
	 */
	void exitPeer(WS2Pv1Parser.PeerContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#block_}.
	 * @param ctx the parse tree
	 */
	void enterBlock_(WS2Pv1Parser.Block_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#block_}.
	 * @param ctx the parse tree
	 */
	void exitBlock_(WS2Pv1Parser.Block_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCertTimestamp(WS2Pv1Parser.CertTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCertTimestamp(WS2Pv1Parser.CertTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#certTS}.
	 * @param ctx the parse tree
	 */
	void enterCertTS(WS2Pv1Parser.CertTSContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#certTS}.
	 * @param ctx the parse tree
	 */
	void exitCertTS(WS2Pv1Parser.CertTSContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(WS2Pv1Parser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(WS2Pv1Parser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#endpoints}.
	 * @param ctx the parse tree
	 */
	void enterEndpoints(WS2Pv1Parser.EndpointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#endpoints}.
	 * @param ctx the parse tree
	 */
	void exitEndpoints(WS2Pv1Parser.EndpointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void enterIdtyissuer(WS2Pv1Parser.IdtyissuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void exitIdtyissuer(WS2Pv1Parser.IdtyissuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void enterIdtyUniqueID(WS2Pv1Parser.IdtyUniqueIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void exitIdtyUniqueID(WS2Pv1Parser.IdtyUniqueIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterIdtyTimestamp(WS2Pv1Parser.IdtyTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitIdtyTimestamp(WS2Pv1Parser.IdtyTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void enterIdtySignature(WS2Pv1Parser.IdtySignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void exitIdtySignature(WS2Pv1Parser.IdtySignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#inputs}.
	 * @param ctx the parse tree
	 */
	void enterInputs(WS2Pv1Parser.InputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#inputs}.
	 * @param ctx the parse tree
	 */
	void exitInputs(WS2Pv1Parser.InputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#issuers}.
	 * @param ctx the parse tree
	 */
	void enterIssuers(WS2Pv1Parser.IssuersContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#issuers}.
	 * @param ctx the parse tree
	 */
	void exitIssuers(WS2Pv1Parser.IssuersContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#locktime}.
	 * @param ctx the parse tree
	 */
	void enterLocktime(WS2Pv1Parser.LocktimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#locktime}.
	 * @param ctx the parse tree
	 */
	void exitLocktime(WS2Pv1Parser.LocktimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(WS2Pv1Parser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(WS2Pv1Parser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(WS2Pv1Parser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(WS2Pv1Parser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#outputs}.
	 * @param ctx the parse tree
	 */
	void enterOutputs(WS2Pv1Parser.OutputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#outputs}.
	 * @param ctx the parse tree
	 */
	void exitOutputs(WS2Pv1Parser.OutputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#poWMin}.
	 * @param ctx the parse tree
	 */
	void enterPoWMin(WS2Pv1Parser.PoWMinContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#poWMin}.
	 * @param ctx the parse tree
	 */
	void exitPoWMin(WS2Pv1Parser.PoWMinContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#publicKey}.
	 * @param ctx the parse tree
	 */
	void enterPublicKey(WS2Pv1Parser.PublicKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#publicKey}.
	 * @param ctx the parse tree
	 */
	void exitPublicKey(WS2Pv1Parser.PublicKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#signatures}.
	 * @param ctx the parse tree
	 */
	void enterSignatures(WS2Pv1Parser.SignaturesContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#signatures}.
	 * @param ctx the parse tree
	 */
	void exitSignatures(WS2Pv1Parser.SignaturesContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#userID}.
	 * @param ctx the parse tree
	 */
	void enterUserID(WS2Pv1Parser.UserIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#userID}.
	 * @param ctx the parse tree
	 */
	void exitUserID(WS2Pv1Parser.UserIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#unlocks}.
	 * @param ctx the parse tree
	 */
	void enterUnlocks(WS2Pv1Parser.UnlocksContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#unlocks}.
	 * @param ctx the parse tree
	 */
	void exitUnlocks(WS2Pv1Parser.UnlocksContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(WS2Pv1Parser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(WS2Pv1Parser.VersionContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#input_}.
	 * @param ctx the parse tree
	 */
	void enterInput_(WS2Pv1Parser.Input_Context ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#input_}.
	 * @param ctx the parse tree
	 */
	void exitInput_(WS2Pv1Parser.Input_Context ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#unlock}.
	 * @param ctx the parse tree
	 */
	void enterUnlock(WS2Pv1Parser.UnlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#unlock}.
	 * @param ctx the parse tree
	 */
	void exitUnlock(WS2Pv1Parser.UnlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#unsig}.
	 * @param ctx the parse tree
	 */
	void enterUnsig(WS2Pv1Parser.UnsigContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#unsig}.
	 * @param ctx the parse tree
	 */
	void exitUnsig(WS2Pv1Parser.UnsigContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#unxhx}.
	 * @param ctx the parse tree
	 */
	void enterUnxhx(WS2Pv1Parser.UnxhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#unxhx}.
	 * @param ctx the parse tree
	 */
	void exitUnxhx(WS2Pv1Parser.UnxhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#output}.
	 * @param ctx the parse tree
	 */
	void enterOutput(WS2Pv1Parser.OutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#output}.
	 * @param ctx the parse tree
	 */
	void exitOutput(WS2Pv1Parser.OutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#issuer}.
	 * @param ctx the parse tree
	 */
	void enterIssuer(WS2Pv1Parser.IssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#issuer}.
	 * @param ctx the parse tree
	 */
	void exitIssuer(WS2Pv1Parser.IssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#endpoint}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint(WS2Pv1Parser.EndpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#endpoint}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint(WS2Pv1Parser.EndpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(WS2Pv1Parser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(WS2Pv1Parser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(WS2Pv1Parser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(WS2Pv1Parser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(WS2Pv1Parser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(WS2Pv1Parser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#sig}.
	 * @param ctx the parse tree
	 */
	void enterSig(WS2Pv1Parser.SigContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#sig}.
	 * @param ctx the parse tree
	 */
	void exitSig(WS2Pv1Parser.SigContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#xhx}.
	 * @param ctx the parse tree
	 */
	void enterXhx(WS2Pv1Parser.XhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#xhx}.
	 * @param ctx the parse tree
	 */
	void exitXhx(WS2Pv1Parser.XhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#csv}.
	 * @param ctx the parse tree
	 */
	void enterCsv(WS2Pv1Parser.CsvContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#csv}.
	 * @param ctx the parse tree
	 */
	void exitCsv(WS2Pv1Parser.CsvContext ctx);
	/**
	 * Enter a parse tree produced by {@link WS2Pv1Parser#cltv}.
	 * @param ctx the parse tree
	 */
	void enterCltv(WS2Pv1Parser.CltvContext ctx);
	/**
	 * Exit a parse tree produced by {@link WS2Pv1Parser#cltv}.
	 * @param ctx the parse tree
	 */
	void exitCltv(WS2Pv1Parser.CltvContext ctx);
}