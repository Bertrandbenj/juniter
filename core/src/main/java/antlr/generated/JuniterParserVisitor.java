// Generated from JuniterParser.g4 by ANTLR 4.7.1
 
package antlr.generated;
//import juniter.crypto.CryptoUtils;
import java.lang.Integer;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JuniterParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JuniterParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JuniterParser#doc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoc(JuniterParser.DocContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#block_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock_(JuniterParser.Block_Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_transactions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_transactions(JuniterParser.Cpt_transactionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#transactions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransactions(JuniterParser.TransactionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#compactTransaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompactTransaction(JuniterParser.CompactTransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nbComment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNbComment(JuniterParser.NbCommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_comm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_comm(JuniterParser.Cpt_commContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_signature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_signature(JuniterParser.Cpt_signatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_out}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_out(JuniterParser.Cpt_outContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_in}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_in(JuniterParser.Cpt_inContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_unlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_unlock(JuniterParser.Cpt_unlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#issuers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIssuers(JuniterParser.IssuersContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#issuers_compact}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIssuers_compact(JuniterParser.Issuers_compactContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#modeSwitch}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModeSwitch(JuniterParser.ModeSwitchContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#certifications}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCertifications(JuniterParser.CertificationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#excluded}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExcluded(JuniterParser.ExcludedContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#revoked}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevoked(JuniterParser.RevokedContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#leavers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLeavers(JuniterParser.LeaversContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#renewed}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRenewed(JuniterParser.RenewedContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#joiners}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoiners(JuniterParser.JoinersContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_joiner}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_joiner(JuniterParser.Cpt_joinerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#identities}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentities(JuniterParser.IdentitiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cpt_idty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCpt_idty(JuniterParser.Cpt_idtyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#membersCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembersCount(JuniterParser.MembersCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(JuniterParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#peer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPeer(JuniterParser.PeerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#transaction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTransaction(JuniterParser.TransactionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#wot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWot(JuniterParser.WotContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#identity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentity(JuniterParser.IdentityContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#certification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCertification(JuniterParser.CertificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#membership}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMembership(JuniterParser.MembershipContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#revocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRevocation(JuniterParser.RevocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#endpoints}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpoints(JuniterParser.EndpointsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#enpoint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnpoint(JuniterParser.EnpointContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sessionid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSessionid(JuniterParser.SessionidContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#idtyTimestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdtyTimestamp(JuniterParser.IdtyTimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#idtyUniqueID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdtyUniqueID(JuniterParser.IdtyUniqueIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#signatures}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignatures(JuniterParser.SignaturesContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#comment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComment(JuniterParser.CommentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#locktime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocktime(JuniterParser.LocktimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#blockstamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockstamp(JuniterParser.BlockstampContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#inputs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInputs(JuniterParser.InputsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput(JuniterParser.InputContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#ud}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUd(JuniterParser.UdContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#tx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTx(JuniterParser.TxContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#amount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAmount(JuniterParser.AmountContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#base}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase(JuniterParser.BaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#tindex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTindex(JuniterParser.TindexContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#unlocks}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlocks(JuniterParser.UnlocksContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#unlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnlock(JuniterParser.UnlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#in_index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_index(JuniterParser.In_indexContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#unsig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsig(JuniterParser.UnsigContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#unxhx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnxhx(JuniterParser.UnxhxContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#outputs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutputs(JuniterParser.OutputsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#output}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput(JuniterParser.OutputContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(JuniterParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#and}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(JuniterParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#or}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(JuniterParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sig}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSig(JuniterParser.SigContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#xhx}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXhx(JuniterParser.XhxContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#csv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCsv(JuniterParser.CsvContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#cltv}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCltv(JuniterParser.CltvContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#outParam}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutParam(JuniterParser.OutParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(JuniterParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#issuer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIssuer(JuniterParser.IssuerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#member}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMember(JuniterParser.MemberContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#certTS}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCertTS(JuniterParser.CertTSContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#userID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserID(JuniterParser.UserIDContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#timestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTimestamp(JuniterParser.TimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#signature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignature(JuniterParser.SignatureContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#userid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUserid(JuniterParser.UseridContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#pubkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPubkey(JuniterParser.PubkeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#currency}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCurrency(JuniterParser.CurrencyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#version}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVersion(JuniterParser.VersionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#buid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuid(JuniterParser.BuidContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#bnum}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBnum(JuniterParser.BnumContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#bhash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBhash(JuniterParser.BhashContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#percentRot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPercentRot(JuniterParser.PercentRotContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#dtDiffEval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtDiffEval(JuniterParser.DtDiffEvalContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#avgGenTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAvgGenTime(JuniterParser.AvgGenTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#medianTimeBlocks}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMedianTimeBlocks(JuniterParser.MedianTimeBlocksContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#stepMax}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStepMax(JuniterParser.StepMaxContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#msValidity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMsValidity(JuniterParser.MsValidityContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#xpercent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitXpercent(JuniterParser.XpercentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#msWindow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMsWindow(JuniterParser.MsWindowContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#idtyWindow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdtyWindow(JuniterParser.IdtyWindowContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sigQty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigQty(JuniterParser.SigQtyContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sigValidity}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigValidity(JuniterParser.SigValidityContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sigWindow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigWindow(JuniterParser.SigWindowContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sigStock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigStock(JuniterParser.SigStockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#sigPeriod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSigPeriod(JuniterParser.SigPeriodContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#udReevalTime0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdReevalTime0(JuniterParser.UdReevalTime0Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#udTime0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUdTime0(JuniterParser.UdTime0Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#dtReeval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDtReeval(JuniterParser.DtReevalContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#dt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDt(JuniterParser.DtContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC(JuniterParser.CContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#ud0}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUd0(JuniterParser.Ud0Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#previousIssuer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreviousIssuer(JuniterParser.PreviousIssuerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#previousHash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreviousHash(JuniterParser.PreviousHashContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#differentIssuersCount}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDifferentIssuersCount(JuniterParser.DifferentIssuersCountContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#issuersFrameVar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIssuersFrameVar(JuniterParser.IssuersFrameVarContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#issuersFrame}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIssuersFrame(JuniterParser.IssuersFrameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#unitBase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitBase(JuniterParser.UnitBaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#universalDividend}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniversalDividend(JuniterParser.UniversalDividendContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#medianTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMedianTime(JuniterParser.MedianTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#time}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime(JuniterParser.TimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#powMin}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowMin(JuniterParser.PowMinContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(JuniterParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nonce}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonce(JuniterParser.NonceContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#innerHash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerHash(JuniterParser.InnerHashContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#certTimestamp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCertTimestamp(JuniterParser.CertTimestampContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#idtyIssuer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdtyIssuer(JuniterParser.IdtyIssuerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#port}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPort(JuniterParser.PortContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#ip6}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIp6(JuniterParser.Ip6Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#ip4}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIp4(JuniterParser.Ip4Context ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#dns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDns(JuniterParser.DnsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#iBlockUid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIBlockUid(JuniterParser.IBlockUidContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#mBlockUid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMBlockUid(JuniterParser.MBlockUidContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#endpointType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEndpointType(JuniterParser.EndpointTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#toPK}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToPK(JuniterParser.ToPKContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#blockstampTime}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockstampTime(JuniterParser.BlockstampTimeContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nbOutput}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNbOutput(JuniterParser.NbOutputContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nbUnlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNbUnlock(JuniterParser.NbUnlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nbInput}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNbInput(JuniterParser.NbInputContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#nbIssuer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNbIssuer(JuniterParser.NbIssuerContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#fromPK}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFromPK(JuniterParser.FromPKContext ctx);
	/**
	 * Visit a parse tree produced by {@link JuniterParser#idtySignature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdtySignature(JuniterParser.IdtySignatureContext ctx);
}