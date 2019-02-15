// Generated from JuniterParser.g4 by ANTLR 4.7.1
 
package antlr.generated;
//import juniter.crypto.CryptoUtils;
import java.lang.Integer;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JuniterParser}.
 */
public interface JuniterParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JuniterParser#doc}.
	 * @param ctx the parse tree
	 */
	void enterDoc(JuniterParser.DocContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#doc}.
	 * @param ctx the parse tree
	 */
	void exitDoc(JuniterParser.DocContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#cpt_transactions}.
	 * @param ctx the parse tree
	 */
	void enterCpt_transactions(JuniterParser.Cpt_transactionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_transactions}.
	 * @param ctx the parse tree
	 */
	void exitCpt_transactions(JuniterParser.Cpt_transactionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#transactions}.
	 * @param ctx the parse tree
	 */
	void enterTransactions(JuniterParser.TransactionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#transactions}.
	 * @param ctx the parse tree
	 */
	void exitTransactions(JuniterParser.TransactionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#compactTransaction}.
	 * @param ctx the parse tree
	 */
	void enterCompactTransaction(JuniterParser.CompactTransactionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#compactTransaction}.
	 * @param ctx the parse tree
	 */
	void exitCompactTransaction(JuniterParser.CompactTransactionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#nbComment}.
	 * @param ctx the parse tree
	 */
	void enterNbComment(JuniterParser.NbCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nbComment}.
	 * @param ctx the parse tree
	 */
	void exitNbComment(JuniterParser.NbCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_comm}.
	 * @param ctx the parse tree
	 */
	void enterCpt_comm(JuniterParser.Cpt_commContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_comm}.
	 * @param ctx the parse tree
	 */
	void exitCpt_comm(JuniterParser.Cpt_commContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_signature}.
	 * @param ctx the parse tree
	 */
	void enterCpt_signature(JuniterParser.Cpt_signatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_signature}.
	 * @param ctx the parse tree
	 */
	void exitCpt_signature(JuniterParser.Cpt_signatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_out}.
	 * @param ctx the parse tree
	 */
	void enterCpt_out(JuniterParser.Cpt_outContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_out}.
	 * @param ctx the parse tree
	 */
	void exitCpt_out(JuniterParser.Cpt_outContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_in}.
	 * @param ctx the parse tree
	 */
	void enterCpt_in(JuniterParser.Cpt_inContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_in}.
	 * @param ctx the parse tree
	 */
	void exitCpt_in(JuniterParser.Cpt_inContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_unlock}.
	 * @param ctx the parse tree
	 */
	void enterCpt_unlock(JuniterParser.Cpt_unlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_unlock}.
	 * @param ctx the parse tree
	 */
	void exitCpt_unlock(JuniterParser.Cpt_unlockContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#issuers_compact}.
	 * @param ctx the parse tree
	 */
	void enterIssuers_compact(JuniterParser.Issuers_compactContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuers_compact}.
	 * @param ctx the parse tree
	 */
	void exitIssuers_compact(JuniterParser.Issuers_compactContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#modeSwitch}.
	 * @param ctx the parse tree
	 */
	void enterModeSwitch(JuniterParser.ModeSwitchContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#modeSwitch}.
	 * @param ctx the parse tree
	 */
	void exitModeSwitch(JuniterParser.ModeSwitchContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#certifications}.
	 * @param ctx the parse tree
	 */
	void enterCertifications(JuniterParser.CertificationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#certifications}.
	 * @param ctx the parse tree
	 */
	void exitCertifications(JuniterParser.CertificationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#excluded}.
	 * @param ctx the parse tree
	 */
	void enterExcluded(JuniterParser.ExcludedContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#excluded}.
	 * @param ctx the parse tree
	 */
	void exitExcluded(JuniterParser.ExcludedContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#revoked}.
	 * @param ctx the parse tree
	 */
	void enterRevoked(JuniterParser.RevokedContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#revoked}.
	 * @param ctx the parse tree
	 */
	void exitRevoked(JuniterParser.RevokedContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#leavers}.
	 * @param ctx the parse tree
	 */
	void enterLeavers(JuniterParser.LeaversContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#leavers}.
	 * @param ctx the parse tree
	 */
	void exitLeavers(JuniterParser.LeaversContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#renewed}.
	 * @param ctx the parse tree
	 */
	void enterRenewed(JuniterParser.RenewedContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#renewed}.
	 * @param ctx the parse tree
	 */
	void exitRenewed(JuniterParser.RenewedContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#joiners}.
	 * @param ctx the parse tree
	 */
	void enterJoiners(JuniterParser.JoinersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#joiners}.
	 * @param ctx the parse tree
	 */
	void exitJoiners(JuniterParser.JoinersContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_joiner}.
	 * @param ctx the parse tree
	 */
	void enterCpt_joiner(JuniterParser.Cpt_joinerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_joiner}.
	 * @param ctx the parse tree
	 */
	void exitCpt_joiner(JuniterParser.Cpt_joinerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#identities}.
	 * @param ctx the parse tree
	 */
	void enterIdentities(JuniterParser.IdentitiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#identities}.
	 * @param ctx the parse tree
	 */
	void exitIdentities(JuniterParser.IdentitiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#cpt_idty}.
	 * @param ctx the parse tree
	 */
	void enterCpt_idty(JuniterParser.Cpt_idtyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#cpt_idty}.
	 * @param ctx the parse tree
	 */
	void exitCpt_idty(JuniterParser.Cpt_idtyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#membersCount}.
	 * @param ctx the parse tree
	 */
	void enterMembersCount(JuniterParser.MembersCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#membersCount}.
	 * @param ctx the parse tree
	 */
	void exitMembersCount(JuniterParser.MembersCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(JuniterParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(JuniterParser.ParametersContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#wot}.
	 * @param ctx the parse tree
	 */
	void enterWot(JuniterParser.WotContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#wot}.
	 * @param ctx the parse tree
	 */
	void exitWot(JuniterParser.WotContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#enpoint}.
	 * @param ctx the parse tree
	 */
	void enterEnpoint(JuniterParser.EnpointContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#enpoint}.
	 * @param ctx the parse tree
	 */
	void exitEnpoint(JuniterParser.EnpointContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sessionid}.
	 * @param ctx the parse tree
	 */
	void enterSessionid(JuniterParser.SessionidContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sessionid}.
	 * @param ctx the parse tree
	 */
	void exitSessionid(JuniterParser.SessionidContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#blockstamp}.
	 * @param ctx the parse tree
	 */
	void enterBlockstamp(JuniterParser.BlockstampContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#blockstamp}.
	 * @param ctx the parse tree
	 */
	void exitBlockstamp(JuniterParser.BlockstampContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#input}.
	 * @param ctx the parse tree
	 */
	void enterInput(JuniterParser.InputContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#input}.
	 * @param ctx the parse tree
	 */
	void exitInput(JuniterParser.InputContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#ud}.
	 * @param ctx the parse tree
	 */
	void enterUd(JuniterParser.UdContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#ud}.
	 * @param ctx the parse tree
	 */
	void exitUd(JuniterParser.UdContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#tx}.
	 * @param ctx the parse tree
	 */
	void enterTx(JuniterParser.TxContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#tx}.
	 * @param ctx the parse tree
	 */
	void exitTx(JuniterParser.TxContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#amount}.
	 * @param ctx the parse tree
	 */
	void enterAmount(JuniterParser.AmountContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#amount}.
	 * @param ctx the parse tree
	 */
	void exitAmount(JuniterParser.AmountContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#base}.
	 * @param ctx the parse tree
	 */
	void enterBase(JuniterParser.BaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#base}.
	 * @param ctx the parse tree
	 */
	void exitBase(JuniterParser.BaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#tindex}.
	 * @param ctx the parse tree
	 */
	void enterTindex(JuniterParser.TindexContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#tindex}.
	 * @param ctx the parse tree
	 */
	void exitTindex(JuniterParser.TindexContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#in_index}.
	 * @param ctx the parse tree
	 */
	void enterIn_index(JuniterParser.In_indexContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#in_index}.
	 * @param ctx the parse tree
	 */
	void exitIn_index(JuniterParser.In_indexContext ctx);
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
	/**
	 * Enter a parse tree produced by {@link JuniterParser#outParam}.
	 * @param ctx the parse tree
	 */
	void enterOutParam(JuniterParser.OutParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#outParam}.
	 * @param ctx the parse tree
	 */
	void exitOutParam(JuniterParser.OutParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JuniterParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JuniterParser.BlockContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void enterTimestamp(JuniterParser.TimestampContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#timestamp}.
	 * @param ctx the parse tree
	 */
	void exitTimestamp(JuniterParser.TimestampContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#userid}.
	 * @param ctx the parse tree
	 */
	void enterUserid(JuniterParser.UseridContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#userid}.
	 * @param ctx the parse tree
	 */
	void exitUserid(JuniterParser.UseridContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#pubkey}.
	 * @param ctx the parse tree
	 */
	void enterPubkey(JuniterParser.PubkeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#pubkey}.
	 * @param ctx the parse tree
	 */
	void exitPubkey(JuniterParser.PubkeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#currency}.
	 * @param ctx the parse tree
	 */
	void enterCurrency(JuniterParser.CurrencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#currency}.
	 * @param ctx the parse tree
	 */
	void exitCurrency(JuniterParser.CurrencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#version}.
	 * @param ctx the parse tree
	 */
	void enterVersion(JuniterParser.VersionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#version}.
	 * @param ctx the parse tree
	 */
	void exitVersion(JuniterParser.VersionContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#bnum}.
	 * @param ctx the parse tree
	 */
	void enterBnum(JuniterParser.BnumContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#bnum}.
	 * @param ctx the parse tree
	 */
	void exitBnum(JuniterParser.BnumContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#bhash}.
	 * @param ctx the parse tree
	 */
	void enterBhash(JuniterParser.BhashContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#bhash}.
	 * @param ctx the parse tree
	 */
	void exitBhash(JuniterParser.BhashContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#percentRot}.
	 * @param ctx the parse tree
	 */
	void enterPercentRot(JuniterParser.PercentRotContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#percentRot}.
	 * @param ctx the parse tree
	 */
	void exitPercentRot(JuniterParser.PercentRotContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#dtDiffEval}.
	 * @param ctx the parse tree
	 */
	void enterDtDiffEval(JuniterParser.DtDiffEvalContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#dtDiffEval}.
	 * @param ctx the parse tree
	 */
	void exitDtDiffEval(JuniterParser.DtDiffEvalContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#avgGenTime}.
	 * @param ctx the parse tree
	 */
	void enterAvgGenTime(JuniterParser.AvgGenTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#avgGenTime}.
	 * @param ctx the parse tree
	 */
	void exitAvgGenTime(JuniterParser.AvgGenTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#medianTimeBlocks}.
	 * @param ctx the parse tree
	 */
	void enterMedianTimeBlocks(JuniterParser.MedianTimeBlocksContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#medianTimeBlocks}.
	 * @param ctx the parse tree
	 */
	void exitMedianTimeBlocks(JuniterParser.MedianTimeBlocksContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#stepMax}.
	 * @param ctx the parse tree
	 */
	void enterStepMax(JuniterParser.StepMaxContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#stepMax}.
	 * @param ctx the parse tree
	 */
	void exitStepMax(JuniterParser.StepMaxContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#msValidity}.
	 * @param ctx the parse tree
	 */
	void enterMsValidity(JuniterParser.MsValidityContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#msValidity}.
	 * @param ctx the parse tree
	 */
	void exitMsValidity(JuniterParser.MsValidityContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#xpercent}.
	 * @param ctx the parse tree
	 */
	void enterXpercent(JuniterParser.XpercentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#xpercent}.
	 * @param ctx the parse tree
	 */
	void exitXpercent(JuniterParser.XpercentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#msWindow}.
	 * @param ctx the parse tree
	 */
	void enterMsWindow(JuniterParser.MsWindowContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#msWindow}.
	 * @param ctx the parse tree
	 */
	void exitMsWindow(JuniterParser.MsWindowContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#idtyWindow}.
	 * @param ctx the parse tree
	 */
	void enterIdtyWindow(JuniterParser.IdtyWindowContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtyWindow}.
	 * @param ctx the parse tree
	 */
	void exitIdtyWindow(JuniterParser.IdtyWindowContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sigQty}.
	 * @param ctx the parse tree
	 */
	void enterSigQty(JuniterParser.SigQtyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sigQty}.
	 * @param ctx the parse tree
	 */
	void exitSigQty(JuniterParser.SigQtyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sigValidity}.
	 * @param ctx the parse tree
	 */
	void enterSigValidity(JuniterParser.SigValidityContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sigValidity}.
	 * @param ctx the parse tree
	 */
	void exitSigValidity(JuniterParser.SigValidityContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sigWindow}.
	 * @param ctx the parse tree
	 */
	void enterSigWindow(JuniterParser.SigWindowContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sigWindow}.
	 * @param ctx the parse tree
	 */
	void exitSigWindow(JuniterParser.SigWindowContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sigStock}.
	 * @param ctx the parse tree
	 */
	void enterSigStock(JuniterParser.SigStockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sigStock}.
	 * @param ctx the parse tree
	 */
	void exitSigStock(JuniterParser.SigStockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#sigPeriod}.
	 * @param ctx the parse tree
	 */
	void enterSigPeriod(JuniterParser.SigPeriodContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#sigPeriod}.
	 * @param ctx the parse tree
	 */
	void exitSigPeriod(JuniterParser.SigPeriodContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#udReevalTime0}.
	 * @param ctx the parse tree
	 */
	void enterUdReevalTime0(JuniterParser.UdReevalTime0Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#udReevalTime0}.
	 * @param ctx the parse tree
	 */
	void exitUdReevalTime0(JuniterParser.UdReevalTime0Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#udTime0}.
	 * @param ctx the parse tree
	 */
	void enterUdTime0(JuniterParser.UdTime0Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#udTime0}.
	 * @param ctx the parse tree
	 */
	void exitUdTime0(JuniterParser.UdTime0Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#dtReeval}.
	 * @param ctx the parse tree
	 */
	void enterDtReeval(JuniterParser.DtReevalContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#dtReeval}.
	 * @param ctx the parse tree
	 */
	void exitDtReeval(JuniterParser.DtReevalContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#dt}.
	 * @param ctx the parse tree
	 */
	void enterDt(JuniterParser.DtContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#dt}.
	 * @param ctx the parse tree
	 */
	void exitDt(JuniterParser.DtContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#c}.
	 * @param ctx the parse tree
	 */
	void enterC(JuniterParser.CContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#c}.
	 * @param ctx the parse tree
	 */
	void exitC(JuniterParser.CContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#ud0}.
	 * @param ctx the parse tree
	 */
	void enterUd0(JuniterParser.Ud0Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#ud0}.
	 * @param ctx the parse tree
	 */
	void exitUd0(JuniterParser.Ud0Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#previousIssuer}.
	 * @param ctx the parse tree
	 */
	void enterPreviousIssuer(JuniterParser.PreviousIssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#previousIssuer}.
	 * @param ctx the parse tree
	 */
	void exitPreviousIssuer(JuniterParser.PreviousIssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#previousHash}.
	 * @param ctx the parse tree
	 */
	void enterPreviousHash(JuniterParser.PreviousHashContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#previousHash}.
	 * @param ctx the parse tree
	 */
	void exitPreviousHash(JuniterParser.PreviousHashContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#differentIssuersCount}.
	 * @param ctx the parse tree
	 */
	void enterDifferentIssuersCount(JuniterParser.DifferentIssuersCountContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#differentIssuersCount}.
	 * @param ctx the parse tree
	 */
	void exitDifferentIssuersCount(JuniterParser.DifferentIssuersCountContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#issuersFrameVar}.
	 * @param ctx the parse tree
	 */
	void enterIssuersFrameVar(JuniterParser.IssuersFrameVarContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuersFrameVar}.
	 * @param ctx the parse tree
	 */
	void exitIssuersFrameVar(JuniterParser.IssuersFrameVarContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#issuersFrame}.
	 * @param ctx the parse tree
	 */
	void enterIssuersFrame(JuniterParser.IssuersFrameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#issuersFrame}.
	 * @param ctx the parse tree
	 */
	void exitIssuersFrame(JuniterParser.IssuersFrameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#unitBase}.
	 * @param ctx the parse tree
	 */
	void enterUnitBase(JuniterParser.UnitBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#unitBase}.
	 * @param ctx the parse tree
	 */
	void exitUnitBase(JuniterParser.UnitBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#universalDividend}.
	 * @param ctx the parse tree
	 */
	void enterUniversalDividend(JuniterParser.UniversalDividendContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#universalDividend}.
	 * @param ctx the parse tree
	 */
	void exitUniversalDividend(JuniterParser.UniversalDividendContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#medianTime}.
	 * @param ctx the parse tree
	 */
	void enterMedianTime(JuniterParser.MedianTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#medianTime}.
	 * @param ctx the parse tree
	 */
	void exitMedianTime(JuniterParser.MedianTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#time}.
	 * @param ctx the parse tree
	 */
	void enterTime(JuniterParser.TimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#time}.
	 * @param ctx the parse tree
	 */
	void exitTime(JuniterParser.TimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#powMin}.
	 * @param ctx the parse tree
	 */
	void enterPowMin(JuniterParser.PowMinContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#powMin}.
	 * @param ctx the parse tree
	 */
	void exitPowMin(JuniterParser.PowMinContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#nonce}.
	 * @param ctx the parse tree
	 */
	void enterNonce(JuniterParser.NonceContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nonce}.
	 * @param ctx the parse tree
	 */
	void exitNonce(JuniterParser.NonceContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#innerHash}.
	 * @param ctx the parse tree
	 */
	void enterInnerHash(JuniterParser.InnerHashContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#innerHash}.
	 * @param ctx the parse tree
	 */
	void exitInnerHash(JuniterParser.InnerHashContext ctx);
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
	 * Enter a parse tree produced by {@link JuniterParser#idtyIssuer}.
	 * @param ctx the parse tree
	 */
	void enterIdtyIssuer(JuniterParser.IdtyIssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#idtyIssuer}.
	 * @param ctx the parse tree
	 */
	void exitIdtyIssuer(JuniterParser.IdtyIssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#port}.
	 * @param ctx the parse tree
	 */
	void enterPort(JuniterParser.PortContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#port}.
	 * @param ctx the parse tree
	 */
	void exitPort(JuniterParser.PortContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#ip6}.
	 * @param ctx the parse tree
	 */
	void enterIp6(JuniterParser.Ip6Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#ip6}.
	 * @param ctx the parse tree
	 */
	void exitIp6(JuniterParser.Ip6Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#ip4}.
	 * @param ctx the parse tree
	 */
	void enterIp4(JuniterParser.Ip4Context ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#ip4}.
	 * @param ctx the parse tree
	 */
	void exitIp4(JuniterParser.Ip4Context ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#dns}.
	 * @param ctx the parse tree
	 */
	void enterDns(JuniterParser.DnsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#dns}.
	 * @param ctx the parse tree
	 */
	void exitDns(JuniterParser.DnsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#iBlockUid}.
	 * @param ctx the parse tree
	 */
	void enterIBlockUid(JuniterParser.IBlockUidContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#iBlockUid}.
	 * @param ctx the parse tree
	 */
	void exitIBlockUid(JuniterParser.IBlockUidContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#mBlockUid}.
	 * @param ctx the parse tree
	 */
	void enterMBlockUid(JuniterParser.MBlockUidContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#mBlockUid}.
	 * @param ctx the parse tree
	 */
	void exitMBlockUid(JuniterParser.MBlockUidContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#endpointType}.
	 * @param ctx the parse tree
	 */
	void enterEndpointType(JuniterParser.EndpointTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#endpointType}.
	 * @param ctx the parse tree
	 */
	void exitEndpointType(JuniterParser.EndpointTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#toPK}.
	 * @param ctx the parse tree
	 */
	void enterToPK(JuniterParser.ToPKContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#toPK}.
	 * @param ctx the parse tree
	 */
	void exitToPK(JuniterParser.ToPKContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#blockstampTime}.
	 * @param ctx the parse tree
	 */
	void enterBlockstampTime(JuniterParser.BlockstampTimeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#blockstampTime}.
	 * @param ctx the parse tree
	 */
	void exitBlockstampTime(JuniterParser.BlockstampTimeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#nbOutput}.
	 * @param ctx the parse tree
	 */
	void enterNbOutput(JuniterParser.NbOutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nbOutput}.
	 * @param ctx the parse tree
	 */
	void exitNbOutput(JuniterParser.NbOutputContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#nbUnlock}.
	 * @param ctx the parse tree
	 */
	void enterNbUnlock(JuniterParser.NbUnlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nbUnlock}.
	 * @param ctx the parse tree
	 */
	void exitNbUnlock(JuniterParser.NbUnlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#nbInput}.
	 * @param ctx the parse tree
	 */
	void enterNbInput(JuniterParser.NbInputContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nbInput}.
	 * @param ctx the parse tree
	 */
	void exitNbInput(JuniterParser.NbInputContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#nbIssuer}.
	 * @param ctx the parse tree
	 */
	void enterNbIssuer(JuniterParser.NbIssuerContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#nbIssuer}.
	 * @param ctx the parse tree
	 */
	void exitNbIssuer(JuniterParser.NbIssuerContext ctx);
	/**
	 * Enter a parse tree produced by {@link JuniterParser#fromPK}.
	 * @param ctx the parse tree
	 */
	void enterFromPK(JuniterParser.FromPKContext ctx);
	/**
	 * Exit a parse tree produced by {@link JuniterParser#fromPK}.
	 * @param ctx the parse tree
	 */
	void exitFromPK(JuniterParser.FromPKContext ctx);
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
}