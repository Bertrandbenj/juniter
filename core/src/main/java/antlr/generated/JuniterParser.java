// Generated from JuniterParser.g4 by ANTLR 4.7.1
 
package antlr.generated;
//import juniter.crypto.CryptoUtils;
import java.lang.Integer;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JuniterParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		HASH=1, TXHASH=2, Timestamp_=3, UniqueID_=4, Issuer_=5, Currency_=6, Type_=7, 
		Version_=8, Block_=9, Member_=10, CertTS_=11, UserID_=12, IdtySignature_=13, 
		IdtyTimestamp_=14, IdtyUniqueID_=15, IdtyIssuer_=16, PublicKey_=17, Unlocks_=18, 
		Signatures_=19, Comment_=20, Inputs_=21, Issuers_=22, Locktime_=23, Blockstamp_=24, 
		Outputs_=25, Endpoints_=26, Number_=27, PoWMin_=28, Time_=29, MedianTime_=30, 
		UniversalDividend_=31, UnitBase_=32, IssuersFrame_=33, IssuersFrameVar_=34, 
		DiffIssuersCount_=35, PreviousHash_=36, PreviousIssuer_=37, Parameters_=38, 
		MembersCount_=39, Nonce_=40, InnerHash_=41, Transactions_=42, Certifications_=43, 
		Excluded_=44, Revoked_=45, Leavers_=46, Actives_=47, Joiners_=48, Identities_=49, 
		TX=50, VALUE_START=51, ARRAY_START=52, BLG_NUM=53, POPONE=54, WOTBUID=55, 
		WOTNUMB=56, WOTPUBK=57, WOTSIGN=58, WOTSEP=59, WOTUID=60, WOTNL=61, NUMBER=62, 
		DASH_INLINED=63, HASH_INLINED=64, EOBUID=65, DOCTYPE_IDTY=66, DOCTYPE_CERT=67, 
		DOCTYPE_MEMB=68, DOCTYPE_REVO=69, DOCTYPE_PEER=70, DOCTYPE_TRAN=71, DOCTYPE_BLCK=72, 
		EOTYPE=73, SIGN=74, CertTimestamp_=75, EOSIGN=76, VERSION=77, EOVERS=78, 
		PUBKEY_INLINED=79, EOPUBK=80, CURRENCY=81, EOCURR=82, USERID=83, EOUSER=84, 
		NUMB=85, EONUMB=86, MEMBER_TYPE=87, EOMEMB=88, COMMENT=89, EOCOMM=90, 
		MULTISIGN=91, SIGN_SEP=92, EOSIGNS=93, OUTNUMB=94, SIG=95, XHX=96, CSV=97, 
		CLTV=98, OR=99, AND=100, OUTLP=101, OUTRP=102, OUTHASH=103, OUTPUT_FIELD_SEP=104, 
		OUTPUT_SEP=105, OUTPUBK=106, EOOUTP=107, UNLOCK_SEP=108, UNSIG=109, UNXHX=110, 
		UNLP=111, UNRP=112, UNNUMB=113, UNLOCK_FIELD_SEP=114, EOULK=115, BLNUMB=116, 
		BLPERCENT=117, BLOCK_FIELD_SEP=118, EOBLK=119, INNUMB=120, INHASH=121, 
		INFIELD_SEP=122, DIVIDEND_TYPE=123, TRANSACTION_TYPE=124, INPUT_SEP=125, 
		EOINPT=126, PUBKEY_MULTILN=127, ISSUER_SEP=128, EOISSU=129, CPT_COL=130, 
		CPT_DIV=131, CPT_TXS=132, BREAK=133, CPT_SIG=134, CPT_NUM=135, CPT_HASH=136, 
		CPT_OP=137, CPT_CP=138, CPT_ISS=139, CPT_SIGN=140, CPT_COMM=141, IP4=142, 
		IP6=143, OCT=144, SESSID=145, DNS=146, FIELD_SEP=147, PORT=148, ENDPOINT_TYPE=149, 
		ENDPT_SEP=150;
	public static final int
		RULE_doc = 0, RULE_block_ = 1, RULE_cpt_transactions = 2, RULE_transactions = 3, 
		RULE_compactTransaction = 4, RULE_nbComment = 5, RULE_cpt_comm = 6, RULE_cpt_signature = 7, 
		RULE_cpt_out = 8, RULE_cpt_in = 9, RULE_cpt_unlock = 10, RULE_issuers = 11, 
		RULE_issuers_compact = 12, RULE_modeSwitch = 13, RULE_certifications = 14, 
		RULE_excluded = 15, RULE_revoked = 16, RULE_leavers = 17, RULE_renewed = 18, 
		RULE_joiners = 19, RULE_cpt_joiner = 20, RULE_identities = 21, RULE_cpt_idty = 22, 
		RULE_membersCount = 23, RULE_parameters = 24, RULE_peer = 25, RULE_transaction = 26, 
		RULE_wot = 27, RULE_identity = 28, RULE_certification = 29, RULE_membership = 30, 
		RULE_revocation = 31, RULE_endpoints = 32, RULE_enpoint = 33, RULE_sessionid = 34, 
		RULE_idtyTimestamp = 35, RULE_idtyUniqueID = 36, RULE_signatures = 37, 
		RULE_comment = 38, RULE_locktime = 39, RULE_blockstamp = 40, RULE_inputs = 41, 
		RULE_input = 42, RULE_ud = 43, RULE_tx = 44, RULE_amount = 45, RULE_base = 46, 
		RULE_tindex = 47, RULE_unlocks = 48, RULE_unlock = 49, RULE_in_index = 50, 
		RULE_unsig = 51, RULE_unxhx = 52, RULE_outputs = 53, RULE_output = 54, 
		RULE_cond = 55, RULE_and = 56, RULE_or = 57, RULE_sig = 58, RULE_xhx = 59, 
		RULE_csv = 60, RULE_cltv = 61, RULE_outParam = 62, RULE_block = 63, RULE_issuer = 64, 
		RULE_member = 65, RULE_certTS = 66, RULE_userID = 67, RULE_timestamp = 68, 
		RULE_signature = 69, RULE_userid = 70, RULE_pubkey = 71, RULE_currency = 72, 
		RULE_version = 73, RULE_buid = 74, RULE_bnum = 75, RULE_bhash = 76, RULE_percentRot = 77, 
		RULE_dtDiffEval = 78, RULE_avgGenTime = 79, RULE_medianTimeBlocks = 80, 
		RULE_stepMax = 81, RULE_msValidity = 82, RULE_xpercent = 83, RULE_msWindow = 84, 
		RULE_idtyWindow = 85, RULE_sigQty = 86, RULE_sigValidity = 87, RULE_sigWindow = 88, 
		RULE_sigStock = 89, RULE_sigPeriod = 90, RULE_udReevalTime0 = 91, RULE_udTime0 = 92, 
		RULE_dtReeval = 93, RULE_dt = 94, RULE_c = 95, RULE_ud0 = 96, RULE_previousIssuer = 97, 
		RULE_previousHash = 98, RULE_differentIssuersCount = 99, RULE_issuersFrameVar = 100, 
		RULE_issuersFrame = 101, RULE_unitBase = 102, RULE_universalDividend = 103, 
		RULE_medianTime = 104, RULE_time = 105, RULE_powMin = 106, RULE_number = 107, 
		RULE_nonce = 108, RULE_innerHash = 109, RULE_certTimestamp = 110, RULE_idtyIssuer = 111, 
		RULE_port = 112, RULE_ip6 = 113, RULE_ip4 = 114, RULE_dns = 115, RULE_iBlockUid = 116, 
		RULE_mBlockUid = 117, RULE_endpointType = 118, RULE_toPK = 119, RULE_blockstampTime = 120, 
		RULE_nbOutput = 121, RULE_nbUnlock = 122, RULE_nbInput = 123, RULE_nbIssuer = 124, 
		RULE_fromPK = 125, RULE_idtySignature = 126;
	public static final String[] ruleNames = {
		"doc", "block_", "cpt_transactions", "transactions", "compactTransaction", 
		"nbComment", "cpt_comm", "cpt_signature", "cpt_out", "cpt_in", "cpt_unlock", 
		"issuers", "issuers_compact", "modeSwitch", "certifications", "excluded", 
		"revoked", "leavers", "renewed", "joiners", "cpt_joiner", "identities", 
		"cpt_idty", "membersCount", "parameters", "peer", "transaction", "wot", 
		"identity", "certification", "membership", "revocation", "endpoints", 
		"enpoint", "sessionid", "idtyTimestamp", "idtyUniqueID", "signatures", 
		"comment", "locktime", "blockstamp", "inputs", "input", "ud", "tx", "amount", 
		"base", "tindex", "unlocks", "unlock", "in_index", "unsig", "unxhx", "outputs", 
		"output", "cond", "and", "or", "sig", "xhx", "csv", "cltv", "outParam", 
		"block", "issuer", "member", "certTS", "userID", "timestamp", "signature", 
		"userid", "pubkey", "currency", "version", "buid", "bnum", "bhash", "percentRot", 
		"dtDiffEval", "avgGenTime", "medianTimeBlocks", "stepMax", "msValidity", 
		"xpercent", "msWindow", "idtyWindow", "sigQty", "sigValidity", "sigWindow", 
		"sigStock", "sigPeriod", "udReevalTime0", "udTime0", "dtReeval", "dt", 
		"c", "ud0", "previousIssuer", "previousHash", "differentIssuersCount", 
		"issuersFrameVar", "issuersFrame", "unitBase", "universalDividend", "medianTime", 
		"time", "powMin", "number", "nonce", "innerHash", "certTimestamp", "idtyIssuer", 
		"port", "ip6", "ip4", "dns", "iBlockUid", "mBlockUid", "endpointType", 
		"toPK", "blockstampTime", "nbOutput", "nbUnlock", "nbInput", "nbIssuer", 
		"fromPK", "idtySignature"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, "'-'", null, null, "'Identity'", "'Certification'", 
		"'Membership'", "'Revocation'", "'Peer'", "'Transaction'", "'Block'", 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"'CSV'", "'CLTV'", "' || '", "' && '", null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, "' '"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "HASH", "TXHASH", "Timestamp_", "UniqueID_", "Issuer_", "Currency_", 
		"Type_", "Version_", "Block_", "Member_", "CertTS_", "UserID_", "IdtySignature_", 
		"IdtyTimestamp_", "IdtyUniqueID_", "IdtyIssuer_", "PublicKey_", "Unlocks_", 
		"Signatures_", "Comment_", "Inputs_", "Issuers_", "Locktime_", "Blockstamp_", 
		"Outputs_", "Endpoints_", "Number_", "PoWMin_", "Time_", "MedianTime_", 
		"UniversalDividend_", "UnitBase_", "IssuersFrame_", "IssuersFrameVar_", 
		"DiffIssuersCount_", "PreviousHash_", "PreviousIssuer_", "Parameters_", 
		"MembersCount_", "Nonce_", "InnerHash_", "Transactions_", "Certifications_", 
		"Excluded_", "Revoked_", "Leavers_", "Actives_", "Joiners_", "Identities_", 
		"TX", "VALUE_START", "ARRAY_START", "BLG_NUM", "POPONE", "WOTBUID", "WOTNUMB", 
		"WOTPUBK", "WOTSIGN", "WOTSEP", "WOTUID", "WOTNL", "NUMBER", "DASH_INLINED", 
		"HASH_INLINED", "EOBUID", "DOCTYPE_IDTY", "DOCTYPE_CERT", "DOCTYPE_MEMB", 
		"DOCTYPE_REVO", "DOCTYPE_PEER", "DOCTYPE_TRAN", "DOCTYPE_BLCK", "EOTYPE", 
		"SIGN", "CertTimestamp_", "EOSIGN", "VERSION", "EOVERS", "PUBKEY_INLINED", 
		"EOPUBK", "CURRENCY", "EOCURR", "USERID", "EOUSER", "NUMB", "EONUMB", 
		"MEMBER_TYPE", "EOMEMB", "COMMENT", "EOCOMM", "MULTISIGN", "SIGN_SEP", 
		"EOSIGNS", "OUTNUMB", "SIG", "XHX", "CSV", "CLTV", "OR", "AND", "OUTLP", 
		"OUTRP", "OUTHASH", "OUTPUT_FIELD_SEP", "OUTPUT_SEP", "OUTPUBK", "EOOUTP", 
		"UNLOCK_SEP", "UNSIG", "UNXHX", "UNLP", "UNRP", "UNNUMB", "UNLOCK_FIELD_SEP", 
		"EOULK", "BLNUMB", "BLPERCENT", "BLOCK_FIELD_SEP", "EOBLK", "INNUMB", 
		"INHASH", "INFIELD_SEP", "DIVIDEND_TYPE", "TRANSACTION_TYPE", "INPUT_SEP", 
		"EOINPT", "PUBKEY_MULTILN", "ISSUER_SEP", "EOISSU", "CPT_COL", "CPT_DIV", 
		"CPT_TXS", "BREAK", "CPT_SIG", "CPT_NUM", "CPT_HASH", "CPT_OP", "CPT_CP", 
		"CPT_ISS", "CPT_SIGN", "CPT_COMM", "IP4", "IP6", "OCT", "SESSID", "DNS", 
		"FIELD_SEP", "PORT", "ENDPOINT_TYPE", "ENDPT_SEP"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "JuniterParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


		int nbIssu=0, nbInputs=0;int nbIssuersExpected=0;
		int nbSign=0;
		int maxEndpoints=3;
		int nbComm=0;
		String indent="          ";	
		int count = 0;  
		String val = "_";
		String issuerPK = "";
		String docSignature="";
		boolean isValid = true;  
		//boolean quickCheck = CryptoUtils.verify("testSignature", "i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ==", "3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa");
		public String rawUnsigned (int lastChar){
			CharStream cs = _ctx.start.getTokenSource().getInputStream();
			return cs.getText(
			  	Interval.of(
			  		0, 
			  		lastChar
			  	));
		}

	public JuniterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocContext extends ParserRuleContext {
		public WotContext wot() {
			return getRuleContext(WotContext.class,0);
		}
		public TransactionContext transaction() {
			return getRuleContext(TransactionContext.class,0);
		}
		public PeerContext peer() {
			return getRuleContext(PeerContext.class,0);
		}
		public Block_Context block_() {
			return getRuleContext(Block_Context.class,0);
		}
		public DocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocContext doc() throws RecognitionException {
		DocContext _localctx = new DocContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_doc);
		try {
			setState(259);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				System.out.println("doc: " );
				setState(255);
				wot();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(256);
				transaction();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(257);
				peer();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(258);
				block_();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Block_Context extends ParserRuleContext {
		public VersionContext version;
		public NumberContext number;
		public PowMinContext powMin;
		public TimeContext time;
		public MedianTimeContext medianTime;
		public UniversalDividendContext universalDividend;
		public UnitBaseContext unitBase;
		public IssuerContext issuer;
		public IssuersFrameContext issuersFrame;
		public IssuersFrameVarContext issuersFrameVar;
		public DifferentIssuersCountContext differentIssuersCount;
		public PreviousHashContext previousHash;
		public PreviousIssuerContext previousIssuer;
		public MembersCountContext membersCount;
		public InnerHashContext innerHash;
		public NonceContext nonce;
		public SignatureContext signature;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_BLCK() { return getToken(JuniterParser.DOCTYPE_BLCK, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public PowMinContext powMin() {
			return getRuleContext(PowMinContext.class,0);
		}
		public TimeContext time() {
			return getRuleContext(TimeContext.class,0);
		}
		public MedianTimeContext medianTime() {
			return getRuleContext(MedianTimeContext.class,0);
		}
		public UnitBaseContext unitBase() {
			return getRuleContext(UnitBaseContext.class,0);
		}
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public IssuersFrameContext issuersFrame() {
			return getRuleContext(IssuersFrameContext.class,0);
		}
		public IssuersFrameVarContext issuersFrameVar() {
			return getRuleContext(IssuersFrameVarContext.class,0);
		}
		public DifferentIssuersCountContext differentIssuersCount() {
			return getRuleContext(DifferentIssuersCountContext.class,0);
		}
		public MembersCountContext membersCount() {
			return getRuleContext(MembersCountContext.class,0);
		}
		public TerminalNode Identities_() { return getToken(JuniterParser.Identities_, 0); }
		public IdentitiesContext identities() {
			return getRuleContext(IdentitiesContext.class,0);
		}
		public TerminalNode Joiners_() { return getToken(JuniterParser.Joiners_, 0); }
		public JoinersContext joiners() {
			return getRuleContext(JoinersContext.class,0);
		}
		public TerminalNode Actives_() { return getToken(JuniterParser.Actives_, 0); }
		public RenewedContext renewed() {
			return getRuleContext(RenewedContext.class,0);
		}
		public TerminalNode Leavers_() { return getToken(JuniterParser.Leavers_, 0); }
		public LeaversContext leavers() {
			return getRuleContext(LeaversContext.class,0);
		}
		public TerminalNode Revoked_() { return getToken(JuniterParser.Revoked_, 0); }
		public RevokedContext revoked() {
			return getRuleContext(RevokedContext.class,0);
		}
		public TerminalNode Excluded_() { return getToken(JuniterParser.Excluded_, 0); }
		public ExcludedContext excluded() {
			return getRuleContext(ExcludedContext.class,0);
		}
		public TerminalNode Certifications_() { return getToken(JuniterParser.Certifications_, 0); }
		public CertificationsContext certifications() {
			return getRuleContext(CertificationsContext.class,0);
		}
		public Cpt_transactionsContext cpt_transactions() {
			return getRuleContext(Cpt_transactionsContext.class,0);
		}
		public InnerHashContext innerHash() {
			return getRuleContext(InnerHashContext.class,0);
		}
		public NonceContext nonce() {
			return getRuleContext(NonceContext.class,0);
		}
		public PreviousHashContext previousHash() {
			return getRuleContext(PreviousHashContext.class,0);
		}
		public PreviousIssuerContext previousIssuer() {
			return getRuleContext(PreviousIssuerContext.class,0);
		}
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public UniversalDividendContext universalDividend() {
			return getRuleContext(UniversalDividendContext.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public Block_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBlock_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBlock_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBlock_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Block_Context block_() throws RecognitionException {
		Block_Context _localctx = new Block_Context(_ctx, getState());
		enterRule(_localctx, 2, RULE_block_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("  block: ");
			setState(262);
			((Block_Context)_localctx).version = version();
			System.out.println("    version: "+(((Block_Context)_localctx).version!=null?_input.getText(((Block_Context)_localctx).version.start,((Block_Context)_localctx).version.stop):null) );
			setState(264);
			match(DOCTYPE_BLCK);
			setState(265);
			currency();
			System.out.println("    currency: "+(((Block_Context)_localctx).version!=null?_input.getText(((Block_Context)_localctx).version.start,((Block_Context)_localctx).version.stop):null) );
			setState(267);
			((Block_Context)_localctx).number = number();
			System.out.println("    number: "+(((Block_Context)_localctx).number!=null?_input.getText(((Block_Context)_localctx).number.start,((Block_Context)_localctx).number.stop):null) );
			setState(269);
			((Block_Context)_localctx).powMin = powMin();
			System.out.println("    powMin: "+(((Block_Context)_localctx).powMin!=null?_input.getText(((Block_Context)_localctx).powMin.start,((Block_Context)_localctx).powMin.stop):null) );
			setState(271);
			((Block_Context)_localctx).time = time();
			System.out.println("    time: "+(((Block_Context)_localctx).time!=null?_input.getText(((Block_Context)_localctx).time.start,((Block_Context)_localctx).time.stop):null) );
			setState(273);
			((Block_Context)_localctx).medianTime = medianTime();
			System.out.println("    medianTime: "+(((Block_Context)_localctx).medianTime!=null?_input.getText(((Block_Context)_localctx).medianTime.start,((Block_Context)_localctx).medianTime.stop):null) );
			setState(278);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(275);
				((Block_Context)_localctx).universalDividend = universalDividend();
				System.out.println("    universalDividend: "+(((Block_Context)_localctx).universalDividend!=null?_input.getText(((Block_Context)_localctx).universalDividend.start,((Block_Context)_localctx).universalDividend.stop):null) );
				}
				break;
			}
			setState(280);
			((Block_Context)_localctx).unitBase = unitBase();
			System.out.println("    unitBase: "+(((Block_Context)_localctx).unitBase!=null?_input.getText(((Block_Context)_localctx).unitBase.start,((Block_Context)_localctx).unitBase.stop):null) );
			setState(282);
			((Block_Context)_localctx).issuer = issuer();
			System.out.println("    issuer: "+(((Block_Context)_localctx).issuer!=null?_input.getText(((Block_Context)_localctx).issuer.start,((Block_Context)_localctx).issuer.stop):null) );
			setState(284);
			((Block_Context)_localctx).issuersFrame = issuersFrame();
			System.out.println("    issuersFrame: "+(((Block_Context)_localctx).issuersFrame!=null?_input.getText(((Block_Context)_localctx).issuersFrame.start,((Block_Context)_localctx).issuersFrame.stop):null) );
			setState(286);
			((Block_Context)_localctx).issuersFrameVar = issuersFrameVar();
			System.out.println("    issuersFrameVar: "+(((Block_Context)_localctx).issuersFrameVar!=null?_input.getText(((Block_Context)_localctx).issuersFrameVar.start,((Block_Context)_localctx).issuersFrameVar.stop):null) );
			setState(288);
			((Block_Context)_localctx).differentIssuersCount = differentIssuersCount();
			System.out.println("    differentIssuersCount: "+(((Block_Context)_localctx).differentIssuersCount!=null?_input.getText(((Block_Context)_localctx).differentIssuersCount.start,((Block_Context)_localctx).differentIssuersCount.stop):null) );
			setState(296);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case HASH_INLINED:
			case OUTHASH:
			case INHASH:
				{
				setState(290);
				((Block_Context)_localctx).previousHash = previousHash();
				System.out.println("    previousHash: "+(((Block_Context)_localctx).previousHash!=null?_input.getText(((Block_Context)_localctx).previousHash.start,((Block_Context)_localctx).previousHash.stop):null) );
				setState(292);
				((Block_Context)_localctx).previousIssuer = previousIssuer();
				System.out.println("    previousIssuer: "+(((Block_Context)_localctx).previousIssuer!=null?_input.getText(((Block_Context)_localctx).previousIssuer.start,((Block_Context)_localctx).previousIssuer.stop):null) );
				}
				break;
			case BLPERCENT:
				{
				setState(295);
				parameters();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(298);
			((Block_Context)_localctx).membersCount = membersCount();
			System.out.println("    membersCount: "+(((Block_Context)_localctx).membersCount!=null?_input.getText(((Block_Context)_localctx).membersCount.start,((Block_Context)_localctx).membersCount.stop):null) );
			setState(300);
			match(Identities_);
			setState(301);
			identities();
			setState(302);
			match(Joiners_);
			setState(303);
			joiners();
			setState(304);
			match(Actives_);
			setState(305);
			renewed();
			setState(306);
			match(Leavers_);
			setState(307);
			leavers();
			setState(308);
			match(Revoked_);
			setState(309);
			revoked();
			setState(310);
			match(Excluded_);
			setState(311);
			excluded();
			setState(312);
			match(Certifications_);
			setState(313);
			certifications();
			setState(314);
			cpt_transactions();
			setState(315);
			((Block_Context)_localctx).innerHash = innerHash();
			System.out.println("    innerHash: "+(((Block_Context)_localctx).innerHash!=null?_input.getText(((Block_Context)_localctx).innerHash.start,((Block_Context)_localctx).innerHash.stop):null) );
			setState(317);
			((Block_Context)_localctx).nonce = nonce();
			System.out.println("    nonce: "+(((Block_Context)_localctx).nonce!=null?_input.getText(((Block_Context)_localctx).nonce.start,((Block_Context)_localctx).nonce.stop):null) );
			setState(322);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 58)) & ~0x3f) == 0 && ((1L << (_la - 58)) & ((1L << (WOTSIGN - 58)) | (1L << (SIGN - 58)) | (1L << (MULTISIGN - 58)))) != 0)) {
				{
				setState(319);
				((Block_Context)_localctx).signature = signature();
				System.out.println("    signature: "+(((Block_Context)_localctx).signature!=null?_input.getText(((Block_Context)_localctx).signature.start,((Block_Context)_localctx).signature.stop):null)+ " # isValid? " );
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_transactionsContext extends ParserRuleContext {
		public List<CompactTransactionContext> compactTransaction() {
			return getRuleContexts(CompactTransactionContext.class);
		}
		public CompactTransactionContext compactTransaction(int i) {
			return getRuleContext(CompactTransactionContext.class,i);
		}
		public List<TerminalNode> POPONE() { return getTokens(JuniterParser.POPONE); }
		public TerminalNode POPONE(int i) {
			return getToken(JuniterParser.POPONE, i);
		}
		public Cpt_transactionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_transactions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_transactions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_transactions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_transactions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_transactionsContext cpt_transactions() throws RecognitionException {
		Cpt_transactionsContext _localctx = new Cpt_transactionsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_cpt_transactions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    transactions: ");
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TX) {
				{
				{
				setState(325);
				compactTransaction();
				setState(326);
				match(POPONE);
				}
				}
				setState(332);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransactionsContext extends ParserRuleContext {
		public int i = 0;
		public List<CompactTransactionContext> compactTransaction() {
			return getRuleContexts(CompactTransactionContext.class);
		}
		public CompactTransactionContext compactTransaction(int i) {
			return getRuleContext(CompactTransactionContext.class,i);
		}
		public TransactionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transactions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTransactions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTransactions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTransactions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransactionsContext transactions() throws RecognitionException {
		TransactionsContext _localctx = new TransactionsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_transactions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    transactions: ");
			setState(338);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TX) {
				{
				{

							nbIssu=0;
							System.out.println("      "+ _localctx.i++ +": ");
						
				setState(335);
				compactTransaction();
				}
				}
				setState(340);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompactTransactionContext extends ParserRuleContext {
		public int nbIn = 0;
		public int nbUn = 0;
		public int nbOut = 0;;
		public VersionContext version;
		public NbIssuerContext nbIssuer;
		public NbInputContext nbInput;
		public NbCommentContext nbComment;
		public TerminalNode TX() { return getToken(JuniterParser.TX, 0); }
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public NbIssuerContext nbIssuer() {
			return getRuleContext(NbIssuerContext.class,0);
		}
		public NbInputContext nbInput() {
			return getRuleContext(NbInputContext.class,0);
		}
		public NbUnlockContext nbUnlock() {
			return getRuleContext(NbUnlockContext.class,0);
		}
		public NbOutputContext nbOutput() {
			return getRuleContext(NbOutputContext.class,0);
		}
		public NbCommentContext nbComment() {
			return getRuleContext(NbCommentContext.class,0);
		}
		public BlockstampTimeContext blockstampTime() {
			return getRuleContext(BlockstampTimeContext.class,0);
		}
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public Cpt_signatureContext cpt_signature() {
			return getRuleContext(Cpt_signatureContext.class,0);
		}
		public List<Issuers_compactContext> issuers_compact() {
			return getRuleContexts(Issuers_compactContext.class);
		}
		public Issuers_compactContext issuers_compact(int i) {
			return getRuleContext(Issuers_compactContext.class,i);
		}
		public List<Cpt_inContext> cpt_in() {
			return getRuleContexts(Cpt_inContext.class);
		}
		public Cpt_inContext cpt_in(int i) {
			return getRuleContext(Cpt_inContext.class,i);
		}
		public List<Cpt_unlockContext> cpt_unlock() {
			return getRuleContexts(Cpt_unlockContext.class);
		}
		public Cpt_unlockContext cpt_unlock(int i) {
			return getRuleContext(Cpt_unlockContext.class,i);
		}
		public List<Cpt_outContext> cpt_out() {
			return getRuleContexts(Cpt_outContext.class);
		}
		public Cpt_outContext cpt_out(int i) {
			return getRuleContext(Cpt_outContext.class,i);
		}
		public Cpt_commContext cpt_comm() {
			return getRuleContext(Cpt_commContext.class,0);
		}
		public CompactTransactionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compactTransaction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCompactTransaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCompactTransaction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCompactTransaction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompactTransactionContext compactTransaction() throws RecognitionException {
		CompactTransactionContext _localctx = new CompactTransactionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_compactTransaction);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(341);
			match(TX);
			setState(342);
			((CompactTransactionContext)_localctx).version = version();
			System.out.println("        version: " + Integer.parseInt((((CompactTransactionContext)_localctx).version!=null?_input.getText(((CompactTransactionContext)_localctx).version.start,((CompactTransactionContext)_localctx).version.stop):null)));
			setState(344);
			((CompactTransactionContext)_localctx).nbIssuer = nbIssuer();
			 
					nbIssuersExpected = Integer.parseInt((((CompactTransactionContext)_localctx).nbIssuer!=null?_input.getText(((CompactTransactionContext)_localctx).nbIssuer.start,((CompactTransactionContext)_localctx).nbIssuer.stop):null));
					System.out.println("        nbIssuer: " + nbIssuersExpected );
				
			setState(346);
			((CompactTransactionContext)_localctx).nbInput = nbInput();
			((CompactTransactionContext)_localctx).nbIn =  Integer.parseInt((((CompactTransactionContext)_localctx).nbInput!=null?_input.getText(((CompactTransactionContext)_localctx).nbInput.start,((CompactTransactionContext)_localctx).nbInput.stop):null)); System.out.println("        nbInput: " +_localctx.nbIn);
			setState(348);
			nbUnlock();
			System.out.println("        nbUnlock: " + Integer.parseInt((((CompactTransactionContext)_localctx).version!=null?_input.getText(((CompactTransactionContext)_localctx).version.start,((CompactTransactionContext)_localctx).version.stop):null)));
			setState(350);
			nbOutput();
			System.out.println("        nbOutput: " + Integer.parseInt((((CompactTransactionContext)_localctx).version!=null?_input.getText(((CompactTransactionContext)_localctx).version.start,((CompactTransactionContext)_localctx).version.stop):null)));
			setState(352);
			((CompactTransactionContext)_localctx).nbComment = nbComment();
			nbComm = Integer.parseInt((((CompactTransactionContext)_localctx).nbComment!=null?_input.getText(((CompactTransactionContext)_localctx).nbComment.start,((CompactTransactionContext)_localctx).nbComment.stop):null)); System.out.println("        nbComment: " + nbComm);
			setState(354);
			blockstampTime();
			System.out.println("        blockstampTime: " + Integer.parseInt((((CompactTransactionContext)_localctx).version!=null?_input.getText(((CompactTransactionContext)_localctx).version.start,((CompactTransactionContext)_localctx).version.stop):null)));

					indent="          ";
					nbIssu=0; 
					System.out.println("        bstamp: ");
				
			setState(357);
			buid();
			setState(359); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(358);
				issuers_compact();
				}
				}
				setState(361); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CPT_ISS );
			setState(364); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(363);
					cpt_in();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(366); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(369); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(368);
					cpt_unlock();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(371); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(373);
			if (!(_localctx.nbOut++ < 2)) throw new FailedPredicateException(this, "$nbOut++ < 2");
			setState(375); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(374);
				cpt_out();
				}
				}
				setState(377); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CPT_NUM );
			setState(380);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CPT_COMM) {
				{
				setState(379);
				cpt_comm();
				}
			}

			setState(382);
			cpt_signature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NbCommentContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public NbCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nbComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNbComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNbComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNbComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NbCommentContext nbComment() throws RecognitionException {
		NbCommentContext _localctx = new NbCommentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_nbComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_commContext extends ParserRuleContext {
		public TerminalNode CPT_COMM() { return getToken(JuniterParser.CPT_COMM, 0); }
		public TerminalNode BREAK() { return getToken(JuniterParser.BREAK, 0); }
		public Cpt_commContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_comm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_comm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_comm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_comm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_commContext cpt_comm() throws RecognitionException {
		Cpt_commContext _localctx = new Cpt_commContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_cpt_comm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("cpt_comm:  " + nbComm);
			setState(387);
			match(CPT_COMM);
			setState(388);
			match(BREAK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_signatureContext extends ParserRuleContext {
		public TerminalNode CPT_SIGN() { return getToken(JuniterParser.CPT_SIGN, 0); }
		public Cpt_signatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_signature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_signature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_signature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_signature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_signatureContext cpt_signature() throws RecognitionException {
		Cpt_signatureContext _localctx = new Cpt_signatureContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cpt_signature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			match(CPT_SIGN);
			System.out.println("cpt_signature:  " + nbComm);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_outContext extends ParserRuleContext {
		public List<TerminalNode> CPT_NUM() { return getTokens(JuniterParser.CPT_NUM); }
		public TerminalNode CPT_NUM(int i) {
			return getToken(JuniterParser.CPT_NUM, i);
		}
		public List<TerminalNode> CPT_COL() { return getTokens(JuniterParser.CPT_COL); }
		public TerminalNode CPT_COL(int i) {
			return getToken(JuniterParser.CPT_COL, i);
		}
		public TerminalNode CPT_SIG() { return getToken(JuniterParser.CPT_SIG, 0); }
		public TerminalNode CPT_ISS() { return getToken(JuniterParser.CPT_ISS, 0); }
		public TerminalNode BREAK() { return getToken(JuniterParser.BREAK, 0); }
		public Cpt_outContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_out; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_out(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_out(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_out(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_outContext cpt_out() throws RecognitionException {
		Cpt_outContext _localctx = new Cpt_outContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_cpt_out);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			match(CPT_NUM);
			setState(394);
			match(CPT_COL);
			setState(395);
			match(CPT_NUM);
			setState(396);
			match(CPT_COL);
			setState(397);
			match(CPT_SIG);
			setState(398);
			match(CPT_ISS);
			setState(399);
			match(BREAK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_inContext extends ParserRuleContext {
		public List<TerminalNode> CPT_NUM() { return getTokens(JuniterParser.CPT_NUM); }
		public TerminalNode CPT_NUM(int i) {
			return getToken(JuniterParser.CPT_NUM, i);
		}
		public List<TerminalNode> CPT_COL() { return getTokens(JuniterParser.CPT_COL); }
		public TerminalNode CPT_COL(int i) {
			return getToken(JuniterParser.CPT_COL, i);
		}
		public TerminalNode BREAK() { return getToken(JuniterParser.BREAK, 0); }
		public TerminalNode CPT_DIV() { return getToken(JuniterParser.CPT_DIV, 0); }
		public TerminalNode CPT_ISS() { return getToken(JuniterParser.CPT_ISS, 0); }
		public TerminalNode CPT_TXS() { return getToken(JuniterParser.CPT_TXS, 0); }
		public TerminalNode CPT_HASH() { return getToken(JuniterParser.CPT_HASH, 0); }
		public Cpt_inContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_in; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_in(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_in(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_in(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_inContext cpt_in() throws RecognitionException {
		Cpt_inContext _localctx = new Cpt_inContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_cpt_in);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			match(CPT_NUM);
			setState(402);
			match(CPT_COL);
			setState(403);
			match(CPT_NUM);
			setState(412);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CPT_DIV:
				{
				setState(404);
				match(CPT_DIV);
				setState(405);
				match(CPT_ISS);
				setState(406);
				match(CPT_COL);
				setState(407);
				match(CPT_NUM);
				}
				break;
			case CPT_TXS:
				{
				setState(408);
				match(CPT_TXS);
				setState(409);
				match(CPT_HASH);
				setState(410);
				match(CPT_COL);
				setState(411);
				match(CPT_NUM);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(414);
			match(BREAK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_unlockContext extends ParserRuleContext {
		public List<TerminalNode> CPT_NUM() { return getTokens(JuniterParser.CPT_NUM); }
		public TerminalNode CPT_NUM(int i) {
			return getToken(JuniterParser.CPT_NUM, i);
		}
		public TerminalNode CPT_COL() { return getToken(JuniterParser.CPT_COL, 0); }
		public TerminalNode CPT_SIG() { return getToken(JuniterParser.CPT_SIG, 0); }
		public TerminalNode BREAK() { return getToken(JuniterParser.BREAK, 0); }
		public Cpt_unlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_unlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_unlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_unlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_unlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_unlockContext cpt_unlock() throws RecognitionException {
		Cpt_unlockContext _localctx = new Cpt_unlockContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cpt_unlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(CPT_NUM);
			setState(417);
			match(CPT_COL);
			setState(418);
			match(CPT_SIG);
			setState(419);
			match(CPT_NUM);
			setState(420);
			match(BREAK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IssuersContext extends ParserRuleContext {
		public PubkeyContext pubkey;
		public List<PubkeyContext> pubkey() {
			return getRuleContexts(PubkeyContext.class);
		}
		public PubkeyContext pubkey(int i) {
			return getRuleContext(PubkeyContext.class,i);
		}
		public IssuersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIssuers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIssuers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIssuers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IssuersContext issuers() throws RecognitionException {
		IssuersContext _localctx = new IssuersContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_issuers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    issuers: ");
			setState(426); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(423);
				((IssuersContext)_localctx).pubkey = pubkey();
				System.out.println("      "+ nbIssu++ +": "+ (((IssuersContext)_localctx).pubkey!=null?_input.getText(((IssuersContext)_localctx).pubkey.start,((IssuersContext)_localctx).pubkey.stop):null));
				}
				}
				setState(428); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Issuers_compactContext extends ParserRuleContext {
		public TerminalNode CPT_ISS() { return getToken(JuniterParser.CPT_ISS, 0); }
		public TerminalNode BREAK() { return getToken(JuniterParser.BREAK, 0); }
		public Issuers_compactContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuers_compact; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIssuers_compact(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIssuers_compact(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIssuers_compact(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Issuers_compactContext issuers_compact() throws RecognitionException {
		Issuers_compactContext _localctx = new Issuers_compactContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_issuers_compact);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("        issuers: ");
			{
			setState(431);
			match(CPT_ISS);
			setState(432);
			match(BREAK);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModeSwitchContext extends ParserRuleContext {
		public ModeSwitchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modeSwitch; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterModeSwitch(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitModeSwitch(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitModeSwitch(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ModeSwitchContext modeSwitch() throws RecognitionException {
		ModeSwitchContext _localctx = new ModeSwitchContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_modeSwitch);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(434);
			if (!(nbIssu == nbIssuersExpected)) throw new FailedPredicateException(this, "nbIssu == nbIssuersExpected");
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CertificationsContext extends ParserRuleContext {
		public int i = 0;
		public FromPKContext fromPK;
		public ToPKContext toPK;
		public BnumContext bnum;
		public SignatureContext signature;
		public List<FromPKContext> fromPK() {
			return getRuleContexts(FromPKContext.class);
		}
		public FromPKContext fromPK(int i) {
			return getRuleContext(FromPKContext.class,i);
		}
		public List<ToPKContext> toPK() {
			return getRuleContexts(ToPKContext.class);
		}
		public ToPKContext toPK(int i) {
			return getRuleContext(ToPKContext.class,i);
		}
		public List<BnumContext> bnum() {
			return getRuleContexts(BnumContext.class);
		}
		public BnumContext bnum(int i) {
			return getRuleContext(BnumContext.class,i);
		}
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public CertificationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certifications; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCertifications(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCertifications(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCertifications(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CertificationsContext certifications() throws RecognitionException {
		CertificationsContext _localctx = new CertificationsContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_certifications);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    certifications: ");
			setState(449);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				System.out.println("      "+ _localctx.i++ +": ");
				setState(438);
				((CertificationsContext)_localctx).fromPK = fromPK();
				System.out.println("        from: " +(((CertificationsContext)_localctx).fromPK!=null?_input.getText(((CertificationsContext)_localctx).fromPK.start,((CertificationsContext)_localctx).fromPK.stop):null));
				setState(440);
				((CertificationsContext)_localctx).toPK = toPK();
				System.out.println("        to: "+(((CertificationsContext)_localctx).toPK!=null?_input.getText(((CertificationsContext)_localctx).toPK.start,((CertificationsContext)_localctx).toPK.stop):null));
				setState(442);
				((CertificationsContext)_localctx).bnum = bnum();
				System.out.println("        bnum: "+(((CertificationsContext)_localctx).bnum!=null?_input.getText(((CertificationsContext)_localctx).bnum.start,((CertificationsContext)_localctx).bnum.stop):null));
				setState(444);
				((CertificationsContext)_localctx).signature = signature();
				System.out.println("        signature: "+(((CertificationsContext)_localctx).signature!=null?_input.getText(((CertificationsContext)_localctx).signature.start,((CertificationsContext)_localctx).signature.stop):null));
				}
				}
				setState(451);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExcludedContext extends ParserRuleContext {
		public int i = 0;
		public PubkeyContext pubkey;
		public List<PubkeyContext> pubkey() {
			return getRuleContexts(PubkeyContext.class);
		}
		public PubkeyContext pubkey(int i) {
			return getRuleContext(PubkeyContext.class,i);
		}
		public ExcludedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_excluded; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterExcluded(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitExcluded(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitExcluded(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExcludedContext excluded() throws RecognitionException {
		ExcludedContext _localctx = new ExcludedContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_excluded);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    excluded: ");
			setState(458);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				setState(453);
				((ExcludedContext)_localctx).pubkey = pubkey();
				System.out.println("      "+ _localctx.i++ +".pubkey: "+(((ExcludedContext)_localctx).pubkey!=null?_input.getText(((ExcludedContext)_localctx).pubkey.start,((ExcludedContext)_localctx).pubkey.stop):null));
				}
				}
				setState(460);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RevokedContext extends ParserRuleContext {
		public int i = 0;
		public List<PubkeyContext> pubkey() {
			return getRuleContexts(PubkeyContext.class);
		}
		public PubkeyContext pubkey(int i) {
			return getRuleContext(PubkeyContext.class,i);
		}
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public RevokedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_revoked; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterRevoked(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitRevoked(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitRevoked(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RevokedContext revoked() throws RecognitionException {
		RevokedContext _localctx = new RevokedContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_revoked);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    revoked: ");
			setState(467);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				setState(462);
				pubkey();
				setState(463);
				signature();
				}
				}
				setState(469);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LeaversContext extends ParserRuleContext {
		public int i = 0;
		public List<PubkeyContext> pubkey() {
			return getRuleContexts(PubkeyContext.class);
		}
		public PubkeyContext pubkey(int i) {
			return getRuleContext(PubkeyContext.class,i);
		}
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public List<MBlockUidContext> mBlockUid() {
			return getRuleContexts(MBlockUidContext.class);
		}
		public MBlockUidContext mBlockUid(int i) {
			return getRuleContext(MBlockUidContext.class,i);
		}
		public List<IBlockUidContext> iBlockUid() {
			return getRuleContexts(IBlockUidContext.class);
		}
		public IBlockUidContext iBlockUid(int i) {
			return getRuleContext(IBlockUidContext.class,i);
		}
		public List<UseridContext> userid() {
			return getRuleContexts(UseridContext.class);
		}
		public UseridContext userid(int i) {
			return getRuleContext(UseridContext.class,i);
		}
		public LeaversContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_leavers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterLeavers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitLeavers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitLeavers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LeaversContext leavers() throws RecognitionException {
		LeaversContext _localctx = new LeaversContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_leavers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    leavers: ");
			setState(479);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				setState(471);
				pubkey();
				setState(472);
				signature();
				setState(473);
				mBlockUid();
				setState(474);
				iBlockUid();
				setState(475);
				userid();
				}
				}
				setState(481);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RenewedContext extends ParserRuleContext {
		public int i = 0;
		public List<PubkeyContext> pubkey() {
			return getRuleContexts(PubkeyContext.class);
		}
		public PubkeyContext pubkey(int i) {
			return getRuleContext(PubkeyContext.class,i);
		}
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public List<MBlockUidContext> mBlockUid() {
			return getRuleContexts(MBlockUidContext.class);
		}
		public MBlockUidContext mBlockUid(int i) {
			return getRuleContext(MBlockUidContext.class,i);
		}
		public List<IBlockUidContext> iBlockUid() {
			return getRuleContexts(IBlockUidContext.class);
		}
		public IBlockUidContext iBlockUid(int i) {
			return getRuleContext(IBlockUidContext.class,i);
		}
		public List<UseridContext> userid() {
			return getRuleContexts(UseridContext.class);
		}
		public UseridContext userid(int i) {
			return getRuleContext(UseridContext.class,i);
		}
		public RenewedContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_renewed; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterRenewed(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitRenewed(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitRenewed(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RenewedContext renewed() throws RecognitionException {
		RenewedContext _localctx = new RenewedContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_renewed);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    renewed: ");
			setState(491);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				setState(483);
				pubkey();
				setState(484);
				signature();
				setState(485);
				mBlockUid();
				setState(486);
				iBlockUid();
				setState(487);
				userid();
				}
				}
				setState(493);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JoinersContext extends ParserRuleContext {
		public int i = 0;
		public List<Cpt_joinerContext> cpt_joiner() {
			return getRuleContexts(Cpt_joinerContext.class);
		}
		public Cpt_joinerContext cpt_joiner(int i) {
			return getRuleContext(Cpt_joinerContext.class,i);
		}
		public List<TerminalNode> WOTNL() { return getTokens(JuniterParser.WOTNL); }
		public TerminalNode WOTNL(int i) {
			return getToken(JuniterParser.WOTNL, i);
		}
		public JoinersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_joiners; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterJoiners(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitJoiners(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitJoiners(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JoinersContext joiners() throws RecognitionException {
		JoinersContext _localctx = new JoinersContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_joiners);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    joiners: ");
			setState(498); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(495);
				cpt_joiner();
				setState(496);
				match(WOTNL);
				}
				}
				setState(500); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_joinerContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public List<TerminalNode> WOTSEP() { return getTokens(JuniterParser.WOTSEP); }
		public TerminalNode WOTSEP(int i) {
			return getToken(JuniterParser.WOTSEP, i);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public MBlockUidContext mBlockUid() {
			return getRuleContext(MBlockUidContext.class,0);
		}
		public IBlockUidContext iBlockUid() {
			return getRuleContext(IBlockUidContext.class,0);
		}
		public UseridContext userid() {
			return getRuleContext(UseridContext.class,0);
		}
		public Cpt_joinerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_joiner; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_joiner(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_joiner(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_joiner(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_joinerContext cpt_joiner() throws RecognitionException {
		Cpt_joinerContext _localctx = new Cpt_joinerContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_cpt_joiner);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(502);
			pubkey();
			setState(503);
			match(WOTSEP);
			setState(504);
			signature();
			setState(505);
			match(WOTSEP);
			setState(506);
			mBlockUid();
			setState(507);
			match(WOTSEP);
			setState(508);
			iBlockUid();
			setState(509);
			match(WOTSEP);
			setState(510);
			userid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentitiesContext extends ParserRuleContext {
		public int i = 0;
		public List<Cpt_idtyContext> cpt_idty() {
			return getRuleContexts(Cpt_idtyContext.class);
		}
		public Cpt_idtyContext cpt_idty(int i) {
			return getRuleContext(Cpt_idtyContext.class,i);
		}
		public IdentitiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identities; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdentities(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdentities(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdentities(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentitiesContext identities() throws RecognitionException {
		IdentitiesContext _localctx = new IdentitiesContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_identities);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    identities: ");
			setState(516);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==WOTPUBK || ((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & ((1L << (PUBKEY_INLINED - 79)) | (1L << (OUTPUBK - 79)) | (1L << (PUBKEY_MULTILN - 79)) | (1L << (CPT_ISS - 79)))) != 0)) {
				{
				{
				setState(513);
				cpt_idty();
				}
				}
				setState(518);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Cpt_idtyContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public List<TerminalNode> WOTSEP() { return getTokens(JuniterParser.WOTSEP); }
		public TerminalNode WOTSEP(int i) {
			return getToken(JuniterParser.WOTSEP, i);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public IBlockUidContext iBlockUid() {
			return getRuleContext(IBlockUidContext.class,0);
		}
		public UseridContext userid() {
			return getRuleContext(UseridContext.class,0);
		}
		public TerminalNode WOTNL() { return getToken(JuniterParser.WOTNL, 0); }
		public Cpt_idtyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cpt_idty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCpt_idty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCpt_idty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCpt_idty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cpt_idtyContext cpt_idty() throws RecognitionException {
		Cpt_idtyContext _localctx = new Cpt_idtyContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_cpt_idty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(519);
			pubkey();
			setState(520);
			match(WOTSEP);
			setState(521);
			signature();
			setState(522);
			match(WOTSEP);
			setState(523);
			iBlockUid();
			setState(524);
			match(WOTSEP);
			setState(525);
			userid();
			setState(526);
			match(WOTNL);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MembersCountContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public MembersCountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membersCount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMembersCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMembersCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMembersCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembersCountContext membersCount() throws RecognitionException {
		MembersCountContext _localctx = new MembersCountContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_membersCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(528);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametersContext extends ParserRuleContext {
		public CContext c;
		public DtContext dt;
		public Ud0Context ud0;
		public SigPeriodContext sigPeriod;
		public SigStockContext sigStock;
		public SigWindowContext sigWindow;
		public SigValidityContext sigValidity;
		public SigQtyContext sigQty;
		public IdtyWindowContext idtyWindow;
		public MsWindowContext msWindow;
		public XpercentContext xpercent;
		public MsValidityContext msValidity;
		public StepMaxContext stepMax;
		public MedianTimeBlocksContext medianTimeBlocks;
		public AvgGenTimeContext avgGenTime;
		public DtDiffEvalContext dtDiffEval;
		public PercentRotContext percentRot;
		public UdTime0Context udTime0;
		public UdReevalTime0Context udReevalTime0;
		public DtReevalContext dtReeval;
		public CContext c() {
			return getRuleContext(CContext.class,0);
		}
		public DtContext dt() {
			return getRuleContext(DtContext.class,0);
		}
		public Ud0Context ud0() {
			return getRuleContext(Ud0Context.class,0);
		}
		public SigPeriodContext sigPeriod() {
			return getRuleContext(SigPeriodContext.class,0);
		}
		public SigStockContext sigStock() {
			return getRuleContext(SigStockContext.class,0);
		}
		public SigWindowContext sigWindow() {
			return getRuleContext(SigWindowContext.class,0);
		}
		public SigValidityContext sigValidity() {
			return getRuleContext(SigValidityContext.class,0);
		}
		public SigQtyContext sigQty() {
			return getRuleContext(SigQtyContext.class,0);
		}
		public IdtyWindowContext idtyWindow() {
			return getRuleContext(IdtyWindowContext.class,0);
		}
		public MsWindowContext msWindow() {
			return getRuleContext(MsWindowContext.class,0);
		}
		public XpercentContext xpercent() {
			return getRuleContext(XpercentContext.class,0);
		}
		public MsValidityContext msValidity() {
			return getRuleContext(MsValidityContext.class,0);
		}
		public StepMaxContext stepMax() {
			return getRuleContext(StepMaxContext.class,0);
		}
		public MedianTimeBlocksContext medianTimeBlocks() {
			return getRuleContext(MedianTimeBlocksContext.class,0);
		}
		public AvgGenTimeContext avgGenTime() {
			return getRuleContext(AvgGenTimeContext.class,0);
		}
		public DtDiffEvalContext dtDiffEval() {
			return getRuleContext(DtDiffEvalContext.class,0);
		}
		public PercentRotContext percentRot() {
			return getRuleContext(PercentRotContext.class,0);
		}
		public UdTime0Context udTime0() {
			return getRuleContext(UdTime0Context.class,0);
		}
		public UdReevalTime0Context udReevalTime0() {
			return getRuleContext(UdReevalTime0Context.class,0);
		}
		public DtReevalContext dtReeval() {
			return getRuleContext(DtReevalContext.class,0);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_parameters);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    parameters: ");
			setState(531);
			((ParametersContext)_localctx).c = c();
			System.out.println("      c: "+(((ParametersContext)_localctx).c!=null?_input.getText(((ParametersContext)_localctx).c.start,((ParametersContext)_localctx).c.stop):null)+" 		# The %growth of the UD every [dt] period ");
			setState(533);
			((ParametersContext)_localctx).dt = dt();
			System.out.println("      dt: "+(((ParametersContext)_localctx).dt!=null?_input.getText(((ParametersContext)_localctx).dt.start,((ParametersContext)_localctx).dt.stop):null)+" 		# Time period between two UD. ");
			setState(535);
			((ParametersContext)_localctx).ud0 = ud0();
			System.out.println("      ud0: "+(((ParametersContext)_localctx).ud0!=null?_input.getText(((ParametersContext)_localctx).ud0.start,((ParametersContext)_localctx).ud0.stop):null)+" # UD(0), i.e. initial Universal Dividend ");
			setState(537);
			((ParametersContext)_localctx).sigPeriod = sigPeriod();
			System.out.println("      sigPeriod: "+(((ParametersContext)_localctx).sigPeriod!=null?_input.getText(((ParametersContext)_localctx).sigPeriod.start,((ParametersContext)_localctx).sigPeriod.stop):null)+" # Minimum delay between 2 certifications of a same issuer, in seconds. Must be positive or zero. ");
			setState(539);
			((ParametersContext)_localctx).sigStock = sigStock();
			System.out.println("      sigStock: "+(((ParametersContext)_localctx).sigStock!=null?_input.getText(((ParametersContext)_localctx).sigStock.start,((ParametersContext)_localctx).sigStock.stop):null)+" # Maximum quantity of active certifications made by member. ");
			setState(541);
			((ParametersContext)_localctx).sigWindow = sigWindow();
			System.out.println("      sigWindow: "+(((ParametersContext)_localctx).sigWindow!=null?_input.getText(((ParametersContext)_localctx).sigWindow.start,((ParametersContext)_localctx).sigWindow.stop):null)+" # Maximum delay a certification can wait before being expired for non-writing. ");
			setState(543);
			((ParametersContext)_localctx).sigValidity = sigValidity();
			System.out.println("      sigValidity: "+(((ParametersContext)_localctx).sigValidity!=null?_input.getText(((ParametersContext)_localctx).sigValidity.start,((ParametersContext)_localctx).sigValidity.stop):null)+" # Maximum age of an active signature (in seconds) ");
			setState(545);
			((ParametersContext)_localctx).sigQty = sigQty();
			System.out.println("      sigQty: "+(((ParametersContext)_localctx).sigQty!=null?_input.getText(((ParametersContext)_localctx).sigQty.start,((ParametersContext)_localctx).sigQty.stop):null)+" # Minimum quantity of signatures to be part of the WoT ");
			setState(547);
			((ParametersContext)_localctx).idtyWindow = idtyWindow();
			System.out.println("      idtyWindow: "+(((ParametersContext)_localctx).idtyWindow!=null?_input.getText(((ParametersContext)_localctx).idtyWindow.start,((ParametersContext)_localctx).idtyWindow.stop):null)+" # Maximum delay an identity can wait before being expired for non-writing. ");
			setState(549);
			((ParametersContext)_localctx).msWindow = msWindow();
			System.out.println("      msWindow: "+(((ParametersContext)_localctx).msWindow!=null?_input.getText(((ParametersContext)_localctx).msWindow.start,((ParametersContext)_localctx).msWindow.stop):null)+" # Maximum delay a membership can wait before being expired for non-writing. ");
			setState(551);
			((ParametersContext)_localctx).xpercent = xpercent();
			System.out.println("      xpercent: "+(((ParametersContext)_localctx).xpercent!=null?_input.getText(((ParametersContext)_localctx).xpercent.start,((ParametersContext)_localctx).xpercent.stop):null)+" # Minimum % of sentries to reach to match the distance rule ");
			setState(553);
			((ParametersContext)_localctx).msValidity = msValidity();
			System.out.println("      msValidity: "+(((ParametersContext)_localctx).msValidity!=null?_input.getText(((ParametersContext)_localctx).msValidity.start,((ParametersContext)_localctx).msValidity.stop):null)+" # Maximum age of an active membership (in seconds) ");
			setState(555);
			((ParametersContext)_localctx).stepMax = stepMax();
			System.out.println("      stepMax: "+(((ParametersContext)_localctx).stepMax!=null?_input.getText(((ParametersContext)_localctx).stepMax.start,((ParametersContext)_localctx).stepMax.stop):null)+" # Maximum distance between each WoT member and a newcomer ");
			setState(557);
			((ParametersContext)_localctx).medianTimeBlocks = medianTimeBlocks();
			System.out.println("      medianTimeBlocks: "+(((ParametersContext)_localctx).medianTimeBlocks!=null?_input.getText(((ParametersContext)_localctx).medianTimeBlocks.start,((ParametersContext)_localctx).medianTimeBlocks.stop):null)+" # Number of blocks used for calculating median time. ");
			setState(559);
			((ParametersContext)_localctx).avgGenTime = avgGenTime();
			System.out.println("      avgGenTime: "+(((ParametersContext)_localctx).avgGenTime!=null?_input.getText(((ParametersContext)_localctx).avgGenTime.start,((ParametersContext)_localctx).avgGenTime.stop):null)+" # The average time for writing 1 block (wished time) ");
			setState(561);
			((ParametersContext)_localctx).dtDiffEval = dtDiffEval();
			System.out.println("      dtDiffEval: "+(((ParametersContext)_localctx).dtDiffEval!=null?_input.getText(((ParametersContext)_localctx).dtDiffEval.start,((ParametersContext)_localctx).dtDiffEval.stop):null)+" # The number of blocks required to evaluate again PoWMin value ");
			setState(563);
			((ParametersContext)_localctx).percentRot = percentRot();
			System.out.println("      percentRot: "+(((ParametersContext)_localctx).percentRot!=null?_input.getText(((ParametersContext)_localctx).percentRot.start,((ParametersContext)_localctx).percentRot.stop):null)+" # The % of previous issuers to reach for personalized difficulty ");
			setState(565);
			((ParametersContext)_localctx).udTime0 = udTime0();
			System.out.println("      udTime0: "+(((ParametersContext)_localctx).udTime0!=null?_input.getText(((ParametersContext)_localctx).udTime0.start,((ParametersContext)_localctx).udTime0.stop):null)+" # Time of first UD. ");
			setState(567);
			((ParametersContext)_localctx).udReevalTime0 = udReevalTime0();
			System.out.println("      udReevalTime0: "+(((ParametersContext)_localctx).udReevalTime0!=null?_input.getText(((ParametersContext)_localctx).udReevalTime0.start,((ParametersContext)_localctx).udReevalTime0.stop):null)+" # Time of first reevaluation of the UD. ");
			setState(569);
			((ParametersContext)_localctx).dtReeval = dtReeval();
			System.out.println("      dtReeval: "+(((ParametersContext)_localctx).dtReeval!=null?_input.getText(((ParametersContext)_localctx).dtReeval.start,((ParametersContext)_localctx).dtReeval.stop):null)+" 		# Time period between two re-evaluation of the UD. ");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PeerContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public PubkeyContext pubkey;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_PEER() { return getToken(JuniterParser.DOCTYPE_PEER, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public EndpointsContext endpoints() {
			return getRuleContext(EndpointsContext.class,0);
		}
		public PeerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_peer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPeer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPeer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPeer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PeerContext peer() throws RecognitionException {
		PeerContext _localctx = new PeerContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_peer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("  peer: ");
			setState(573);
			((PeerContext)_localctx).version = version();
			System.out.println("    version: "+(((PeerContext)_localctx).version!=null?_input.getText(((PeerContext)_localctx).version.start,((PeerContext)_localctx).version.stop):null) );
			setState(575);
			match(DOCTYPE_PEER);
			setState(576);
			((PeerContext)_localctx).currency = currency();
			System.out.println("    currency: "+(((PeerContext)_localctx).currency!=null?_input.getText(((PeerContext)_localctx).currency.start,((PeerContext)_localctx).currency.stop):null));
			setState(578);
			((PeerContext)_localctx).pubkey = pubkey();
			System.out.println("    pubkey: "+(((PeerContext)_localctx).pubkey!=null?_input.getText(((PeerContext)_localctx).pubkey.start,((PeerContext)_localctx).pubkey.stop):null));
			System.out.println("    block: ");indent="      ";
			setState(581);
			block();
			setState(582);
			endpoints();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TransactionContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public BlockstampContext blockstamp;
		public LocktimeContext locktime;
		public CommentContext comment;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_TRAN() { return getToken(JuniterParser.DOCTYPE_TRAN, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public BlockstampContext blockstamp() {
			return getRuleContext(BlockstampContext.class,0);
		}
		public LocktimeContext locktime() {
			return getRuleContext(LocktimeContext.class,0);
		}
		public IssuersContext issuers() {
			return getRuleContext(IssuersContext.class,0);
		}
		public InputsContext inputs() {
			return getRuleContext(InputsContext.class,0);
		}
		public UnlocksContext unlocks() {
			return getRuleContext(UnlocksContext.class,0);
		}
		public OutputsContext outputs() {
			return getRuleContext(OutputsContext.class,0);
		}
		public SignaturesContext signatures() {
			return getRuleContext(SignaturesContext.class,0);
		}
		public CommentContext comment() {
			return getRuleContext(CommentContext.class,0);
		}
		public TransactionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_transaction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTransaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTransaction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTransaction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TransactionContext transaction() throws RecognitionException {
		TransactionContext _localctx = new TransactionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_transaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("  transaction:");
			setState(585);
			((TransactionContext)_localctx).version = version();
			System.out.println("    version: "+(((TransactionContext)_localctx).version!=null?_input.getText(((TransactionContext)_localctx).version.start,((TransactionContext)_localctx).version.stop):null) );
			setState(587);
			match(DOCTYPE_TRAN);
			setState(588);
			((TransactionContext)_localctx).currency = currency();
			System.out.println("    currency: "+(((TransactionContext)_localctx).currency!=null?_input.getText(((TransactionContext)_localctx).currency.start,((TransactionContext)_localctx).currency.stop):null));
			setState(590);
			((TransactionContext)_localctx).blockstamp = blockstamp();
			System.out.println("    blockstamp: "+(((TransactionContext)_localctx).blockstamp!=null?_input.getText(((TransactionContext)_localctx).blockstamp.start,((TransactionContext)_localctx).blockstamp.stop):null));
			setState(592);
			((TransactionContext)_localctx).locktime = locktime();
			System.out.println("    locktime: "+(((TransactionContext)_localctx).locktime!=null?_input.getText(((TransactionContext)_localctx).locktime.start,((TransactionContext)_localctx).locktime.stop):null));
			setState(594);
			issuers();
			setState(595);
			inputs();
			setState(596);
			unlocks();
			setState(597);
			outputs();
			setState(598);
			signatures();
			setState(599);
			((TransactionContext)_localctx).comment = comment();
			System.out.println("    comment: "+(((TransactionContext)_localctx).comment!=null?_input.getText(((TransactionContext)_localctx).comment.start,((TransactionContext)_localctx).comment.stop):null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class WotContext extends ParserRuleContext {
		public SignatureContext signature;
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public MembershipContext membership() {
			return getRuleContext(MembershipContext.class,0);
		}
		public RevocationContext revocation() {
			return getRuleContext(RevocationContext.class,0);
		}
		public CertificationContext certification() {
			return getRuleContext(CertificationContext.class,0);
		}
		public WotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterWot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitWot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitWot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WotContext wot() throws RecognitionException {
		WotContext _localctx = new WotContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_wot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("  wot: 		#  Identity,Certification, Membership, Revocation"); 
			setState(607);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(603);
				identity();
				}
				break;
			case 2:
				{
				setState(604);
				membership();
				}
				break;
			case 3:
				{
				setState(605);
				revocation();
				}
				break;
			case 4:
				{
				setState(606);
				certification();
				}
				break;
			}
			setState(609);
			((WotContext)_localctx).signature = signature();
			System.out.println("    signature: "+(((WotContext)_localctx).signature!=null?_input.getText(((WotContext)_localctx).signature.start,((WotContext)_localctx).signature.stop):null) + " # isValid? " );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentityContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public IssuerContext issuer;
		public UseridContext userid;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_IDTY() { return getToken(JuniterParser.DOCTYPE_IDTY, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public UseridContext userid() {
			return getRuleContext(UseridContext.class,0);
		}
		public TimestampContext timestamp() {
			return getRuleContext(TimestampContext.class,0);
		}
		public IdentityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdentity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdentity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentityContext identity() throws RecognitionException {
		IdentityContext _localctx = new IdentityContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_identity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    identity:");
			setState(613);
			((IdentityContext)_localctx).version = version();
			System.out.println("      version: "+(((IdentityContext)_localctx).version!=null?_input.getText(((IdentityContext)_localctx).version.start,((IdentityContext)_localctx).version.stop):null) );
			setState(615);
			match(DOCTYPE_IDTY);
			setState(616);
			((IdentityContext)_localctx).currency = currency();
			System.out.println("      currency: "+(((IdentityContext)_localctx).currency!=null?_input.getText(((IdentityContext)_localctx).currency.start,((IdentityContext)_localctx).currency.stop):null));
			setState(618);
			((IdentityContext)_localctx).issuer = issuer();
			System.out.println("      issuer: "+(((IdentityContext)_localctx).issuer!=null?_input.getText(((IdentityContext)_localctx).issuer.start,((IdentityContext)_localctx).issuer.stop):null)); 
			setState(620);
			((IdentityContext)_localctx).userid = userid();
			System.out.println("      userid: "+(((IdentityContext)_localctx).userid!=null?_input.getText(((IdentityContext)_localctx).userid.start,((IdentityContext)_localctx).userid.stop):null));
			System.out.println("      timestamp: ");
			setState(623);
			timestamp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CertificationContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public IssuerContext iss;
		public IdtyIssuerContext idi;
		public IdtyUniqueIDContext uid;
		public IdtySignatureContext ids;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_CERT() { return getToken(JuniterParser.DOCTYPE_CERT, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public IdtyTimestampContext idtyTimestamp() {
			return getRuleContext(IdtyTimestampContext.class,0);
		}
		public TerminalNode IdtySignature_() { return getToken(JuniterParser.IdtySignature_, 0); }
		public TerminalNode CertTimestamp_() { return getToken(JuniterParser.CertTimestamp_, 0); }
		public CertTimestampContext certTimestamp() {
			return getRuleContext(CertTimestampContext.class,0);
		}
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public IdtyIssuerContext idtyIssuer() {
			return getRuleContext(IdtyIssuerContext.class,0);
		}
		public IdtyUniqueIDContext idtyUniqueID() {
			return getRuleContext(IdtyUniqueIDContext.class,0);
		}
		public IdtySignatureContext idtySignature() {
			return getRuleContext(IdtySignatureContext.class,0);
		}
		public CertificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCertification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCertification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCertification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CertificationContext certification() throws RecognitionException {
		CertificationContext _localctx = new CertificationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_certification);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    certification: ");
			setState(626);
			((CertificationContext)_localctx).version = version();
			System.out.println("      version: "+(((CertificationContext)_localctx).version!=null?_input.getText(((CertificationContext)_localctx).version.start,((CertificationContext)_localctx).version.stop):null) );
			setState(628);
			match(DOCTYPE_CERT);
			setState(629);
			((CertificationContext)_localctx).currency = currency();
			System.out.println("      currency: "+(((CertificationContext)_localctx).currency!=null?_input.getText(((CertificationContext)_localctx).currency.start,((CertificationContext)_localctx).currency.stop):null));
			setState(631);
			((CertificationContext)_localctx).iss = issuer();
			System.out.println("      issuer: "+(((CertificationContext)_localctx).iss!=null?_input.getText(((CertificationContext)_localctx).iss.start,((CertificationContext)_localctx).iss.stop):null));
			setState(633);
			((CertificationContext)_localctx).idi = idtyIssuer();
			System.out.println("      idtyIssuer: "+(((CertificationContext)_localctx).idi!=null?_input.getText(((CertificationContext)_localctx).idi.start,((CertificationContext)_localctx).idi.stop):null));
			setState(635);
			((CertificationContext)_localctx).uid = idtyUniqueID();
			System.out.println("      idtyUniqueID: "+(((CertificationContext)_localctx).uid!=null?_input.getText(((CertificationContext)_localctx).uid.start,((CertificationContext)_localctx).uid.stop):null));
			System.out.println("      idtyTimestamp: ");
			setState(638);
			idtyTimestamp();
			setState(639);
			match(IdtySignature_);
			setState(640);
			((CertificationContext)_localctx).ids = idtySignature();
			System.out.println("      idtySignature: "+(((CertificationContext)_localctx).ids!=null?_input.getText(((CertificationContext)_localctx).ids.start,((CertificationContext)_localctx).ids.stop):null));
			System.out.println("      certTimestamp: ");
			setState(643);
			match(CertTimestamp_);
			setState(644);
			certTimestamp();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MembershipContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public IssuerContext issuer;
		public MemberContext member;
		public UserIDContext userID;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_MEMB() { return getToken(JuniterParser.DOCTYPE_MEMB, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MemberContext member() {
			return getRuleContext(MemberContext.class,0);
		}
		public UserIDContext userID() {
			return getRuleContext(UserIDContext.class,0);
		}
		public CertTSContext certTS() {
			return getRuleContext(CertTSContext.class,0);
		}
		public MembershipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membership; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMembership(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMembership(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MembershipContext membership() throws RecognitionException {
		MembershipContext _localctx = new MembershipContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_membership);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    membership:");
			setState(647);
			((MembershipContext)_localctx).version = version();
			System.out.println("      version: "+(((MembershipContext)_localctx).version!=null?_input.getText(((MembershipContext)_localctx).version.start,((MembershipContext)_localctx).version.stop):null) );
			setState(649);
			match(DOCTYPE_MEMB);
			setState(650);
			((MembershipContext)_localctx).currency = currency();
			System.out.println("      currency: "+(((MembershipContext)_localctx).currency!=null?_input.getText(((MembershipContext)_localctx).currency.start,((MembershipContext)_localctx).currency.stop):null));
			setState(652);
			((MembershipContext)_localctx).issuer = issuer();
			System.out.println("      issuer: "+(((MembershipContext)_localctx).issuer!=null?_input.getText(((MembershipContext)_localctx).issuer.start,((MembershipContext)_localctx).issuer.stop):null));
			System.out.println("      block: "); indent="        ";
			setState(655);
			block();
			setState(656);
			((MembershipContext)_localctx).member = member();
			System.out.println("      member: "+(((MembershipContext)_localctx).member!=null?_input.getText(((MembershipContext)_localctx).member.start,((MembershipContext)_localctx).member.stop):null));
			setState(658);
			((MembershipContext)_localctx).userID = userID();
			System.out.println("      userID: "+(((MembershipContext)_localctx).userID!=null?_input.getText(((MembershipContext)_localctx).userID.start,((MembershipContext)_localctx).userID.stop):null));
			System.out.println("      certTS: ");indent="        ";
			setState(661);
			certTS();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RevocationContext extends ParserRuleContext {
		public VersionContext version;
		public CurrencyContext currency;
		public IssuerContext issuer;
		public IdtyUniqueIDContext idtyUniqueID;
		public IdtySignatureContext idtySignature;
		public VersionContext version() {
			return getRuleContext(VersionContext.class,0);
		}
		public TerminalNode DOCTYPE_REVO() { return getToken(JuniterParser.DOCTYPE_REVO, 0); }
		public CurrencyContext currency() {
			return getRuleContext(CurrencyContext.class,0);
		}
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public IdtyUniqueIDContext idtyUniqueID() {
			return getRuleContext(IdtyUniqueIDContext.class,0);
		}
		public IdtyTimestampContext idtyTimestamp() {
			return getRuleContext(IdtyTimestampContext.class,0);
		}
		public TerminalNode IdtySignature_() { return getToken(JuniterParser.IdtySignature_, 0); }
		public IdtySignatureContext idtySignature() {
			return getRuleContext(IdtySignatureContext.class,0);
		}
		public RevocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_revocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterRevocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitRevocation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitRevocation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RevocationContext revocation() throws RecognitionException {
		RevocationContext _localctx = new RevocationContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_revocation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    revocation:");
			setState(664);
			((RevocationContext)_localctx).version = version();
			System.out.println("      version: "+(((RevocationContext)_localctx).version!=null?_input.getText(((RevocationContext)_localctx).version.start,((RevocationContext)_localctx).version.stop):null) );
			setState(666);
			match(DOCTYPE_REVO);
			setState(667);
			((RevocationContext)_localctx).currency = currency();
			System.out.println("      currency: "+(((RevocationContext)_localctx).currency!=null?_input.getText(((RevocationContext)_localctx).currency.start,((RevocationContext)_localctx).currency.stop):null));
			setState(669);
			((RevocationContext)_localctx).issuer = issuer();
			System.out.println("      issuer: "+(((RevocationContext)_localctx).issuer!=null?_input.getText(((RevocationContext)_localctx).issuer.start,((RevocationContext)_localctx).issuer.stop):null));
			setState(671);
			((RevocationContext)_localctx).idtyUniqueID = idtyUniqueID();
			System.out.println("      idtyUniqueID: "+(((RevocationContext)_localctx).idtyUniqueID!=null?_input.getText(((RevocationContext)_localctx).idtyUniqueID.start,((RevocationContext)_localctx).idtyUniqueID.stop):null));
			System.out.println("      idtyTimestamp: "); indent="        ";
			setState(674);
			idtyTimestamp();
			setState(675);
			match(IdtySignature_);
			setState(676);
			((RevocationContext)_localctx).idtySignature = idtySignature();
			System.out.println("      idtySignature: "+(((RevocationContext)_localctx).idtySignature!=null?_input.getText(((RevocationContext)_localctx).idtySignature.start,((RevocationContext)_localctx).idtySignature.stop):null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndpointsContext extends ParserRuleContext {
		public int i = 0;
		public List<EnpointContext> enpoint() {
			return getRuleContexts(EnpointContext.class);
		}
		public EnpointContext enpoint(int i) {
			return getRuleContext(EnpointContext.class,i);
		}
		public TerminalNode EOF() { return getToken(JuniterParser.EOF, 0); }
		public EndpointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterEndpoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitEndpoints(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitEndpoints(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndpointsContext endpoints() throws RecognitionException {
		EndpointsContext _localctx = new EndpointsContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_endpoints);
		try {
			int _alt;
			setState(688);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				System.out.println("    endpoints: ");
				setState(683); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(680);
						if (!(_localctx.i<maxEndpoints)) throw new FailedPredicateException(this, "$i<maxEndpoints");
						System.out.println("      "+ _localctx.i++ +": ");
						setState(682);
						enpoint();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(685); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,21,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(687);
				match(EOF);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnpointContext extends ParserRuleContext {
		public EndpointTypeContext endpointType;
		public DnsContext dns;
		public Ip4Context ip4;
		public Ip6Context ip6;
		public PortContext port;
		public EndpointTypeContext endpointType() {
			return getRuleContext(EndpointTypeContext.class,0);
		}
		public List<TerminalNode> FIELD_SEP() { return getTokens(JuniterParser.FIELD_SEP); }
		public TerminalNode FIELD_SEP(int i) {
			return getToken(JuniterParser.FIELD_SEP, i);
		}
		public TerminalNode ENDPT_SEP() { return getToken(JuniterParser.ENDPT_SEP, 0); }
		public SessionidContext sessionid() {
			return getRuleContext(SessionidContext.class,0);
		}
		public List<DnsContext> dns() {
			return getRuleContexts(DnsContext.class);
		}
		public DnsContext dns(int i) {
			return getRuleContext(DnsContext.class,i);
		}
		public List<Ip4Context> ip4() {
			return getRuleContexts(Ip4Context.class);
		}
		public Ip4Context ip4(int i) {
			return getRuleContext(Ip4Context.class,i);
		}
		public List<Ip6Context> ip6() {
			return getRuleContexts(Ip6Context.class);
		}
		public Ip6Context ip6(int i) {
			return getRuleContext(Ip6Context.class,i);
		}
		public List<PortContext> port() {
			return getRuleContexts(PortContext.class);
		}
		public PortContext port(int i) {
			return getRuleContext(PortContext.class,i);
		}
		public EnpointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enpoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterEnpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitEnpoint(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitEnpoint(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnpointContext enpoint() throws RecognitionException {
		EnpointContext _localctx = new EnpointContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_enpoint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
			((EnpointContext)_localctx).endpointType = endpointType();
			setState(691);
			match(FIELD_SEP);
			System.out.println("        type: "+(((EnpointContext)_localctx).endpointType!=null?_input.getText(((EnpointContext)_localctx).endpointType.start,((EnpointContext)_localctx).endpointType.stop):null));
			setState(696);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SESSID) {
				{
				setState(693);
				sessionid();
				setState(694);
				match(FIELD_SEP);
				}
			}

			setState(713); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(713);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case DNS:
					{
					setState(698);
					((EnpointContext)_localctx).dns = dns();
					setState(699);
					match(FIELD_SEP);
					System.out.println("        dns: "+(((EnpointContext)_localctx).dns!=null?_input.getText(((EnpointContext)_localctx).dns.start,((EnpointContext)_localctx).dns.stop):null));
					}
					break;
				case IP4:
					{
					setState(702);
					((EnpointContext)_localctx).ip4 = ip4();
					setState(703);
					match(FIELD_SEP);
					System.out.println("        ip4: "+(((EnpointContext)_localctx).ip4!=null?_input.getText(((EnpointContext)_localctx).ip4.start,((EnpointContext)_localctx).ip4.stop):null));
					}
					break;
				case IP6:
					{
					setState(706);
					((EnpointContext)_localctx).ip6 = ip6();
					setState(707);
					match(FIELD_SEP);
					System.out.println("        ip6: "+(((EnpointContext)_localctx).ip6!=null?_input.getText(((EnpointContext)_localctx).ip6.start,((EnpointContext)_localctx).ip6.stop):null));
					}
					break;
				case PORT:
					{
					setState(710);
					((EnpointContext)_localctx).port = port();
					System.out.println("        port: "+(((EnpointContext)_localctx).port!=null?_input.getText(((EnpointContext)_localctx).port.start,((EnpointContext)_localctx).port.stop):null));
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(715); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 142)) & ~0x3f) == 0 && ((1L << (_la - 142)) & ((1L << (IP4 - 142)) | (1L << (IP6 - 142)) | (1L << (DNS - 142)) | (1L << (PORT - 142)))) != 0) );
			setState(717);
			match(ENDPT_SEP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SessionidContext extends ParserRuleContext {
		public TerminalNode SESSID() { return getToken(JuniterParser.SESSID, 0); }
		public SessionidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sessionid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSessionid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSessionid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSessionid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SessionidContext sessionid() throws RecognitionException {
		SessionidContext _localctx = new SessionidContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_sessionid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(719);
			match(SESSID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdtyTimestampContext extends ParserRuleContext {
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public IdtyTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdtyTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdtyTimestamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdtyTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdtyTimestampContext idtyTimestamp() throws RecognitionException {
		IdtyTimestampContext _localctx = new IdtyTimestampContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_idtyTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			indent="        ";
			setState(722);
			buid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdtyUniqueIDContext extends ParserRuleContext {
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public IdtyUniqueIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyUniqueID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdtyUniqueID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdtyUniqueID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdtyUniqueID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdtyUniqueIDContext idtyUniqueID() throws RecognitionException {
		IdtyUniqueIDContext _localctx = new IdtyUniqueIDContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_idtyUniqueID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(724);
			match(USERID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SignaturesContext extends ParserRuleContext {
		public int i = 0;
		public SignatureContext signature;
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public List<TerminalNode> SIGN_SEP() { return getTokens(JuniterParser.SIGN_SEP); }
		public TerminalNode SIGN_SEP(int i) {
			return getToken(JuniterParser.SIGN_SEP, i);
		}
		public SignaturesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signatures; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSignatures(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSignatures(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSignatures(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignaturesContext signatures() throws RecognitionException {
		SignaturesContext _localctx = new SignaturesContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_signatures);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    signatures: ");
			setState(732); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(727);
					if (!( _localctx.i < nbIssu )) throw new FailedPredicateException(this, " $i < nbIssu ");
					setState(728);
					((SignaturesContext)_localctx).signature = signature();
					System.out.println("      "+ _localctx.i++ +": "+(((SignaturesContext)_localctx).signature!=null?_input.getText(((SignaturesContext)_localctx).signature.start,((SignaturesContext)_localctx).signature.stop):null));
					setState(730);
					match(SIGN_SEP);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(734); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CommentContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(JuniterParser.COMMENT, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			match(COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LocktimeContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public LocktimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locktime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterLocktime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitLocktime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitLocktime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocktimeContext locktime() throws RecognitionException {
		LocktimeContext _localctx = new LocktimeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_locktime);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(738);
			_la = _input.LA(1);
			if ( !(_la==NUMB || _la==BLNUMB) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockstampContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public BlockstampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockstamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBlockstamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBlockstamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBlockstamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockstampContext blockstamp() throws RecognitionException {
		BlockstampContext _localctx = new BlockstampContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_blockstamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(740);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputsContext extends ParserRuleContext {
		public int i = 0;
		public List<InputContext> input() {
			return getRuleContexts(InputContext.class);
		}
		public InputContext input(int i) {
			return getRuleContext(InputContext.class,i);
		}
		public List<TerminalNode> INPUT_SEP() { return getTokens(JuniterParser.INPUT_SEP); }
		public TerminalNode INPUT_SEP(int i) {
			return getToken(JuniterParser.INPUT_SEP, i);
		}
		public List<TerminalNode> BREAK() { return getTokens(JuniterParser.BREAK); }
		public TerminalNode BREAK(int i) {
			return getToken(JuniterParser.BREAK, i);
		}
		public InputsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterInputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitInputs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitInputs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputsContext inputs() throws RecognitionException {
		InputsContext _localctx = new InputsContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_inputs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    inputs: ");
			setState(747); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				System.out.println("      "+ _localctx.i++ +": ");
				setState(744);
				input();
				setState(745);
				_la = _input.LA(1);
				if ( !(_la==INPUT_SEP || _la==BREAK) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(749); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (OUTNUMB - 94)) | (1L << (INNUMB - 94)) | (1L << (CPT_NUM - 94)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InputContext extends ParserRuleContext {
		public AmountContext amount;
		public BaseContext base;
		public AmountContext amount() {
			return getRuleContext(AmountContext.class,0);
		}
		public BaseContext base() {
			return getRuleContext(BaseContext.class,0);
		}
		public TerminalNode CPT_COL() { return getToken(JuniterParser.CPT_COL, 0); }
		public TerminalNode INFIELD_SEP() { return getToken(JuniterParser.INFIELD_SEP, 0); }
		public UdContext ud() {
			return getRuleContext(UdContext.class,0);
		}
		public TxContext tx() {
			return getRuleContext(TxContext.class,0);
		}
		public TerminalNode DIVIDEND_TYPE() { return getToken(JuniterParser.DIVIDEND_TYPE, 0); }
		public TerminalNode CPT_DIV() { return getToken(JuniterParser.CPT_DIV, 0); }
		public TerminalNode TRANSACTION_TYPE() { return getToken(JuniterParser.TRANSACTION_TYPE, 0); }
		public InputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitInput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitInput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputContext input() throws RecognitionException {
		InputContext _localctx = new InputContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_input);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			((InputContext)_localctx).amount = amount();
			System.out.println("        amount: "+(((InputContext)_localctx).amount!=null?_input.getText(((InputContext)_localctx).amount.start,((InputContext)_localctx).amount.stop):null));
			setState(753);
			_la = _input.LA(1);
			if ( !(_la==INFIELD_SEP || _la==CPT_COL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(754);
			((InputContext)_localctx).base = base();
			System.out.println("        base: "+(((InputContext)_localctx).base!=null?_input.getText(((InputContext)_localctx).base.start,((InputContext)_localctx).base.stop):null));
			setState(760);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				{
				setState(756);
				_la = _input.LA(1);
				if ( !(_la==DIVIDEND_TYPE || _la==CPT_DIV) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(757);
				ud();
				}
				}
				break;
			case 2:
				{
				{
				setState(758);
				_la = _input.LA(1);
				if ( !(_la==TRANSACTION_TYPE || _la==CPT_DIV) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(759);
				tx();
				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UdContext extends ParserRuleContext {
		public PubkeyContext pubkey;
		public BnumContext bnum;
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public BnumContext bnum() {
			return getRuleContext(BnumContext.class,0);
		}
		public TerminalNode CPT_COL() { return getToken(JuniterParser.CPT_COL, 0); }
		public TerminalNode INFIELD_SEP() { return getToken(JuniterParser.INFIELD_SEP, 0); }
		public UdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ud; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UdContext ud() throws RecognitionException {
		UdContext _localctx = new UdContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_ud);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("        ud:  # Universal Basic Income");
			setState(763);
			((UdContext)_localctx).pubkey = pubkey();
			System.out.println("          pubkey: "+(((UdContext)_localctx).pubkey!=null?_input.getText(((UdContext)_localctx).pubkey.start,((UdContext)_localctx).pubkey.stop):null)+" # of this pubkey");
			setState(765);
			_la = _input.LA(1);
			if ( !(_la==INFIELD_SEP || _la==CPT_COL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(766);
			((UdContext)_localctx).bnum = bnum();
			System.out.println("          bnum: "+(((UdContext)_localctx).bnum!=null?_input.getText(((UdContext)_localctx).bnum.start,((UdContext)_localctx).bnum.stop):null)+ " # at that block number ");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TxContext extends ParserRuleContext {
		public BhashContext bhash;
		public TindexContext tindex;
		public BhashContext bhash() {
			return getRuleContext(BhashContext.class,0);
		}
		public TindexContext tindex() {
			return getRuleContext(TindexContext.class,0);
		}
		public TerminalNode CPT_COL() { return getToken(JuniterParser.CPT_COL, 0); }
		public TerminalNode INFIELD_SEP() { return getToken(JuniterParser.INFIELD_SEP, 0); }
		public TxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TxContext tx() throws RecognitionException {
		TxContext _localctx = new TxContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_tx);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("        tx:  # a previous Transaction");
			setState(770);
			((TxContext)_localctx).bhash = bhash();
			System.out.println("          bhash: "+(((TxContext)_localctx).bhash!=null?_input.getText(((TxContext)_localctx).bhash.start,((TxContext)_localctx).bhash.stop):null)+" # in this block hash");
			setState(772);
			_la = _input.LA(1);
			if ( !(_la==INFIELD_SEP || _la==CPT_COL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(773);
			((TxContext)_localctx).tindex = tindex();
			System.out.println("          tindex: "+(((TxContext)_localctx).tindex!=null?_input.getText(((TxContext)_localctx).tindex.start,((TxContext)_localctx).tindex.stop):null)+" # at that index");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AmountContext extends ParserRuleContext {
		public TerminalNode INNUMB() { return getToken(JuniterParser.INNUMB, 0); }
		public TerminalNode OUTNUMB() { return getToken(JuniterParser.OUTNUMB, 0); }
		public TerminalNode CPT_NUM() { return getToken(JuniterParser.CPT_NUM, 0); }
		public AmountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_amount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterAmount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitAmount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitAmount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AmountContext amount() throws RecognitionException {
		AmountContext _localctx = new AmountContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_amount);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(776);
			_la = _input.LA(1);
			if ( !(((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (OUTNUMB - 94)) | (1L << (INNUMB - 94)) | (1L << (CPT_NUM - 94)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BaseContext extends ParserRuleContext {
		public TerminalNode INNUMB() { return getToken(JuniterParser.INNUMB, 0); }
		public TerminalNode OUTNUMB() { return getToken(JuniterParser.OUTNUMB, 0); }
		public TerminalNode CPT_NUM() { return getToken(JuniterParser.CPT_NUM, 0); }
		public BaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_base; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseContext base() throws RecognitionException {
		BaseContext _localctx = new BaseContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_base);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(778);
			_la = _input.LA(1);
			if ( !(((((_la - 94)) & ~0x3f) == 0 && ((1L << (_la - 94)) & ((1L << (OUTNUMB - 94)) | (1L << (INNUMB - 94)) | (1L << (CPT_NUM - 94)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TindexContext extends ParserRuleContext {
		public TerminalNode INNUMB() { return getToken(JuniterParser.INNUMB, 0); }
		public TindexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tindex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTindex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTindex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTindex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TindexContext tindex() throws RecognitionException {
		TindexContext _localctx = new TindexContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_tindex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(780);
			match(INNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnlocksContext extends ParserRuleContext {
		public int i = 0;
		public List<UnlockContext> unlock() {
			return getRuleContexts(UnlockContext.class);
		}
		public UnlockContext unlock(int i) {
			return getRuleContext(UnlockContext.class,i);
		}
		public List<TerminalNode> UNLOCK_SEP() { return getTokens(JuniterParser.UNLOCK_SEP); }
		public TerminalNode UNLOCK_SEP(int i) {
			return getToken(JuniterParser.UNLOCK_SEP, i);
		}
		public UnlocksContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unlocks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUnlocks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUnlocks(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUnlocks(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnlocksContext unlocks() throws RecognitionException {
		UnlocksContext _localctx = new UnlocksContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_unlocks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    unlocks: ");
			setState(787); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				System.out.println("      "+ _localctx.i++ +": ");
				setState(784);
				unlock();
				setState(785);
				match(UNLOCK_SEP);
				}
				}
				setState(789); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==UNNUMB );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnlockContext extends ParserRuleContext {
		public In_indexContext in_index;
		public UnsigContext unsig;
		public UnxhxContext unxhx;
		public In_indexContext in_index() {
			return getRuleContext(In_indexContext.class,0);
		}
		public UnsigContext unsig() {
			return getRuleContext(UnsigContext.class,0);
		}
		public TerminalNode UNXHX() { return getToken(JuniterParser.UNXHX, 0); }
		public UnxhxContext unxhx() {
			return getRuleContext(UnxhxContext.class,0);
		}
		public TerminalNode UNSIG() { return getToken(JuniterParser.UNSIG, 0); }
		public UnlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUnlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUnlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUnlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnlockContext unlock() throws RecognitionException {
		UnlockContext _localctx = new UnlockContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_unlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(791);
			((UnlockContext)_localctx).in_index = in_index();
			System.out.println("        in_index: "+(((UnlockContext)_localctx).in_index!=null?_input.getText(((UnlockContext)_localctx).in_index.start,((UnlockContext)_localctx).in_index.stop):null)+"");
			System.out.println("        ul_condition: ");
			setState(802);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case UNSIG:
				{
				{
				setState(794);
				match(UNSIG);
				}
				setState(795);
				((UnlockContext)_localctx).unsig = unsig();
				System.out.println("          unsig: "+(((UnlockContext)_localctx).unsig!=null?_input.getText(((UnlockContext)_localctx).unsig.start,((UnlockContext)_localctx).unsig.stop):null)+"");
				}
				break;
			case UNXHX:
				{
				setState(798);
				match(UNXHX);
				setState(799);
				((UnlockContext)_localctx).unxhx = unxhx();
				System.out.println("          unxhx: "+(((UnlockContext)_localctx).unxhx!=null?_input.getText(((UnlockContext)_localctx).unxhx.start,((UnlockContext)_localctx).unxhx.stop):null)+"");
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class In_indexContext extends ParserRuleContext {
		public TerminalNode UNNUMB() { return getToken(JuniterParser.UNNUMB, 0); }
		public In_indexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in_index; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIn_index(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIn_index(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIn_index(this);
			else return visitor.visitChildren(this);
		}
	}

	public final In_indexContext in_index() throws RecognitionException {
		In_indexContext _localctx = new In_indexContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_in_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(804);
			match(UNNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnsigContext extends ParserRuleContext {
		public TerminalNode UNNUMB() { return getToken(JuniterParser.UNNUMB, 0); }
		public UnsigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUnsig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUnsig(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUnsig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnsigContext unsig() throws RecognitionException {
		UnsigContext _localctx = new UnsigContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_unsig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(806);
			match(UNNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnxhxContext extends ParserRuleContext {
		public TerminalNode UNNUMB() { return getToken(JuniterParser.UNNUMB, 0); }
		public UnxhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unxhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUnxhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUnxhx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUnxhx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnxhxContext unxhx() throws RecognitionException {
		UnxhxContext _localctx = new UnxhxContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_unxhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(808);
			match(UNNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutputsContext extends ParserRuleContext {
		public int i = 0;
		public List<OutputContext> output() {
			return getRuleContexts(OutputContext.class);
		}
		public OutputContext output(int i) {
			return getRuleContext(OutputContext.class,i);
		}
		public List<TerminalNode> OUTPUT_SEP() { return getTokens(JuniterParser.OUTPUT_SEP); }
		public TerminalNode OUTPUT_SEP(int i) {
			return getToken(JuniterParser.OUTPUT_SEP, i);
		}
		public OutputsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outputs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterOutputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitOutputs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitOutputs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OutputsContext outputs() throws RecognitionException {
		OutputsContext _localctx = new OutputsContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_outputs);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			System.out.println("    outputs: ");
			setState(815); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					System.out.println("      "+_localctx.i++ +": ");
					setState(812);
					output();
					setState(813);
					match(OUTPUT_SEP);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(817); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutputContext extends ParserRuleContext {
		public AmountContext amount;
		public BaseContext base;
		public AmountContext amount() {
			return getRuleContext(AmountContext.class,0);
		}
		public BaseContext base() {
			return getRuleContext(BaseContext.class,0);
		}
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public OutputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_output; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitOutput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitOutput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OutputContext output() throws RecognitionException {
		OutputContext _localctx = new OutputContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_output);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(819);
			((OutputContext)_localctx).amount = amount();
			System.out.println("        amount: "+(((OutputContext)_localctx).amount!=null?_input.getText(((OutputContext)_localctx).amount.start,((OutputContext)_localctx).amount.stop):null)+"");
			setState(821);
			((OutputContext)_localctx).base = base();
			System.out.println("        base: "+(((OutputContext)_localctx).base!=null?_input.getText(((OutputContext)_localctx).base.start,((OutputContext)_localctx).base.stop):null)+"");
			System.out.println("        condition: ");
			setState(824);
			cond();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CondContext extends ParserRuleContext {
		public SigContext sig;
		public XhxContext xhx;
		public CsvContext csv;
		public CltvContext cltv;
		public SigContext sig() {
			return getRuleContext(SigContext.class,0);
		}
		public XhxContext xhx() {
			return getRuleContext(XhxContext.class,0);
		}
		public CsvContext csv() {
			return getRuleContext(CsvContext.class,0);
		}
		public CltvContext cltv() {
			return getRuleContext(CltvContext.class,0);
		}
		public OrContext or() {
			return getRuleContext(OrContext.class,0);
		}
		public AndContext and() {
			return getRuleContext(AndContext.class,0);
		}
		public CondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		CondContext _localctx = new CondContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_cond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(842);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(826);
				((CondContext)_localctx).sig = sig();
				System.out.println(indent+"sig: "+(((CondContext)_localctx).sig!=null?_input.getText(((CondContext)_localctx).sig.start,((CondContext)_localctx).sig.stop):null)+"");
				}
				break;
			case 2:
				{
				setState(829);
				((CondContext)_localctx).xhx = xhx();
				System.out.println(indent+"xhx: "+(((CondContext)_localctx).xhx!=null?_input.getText(((CondContext)_localctx).xhx.start,((CondContext)_localctx).xhx.stop):null)+"");
				}
				break;
			case 3:
				{
				setState(832);
				((CondContext)_localctx).csv = csv();
				System.out.println(indent+"csv: "+(((CondContext)_localctx).csv!=null?_input.getText(((CondContext)_localctx).csv.start,((CondContext)_localctx).csv.stop):null)+"");
				}
				break;
			case 4:
				{
				setState(835);
				((CondContext)_localctx).cltv = cltv();
				System.out.println(indent+"cltv: "+(((CondContext)_localctx).cltv!=null?_input.getText(((CondContext)_localctx).cltv.start,((CondContext)_localctx).cltv.stop):null)+"");
				}
				break;
			case 5:
				{
				System.out.println(indent+"or: ");
				setState(839);
				or();
				}
				break;
			case 6:
				{
				System.out.println(indent+"and: ");
				setState(841);
				and();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AndContext extends ParserRuleContext {
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode AND() { return getToken(JuniterParser.AND, 0); }
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_and);
		try {
			enterOuterAlt(_localctx, 1);
			{
			indent+="  ";
			setState(845);
			match(OUTLP);
			setState(846);
			cond();
			setState(847);
			match(AND);
			setState(848);
			cond();
			setState(849);
			match(OUTRP);
			indent = indent.substring(2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OrContext extends ParserRuleContext {
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public TerminalNode OR() { return getToken(JuniterParser.OR, 0); }
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public OrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrContext or() throws RecognitionException {
		OrContext _localctx = new OrContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_or);
		try {
			enterOuterAlt(_localctx, 1);
			{
			indent+="  ";
			setState(853);
			match(OUTLP);
			setState(854);
			cond();
			setState(855);
			match(OR);
			setState(856);
			cond();
			setState(857);
			match(OUTRP);
			indent = indent.substring(2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigContext extends ParserRuleContext {
		public TerminalNode SIG() { return getToken(JuniterParser.SIG, 0); }
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public SigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSig(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigContext sig() throws RecognitionException {
		SigContext _localctx = new SigContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_sig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(860);
			match(SIG);
			setState(861);
			match(OUTLP);
			setState(862);
			pubkey();
			setState(863);
			match(OUTRP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XhxContext extends ParserRuleContext {
		public TerminalNode XHX() { return getToken(JuniterParser.XHX, 0); }
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public BhashContext bhash() {
			return getRuleContext(BhashContext.class,0);
		}
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public XhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterXhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitXhx(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitXhx(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XhxContext xhx() throws RecognitionException {
		XhxContext _localctx = new XhxContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_xhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(865);
			match(XHX);
			setState(866);
			match(OUTLP);
			setState(867);
			bhash();
			setState(868);
			match(OUTRP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CsvContext extends ParserRuleContext {
		public TerminalNode CSV() { return getToken(JuniterParser.CSV, 0); }
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public OutParamContext outParam() {
			return getRuleContext(OutParamContext.class,0);
		}
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public CsvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_csv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCsv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCsv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCsv(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CsvContext csv() throws RecognitionException {
		CsvContext _localctx = new CsvContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_csv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(870);
			match(CSV);
			setState(871);
			match(OUTLP);
			setState(872);
			outParam();
			setState(873);
			match(OUTRP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CltvContext extends ParserRuleContext {
		public TerminalNode CLTV() { return getToken(JuniterParser.CLTV, 0); }
		public TerminalNode OUTLP() { return getToken(JuniterParser.OUTLP, 0); }
		public OutParamContext outParam() {
			return getRuleContext(OutParamContext.class,0);
		}
		public TerminalNode OUTRP() { return getToken(JuniterParser.OUTRP, 0); }
		public CltvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cltv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCltv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCltv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCltv(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CltvContext cltv() throws RecognitionException {
		CltvContext _localctx = new CltvContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_cltv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(875);
			match(CLTV);
			setState(876);
			match(OUTLP);
			setState(877);
			outParam();
			setState(878);
			match(OUTRP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OutParamContext extends ParserRuleContext {
		public TerminalNode OUTNUMB() { return getToken(JuniterParser.OUTNUMB, 0); }
		public OutParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterOutParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitOutParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitOutParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OutParamContext outParam() throws RecognitionException {
		OutParamContext _localctx = new OutParamContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_outParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(880);
			match(OUTNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public BuidContext bl;
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(882);
			((BlockContext)_localctx).bl = buid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IssuerContext extends ParserRuleContext {
		public Token iss;
		public TerminalNode PUBKEY_INLINED() { return getToken(JuniterParser.PUBKEY_INLINED, 0); }
		public TerminalNode PUBKEY_MULTILN() { return getToken(JuniterParser.PUBKEY_MULTILN, 0); }
		public IssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIssuer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIssuer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IssuerContext issuer() throws RecognitionException {
		IssuerContext _localctx = new IssuerContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_issuer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(884);
			((IssuerContext)_localctx).iss = _input.LT(1);
			_la = _input.LA(1);
			if ( !(_la==PUBKEY_INLINED || _la==PUBKEY_MULTILN) ) {
				((IssuerContext)_localctx).iss = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			 issuerPK = (((IssuerContext)_localctx).iss!=null?((IssuerContext)_localctx).iss.getText():null); 
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MemberContext extends ParserRuleContext {
		public Token mem;
		public TerminalNode MEMBER_TYPE() { return getToken(JuniterParser.MEMBER_TYPE, 0); }
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMember(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMember(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_member);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(887);
			((MemberContext)_localctx).mem = match(MEMBER_TYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CertTSContext extends ParserRuleContext {
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public CertTSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTS; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCertTS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCertTS(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCertTS(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CertTSContext certTS() throws RecognitionException {
		CertTSContext _localctx = new CertTSContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_certTS);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(889);
			buid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UserIDContext extends ParserRuleContext {
		public Token uid;
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public UserIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUserID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUserID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUserID(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UserIDContext userID() throws RecognitionException {
		UserIDContext _localctx = new UserIDContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_userID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(891);
			((UserIDContext)_localctx).uid = match(USERID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimestampContext extends ParserRuleContext {
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTimestamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimestampContext timestamp() throws RecognitionException {
		TimestampContext _localctx = new TimestampContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_timestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(893);
			buid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SignatureContext extends ParserRuleContext {
		public TerminalNode SIGN() { return getToken(JuniterParser.SIGN, 0); }
		public TerminalNode MULTISIGN() { return getToken(JuniterParser.MULTISIGN, 0); }
		public TerminalNode WOTSIGN() { return getToken(JuniterParser.WOTSIGN, 0); }
		public SignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSignature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SignatureContext signature() throws RecognitionException {
		SignatureContext _localctx = new SignatureContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_signature);
		try {
			setState(899);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SIGN:
				enterOuterAlt(_localctx, 1);
				{
				setState(895);
				match(SIGN);
				}
				break;
			case MULTISIGN:
				enterOuterAlt(_localctx, 2);
				{
				setState(896);
				match(MULTISIGN);
				}
				break;
			case WOTSIGN:
				enterOuterAlt(_localctx, 3);
				{
				setState(897);
				match(WOTSIGN);
				nbSign++;
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UseridContext extends ParserRuleContext {
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public TerminalNode WOTUID() { return getToken(JuniterParser.WOTUID, 0); }
		public UseridContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUserid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUserid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUserid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseridContext userid() throws RecognitionException {
		UseridContext _localctx = new UseridContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_userid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(901);
			_la = _input.LA(1);
			if ( !(_la==WOTUID || _la==USERID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PubkeyContext extends ParserRuleContext {
		public Token pk;
		public TerminalNode PUBKEY_INLINED() { return getToken(JuniterParser.PUBKEY_INLINED, 0); }
		public TerminalNode PUBKEY_MULTILN() { return getToken(JuniterParser.PUBKEY_MULTILN, 0); }
		public TerminalNode OUTPUBK() { return getToken(JuniterParser.OUTPUBK, 0); }
		public TerminalNode WOTPUBK() { return getToken(JuniterParser.WOTPUBK, 0); }
		public TerminalNode CPT_ISS() { return getToken(JuniterParser.CPT_ISS, 0); }
		public PubkeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pubkey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPubkey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPubkey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPubkey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PubkeyContext pubkey() throws RecognitionException {
		PubkeyContext _localctx = new PubkeyContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_pubkey);
		try {
			setState(908);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PUBKEY_INLINED:
				enterOuterAlt(_localctx, 1);
				{
				setState(903);
				((PubkeyContext)_localctx).pk = match(PUBKEY_INLINED);
				}
				break;
			case PUBKEY_MULTILN:
				enterOuterAlt(_localctx, 2);
				{
				setState(904);
				match(PUBKEY_MULTILN);
				}
				break;
			case OUTPUBK:
				enterOuterAlt(_localctx, 3);
				{
				setState(905);
				match(OUTPUBK);
				}
				break;
			case WOTPUBK:
				enterOuterAlt(_localctx, 4);
				{
				setState(906);
				match(WOTPUBK);
				}
				break;
			case CPT_ISS:
				enterOuterAlt(_localctx, 5);
				{
				setState(907);
				match(CPT_ISS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CurrencyContext extends ParserRuleContext {
		public TerminalNode CURRENCY() { return getToken(JuniterParser.CURRENCY, 0); }
		public CurrencyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_currency; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCurrency(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCurrency(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCurrency(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CurrencyContext currency() throws RecognitionException {
		CurrencyContext _localctx = new CurrencyContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_currency);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(910);
			match(CURRENCY);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VersionContext extends ParserRuleContext {
		public TerminalNode VERSION() { return getToken(JuniterParser.VERSION, 0); }
		public TerminalNode WOTNUMB() { return getToken(JuniterParser.WOTNUMB, 0); }
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public VersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterVersion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitVersion(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitVersion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VersionContext version() throws RecognitionException {
		VersionContext _localctx = new VersionContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_version);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(912);
			_la = _input.LA(1);
			if ( !(((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & ((1L << (WOTNUMB - 56)) | (1L << (VERSION - 56)) | (1L << (BLNUMB - 56)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BuidContext extends ParserRuleContext {
		public BnumContext bnum;
		public Token d;
		public BhashContext bhash;
		public BnumContext bnum() {
			return getRuleContext(BnumContext.class,0);
		}
		public BhashContext bhash() {
			return getRuleContext(BhashContext.class,0);
		}
		public TerminalNode DASH_INLINED() { return getToken(JuniterParser.DASH_INLINED, 0); }
		public BuidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBuid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBuid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBuid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BuidContext buid() throws RecognitionException {
		BuidContext _localctx = new BuidContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_buid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(914);
			((BuidContext)_localctx).bnum = bnum();
			System.out.println(indent+"buid.bnum: "+(((BuidContext)_localctx).bnum!=null?_input.getText(((BuidContext)_localctx).bnum.start,((BuidContext)_localctx).bnum.stop):null));
			setState(916);
			((BuidContext)_localctx).d = match(DASH_INLINED);
			setState(917);
			((BuidContext)_localctx).bhash = bhash();
			System.out.println(indent+"buid.bhash: "+(((BuidContext)_localctx).bhash!=null?_input.getText(((BuidContext)_localctx).bhash.start,((BuidContext)_localctx).bhash.stop):null));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BnumContext extends ParserRuleContext {
		public Token a;
		public TerminalNode NUMBER() { return getToken(JuniterParser.NUMBER, 0); }
		public TerminalNode INNUMB() { return getToken(JuniterParser.INNUMB, 0); }
		public TerminalNode WOTNUMB() { return getToken(JuniterParser.WOTNUMB, 0); }
		public TerminalNode CPT_NUM() { return getToken(JuniterParser.CPT_NUM, 0); }
		public BnumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bnum; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBnum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBnum(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBnum(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BnumContext bnum() throws RecognitionException {
		BnumContext _localctx = new BnumContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_bnum);
		try {
			setState(924);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(920);
				((BnumContext)_localctx).a = match(NUMBER);
				}
				break;
			case INNUMB:
				enterOuterAlt(_localctx, 2);
				{
				setState(921);
				match(INNUMB);
				}
				break;
			case WOTNUMB:
				enterOuterAlt(_localctx, 3);
				{
				setState(922);
				match(WOTNUMB);
				}
				break;
			case CPT_NUM:
				enterOuterAlt(_localctx, 4);
				{
				setState(923);
				match(CPT_NUM);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BhashContext extends ParserRuleContext {
		public TerminalNode HASH_INLINED() { return getToken(JuniterParser.HASH_INLINED, 0); }
		public TerminalNode INHASH() { return getToken(JuniterParser.INHASH, 0); }
		public TerminalNode OUTHASH() { return getToken(JuniterParser.OUTHASH, 0); }
		public BhashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bhash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBhash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBhash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBhash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BhashContext bhash() throws RecognitionException {
		BhashContext _localctx = new BhashContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_bhash);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(926);
			_la = _input.LA(1);
			if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & ((1L << (HASH_INLINED - 64)) | (1L << (OUTHASH - 64)) | (1L << (INHASH - 64)))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PercentRotContext extends ParserRuleContext {
		public TerminalNode BLPERCENT() { return getToken(JuniterParser.BLPERCENT, 0); }
		public PercentRotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_percentRot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPercentRot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPercentRot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPercentRot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PercentRotContext percentRot() throws RecognitionException {
		PercentRotContext _localctx = new PercentRotContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_percentRot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(928);
			match(BLPERCENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DtDiffEvalContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public DtDiffEvalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dtDiffEval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDtDiffEval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDtDiffEval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDtDiffEval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DtDiffEvalContext dtDiffEval() throws RecognitionException {
		DtDiffEvalContext _localctx = new DtDiffEvalContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_dtDiffEval);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(930);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AvgGenTimeContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public AvgGenTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_avgGenTime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterAvgGenTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitAvgGenTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitAvgGenTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AvgGenTimeContext avgGenTime() throws RecognitionException {
		AvgGenTimeContext _localctx = new AvgGenTimeContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_avgGenTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(932);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MedianTimeBlocksContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public MedianTimeBlocksContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_medianTimeBlocks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMedianTimeBlocks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMedianTimeBlocks(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMedianTimeBlocks(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MedianTimeBlocksContext medianTimeBlocks() throws RecognitionException {
		MedianTimeBlocksContext _localctx = new MedianTimeBlocksContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_medianTimeBlocks);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(934);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StepMaxContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public StepMaxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stepMax; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterStepMax(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitStepMax(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitStepMax(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StepMaxContext stepMax() throws RecognitionException {
		StepMaxContext _localctx = new StepMaxContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_stepMax);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(936);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MsValidityContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public MsValidityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_msValidity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMsValidity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMsValidity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMsValidity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MsValidityContext msValidity() throws RecognitionException {
		MsValidityContext _localctx = new MsValidityContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_msValidity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(938);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class XpercentContext extends ParserRuleContext {
		public TerminalNode BLPERCENT() { return getToken(JuniterParser.BLPERCENT, 0); }
		public XpercentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xpercent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterXpercent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitXpercent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitXpercent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final XpercentContext xpercent() throws RecognitionException {
		XpercentContext _localctx = new XpercentContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_xpercent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(940);
			match(BLPERCENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MsWindowContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public MsWindowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_msWindow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMsWindow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMsWindow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMsWindow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MsWindowContext msWindow() throws RecognitionException {
		MsWindowContext _localctx = new MsWindowContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_msWindow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(942);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdtyWindowContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public IdtyWindowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyWindow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdtyWindow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdtyWindow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdtyWindow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdtyWindowContext idtyWindow() throws RecognitionException {
		IdtyWindowContext _localctx = new IdtyWindowContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_idtyWindow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(944);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigQtyContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public SigQtyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sigQty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSigQty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSigQty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSigQty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigQtyContext sigQty() throws RecognitionException {
		SigQtyContext _localctx = new SigQtyContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_sigQty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(946);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigValidityContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public SigValidityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sigValidity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSigValidity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSigValidity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSigValidity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigValidityContext sigValidity() throws RecognitionException {
		SigValidityContext _localctx = new SigValidityContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_sigValidity);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigWindowContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public SigWindowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sigWindow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSigWindow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSigWindow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSigWindow(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigWindowContext sigWindow() throws RecognitionException {
		SigWindowContext _localctx = new SigWindowContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_sigWindow);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(950);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigStockContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public SigStockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sigStock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSigStock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSigStock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSigStock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigStockContext sigStock() throws RecognitionException {
		SigStockContext _localctx = new SigStockContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_sigStock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(952);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SigPeriodContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public SigPeriodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sigPeriod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterSigPeriod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitSigPeriod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitSigPeriod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SigPeriodContext sigPeriod() throws RecognitionException {
		SigPeriodContext _localctx = new SigPeriodContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_sigPeriod);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(954);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UdReevalTime0Context extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public UdReevalTime0Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_udReevalTime0; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUdReevalTime0(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUdReevalTime0(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUdReevalTime0(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UdReevalTime0Context udReevalTime0() throws RecognitionException {
		UdReevalTime0Context _localctx = new UdReevalTime0Context(_ctx, getState());
		enterRule(_localctx, 182, RULE_udReevalTime0);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(956);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UdTime0Context extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public UdTime0Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_udTime0; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUdTime0(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUdTime0(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUdTime0(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UdTime0Context udTime0() throws RecognitionException {
		UdTime0Context _localctx = new UdTime0Context(_ctx, getState());
		enterRule(_localctx, 184, RULE_udTime0);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(958);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DtReevalContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public DtReevalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dtReeval; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDtReeval(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDtReeval(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDtReeval(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DtReevalContext dtReeval() throws RecognitionException {
		DtReevalContext _localctx = new DtReevalContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_dtReeval);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(960);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DtContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public DtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DtContext dt() throws RecognitionException {
		DtContext _localctx = new DtContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_dt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(962);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CContext extends ParserRuleContext {
		public TerminalNode BLPERCENT() { return getToken(JuniterParser.BLPERCENT, 0); }
		public CContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_c; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterC(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitC(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitC(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CContext c() throws RecognitionException {
		CContext _localctx = new CContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_c);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(964);
			match(BLPERCENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ud0Context extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public Ud0Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ud0; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUd0(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUd0(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUd0(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ud0Context ud0() throws RecognitionException {
		Ud0Context _localctx = new Ud0Context(_ctx, getState());
		enterRule(_localctx, 192, RULE_ud0);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PreviousIssuerContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public PreviousIssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_previousIssuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPreviousIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPreviousIssuer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPreviousIssuer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PreviousIssuerContext previousIssuer() throws RecognitionException {
		PreviousIssuerContext _localctx = new PreviousIssuerContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_previousIssuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(968);
			pubkey();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PreviousHashContext extends ParserRuleContext {
		public BhashContext bhash() {
			return getRuleContext(BhashContext.class,0);
		}
		public PreviousHashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_previousHash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPreviousHash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPreviousHash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPreviousHash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PreviousHashContext previousHash() throws RecognitionException {
		PreviousHashContext _localctx = new PreviousHashContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_previousHash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			bhash();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DifferentIssuersCountContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public DifferentIssuersCountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_differentIssuersCount; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDifferentIssuersCount(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDifferentIssuersCount(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDifferentIssuersCount(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DifferentIssuersCountContext differentIssuersCount() throws RecognitionException {
		DifferentIssuersCountContext _localctx = new DifferentIssuersCountContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_differentIssuersCount);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(972);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IssuersFrameVarContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public IssuersFrameVarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuersFrameVar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIssuersFrameVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIssuersFrameVar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIssuersFrameVar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IssuersFrameVarContext issuersFrameVar() throws RecognitionException {
		IssuersFrameVarContext _localctx = new IssuersFrameVarContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_issuersFrameVar);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(974);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IssuersFrameContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public IssuersFrameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuersFrame; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIssuersFrame(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIssuersFrame(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIssuersFrame(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IssuersFrameContext issuersFrame() throws RecognitionException {
		IssuersFrameContext _localctx = new IssuersFrameContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_issuersFrame);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(976);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnitBaseContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public UnitBaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unitBase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUnitBase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUnitBase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUnitBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitBaseContext unitBase() throws RecognitionException {
		UnitBaseContext _localctx = new UnitBaseContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_unitBase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(978);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UniversalDividendContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public UniversalDividendContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_universalDividend; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterUniversalDividend(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitUniversalDividend(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitUniversalDividend(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UniversalDividendContext universalDividend() throws RecognitionException {
		UniversalDividendContext _localctx = new UniversalDividendContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_universalDividend);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(980);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MedianTimeContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public MedianTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_medianTime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMedianTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMedianTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMedianTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MedianTimeContext medianTime() throws RecognitionException {
		MedianTimeContext _localctx = new MedianTimeContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_medianTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(982);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TimeContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public TimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_time; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TimeContext time() throws RecognitionException {
		TimeContext _localctx = new TimeContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_time);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(984);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PowMinContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public PowMinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_powMin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPowMin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPowMin(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPowMin(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PowMinContext powMin() throws RecognitionException {
		PowMinContext _localctx = new PowMinContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_powMin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(986);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(988);
			match(NUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NonceContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public TerminalNode NUMB() { return getToken(JuniterParser.NUMB, 0); }
		public NonceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonce; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNonce(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNonce(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNonce(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonceContext nonce() throws RecognitionException {
		NonceContext _localctx = new NonceContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_nonce);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(990);
			_la = _input.LA(1);
			if ( !(_la==NUMB || _la==BLNUMB) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InnerHashContext extends ParserRuleContext {
		public BhashContext bhash() {
			return getRuleContext(BhashContext.class,0);
		}
		public InnerHashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_innerHash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterInnerHash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitInnerHash(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitInnerHash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InnerHashContext innerHash() throws RecognitionException {
		InnerHashContext _localctx = new InnerHashContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_innerHash);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(992);
			bhash();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CertTimestampContext extends ParserRuleContext {
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public CertTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterCertTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitCertTimestamp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitCertTimestamp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CertTimestampContext certTimestamp() throws RecognitionException {
		CertTimestampContext _localctx = new CertTimestampContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_certTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(994);
			buid();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdtyIssuerContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public IdtyIssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyIssuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdtyIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdtyIssuer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdtyIssuer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdtyIssuerContext idtyIssuer() throws RecognitionException {
		IdtyIssuerContext _localctx = new IdtyIssuerContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_idtyIssuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(996);
			pubkey();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PortContext extends ParserRuleContext {
		public TerminalNode PORT() { return getToken(JuniterParser.PORT, 0); }
		public PortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_port; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterPort(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitPort(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitPort(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PortContext port() throws RecognitionException {
		PortContext _localctx = new PortContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_port);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(998);
			match(PORT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ip6Context extends ParserRuleContext {
		public TerminalNode IP6() { return getToken(JuniterParser.IP6, 0); }
		public Ip6Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ip6; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIp6(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIp6(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIp6(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ip6Context ip6() throws RecognitionException {
		Ip6Context _localctx = new Ip6Context(_ctx, getState());
		enterRule(_localctx, 226, RULE_ip6);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1000);
			match(IP6);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ip4Context extends ParserRuleContext {
		public TerminalNode IP4() { return getToken(JuniterParser.IP4, 0); }
		public Ip4Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ip4; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIp4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIp4(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIp4(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ip4Context ip4() throws RecognitionException {
		Ip4Context _localctx = new Ip4Context(_ctx, getState());
		enterRule(_localctx, 228, RULE_ip4);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1002);
			match(IP4);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DnsContext extends ParserRuleContext {
		public TerminalNode DNS() { return getToken(JuniterParser.DNS, 0); }
		public DnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterDns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitDns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitDns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DnsContext dns() throws RecognitionException {
		DnsContext _localctx = new DnsContext(_ctx, getState());
		enterRule(_localctx, 230, RULE_dns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1004);
			match(DNS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IBlockUidContext extends ParserRuleContext {
		public TerminalNode WOTBUID() { return getToken(JuniterParser.WOTBUID, 0); }
		public IBlockUidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iBlockUid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIBlockUid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIBlockUid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIBlockUid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IBlockUidContext iBlockUid() throws RecognitionException {
		IBlockUidContext _localctx = new IBlockUidContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_iBlockUid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1006);
			match(WOTBUID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MBlockUidContext extends ParserRuleContext {
		public TerminalNode WOTBUID() { return getToken(JuniterParser.WOTBUID, 0); }
		public MBlockUidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mBlockUid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterMBlockUid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitMBlockUid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitMBlockUid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MBlockUidContext mBlockUid() throws RecognitionException {
		MBlockUidContext _localctx = new MBlockUidContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_mBlockUid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1008);
			match(WOTBUID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EndpointTypeContext extends ParserRuleContext {
		public TerminalNode ENDPOINT_TYPE() { return getToken(JuniterParser.ENDPOINT_TYPE, 0); }
		public EndpointTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpointType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterEndpointType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitEndpointType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitEndpointType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EndpointTypeContext endpointType() throws RecognitionException {
		EndpointTypeContext _localctx = new EndpointTypeContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_endpointType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1010);
			match(ENDPOINT_TYPE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ToPKContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public ToPKContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_toPK; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterToPK(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitToPK(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitToPK(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ToPKContext toPK() throws RecognitionException {
		ToPKContext _localctx = new ToPKContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_toPK);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1012);
			pubkey();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockstampTimeContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public BlockstampTimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockstampTime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterBlockstampTime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitBlockstampTime(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitBlockstampTime(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockstampTimeContext blockstampTime() throws RecognitionException {
		BlockstampTimeContext _localctx = new BlockstampTimeContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_blockstampTime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1014);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NbOutputContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public NbOutputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nbOutput; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNbOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNbOutput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNbOutput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NbOutputContext nbOutput() throws RecognitionException {
		NbOutputContext _localctx = new NbOutputContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_nbOutput);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1016);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NbUnlockContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public NbUnlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nbUnlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNbUnlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNbUnlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNbUnlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NbUnlockContext nbUnlock() throws RecognitionException {
		NbUnlockContext _localctx = new NbUnlockContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_nbUnlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1018);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NbInputContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public NbInputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nbInput; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNbInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNbInput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNbInput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NbInputContext nbInput() throws RecognitionException {
		NbInputContext _localctx = new NbInputContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_nbInput);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1020);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NbIssuerContext extends ParserRuleContext {
		public TerminalNode BLNUMB() { return getToken(JuniterParser.BLNUMB, 0); }
		public NbIssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nbIssuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterNbIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitNbIssuer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitNbIssuer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NbIssuerContext nbIssuer() throws RecognitionException {
		NbIssuerContext _localctx = new NbIssuerContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_nbIssuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1022);
			match(BLNUMB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FromPKContext extends ParserRuleContext {
		public PubkeyContext pubkey() {
			return getRuleContext(PubkeyContext.class,0);
		}
		public FromPKContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fromPK; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterFromPK(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitFromPK(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitFromPK(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FromPKContext fromPK() throws RecognitionException {
		FromPKContext _localctx = new FromPKContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_fromPK);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1024);
			pubkey();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdtySignatureContext extends ParserRuleContext {
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public IdtySignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtySignature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).enterIdtySignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterParserListener ) ((JuniterParserListener)listener).exitIdtySignature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JuniterParserVisitor ) return ((JuniterParserVisitor<? extends T>)visitor).visitIdtySignature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdtySignatureContext idtySignature() throws RecognitionException {
		IdtySignatureContext _localctx = new IdtySignatureContext(_ctx, getState());
		enterRule(_localctx, 252, RULE_idtySignature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1026);
			signature();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 4:
			return compactTransaction_sempred((CompactTransactionContext)_localctx, predIndex);
		case 13:
			return modeSwitch_sempred((ModeSwitchContext)_localctx, predIndex);
		case 32:
			return endpoints_sempred((EndpointsContext)_localctx, predIndex);
		case 37:
			return signatures_sempred((SignaturesContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean compactTransaction_sempred(CompactTransactionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return _localctx.nbOut++ < 2;
		}
		return true;
	}
	private boolean modeSwitch_sempred(ModeSwitchContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return nbIssu == nbIssuersExpected;
		}
		return true;
	}
	private boolean endpoints_sempred(EndpointsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return _localctx.i<maxEndpoints;
		}
		return true;
	}
	private boolean signatures_sempred(SignaturesContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return  _localctx.i < nbIssu ;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0098\u0407\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
		"`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"+
		"k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"+
		"w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"+
		"\3\2\3\2\3\2\3\2\3\2\5\2\u0106\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0119\n\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u012b\n\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\5\3\u0145\n\3\3\4\3\4\3\4\3\4\7\4\u014b\n\4\f\4\16\4\u014e"+
		"\13\4\3\5\3\5\3\5\7\5\u0153\n\5\f\5\16\5\u0156\13\5\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\6\6\u016a\n\6\r"+
		"\6\16\6\u016b\3\6\6\6\u016f\n\6\r\6\16\6\u0170\3\6\6\6\u0174\n\6\r\6\16"+
		"\6\u0175\3\6\3\6\6\6\u017a\n\6\r\6\16\6\u017b\3\6\5\6\u017f\n\6\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u019f\n\13"+
		"\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\6\r\u01ad\n\r\r\r\16"+
		"\r\u01ae\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\3\20\7\20\u01c2\n\20\f\20\16\20\u01c5\13\20\3\21\3"+
		"\21\3\21\3\21\7\21\u01cb\n\21\f\21\16\21\u01ce\13\21\3\22\3\22\3\22\3"+
		"\22\7\22\u01d4\n\22\f\22\16\22\u01d7\13\22\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\7\23\u01e0\n\23\f\23\16\23\u01e3\13\23\3\24\3\24\3\24\3\24\3"+
		"\24\3\24\3\24\7\24\u01ec\n\24\f\24\16\24\u01ef\13\24\3\25\3\25\3\25\3"+
		"\25\6\25\u01f5\n\25\r\25\16\25\u01f6\3\26\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\26\3\26\3\27\3\27\7\27\u0205\n\27\f\27\16\27\u0208\13\27\3\30"+
		"\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35"+
		"\3\35\3\35\3\35\5\35\u0262\n\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36"+
		"\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3"+
		"!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\6\"\u02ae\n\"\r\"\16"+
		"\"\u02af\3\"\5\"\u02b3\n\"\3#\3#\3#\3#\3#\3#\5#\u02bb\n#\3#\3#\3#\3#\3"+
		"#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\6#\u02cc\n#\r#\16#\u02cd\3#\3#\3$\3$\3"+
		"%\3%\3%\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\6\'\u02df\n\'\r\'\16\'\u02e0\3("+
		"\3(\3)\3)\3*\3*\3+\3+\3+\3+\3+\6+\u02ee\n+\r+\16+\u02ef\3,\3,\3,\3,\3"+
		",\3,\3,\3,\3,\5,\u02fb\n,\3-\3-\3-\3-\3-\3-\3-\3.\3.\3.\3.\3.\3.\3.\3"+
		"/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\62\3\62\3\62\6\62\u0316\n\62\r\62"+
		"\16\62\u0317\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\5"+
		"\63\u0325\n\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\3\67\3\67\3\67"+
		"\6\67\u0332\n\67\r\67\16\67\u0333\38\38\38\38\38\38\38\39\39\39\39\39"+
		"\39\39\39\39\39\39\39\39\39\39\39\59\u034d\n9\3:\3:\3:\3:\3:\3:\3:\3:"+
		"\3;\3;\3;\3;\3;\3;\3;\3;\3<\3<\3<\3<\3<\3=\3=\3=\3=\3=\3>\3>\3>\3>\3>"+
		"\3?\3?\3?\3?\3?\3@\3@\3A\3A\3B\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F\3G\3G\3G"+
		"\3G\5G\u0386\nG\3H\3H\3I\3I\3I\3I\3I\5I\u038f\nI\3J\3J\3K\3K\3L\3L\3L"+
		"\3L\3L\3L\3M\3M\3M\3M\5M\u039f\nM\3N\3N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S"+
		"\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3[\3\\\3\\\3]\3]\3^\3^\3"+
		"_\3_\3`\3`\3a\3a\3b\3b\3c\3c\3d\3d\3e\3e\3f\3f\3g\3g\3h\3h\3i\3i\3j\3"+
		"j\3k\3k\3l\3l\3m\3m\3n\3n\3o\3o\3p\3p\3q\3q\3r\3r\3s\3s\3t\3t\3u\3u\3"+
		"v\3v\3w\3w\3x\3x\3y\3y\3z\3z\3{\3{\3|\3|\3}\3}\3~\3~\3\177\3\177\3\u0080"+
		"\3\u0080\3\u0080\2\2\u0081\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$"+
		"&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c"+
		"\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4"+
		"\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc"+
		"\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4"+
		"\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc"+
		"\u00fe\2\f\4\2WWvv\4\2\177\177\u0087\u0087\4\2||\u0084\u0084\4\2}}\u0085"+
		"\u0085\4\2~~\u0085\u0085\5\2``zz\u0089\u0089\4\2QQ\u0081\u0081\4\2>>U"+
		"U\5\2::OOvv\5\2BBii{{\2\u03bb\2\u0105\3\2\2\2\4\u0107\3\2\2\2\6\u0146"+
		"\3\2\2\2\b\u014f\3\2\2\2\n\u0157\3\2\2\2\f\u0182\3\2\2\2\16\u0184\3\2"+
		"\2\2\20\u0188\3\2\2\2\22\u018b\3\2\2\2\24\u0193\3\2\2\2\26\u01a2\3\2\2"+
		"\2\30\u01a8\3\2\2\2\32\u01b0\3\2\2\2\34\u01b4\3\2\2\2\36\u01b6\3\2\2\2"+
		" \u01c6\3\2\2\2\"\u01cf\3\2\2\2$\u01d8\3\2\2\2&\u01e4\3\2\2\2(\u01f0\3"+
		"\2\2\2*\u01f8\3\2\2\2,\u0202\3\2\2\2.\u0209\3\2\2\2\60\u0212\3\2\2\2\62"+
		"\u0214\3\2\2\2\64\u023e\3\2\2\2\66\u024a\3\2\2\28\u025c\3\2\2\2:\u0266"+
		"\3\2\2\2<\u0273\3\2\2\2>\u0288\3\2\2\2@\u0299\3\2\2\2B\u02b2\3\2\2\2D"+
		"\u02b4\3\2\2\2F\u02d1\3\2\2\2H\u02d3\3\2\2\2J\u02d6\3\2\2\2L\u02d8\3\2"+
		"\2\2N\u02e2\3\2\2\2P\u02e4\3\2\2\2R\u02e6\3\2\2\2T\u02e8\3\2\2\2V\u02f1"+
		"\3\2\2\2X\u02fc\3\2\2\2Z\u0303\3\2\2\2\\\u030a\3\2\2\2^\u030c\3\2\2\2"+
		"`\u030e\3\2\2\2b\u0310\3\2\2\2d\u0319\3\2\2\2f\u0326\3\2\2\2h\u0328\3"+
		"\2\2\2j\u032a\3\2\2\2l\u032c\3\2\2\2n\u0335\3\2\2\2p\u034c\3\2\2\2r\u034e"+
		"\3\2\2\2t\u0356\3\2\2\2v\u035e\3\2\2\2x\u0363\3\2\2\2z\u0368\3\2\2\2|"+
		"\u036d\3\2\2\2~\u0372\3\2\2\2\u0080\u0374\3\2\2\2\u0082\u0376\3\2\2\2"+
		"\u0084\u0379\3\2\2\2\u0086\u037b\3\2\2\2\u0088\u037d\3\2\2\2\u008a\u037f"+
		"\3\2\2\2\u008c\u0385\3\2\2\2\u008e\u0387\3\2\2\2\u0090\u038e\3\2\2\2\u0092"+
		"\u0390\3\2\2\2\u0094\u0392\3\2\2\2\u0096\u0394\3\2\2\2\u0098\u039e\3\2"+
		"\2\2\u009a\u03a0\3\2\2\2\u009c\u03a2\3\2\2\2\u009e\u03a4\3\2\2\2\u00a0"+
		"\u03a6\3\2\2\2\u00a2\u03a8\3\2\2\2\u00a4\u03aa\3\2\2\2\u00a6\u03ac\3\2"+
		"\2\2\u00a8\u03ae\3\2\2\2\u00aa\u03b0\3\2\2\2\u00ac\u03b2\3\2\2\2\u00ae"+
		"\u03b4\3\2\2\2\u00b0\u03b6\3\2\2\2\u00b2\u03b8\3\2\2\2\u00b4\u03ba\3\2"+
		"\2\2\u00b6\u03bc\3\2\2\2\u00b8\u03be\3\2\2\2\u00ba\u03c0\3\2\2\2\u00bc"+
		"\u03c2\3\2\2\2\u00be\u03c4\3\2\2\2\u00c0\u03c6\3\2\2\2\u00c2\u03c8\3\2"+
		"\2\2\u00c4\u03ca\3\2\2\2\u00c6\u03cc\3\2\2\2\u00c8\u03ce\3\2\2\2\u00ca"+
		"\u03d0\3\2\2\2\u00cc\u03d2\3\2\2\2\u00ce\u03d4\3\2\2\2\u00d0\u03d6\3\2"+
		"\2\2\u00d2\u03d8\3\2\2\2\u00d4\u03da\3\2\2\2\u00d6\u03dc\3\2\2\2\u00d8"+
		"\u03de\3\2\2\2\u00da\u03e0\3\2\2\2\u00dc\u03e2\3\2\2\2\u00de\u03e4\3\2"+
		"\2\2\u00e0\u03e6\3\2\2\2\u00e2\u03e8\3\2\2\2\u00e4\u03ea\3\2\2\2\u00e6"+
		"\u03ec\3\2\2\2\u00e8\u03ee\3\2\2\2\u00ea\u03f0\3\2\2\2\u00ec\u03f2\3\2"+
		"\2\2\u00ee\u03f4\3\2\2\2\u00f0\u03f6\3\2\2\2\u00f2\u03f8\3\2\2\2\u00f4"+
		"\u03fa\3\2\2\2\u00f6\u03fc\3\2\2\2\u00f8\u03fe\3\2\2\2\u00fa\u0400\3\2"+
		"\2\2\u00fc\u0402\3\2\2\2\u00fe\u0404\3\2\2\2\u0100\u0101\b\2\1\2\u0101"+
		"\u0106\58\35\2\u0102\u0106\5\66\34\2\u0103\u0106\5\64\33\2\u0104\u0106"+
		"\5\4\3\2\u0105\u0100\3\2\2\2\u0105\u0102\3\2\2\2\u0105\u0103\3\2\2\2\u0105"+
		"\u0104\3\2\2\2\u0106\3\3\2\2\2\u0107\u0108\b\3\1\2\u0108\u0109\5\u0094"+
		"K\2\u0109\u010a\b\3\1\2\u010a\u010b\7J\2\2\u010b\u010c\5\u0092J\2\u010c"+
		"\u010d\b\3\1\2\u010d\u010e\5\u00d8m\2\u010e\u010f\b\3\1\2\u010f\u0110"+
		"\5\u00d6l\2\u0110\u0111\b\3\1\2\u0111\u0112\5\u00d4k\2\u0112\u0113\b\3"+
		"\1\2\u0113\u0114\5\u00d2j\2\u0114\u0118\b\3\1\2\u0115\u0116\5\u00d0i\2"+
		"\u0116\u0117\b\3\1\2\u0117\u0119\3\2\2\2\u0118\u0115\3\2\2\2\u0118\u0119"+
		"\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\5\u00ceh\2\u011b\u011c\b\3\1"+
		"\2\u011c\u011d\5\u0082B\2\u011d\u011e\b\3\1\2\u011e\u011f\5\u00ccg\2\u011f"+
		"\u0120\b\3\1\2\u0120\u0121\5\u00caf\2\u0121\u0122\b\3\1\2\u0122\u0123"+
		"\5\u00c8e\2\u0123\u012a\b\3\1\2\u0124\u0125\5\u00c6d\2\u0125\u0126\b\3"+
		"\1\2\u0126\u0127\5\u00c4c\2\u0127\u0128\b\3\1\2\u0128\u012b\3\2\2\2\u0129"+
		"\u012b\5\62\32\2\u012a\u0124\3\2\2\2\u012a\u0129\3\2\2\2\u012b\u012c\3"+
		"\2\2\2\u012c\u012d\5\60\31\2\u012d\u012e\b\3\1\2\u012e\u012f\7\63\2\2"+
		"\u012f\u0130\5,\27\2\u0130\u0131\7\62\2\2\u0131\u0132\5(\25\2\u0132\u0133"+
		"\7\61\2\2\u0133\u0134\5&\24\2\u0134\u0135\7\60\2\2\u0135\u0136\5$\23\2"+
		"\u0136\u0137\7/\2\2\u0137\u0138\5\"\22\2\u0138\u0139\7.\2\2\u0139\u013a"+
		"\5 \21\2\u013a\u013b\7-\2\2\u013b\u013c\5\36\20\2\u013c\u013d\5\6\4\2"+
		"\u013d\u013e\5\u00dco\2\u013e\u013f\b\3\1\2\u013f\u0140\5\u00dan\2\u0140"+
		"\u0144\b\3\1\2\u0141\u0142\5\u008cG\2\u0142\u0143\b\3\1\2\u0143\u0145"+
		"\3\2\2\2\u0144\u0141\3\2\2\2\u0144\u0145\3\2\2\2\u0145\5\3\2\2\2\u0146"+
		"\u014c\b\4\1\2\u0147\u0148\5\n\6\2\u0148\u0149\78\2\2\u0149\u014b\3\2"+
		"\2\2\u014a\u0147\3\2\2\2\u014b\u014e\3\2\2\2\u014c\u014a\3\2\2\2\u014c"+
		"\u014d\3\2\2\2\u014d\7\3\2\2\2\u014e\u014c\3\2\2\2\u014f\u0154\b\5\1\2"+
		"\u0150\u0151\b\5\1\2\u0151\u0153\5\n\6\2\u0152\u0150\3\2\2\2\u0153\u0156"+
		"\3\2\2\2\u0154\u0152\3\2\2\2\u0154\u0155\3\2\2\2\u0155\t\3\2\2\2\u0156"+
		"\u0154\3\2\2\2\u0157\u0158\7\64\2\2\u0158\u0159\5\u0094K\2\u0159\u015a"+
		"\b\6\1\2\u015a\u015b\5\u00fa~\2\u015b\u015c\b\6\1\2\u015c\u015d\5\u00f8"+
		"}\2\u015d\u015e\b\6\1\2\u015e\u015f\5\u00f6|\2\u015f\u0160\b\6\1\2\u0160"+
		"\u0161\5\u00f4{\2\u0161\u0162\b\6\1\2\u0162\u0163\5\f\7\2\u0163\u0164"+
		"\b\6\1\2\u0164\u0165\5\u00f2z\2\u0165\u0166\b\6\1\2\u0166\u0167\b\6\1"+
		"\2\u0167\u0169\5\u0096L\2\u0168\u016a\5\32\16\2\u0169\u0168\3\2\2\2\u016a"+
		"\u016b\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c\u016e\3\2"+
		"\2\2\u016d\u016f\5\24\13\2\u016e\u016d\3\2\2\2\u016f\u0170\3\2\2\2\u0170"+
		"\u016e\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0173\3\2\2\2\u0172\u0174\5\26"+
		"\f\2\u0173\u0172\3\2\2\2\u0174\u0175\3\2\2\2\u0175\u0173\3\2\2\2\u0175"+
		"\u0176\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0179\6\6\2\3\u0178\u017a\5\22"+
		"\n\2\u0179\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017b\u0179\3\2\2\2\u017b"+
		"\u017c\3\2\2\2\u017c\u017e\3\2\2\2\u017d\u017f\5\16\b\2\u017e\u017d\3"+
		"\2\2\2\u017e\u017f\3\2\2\2\u017f\u0180\3\2\2\2\u0180\u0181\5\20\t\2\u0181"+
		"\13\3\2\2\2\u0182\u0183\7v\2\2\u0183\r\3\2\2\2\u0184\u0185\b\b\1\2\u0185"+
		"\u0186\7\u008f\2\2\u0186\u0187\7\u0087\2\2\u0187\17\3\2\2\2\u0188\u0189"+
		"\7\u008e\2\2\u0189\u018a\b\t\1\2\u018a\21\3\2\2\2\u018b\u018c\7\u0089"+
		"\2\2\u018c\u018d\7\u0084\2\2\u018d\u018e\7\u0089\2\2\u018e\u018f\7\u0084"+
		"\2\2\u018f\u0190\7\u0088\2\2\u0190\u0191\7\u008d\2\2\u0191\u0192\7\u0087"+
		"\2\2\u0192\23\3\2\2\2\u0193\u0194\7\u0089\2\2\u0194\u0195\7\u0084\2\2"+
		"\u0195\u019e\7\u0089\2\2\u0196\u0197\7\u0085\2\2\u0197\u0198\7\u008d\2"+
		"\2\u0198\u0199\7\u0084\2\2\u0199\u019f\7\u0089\2\2\u019a\u019b\7\u0086"+
		"\2\2\u019b\u019c\7\u008a\2\2\u019c\u019d\7\u0084\2\2\u019d\u019f\7\u0089"+
		"\2\2\u019e\u0196\3\2\2\2\u019e\u019a\3\2\2\2\u019f\u01a0\3\2\2\2\u01a0"+
		"\u01a1\7\u0087\2\2\u01a1\25\3\2\2\2\u01a2\u01a3\7\u0089\2\2\u01a3\u01a4"+
		"\7\u0084\2\2\u01a4\u01a5\7\u0088\2\2\u01a5\u01a6\7\u0089\2\2\u01a6\u01a7"+
		"\7\u0087\2\2\u01a7\27\3\2\2\2\u01a8\u01ac\b\r\1\2\u01a9\u01aa\5\u0090"+
		"I\2\u01aa\u01ab\b\r\1\2\u01ab\u01ad\3\2\2\2\u01ac\u01a9\3\2\2\2\u01ad"+
		"\u01ae\3\2\2\2\u01ae\u01ac\3\2\2\2\u01ae\u01af\3\2\2\2\u01af\31\3\2\2"+
		"\2\u01b0\u01b1\b\16\1\2\u01b1\u01b2\7\u008d\2\2\u01b2\u01b3\7\u0087\2"+
		"\2\u01b3\33\3\2\2\2\u01b4\u01b5\6\17\3\2\u01b5\35\3\2\2\2\u01b6\u01c3"+
		"\b\20\1\2\u01b7\u01b8\b\20\1\2\u01b8\u01b9\5\u00fc\177\2\u01b9\u01ba\b"+
		"\20\1\2\u01ba\u01bb\5\u00f0y\2\u01bb\u01bc\b\20\1\2\u01bc\u01bd\5\u0098"+
		"M\2\u01bd\u01be\b\20\1\2\u01be\u01bf\5\u008cG\2\u01bf\u01c0\b\20\1\2\u01c0"+
		"\u01c2\3\2\2\2\u01c1\u01b7\3\2\2\2\u01c2\u01c5\3\2\2\2\u01c3\u01c1\3\2"+
		"\2\2\u01c3\u01c4\3\2\2\2\u01c4\37\3\2\2\2\u01c5\u01c3\3\2\2\2\u01c6\u01cc"+
		"\b\21\1\2\u01c7\u01c8\5\u0090I\2\u01c8\u01c9\b\21\1\2\u01c9\u01cb\3\2"+
		"\2\2\u01ca\u01c7\3\2\2\2\u01cb\u01ce\3\2\2\2\u01cc\u01ca\3\2\2\2\u01cc"+
		"\u01cd\3\2\2\2\u01cd!\3\2\2\2\u01ce\u01cc\3\2\2\2\u01cf\u01d5\b\22\1\2"+
		"\u01d0\u01d1\5\u0090I\2\u01d1\u01d2\5\u008cG\2\u01d2\u01d4\3\2\2\2\u01d3"+
		"\u01d0\3\2\2\2\u01d4\u01d7\3\2\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2"+
		"\2\2\u01d6#\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d8\u01e1\b\23\1\2\u01d9\u01da"+
		"\5\u0090I\2\u01da\u01db\5\u008cG\2\u01db\u01dc\5\u00ecw\2\u01dc\u01dd"+
		"\5\u00eav\2\u01dd\u01de\5\u008eH\2\u01de\u01e0\3\2\2\2\u01df\u01d9\3\2"+
		"\2\2\u01e0\u01e3\3\2\2\2\u01e1\u01df\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2"+
		"%\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e4\u01ed\b\24\1\2\u01e5\u01e6\5\u0090"+
		"I\2\u01e6\u01e7\5\u008cG\2\u01e7\u01e8\5\u00ecw\2\u01e8\u01e9\5\u00ea"+
		"v\2\u01e9\u01ea\5\u008eH\2\u01ea\u01ec\3\2\2\2\u01eb\u01e5\3\2\2\2\u01ec"+
		"\u01ef\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\'\3\2\2\2"+
		"\u01ef\u01ed\3\2\2\2\u01f0\u01f4\b\25\1\2\u01f1\u01f2\5*\26\2\u01f2\u01f3"+
		"\7?\2\2\u01f3\u01f5\3\2\2\2\u01f4\u01f1\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6"+
		"\u01f4\3\2\2\2\u01f6\u01f7\3\2\2\2\u01f7)\3\2\2\2\u01f8\u01f9\5\u0090"+
		"I\2\u01f9\u01fa\7=\2\2\u01fa\u01fb\5\u008cG\2\u01fb\u01fc\7=\2\2\u01fc"+
		"\u01fd\5\u00ecw\2\u01fd\u01fe\7=\2\2\u01fe\u01ff\5\u00eav\2\u01ff\u0200"+
		"\7=\2\2\u0200\u0201\5\u008eH\2\u0201+\3\2\2\2\u0202\u0206\b\27\1\2\u0203"+
		"\u0205\5.\30\2\u0204\u0203\3\2\2\2\u0205\u0208\3\2\2\2\u0206\u0204\3\2"+
		"\2\2\u0206\u0207\3\2\2\2\u0207-\3\2\2\2\u0208\u0206\3\2\2\2\u0209\u020a"+
		"\5\u0090I\2\u020a\u020b\7=\2\2\u020b\u020c\5\u008cG\2\u020c\u020d\7=\2"+
		"\2\u020d\u020e\5\u00eav\2\u020e\u020f\7=\2\2\u020f\u0210\5\u008eH\2\u0210"+
		"\u0211\7?\2\2\u0211/\3\2\2\2\u0212\u0213\7W\2\2\u0213\61\3\2\2\2\u0214"+
		"\u0215\b\32\1\2\u0215\u0216\5\u00c0a\2\u0216\u0217\b\32\1\2\u0217\u0218"+
		"\5\u00be`\2\u0218\u0219\b\32\1\2\u0219\u021a\5\u00c2b\2\u021a\u021b\b"+
		"\32\1\2\u021b\u021c\5\u00b6\\\2\u021c\u021d\b\32\1\2\u021d\u021e\5\u00b4"+
		"[\2\u021e\u021f\b\32\1\2\u021f\u0220\5\u00b2Z\2\u0220\u0221\b\32\1\2\u0221"+
		"\u0222\5\u00b0Y\2\u0222\u0223\b\32\1\2\u0223\u0224\5\u00aeX\2\u0224\u0225"+
		"\b\32\1\2\u0225\u0226\5\u00acW\2\u0226\u0227\b\32\1\2\u0227\u0228\5\u00aa"+
		"V\2\u0228\u0229\b\32\1\2\u0229\u022a\5\u00a8U\2\u022a\u022b\b\32\1\2\u022b"+
		"\u022c\5\u00a6T\2\u022c\u022d\b\32\1\2\u022d\u022e\5\u00a4S\2\u022e\u022f"+
		"\b\32\1\2\u022f\u0230\5\u00a2R\2\u0230\u0231\b\32\1\2\u0231\u0232\5\u00a0"+
		"Q\2\u0232\u0233\b\32\1\2\u0233\u0234\5\u009eP\2\u0234\u0235\b\32\1\2\u0235"+
		"\u0236\5\u009cO\2\u0236\u0237\b\32\1\2\u0237\u0238\5\u00ba^\2\u0238\u0239"+
		"\b\32\1\2\u0239\u023a\5\u00b8]\2\u023a\u023b\b\32\1\2\u023b\u023c\5\u00bc"+
		"_\2\u023c\u023d\b\32\1\2\u023d\63\3\2\2\2\u023e\u023f\b\33\1\2\u023f\u0240"+
		"\5\u0094K\2\u0240\u0241\b\33\1\2\u0241\u0242\7H\2\2\u0242\u0243\5\u0092"+
		"J\2\u0243\u0244\b\33\1\2\u0244\u0245\5\u0090I\2\u0245\u0246\b\33\1\2\u0246"+
		"\u0247\b\33\1\2\u0247\u0248\5\u0080A\2\u0248\u0249\5B\"\2\u0249\65\3\2"+
		"\2\2\u024a\u024b\b\34\1\2\u024b\u024c\5\u0094K\2\u024c\u024d\b\34\1\2"+
		"\u024d\u024e\7I\2\2\u024e\u024f\5\u0092J\2\u024f\u0250\b\34\1\2\u0250"+
		"\u0251\5R*\2\u0251\u0252\b\34\1\2\u0252\u0253\5P)\2\u0253\u0254\b\34\1"+
		"\2\u0254\u0255\5\30\r\2\u0255\u0256\5T+\2\u0256\u0257\5b\62\2\u0257\u0258"+
		"\5l\67\2\u0258\u0259\5L\'\2\u0259\u025a\5N(\2\u025a\u025b\b\34\1\2\u025b"+
		"\67\3\2\2\2\u025c\u0261\b\35\1\2\u025d\u0262\5:\36\2\u025e\u0262\5> \2"+
		"\u025f\u0262\5@!\2\u0260\u0262\5<\37\2\u0261\u025d\3\2\2\2\u0261\u025e"+
		"\3\2\2\2\u0261\u025f\3\2\2\2\u0261\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263"+
		"\u0264\5\u008cG\2\u0264\u0265\b\35\1\2\u02659\3\2\2\2\u0266\u0267\b\36"+
		"\1\2\u0267\u0268\5\u0094K\2\u0268\u0269\b\36\1\2\u0269\u026a\7D\2\2\u026a"+
		"\u026b\5\u0092J\2\u026b\u026c\b\36\1\2\u026c\u026d\5\u0082B\2\u026d\u026e"+
		"\b\36\1\2\u026e\u026f\5\u008eH\2\u026f\u0270\b\36\1\2\u0270\u0271\b\36"+
		"\1\2\u0271\u0272\5\u008aF\2\u0272;\3\2\2\2\u0273\u0274\b\37\1\2\u0274"+
		"\u0275\5\u0094K\2\u0275\u0276\b\37\1\2\u0276\u0277\7E\2\2\u0277\u0278"+
		"\5\u0092J\2\u0278\u0279\b\37\1\2\u0279\u027a\5\u0082B\2\u027a\u027b\b"+
		"\37\1\2\u027b\u027c\5\u00e0q\2\u027c\u027d\b\37\1\2\u027d\u027e\5J&\2"+
		"\u027e\u027f\b\37\1\2\u027f\u0280\b\37\1\2\u0280\u0281\5H%\2\u0281\u0282"+
		"\7\17\2\2\u0282\u0283\5\u00fe\u0080\2\u0283\u0284\b\37\1\2\u0284\u0285"+
		"\b\37\1\2\u0285\u0286\7M\2\2\u0286\u0287\5\u00dep\2\u0287=\3\2\2\2\u0288"+
		"\u0289\b \1\2\u0289\u028a\5\u0094K\2\u028a\u028b\b \1\2\u028b\u028c\7"+
		"F\2\2\u028c\u028d\5\u0092J\2\u028d\u028e\b \1\2\u028e\u028f\5\u0082B\2"+
		"\u028f\u0290\b \1\2\u0290\u0291\b \1\2\u0291\u0292\5\u0080A\2\u0292\u0293"+
		"\5\u0084C\2\u0293\u0294\b \1\2\u0294\u0295\5\u0088E\2\u0295\u0296\b \1"+
		"\2\u0296\u0297\b \1\2\u0297\u0298\5\u0086D\2\u0298?\3\2\2\2\u0299\u029a"+
		"\b!\1\2\u029a\u029b\5\u0094K\2\u029b\u029c\b!\1\2\u029c\u029d\7G\2\2\u029d"+
		"\u029e\5\u0092J\2\u029e\u029f\b!\1\2\u029f\u02a0\5\u0082B\2\u02a0\u02a1"+
		"\b!\1\2\u02a1\u02a2\5J&\2\u02a2\u02a3\b!\1\2\u02a3\u02a4\b!\1\2\u02a4"+
		"\u02a5\5H%\2\u02a5\u02a6\7\17\2\2\u02a6\u02a7\5\u00fe\u0080\2\u02a7\u02a8"+
		"\b!\1\2\u02a8A\3\2\2\2\u02a9\u02ad\b\"\1\2\u02aa\u02ab\6\"\4\3\u02ab\u02ac"+
		"\b\"\1\2\u02ac\u02ae\5D#\2\u02ad\u02aa\3\2\2\2\u02ae\u02af\3\2\2\2\u02af"+
		"\u02ad\3\2\2\2\u02af\u02b0\3\2\2\2\u02b0\u02b3\3\2\2\2\u02b1\u02b3\7\2"+
		"\2\3\u02b2\u02a9\3\2\2\2\u02b2\u02b1\3\2\2\2\u02b3C\3\2\2\2\u02b4\u02b5"+
		"\5\u00eex\2\u02b5\u02b6\7\u0095\2\2\u02b6\u02ba\b#\1\2\u02b7\u02b8\5F"+
		"$\2\u02b8\u02b9\7\u0095\2\2\u02b9\u02bb\3\2\2\2\u02ba\u02b7\3\2\2\2\u02ba"+
		"\u02bb\3\2\2\2\u02bb\u02cb\3\2\2\2\u02bc\u02bd\5\u00e8u\2\u02bd\u02be"+
		"\7\u0095\2\2\u02be\u02bf\b#\1\2\u02bf\u02cc\3\2\2\2\u02c0\u02c1\5\u00e6"+
		"t\2\u02c1\u02c2\7\u0095\2\2\u02c2\u02c3\b#\1\2\u02c3\u02cc\3\2\2\2\u02c4"+
		"\u02c5\5\u00e4s\2\u02c5\u02c6\7\u0095\2\2\u02c6\u02c7\b#\1\2\u02c7\u02cc"+
		"\3\2\2\2\u02c8\u02c9\5\u00e2r\2\u02c9\u02ca\b#\1\2\u02ca\u02cc\3\2\2\2"+
		"\u02cb\u02bc\3\2\2\2\u02cb\u02c0\3\2\2\2\u02cb\u02c4\3\2\2\2\u02cb\u02c8"+
		"\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd\u02cb\3\2\2\2\u02cd\u02ce\3\2\2\2\u02ce"+
		"\u02cf\3\2\2\2\u02cf\u02d0\7\u0098\2\2\u02d0E\3\2\2\2\u02d1\u02d2\7\u0093"+
		"\2\2\u02d2G\3\2\2\2\u02d3\u02d4\b%\1\2\u02d4\u02d5\5\u0096L\2\u02d5I\3"+
		"\2\2\2\u02d6\u02d7\7U\2\2\u02d7K\3\2\2\2\u02d8\u02de\b\'\1\2\u02d9\u02da"+
		"\6\'\5\3\u02da\u02db\5\u008cG\2\u02db\u02dc\b\'\1\2\u02dc\u02dd\7^\2\2"+
		"\u02dd\u02df\3\2\2\2\u02de\u02d9\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0\u02de"+
		"\3\2\2\2\u02e0\u02e1\3\2\2\2\u02e1M\3\2\2\2\u02e2\u02e3\7[\2\2\u02e3O"+
		"\3\2\2\2\u02e4\u02e5\t\2\2\2\u02e5Q\3\2\2\2\u02e6\u02e7\7W\2\2\u02e7S"+
		"\3\2\2\2\u02e8\u02ed\b+\1\2\u02e9\u02ea\b+\1\2\u02ea\u02eb\5V,\2\u02eb"+
		"\u02ec\t\3\2\2\u02ec\u02ee\3\2\2\2\u02ed\u02e9\3\2\2\2\u02ee\u02ef\3\2"+
		"\2\2\u02ef\u02ed\3\2\2\2\u02ef\u02f0\3\2\2\2\u02f0U\3\2\2\2\u02f1\u02f2"+
		"\5\\/\2\u02f2\u02f3\b,\1\2\u02f3\u02f4\t\4\2\2\u02f4\u02f5\5^\60\2\u02f5"+
		"\u02fa\b,\1\2\u02f6\u02f7\t\5\2\2\u02f7\u02fb\5X-\2\u02f8\u02f9\t\6\2"+
		"\2\u02f9\u02fb\5Z.\2\u02fa\u02f6\3\2\2\2\u02fa\u02f8\3\2\2\2\u02fbW\3"+
		"\2\2\2\u02fc\u02fd\b-\1\2\u02fd\u02fe\5\u0090I\2\u02fe\u02ff\b-\1\2\u02ff"+
		"\u0300\t\4\2\2\u0300\u0301\5\u0098M\2\u0301\u0302\b-\1\2\u0302Y\3\2\2"+
		"\2\u0303\u0304\b.\1\2\u0304\u0305\5\u009aN\2\u0305\u0306\b.\1\2\u0306"+
		"\u0307\t\4\2\2\u0307\u0308\5`\61\2\u0308\u0309\b.\1\2\u0309[\3\2\2\2\u030a"+
		"\u030b\t\7\2\2\u030b]\3\2\2\2\u030c\u030d\t\7\2\2\u030d_\3\2\2\2\u030e"+
		"\u030f\7z\2\2\u030fa\3\2\2\2\u0310\u0315\b\62\1\2\u0311\u0312\b\62\1\2"+
		"\u0312\u0313\5d\63\2\u0313\u0314\7n\2\2\u0314\u0316\3\2\2\2\u0315\u0311"+
		"\3\2\2\2\u0316\u0317\3\2\2\2\u0317\u0315\3\2\2\2\u0317\u0318\3\2\2\2\u0318"+
		"c\3\2\2\2\u0319\u031a\5f\64\2\u031a\u031b\b\63\1\2\u031b\u0324\b\63\1"+
		"\2\u031c\u031d\7o\2\2\u031d\u031e\5h\65\2\u031e\u031f\b\63\1\2\u031f\u0325"+
		"\3\2\2\2\u0320\u0321\7p\2\2\u0321\u0322\5j\66\2\u0322\u0323\b\63\1\2\u0323"+
		"\u0325\3\2\2\2\u0324\u031c\3\2\2\2\u0324\u0320\3\2\2\2\u0325e\3\2\2\2"+
		"\u0326\u0327\7s\2\2\u0327g\3\2\2\2\u0328\u0329\7s\2\2\u0329i\3\2\2\2\u032a"+
		"\u032b\7s\2\2\u032bk\3\2\2\2\u032c\u0331\b\67\1\2\u032d\u032e\b\67\1\2"+
		"\u032e\u032f\5n8\2\u032f\u0330\7k\2\2\u0330\u0332\3\2\2\2\u0331\u032d"+
		"\3\2\2\2\u0332\u0333\3\2\2\2\u0333\u0331\3\2\2\2\u0333\u0334\3\2\2\2\u0334"+
		"m\3\2\2\2\u0335\u0336\5\\/\2\u0336\u0337\b8\1\2\u0337\u0338\5^\60\2\u0338"+
		"\u0339\b8\1\2\u0339\u033a\b8\1\2\u033a\u033b\5p9\2\u033bo\3\2\2\2\u033c"+
		"\u033d\5v<\2\u033d\u033e\b9\1\2\u033e\u034d\3\2\2\2\u033f\u0340\5x=\2"+
		"\u0340\u0341\b9\1\2\u0341\u034d\3\2\2\2\u0342\u0343\5z>\2\u0343\u0344"+
		"\b9\1\2\u0344\u034d\3\2\2\2\u0345\u0346\5|?\2\u0346\u0347\b9\1\2\u0347"+
		"\u034d\3\2\2\2\u0348\u0349\b9\1\2\u0349\u034d\5t;\2\u034a\u034b\b9\1\2"+
		"\u034b\u034d\5r:\2\u034c\u033c\3\2\2\2\u034c\u033f\3\2\2\2\u034c\u0342"+
		"\3\2\2\2\u034c\u0345\3\2\2\2\u034c\u0348\3\2\2\2\u034c\u034a\3\2\2\2\u034d"+
		"q\3\2\2\2\u034e\u034f\b:\1\2\u034f\u0350\7g\2\2\u0350\u0351\5p9\2\u0351"+
		"\u0352\7f\2\2\u0352\u0353\5p9\2\u0353\u0354\7h\2\2\u0354\u0355\b:\1\2"+
		"\u0355s\3\2\2\2\u0356\u0357\b;\1\2\u0357\u0358\7g\2\2\u0358\u0359\5p9"+
		"\2\u0359\u035a\7e\2\2\u035a\u035b\5p9\2\u035b\u035c\7h\2\2\u035c\u035d"+
		"\b;\1\2\u035du\3\2\2\2\u035e\u035f\7a\2\2\u035f\u0360\7g\2\2\u0360\u0361"+
		"\5\u0090I\2\u0361\u0362\7h\2\2\u0362w\3\2\2\2\u0363\u0364\7b\2\2\u0364"+
		"\u0365\7g\2\2\u0365\u0366\5\u009aN\2\u0366\u0367\7h\2\2\u0367y\3\2\2\2"+
		"\u0368\u0369\7c\2\2\u0369\u036a\7g\2\2\u036a\u036b\5~@\2\u036b\u036c\7"+
		"h\2\2\u036c{\3\2\2\2\u036d\u036e\7d\2\2\u036e\u036f\7g\2\2\u036f\u0370"+
		"\5~@\2\u0370\u0371\7h\2\2\u0371}\3\2\2\2\u0372\u0373\7`\2\2\u0373\177"+
		"\3\2\2\2\u0374\u0375\5\u0096L\2\u0375\u0081\3\2\2\2\u0376\u0377\t\b\2"+
		"\2\u0377\u0378\bB\1\2\u0378\u0083\3\2\2\2\u0379\u037a\7Y\2\2\u037a\u0085"+
		"\3\2\2\2\u037b\u037c\5\u0096L\2\u037c\u0087\3\2\2\2\u037d\u037e\7U\2\2"+
		"\u037e\u0089\3\2\2\2\u037f\u0380\5\u0096L\2\u0380\u008b\3\2\2\2\u0381"+
		"\u0386\7L\2\2\u0382\u0386\7]\2\2\u0383\u0384\7<\2\2\u0384\u0386\bG\1\2"+
		"\u0385\u0381\3\2\2\2\u0385\u0382\3\2\2\2\u0385\u0383\3\2\2\2\u0386\u008d"+
		"\3\2\2\2\u0387\u0388\t\t\2\2\u0388\u008f\3\2\2\2\u0389\u038f\7Q\2\2\u038a"+
		"\u038f\7\u0081\2\2\u038b\u038f\7l\2\2\u038c\u038f\7;\2\2\u038d\u038f\7"+
		"\u008d\2\2\u038e\u0389\3\2\2\2\u038e\u038a\3\2\2\2\u038e\u038b\3\2\2\2"+
		"\u038e\u038c\3\2\2\2\u038e\u038d\3\2\2\2\u038f\u0091\3\2\2\2\u0390\u0391"+
		"\7S\2\2\u0391\u0093\3\2\2\2\u0392\u0393\t\n\2\2\u0393\u0095\3\2\2\2\u0394"+
		"\u0395\5\u0098M\2\u0395\u0396\bL\1\2\u0396\u0397\7A\2\2\u0397\u0398\5"+
		"\u009aN\2\u0398\u0399\bL\1\2\u0399\u0097\3\2\2\2\u039a\u039f\7@\2\2\u039b"+
		"\u039f\7z\2\2\u039c\u039f\7:\2\2\u039d\u039f\7\u0089\2\2\u039e\u039a\3"+
		"\2\2\2\u039e\u039b\3\2\2\2\u039e\u039c\3\2\2\2\u039e\u039d\3\2\2\2\u039f"+
		"\u0099\3\2\2\2\u03a0\u03a1\t\13\2\2\u03a1\u009b\3\2\2\2\u03a2\u03a3\7"+
		"w\2\2\u03a3\u009d\3\2\2\2\u03a4\u03a5\7v\2\2\u03a5\u009f\3\2\2\2\u03a6"+
		"\u03a7\7v\2\2\u03a7\u00a1\3\2\2\2\u03a8\u03a9\7v\2\2\u03a9\u00a3\3\2\2"+
		"\2\u03aa\u03ab\7v\2\2\u03ab\u00a5\3\2\2\2\u03ac\u03ad\7v\2\2\u03ad\u00a7"+
		"\3\2\2\2\u03ae\u03af\7w\2\2\u03af\u00a9\3\2\2\2\u03b0\u03b1\7v\2\2\u03b1"+
		"\u00ab\3\2\2\2\u03b2\u03b3\7v\2\2\u03b3\u00ad\3\2\2\2\u03b4\u03b5\7v\2"+
		"\2\u03b5\u00af\3\2\2\2\u03b6\u03b7\7v\2\2\u03b7\u00b1\3\2\2\2\u03b8\u03b9"+
		"\7v\2\2\u03b9\u00b3\3\2\2\2\u03ba\u03bb\7v\2\2\u03bb\u00b5\3\2\2\2\u03bc"+
		"\u03bd\7v\2\2\u03bd\u00b7\3\2\2\2\u03be\u03bf\7v\2\2\u03bf\u00b9\3\2\2"+
		"\2\u03c0\u03c1\7v\2\2\u03c1\u00bb\3\2\2\2\u03c2\u03c3\7v\2\2\u03c3\u00bd"+
		"\3\2\2\2\u03c4\u03c5\7v\2\2\u03c5\u00bf\3\2\2\2\u03c6\u03c7\7w\2\2\u03c7"+
		"\u00c1\3\2\2\2\u03c8\u03c9\7v\2\2\u03c9\u00c3\3\2\2\2\u03ca\u03cb\5\u0090"+
		"I\2\u03cb\u00c5\3\2\2\2\u03cc\u03cd\5\u009aN\2\u03cd\u00c7\3\2\2\2\u03ce"+
		"\u03cf\7W\2\2\u03cf\u00c9\3\2\2\2\u03d0\u03d1\7W\2\2\u03d1\u00cb\3\2\2"+
		"\2\u03d2\u03d3\7W\2\2\u03d3\u00cd\3\2\2\2\u03d4\u03d5\7W\2\2\u03d5\u00cf"+
		"\3\2\2\2\u03d6\u03d7\7W\2\2\u03d7\u00d1\3\2\2\2\u03d8\u03d9\7W\2\2\u03d9"+
		"\u00d3\3\2\2\2\u03da\u03db\7W\2\2\u03db\u00d5\3\2\2\2\u03dc\u03dd\7W\2"+
		"\2\u03dd\u00d7\3\2\2\2\u03de\u03df\7W\2\2\u03df\u00d9\3\2\2\2\u03e0\u03e1"+
		"\t\2\2\2\u03e1\u00db\3\2\2\2\u03e2\u03e3\5\u009aN\2\u03e3\u00dd\3\2\2"+
		"\2\u03e4\u03e5\5\u0096L\2\u03e5\u00df\3\2\2\2\u03e6\u03e7\5\u0090I\2\u03e7"+
		"\u00e1\3\2\2\2\u03e8\u03e9\7\u0096\2\2\u03e9\u00e3\3\2\2\2\u03ea\u03eb"+
		"\7\u0091\2\2\u03eb\u00e5\3\2\2\2\u03ec\u03ed\7\u0090\2\2\u03ed\u00e7\3"+
		"\2\2\2\u03ee\u03ef\7\u0094\2\2\u03ef\u00e9\3\2\2\2\u03f0\u03f1\79\2\2"+
		"\u03f1\u00eb\3\2\2\2\u03f2\u03f3\79\2\2\u03f3\u00ed\3\2\2\2\u03f4\u03f5"+
		"\7\u0097\2\2\u03f5\u00ef\3\2\2\2\u03f6\u03f7\5\u0090I\2\u03f7\u00f1\3"+
		"\2\2\2\u03f8\u03f9\7v\2\2\u03f9\u00f3\3\2\2\2\u03fa\u03fb\7v\2\2\u03fb"+
		"\u00f5\3\2\2\2\u03fc\u03fd\7v\2\2\u03fd\u00f7\3\2\2\2\u03fe\u03ff\7v\2"+
		"\2\u03ff\u00f9\3\2\2\2\u0400\u0401\7v\2\2\u0401\u00fb\3\2\2\2\u0402\u0403"+
		"\5\u0090I\2\u0403\u00fd\3\2\2\2\u0404\u0405\5\u008cG\2\u0405\u00ff\3\2"+
		"\2\2&\u0105\u0118\u012a\u0144\u014c\u0154\u016b\u0170\u0175\u017b\u017e"+
		"\u019e\u01ae\u01c3\u01cc\u01d5\u01e1\u01ed\u01f6\u0206\u0261\u02af\u02b2"+
		"\u02ba\u02cb\u02cd\u02e0\u02ef\u02fa\u0317\u0324\u0333\u034c\u0385\u038e"+
		"\u039e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}