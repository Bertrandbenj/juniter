// Generated from DUPPrimitives.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DUPPrimitives extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		STR=1, INT=2, SIGNATURE=3, HASH=4, PUBKEY=5, VERSION=6, CURRENCY=7, USERID=8, 
		DOCTYPE=9, BUID=10, LP=11, RP=12, WS=13, NL=14, COLON=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"STR", "INT", "SIGNATURE", "HASH", "PUBKEY", "VERSION", "CURRENCY", "USERID", 
		"DOCTYPE", "BUID", "BASE58", "HEX", "CHAR", "BASE64", "LP", "RP", "WS", 
		"NL", "COLON"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, "'('", 
		"')'", "' '", "'\n'", "':'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "STR", "INT", "SIGNATURE", "HASH", "PUBKEY", "VERSION", "CURRENCY", 
		"USERID", "DOCTYPE", "BUID", "LP", "RP", "WS", "NL", "COLON"
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


	public DUPPrimitives(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DUPPrimitives.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21\u00aa\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\6\2+\n\2\r\2\16\2,\3\2\3\2\6\2\61\n\2\r"+
		"\2\16\2\62\6\2\65\n\2\r\2\16\2\66\3\3\3\3\7\3;\n\3\f\3\16\3>\13\3\3\4"+
		"\6\4A\n\4\r\4\16\4B\3\5\6\5F\n\5\r\5\16\5G\3\6\6\6K\n\6\r\6\16\6L\3\7"+
		"\3\7\3\b\6\bR\n\b\r\b\16\bS\3\t\6\tW\n\t\r\t\16\tX\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\n\u0093"+
		"\n\n\3\13\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\2\2\25\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\f\27\2\31\2\33\2\35\2\37\r!\16#\17%\20\'\21\3\2"+
		"\7\3\2\63;\3\2\62;\b\2\63;CJLPR\\cmo|\4\2\62;CH\7\2--\61;C\\aac|\2\u00b3"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\37\3\2\2\2"+
		"\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\3*\3\2\2\2\58\3\2\2\2\7"+
		"@\3\2\2\2\tE\3\2\2\2\13J\3\2\2\2\rN\3\2\2\2\17Q\3\2\2\2\21V\3\2\2\2\23"+
		"\u0092\3\2\2\2\25\u0094\3\2\2\2\27\u0098\3\2\2\2\31\u009a\3\2\2\2\33\u009c"+
		"\3\2\2\2\35\u009e\3\2\2\2\37\u00a0\3\2\2\2!\u00a2\3\2\2\2#\u00a4\3\2\2"+
		"\2%\u00a6\3\2\2\2\'\u00a8\3\2\2\2)+\5\33\16\2*)\3\2\2\2+,\3\2\2\2,*\3"+
		"\2\2\2,-\3\2\2\2-\64\3\2\2\2.\60\7\"\2\2/\61\5\33\16\2\60/\3\2\2\2\61"+
		"\62\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\65\3\2\2\2\64.\3\2\2\2\65\66"+
		"\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67\4\3\2\2\28<\t\2\2\29;\t\3\2\2"+
		":9\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=\6\3\2\2\2><\3\2\2\2?A\5\35\17"+
		"\2@?\3\2\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2C\b\3\2\2\2DF\5\31\r\2ED\3\2"+
		"\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2H\n\3\2\2\2IK\5\27\f\2JI\3\2\2\2KL\3"+
		"\2\2\2LJ\3\2\2\2LM\3\2\2\2M\f\3\2\2\2NO\5\5\3\2O\16\3\2\2\2PR\5\35\17"+
		"\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\20\3\2\2\2UW\5\33\16\2VU\3"+
		"\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\22\3\2\2\2Z[\7K\2\2[\\\7f\2\2\\"+
		"]\7g\2\2]^\7p\2\2^_\7v\2\2_`\7k\2\2`a\7v\2\2a\u0093\7{\2\2bc\7E\2\2cd"+
		"\7g\2\2de\7t\2\2ef\7v\2\2fg\7k\2\2gh\7h\2\2hi\7k\2\2ij\7e\2\2jk\7c\2\2"+
		"kl\7v\2\2lm\7k\2\2mn\7q\2\2n\u0093\7p\2\2op\7O\2\2pq\7g\2\2qr\7o\2\2r"+
		"s\7d\2\2st\7g\2\2tu\7t\2\2uv\7u\2\2vw\7j\2\2wx\7k\2\2x\u0093\7r\2\2yz"+
		"\7T\2\2z{\7g\2\2{|\7x\2\2|}\7q\2\2}~\7e\2\2~\177\7c\2\2\177\u0080\7v\2"+
		"\2\u0080\u0081\7k\2\2\u0081\u0082\7q\2\2\u0082\u0093\7p\2\2\u0083\u0084"+
		"\7R\2\2\u0084\u0085\7g\2\2\u0085\u0086\7g\2\2\u0086\u0093\7t\2\2\u0087"+
		"\u0088\7V\2\2\u0088\u0089\7t\2\2\u0089\u008a\7c\2\2\u008a\u008b\7p\2\2"+
		"\u008b\u008c\7u\2\2\u008c\u008d\7c\2\2\u008d\u008e\7e\2\2\u008e\u008f"+
		"\7v\2\2\u008f\u0090\7k\2\2\u0090\u0091\7q\2\2\u0091\u0093\7p\2\2\u0092"+
		"Z\3\2\2\2\u0092b\3\2\2\2\u0092o\3\2\2\2\u0092y\3\2\2\2\u0092\u0083\3\2"+
		"\2\2\u0092\u0087\3\2\2\2\u0093\24\3\2\2\2\u0094\u0095\5\5\3\2\u0095\u0096"+
		"\7/\2\2\u0096\u0097\5\t\5\2\u0097\26\3\2\2\2\u0098\u0099\t\4\2\2\u0099"+
		"\30\3\2\2\2\u009a\u009b\t\5\2\2\u009b\32\3\2\2\2\u009c\u009d\5\35\17\2"+
		"\u009d\34\3\2\2\2\u009e\u009f\t\6\2\2\u009f\36\3\2\2\2\u00a0\u00a1\7*"+
		"\2\2\u00a1 \3\2\2\2\u00a2\u00a3\7+\2\2\u00a3\"\3\2\2\2\u00a4\u00a5\7\""+
		"\2\2\u00a5$\3\2\2\2\u00a6\u00a7\7\f\2\2\u00a7&\3\2\2\2\u00a8\u00a9\7<"+
		"\2\2\u00a9(\3\2\2\2\r\2,\62\66<BGLSX\u0092\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}