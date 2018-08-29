// Generated from Juniter.g4 by ANTLR 4.7.1
 
package juniter.grammar.antlr4; 

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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, VERSION=27, CURRENCY=28, Iss_=29, Signature_=30, Version=31, 
		Curr=32, Timestamp_=33, Type_=34, Currency_=35, VersionHeader=36, UniqueIDHeader=37, 
		IdtyTimestampHeader=38, Issuer_=39, Blockstamp=40, STR=41, INT=42, SIGNATURE=43, 
		HASH=44, PUBKEY=45, USERID=46, DOCTYPE=47, LP=48, RP=49, WS=50, DASH=51, 
		EOL=52, COLON=53;
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
		RULE_dassh = 35, RULE_intt = 36, RULE_hashh = 37, RULE_issuer = 38, RULE_signature = 39, 
		RULE_input_ = 40, RULE_unlock = 41, RULE_unsig = 42, RULE_unxhx = 43, 
		RULE_output = 44, RULE_endpoint = 45, RULE_cond = 46, RULE_and = 47, RULE_or = 48, 
		RULE_sig = 49, RULE_xhx = 50, RULE_csv = 51, RULE_cltv = 52;
	public static final String[] ruleNames = {
		"document", "transaction", "revocation", "certification", "membership", 
		"peer", "identity", "doctype", "version_", "type_", "currency_", "issuer_", 
		"timestamp_", "uniqueID_", "block_", "certTimestamp", "certTS", "comment", 
		"endpoints", "idtyissuer", "idtyUniqueID", "idtyTimestamp", "idtySignature", 
		"inputs", "issuers", "locktime", "member", "number", "outputs", "poWMin", 
		"publicKey", "signatures", "userID", "unlocks", "buid", "dassh", "intt", 
		"hashh", "issuer", "signature", "input_", "unlock", "unsig", "unxhx", 
		"output", "endpoint", "cond", "and", "or", "sig", "xhx", "csv", "cltv"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'Block'", "'CertTimestamp'", "'CertTS'", "'Comment'", "'Endpoints'", 
		"'Idtyissuer'", "'Locktime'", "'IdtySignature'", "'Inputs'", "'Issuers'", 
		"'Membership'", "'Number'", "'Outputs'", "'PoWMin'", "'PublicKey'", "'Signatures'", 
		"'UserID'", "'Unlocks'", "'D'", "'T'", "'SIG'", "'XHX'", "'&&'", "'||'", 
		"'CSV'", "'CLTV'", "'10'", "'g1'", null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "'('", "')'", "' '", "'-'", "'\n'", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, "VERSION", "CURRENCY", "Iss_", "Signature_", "Version", 
		"Curr", "Timestamp_", "Type_", "Currency_", "VersionHeader", "UniqueIDHeader", 
		"IdtyTimestampHeader", "Issuer_", "Blockstamp", "STR", "INT", "SIGNATURE", 
		"HASH", "PUBKEY", "USERID", "DOCTYPE", "LP", "RP", "WS", "DASH", "EOL", 
		"COLON"
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
	public String getGrammarFileName() { return "Juniter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	int count = 0;  
	String val = "_";

	public JuniterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class DocumentContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JuniterParser.EOF, 0); }
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitDocument(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(106);
				identity();
				}
				break;
			case 2:
				{
				setState(107);
				membership();
				}
				break;
			case 3:
				{
				setState(108);
				certification();
				}
				break;
			case 4:
				{
				setState(109);
				revocation();
				}
				break;
			case 5:
				{
				setState(110);
				peer();
				}
				break;
			case 6:
				{
				setState(111);
				transaction();
				}
				break;
			}
			count++;
			setState(115);
			match(EOF);
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
		public TerminalNode Blockstamp() { return getToken(JuniterParser.Blockstamp, 0); }
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterTransaction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitTransaction(this);
		}
	}

	public final TransactionContext transaction() throws RecognitionException {
		TransactionContext _localctx = new TransactionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_transaction);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			version_();
			setState(118);
			type_();
			setState(119);
			currency_();
			setState(120);
			match(Blockstamp);
			setState(121);
			locktime();
			setState(122);
			issuers();
			setState(123);
			inputs();
			setState(124);
			unlocks();
			setState(125);
			outputs();
			setState(126);
			signatures();
			setState(127);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterRevocation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitRevocation(this);
		}
	}

	public final RevocationContext revocation() throws RecognitionException {
		RevocationContext _localctx = new RevocationContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_revocation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			version_();
			setState(130);
			type_();
			setState(131);
			currency_();
			setState(132);
			issuer_();
			setState(133);
			locktime();
			setState(134);
			idtyTimestamp();
			setState(135);
			idtySignature();
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Signature_) {
				{
				setState(136);
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
		public TerminalNode SIGNATURE() { return getToken(JuniterParser.SIGNATURE, 0); }
		public CertificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCertification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCertification(this);
		}
	}

	public final CertificationContext certification() throws RecognitionException {
		CertificationContext _localctx = new CertificationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_certification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			version_();
			setState(140);
			type_();
			setState(141);
			currency_();
			setState(142);
			issuer_();
			setState(143);
			idtyissuer();
			setState(144);
			locktime();
			setState(145);
			idtyTimestamp();
			setState(146);
			idtySignature();
			setState(147);
			certTimestamp();
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SIGNATURE) {
				{
				setState(148);
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
		public TerminalNode SIGNATURE() { return getToken(JuniterParser.SIGNATURE, 0); }
		public MembershipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_membership; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterMembership(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitMembership(this);
		}
	}

	public final MembershipContext membership() throws RecognitionException {
		MembershipContext _localctx = new MembershipContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_membership);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			version_();
			setState(152);
			type_();
			setState(153);
			currency_();
			setState(154);
			issuer_();
			setState(155);
			block_();
			setState(156);
			member();
			setState(157);
			userID();
			setState(158);
			certTS();
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SIGNATURE) {
				{
				setState(159);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterPeer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitPeer(this);
		}
	}

	public final PeerContext peer() throws RecognitionException {
		PeerContext _localctx = new PeerContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_peer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			version_();
			setState(163);
			type_();
			setState(164);
			currency_();
			setState(165);
			publicKey();
			setState(166);
			block_();
			setState(167);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIdentity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIdentity(this);
		}
	}

	public final IdentityContext identity() throws RecognitionException {
		IdentityContext _localctx = new IdentityContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_identity);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			version_();
			setState(170);
			type_();
			setState(171);
			currency_();
			setState(172);
			issuer_();
			setState(173);
			uniqueID_();
			setState(174);
			timestamp_();
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Signature_) {
				{
				setState(175);
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
		public TerminalNode DOCTYPE() { return getToken(JuniterParser.DOCTYPE, 0); }
		public DoctypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doctype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterDoctype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitDoctype(this);
		}
	}

	public final DoctypeContext doctype() throws RecognitionException {
		DoctypeContext _localctx = new DoctypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_doctype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
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
		public Token Version;
		public TerminalNode Version() { return getToken(JuniterParser.Version, 0); }
		public Version_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_version_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterVersion_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitVersion_(this);
		}
	}

	public final Version_Context version_() throws RecognitionException {
		Version_Context _localctx = new Version_Context(_ctx, getState());
		enterRule(_localctx, 16, RULE_version_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			((Version_Context)_localctx).Version = match(Version);
			System.out.println("version "+(((Version_Context)_localctx).Version!=null?((Version_Context)_localctx).Version.getText():null)+":"+";");
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
		public Token DOCTYPE;
		public TerminalNode Type_() { return getToken(JuniterParser.Type_, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode DOCTYPE() { return getToken(JuniterParser.DOCTYPE, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public Type_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterType_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitType_(this);
		}
	}

	public final Type_Context type_() throws RecognitionException {
		Type_Context _localctx = new Type_Context(_ctx, getState());
		enterRule(_localctx, 18, RULE_type_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(Type_);
			setState(184);
			match(WS);
			setState(185);
			((Type_Context)_localctx).DOCTYPE = match(DOCTYPE);
			setState(186);
			match(EOL);
			if ( ((Type_Context)_localctx).DOCTYPE!=null ) System.out.println("found a doctype");
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
		public TerminalNode Curr() { return getToken(JuniterParser.Curr, 0); }
		public Currency_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_currency_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCurrency_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCurrency_(this);
		}
	}

	public final Currency_Context currency_() throws RecognitionException {
		Currency_Context _localctx = new Currency_Context(_ctx, getState());
		enterRule(_localctx, 20, RULE_currency_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
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
		public TerminalNode Issuer_() { return getToken(JuniterParser.Issuer_, 0); }
		public IssuerContext issuer() {
			return getRuleContext(IssuerContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public Issuer_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuer_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIssuer_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIssuer_(this);
		}
	}

	public final Issuer_Context issuer_() throws RecognitionException {
		Issuer_Context _localctx = new Issuer_Context(_ctx, getState());
		enterRule(_localctx, 22, RULE_issuer_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(Issuer_);
			setState(192);
			issuer();
			setState(193);
			match(EOL);
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
		public TerminalNode Timestamp_() { return getToken(JuniterParser.Timestamp_, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public Timestamp_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_timestamp_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterTimestamp_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitTimestamp_(this);
		}
	}

	public final Timestamp_Context timestamp_() throws RecognitionException {
		Timestamp_Context _localctx = new Timestamp_Context(_ctx, getState());
		enterRule(_localctx, 24, RULE_timestamp_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			match(Timestamp_);
			setState(196);
			buid();
			setState(197);
			match(EOL);
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
		public TerminalNode UniqueIDHeader() { return getToken(JuniterParser.UniqueIDHeader, 0); }
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public UniqueID_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_uniqueID_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUniqueID_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUniqueID_(this);
		}
	}

	public final UniqueID_Context uniqueID_() throws RecognitionException {
		UniqueID_Context _localctx = new UniqueID_Context(_ctx, getState());
		enterRule(_localctx, 26, RULE_uniqueID_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(UniqueIDHeader);
			setState(200);
			match(USERID);
			setState(201);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public Block_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterBlock_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitBlock_(this);
		}
	}

	public final Block_Context block_() throws RecognitionException {
		Block_Context _localctx = new Block_Context(_ctx, getState());
		enterRule(_localctx, 28, RULE_block_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(203);
			match(T__0);
			setState(204);
			match(COLON);
			setState(205);
			match(WS);
			setState(206);
			buid();
			setState(207);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public CertTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCertTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCertTimestamp(this);
		}
	}

	public final CertTimestampContext certTimestamp() throws RecognitionException {
		CertTimestampContext _localctx = new CertTimestampContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_certTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			match(T__1);
			setState(210);
			match(COLON);
			setState(211);
			match(WS);
			setState(212);
			buid();
			setState(213);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public CertTSContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_certTS; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCertTS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCertTS(this);
		}
	}

	public final CertTSContext certTS() throws RecognitionException {
		CertTSContext _localctx = new CertTSContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_certTS);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(215);
			match(T__2);
			setState(216);
			match(COLON);
			setState(217);
			match(WS);
			setState(218);
			buid();
			setState(219);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode STR() { return getToken(JuniterParser.STR, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public CommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitComment(this);
		}
	}

	public final CommentContext comment() throws RecognitionException {
		CommentContext _localctx = new CommentContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_comment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(T__3);
			setState(222);
			match(COLON);
			setState(223);
			match(WS);
			setState(224);
			match(STR);
			setState(225);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterEndpoints(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitEndpoints(this);
		}
	}

	public final EndpointsContext endpoints() throws RecognitionException {
		EndpointsContext _localctx = new EndpointsContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_endpoints);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			match(T__4);
			setState(228);
			match(COLON);
			setState(229);
			match(EOL);
			setState(233); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(230);
				endpoint();
				setState(231);
				match(EOL);
				}
				}
				setState(235); 
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode PUBKEY() { return getToken(JuniterParser.PUBKEY, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public IdtyissuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyissuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIdtyissuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIdtyissuer(this);
		}
	}

	public final IdtyissuerContext idtyissuer() throws RecognitionException {
		IdtyissuerContext _localctx = new IdtyissuerContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_idtyissuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			match(T__5);
			setState(238);
			match(COLON);
			setState(239);
			match(WS);
			setState(240);
			match(PUBKEY);
			setState(241);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public IdtyUniqueIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyUniqueID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIdtyUniqueID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIdtyUniqueID(this);
		}
	}

	public final IdtyUniqueIDContext idtyUniqueID() throws RecognitionException {
		IdtyUniqueIDContext _localctx = new IdtyUniqueIDContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_idtyUniqueID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			match(T__6);
			setState(244);
			match(COLON);
			setState(245);
			match(WS);
			setState(246);
			match(USERID);
			setState(247);
			match(EOL);
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
		public TerminalNode IdtyTimestampHeader() { return getToken(JuniterParser.IdtyTimestampHeader, 0); }
		public BuidContext buid() {
			return getRuleContext(BuidContext.class,0);
		}
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public IdtyTimestampContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtyTimestamp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIdtyTimestamp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIdtyTimestamp(this);
		}
	}

	public final IdtyTimestampContext idtyTimestamp() throws RecognitionException {
		IdtyTimestampContext _localctx = new IdtyTimestampContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_idtyTimestamp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(IdtyTimestampHeader);
			setState(250);
			buid();
			setState(251);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode SIGNATURE() { return getToken(JuniterParser.SIGNATURE, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public IdtySignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idtySignature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIdtySignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIdtySignature(this);
		}
	}

	public final IdtySignatureContext idtySignature() throws RecognitionException {
		IdtySignatureContext _localctx = new IdtySignatureContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_idtySignature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			match(T__7);
			setState(254);
			match(COLON);
			setState(255);
			match(WS);
			setState(256);
			match(SIGNATURE);
			setState(257);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterInputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitInputs(this);
		}
	}

	public final InputsContext inputs() throws RecognitionException {
		InputsContext _localctx = new InputsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_inputs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			match(T__8);
			setState(260);
			match(COLON);
			setState(261);
			match(EOL);
			setState(265); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(262);
				input_();
				setState(263);
				match(EOL);
				}
				}
				setState(267); 
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIssuers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIssuers(this);
		}
	}

	public final IssuersContext issuers() throws RecognitionException {
		IssuersContext _localctx = new IssuersContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_issuers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(T__9);
			setState(270);
			match(COLON);
			setState(271);
			match(EOL);
			setState(275); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(272);
				issuer();
				setState(273);
				match(EOL);
				}
				}
				setState(277); 
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public LocktimeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_locktime; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterLocktime(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitLocktime(this);
		}
	}

	public final LocktimeContext locktime() throws RecognitionException {
		LocktimeContext _localctx = new LocktimeContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_locktime);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(T__6);
			setState(280);
			match(COLON);
			setState(281);
			match(WS);
			setState(282);
			match(INT);
			setState(283);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode STR() { return getToken(JuniterParser.STR, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public MemberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_member; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterMember(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitMember(this);
		}
	}

	public final MemberContext member() throws RecognitionException {
		MemberContext _localctx = new MemberContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_member);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(T__10);
			setState(286);
			match(COLON);
			setState(287);
			match(WS);
			setState(288);
			match(STR);
			setState(289);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitNumber(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(291);
			match(T__11);
			setState(292);
			match(COLON);
			setState(293);
			match(WS);
			setState(294);
			match(INT);
			setState(295);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterOutputs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitOutputs(this);
		}
	}

	public final OutputsContext outputs() throws RecognitionException {
		OutputsContext _localctx = new OutputsContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_outputs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(T__12);
			setState(298);
			match(COLON);
			setState(299);
			match(EOL);
			setState(303); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(300);
				output();
				setState(301);
				match(EOL);
				}
				}
				setState(305); 
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public PoWMinContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_poWMin; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterPoWMin(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitPoWMin(this);
		}
	}

	public final PoWMinContext poWMin() throws RecognitionException {
		PoWMinContext _localctx = new PoWMinContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_poWMin);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
			match(T__13);
			setState(308);
			match(COLON);
			setState(309);
			match(WS);
			setState(310);
			match(INT);
			setState(311);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode PUBKEY() { return getToken(JuniterParser.PUBKEY, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public PublicKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_publicKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterPublicKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitPublicKey(this);
		}
	}

	public final PublicKeyContext publicKey() throws RecognitionException {
		PublicKeyContext _localctx = new PublicKeyContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_publicKey);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			match(T__14);
			setState(314);
			match(COLON);
			setState(315);
			match(WS);
			setState(316);
			match(PUBKEY);
			setState(317);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterSignatures(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitSignatures(this);
		}
	}

	public final SignaturesContext signatures() throws RecognitionException {
		SignaturesContext _localctx = new SignaturesContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_signatures);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(T__15);
			setState(320);
			match(COLON);
			setState(321);
			match(EOL);
			setState(325); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(322);
				signature();
				setState(323);
				match(EOL);
				}
				}
				setState(327); 
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public TerminalNode WS() { return getToken(JuniterParser.WS, 0); }
		public TerminalNode USERID() { return getToken(JuniterParser.USERID, 0); }
		public TerminalNode EOL() { return getToken(JuniterParser.EOL, 0); }
		public UserIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_userID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUserID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUserID(this);
		}
	}

	public final UserIDContext userID() throws RecognitionException {
		UserIDContext _localctx = new UserIDContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_userID);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			match(T__16);
			setState(330);
			match(COLON);
			setState(331);
			match(WS);
			setState(332);
			match(USERID);
			setState(333);
			match(EOL);
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
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
		public List<TerminalNode> EOL() { return getTokens(JuniterParser.EOL); }
		public TerminalNode EOL(int i) {
			return getToken(JuniterParser.EOL, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUnlocks(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUnlocks(this);
		}
	}

	public final UnlocksContext unlocks() throws RecognitionException {
		UnlocksContext _localctx = new UnlocksContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_unlocks);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(T__17);
			setState(336);
			match(COLON);
			setState(337);
			match(EOL);
			setState(341); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(338);
				unlock();
				setState(339);
				match(EOL);
				}
				}
				setState(343); 
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
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode DASH() { return getToken(JuniterParser.DASH, 0); }
		public TerminalNode HASH() { return getToken(JuniterParser.HASH, 0); }
		public BuidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_buid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterBuid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitBuid(this);
		}
	}

	public final BuidContext buid() throws RecognitionException {
		BuidContext _localctx = new BuidContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_buid);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(INT);
			setState(346);
			match(DASH);
			setState(347);
			match(HASH);
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

	public static class DasshContext extends ParserRuleContext {
		public TerminalNode DASH() { return getToken(JuniterParser.DASH, 0); }
		public DasshContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dassh; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterDassh(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitDassh(this);
		}
	}

	public final DasshContext dassh() throws RecognitionException {
		DasshContext _localctx = new DasshContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_dassh);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			match(DASH);
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

	public static class InttContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public InttContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIntt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIntt(this);
		}
	}

	public final InttContext intt() throws RecognitionException {
		InttContext _localctx = new InttContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_intt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(351);
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

	public static class HashhContext extends ParserRuleContext {
		public TerminalNode HASH() { return getToken(JuniterParser.HASH, 0); }
		public HashhContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hashh; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterHashh(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitHashh(this);
		}
	}

	public final HashhContext hashh() throws RecognitionException {
		HashhContext _localctx = new HashhContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_hashh);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			match(HASH);
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
		public TerminalNode PUBKEY() { return getToken(JuniterParser.PUBKEY, 0); }
		public IssuerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_issuer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterIssuer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitIssuer(this);
		}
	}

	public final IssuerContext issuer() throws RecognitionException {
		IssuerContext _localctx = new IssuerContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_issuer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(355);
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
		public TerminalNode Signature_() { return getToken(JuniterParser.Signature_, 0); }
		public SignatureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_signature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterSignature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitSignature(this);
		}
	}

	public final SignatureContext signature() throws RecognitionException {
		SignatureContext _localctx = new SignatureContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_signature);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(357);
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
		public List<TerminalNode> INT() { return getTokens(JuniterParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(JuniterParser.INT, i);
		}
		public List<TerminalNode> COLON() { return getTokens(JuniterParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(JuniterParser.COLON, i);
		}
		public TerminalNode HASH() { return getToken(JuniterParser.HASH, 0); }
		public Input_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterInput_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitInput_(this);
		}
	}

	public final Input_Context input_() throws RecognitionException {
		Input_Context _localctx = new Input_Context(_ctx, getState());
		enterRule(_localctx, 80, RULE_input_);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(359);
			match(INT);
			setState(360);
			match(COLON);
			setState(361);
			match(INT);
			setState(374);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				{
				setState(362);
				match(COLON);
				setState(363);
				match(T__18);
				setState(364);
				match(COLON);
				setState(365);
				match(HASH);
				setState(366);
				match(COLON);
				setState(367);
				match(INT);
				}
				}
				break;
			case 2:
				{
				{
				setState(368);
				match(COLON);
				setState(369);
				match(T__19);
				setState(370);
				match(COLON);
				setState(371);
				match(HASH);
				setState(372);
				match(COLON);
				setState(373);
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
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode COLON() { return getToken(JuniterParser.COLON, 0); }
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUnlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUnlock(this);
		}
	}

	public final UnlockContext unlock() throws RecognitionException {
		UnlockContext _localctx = new UnlockContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_unlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(376);
			match(INT);
			setState(377);
			match(COLON);
			setState(380);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__20:
				{
				setState(378);
				unsig();
				}
				break;
			case T__21:
				{
				setState(379);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public UnsigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unsig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUnsig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUnsig(this);
		}
	}

	public final UnsigContext unsig() throws RecognitionException {
		UnsigContext _localctx = new UnsigContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_unsig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			match(T__20);
			setState(383);
			match(LP);
			setState(384);
			match(INT);
			setState(385);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public UnxhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unxhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterUnxhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitUnxhx(this);
		}
	}

	public final UnxhxContext unxhx() throws RecognitionException {
		UnxhxContext _localctx = new UnxhxContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_unxhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			match(T__21);
			setState(388);
			match(LP);
			setState(389);
			match(INT);
			setState(390);
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
		public List<TerminalNode> INT() { return getTokens(JuniterParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(JuniterParser.INT, i);
		}
		public List<TerminalNode> COLON() { return getTokens(JuniterParser.COLON); }
		public TerminalNode COLON(int i) {
			return getToken(JuniterParser.COLON, i);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitOutput(this);
		}
	}

	public final OutputContext output() throws RecognitionException {
		OutputContext _localctx = new OutputContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_output);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			match(INT);
			setState(393);
			match(COLON);
			setState(394);
			match(INT);
			setState(395);
			match(COLON);
			setState(396);
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
		public List<TerminalNode> STR() { return getTokens(JuniterParser.STR); }
		public TerminalNode STR(int i) {
			return getToken(JuniterParser.STR, i);
		}
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public EndpointContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_endpoint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterEndpoint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitEndpoint(this);
		}
	}

	public final EndpointContext endpoint() throws RecognitionException {
		EndpointContext _localctx = new EndpointContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_endpoint);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(398);
			match(STR);
			setState(401); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(399);
				match(WS);
				setState(400);
				match(STR);
				}
				}
				setState(403); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==WS );
			setState(405);
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
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCond(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		CondContext _localctx = new CondContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_cond);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(407);
				sig();
				}
				break;
			case 2:
				{
				setState(408);
				xhx();
				}
				break;
			case 3:
				{
				setState(409);
				csv();
				}
				break;
			case 4:
				{
				setState(410);
				cltv();
				}
				break;
			case 5:
				{
				setState(411);
				or();
				}
				break;
			case 6:
				{
				setState(412);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(JuniterParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(JuniterParser.WS, i);
		}
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_and; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitAnd(this);
		}
	}

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_and);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			match(LP);
			setState(416);
			cond();
			setState(417);
			match(WS);
			setState(418);
			match(T__22);
			setState(419);
			match(WS);
			setState(420);
			cond();
			setState(421);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public List<CondContext> cond() {
			return getRuleContexts(CondContext.class);
		}
		public CondContext cond(int i) {
			return getRuleContext(CondContext.class,i);
		}
		public List<TerminalNode> WS() { return getTokens(JuniterParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(JuniterParser.WS, i);
		}
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public OrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_or; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitOr(this);
		}
	}

	public final OrContext or() throws RecognitionException {
		OrContext _localctx = new OrContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_or);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(423);
			match(LP);
			setState(424);
			cond();
			setState(425);
			match(WS);
			setState(426);
			match(T__23);
			setState(427);
			match(WS);
			setState(428);
			cond();
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

	public static class SigContext extends ParserRuleContext {
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode PUBKEY() { return getToken(JuniterParser.PUBKEY, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public SigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sig; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterSig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitSig(this);
		}
	}

	public final SigContext sig() throws RecognitionException {
		SigContext _localctx = new SigContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_sig);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(431);
			match(T__20);
			setState(432);
			match(LP);
			setState(433);
			match(PUBKEY);
			setState(434);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode HASH() { return getToken(JuniterParser.HASH, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public XhxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_xhx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterXhx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitXhx(this);
		}
	}

	public final XhxContext xhx() throws RecognitionException {
		XhxContext _localctx = new XhxContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_xhx);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(436);
			match(T__21);
			setState(437);
			match(LP);
			setState(438);
			match(HASH);
			setState(439);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public CsvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_csv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCsv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCsv(this);
		}
	}

	public final CsvContext csv() throws RecognitionException {
		CsvContext _localctx = new CsvContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_csv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			match(T__24);
			setState(442);
			match(LP);
			setState(443);
			match(INT);
			setState(444);
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
		public TerminalNode LP() { return getToken(JuniterParser.LP, 0); }
		public TerminalNode INT() { return getToken(JuniterParser.INT, 0); }
		public TerminalNode RP() { return getToken(JuniterParser.RP, 0); }
		public CltvContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cltv; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).enterCltv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JuniterListener ) ((JuniterListener)listener).exitCltv(this);
		}
	}

	public final CltvContext cltv() throws RecognitionException {
		CltvContext _localctx = new CltvContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_cltv);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(446);
			match(T__25);
			setState(447);
			match(LP);
			setState(448);
			match(INT);
			setState(449);
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\67\u01c6\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\3\2\3\2\3\2\3\2\3\2\3\2\5\2s\n\2\3\2\3\2\3\2\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\5\4\u008c\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u0098"+
		"\n\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6\u00a3\n\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\b\u00b3\n\b\3\t\3\t\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3"+
		"\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\6\24\u00ec\n\24\r\24\16\24\u00ed"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\6\31\u010c\n\31\r\31\16\31\u010d\3\32\3\32\3\32\3\32\3\32\3\32\6\32\u0116"+
		"\n\32\r\32\16\32\u0117\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3"+
		"\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3"+
		"\36\6\36\u0132\n\36\r\36\16\36\u0133\3\37\3\37\3\37\3\37\3\37\3\37\3 "+
		"\3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\6!\u0148\n!\r!\16!\u0149\3\"\3\"\3\""+
		"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\6#\u0158\n#\r#\16#\u0159\3$\3$\3$\3$\3"+
		"%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*\3*"+
		"\3*\3*\5*\u0179\n*\3+\3+\3+\3+\5+\u017f\n+\3,\3,\3,\3,\3,\3-\3-\3-\3-"+
		"\3-\3.\3.\3.\3.\3.\3.\3/\3/\3/\6/\u0194\n/\r/\16/\u0195\3/\3/\3\60\3\60"+
		"\3\60\3\60\3\60\3\60\5\60\u01a0\n\60\3\61\3\61\3\61\3\61\3\61\3\61\3\61"+
		"\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\3\63"+
		"\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65\3\65\3\66\3\66\3\66\3\66"+
		"\3\66\3\66\2\2\67\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62"+
		"\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhj\2\2\2\u01a7\2r\3\2\2\2\4w\3\2\2\2\6"+
		"\u0083\3\2\2\2\b\u008d\3\2\2\2\n\u0099\3\2\2\2\f\u00a4\3\2\2\2\16\u00ab"+
		"\3\2\2\2\20\u00b4\3\2\2\2\22\u00b6\3\2\2\2\24\u00b9\3\2\2\2\26\u00bf\3"+
		"\2\2\2\30\u00c1\3\2\2\2\32\u00c5\3\2\2\2\34\u00c9\3\2\2\2\36\u00cd\3\2"+
		"\2\2 \u00d3\3\2\2\2\"\u00d9\3\2\2\2$\u00df\3\2\2\2&\u00e5\3\2\2\2(\u00ef"+
		"\3\2\2\2*\u00f5\3\2\2\2,\u00fb\3\2\2\2.\u00ff\3\2\2\2\60\u0105\3\2\2\2"+
		"\62\u010f\3\2\2\2\64\u0119\3\2\2\2\66\u011f\3\2\2\28\u0125\3\2\2\2:\u012b"+
		"\3\2\2\2<\u0135\3\2\2\2>\u013b\3\2\2\2@\u0141\3\2\2\2B\u014b\3\2\2\2D"+
		"\u0151\3\2\2\2F\u015b\3\2\2\2H\u015f\3\2\2\2J\u0161\3\2\2\2L\u0163\3\2"+
		"\2\2N\u0165\3\2\2\2P\u0167\3\2\2\2R\u0169\3\2\2\2T\u017a\3\2\2\2V\u0180"+
		"\3\2\2\2X\u0185\3\2\2\2Z\u018a\3\2\2\2\\\u0190\3\2\2\2^\u019f\3\2\2\2"+
		"`\u01a1\3\2\2\2b\u01a9\3\2\2\2d\u01b1\3\2\2\2f\u01b6\3\2\2\2h\u01bb\3"+
		"\2\2\2j\u01c0\3\2\2\2ls\5\16\b\2ms\5\n\6\2ns\5\b\5\2os\5\6\4\2ps\5\f\7"+
		"\2qs\5\4\3\2rl\3\2\2\2rm\3\2\2\2rn\3\2\2\2ro\3\2\2\2rp\3\2\2\2rq\3\2\2"+
		"\2st\3\2\2\2tu\b\2\1\2uv\7\2\2\3v\3\3\2\2\2wx\5\22\n\2xy\5\24\13\2yz\5"+
		"\26\f\2z{\7*\2\2{|\5\64\33\2|}\5\62\32\2}~\5\60\31\2~\177\5D#\2\177\u0080"+
		"\5:\36\2\u0080\u0081\5@!\2\u0081\u0082\5$\23\2\u0082\5\3\2\2\2\u0083\u0084"+
		"\5\22\n\2\u0084\u0085\5\24\13\2\u0085\u0086\5\26\f\2\u0086\u0087\5\30"+
		"\r\2\u0087\u0088\5\64\33\2\u0088\u0089\5,\27\2\u0089\u008b\5.\30\2\u008a"+
		"\u008c\5P)\2\u008b\u008a\3\2\2\2\u008b\u008c\3\2\2\2\u008c\7\3\2\2\2\u008d"+
		"\u008e\5\22\n\2\u008e\u008f\5\24\13\2\u008f\u0090\5\26\f\2\u0090\u0091"+
		"\5\30\r\2\u0091\u0092\5(\25\2\u0092\u0093\5\64\33\2\u0093\u0094\5,\27"+
		"\2\u0094\u0095\5.\30\2\u0095\u0097\5 \21\2\u0096\u0098\7-\2\2\u0097\u0096"+
		"\3\2\2\2\u0097\u0098\3\2\2\2\u0098\t\3\2\2\2\u0099\u009a\5\22\n\2\u009a"+
		"\u009b\5\24\13\2\u009b\u009c\5\26\f\2\u009c\u009d\5\30\r\2\u009d\u009e"+
		"\5\36\20\2\u009e\u009f\5\66\34\2\u009f\u00a0\5B\"\2\u00a0\u00a2\5\"\22"+
		"\2\u00a1\u00a3\7-\2\2\u00a2\u00a1\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\13"+
		"\3\2\2\2\u00a4\u00a5\5\22\n\2\u00a5\u00a6\5\24\13\2\u00a6\u00a7\5\26\f"+
		"\2\u00a7\u00a8\5> \2\u00a8\u00a9\5\36\20\2\u00a9\u00aa\5&\24\2\u00aa\r"+
		"\3\2\2\2\u00ab\u00ac\5\22\n\2\u00ac\u00ad\5\24\13\2\u00ad\u00ae\5\26\f"+
		"\2\u00ae\u00af\5\30\r\2\u00af\u00b0\5\34\17\2\u00b0\u00b2\5\32\16\2\u00b1"+
		"\u00b3\5P)\2\u00b2\u00b1\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3\17\3\2\2\2"+
		"\u00b4\u00b5\7\61\2\2\u00b5\21\3\2\2\2\u00b6\u00b7\7!\2\2\u00b7\u00b8"+
		"\b\n\1\2\u00b8\23\3\2\2\2\u00b9\u00ba\7$\2\2\u00ba\u00bb\7\64\2\2\u00bb"+
		"\u00bc\7\61\2\2\u00bc\u00bd\7\66\2\2\u00bd\u00be\b\13\1\2\u00be\25\3\2"+
		"\2\2\u00bf\u00c0\7\"\2\2\u00c0\27\3\2\2\2\u00c1\u00c2\7)\2\2\u00c2\u00c3"+
		"\5N(\2\u00c3\u00c4\7\66\2\2\u00c4\31\3\2\2\2\u00c5\u00c6\7#\2\2\u00c6"+
		"\u00c7\5F$\2\u00c7\u00c8\7\66\2\2\u00c8\33\3\2\2\2\u00c9\u00ca\7\'\2\2"+
		"\u00ca\u00cb\7\60\2\2\u00cb\u00cc\7\66\2\2\u00cc\35\3\2\2\2\u00cd\u00ce"+
		"\7\3\2\2\u00ce\u00cf\7\67\2\2\u00cf\u00d0\7\64\2\2\u00d0\u00d1\5F$\2\u00d1"+
		"\u00d2\7\66\2\2\u00d2\37\3\2\2\2\u00d3\u00d4\7\4\2\2\u00d4\u00d5\7\67"+
		"\2\2\u00d5\u00d6\7\64\2\2\u00d6\u00d7\5F$\2\u00d7\u00d8\7\66\2\2\u00d8"+
		"!\3\2\2\2\u00d9\u00da\7\5\2\2\u00da\u00db\7\67\2\2\u00db\u00dc\7\64\2"+
		"\2\u00dc\u00dd\5F$\2\u00dd\u00de\7\66\2\2\u00de#\3\2\2\2\u00df\u00e0\7"+
		"\6\2\2\u00e0\u00e1\7\67\2\2\u00e1\u00e2\7\64\2\2\u00e2\u00e3\7+\2\2\u00e3"+
		"\u00e4\7\66\2\2\u00e4%\3\2\2\2\u00e5\u00e6\7\7\2\2\u00e6\u00e7\7\67\2"+
		"\2\u00e7\u00eb\7\66\2\2\u00e8\u00e9\5\\/\2\u00e9\u00ea\7\66\2\2\u00ea"+
		"\u00ec\3\2\2\2\u00eb\u00e8\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00eb\3\2"+
		"\2\2\u00ed\u00ee\3\2\2\2\u00ee\'\3\2\2\2\u00ef\u00f0\7\b\2\2\u00f0\u00f1"+
		"\7\67\2\2\u00f1\u00f2\7\64\2\2\u00f2\u00f3\7/\2\2\u00f3\u00f4\7\66\2\2"+
		"\u00f4)\3\2\2\2\u00f5\u00f6\7\t\2\2\u00f6\u00f7\7\67\2\2\u00f7\u00f8\7"+
		"\64\2\2\u00f8\u00f9\7\60\2\2\u00f9\u00fa\7\66\2\2\u00fa+\3\2\2\2\u00fb"+
		"\u00fc\7(\2\2\u00fc\u00fd\5F$\2\u00fd\u00fe\7\66\2\2\u00fe-\3\2\2\2\u00ff"+
		"\u0100\7\n\2\2\u0100\u0101\7\67\2\2\u0101\u0102\7\64\2\2\u0102\u0103\7"+
		"-\2\2\u0103\u0104\7\66\2\2\u0104/\3\2\2\2\u0105\u0106\7\13\2\2\u0106\u0107"+
		"\7\67\2\2\u0107\u010b\7\66\2\2\u0108\u0109\5R*\2\u0109\u010a\7\66\2\2"+
		"\u010a\u010c\3\2\2\2\u010b\u0108\3\2\2\2\u010c\u010d\3\2\2\2\u010d\u010b"+
		"\3\2\2\2\u010d\u010e\3\2\2\2\u010e\61\3\2\2\2\u010f\u0110\7\f\2\2\u0110"+
		"\u0111\7\67\2\2\u0111\u0115\7\66\2\2\u0112\u0113\5N(\2\u0113\u0114\7\66"+
		"\2\2\u0114\u0116\3\2\2\2\u0115\u0112\3\2\2\2\u0116\u0117\3\2\2\2\u0117"+
		"\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\63\3\2\2\2\u0119\u011a\7\t\2"+
		"\2\u011a\u011b\7\67\2\2\u011b\u011c\7\64\2\2\u011c\u011d\7,\2\2\u011d"+
		"\u011e\7\66\2\2\u011e\65\3\2\2\2\u011f\u0120\7\r\2\2\u0120\u0121\7\67"+
		"\2\2\u0121\u0122\7\64\2\2\u0122\u0123\7+\2\2\u0123\u0124\7\66\2\2\u0124"+
		"\67\3\2\2\2\u0125\u0126\7\16\2\2\u0126\u0127\7\67\2\2\u0127\u0128\7\64"+
		"\2\2\u0128\u0129\7,\2\2\u0129\u012a\7\66\2\2\u012a9\3\2\2\2\u012b\u012c"+
		"\7\17\2\2\u012c\u012d\7\67\2\2\u012d\u0131\7\66\2\2\u012e\u012f\5Z.\2"+
		"\u012f\u0130\7\66\2\2\u0130\u0132\3\2\2\2\u0131\u012e\3\2\2\2\u0132\u0133"+
		"\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0134\3\2\2\2\u0134;\3\2\2\2\u0135"+
		"\u0136\7\20\2\2\u0136\u0137\7\67\2\2\u0137\u0138\7\64\2\2\u0138\u0139"+
		"\7,\2\2\u0139\u013a\7\66\2\2\u013a=\3\2\2\2\u013b\u013c\7\21\2\2\u013c"+
		"\u013d\7\67\2\2\u013d\u013e\7\64\2\2\u013e\u013f\7/\2\2\u013f\u0140\7"+
		"\66\2\2\u0140?\3\2\2\2\u0141\u0142\7\22\2\2\u0142\u0143\7\67\2\2\u0143"+
		"\u0147\7\66\2\2\u0144\u0145\5P)\2\u0145\u0146\7\66\2\2\u0146\u0148\3\2"+
		"\2\2\u0147\u0144\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u0147\3\2\2\2\u0149"+
		"\u014a\3\2\2\2\u014aA\3\2\2\2\u014b\u014c\7\23\2\2\u014c\u014d\7\67\2"+
		"\2\u014d\u014e\7\64\2\2\u014e\u014f\7\60\2\2\u014f\u0150\7\66\2\2\u0150"+
		"C\3\2\2\2\u0151\u0152\7\24\2\2\u0152\u0153\7\67\2\2\u0153\u0157\7\66\2"+
		"\2\u0154\u0155\5T+\2\u0155\u0156\7\66\2\2\u0156\u0158\3\2\2\2\u0157\u0154"+
		"\3\2\2\2\u0158\u0159\3\2\2\2\u0159\u0157\3\2\2\2\u0159\u015a\3\2\2\2\u015a"+
		"E\3\2\2\2\u015b\u015c\7,\2\2\u015c\u015d\7\65\2\2\u015d\u015e\7.\2\2\u015e"+
		"G\3\2\2\2\u015f\u0160\7\65\2\2\u0160I\3\2\2\2\u0161\u0162\7,\2\2\u0162"+
		"K\3\2\2\2\u0163\u0164\7.\2\2\u0164M\3\2\2\2\u0165\u0166\7/\2\2\u0166O"+
		"\3\2\2\2\u0167\u0168\7 \2\2\u0168Q\3\2\2\2\u0169\u016a\7,\2\2\u016a\u016b"+
		"\7\67\2\2\u016b\u0178\7,\2\2\u016c\u016d\7\67\2\2\u016d\u016e\7\25\2\2"+
		"\u016e\u016f\7\67\2\2\u016f\u0170\7.\2\2\u0170\u0171\7\67\2\2\u0171\u0179"+
		"\7,\2\2\u0172\u0173\7\67\2\2\u0173\u0174\7\26\2\2\u0174\u0175\7\67\2\2"+
		"\u0175\u0176\7.\2\2\u0176\u0177\7\67\2\2\u0177\u0179\7,\2\2\u0178\u016c"+
		"\3\2\2\2\u0178\u0172\3\2\2\2\u0179S\3\2\2\2\u017a\u017b\7,\2\2\u017b\u017e"+
		"\7\67\2\2\u017c\u017f\5V,\2\u017d\u017f\5X-\2\u017e\u017c\3\2\2\2\u017e"+
		"\u017d\3\2\2\2\u017fU\3\2\2\2\u0180\u0181\7\27\2\2\u0181\u0182\7\62\2"+
		"\2\u0182\u0183\7,\2\2\u0183\u0184\7\63\2\2\u0184W\3\2\2\2\u0185\u0186"+
		"\7\30\2\2\u0186\u0187\7\62\2\2\u0187\u0188\7,\2\2\u0188\u0189\7\63\2\2"+
		"\u0189Y\3\2\2\2\u018a\u018b\7,\2\2\u018b\u018c\7\67\2\2\u018c\u018d\7"+
		",\2\2\u018d\u018e\7\67\2\2\u018e\u018f\5^\60\2\u018f[\3\2\2\2\u0190\u0193"+
		"\7+\2\2\u0191\u0192\7\64\2\2\u0192\u0194\7+\2\2\u0193\u0191\3\2\2\2\u0194"+
		"\u0195\3\2\2\2\u0195\u0193\3\2\2\2\u0195\u0196\3\2\2\2\u0196\u0197\3\2"+
		"\2\2\u0197\u0198\7,\2\2\u0198]\3\2\2\2\u0199\u01a0\5d\63\2\u019a\u01a0"+
		"\5f\64\2\u019b\u01a0\5h\65\2\u019c\u01a0\5j\66\2\u019d\u01a0\5b\62\2\u019e"+
		"\u01a0\5`\61\2\u019f\u0199\3\2\2\2\u019f\u019a\3\2\2\2\u019f\u019b\3\2"+
		"\2\2\u019f\u019c\3\2\2\2\u019f\u019d\3\2\2\2\u019f\u019e\3\2\2\2\u01a0"+
		"_\3\2\2\2\u01a1\u01a2\7\62\2\2\u01a2\u01a3\5^\60\2\u01a3\u01a4\7\64\2"+
		"\2\u01a4\u01a5\7\31\2\2\u01a5\u01a6\7\64\2\2\u01a6\u01a7\5^\60\2\u01a7"+
		"\u01a8\7\63\2\2\u01a8a\3\2\2\2\u01a9\u01aa\7\62\2\2\u01aa\u01ab\5^\60"+
		"\2\u01ab\u01ac\7\64\2\2\u01ac\u01ad\7\32\2\2\u01ad\u01ae\7\64\2\2\u01ae"+
		"\u01af\5^\60\2\u01af\u01b0\7\63\2\2\u01b0c\3\2\2\2\u01b1\u01b2\7\27\2"+
		"\2\u01b2\u01b3\7\62\2\2\u01b3\u01b4\7/\2\2\u01b4\u01b5\7\63\2\2\u01b5"+
		"e\3\2\2\2\u01b6\u01b7\7\30\2\2\u01b7\u01b8\7\62\2\2\u01b8\u01b9\7.\2\2"+
		"\u01b9\u01ba\7\63\2\2\u01bag\3\2\2\2\u01bb\u01bc\7\33\2\2\u01bc\u01bd"+
		"\7\62\2\2\u01bd\u01be\7,\2\2\u01be\u01bf\7\63\2\2\u01bfi\3\2\2\2\u01c0"+
		"\u01c1\7\34\2\2\u01c1\u01c2\7\62\2\2\u01c2\u01c3\7,\2\2\u01c3\u01c4\7"+
		"\63\2\2\u01c4k\3\2\2\2\21r\u008b\u0097\u00a2\u00b2\u00ed\u010d\u0117\u0133"+
		"\u0149\u0159\u0178\u017e\u0195\u019f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}