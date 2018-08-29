// Generated from Juniter.g4 by ANTLR 4.7.1
 
package juniter.grammar.antlr4; 

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JuniterParser}.
 */
public interface JuniterListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JuniterParser#document}.
	 * @param ctx the parse tree
	 */
	void enterDocument(JuniterParser.DocumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#document}.
	 * @param ctx the parse tree
	 */
	void exitDocument(JuniterParser.DocumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#transaction}.
	 * @param ctx the parse tree
	 */
	void enterTransaction(JuniterParser.TransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#transaction}.
	 * @param ctx the parse tree
	 */
	void exitTransaction(JuniterParser.TransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#revocation}.
	 * @param ctx the parse tree
	 */
	void enterRevocation(JuniterParser.RevocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#revocation}.
	 * @param ctx the parse tree
	 */
	void exitRevocation(JuniterParser.RevocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#certification}.
	 * @param ctx the parse tree
	 */
	void enterCertification(JuniterParser.CertificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#certification}.
	 * @param ctx the parse tree
	 */
	void exitCertification(JuniterParser.CertificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#membership}.
	 * @param ctx the parse tree
	 */
	void enterMembership(JuniterParser.MembershipContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#membership}.
	 * @param ctx the parse tree
	 */
	void exitMembership(JuniterParser.MembershipContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#peer}.
	 * @param ctx the parse tree
	 */
	void enterPeer(JuniterParser.PeerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#peer}.
	 * @param ctx the parse tree
	 */
	void exitPeer(JuniterParser.PeerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#identity}.
	 * @param ctx the parse tree
	 */
	void enterIdentity(JuniterParser.IdentityContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#identity}.
	 * @param ctx the parse tree
	 */
	void exitIdentity(JuniterParser.IdentityContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#doctype}.
	 * @param ctx the parse tree
	 */
	void enterDoctype(JuniterParser.DoctypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#doctype}.
	 * @param ctx the parse tree
	 */
	void exitDoctype(JuniterParser.DoctypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#version_}.
	 * @param ctx the parse tree
	 */
	void enterVersion_(JuniterParser.Version_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#version_}.
	 * @param ctx the parse tree
	 */
	void exitVersion_(JuniterParser.Version_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(JuniterParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(JuniterParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#currency_}.
	 * @param ctx the parse tree
	 */
	void enterCurrency_(JuniterParser.Currency_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#currency_}.
	 * @param ctx the parse tree
	 */
	void exitCurrency_(JuniterParser.Currency_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#issuer_}.
	 * @param ctx the parse tree
	 */
	void enterIssuer_(JuniterParser.Issuer_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuer_}.
	 * @param ctx the parse tree
	 */
	void exitIssuer_(JuniterParser.Issuer_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#timestamp_}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp_(JuniterParser.Timestamp_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#timestamp_}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp_(JuniterParser.Timestamp_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#uniqueID_}.
	 * @param ctx the parse tree
	 */
	void enterUniqueID_(JuniterParser.UniqueID_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#uniqueID_}.
	 * @param ctx the parse tree
	 */
	void exitUniqueID_(JuniterParser.UniqueID_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#block_}.
	 * @param ctx the parse tree
	 */
	void enterBlock_(JuniterParser.Block_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#block_}.
	 * @param ctx the parse tree
	 */
	void exitBlock_(JuniterParser.Block_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterCertTimestamp(JuniterParser.CertTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#certTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitCertTimestamp(JuniterParser.CertTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#certTS}.
	 * @param ctx the parse tree
	 */
	void enterCertTS(JuniterParser.CertTSContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#certTS}.
	 * @param ctx the parse tree
	 */
	void exitCertTS(JuniterParser.CertTSContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#comment}.
	 * @param ctx the parse tree
	 */
	void enterComment(JuniterParser.CommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#comment}.
	 * @param ctx the parse tree
	 */
	void exitComment(JuniterParser.CommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void enterEndpoints(JuniterParser.EndpointsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#endpoints}.
	 * @param ctx the parse tree
	 */
	void exitEndpoints(JuniterParser.EndpointsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void enterIdtyissuer(JuniterParser.IdtyissuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtyissuer}.
	 * @param ctx the parse tree
	 */
	void exitIdtyissuer(JuniterParser.IdtyissuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void enterIdtyUniqueID(JuniterParser.IdtyUniqueIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtyUniqueID}.
	 * @param ctx the parse tree
	 */
	void exitIdtyUniqueID(JuniterParser.IdtyUniqueIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void enterIdtyTimestamp(JuniterParser.IdtyTimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtyTimestamp}.
	 * @param ctx the parse tree
	 */
	void exitIdtyTimestamp(JuniterParser.IdtyTimestampContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void enterIdtySignature(JuniterParser.IdtySignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtySignature}.
	 * @param ctx the parse tree
	 */
	void exitIdtySignature(JuniterParser.IdtySignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#inputs}.
	 * @param ctx the parse tree
	 */
	void enterInputs(JuniterParser.InputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#inputs}.
	 * @param ctx the parse tree
	 */
	void exitInputs(JuniterParser.InputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#issuers}.
	 * @param ctx the parse tree
	 */
	void enterIssuers(JuniterParser.IssuersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuers}.
	 * @param ctx the parse tree
	 */
	void exitIssuers(JuniterParser.IssuersContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#locktime}.
	 * @param ctx the parse tree
	 */
	void enterLocktime(JuniterParser.LocktimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#locktime}.
	 * @param ctx the parse tree
	 */
	void exitLocktime(JuniterParser.LocktimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#member}.
	 * @param ctx the parse tree
	 */
	void enterMember(JuniterParser.MemberContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#member}.
	 * @param ctx the parse tree
	 */
	void exitMember(JuniterParser.MemberContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#number}.
	 * @param ctx the parse tree
	 */
	void enterNumber(JuniterParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#number}.
	 * @param ctx the parse tree
	 */
	void exitNumber(JuniterParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#outputs}.
	 * @param ctx the parse tree
	 */
	void enterOutputs(JuniterParser.OutputsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#outputs}.
	 * @param ctx the parse tree
	 */
	void exitOutputs(JuniterParser.OutputsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#poWMin}.
	 * @param ctx the parse tree
	 */
	void enterPoWMin(JuniterParser.PoWMinContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#poWMin}.
	 * @param ctx the parse tree
	 */
	void exitPoWMin(JuniterParser.PoWMinContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#publicKey}.
	 * @param ctx the parse tree
	 */
	void enterPublicKey(JuniterParser.PublicKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#publicKey}.
	 * @param ctx the parse tree
	 */
	void exitPublicKey(JuniterParser.PublicKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#signatures}.
	 * @param ctx the parse tree
	 */
	void enterSignatures(JuniterParser.SignaturesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#signatures}.
	 * @param ctx the parse tree
	 */
	void exitSignatures(JuniterParser.SignaturesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#userID}.
	 * @param ctx the parse tree
	 */
	void enterUserID(JuniterParser.UserIDContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#userID}.
	 * @param ctx the parse tree
	 */
	void exitUserID(JuniterParser.UserIDContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#unlocks}.
	 * @param ctx the parse tree
	 */
	void enterUnlocks(JuniterParser.UnlocksContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#unlocks}.
	 * @param ctx the parse tree
	 */
	void exitUnlocks(JuniterParser.UnlocksContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#buid}.
	 * @param ctx the parse tree
	 */
	void enterBuid(JuniterParser.BuidContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#buid}.
	 * @param ctx the parse tree
	 */
	void exitBuid(JuniterParser.BuidContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#dassh}.
	 * @param ctx the parse tree
	 */
	void enterDassh(JuniterParser.DasshContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#dassh}.
	 * @param ctx the parse tree
	 */
	void exitDassh(JuniterParser.DasshContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#intt}.
	 * @param ctx the parse tree
	 */
	void enterIntt(JuniterParser.InttContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#intt}.
	 * @param ctx the parse tree
	 */
	void exitIntt(JuniterParser.InttContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#hashh}.
	 * @param ctx the parse tree
	 */
	void enterHashh(JuniterParser.HashhContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#hashh}.
	 * @param ctx the parse tree
	 */
	void exitHashh(JuniterParser.HashhContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#issuer}.
	 * @param ctx the parse tree
	 */
	void enterIssuer(JuniterParser.IssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuer}.
	 * @param ctx the parse tree
	 */
	void exitIssuer(JuniterParser.IssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#signature}.
	 * @param ctx the parse tree
	 */
	void enterSignature(JuniterParser.SignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#signature}.
	 * @param ctx the parse tree
	 */
	void exitSignature(JuniterParser.SignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#input_}.
	 * @param ctx the parse tree
	 */
	void enterInput_(JuniterParser.Input_Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#input_}.
	 * @param ctx the parse tree
	 */
	void exitInput_(JuniterParser.Input_Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#unlock}.
	 * @param ctx the parse tree
	 */
	void enterUnlock(JuniterParser.UnlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#unlock}.
	 * @param ctx the parse tree
	 */
	void exitUnlock(JuniterParser.UnlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#unsig}.
	 * @param ctx the parse tree
	 */
	void enterUnsig(JuniterParser.UnsigContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#unsig}.
	 * @param ctx the parse tree
	 */
	void exitUnsig(JuniterParser.UnsigContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#unxhx}.
	 * @param ctx the parse tree
	 */
	void enterUnxhx(JuniterParser.UnxhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#unxhx}.
	 * @param ctx the parse tree
	 */
	void exitUnxhx(JuniterParser.UnxhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#output}.
	 * @param ctx the parse tree
	 */
	void enterOutput(JuniterParser.OutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#output}.
	 * @param ctx the parse tree
	 */
	void exitOutput(JuniterParser.OutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void enterEndpoint(JuniterParser.EndpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#endpoint}.
	 * @param ctx the parse tree
	 */
	void exitEndpoint(JuniterParser.EndpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(JuniterParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(JuniterParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#and}.
	 * @param ctx the parse tree
	 */
	void enterAnd(JuniterParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#and}.
	 * @param ctx the parse tree
	 */
	void exitAnd(JuniterParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#or}.
	 * @param ctx the parse tree
	 */
	void enterOr(JuniterParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#or}.
	 * @param ctx the parse tree
	 */
	void exitOr(JuniterParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sig}.
	 * @param ctx the parse tree
	 */
	void enterSig(JuniterParser.SigContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sig}.
	 * @param ctx the parse tree
	 */
	void exitSig(JuniterParser.SigContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#xhx}.
	 * @param ctx the parse tree
	 */
	void enterXhx(JuniterParser.XhxContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#xhx}.
	 * @param ctx the parse tree
	 */
	void exitXhx(JuniterParser.XhxContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#csv}.
	 * @param ctx the parse tree
	 */
	void enterCsv(JuniterParser.CsvContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#csv}.
	 * @param ctx the parse tree
	 */
	void exitCsv(JuniterParser.CsvContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cltv}.
	 * @param ctx the parse tree
	 */
	void enterCltv(JuniterParser.CltvContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cltv}.
	 * @param ctx the parse tree
	 */
	void exitCltv(JuniterParser.CltvContext ctx);
}