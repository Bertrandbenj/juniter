// Generated from DUP.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DUPParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, Version=27, Curr=28, Blockstamp=29, Iss_=30, Signature_=31, 
		Timestamp_=32, Type_=33, Currency_=34, VersionHeader=35, UniqueIDHeader=36, 
		IdtyTimestampHeader=37, Issuer_=38, STR=39, INT=40, SIGNATURE=41, HASH=42, 
		PUBKEY=43, VERSION=44, CURRENCY=45, USERID=46, DOCTYPE=47, BUID=48, LP=49, 
		RP=50, WS=51, NL=52, COLON=53;
	public static final int
		RULE_document = 0, RULE_transaction = 1, RULE_revocation = 2, RULE_certification = 3, 
		RULE_membership = 4, RULE_peer = 5, RULE_identity = 6, RULE_doctype = 7, 
		RULE_version_ = 8, RULE_type_ = 9, RULE_currency_ = 10, RULE_issuer_ = 11, 
		RULE_timestamp_ = 12, RULE_uniqueID_ = 13, RULE_block_ = 14, RULE_certTimestamp = 15, 
		RULE_certTS = 16, RULE_comment = 17, RULE_endpoints = 18, RULE_idtyissuer = 19, 
		RULE_idtyUniqueID = 20, RULE_idtyTimestamp = 21, RULE_idtySignature = 22, 
		RULE_inputs = 23, RULE_issuers = 24, RULE_locktime = 25, RULE_member = 26, 
		RULE_number = 27, RULE_outputs = 28, RULE_poWMin = 29, RULE_publicKey = 30, 
		RULE_signatures = 31, RULE_userID = 32, RULE_unlocks = 33, RULE_buid = 34, 
		RULE_issuer = 35, RULE_signature = 36, RULE_input_ = 37, RULE_unlock = 38, 
		RULE_unsig = 39, RULE_unxhx = 40, RULE_output = 41, RULE_endpoint = 42, 
		RULE_cond = 43, RULE_and = 44, RULE_or = 45, RULE_sig = 46, RULE_xhx = 47, 
		RULE_csv = 48, RULE_cltv = 49;
	public static final String[] ruleNames = {
		"document", "transaction", "revocation", "certification", "membership", 
		"peer", "identity", "doctype", "version_", "type_", "currency_", "issuer_", 
		"timestamp_", "uniqueID_", "block_", "certTimestamp", "certTS", "comment", 
		"endpoints", "idtyissuer", "idtyUniqueID", "idtyTimestamp", "idtySignature", 
		"inputs", "issuers", "locktime", "member", "number", "outputs", "poWMin", 
		"publicKey", "signatures", "userID", "unlocks", "buid", "issuer", "signature", 
		"input_", "unlock", "unsig", "unxhx", "output", "endpoint", "cond", "and", 
		"or", "sig", "xhx", "csv", "cltv"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'Block'", "'CertTimestamp'", "'CertTS'", "'Comment'", "'Endpoints'", 
		"'Idtyissuer'", "'Locktime'", "'IdtySignature'", "'Inputs'", "'Issuers'", 
		"'Membership'", "'Number'", "'Outputs'", "'PoWMin'", "'PublicKey'", "'Signatures'", 
		"'UserID'", "'Unlocks'", "'D'", "'T'", "'SIG'", "'XHX'", "'&&'", "'||'", 
		"'CSV'", "'CLTV'", null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "'('", "')'", "' '", "'\n'", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, "Version", "Curr", "Blockstamp", "Iss_", "Signature_", 
		"Timestamp_", "Type_", "Currency_", "VersionHeader", "UniqueIDHeader", 
		"IdtyTimestampHeader", "Issuer_", "STR", "INT", "SIGNATURE", "HASH", "PUBKEY", 
		"VERSION", "CURRENCY", "USERID", "DOCTYPE", "BUID", "LP", "RP", "WS", 
		"NL", "COLON"
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
	public String getGrammarFileName() { return "DUP.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DUPParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocumentContext extends ParserRuleContext {
		public IdentityContext identity() {
			return getRuleContext(IdentityContext.class,0);
		}
		public MembershipContext membership() {
			return getRuleContext(MembershipContext.class,0);
		}
		public CertificationContext certification() {
			return getRuleContext(CertificationContext.class,0);
		}
		public RevocationContext revocation() {
			return getRuleContext(RevocationContext.class,0);
		}
		public PeerContext peer() {
			return getRuleContext(PeerContext.class,0);
		}
		public TransactionContext transaction() {
			return getRuleContext(TransactionContext.class,0);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitDocument(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(100);
				identity();
				}
				break;
			case 2:
				{
				setState(101);
				membership();
				}
				break;
			case 3:
				{
				setState(102);
				certification();
				}
				break;
			case 4:
				{
				setState(103);
				revocation();
				}
				break;
			case 5:
				{
				setState(104);
				peer();
				}
				break;
			case 6:
				{
				setState(105);
				transaction();
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

	public static class TransactionContext extends ParserRuleContext {
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public TerminalNode Blockstamp() { return getToken(DUPParser.Blockstamp, 0); }
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
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterTransaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitTransaction(this);
		}
	}

	public final TransactionContext transaction() throws RecognitionException {
		TransactionContext _localctx = new TransactionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_transaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			version_();
			setState(109);
			type_();
			setState(110);
			currency_();
			setState(111);
			match(Blockstamp);
			setState(112);
			locktime();
			setState(113);
			issuers();
			setState(114);
			inputs();
			setState(115);
			unlocks();
			setState(116);
			outputs();
			setState(117);
			signatures();
			setState(118);
			comment();
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
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public Issuer_Context issuer_() {
			return getRuleContext(Issuer_Context.class,0);
		}
		public LocktimeContext locktime() {
			return getRuleContext(LocktimeContext.class,0);
		}
		public IdtyTimestampContext idtyTimestamp() {
			return getRuleContext(IdtyTimestampContext.class,0);
		}
		public IdtySignatureContext idtySignature() {
			return getRuleContext(IdtySignatureContext.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public RevocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_revocation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterRevocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitRevocation(this);
		}
	}

	public final RevocationContext revocation() throws RecognitionException {
		RevocationContext _localctx = new RevocationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_revocation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			version_();
			setState(121);
			type_();
			setState(122);
			currency_();
			setState(123);
			issuer_();
			setState(124);
			locktime();
			setState(125);
			idtyTimestamp();
			setState(126);
			idtySignature();
			setState(128);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Signature_) {
				{
				setState(127);
				signature();
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

	public static class CertificationContext extends ParserRuleContext {
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public Issuer_Context issuer_() {
			return getRuleContext(Issuer_Context.class,0);
		}
		public IdtyissuerContext idtyissuer() {
			return getRuleContext(IdtyissuerContext.class,0);
		}
		public LocktimeContext locktime() {
			return getRuleContext(LocktimeContext.class,0);
		}
		public IdtyTimestampContext idtyTimestamp() {
			return getRuleContext(IdtyTimestampContext.class,0);
		}
		public IdtySignatureContext idtySignature() {
			return getRuleContext(IdtySignatureContext.class,0);
		}
		public CertTimestampContext certTimestamp() {
			return getRuleContext(CertTimestampContext.class,0);
		}
		public TerminalNode SIGNATURE() { return getToken(DUPParser.SIGNATURE, 0); }
		public CertificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCertification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCertification(this);
		}
	}

	public final CertificationContext certification() throws RecognitionException {
		CertificationContext _localctx = new CertificationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_certification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			version_();
			setState(131);
			type_();
			setState(132);
			currency_();
			setState(133);
			issuer_();
			setState(134);
			idtyissuer();
			setState(135);
			locktime();
			setState(136);
			idtyTimestamp();
			setState(137);
			idtySignature();
			setState(138);
			certTimestamp();
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SIGNATURE) {
				{
				setState(139);
				match(SIGNATURE);
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

	public static class MembershipContext extends ParserRuleContext {
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public Issuer_Context issuer_() {
			return getRuleContext(Issuer_Context.class,0);
		}
		public Block_Context block_() {
			return getRuleContext(Block_Context.class,0);
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
		public TerminalNode SIGNATURE() { return getToken(DUPParser.SIGNATURE, 0); }
		public MembershipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membership; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitMembership(this);
		}
	}

	public final MembershipContext membership() throws RecognitionException {
		MembershipContext _localctx = new MembershipContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_membership);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			version_();
			setState(143);
			type_();
			setState(144);
			currency_();
			setState(145);
			issuer_();
			setState(146);
			block_();
			setState(147);
			member();
			setState(148);
			userID();
			setState(149);
			certTS();
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SIGNATURE) {
				{
				setState(150);
				match(SIGNATURE);
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

	public static class PeerContext extends ParserRuleContext {
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public PublicKeyContext publicKey() {
			return getRuleContext(PublicKeyContext.class,0);
		}
		public Block_Context block_() {
			return getRuleContext(Block_Context.class,0);
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
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterPeer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitPeer(this);
		}
	}

	public final PeerContext peer() throws RecognitionException {
		PeerContext _localctx = new PeerContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_peer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(153);
			version_();
			setState(154);
			type_();
			setState(155);
			currency_();
			setState(156);
			publicKey();
			setState(157);
			block_();
			setState(158);
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

	public static class IdentityContext extends ParserRuleContext {
		public Version_Context version_() {
			return getRuleContext(Version_Context.class,0);
		}
		public Type_Context type_() {
			return getRuleContext(Type_Context.class,0);
		}
		public Currency_Context currency_() {
			return getRuleContext(Currency_Context.class,0);
		}
		public Issuer_Context issuer_() {
			return getRuleContext(Issuer_Context.class,0);
		}
		public UniqueID_Context uniqueID_() {
			return getRuleContext(UniqueID_Context.class,0);
		}
		public Timestamp_Context timestamp_() {
			return getRuleContext(Timestamp_Context.class,0);
		}
		public SignatureContext signature() {
			return getRuleContext(SignatureContext.class,0);
		}
		public IdentityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIdentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIdentity(this);
		}
	}

	public final IdentityContext identity() throws RecognitionException {
		IdentityContext _localctx = new IdentityContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_identity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			version_();
			setState(161);
			type_();
			setState(162);
			currency_();
			setState(163);
			issuer_();
			setState(164);
			uniqueID_();
			setState(165);
			timestamp_();
			setState(167);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Signature_) {
				{
				setState(166);
				signature();
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

	public static class DoctypeContext extends ParserRuleContext {
		public TerminalNode DOCTYPE() { return getToken(DUPParser.DOCTYPE, 0); }
		public DoctypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doctype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterDoctype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitDoctype(this);
		}
	}

	public final DoctypeContext doctype() throws RecognitionException {
		DoctypeContext _localctx = new DoctypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_doctype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			match(DOCTYPE);
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

	public static class Version_Context extends ParserRuleContext {
		public TerminalNode Version() { return getToken(DUPParser.Version, 0); }
		public Version_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterVersion_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitVersion_(this);
		}
	}

	public final Version_Context version_() throws RecognitionException {
		Version_Context _localctx = new Version_Context(_ctx, getState());
		enterRule(_localctx, 16, RULE_version_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			match(Version);
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

	public static class Type_Context extends ParserRuleContext {
		public TerminalNode Type_() { return getToken(DUPParser.Type_, 0); }
		public DoctypeContext doctype() {
			return getRuleContext(DoctypeContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public Type_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterType_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitType_(this);
		}
	}

	public final Type_Context type_() throws RecognitionException {
		Type_Context _localctx = new Type_Context(_ctx, getState());
		enterRule(_localctx, 18, RULE_type_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(Type_);
			setState(174);
			doctype();
			setState(175);
			match(NL);
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

	public static class Currency_Context extends ParserRuleContext {
		public TerminalNode Curr() { return getToken(DUPParser.Curr, 0); }
		public Currency_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_currency_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCurrency_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCurrency_(this);
		}
	}

	public final Currency_Context currency_() throws RecognitionException {
		Currency_Context _localctx = new Currency_Context(_ctx, getState());
		enterRule(_localctx, 20, RULE_currency_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(Curr);
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

	public static class Issuer_Context extends ParserRuleContext {
		public TerminalNode Issuer_() { return getToken(DUPParser.Issuer_, 0); }
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public Issuer_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuer_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIssuer_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIssuer_(this);
		}
	}

	public final Issuer_Context issuer_() throws RecognitionException {
		Issuer_Context _localctx = new Issuer_Context(_ctx, getState());
		enterRule(_localctx, 22, RULE_issuer_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(Issuer_);
			setState(180);
			issuer();
			setState(181);
			match(NL);
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

	public static class Timestamp_Context extends ParserRuleContext {
		public TerminalNode Timestamp_() { return getToken(DUPParser.Timestamp_, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public Timestamp_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timestamp_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterTimestamp_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitTimestamp_(this);
		}
	}

	public final Timestamp_Context timestamp_() throws RecognitionException {
		Timestamp_Context _localctx = new Timestamp_Context(_ctx, getState());
		enterRule(_localctx, 24, RULE_timestamp_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(Timestamp_);
			setState(184);
			buid();
			setState(185);
			match(NL);
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

	public static class UniqueID_Context extends ParserRuleContext {
		public TerminalNode UniqueIDHeader() { return getToken(DUPParser.UniqueIDHeader, 0); }
		public TerminalNode USERID() { return getToken(DUPParser.USERID, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public UniqueID_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uniqueID_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUniqueID_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUniqueID_(this);
		}
	}

	public final UniqueID_Context uniqueID_() throws RecognitionException {
		UniqueID_Context _localctx = new UniqueID_Context(_ctx, getState());
		enterRule(_localctx, 26, RULE_uniqueID_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			match(UniqueIDHeader);
			setState(188);
			match(USERID);
			setState(189);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public Block_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterBlock_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitBlock_(this);
		}
	}

	public final Block_Context block_() throws RecognitionException {
		Block_Context _localctx = new Block_Context(_ctx, getState());
		enterRule(_localctx, 28, RULE_block_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(T__0);
			setState(192);
			match(COLON);
			setState(193);
			match(WS);
			setState(194);
			buid();
			setState(195);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public CertTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCertTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCertTimestamp(this);
		}
	}

	public final CertTimestampContext certTimestamp() throws RecognitionException {
		CertTimestampContext _localctx = new CertTimestampContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_certTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			match(T__1);
			setState(198);
			match(COLON);
			setState(199);
			match(WS);
			setState(200);
			buid();
			setState(201);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public CertTSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTS; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCertTS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCertTS(this);
		}
	}

	public final CertTSContext certTS() throws RecognitionException {
		CertTSContext _localctx = new CertTSContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_certTS);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(T__2);
			setState(204);
			match(COLON);
			setState(205);
			match(WS);
			setState(206);
			buid();
			setState(207);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode STR() { return getToken(DUPParser.STR, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitComment(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(T__3);
			setState(210);
			match(COLON);
			setState(211);
			match(WS);
			setState(212);
			match(STR);
			setState(213);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<EndpointContext> endpoint() {
			return getRuleContexts(EndpointContext.class);
		}
		public EndpointContext endpoint(int i) {
			return getRuleContext(EndpointContext.class,i);
		}
		public EndpointsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoints; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterEndpoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitEndpoints(this);
		}
	}

	public final EndpointsContext endpoints() throws RecognitionException {
		EndpointsContext _localctx = new EndpointsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_endpoints);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(T__4);
			setState(216);
			match(COLON);
			setState(217);
			match(NL);
			setState(221); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(218);
				endpoint();
				setState(219);
				match(NL);
				}
				}
				setState(223); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==STR );
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

	public static class IdtyissuerContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode PUBKEY() { return getToken(DUPParser.PUBKEY, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public IdtyissuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyissuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIdtyissuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIdtyissuer(this);
		}
	}

	public final IdtyissuerContext idtyissuer() throws RecognitionException {
		IdtyissuerContext _localctx = new IdtyissuerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_idtyissuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(T__5);
			setState(226);
			match(COLON);
			setState(227);
			match(WS);
			setState(228);
			match(PUBKEY);
			setState(229);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode USERID() { return getToken(DUPParser.USERID, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public IdtyUniqueIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyUniqueID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIdtyUniqueID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIdtyUniqueID(this);
		}
	}

	public final IdtyUniqueIDContext idtyUniqueID() throws RecognitionException {
		IdtyUniqueIDContext _localctx = new IdtyUniqueIDContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_idtyUniqueID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(T__6);
			setState(232);
			match(COLON);
			setState(233);
			match(WS);
			setState(234);
			match(USERID);
			setState(235);
			match(NL);
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
		public TerminalNode IdtyTimestampHeader() { return getToken(DUPParser.IdtyTimestampHeader, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public IdtyTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIdtyTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIdtyTimestamp(this);
		}
	}

	public final IdtyTimestampContext idtyTimestamp() throws RecognitionException {
		IdtyTimestampContext _localctx = new IdtyTimestampContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_idtyTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(IdtyTimestampHeader);
			setState(238);
			buid();
			setState(239);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode SIGNATURE() { return getToken(DUPParser.SIGNATURE, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public IdtySignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtySignature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIdtySignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIdtySignature(this);
		}
	}

	public final IdtySignatureContext idtySignature() throws RecognitionException {
		IdtySignatureContext _localctx = new IdtySignatureContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_idtySignature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			match(T__7);
			setState(242);
			match(COLON);
			setState(243);
			match(WS);
			setState(244);
			match(SIGNATURE);
			setState(245);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<Input_Context> input_() {
			return getRuleContexts(Input_Context.class);
		}
		public Input_Context input_(int i) {
			return getRuleContext(Input_Context.class,i);
		}
		public InputsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inputs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterInputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitInputs(this);
		}
	}

	public final InputsContext inputs() throws RecognitionException {
		InputsContext _localctx = new InputsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_inputs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(247);
			match(T__8);
			setState(248);
			match(COLON);
			setState(249);
			match(NL);
			setState(253); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(250);
				input_();
				setState(251);
				match(NL);
				}
				}
				setState(255); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==INT );
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<IssuerContext> issuer() {
			return getRuleContexts(IssuerContext.class);
		}
		public IssuerContext issuer(int i) {
			return getRuleContext(IssuerContext.class,i);
		}
		public IssuersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIssuers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIssuers(this);
		}
	}

	public final IssuersContext issuers() throws RecognitionException {
		IssuersContext _localctx = new IssuersContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_issuers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(T__9);
			setState(258);
			match(COLON);
			setState(259);
			match(NL);
			setState(263); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(260);
				issuer();
				setState(261);
				match(NL);
				}
				}
				setState(265); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==PUBKEY );
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public LocktimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locktime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterLocktime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitLocktime(this);
		}
	}

	public final LocktimeContext locktime() throws RecognitionException {
		LocktimeContext _localctx = new LocktimeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_locktime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(267);
			match(T__6);
			setState(268);
			match(COLON);
			setState(269);
			match(WS);
			setState(270);
			match(INT);
			setState(271);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode STR() { return getToken(DUPParser.STR, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitMember(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_member);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			match(T__10);
			setState(274);
			match(COLON);
			setState(275);
			match(WS);
			setState(276);
			match(STR);
			setState(277);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitNumber(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(T__11);
			setState(280);
			match(COLON);
			setState(281);
			match(WS);
			setState(282);
			match(INT);
			setState(283);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<OutputContext> output() {
			return getRuleContexts(OutputContext.class);
		}
		public OutputContext output(int i) {
			return getRuleContext(OutputContext.class,i);
		}
		public OutputsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outputs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterOutputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitOutputs(this);
		}
	}

	public final OutputsContext outputs() throws RecognitionException {
		OutputsContext _localctx = new OutputsContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_outputs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(T__12);
			setState(286);
			match(COLON);
			setState(287);
			match(NL);
			setState(291); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(288);
				output();
				setState(289);
				match(NL);
				}
				}
				setState(293); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==INT );
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

	public static class PoWMinContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public PoWMinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_poWMin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterPoWMin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitPoWMin(this);
		}
	}

	public final PoWMinContext poWMin() throws RecognitionException {
		PoWMinContext _localctx = new PoWMinContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_poWMin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(T__13);
			setState(296);
			match(COLON);
			setState(297);
			match(WS);
			setState(298);
			match(INT);
			setState(299);
			match(NL);
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

	public static class PublicKeyContext extends ParserRuleContext {
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode PUBKEY() { return getToken(DUPParser.PUBKEY, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public PublicKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_publicKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterPublicKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitPublicKey(this);
		}
	}

	public final PublicKeyContext publicKey() throws RecognitionException {
		PublicKeyContext _localctx = new PublicKeyContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_publicKey);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			match(T__14);
			setState(302);
			match(COLON);
			setState(303);
			match(WS);
			setState(304);
			match(PUBKEY);
			setState(305);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<SignatureContext> signature() {
			return getRuleContexts(SignatureContext.class);
		}
		public SignatureContext signature(int i) {
			return getRuleContext(SignatureContext.class,i);
		}
		public SignaturesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signatures; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterSignatures(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitSignatures(this);
		}
	}

	public final SignaturesContext signatures() throws RecognitionException {
		SignaturesContext _localctx = new SignaturesContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_signatures);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			match(T__15);
			setState(308);
			match(COLON);
			setState(309);
			match(NL);
			setState(313); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(310);
				signature();
				setState(311);
				match(NL);
				}
				}
				setState(315); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Signature_ );
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public TerminalNode WS() { return getToken(DUPParser.WS, 0); }
		public TerminalNode USERID() { return getToken(DUPParser.USERID, 0); }
		public TerminalNode NL() { return getToken(DUPParser.NL, 0); }
		public UserIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUserID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUserID(this);
		}
	}

	public final UserIDContext userID() throws RecognitionException {
		UserIDContext _localctx = new UserIDContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_userID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(T__16);
			setState(318);
			match(COLON);
			setState(319);
			match(WS);
			setState(320);
			match(USERID);
			setState(321);
			match(NL);
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
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public List<TerminalNode> NL() { return getTokens(DUPParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(DUPParser.NL, i);
		}
		public List<UnlockContext> unlock() {
			return getRuleContexts(UnlockContext.class);
		}
		public UnlockContext unlock(int i) {
			return getRuleContext(UnlockContext.class,i);
		}
		public UnlocksContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unlocks; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUnlocks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUnlocks(this);
		}
	}

	public final UnlocksContext unlocks() throws RecognitionException {
		UnlocksContext _localctx = new UnlocksContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_unlocks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(323);
			match(T__17);
			setState(324);
			match(COLON);
			setState(325);
			match(NL);
			setState(329); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(326);
				unlock();
				setState(327);
				match(NL);
				}
				}
				setState(331); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==INT );
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
		public TerminalNode BUID() { return getToken(DUPParser.BUID, 0); }
		public BuidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterBuid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitBuid(this);
		}
	}

	public final BuidContext buid() throws RecognitionException {
		BuidContext _localctx = new BuidContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_buid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			match(BUID);
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
		public TerminalNode PUBKEY() { return getToken(DUPParser.PUBKEY, 0); }
		public IssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitIssuer(this);
		}
	}

	public final IssuerContext issuer() throws RecognitionException {
		IssuerContext _localctx = new IssuerContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_issuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(PUBKEY);
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
		public TerminalNode Signature_() { return getToken(DUPParser.Signature_, 0); }
		public SignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterSignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitSignature(this);
		}
	}

	public final SignatureContext signature() throws RecognitionException {
		SignatureContext _localctx = new SignatureContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_signature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			match(Signature_);
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

	public static class Input_Context extends ParserRuleContext {
		public List<TerminalNode> INT() { return getTokens(DUPParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(DUPParser.INT, i);
		}
		public List<TerminalNode> COLON() { return getTokens(DUPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(DUPParser.COLON, i);
		}
		public TerminalNode HASH() { return getToken(DUPParser.HASH, 0); }
		public Input_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterInput_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitInput_(this);
		}
	}

	public final Input_Context input_() throws RecognitionException {
		Input_Context _localctx = new Input_Context(_ctx, getState());
		enterRule(_localctx, 74, RULE_input_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(339);
			match(INT);
			setState(340);
			match(COLON);
			setState(341);
			match(INT);
			setState(354);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				{
				setState(342);
				match(COLON);
				setState(343);
				match(T__18);
				setState(344);
				match(COLON);
				setState(345);
				match(HASH);
				setState(346);
				match(COLON);
				setState(347);
				match(INT);
				}
				}
				break;
			case 2:
				{
				{
				setState(348);
				match(COLON);
				setState(349);
				match(T__19);
				setState(350);
				match(COLON);
				setState(351);
				match(HASH);
				setState(352);
				match(COLON);
				setState(353);
				match(INT);
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

	public static class UnlockContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode COLON() { return getToken(DUPParser.COLON, 0); }
		public UnsigContext unsig() {
			return getRuleContext(UnsigContext.class,0);
		}
		public UnxhxContext unxhx() {
			return getRuleContext(UnxhxContext.class,0);
		}
		public UnlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUnlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUnlock(this);
		}
	}

	public final UnlockContext unlock() throws RecognitionException {
		UnlockContext _localctx = new UnlockContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_unlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			match(INT);
			setState(357);
			match(COLON);
			setState(360);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				setState(358);
				unsig();
				}
				break;
			case T__21:
				{
				setState(359);
				unxhx();
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

	public static class UnsigContext extends ParserRuleContext {
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public UnsigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUnsig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUnsig(this);
		}
	}

	public final UnsigContext unsig() throws RecognitionException {
		UnsigContext _localctx = new UnsigContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_unsig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(362);
			match(T__20);
			setState(363);
			match(LP);
			setState(364);
			match(INT);
			setState(365);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public UnxhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unxhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterUnxhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitUnxhx(this);
		}
	}

	public final UnxhxContext unxhx() throws RecognitionException {
		UnxhxContext _localctx = new UnxhxContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_unxhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			match(T__21);
			setState(368);
			match(LP);
			setState(369);
			match(INT);
			setState(370);
			match(RP);
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
		public List<TerminalNode> INT() { return getTokens(DUPParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(DUPParser.INT, i);
		}
		public List<TerminalNode> COLON() { return getTokens(DUPParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(DUPParser.COLON, i);
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
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitOutput(this);
		}
	}

	public final OutputContext output() throws RecognitionException {
		OutputContext _localctx = new OutputContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_output);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(372);
			match(INT);
			setState(373);
			match(COLON);
			setState(374);
			match(INT);
			setState(375);
			match(COLON);
			setState(376);
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

	public static class EndpointContext extends ParserRuleContext {
		public List<TerminalNode> STR() { return getTokens(DUPParser.STR); }
		public TerminalNode STR(int i) {
			return getToken(DUPParser.STR, i);
		}
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public EndpointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterEndpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitEndpoint(this);
		}
	}

	public final EndpointContext endpoint() throws RecognitionException {
		EndpointContext _localctx = new EndpointContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_endpoint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(378);
			match(STR);
			setState(381); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(379);
				match(WS);
				setState(380);
				match(STR);
				}
				}
				setState(383); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(385);
			match(INT);
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
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCond(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		CondContext _localctx = new CondContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_cond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(387);
				sig();
				}
				break;
			case 2:
				{
				setState(388);
				xhx();
				}
				break;
			case 3:
				{
				setState(389);
				csv();
				}
				break;
			case 4:
				{
				setState(390);
				cltv();
				}
				break;
			case 5:
				{
				setState(391);
				or();
				}
				break;
			case 6:
				{
				setState(392);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(DUPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DUPParser.WS, i);
		}
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitAnd(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_and);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			match(LP);
			setState(396);
			cond();
			setState(397);
			match(WS);
			setState(398);
			match(T__22);
			setState(399);
			match(WS);
			setState(400);
			cond();
			setState(401);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(DUPParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(DUPParser.WS, i);
		}
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public OrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitOr(this);
		}
	}

	public final OrContext or() throws RecognitionException {
		OrContext _localctx = new OrContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_or);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			match(LP);
			setState(404);
			cond();
			setState(405);
			match(WS);
			setState(406);
			match(T__23);
			setState(407);
			match(WS);
			setState(408);
			cond();
			setState(409);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode PUBKEY() { return getToken(DUPParser.PUBKEY, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public SigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterSig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitSig(this);
		}
	}

	public final SigContext sig() throws RecognitionException {
		SigContext _localctx = new SigContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_sig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(T__20);
			setState(412);
			match(LP);
			setState(413);
			match(PUBKEY);
			setState(414);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode HASH() { return getToken(DUPParser.HASH, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public XhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterXhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitXhx(this);
		}
	}

	public final XhxContext xhx() throws RecognitionException {
		XhxContext _localctx = new XhxContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_xhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(416);
			match(T__21);
			setState(417);
			match(LP);
			setState(418);
			match(HASH);
			setState(419);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public CsvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_csv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCsv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCsv(this);
		}
	}

	public final CsvContext csv() throws RecognitionException {
		CsvContext _localctx = new CsvContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_csv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(421);
			match(T__24);
			setState(422);
			match(LP);
			setState(423);
			match(INT);
			setState(424);
			match(RP);
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
		public TerminalNode LP() { return getToken(DUPParser.LP, 0); }
		public TerminalNode INT() { return getToken(DUPParser.INT, 0); }
		public TerminalNode RP() { return getToken(DUPParser.RP, 0); }
		public CltvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cltv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).enterCltv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DUPListener ) ((DUPListener)listener).exitCltv(this);
		}
	}

	public final CltvContext cltv() throws RecognitionException {
		CltvContext _localctx = new CltvContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_cltv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			match(T__25);
			setState(427);
			match(LP);
			setState(428);
			match(INT);
			setState(429);
			match(RP);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\67\u01b2\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\5\2m\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0083\n\4\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\5\5\u008f\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\5\6\u009a\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\5\b\u00aa\n\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\6\24\u00e0\n\24"+
		"\r\24\16\24\u00e1\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\6\31\u0100\n\31\r\31\16\31\u0101\3\32\3\32\3\32\3\32"+
		"\3\32\3\32\6\32\u010a\n\32\r\32\16\32\u010b\3\33\3\33\3\33\3\33\3\33\3"+
		"\33\3\34\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3"+
		"\36\3\36\3\36\3\36\3\36\6\36\u0126\n\36\r\36\16\36\u0127\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\6!\u013c\n!\r!\16"+
		"!\u013d\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\6#\u014c\n#\r#\16#\u014d"+
		"\3$\3$\3%\3%\3&\3&\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'\3\'"+
		"\3\'\3\'\5\'\u0165\n\'\3(\3(\3(\3(\5(\u016b\n(\3)\3)\3)\3)\3)\3*\3*\3"+
		"*\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\3,\6,\u0180\n,\r,\16,\u0181\3,\3,\3-\3"+
		"-\3-\3-\3-\3-\5-\u018c\n-\3.\3.\3.\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3"+
		"/\3/\3\60\3\60\3\60\3\60\3\60\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62"+
		"\3\62\3\62\3\63\3\63\3\63\3\63\3\63\3\63\2\2\64\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd\2\2\2\u0196"+
		"\2l\3\2\2\2\4n\3\2\2\2\6z\3\2\2\2\b\u0084\3\2\2\2\n\u0090\3\2\2\2\f\u009b"+
		"\3\2\2\2\16\u00a2\3\2\2\2\20\u00ab\3\2\2\2\22\u00ad\3\2\2\2\24\u00af\3"+
		"\2\2\2\26\u00b3\3\2\2\2\30\u00b5\3\2\2\2\32\u00b9\3\2\2\2\34\u00bd\3\2"+
		"\2\2\36\u00c1\3\2\2\2 \u00c7\3\2\2\2\"\u00cd\3\2\2\2$\u00d3\3\2\2\2&\u00d9"+
		"\3\2\2\2(\u00e3\3\2\2\2*\u00e9\3\2\2\2,\u00ef\3\2\2\2.\u00f3\3\2\2\2\60"+
		"\u00f9\3\2\2\2\62\u0103\3\2\2\2\64\u010d\3\2\2\2\66\u0113\3\2\2\28\u0119"+
		"\3\2\2\2:\u011f\3\2\2\2<\u0129\3\2\2\2>\u012f\3\2\2\2@\u0135\3\2\2\2B"+
		"\u013f\3\2\2\2D\u0145\3\2\2\2F\u014f\3\2\2\2H\u0151\3\2\2\2J\u0153\3\2"+
		"\2\2L\u0155\3\2\2\2N\u0166\3\2\2\2P\u016c\3\2\2\2R\u0171\3\2\2\2T\u0176"+
		"\3\2\2\2V\u017c\3\2\2\2X\u018b\3\2\2\2Z\u018d\3\2\2\2\\\u0195\3\2\2\2"+
		"^\u019d\3\2\2\2`\u01a2\3\2\2\2b\u01a7\3\2\2\2d\u01ac\3\2\2\2fm\5\16\b"+
		"\2gm\5\n\6\2hm\5\b\5\2im\5\6\4\2jm\5\f\7\2km\5\4\3\2lf\3\2\2\2lg\3\2\2"+
		"\2lh\3\2\2\2li\3\2\2\2lj\3\2\2\2lk\3\2\2\2m\3\3\2\2\2no\5\22\n\2op\5\24"+
		"\13\2pq\5\26\f\2qr\7\37\2\2rs\5\64\33\2st\5\62\32\2tu\5\60\31\2uv\5D#"+
		"\2vw\5:\36\2wx\5@!\2xy\5$\23\2y\5\3\2\2\2z{\5\22\n\2{|\5\24\13\2|}\5\26"+
		"\f\2}~\5\30\r\2~\177\5\64\33\2\177\u0080\5,\27\2\u0080\u0082\5.\30\2\u0081"+
		"\u0083\5J&\2\u0082\u0081\3\2\2\2\u0082\u0083\3\2\2\2\u0083\7\3\2\2\2\u0084"+
		"\u0085\5\22\n\2\u0085\u0086\5\24\13\2\u0086\u0087\5\26\f\2\u0087\u0088"+
		"\5\30\r\2\u0088\u0089\5(\25\2\u0089\u008a\5\64\33\2\u008a\u008b\5,\27"+
		"\2\u008b\u008c\5.\30\2\u008c\u008e\5 \21\2\u008d\u008f\7+\2\2\u008e\u008d"+
		"\3\2\2\2\u008e\u008f\3\2\2\2\u008f\t\3\2\2\2\u0090\u0091\5\22\n\2\u0091"+
		"\u0092\5\24\13\2\u0092\u0093\5\26\f\2\u0093\u0094\5\30\r\2\u0094\u0095"+
		"\5\36\20\2\u0095\u0096\5\66\34\2\u0096\u0097\5B\"\2\u0097\u0099\5\"\22"+
		"\2\u0098\u009a\7+\2\2\u0099\u0098\3\2\2\2\u0099\u009a\3\2\2\2\u009a\13"+
		"\3\2\2\2\u009b\u009c\5\22\n\2\u009c\u009d\5\24\13\2\u009d\u009e\5\26\f"+
		"\2\u009e\u009f\5> \2\u009f\u00a0\5\36\20\2\u00a0\u00a1\5&\24\2\u00a1\r"+
		"\3\2\2\2\u00a2\u00a3\5\22\n\2\u00a3\u00a4\5\24\13\2\u00a4\u00a5\5\26\f"+
		"\2\u00a5\u00a6\5\30\r\2\u00a6\u00a7\5\34\17\2\u00a7\u00a9\5\32\16\2\u00a8"+
		"\u00aa\5J&\2\u00a9\u00a8\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\17\3\2\2\2"+
		"\u00ab\u00ac\7\61\2\2\u00ac\21\3\2\2\2\u00ad\u00ae\7\35\2\2\u00ae\23\3"+
		"\2\2\2\u00af\u00b0\7#\2\2\u00b0\u00b1\5\20\t\2\u00b1\u00b2\7\66\2\2\u00b2"+
		"\25\3\2\2\2\u00b3\u00b4\7\36\2\2\u00b4\27\3\2\2\2\u00b5\u00b6\7(\2\2\u00b6"+
		"\u00b7\5H%\2\u00b7\u00b8\7\66\2\2\u00b8\31\3\2\2\2\u00b9\u00ba\7\"\2\2"+
		"\u00ba\u00bb\5F$\2\u00bb\u00bc\7\66\2\2\u00bc\33\3\2\2\2\u00bd\u00be\7"+
		"&\2\2\u00be\u00bf\7\60\2\2\u00bf\u00c0\7\66\2\2\u00c0\35\3\2\2\2\u00c1"+
		"\u00c2\7\3\2\2\u00c2\u00c3\7\67\2\2\u00c3\u00c4\7\65\2\2\u00c4\u00c5\5"+
		"F$\2\u00c5\u00c6\7\66\2\2\u00c6\37\3\2\2\2\u00c7\u00c8\7\4\2\2\u00c8\u00c9"+
		"\7\67\2\2\u00c9\u00ca\7\65\2\2\u00ca\u00cb\5F$\2\u00cb\u00cc\7\66\2\2"+
		"\u00cc!\3\2\2\2\u00cd\u00ce\7\5\2\2\u00ce\u00cf\7\67\2\2\u00cf\u00d0\7"+
		"\65\2\2\u00d0\u00d1\5F$\2\u00d1\u00d2\7\66\2\2\u00d2#\3\2\2\2\u00d3\u00d4"+
		"\7\6\2\2\u00d4\u00d5\7\67\2\2\u00d5\u00d6\7\65\2\2\u00d6\u00d7\7)\2\2"+
		"\u00d7\u00d8\7\66\2\2\u00d8%\3\2\2\2\u00d9\u00da\7\7\2\2\u00da\u00db\7"+
		"\67\2\2\u00db\u00df\7\66\2\2\u00dc\u00dd\5V,\2\u00dd\u00de\7\66\2\2\u00de"+
		"\u00e0\3\2\2\2\u00df\u00dc\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1\u00df\3\2"+
		"\2\2\u00e1\u00e2\3\2\2\2\u00e2\'\3\2\2\2\u00e3\u00e4\7\b\2\2\u00e4\u00e5"+
		"\7\67\2\2\u00e5\u00e6\7\65\2\2\u00e6\u00e7\7-\2\2\u00e7\u00e8\7\66\2\2"+
		"\u00e8)\3\2\2\2\u00e9\u00ea\7\t\2\2\u00ea\u00eb\7\67\2\2\u00eb\u00ec\7"+
		"\65\2\2\u00ec\u00ed\7\60\2\2\u00ed\u00ee\7\66\2\2\u00ee+\3\2\2\2\u00ef"+
		"\u00f0\7\'\2\2\u00f0\u00f1\5F$\2\u00f1\u00f2\7\66\2\2\u00f2-\3\2\2\2\u00f3"+
		"\u00f4\7\n\2\2\u00f4\u00f5\7\67\2\2\u00f5\u00f6\7\65\2\2\u00f6\u00f7\7"+
		"+\2\2\u00f7\u00f8\7\66\2\2\u00f8/\3\2\2\2\u00f9\u00fa\7\13\2\2\u00fa\u00fb"+
		"\7\67\2\2\u00fb\u00ff\7\66\2\2\u00fc\u00fd\5L\'\2\u00fd\u00fe\7\66\2\2"+
		"\u00fe\u0100\3\2\2\2\u00ff\u00fc\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u00ff"+
		"\3\2\2\2\u0101\u0102\3\2\2\2\u0102\61\3\2\2\2\u0103\u0104\7\f\2\2\u0104"+
		"\u0105\7\67\2\2\u0105\u0109\7\66\2\2\u0106\u0107\5H%\2\u0107\u0108\7\66"+
		"\2\2\u0108\u010a\3\2\2\2\u0109\u0106\3\2\2\2\u010a\u010b\3\2\2\2\u010b"+
		"\u0109\3\2\2\2\u010b\u010c\3\2\2\2\u010c\63\3\2\2\2\u010d\u010e\7\t\2"+
		"\2\u010e\u010f\7\67\2\2\u010f\u0110\7\65\2\2\u0110\u0111\7*\2\2\u0111"+
		"\u0112\7\66\2\2\u0112\65\3\2\2\2\u0113\u0114\7\r\2\2\u0114\u0115\7\67"+
		"\2\2\u0115\u0116\7\65\2\2\u0116\u0117\7)\2\2\u0117\u0118\7\66\2\2\u0118"+
		"\67\3\2\2\2\u0119\u011a\7\16\2\2\u011a\u011b\7\67\2\2\u011b\u011c\7\65"+
		"\2\2\u011c\u011d\7*\2\2\u011d\u011e\7\66\2\2\u011e9\3\2\2\2\u011f\u0120"+
		"\7\17\2\2\u0120\u0121\7\67\2\2\u0121\u0125\7\66\2\2\u0122\u0123\5T+\2"+
		"\u0123\u0124\7\66\2\2\u0124\u0126\3\2\2\2\u0125\u0122\3\2\2\2\u0126\u0127"+
		"\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128;\3\2\2\2\u0129"+
		"\u012a\7\20\2\2\u012a\u012b\7\67\2\2\u012b\u012c\7\65\2\2\u012c\u012d"+
		"\7*\2\2\u012d\u012e\7\66\2\2\u012e=\3\2\2\2\u012f\u0130\7\21\2\2\u0130"+
		"\u0131\7\67\2\2\u0131\u0132\7\65\2\2\u0132\u0133\7-\2\2\u0133\u0134\7"+
		"\66\2\2\u0134?\3\2\2\2\u0135\u0136\7\22\2\2\u0136\u0137\7\67\2\2\u0137"+
		"\u013b\7\66\2\2\u0138\u0139\5J&\2\u0139\u013a\7\66\2\2\u013a\u013c\3\2"+
		"\2\2\u013b\u0138\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013b\3\2\2\2\u013d"+
		"\u013e\3\2\2\2\u013eA\3\2\2\2\u013f\u0140\7\23\2\2\u0140\u0141\7\67\2"+
		"\2\u0141\u0142\7\65\2\2\u0142\u0143\7\60\2\2\u0143\u0144\7\66\2\2\u0144"+
		"C\3\2\2\2\u0145\u0146\7\24\2\2\u0146\u0147\7\67\2\2\u0147\u014b\7\66\2"+
		"\2\u0148\u0149\5N(\2\u0149\u014a\7\66\2\2\u014a\u014c\3\2\2\2\u014b\u0148"+
		"\3\2\2\2\u014c\u014d\3\2\2\2\u014d\u014b\3\2\2\2\u014d\u014e\3\2\2\2\u014e"+
		"E\3\2\2\2\u014f\u0150\7\62\2\2\u0150G\3\2\2\2\u0151\u0152\7-\2\2\u0152"+
		"I\3\2\2\2\u0153\u0154\7!\2\2\u0154K\3\2\2\2\u0155\u0156\7*\2\2\u0156\u0157"+
		"\7\67\2\2\u0157\u0164\7*\2\2\u0158\u0159\7\67\2\2\u0159\u015a\7\25\2\2"+
		"\u015a\u015b\7\67\2\2\u015b\u015c\7,\2\2\u015c\u015d\7\67\2\2\u015d\u0165"+
		"\7*\2\2\u015e\u015f\7\67\2\2\u015f\u0160\7\26\2\2\u0160\u0161\7\67\2\2"+
		"\u0161\u0162\7,\2\2\u0162\u0163\7\67\2\2\u0163\u0165\7*\2\2\u0164\u0158"+
		"\3\2\2\2\u0164\u015e\3\2\2\2\u0165M\3\2\2\2\u0166\u0167\7*\2\2\u0167\u016a"+
		"\7\67\2\2\u0168\u016b\5P)\2\u0169\u016b\5R*\2\u016a\u0168\3\2\2\2\u016a"+
		"\u0169\3\2\2\2\u016bO\3\2\2\2\u016c\u016d\7\27\2\2\u016d\u016e\7\63\2"+
		"\2\u016e\u016f\7*\2\2\u016f\u0170\7\64\2\2\u0170Q\3\2\2\2\u0171\u0172"+
		"\7\30\2\2\u0172\u0173\7\63\2\2\u0173\u0174\7*\2\2\u0174\u0175\7\64\2\2"+
		"\u0175S\3\2\2\2\u0176\u0177\7*\2\2\u0177\u0178\7\67\2\2\u0178\u0179\7"+
		"*\2\2\u0179\u017a\7\67\2\2\u017a\u017b\5X-\2\u017bU\3\2\2\2\u017c\u017f"+
		"\7)\2\2\u017d\u017e\7\65\2\2\u017e\u0180\7)\2\2\u017f\u017d\3\2\2\2\u0180"+
		"\u0181\3\2\2\2\u0181\u017f\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0183\3\2"+
		"\2\2\u0183\u0184\7*\2\2\u0184W\3\2\2\2\u0185\u018c\5^\60\2\u0186\u018c"+
		"\5`\61\2\u0187\u018c\5b\62\2\u0188\u018c\5d\63\2\u0189\u018c\5\\/\2\u018a"+
		"\u018c\5Z.\2\u018b\u0185\3\2\2\2\u018b\u0186\3\2\2\2\u018b\u0187\3\2\2"+
		"\2\u018b\u0188\3\2\2\2\u018b\u0189\3\2\2\2\u018b\u018a\3\2\2\2\u018cY"+
		"\3\2\2\2\u018d\u018e\7\63\2\2\u018e\u018f\5X-\2\u018f\u0190\7\65\2\2\u0190"+
		"\u0191\7\31\2\2\u0191\u0192\7\65\2\2\u0192\u0193\5X-\2\u0193\u0194\7\64"+
		"\2\2\u0194[\3\2\2\2\u0195\u0196\7\63\2\2\u0196\u0197\5X-\2\u0197\u0198"+
		"\7\65\2\2\u0198\u0199\7\32\2\2\u0199\u019a\7\65\2\2\u019a\u019b\5X-\2"+
		"\u019b\u019c\7\64\2\2\u019c]\3\2\2\2\u019d\u019e\7\27\2\2\u019e\u019f"+
		"\7\63\2\2\u019f\u01a0\7-\2\2\u01a0\u01a1\7\64\2\2\u01a1_\3\2\2\2\u01a2"+
		"\u01a3\7\30\2\2\u01a3\u01a4\7\63\2\2\u01a4\u01a5\7,\2\2\u01a5\u01a6\7"+
		"\64\2\2\u01a6a\3\2\2\2\u01a7\u01a8\7\33\2\2\u01a8\u01a9\7\63\2\2\u01a9"+
		"\u01aa\7*\2\2\u01aa\u01ab\7\64\2\2\u01abc\3\2\2\2\u01ac\u01ad\7\34\2\2"+
		"\u01ad\u01ae\7\63\2\2\u01ae\u01af\7*\2\2\u01af\u01b0\7\64\2\2\u01b0e\3"+
		"\2\2\2\21l\u0082\u008e\u0099\u00a9\u00e1\u0101\u010b\u0127\u013d\u014d"+
		"\u0164\u016a\u0181\u018b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}