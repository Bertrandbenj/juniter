// Generated from DUP.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DUPParser}.
 */
public interface DUPListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DUPParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(DUPParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(DUPParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#transaction}.
	 * @param ctx the parse tree
	 */
	void enterTransaction(DUPParser.TransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#transaction}.
	 * @param ctx the parse tree
	 */
	void exitTransaction(DUPParser.TransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#revocation}.
	 * @param ctx the parse tree
	 */
	void enterRevocation(DUPParser.RevocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#revocation}.
	 * @param ctx the parse tree
	 */
	void exitRevocation(DUPParser.RevocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#certification}.
	 * @param ctx the parse tree
	 */
	void enterCertification(DUPParser.CertificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#certification}.
	 * @param ctx the parse tree
	 */
	void exitCertification(DUPParser.CertificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#membership}.
	 * @param ctx the parse tree
	 */
	void enterMembership(DUPParser.MembershipContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#membership}.
	 * @param ctx the parse tree
	 */
	void exitMembership(DUPParser.MembershipContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#peer}.
	 * @param ctx the parse tree
	 */
	void enterPeer(DUPParser.PeerContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#peer}.
	 * @param ctx the parse tree
	 */
	void exitPeer(DUPParser.PeerContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#identity}.
	 * @param ctx the parse tree
	 */
	void enterIdentity(DUPParser.IdentityContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#identity}.
	 * @param ctx the parse tree
	 */
	void exitIdentity(DUPParser.IdentityContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#doctype}.
	 * @param ctx the parse tree
	 */
	void enterDoctype(DUPParser.DoctypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#doctype}.
	 * @param ctx the parse tree
	 */
	void exitDoctype(DUPParser.DoctypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#version_}.
	 * @param ctx the parse tree
	 */
	void enterVersion_(DUPParser.Version_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#version_}.
	 * @param ctx the parse tree
	 */
	void exitVersion_(DUPParser.Version_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(DUPParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(DUPParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#currency_}.
	 * @param ctx the parse tree
	 */
	void enterCurrency_(DUPParser.Currency_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#currency_}.
	 * @param ctx the parse tree
	 */
	void exitCurrency_(DUPParser.Currency_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#issuer_}.
	 * @param ctx the parse tree
	 */
	void enterIssuer_(DUPParser.Issuer_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#issuer_}.
	 * @param ctx the parse tree
	 */
	void exitIssuer_(DUPParser.Issuer_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#timestamp_}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp_(DUPParser.Timestamp_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#timestamp_}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp_(DUPParser.Timestamp_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#uniqueID_}.
	 * @param ctx the parse tree
	 */
	void enterUniqueID_(DUPParser.UniqueID_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#uniqueID_}.
	 * @param ctx the parse tree
	 */
	void exitUniqueID_(DUPParser.UniqueID_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#block_}.
	 * @param ctx the parse tree
	 */
	void enterBlock_(DUPParser.Block_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#block_}.
	 * @param ctx the parse tree
	 */
	void exitBlock_(DUPParser.Block_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCertTimestamp(DUPParser.CertTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCertTimestamp(DUPParser.CertTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#certTS}.
	 * @param ctx the parse tree
	 */
	void enterCertTS(DUPParser.CertTSContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#certTS}.
	 * @param ctx the parse tree
	 */
	void exitCertTS(DUPParser.CertTSContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(DUPParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(DUPParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void enterEndpoints(DUPParser.EndpointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void exitEndpoints(DUPParser.EndpointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void enterIdtyissuer(DUPParser.IdtyissuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void exitIdtyissuer(DUPParser.IdtyissuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void enterIdtyUniqueID(DUPParser.IdtyUniqueIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void exitIdtyUniqueID(DUPParser.IdtyUniqueIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterIdtyTimestamp(DUPParser.IdtyTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitIdtyTimestamp(DUPParser.IdtyTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void enterIdtySignature(DUPParser.IdtySignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void exitIdtySignature(DUPParser.IdtySignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#inputs}.
	 * @param ctx the parse tree
	 */
	void enterInputs(DUPParser.InputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#inputs}.
	 * @param ctx the parse tree
	 */
	void exitInputs(DUPParser.InputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#issuers}.
	 * @param ctx the parse tree
	 */
	void enterIssuers(DUPParser.IssuersContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#issuers}.
	 * @param ctx the parse tree
	 */
	void exitIssuers(DUPParser.IssuersContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#locktime}.
	 * @param ctx the parse tree
	 */
	void enterLocktime(DUPParser.LocktimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#locktime}.
	 * @param ctx the parse tree
	 */
	void exitLocktime(DUPParser.LocktimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(DUPParser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(DUPParser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(DUPParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(DUPParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#outputs}.
	 * @param ctx the parse tree
	 */
	void enterOutputs(DUPParser.OutputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#outputs}.
	 * @param ctx the parse tree
	 */
	void exitOutputs(DUPParser.OutputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#poWMin}.
	 * @param ctx the parse tree
	 */
	void enterPoWMin(DUPParser.PoWMinContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#poWMin}.
	 * @param ctx the parse tree
	 */
	void exitPoWMin(DUPParser.PoWMinContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#publicKey}.
	 * @param ctx the parse tree
	 */
	void enterPublicKey(DUPParser.PublicKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#publicKey}.
	 * @param ctx the parse tree
	 */
	void exitPublicKey(DUPParser.PublicKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#signatures}.
	 * @param ctx the parse tree
	 */
	void enterSignatures(DUPParser.SignaturesContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#signatures}.
	 * @param ctx the parse tree
	 */
	void exitSignatures(DUPParser.SignaturesContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#userID}.
	 * @param ctx the parse tree
	 */
	void enterUserID(DUPParser.UserIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#userID}.
	 * @param ctx the parse tree
	 */
	void exitUserID(DUPParser.UserIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#unlocks}.
	 * @param ctx the parse tree
	 */
	void enterUnlocks(DUPParser.UnlocksContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#unlocks}.
	 * @param ctx the parse tree
	 */
	void exitUnlocks(DUPParser.UnlocksContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#buid}.
	 * @param ctx the parse tree
	 */
	void enterBuid(DUPParser.BuidContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#buid}.
	 * @param ctx the parse tree
	 */
	void exitBuid(DUPParser.BuidContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#issuer}.
	 * @param ctx the parse tree
	 */
	void enterIssuer(DUPParser.IssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#issuer}.
	 * @param ctx the parse tree
	 */
	void exitIssuer(DUPParser.IssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#signature}.
	 * @param ctx the parse tree
	 */
	void enterSignature(DUPParser.SignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#signature}.
	 * @param ctx the parse tree
	 */
	void exitSignature(DUPParser.SignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#input_}.
	 * @param ctx the parse tree
	 */
	void enterInput_(DUPParser.Input_Context ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#input_}.
	 * @param ctx the parse tree
	 */
	void exitInput_(DUPParser.Input_Context ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#unlock}.
	 * @param ctx the parse tree
	 */
	void enterUnlock(DUPParser.UnlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#unlock}.
	 * @param ctx the parse tree
	 */
	void exitUnlock(DUPParser.UnlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#unsig}.
	 * @param ctx the parse tree
	 */
	void enterUnsig(DUPParser.UnsigContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#unsig}.
	 * @param ctx the parse tree
	 */
	void exitUnsig(DUPParser.UnsigContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#unxhx}.
	 * @param ctx the parse tree
	 */
	void enterUnxhx(DUPParser.UnxhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#unxhx}.
	 * @param ctx the parse tree
	 */
	void exitUnxhx(DUPParser.UnxhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#output}.
	 * @param ctx the parse tree
	 */
	void enterOutput(DUPParser.OutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#output}.
	 * @param ctx the parse tree
	 */
	void exitOutput(DUPParser.OutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint(DUPParser.EndpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint(DUPParser.EndpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(DUPParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(DUPParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(DUPParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(DUPParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(DUPParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(DUPParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#sig}.
	 * @param ctx the parse tree
	 */
	void enterSig(DUPParser.SigContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#sig}.
	 * @param ctx the parse tree
	 */
	void exitSig(DUPParser.SigContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#xhx}.
	 * @param ctx the parse tree
	 */
	void enterXhx(DUPParser.XhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#xhx}.
	 * @param ctx the parse tree
	 */
	void exitXhx(DUPParser.XhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#csv}.
	 * @param ctx the parse tree
	 */
	void enterCsv(DUPParser.CsvContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#csv}.
	 * @param ctx the parse tree
	 */
	void exitCsv(DUPParser.CsvContext ctx);
	/**
	 * Enter a parse tree produced by {@link DUPParser#cltv}.
	 * @param ctx the parse tree
	 */
	void enterCltv(DUPParser.CltvContext ctx);
	/**
	 * Exit a parse tree produced by {@link DUPParser#cltv}.
	 * @param ctx the parse tree
	 */
	void exitCltv(DUPParser.CltvContext ctx);
}