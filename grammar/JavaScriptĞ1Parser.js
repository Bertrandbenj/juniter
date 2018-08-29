// Generated from JavaScriptĞ1.g4 by ANTLR 4.7.1
// jshint ignore: start
var antlr4 = require('antlr4/index');
var JavaScriptĞ1Listener = require('./JavaScriptĞ1Listener').JavaScriptĞ1Listener;
var grammarFileName = "JavaScriptĞ1.g4";

var serializedATN = ["\u0003\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964",
    "\u00037\u01b4\u0004\u0002\t\u0002\u0004\u0003\t\u0003\u0004\u0004\t",
    "\u0004\u0004\u0005\t\u0005\u0004\u0006\t\u0006\u0004\u0007\t\u0007\u0004",
    "\b\t\b\u0004\t\t\t\u0004\n\t\n\u0004\u000b\t\u000b\u0004\f\t\f\u0004",
    "\r\t\r\u0004\u000e\t\u000e\u0004\u000f\t\u000f\u0004\u0010\t\u0010\u0004",
    "\u0011\t\u0011\u0004\u0012\t\u0012\u0004\u0013\t\u0013\u0004\u0014\t",
    "\u0014\u0004\u0015\t\u0015\u0004\u0016\t\u0016\u0004\u0017\t\u0017\u0004",
    "\u0018\t\u0018\u0004\u0019\t\u0019\u0004\u001a\t\u001a\u0004\u001b\t",
    "\u001b\u0004\u001c\t\u001c\u0004\u001d\t\u001d\u0004\u001e\t\u001e\u0004",
    "\u001f\t\u001f\u0004 \t \u0004!\t!\u0004\"\t\"\u0004#\t#\u0004$\t$\u0004",
    "%\t%\u0004&\t&\u0004\'\t\'\u0004(\t(\u0004)\t)\u0004*\t*\u0004+\t+\u0004",
    ",\t,\u0004-\t-\u0004.\t.\u0004/\t/\u00040\t0\u00041\t1\u00042\t2\u0004",
    "3\t3\u0003\u0002\u0003\u0002\u0003\u0002\u0003\u0002\u0003\u0002\u0003",
    "\u0002\u0005\u0002m\n\u0002\u0003\u0002\u0003\u0002\u0003\u0003\u0003",
    "\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003",
    "\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0003\u0004\u0003",
    "\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0003\u0004\u0003",
    "\u0004\u0005\u0004\u0085\n\u0004\u0003\u0005\u0003\u0005\u0003\u0005",
    "\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005\u0003\u0005",
    "\u0003\u0005\u0005\u0005\u0091\n\u0005\u0003\u0006\u0003\u0006\u0003",
    "\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003\u0006\u0003",
    "\u0006\u0005\u0006\u009c\n\u0006\u0003\u0007\u0003\u0007\u0003\u0007",
    "\u0003\u0007\u0003\u0007\u0003\u0007\u0003\u0007\u0003\b\u0003\b\u0003",
    "\b\u0003\b\u0003\b\u0003\b\u0003\b\u0005\b\u00ac\n\b\u0003\t\u0003\t",
    "\u0003\n\u0003\n\u0003\u000b\u0003\u000b\u0003\u000b\u0003\u000b\u0003",
    "\f\u0003\f\u0003\r\u0003\r\u0003\r\u0003\r\u0003\u000e\u0003\u000e\u0003",
    "\u000e\u0003\u000e\u0003\u000f\u0003\u000f\u0003\u000f\u0003\u000f\u0003",
    "\u0010\u0003\u0010\u0003\u0010\u0003\u0010\u0003\u0010\u0003\u0010\u0003",
    "\u0011\u0003\u0011\u0003\u0011\u0003\u0011\u0003\u0011\u0003\u0011\u0003",
    "\u0012\u0003\u0012\u0003\u0012\u0003\u0012\u0003\u0012\u0003\u0012\u0003",
    "\u0013\u0003\u0013\u0003\u0013\u0003\u0013\u0003\u0013\u0003\u0013\u0003",
    "\u0014\u0003\u0014\u0003\u0014\u0003\u0014\u0003\u0014\u0003\u0014\u0006",
    "\u0014\u00e2\n\u0014\r\u0014\u000e\u0014\u00e3\u0003\u0015\u0003\u0015",
    "\u0003\u0015\u0003\u0015\u0003\u0015\u0003\u0015\u0003\u0016\u0003\u0016",
    "\u0003\u0016\u0003\u0016\u0003\u0016\u0003\u0016\u0003\u0017\u0003\u0017",
    "\u0003\u0017\u0003\u0017\u0003\u0018\u0003\u0018\u0003\u0018\u0003\u0018",
    "\u0003\u0018\u0003\u0018\u0003\u0019\u0003\u0019\u0003\u0019\u0003\u0019",
    "\u0003\u0019\u0003\u0019\u0006\u0019\u0102\n\u0019\r\u0019\u000e\u0019",
    "\u0103\u0003\u001a\u0003\u001a\u0003\u001a\u0003\u001a\u0003\u001a\u0003",
    "\u001a\u0006\u001a\u010c\n\u001a\r\u001a\u000e\u001a\u010d\u0003\u001b",
    "\u0003\u001b\u0003\u001b\u0003\u001b\u0003\u001b\u0003\u001b\u0003\u001c",
    "\u0003\u001c\u0003\u001c\u0003\u001c\u0003\u001c\u0003\u001c\u0003\u001d",
    "\u0003\u001d\u0003\u001d\u0003\u001d\u0003\u001d\u0003\u001d\u0003\u001e",
    "\u0003\u001e\u0003\u001e\u0003\u001e\u0003\u001e\u0003\u001e\u0006\u001e",
    "\u0128\n\u001e\r\u001e\u000e\u001e\u0129\u0003\u001f\u0003\u001f\u0003",
    "\u001f\u0003\u001f\u0003\u001f\u0003\u001f\u0003 \u0003 \u0003 \u0003",
    " \u0003 \u0003 \u0003!\u0003!\u0003!\u0003!\u0003!\u0003!\u0006!\u013e",
    "\n!\r!\u000e!\u013f\u0003\"\u0003\"\u0003\"\u0003\"\u0003\"\u0003\"",
    "\u0003#\u0003#\u0003#\u0003#\u0003#\u0003#\u0006#\u014e\n#\r#\u000e",
    "#\u014f\u0003$\u0003$\u0003%\u0003%\u0003&\u0003&\u0003\'\u0003\'\u0003",
    "\'\u0003\'\u0003\'\u0003\'\u0003\'\u0003\'\u0003\'\u0003\'\u0003\'\u0003",
    "\'\u0003\'\u0003\'\u0003\'\u0005\'\u0167\n\'\u0003(\u0003(\u0003(\u0003",
    "(\u0005(\u016d\n(\u0003)\u0003)\u0003)\u0003)\u0003)\u0003*\u0003*\u0003",
    "*\u0003*\u0003*\u0003+\u0003+\u0003+\u0003+\u0003+\u0003+\u0003,\u0003",
    ",\u0003,\u0006,\u0182\n,\r,\u000e,\u0183\u0003,\u0003,\u0003-\u0003",
    "-\u0003-\u0003-\u0003-\u0003-\u0005-\u018e\n-\u0003.\u0003.\u0003.\u0003",
    ".\u0003.\u0003.\u0003.\u0003.\u0003/\u0003/\u0003/\u0003/\u0003/\u0003",
    "/\u0003/\u0003/\u00030\u00030\u00030\u00030\u00030\u00031\u00031\u0003",
    "1\u00031\u00031\u00032\u00032\u00032\u00032\u00032\u00033\u00033\u0003",
    "3\u00033\u00033\u00033\u0002\u00024\u0002\u0004\u0006\b\n\f\u000e\u0010",
    "\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLN",
    "PRTVXZ\\^`bd\u0002\u0002\u0002\u0198\u0002l\u0003\u0002\u0002\u0002",
    "\u0004p\u0003\u0002\u0002\u0002\u0006|\u0003\u0002\u0002\u0002\b\u0086",
    "\u0003\u0002\u0002\u0002\n\u0092\u0003\u0002\u0002\u0002\f\u009d\u0003",
    "\u0002\u0002\u0002\u000e\u00a4\u0003\u0002\u0002\u0002\u0010\u00ad\u0003",
    "\u0002\u0002\u0002\u0012\u00af\u0003\u0002\u0002\u0002\u0014\u00b1\u0003",
    "\u0002\u0002\u0002\u0016\u00b5\u0003\u0002\u0002\u0002\u0018\u00b7\u0003",
    "\u0002\u0002\u0002\u001a\u00bb\u0003\u0002\u0002\u0002\u001c\u00bf\u0003",
    "\u0002\u0002\u0002\u001e\u00c3\u0003\u0002\u0002\u0002 \u00c9\u0003",
    "\u0002\u0002\u0002\"\u00cf\u0003\u0002\u0002\u0002$\u00d5\u0003\u0002",
    "\u0002\u0002&\u00db\u0003\u0002\u0002\u0002(\u00e5\u0003\u0002\u0002",
    "\u0002*\u00eb\u0003\u0002\u0002\u0002,\u00f1\u0003\u0002\u0002\u0002",
    ".\u00f5\u0003\u0002\u0002\u00020\u00fb\u0003\u0002\u0002\u00022\u0105",
    "\u0003\u0002\u0002\u00024\u010f\u0003\u0002\u0002\u00026\u0115\u0003",
    "\u0002\u0002\u00028\u011b\u0003\u0002\u0002\u0002:\u0121\u0003\u0002",
    "\u0002\u0002<\u012b\u0003\u0002\u0002\u0002>\u0131\u0003\u0002\u0002",
    "\u0002@\u0137\u0003\u0002\u0002\u0002B\u0141\u0003\u0002\u0002\u0002",
    "D\u0147\u0003\u0002\u0002\u0002F\u0151\u0003\u0002\u0002\u0002H\u0153",
    "\u0003\u0002\u0002\u0002J\u0155\u0003\u0002\u0002\u0002L\u0157\u0003",
    "\u0002\u0002\u0002N\u0168\u0003\u0002\u0002\u0002P\u016e\u0003\u0002",
    "\u0002\u0002R\u0173\u0003\u0002\u0002\u0002T\u0178\u0003\u0002\u0002",
    "\u0002V\u017e\u0003\u0002\u0002\u0002X\u018d\u0003\u0002\u0002\u0002",
    "Z\u018f\u0003\u0002\u0002\u0002\\\u0197\u0003\u0002\u0002\u0002^\u019f",
    "\u0003\u0002\u0002\u0002`\u01a4\u0003\u0002\u0002\u0002b\u01a9\u0003",
    "\u0002\u0002\u0002d\u01ae\u0003\u0002\u0002\u0002fm\u0005\u000e\b\u0002",
    "gm\u0005\n\u0006\u0002hm\u0005\b\u0005\u0002im\u0005\u0006\u0004\u0002",
    "jm\u0005\f\u0007\u0002km\u0005\u0004\u0003\u0002lf\u0003\u0002\u0002",
    "\u0002lg\u0003\u0002\u0002\u0002lh\u0003\u0002\u0002\u0002li\u0003\u0002",
    "\u0002\u0002lj\u0003\u0002\u0002\u0002lk\u0003\u0002\u0002\u0002mn\u0003",
    "\u0002\u0002\u0002no\u0007\u0002\u0002\u0003o\u0003\u0003\u0002\u0002",
    "\u0002pq\u0005\u0012\n\u0002qr\u0005\u0014\u000b\u0002rs\u0005\u0016",
    "\f\u0002st\u0007!\u0002\u0002tu\u00054\u001b\u0002uv\u00052\u001a\u0002",
    "vw\u00050\u0019\u0002wx\u0005D#\u0002xy\u0005:\u001e\u0002yz\u0005@",
    "!\u0002z{\u0005$\u0013\u0002{\u0005\u0003\u0002\u0002\u0002|}\u0005",
    "\u0012\n\u0002}~\u0005\u0014\u000b\u0002~\u007f\u0005\u0016\f\u0002",
    "\u007f\u0080\u0005\u0018\r\u0002\u0080\u0081\u00054\u001b\u0002\u0081",
    "\u0082\u0005,\u0017\u0002\u0082\u0084\u0005.\u0018\u0002\u0083\u0085",
    "\u0005J&\u0002\u0084\u0083\u0003\u0002\u0002\u0002\u0084\u0085\u0003",
    "\u0002\u0002\u0002\u0085\u0007\u0003\u0002\u0002\u0002\u0086\u0087\u0005",
    "\u0012\n\u0002\u0087\u0088\u0005\u0014\u000b\u0002\u0088\u0089\u0005",
    "\u0016\f\u0002\u0089\u008a\u0005\u0018\r\u0002\u008a\u008b\u0005(\u0015",
    "\u0002\u008b\u008c\u00054\u001b\u0002\u008c\u008d\u0005,\u0017\u0002",
    "\u008d\u008e\u0005.\u0018\u0002\u008e\u0090\u0005 \u0011\u0002\u008f",
    "\u0091\u0007-\u0002\u0002\u0090\u008f\u0003\u0002\u0002\u0002\u0090",
    "\u0091\u0003\u0002\u0002\u0002\u0091\t\u0003\u0002\u0002\u0002\u0092",
    "\u0093\u0005\u0012\n\u0002\u0093\u0094\u0005\u0014\u000b\u0002\u0094",
    "\u0095\u0005\u0016\f\u0002\u0095\u0096\u0005\u0018\r\u0002\u0096\u0097",
    "\u0005\u001e\u0010\u0002\u0097\u0098\u00056\u001c\u0002\u0098\u0099",
    "\u0005B\"\u0002\u0099\u009b\u0005\"\u0012\u0002\u009a\u009c\u0007-\u0002",
    "\u0002\u009b\u009a\u0003\u0002\u0002\u0002\u009b\u009c\u0003\u0002\u0002",
    "\u0002\u009c\u000b\u0003\u0002\u0002\u0002\u009d\u009e\u0005\u0012\n",
    "\u0002\u009e\u009f\u0005\u0014\u000b\u0002\u009f\u00a0\u0005\u0016\f",
    "\u0002\u00a0\u00a1\u0005> \u0002\u00a1\u00a2\u0005\u001e\u0010\u0002",
    "\u00a2\u00a3\u0005&\u0014\u0002\u00a3\r\u0003\u0002\u0002\u0002\u00a4",
    "\u00a5\u0005\u0012\n\u0002\u00a5\u00a6\u0005\u0014\u000b\u0002\u00a6",
    "\u00a7\u0005\u0016\f\u0002\u00a7\u00a8\u0005\u0018\r\u0002\u00a8\u00a9",
    "\u0005\u001c\u000f\u0002\u00a9\u00ab\u0005\u001a\u000e\u0002\u00aa\u00ac",
    "\u0005J&\u0002\u00ab\u00aa\u0003\u0002\u0002\u0002\u00ab\u00ac\u0003",
    "\u0002\u0002\u0002\u00ac\u000f\u0003\u0002\u0002\u0002\u00ad\u00ae\u0007",
    "1\u0002\u0002\u00ae\u0011\u0003\u0002\u0002\u0002\u00af\u00b0\u0007",
    "\u001f\u0002\u0002\u00b0\u0013\u0003\u0002\u0002\u0002\u00b1\u00b2\u0007",
    "%\u0002\u0002\u00b2\u00b3\u0005\u0010\t\u0002\u00b3\u00b4\u00076\u0002",
    "\u0002\u00b4\u0015\u0003\u0002\u0002\u0002\u00b5\u00b6\u0007 \u0002",
    "\u0002\u00b6\u0017\u0003\u0002\u0002\u0002\u00b7\u00b8\u0007*\u0002",
    "\u0002\u00b8\u00b9\u0005H%\u0002\u00b9\u00ba\u00076\u0002\u0002\u00ba",
    "\u0019\u0003\u0002\u0002\u0002\u00bb\u00bc\u0007$\u0002\u0002\u00bc",
    "\u00bd\u0005F$\u0002\u00bd\u00be\u00076\u0002\u0002\u00be\u001b\u0003",
    "\u0002\u0002\u0002\u00bf\u00c0\u0007(\u0002\u0002\u00c0\u00c1\u0007",
    "0\u0002\u0002\u00c1\u00c2\u00076\u0002\u0002\u00c2\u001d\u0003\u0002",
    "\u0002\u0002\u00c3\u00c4\u0007\u0003\u0002\u0002\u00c4\u00c5\u00077",
    "\u0002\u0002\u00c5\u00c6\u00075\u0002\u0002\u00c6\u00c7\u0005F$\u0002",
    "\u00c7\u00c8\u00076\u0002\u0002\u00c8\u001f\u0003\u0002\u0002\u0002",
    "\u00c9\u00ca\u0007\u0004\u0002\u0002\u00ca\u00cb\u00077\u0002\u0002",
    "\u00cb\u00cc\u00075\u0002\u0002\u00cc\u00cd\u0005F$\u0002\u00cd\u00ce",
    "\u00076\u0002\u0002\u00ce!\u0003\u0002\u0002\u0002\u00cf\u00d0\u0007",
    "\u0005\u0002\u0002\u00d0\u00d1\u00077\u0002\u0002\u00d1\u00d2\u0007",
    "5\u0002\u0002\u00d2\u00d3\u0005F$\u0002\u00d3\u00d4\u00076\u0002\u0002",
    "\u00d4#\u0003\u0002\u0002\u0002\u00d5\u00d6\u0007\u0006\u0002\u0002",
    "\u00d6\u00d7\u00077\u0002\u0002\u00d7\u00d8\u00075\u0002\u0002\u00d8",
    "\u00d9\u0007+\u0002\u0002\u00d9\u00da\u00076\u0002\u0002\u00da%\u0003",
    "\u0002\u0002\u0002\u00db\u00dc\u0007\u0007\u0002\u0002\u00dc\u00dd\u0007",
    "7\u0002\u0002\u00dd\u00e1\u00076\u0002\u0002\u00de\u00df\u0005V,\u0002",
    "\u00df\u00e0\u00076\u0002\u0002\u00e0\u00e2\u0003\u0002\u0002\u0002",
    "\u00e1\u00de\u0003\u0002\u0002\u0002\u00e2\u00e3\u0003\u0002\u0002\u0002",
    "\u00e3\u00e1\u0003\u0002\u0002\u0002\u00e3\u00e4\u0003\u0002\u0002\u0002",
    "\u00e4\'\u0003\u0002\u0002\u0002\u00e5\u00e6\u0007\b\u0002\u0002\u00e6",
    "\u00e7\u00077\u0002\u0002\u00e7\u00e8\u00075\u0002\u0002\u00e8\u00e9",
    "\u0007/\u0002\u0002\u00e9\u00ea\u00076\u0002\u0002\u00ea)\u0003\u0002",
    "\u0002\u0002\u00eb\u00ec\u0007\t\u0002\u0002\u00ec\u00ed\u00077\u0002",
    "\u0002\u00ed\u00ee\u00075\u0002\u0002\u00ee\u00ef\u00070\u0002\u0002",
    "\u00ef\u00f0\u00076\u0002\u0002\u00f0+\u0003\u0002\u0002\u0002\u00f1",
    "\u00f2\u0007)\u0002\u0002\u00f2\u00f3\u0005F$\u0002\u00f3\u00f4\u0007",
    "6\u0002\u0002\u00f4-\u0003\u0002\u0002\u0002\u00f5\u00f6\u0007\n\u0002",
    "\u0002\u00f6\u00f7\u00077\u0002\u0002\u00f7\u00f8\u00075\u0002\u0002",
    "\u00f8\u00f9\u0007-\u0002\u0002\u00f9\u00fa\u00076\u0002\u0002\u00fa",
    "/\u0003\u0002\u0002\u0002\u00fb\u00fc\u0007\u000b\u0002\u0002\u00fc",
    "\u00fd\u00077\u0002\u0002\u00fd\u0101\u00076\u0002\u0002\u00fe\u00ff",
    "\u0005L\'\u0002\u00ff\u0100\u00076\u0002\u0002\u0100\u0102\u0003\u0002",
    "\u0002\u0002\u0101\u00fe\u0003\u0002\u0002\u0002\u0102\u0103\u0003\u0002",
    "\u0002\u0002\u0103\u0101\u0003\u0002\u0002\u0002\u0103\u0104\u0003\u0002",
    "\u0002\u0002\u01041\u0003\u0002\u0002\u0002\u0105\u0106\u0007\f\u0002",
    "\u0002\u0106\u0107\u00077\u0002\u0002\u0107\u010b\u00076\u0002\u0002",
    "\u0108\u0109\u0005H%\u0002\u0109\u010a\u00076\u0002\u0002\u010a\u010c",
    "\u0003\u0002\u0002\u0002\u010b\u0108\u0003\u0002\u0002\u0002\u010c\u010d",
    "\u0003\u0002\u0002\u0002\u010d\u010b\u0003\u0002\u0002\u0002\u010d\u010e",
    "\u0003\u0002\u0002\u0002\u010e3\u0003\u0002\u0002\u0002\u010f\u0110",
    "\u0007\t\u0002\u0002\u0110\u0111\u00077\u0002\u0002\u0111\u0112\u0007",
    "5\u0002\u0002\u0112\u0113\u0007,\u0002\u0002\u0113\u0114\u00076\u0002",
    "\u0002\u01145\u0003\u0002\u0002\u0002\u0115\u0116\u0007\r\u0002\u0002",
    "\u0116\u0117\u00077\u0002\u0002\u0117\u0118\u00075\u0002\u0002\u0118",
    "\u0119\u0007+\u0002\u0002\u0119\u011a\u00076\u0002\u0002\u011a7\u0003",
    "\u0002\u0002\u0002\u011b\u011c\u0007\u000e\u0002\u0002\u011c\u011d\u0007",
    "7\u0002\u0002\u011d\u011e\u00075\u0002\u0002\u011e\u011f\u0007,\u0002",
    "\u0002\u011f\u0120\u00076\u0002\u0002\u01209\u0003\u0002\u0002\u0002",
    "\u0121\u0122\u0007\u000f\u0002\u0002\u0122\u0123\u00077\u0002\u0002",
    "\u0123\u0127\u00076\u0002\u0002\u0124\u0125\u0005T+\u0002\u0125\u0126",
    "\u00076\u0002\u0002\u0126\u0128\u0003\u0002\u0002\u0002\u0127\u0124",
    "\u0003\u0002\u0002\u0002\u0128\u0129\u0003\u0002\u0002\u0002\u0129\u0127",
    "\u0003\u0002\u0002\u0002\u0129\u012a\u0003\u0002\u0002\u0002\u012a;",
    "\u0003\u0002\u0002\u0002\u012b\u012c\u0007\u0010\u0002\u0002\u012c\u012d",
    "\u00077\u0002\u0002\u012d\u012e\u00075\u0002\u0002\u012e\u012f\u0007",
    ",\u0002\u0002\u012f\u0130\u00076\u0002\u0002\u0130=\u0003\u0002\u0002",
    "\u0002\u0131\u0132\u0007\u0011\u0002\u0002\u0132\u0133\u00077\u0002",
    "\u0002\u0133\u0134\u00075\u0002\u0002\u0134\u0135\u0007/\u0002\u0002",
    "\u0135\u0136\u00076\u0002\u0002\u0136?\u0003\u0002\u0002\u0002\u0137",
    "\u0138\u0007\u0012\u0002\u0002\u0138\u0139\u00077\u0002\u0002\u0139",
    "\u013d\u00076\u0002\u0002\u013a\u013b\u0005J&\u0002\u013b\u013c\u0007",
    "6\u0002\u0002\u013c\u013e\u0003\u0002\u0002\u0002\u013d\u013a\u0003",
    "\u0002\u0002\u0002\u013e\u013f\u0003\u0002\u0002\u0002\u013f\u013d\u0003",
    "\u0002\u0002\u0002\u013f\u0140\u0003\u0002\u0002\u0002\u0140A\u0003",
    "\u0002\u0002\u0002\u0141\u0142\u0007\u0013\u0002\u0002\u0142\u0143\u0007",
    "7\u0002\u0002\u0143\u0144\u00075\u0002\u0002\u0144\u0145\u00070\u0002",
    "\u0002\u0145\u0146\u00076\u0002\u0002\u0146C\u0003\u0002\u0002\u0002",
    "\u0147\u0148\u0007\u0014\u0002\u0002\u0148\u0149\u00077\u0002\u0002",
    "\u0149\u014d\u00076\u0002\u0002\u014a\u014b\u0005N(\u0002\u014b\u014c",
    "\u00076\u0002\u0002\u014c\u014e\u0003\u0002\u0002\u0002\u014d\u014a",
    "\u0003\u0002\u0002\u0002\u014e\u014f\u0003\u0002\u0002\u0002\u014f\u014d",
    "\u0003\u0002\u0002\u0002\u014f\u0150\u0003\u0002\u0002\u0002\u0150E",
    "\u0003\u0002\u0002\u0002\u0151\u0152\u00072\u0002\u0002\u0152G\u0003",
    "\u0002\u0002\u0002\u0153\u0154\u0007/\u0002\u0002\u0154I\u0003\u0002",
    "\u0002\u0002\u0155\u0156\u0007#\u0002\u0002\u0156K\u0003\u0002\u0002",
    "\u0002\u0157\u0158\u0007,\u0002\u0002\u0158\u0159\u00077\u0002\u0002",
    "\u0159\u0166\u0007,\u0002\u0002\u015a\u015b\u00077\u0002\u0002\u015b",
    "\u015c\u0007\u0015\u0002\u0002\u015c\u015d\u00077\u0002\u0002\u015d",
    "\u015e\u0007.\u0002\u0002\u015e\u015f\u00077\u0002\u0002\u015f\u0167",
    "\u0007,\u0002\u0002\u0160\u0161\u00077\u0002\u0002\u0161\u0162\u0007",
    "\u0016\u0002\u0002\u0162\u0163\u00077\u0002\u0002\u0163\u0164\u0007",
    ".\u0002\u0002\u0164\u0165\u00077\u0002\u0002\u0165\u0167\u0007,\u0002",
    "\u0002\u0166\u015a\u0003\u0002\u0002\u0002\u0166\u0160\u0003\u0002\u0002",
    "\u0002\u0167M\u0003\u0002\u0002\u0002\u0168\u0169\u0007,\u0002\u0002",
    "\u0169\u016c\u00077\u0002\u0002\u016a\u016d\u0005P)\u0002\u016b\u016d",
    "\u0005R*\u0002\u016c\u016a\u0003\u0002\u0002\u0002\u016c\u016b\u0003",
    "\u0002\u0002\u0002\u016dO\u0003\u0002\u0002\u0002\u016e\u016f\u0007",
    "\u0017\u0002\u0002\u016f\u0170\u00073\u0002\u0002\u0170\u0171\u0007",
    ",\u0002\u0002\u0171\u0172\u00074\u0002\u0002\u0172Q\u0003\u0002\u0002",
    "\u0002\u0173\u0174\u0007\u0018\u0002\u0002\u0174\u0175\u00073\u0002",
    "\u0002\u0175\u0176\u0007,\u0002\u0002\u0176\u0177\u00074\u0002\u0002",
    "\u0177S\u0003\u0002\u0002\u0002\u0178\u0179\u0007,\u0002\u0002\u0179",
    "\u017a\u00077\u0002\u0002\u017a\u017b\u0007,\u0002\u0002\u017b\u017c",
    "\u00077\u0002\u0002\u017c\u017d\u0005X-\u0002\u017dU\u0003\u0002\u0002",
    "\u0002\u017e\u0181\u0007+\u0002\u0002\u017f\u0180\u00075\u0002\u0002",
    "\u0180\u0182\u0007+\u0002\u0002\u0181\u017f\u0003\u0002\u0002\u0002",
    "\u0182\u0183\u0003\u0002\u0002\u0002\u0183\u0181\u0003\u0002\u0002\u0002",
    "\u0183\u0184\u0003\u0002\u0002\u0002\u0184\u0185\u0003\u0002\u0002\u0002",
    "\u0185\u0186\u0007,\u0002\u0002\u0186W\u0003\u0002\u0002\u0002\u0187",
    "\u018e\u0005^0\u0002\u0188\u018e\u0005`1\u0002\u0189\u018e\u0005b2\u0002",
    "\u018a\u018e\u0005d3\u0002\u018b\u018e\u0005\\/\u0002\u018c\u018e\u0005",
    "Z.\u0002\u018d\u0187\u0003\u0002\u0002\u0002\u018d\u0188\u0003\u0002",
    "\u0002\u0002\u018d\u0189\u0003\u0002\u0002\u0002\u018d\u018a\u0003\u0002",
    "\u0002\u0002\u018d\u018b\u0003\u0002\u0002\u0002\u018d\u018c\u0003\u0002",
    "\u0002\u0002\u018eY\u0003\u0002\u0002\u0002\u018f\u0190\u00073\u0002",
    "\u0002\u0190\u0191\u0005X-\u0002\u0191\u0192\u00075\u0002\u0002\u0192",
    "\u0193\u0007\u0019\u0002\u0002\u0193\u0194\u00075\u0002\u0002\u0194",
    "\u0195\u0005X-\u0002\u0195\u0196\u00074\u0002\u0002\u0196[\u0003\u0002",
    "\u0002\u0002\u0197\u0198\u00073\u0002\u0002\u0198\u0199\u0005X-\u0002",
    "\u0199\u019a\u00075\u0002\u0002\u019a\u019b\u0007\u001a\u0002\u0002",
    "\u019b\u019c\u00075\u0002\u0002\u019c\u019d\u0005X-\u0002\u019d\u019e",
    "\u00074\u0002\u0002\u019e]\u0003\u0002\u0002\u0002\u019f\u01a0\u0007",
    "\u0017\u0002\u0002\u01a0\u01a1\u00073\u0002\u0002\u01a1\u01a2\u0007",
    "/\u0002\u0002\u01a2\u01a3\u00074\u0002\u0002\u01a3_\u0003\u0002\u0002",
    "\u0002\u01a4\u01a5\u0007\u0018\u0002\u0002\u01a5\u01a6\u00073\u0002",
    "\u0002\u01a6\u01a7\u0007.\u0002\u0002\u01a7\u01a8\u00074\u0002\u0002",
    "\u01a8a\u0003\u0002\u0002\u0002\u01a9\u01aa\u0007\u001b\u0002\u0002",
    "\u01aa\u01ab\u00073\u0002\u0002\u01ab\u01ac\u0007,\u0002\u0002\u01ac",
    "\u01ad\u00074\u0002\u0002\u01adc\u0003\u0002\u0002\u0002\u01ae\u01af",
    "\u0007\u001c\u0002\u0002\u01af\u01b0\u00073\u0002\u0002\u01b0\u01b1",
    "\u0007,\u0002\u0002\u01b1\u01b2\u00074\u0002\u0002\u01b2e\u0003\u0002",
    "\u0002\u0002\u0011l\u0084\u0090\u009b\u00ab\u00e3\u0103\u010d\u0129",
    "\u013f\u014f\u0166\u016c\u0183\u018d"].join("");


var atn = new antlr4.atn.ATNDeserializer().deserialize(serializedATN);

var decisionsToDFA = atn.decisionToState.map( function(ds, index) { return new antlr4.dfa.DFA(ds, index); });

var sharedContextCache = new antlr4.PredictionContextCache();

var literalNames = [ null, "'Block'", "'CertTimestamp'", "'CertTS'", "'Comment'", 
                     "'Endpoints'", "'Idtyissuer'", "'Locktime'", "'IdtySignature'", 
                     "'Inputs'", "'Issuers'", "'Membership'", "'Number'", 
                     "'Outputs'", "'PoWMin'", "'PublicKey'", "'Signatures'", 
                     "'UserID'", "'Unlocks'", "'D'", "'T'", "'SIG'", "'XHX'", 
                     "'&&'", "'||'", "'CSV'", "'CLTV'", "'10'", "'g1'", 
                     null, null, null, null, null, null, null, null, null, 
                     null, null, null, null, null, null, null, null, null, 
                     null, null, "'('", "')'", "' '", "'\n'", "':'" ];

var symbolicNames = [ null, null, null, null, null, null, null, null, null, 
                      null, null, null, null, null, null, null, null, null, 
                      null, null, null, null, null, null, null, null, null, 
                      "VERSION", "CURRENCY", "Version", "Curr", "Blockstamp", 
                      "Iss_", "Signature_", "Timestamp_", "Type_", "Currency_", 
                      "VersionHeader", "UniqueIDHeader", "IdtyTimestampHeader", 
                      "Issuer_", "STR", "INT", "SIGNATURE", "HASH", "PUBKEY", 
                      "USERID", "DOCTYPE", "BUID", "LP", "RP", "WS", "NL", 
                      "COLON" ];

var ruleNames =  [ "document", "transaction", "revocation", "certification", 
                   "membership", "peer", "identity", "doctype", "version_", 
                   "type_", "currency_", "issuer_", "timestamp_", "uniqueID_", 
                   "block_", "certTimestamp", "certTS", "comment", "endpoints", 
                   "idtyissuer", "idtyUniqueID", "idtyTimestamp", "idtySignature", 
                   "inputs", "issuers", "locktime", "member", "number", 
                   "outputs", "poWMin", "publicKey", "signatures", "userID", 
                   "unlocks", "buid", "issuer", "signature", "input_", "unlock", 
                   "unsig", "unxhx", "output", "endpoint", "cond", "and", 
                   "or", "sig", "xhx", "csv", "cltv" ];

function JavaScriptĞ1Parser (input) {
	antlr4.Parser.call(this, input);
    this._interp = new antlr4.atn.ParserATNSimulator(this, atn, decisionsToDFA, sharedContextCache);
    this.ruleNames = ruleNames;
    this.literalNames = literalNames;
    this.symbolicNames = symbolicNames;
    return this;
}

JavaScriptĞ1Parser.prototype = Object.create(antlr4.Parser.prototype);
JavaScriptĞ1Parser.prototype.constructor = JavaScriptĞ1Parser;

Object.defineProperty(JavaScriptĞ1Parser.prototype, "atn", {
	get : function() {
		return atn;
	}
});

JavaScriptĞ1Parser.EOF = antlr4.Token.EOF;
JavaScriptĞ1Parser.T__0 = 1;
JavaScriptĞ1Parser.T__1 = 2;
JavaScriptĞ1Parser.T__2 = 3;
JavaScriptĞ1Parser.T__3 = 4;
JavaScriptĞ1Parser.T__4 = 5;
JavaScriptĞ1Parser.T__5 = 6;
JavaScriptĞ1Parser.T__6 = 7;
JavaScriptĞ1Parser.T__7 = 8;
JavaScriptĞ1Parser.T__8 = 9;
JavaScriptĞ1Parser.T__9 = 10;
JavaScriptĞ1Parser.T__10 = 11;
JavaScriptĞ1Parser.T__11 = 12;
JavaScriptĞ1Parser.T__12 = 13;
JavaScriptĞ1Parser.T__13 = 14;
JavaScriptĞ1Parser.T__14 = 15;
JavaScriptĞ1Parser.T__15 = 16;
JavaScriptĞ1Parser.T__16 = 17;
JavaScriptĞ1Parser.T__17 = 18;
JavaScriptĞ1Parser.T__18 = 19;
JavaScriptĞ1Parser.T__19 = 20;
JavaScriptĞ1Parser.T__20 = 21;
JavaScriptĞ1Parser.T__21 = 22;
JavaScriptĞ1Parser.T__22 = 23;
JavaScriptĞ1Parser.T__23 = 24;
JavaScriptĞ1Parser.T__24 = 25;
JavaScriptĞ1Parser.T__25 = 26;
JavaScriptĞ1Parser.VERSION = 27;
JavaScriptĞ1Parser.CURRENCY = 28;
JavaScriptĞ1Parser.Version = 29;
JavaScriptĞ1Parser.Curr = 30;
JavaScriptĞ1Parser.Blockstamp = 31;
JavaScriptĞ1Parser.Iss_ = 32;
JavaScriptĞ1Parser.Signature_ = 33;
JavaScriptĞ1Parser.Timestamp_ = 34;
JavaScriptĞ1Parser.Type_ = 35;
JavaScriptĞ1Parser.Currency_ = 36;
JavaScriptĞ1Parser.VersionHeader = 37;
JavaScriptĞ1Parser.UniqueIDHeader = 38;
JavaScriptĞ1Parser.IdtyTimestampHeader = 39;
JavaScriptĞ1Parser.Issuer_ = 40;
JavaScriptĞ1Parser.STR = 41;
JavaScriptĞ1Parser.INT = 42;
JavaScriptĞ1Parser.SIGNATURE = 43;
JavaScriptĞ1Parser.HASH = 44;
JavaScriptĞ1Parser.PUBKEY = 45;
JavaScriptĞ1Parser.USERID = 46;
JavaScriptĞ1Parser.DOCTYPE = 47;
JavaScriptĞ1Parser.BUID = 48;
JavaScriptĞ1Parser.LP = 49;
JavaScriptĞ1Parser.RP = 50;
JavaScriptĞ1Parser.WS = 51;
JavaScriptĞ1Parser.NL = 52;
JavaScriptĞ1Parser.COLON = 53;

JavaScriptĞ1Parser.RULE_document = 0;
JavaScriptĞ1Parser.RULE_transaction = 1;
JavaScriptĞ1Parser.RULE_revocation = 2;
JavaScriptĞ1Parser.RULE_certification = 3;
JavaScriptĞ1Parser.RULE_membership = 4;
JavaScriptĞ1Parser.RULE_peer = 5;
JavaScriptĞ1Parser.RULE_identity = 6;
JavaScriptĞ1Parser.RULE_doctype = 7;
JavaScriptĞ1Parser.RULE_version_ = 8;
JavaScriptĞ1Parser.RULE_type_ = 9;
JavaScriptĞ1Parser.RULE_currency_ = 10;
JavaScriptĞ1Parser.RULE_issuer_ = 11;
JavaScriptĞ1Parser.RULE_timestamp_ = 12;
JavaScriptĞ1Parser.RULE_uniqueID_ = 13;
JavaScriptĞ1Parser.RULE_block_ = 14;
JavaScriptĞ1Parser.RULE_certTimestamp = 15;
JavaScriptĞ1Parser.RULE_certTS = 16;
JavaScriptĞ1Parser.RULE_comment = 17;
JavaScriptĞ1Parser.RULE_endpoints = 18;
JavaScriptĞ1Parser.RULE_idtyissuer = 19;
JavaScriptĞ1Parser.RULE_idtyUniqueID = 20;
JavaScriptĞ1Parser.RULE_idtyTimestamp = 21;
JavaScriptĞ1Parser.RULE_idtySignature = 22;
JavaScriptĞ1Parser.RULE_inputs = 23;
JavaScriptĞ1Parser.RULE_issuers = 24;
JavaScriptĞ1Parser.RULE_locktime = 25;
JavaScriptĞ1Parser.RULE_member = 26;
JavaScriptĞ1Parser.RULE_number = 27;
JavaScriptĞ1Parser.RULE_outputs = 28;
JavaScriptĞ1Parser.RULE_poWMin = 29;
JavaScriptĞ1Parser.RULE_publicKey = 30;
JavaScriptĞ1Parser.RULE_signatures = 31;
JavaScriptĞ1Parser.RULE_userID = 32;
JavaScriptĞ1Parser.RULE_unlocks = 33;
JavaScriptĞ1Parser.RULE_buid = 34;
JavaScriptĞ1Parser.RULE_issuer = 35;
JavaScriptĞ1Parser.RULE_signature = 36;
JavaScriptĞ1Parser.RULE_input_ = 37;
JavaScriptĞ1Parser.RULE_unlock = 38;
JavaScriptĞ1Parser.RULE_unsig = 39;
JavaScriptĞ1Parser.RULE_unxhx = 40;
JavaScriptĞ1Parser.RULE_output = 41;
JavaScriptĞ1Parser.RULE_endpoint = 42;
JavaScriptĞ1Parser.RULE_cond = 43;
JavaScriptĞ1Parser.RULE_and = 44;
JavaScriptĞ1Parser.RULE_or = 45;
JavaScriptĞ1Parser.RULE_sig = 46;
JavaScriptĞ1Parser.RULE_xhx = 47;
JavaScriptĞ1Parser.RULE_csv = 48;
JavaScriptĞ1Parser.RULE_cltv = 49;

function DocumentContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_document;
    return this;
}

DocumentContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
DocumentContext.prototype.constructor = DocumentContext;

DocumentContext.prototype.EOF = function() {
    return this.getToken(JavaScriptĞ1Parser.EOF, 0);
};

DocumentContext.prototype.identity = function() {
    return this.getTypedRuleContext(IdentityContext,0);
};

DocumentContext.prototype.membership = function() {
    return this.getTypedRuleContext(MembershipContext,0);
};

DocumentContext.prototype.certification = function() {
    return this.getTypedRuleContext(CertificationContext,0);
};

DocumentContext.prototype.revocation = function() {
    return this.getTypedRuleContext(RevocationContext,0);
};

DocumentContext.prototype.peer = function() {
    return this.getTypedRuleContext(PeerContext,0);
};

DocumentContext.prototype.transaction = function() {
    return this.getTypedRuleContext(TransactionContext,0);
};

DocumentContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterDocument(this);
	}
};

DocumentContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitDocument(this);
	}
};




JavaScriptĞ1Parser.DocumentContext = DocumentContext;

JavaScriptĞ1Parser.prototype.document = function() {

    var localctx = new DocumentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 0, JavaScriptĞ1Parser.RULE_document);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 106;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,0,this._ctx);
        switch(la_) {
        case 1:
            this.state = 100;
            this.identity();
            break;

        case 2:
            this.state = 101;
            this.membership();
            break;

        case 3:
            this.state = 102;
            this.certification();
            break;

        case 4:
            this.state = 103;
            this.revocation();
            break;

        case 5:
            this.state = 104;
            this.peer();
            break;

        case 6:
            this.state = 105;
            this.transaction();
            break;

        }
        this.state = 108;
        this.match(JavaScriptĞ1Parser.EOF);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function TransactionContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_transaction;
    return this;
}

TransactionContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
TransactionContext.prototype.constructor = TransactionContext;

TransactionContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

TransactionContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

TransactionContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

TransactionContext.prototype.Blockstamp = function() {
    return this.getToken(JavaScriptĞ1Parser.Blockstamp, 0);
};

TransactionContext.prototype.locktime = function() {
    return this.getTypedRuleContext(LocktimeContext,0);
};

TransactionContext.prototype.issuers = function() {
    return this.getTypedRuleContext(IssuersContext,0);
};

TransactionContext.prototype.inputs = function() {
    return this.getTypedRuleContext(InputsContext,0);
};

TransactionContext.prototype.unlocks = function() {
    return this.getTypedRuleContext(UnlocksContext,0);
};

TransactionContext.prototype.outputs = function() {
    return this.getTypedRuleContext(OutputsContext,0);
};

TransactionContext.prototype.signatures = function() {
    return this.getTypedRuleContext(SignaturesContext,0);
};

TransactionContext.prototype.comment = function() {
    return this.getTypedRuleContext(CommentContext,0);
};

TransactionContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterTransaction(this);
	}
};

TransactionContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitTransaction(this);
	}
};




JavaScriptĞ1Parser.TransactionContext = TransactionContext;

JavaScriptĞ1Parser.prototype.transaction = function() {

    var localctx = new TransactionContext(this, this._ctx, this.state);
    this.enterRule(localctx, 2, JavaScriptĞ1Parser.RULE_transaction);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 110;
        this.version_();
        this.state = 111;
        this.type_();
        this.state = 112;
        this.currency_();
        this.state = 113;
        this.match(JavaScriptĞ1Parser.Blockstamp);
        this.state = 114;
        this.locktime();
        this.state = 115;
        this.issuers();
        this.state = 116;
        this.inputs();
        this.state = 117;
        this.unlocks();
        this.state = 118;
        this.outputs();
        this.state = 119;
        this.signatures();
        this.state = 120;
        this.comment();
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function RevocationContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_revocation;
    return this;
}

RevocationContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
RevocationContext.prototype.constructor = RevocationContext;

RevocationContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

RevocationContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

RevocationContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

RevocationContext.prototype.issuer_ = function() {
    return this.getTypedRuleContext(Issuer_Context,0);
};

RevocationContext.prototype.locktime = function() {
    return this.getTypedRuleContext(LocktimeContext,0);
};

RevocationContext.prototype.idtyTimestamp = function() {
    return this.getTypedRuleContext(IdtyTimestampContext,0);
};

RevocationContext.prototype.idtySignature = function() {
    return this.getTypedRuleContext(IdtySignatureContext,0);
};

RevocationContext.prototype.signature = function() {
    return this.getTypedRuleContext(SignatureContext,0);
};

RevocationContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterRevocation(this);
	}
};

RevocationContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitRevocation(this);
	}
};




JavaScriptĞ1Parser.RevocationContext = RevocationContext;

JavaScriptĞ1Parser.prototype.revocation = function() {

    var localctx = new RevocationContext(this, this._ctx, this.state);
    this.enterRule(localctx, 4, JavaScriptĞ1Parser.RULE_revocation);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 122;
        this.version_();
        this.state = 123;
        this.type_();
        this.state = 124;
        this.currency_();
        this.state = 125;
        this.issuer_();
        this.state = 126;
        this.locktime();
        this.state = 127;
        this.idtyTimestamp();
        this.state = 128;
        this.idtySignature();
        this.state = 130;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===JavaScriptĞ1Parser.Signature_) {
            this.state = 129;
            this.signature();
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CertificationContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_certification;
    return this;
}

CertificationContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CertificationContext.prototype.constructor = CertificationContext;

CertificationContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

CertificationContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

CertificationContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

CertificationContext.prototype.issuer_ = function() {
    return this.getTypedRuleContext(Issuer_Context,0);
};

CertificationContext.prototype.idtyissuer = function() {
    return this.getTypedRuleContext(IdtyissuerContext,0);
};

CertificationContext.prototype.locktime = function() {
    return this.getTypedRuleContext(LocktimeContext,0);
};

CertificationContext.prototype.idtyTimestamp = function() {
    return this.getTypedRuleContext(IdtyTimestampContext,0);
};

CertificationContext.prototype.idtySignature = function() {
    return this.getTypedRuleContext(IdtySignatureContext,0);
};

CertificationContext.prototype.certTimestamp = function() {
    return this.getTypedRuleContext(CertTimestampContext,0);
};

CertificationContext.prototype.SIGNATURE = function() {
    return this.getToken(JavaScriptĞ1Parser.SIGNATURE, 0);
};

CertificationContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCertification(this);
	}
};

CertificationContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCertification(this);
	}
};




JavaScriptĞ1Parser.CertificationContext = CertificationContext;

JavaScriptĞ1Parser.prototype.certification = function() {

    var localctx = new CertificationContext(this, this._ctx, this.state);
    this.enterRule(localctx, 6, JavaScriptĞ1Parser.RULE_certification);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 132;
        this.version_();
        this.state = 133;
        this.type_();
        this.state = 134;
        this.currency_();
        this.state = 135;
        this.issuer_();
        this.state = 136;
        this.idtyissuer();
        this.state = 137;
        this.locktime();
        this.state = 138;
        this.idtyTimestamp();
        this.state = 139;
        this.idtySignature();
        this.state = 140;
        this.certTimestamp();
        this.state = 142;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===JavaScriptĞ1Parser.SIGNATURE) {
            this.state = 141;
            this.match(JavaScriptĞ1Parser.SIGNATURE);
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function MembershipContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_membership;
    return this;
}

MembershipContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
MembershipContext.prototype.constructor = MembershipContext;

MembershipContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

MembershipContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

MembershipContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

MembershipContext.prototype.issuer_ = function() {
    return this.getTypedRuleContext(Issuer_Context,0);
};

MembershipContext.prototype.block_ = function() {
    return this.getTypedRuleContext(Block_Context,0);
};

MembershipContext.prototype.member = function() {
    return this.getTypedRuleContext(MemberContext,0);
};

MembershipContext.prototype.userID = function() {
    return this.getTypedRuleContext(UserIDContext,0);
};

MembershipContext.prototype.certTS = function() {
    return this.getTypedRuleContext(CertTSContext,0);
};

MembershipContext.prototype.SIGNATURE = function() {
    return this.getToken(JavaScriptĞ1Parser.SIGNATURE, 0);
};

MembershipContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterMembership(this);
	}
};

MembershipContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitMembership(this);
	}
};




JavaScriptĞ1Parser.MembershipContext = MembershipContext;

JavaScriptĞ1Parser.prototype.membership = function() {

    var localctx = new MembershipContext(this, this._ctx, this.state);
    this.enterRule(localctx, 8, JavaScriptĞ1Parser.RULE_membership);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 144;
        this.version_();
        this.state = 145;
        this.type_();
        this.state = 146;
        this.currency_();
        this.state = 147;
        this.issuer_();
        this.state = 148;
        this.block_();
        this.state = 149;
        this.member();
        this.state = 150;
        this.userID();
        this.state = 151;
        this.certTS();
        this.state = 153;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===JavaScriptĞ1Parser.SIGNATURE) {
            this.state = 152;
            this.match(JavaScriptĞ1Parser.SIGNATURE);
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function PeerContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_peer;
    return this;
}

PeerContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
PeerContext.prototype.constructor = PeerContext;

PeerContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

PeerContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

PeerContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

PeerContext.prototype.publicKey = function() {
    return this.getTypedRuleContext(PublicKeyContext,0);
};

PeerContext.prototype.block_ = function() {
    return this.getTypedRuleContext(Block_Context,0);
};

PeerContext.prototype.endpoints = function() {
    return this.getTypedRuleContext(EndpointsContext,0);
};

PeerContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterPeer(this);
	}
};

PeerContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitPeer(this);
	}
};




JavaScriptĞ1Parser.PeerContext = PeerContext;

JavaScriptĞ1Parser.prototype.peer = function() {

    var localctx = new PeerContext(this, this._ctx, this.state);
    this.enterRule(localctx, 10, JavaScriptĞ1Parser.RULE_peer);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 155;
        this.version_();
        this.state = 156;
        this.type_();
        this.state = 157;
        this.currency_();
        this.state = 158;
        this.publicKey();
        this.state = 159;
        this.block_();
        this.state = 160;
        this.endpoints();
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IdentityContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_identity;
    return this;
}

IdentityContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IdentityContext.prototype.constructor = IdentityContext;

IdentityContext.prototype.version_ = function() {
    return this.getTypedRuleContext(Version_Context,0);
};

IdentityContext.prototype.type_ = function() {
    return this.getTypedRuleContext(Type_Context,0);
};

IdentityContext.prototype.currency_ = function() {
    return this.getTypedRuleContext(Currency_Context,0);
};

IdentityContext.prototype.issuer_ = function() {
    return this.getTypedRuleContext(Issuer_Context,0);
};

IdentityContext.prototype.uniqueID_ = function() {
    return this.getTypedRuleContext(UniqueID_Context,0);
};

IdentityContext.prototype.timestamp_ = function() {
    return this.getTypedRuleContext(Timestamp_Context,0);
};

IdentityContext.prototype.signature = function() {
    return this.getTypedRuleContext(SignatureContext,0);
};

IdentityContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIdentity(this);
	}
};

IdentityContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIdentity(this);
	}
};




JavaScriptĞ1Parser.IdentityContext = IdentityContext;

JavaScriptĞ1Parser.prototype.identity = function() {

    var localctx = new IdentityContext(this, this._ctx, this.state);
    this.enterRule(localctx, 12, JavaScriptĞ1Parser.RULE_identity);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 162;
        this.version_();
        this.state = 163;
        this.type_();
        this.state = 164;
        this.currency_();
        this.state = 165;
        this.issuer_();
        this.state = 166;
        this.uniqueID_();
        this.state = 167;
        this.timestamp_();
        this.state = 169;
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        if(_la===JavaScriptĞ1Parser.Signature_) {
            this.state = 168;
            this.signature();
        }

    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function DoctypeContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_doctype;
    return this;
}

DoctypeContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
DoctypeContext.prototype.constructor = DoctypeContext;

DoctypeContext.prototype.DOCTYPE = function() {
    return this.getToken(JavaScriptĞ1Parser.DOCTYPE, 0);
};

DoctypeContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterDoctype(this);
	}
};

DoctypeContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitDoctype(this);
	}
};




JavaScriptĞ1Parser.DoctypeContext = DoctypeContext;

JavaScriptĞ1Parser.prototype.doctype = function() {

    var localctx = new DoctypeContext(this, this._ctx, this.state);
    this.enterRule(localctx, 14, JavaScriptĞ1Parser.RULE_doctype);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 171;
        this.match(JavaScriptĞ1Parser.DOCTYPE);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Version_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_version_;
    return this;
}

Version_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Version_Context.prototype.constructor = Version_Context;

Version_Context.prototype.Version = function() {
    return this.getToken(JavaScriptĞ1Parser.Version, 0);
};

Version_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterVersion_(this);
	}
};

Version_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitVersion_(this);
	}
};




JavaScriptĞ1Parser.Version_Context = Version_Context;

JavaScriptĞ1Parser.prototype.version_ = function() {

    var localctx = new Version_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 16, JavaScriptĞ1Parser.RULE_version_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 173;
        this.match(JavaScriptĞ1Parser.Version);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Type_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_type_;
    return this;
}

Type_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Type_Context.prototype.constructor = Type_Context;

Type_Context.prototype.Type_ = function() {
    return this.getToken(JavaScriptĞ1Parser.Type_, 0);
};

Type_Context.prototype.doctype = function() {
    return this.getTypedRuleContext(DoctypeContext,0);
};

Type_Context.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

Type_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterType_(this);
	}
};

Type_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitType_(this);
	}
};




JavaScriptĞ1Parser.Type_Context = Type_Context;

JavaScriptĞ1Parser.prototype.type_ = function() {

    var localctx = new Type_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 18, JavaScriptĞ1Parser.RULE_type_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 175;
        this.match(JavaScriptĞ1Parser.Type_);
        this.state = 176;
        this.doctype();
        this.state = 177;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Currency_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_currency_;
    return this;
}

Currency_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Currency_Context.prototype.constructor = Currency_Context;

Currency_Context.prototype.Curr = function() {
    return this.getToken(JavaScriptĞ1Parser.Curr, 0);
};

Currency_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCurrency_(this);
	}
};

Currency_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCurrency_(this);
	}
};




JavaScriptĞ1Parser.Currency_Context = Currency_Context;

JavaScriptĞ1Parser.prototype.currency_ = function() {

    var localctx = new Currency_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 20, JavaScriptĞ1Parser.RULE_currency_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 179;
        this.match(JavaScriptĞ1Parser.Curr);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Issuer_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_issuer_;
    return this;
}

Issuer_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Issuer_Context.prototype.constructor = Issuer_Context;

Issuer_Context.prototype.Issuer_ = function() {
    return this.getToken(JavaScriptĞ1Parser.Issuer_, 0);
};

Issuer_Context.prototype.issuer = function() {
    return this.getTypedRuleContext(IssuerContext,0);
};

Issuer_Context.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

Issuer_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIssuer_(this);
	}
};

Issuer_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIssuer_(this);
	}
};




JavaScriptĞ1Parser.Issuer_Context = Issuer_Context;

JavaScriptĞ1Parser.prototype.issuer_ = function() {

    var localctx = new Issuer_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 22, JavaScriptĞ1Parser.RULE_issuer_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 181;
        this.match(JavaScriptĞ1Parser.Issuer_);
        this.state = 182;
        this.issuer();
        this.state = 183;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Timestamp_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_timestamp_;
    return this;
}

Timestamp_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Timestamp_Context.prototype.constructor = Timestamp_Context;

Timestamp_Context.prototype.Timestamp_ = function() {
    return this.getToken(JavaScriptĞ1Parser.Timestamp_, 0);
};

Timestamp_Context.prototype.buid = function() {
    return this.getTypedRuleContext(BuidContext,0);
};

Timestamp_Context.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

Timestamp_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterTimestamp_(this);
	}
};

Timestamp_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitTimestamp_(this);
	}
};




JavaScriptĞ1Parser.Timestamp_Context = Timestamp_Context;

JavaScriptĞ1Parser.prototype.timestamp_ = function() {

    var localctx = new Timestamp_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 24, JavaScriptĞ1Parser.RULE_timestamp_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 185;
        this.match(JavaScriptĞ1Parser.Timestamp_);
        this.state = 186;
        this.buid();
        this.state = 187;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UniqueID_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_uniqueID_;
    return this;
}

UniqueID_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UniqueID_Context.prototype.constructor = UniqueID_Context;

UniqueID_Context.prototype.UniqueIDHeader = function() {
    return this.getToken(JavaScriptĞ1Parser.UniqueIDHeader, 0);
};

UniqueID_Context.prototype.USERID = function() {
    return this.getToken(JavaScriptĞ1Parser.USERID, 0);
};

UniqueID_Context.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

UniqueID_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUniqueID_(this);
	}
};

UniqueID_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUniqueID_(this);
	}
};




JavaScriptĞ1Parser.UniqueID_Context = UniqueID_Context;

JavaScriptĞ1Parser.prototype.uniqueID_ = function() {

    var localctx = new UniqueID_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 26, JavaScriptĞ1Parser.RULE_uniqueID_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 189;
        this.match(JavaScriptĞ1Parser.UniqueIDHeader);
        this.state = 190;
        this.match(JavaScriptĞ1Parser.USERID);
        this.state = 191;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Block_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_block_;
    return this;
}

Block_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Block_Context.prototype.constructor = Block_Context;

Block_Context.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

Block_Context.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

Block_Context.prototype.buid = function() {
    return this.getTypedRuleContext(BuidContext,0);
};

Block_Context.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

Block_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterBlock_(this);
	}
};

Block_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitBlock_(this);
	}
};




JavaScriptĞ1Parser.Block_Context = Block_Context;

JavaScriptĞ1Parser.prototype.block_ = function() {

    var localctx = new Block_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 28, JavaScriptĞ1Parser.RULE_block_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 193;
        this.match(JavaScriptĞ1Parser.T__0);
        this.state = 194;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 195;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 196;
        this.buid();
        this.state = 197;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CertTimestampContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_certTimestamp;
    return this;
}

CertTimestampContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CertTimestampContext.prototype.constructor = CertTimestampContext;

CertTimestampContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

CertTimestampContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

CertTimestampContext.prototype.buid = function() {
    return this.getTypedRuleContext(BuidContext,0);
};

CertTimestampContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

CertTimestampContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCertTimestamp(this);
	}
};

CertTimestampContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCertTimestamp(this);
	}
};




JavaScriptĞ1Parser.CertTimestampContext = CertTimestampContext;

JavaScriptĞ1Parser.prototype.certTimestamp = function() {

    var localctx = new CertTimestampContext(this, this._ctx, this.state);
    this.enterRule(localctx, 30, JavaScriptĞ1Parser.RULE_certTimestamp);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 199;
        this.match(JavaScriptĞ1Parser.T__1);
        this.state = 200;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 201;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 202;
        this.buid();
        this.state = 203;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CertTSContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_certTS;
    return this;
}

CertTSContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CertTSContext.prototype.constructor = CertTSContext;

CertTSContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

CertTSContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

CertTSContext.prototype.buid = function() {
    return this.getTypedRuleContext(BuidContext,0);
};

CertTSContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

CertTSContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCertTS(this);
	}
};

CertTSContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCertTS(this);
	}
};




JavaScriptĞ1Parser.CertTSContext = CertTSContext;

JavaScriptĞ1Parser.prototype.certTS = function() {

    var localctx = new CertTSContext(this, this._ctx, this.state);
    this.enterRule(localctx, 32, JavaScriptĞ1Parser.RULE_certTS);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 205;
        this.match(JavaScriptĞ1Parser.T__2);
        this.state = 206;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 207;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 208;
        this.buid();
        this.state = 209;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CommentContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_comment;
    return this;
}

CommentContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CommentContext.prototype.constructor = CommentContext;

CommentContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

CommentContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

CommentContext.prototype.STR = function() {
    return this.getToken(JavaScriptĞ1Parser.STR, 0);
};

CommentContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

CommentContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterComment(this);
	}
};

CommentContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitComment(this);
	}
};




JavaScriptĞ1Parser.CommentContext = CommentContext;

JavaScriptĞ1Parser.prototype.comment = function() {

    var localctx = new CommentContext(this, this._ctx, this.state);
    this.enterRule(localctx, 34, JavaScriptĞ1Parser.RULE_comment);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 211;
        this.match(JavaScriptĞ1Parser.T__3);
        this.state = 212;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 213;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 214;
        this.match(JavaScriptĞ1Parser.STR);
        this.state = 215;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function EndpointsContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_endpoints;
    return this;
}

EndpointsContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
EndpointsContext.prototype.constructor = EndpointsContext;

EndpointsContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

EndpointsContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


EndpointsContext.prototype.endpoint = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(EndpointContext);
    } else {
        return this.getTypedRuleContext(EndpointContext,i);
    }
};

EndpointsContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterEndpoints(this);
	}
};

EndpointsContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitEndpoints(this);
	}
};




JavaScriptĞ1Parser.EndpointsContext = EndpointsContext;

JavaScriptĞ1Parser.prototype.endpoints = function() {

    var localctx = new EndpointsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 36, JavaScriptĞ1Parser.RULE_endpoints);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 217;
        this.match(JavaScriptĞ1Parser.T__4);
        this.state = 218;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 219;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 223; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 220;
            this.endpoint();
            this.state = 221;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 225; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.STR);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IdtyissuerContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_idtyissuer;
    return this;
}

IdtyissuerContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IdtyissuerContext.prototype.constructor = IdtyissuerContext;

IdtyissuerContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

IdtyissuerContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

IdtyissuerContext.prototype.PUBKEY = function() {
    return this.getToken(JavaScriptĞ1Parser.PUBKEY, 0);
};

IdtyissuerContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

IdtyissuerContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIdtyissuer(this);
	}
};

IdtyissuerContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIdtyissuer(this);
	}
};




JavaScriptĞ1Parser.IdtyissuerContext = IdtyissuerContext;

JavaScriptĞ1Parser.prototype.idtyissuer = function() {

    var localctx = new IdtyissuerContext(this, this._ctx, this.state);
    this.enterRule(localctx, 38, JavaScriptĞ1Parser.RULE_idtyissuer);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 227;
        this.match(JavaScriptĞ1Parser.T__5);
        this.state = 228;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 229;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 230;
        this.match(JavaScriptĞ1Parser.PUBKEY);
        this.state = 231;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IdtyUniqueIDContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_idtyUniqueID;
    return this;
}

IdtyUniqueIDContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IdtyUniqueIDContext.prototype.constructor = IdtyUniqueIDContext;

IdtyUniqueIDContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

IdtyUniqueIDContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

IdtyUniqueIDContext.prototype.USERID = function() {
    return this.getToken(JavaScriptĞ1Parser.USERID, 0);
};

IdtyUniqueIDContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

IdtyUniqueIDContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIdtyUniqueID(this);
	}
};

IdtyUniqueIDContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIdtyUniqueID(this);
	}
};




JavaScriptĞ1Parser.IdtyUniqueIDContext = IdtyUniqueIDContext;

JavaScriptĞ1Parser.prototype.idtyUniqueID = function() {

    var localctx = new IdtyUniqueIDContext(this, this._ctx, this.state);
    this.enterRule(localctx, 40, JavaScriptĞ1Parser.RULE_idtyUniqueID);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 233;
        this.match(JavaScriptĞ1Parser.T__6);
        this.state = 234;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 235;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 236;
        this.match(JavaScriptĞ1Parser.USERID);
        this.state = 237;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IdtyTimestampContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_idtyTimestamp;
    return this;
}

IdtyTimestampContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IdtyTimestampContext.prototype.constructor = IdtyTimestampContext;

IdtyTimestampContext.prototype.IdtyTimestampHeader = function() {
    return this.getToken(JavaScriptĞ1Parser.IdtyTimestampHeader, 0);
};

IdtyTimestampContext.prototype.buid = function() {
    return this.getTypedRuleContext(BuidContext,0);
};

IdtyTimestampContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

IdtyTimestampContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIdtyTimestamp(this);
	}
};

IdtyTimestampContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIdtyTimestamp(this);
	}
};




JavaScriptĞ1Parser.IdtyTimestampContext = IdtyTimestampContext;

JavaScriptĞ1Parser.prototype.idtyTimestamp = function() {

    var localctx = new IdtyTimestampContext(this, this._ctx, this.state);
    this.enterRule(localctx, 42, JavaScriptĞ1Parser.RULE_idtyTimestamp);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 239;
        this.match(JavaScriptĞ1Parser.IdtyTimestampHeader);
        this.state = 240;
        this.buid();
        this.state = 241;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IdtySignatureContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_idtySignature;
    return this;
}

IdtySignatureContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IdtySignatureContext.prototype.constructor = IdtySignatureContext;

IdtySignatureContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

IdtySignatureContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

IdtySignatureContext.prototype.SIGNATURE = function() {
    return this.getToken(JavaScriptĞ1Parser.SIGNATURE, 0);
};

IdtySignatureContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

IdtySignatureContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIdtySignature(this);
	}
};

IdtySignatureContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIdtySignature(this);
	}
};




JavaScriptĞ1Parser.IdtySignatureContext = IdtySignatureContext;

JavaScriptĞ1Parser.prototype.idtySignature = function() {

    var localctx = new IdtySignatureContext(this, this._ctx, this.state);
    this.enterRule(localctx, 44, JavaScriptĞ1Parser.RULE_idtySignature);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 243;
        this.match(JavaScriptĞ1Parser.T__7);
        this.state = 244;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 245;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 246;
        this.match(JavaScriptĞ1Parser.SIGNATURE);
        this.state = 247;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function InputsContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_inputs;
    return this;
}

InputsContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
InputsContext.prototype.constructor = InputsContext;

InputsContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

InputsContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


InputsContext.prototype.input_ = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(Input_Context);
    } else {
        return this.getTypedRuleContext(Input_Context,i);
    }
};

InputsContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterInputs(this);
	}
};

InputsContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitInputs(this);
	}
};




JavaScriptĞ1Parser.InputsContext = InputsContext;

JavaScriptĞ1Parser.prototype.inputs = function() {

    var localctx = new InputsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 46, JavaScriptĞ1Parser.RULE_inputs);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 249;
        this.match(JavaScriptĞ1Parser.T__8);
        this.state = 250;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 251;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 255; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 252;
            this.input_();
            this.state = 253;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 257; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.INT);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IssuersContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_issuers;
    return this;
}

IssuersContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IssuersContext.prototype.constructor = IssuersContext;

IssuersContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

IssuersContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


IssuersContext.prototype.issuer = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(IssuerContext);
    } else {
        return this.getTypedRuleContext(IssuerContext,i);
    }
};

IssuersContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIssuers(this);
	}
};

IssuersContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIssuers(this);
	}
};




JavaScriptĞ1Parser.IssuersContext = IssuersContext;

JavaScriptĞ1Parser.prototype.issuers = function() {

    var localctx = new IssuersContext(this, this._ctx, this.state);
    this.enterRule(localctx, 48, JavaScriptĞ1Parser.RULE_issuers);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 259;
        this.match(JavaScriptĞ1Parser.T__9);
        this.state = 260;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 261;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 265; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 262;
            this.issuer();
            this.state = 263;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 267; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.PUBKEY);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function LocktimeContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_locktime;
    return this;
}

LocktimeContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
LocktimeContext.prototype.constructor = LocktimeContext;

LocktimeContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

LocktimeContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

LocktimeContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

LocktimeContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

LocktimeContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterLocktime(this);
	}
};

LocktimeContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitLocktime(this);
	}
};




JavaScriptĞ1Parser.LocktimeContext = LocktimeContext;

JavaScriptĞ1Parser.prototype.locktime = function() {

    var localctx = new LocktimeContext(this, this._ctx, this.state);
    this.enterRule(localctx, 50, JavaScriptĞ1Parser.RULE_locktime);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 269;
        this.match(JavaScriptĞ1Parser.T__6);
        this.state = 270;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 271;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 272;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 273;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function MemberContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_member;
    return this;
}

MemberContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
MemberContext.prototype.constructor = MemberContext;

MemberContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

MemberContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

MemberContext.prototype.STR = function() {
    return this.getToken(JavaScriptĞ1Parser.STR, 0);
};

MemberContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

MemberContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterMember(this);
	}
};

MemberContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitMember(this);
	}
};




JavaScriptĞ1Parser.MemberContext = MemberContext;

JavaScriptĞ1Parser.prototype.member = function() {

    var localctx = new MemberContext(this, this._ctx, this.state);
    this.enterRule(localctx, 52, JavaScriptĞ1Parser.RULE_member);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 275;
        this.match(JavaScriptĞ1Parser.T__10);
        this.state = 276;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 277;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 278;
        this.match(JavaScriptĞ1Parser.STR);
        this.state = 279;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function NumberContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_number;
    return this;
}

NumberContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
NumberContext.prototype.constructor = NumberContext;

NumberContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

NumberContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

NumberContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

NumberContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

NumberContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterNumber(this);
	}
};

NumberContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitNumber(this);
	}
};




JavaScriptĞ1Parser.NumberContext = NumberContext;

JavaScriptĞ1Parser.prototype.number = function() {

    var localctx = new NumberContext(this, this._ctx, this.state);
    this.enterRule(localctx, 54, JavaScriptĞ1Parser.RULE_number);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 281;
        this.match(JavaScriptĞ1Parser.T__11);
        this.state = 282;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 283;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 284;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 285;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function OutputsContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_outputs;
    return this;
}

OutputsContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
OutputsContext.prototype.constructor = OutputsContext;

OutputsContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

OutputsContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


OutputsContext.prototype.output = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(OutputContext);
    } else {
        return this.getTypedRuleContext(OutputContext,i);
    }
};

OutputsContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterOutputs(this);
	}
};

OutputsContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitOutputs(this);
	}
};




JavaScriptĞ1Parser.OutputsContext = OutputsContext;

JavaScriptĞ1Parser.prototype.outputs = function() {

    var localctx = new OutputsContext(this, this._ctx, this.state);
    this.enterRule(localctx, 56, JavaScriptĞ1Parser.RULE_outputs);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 287;
        this.match(JavaScriptĞ1Parser.T__12);
        this.state = 288;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 289;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 293; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 290;
            this.output();
            this.state = 291;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 295; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.INT);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function PoWMinContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_poWMin;
    return this;
}

PoWMinContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
PoWMinContext.prototype.constructor = PoWMinContext;

PoWMinContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

PoWMinContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

PoWMinContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

PoWMinContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

PoWMinContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterPoWMin(this);
	}
};

PoWMinContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitPoWMin(this);
	}
};




JavaScriptĞ1Parser.PoWMinContext = PoWMinContext;

JavaScriptĞ1Parser.prototype.poWMin = function() {

    var localctx = new PoWMinContext(this, this._ctx, this.state);
    this.enterRule(localctx, 58, JavaScriptĞ1Parser.RULE_poWMin);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 297;
        this.match(JavaScriptĞ1Parser.T__13);
        this.state = 298;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 299;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 300;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 301;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function PublicKeyContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_publicKey;
    return this;
}

PublicKeyContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
PublicKeyContext.prototype.constructor = PublicKeyContext;

PublicKeyContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

PublicKeyContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

PublicKeyContext.prototype.PUBKEY = function() {
    return this.getToken(JavaScriptĞ1Parser.PUBKEY, 0);
};

PublicKeyContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

PublicKeyContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterPublicKey(this);
	}
};

PublicKeyContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitPublicKey(this);
	}
};




JavaScriptĞ1Parser.PublicKeyContext = PublicKeyContext;

JavaScriptĞ1Parser.prototype.publicKey = function() {

    var localctx = new PublicKeyContext(this, this._ctx, this.state);
    this.enterRule(localctx, 60, JavaScriptĞ1Parser.RULE_publicKey);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 303;
        this.match(JavaScriptĞ1Parser.T__14);
        this.state = 304;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 305;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 306;
        this.match(JavaScriptĞ1Parser.PUBKEY);
        this.state = 307;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function SignaturesContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_signatures;
    return this;
}

SignaturesContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
SignaturesContext.prototype.constructor = SignaturesContext;

SignaturesContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

SignaturesContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


SignaturesContext.prototype.signature = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(SignatureContext);
    } else {
        return this.getTypedRuleContext(SignatureContext,i);
    }
};

SignaturesContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterSignatures(this);
	}
};

SignaturesContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitSignatures(this);
	}
};




JavaScriptĞ1Parser.SignaturesContext = SignaturesContext;

JavaScriptĞ1Parser.prototype.signatures = function() {

    var localctx = new SignaturesContext(this, this._ctx, this.state);
    this.enterRule(localctx, 62, JavaScriptĞ1Parser.RULE_signatures);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 309;
        this.match(JavaScriptĞ1Parser.T__15);
        this.state = 310;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 311;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 315; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 312;
            this.signature();
            this.state = 313;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 317; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.Signature_);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UserIDContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_userID;
    return this;
}

UserIDContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UserIDContext.prototype.constructor = UserIDContext;

UserIDContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

UserIDContext.prototype.WS = function() {
    return this.getToken(JavaScriptĞ1Parser.WS, 0);
};

UserIDContext.prototype.USERID = function() {
    return this.getToken(JavaScriptĞ1Parser.USERID, 0);
};

UserIDContext.prototype.NL = function() {
    return this.getToken(JavaScriptĞ1Parser.NL, 0);
};

UserIDContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUserID(this);
	}
};

UserIDContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUserID(this);
	}
};




JavaScriptĞ1Parser.UserIDContext = UserIDContext;

JavaScriptĞ1Parser.prototype.userID = function() {

    var localctx = new UserIDContext(this, this._ctx, this.state);
    this.enterRule(localctx, 64, JavaScriptĞ1Parser.RULE_userID);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 319;
        this.match(JavaScriptĞ1Parser.T__16);
        this.state = 320;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 321;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 322;
        this.match(JavaScriptĞ1Parser.USERID);
        this.state = 323;
        this.match(JavaScriptĞ1Parser.NL);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UnlocksContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_unlocks;
    return this;
}

UnlocksContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UnlocksContext.prototype.constructor = UnlocksContext;

UnlocksContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

UnlocksContext.prototype.NL = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.NL);
    } else {
        return this.getToken(JavaScriptĞ1Parser.NL, i);
    }
};


UnlocksContext.prototype.unlock = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(UnlockContext);
    } else {
        return this.getTypedRuleContext(UnlockContext,i);
    }
};

UnlocksContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUnlocks(this);
	}
};

UnlocksContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUnlocks(this);
	}
};




JavaScriptĞ1Parser.UnlocksContext = UnlocksContext;

JavaScriptĞ1Parser.prototype.unlocks = function() {

    var localctx = new UnlocksContext(this, this._ctx, this.state);
    this.enterRule(localctx, 66, JavaScriptĞ1Parser.RULE_unlocks);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 325;
        this.match(JavaScriptĞ1Parser.T__17);
        this.state = 326;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 327;
        this.match(JavaScriptĞ1Parser.NL);
        this.state = 331; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 328;
            this.unlock();
            this.state = 329;
            this.match(JavaScriptĞ1Parser.NL);
            this.state = 333; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.INT);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function BuidContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_buid;
    return this;
}

BuidContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
BuidContext.prototype.constructor = BuidContext;

BuidContext.prototype.BUID = function() {
    return this.getToken(JavaScriptĞ1Parser.BUID, 0);
};

BuidContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterBuid(this);
	}
};

BuidContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitBuid(this);
	}
};




JavaScriptĞ1Parser.BuidContext = BuidContext;

JavaScriptĞ1Parser.prototype.buid = function() {

    var localctx = new BuidContext(this, this._ctx, this.state);
    this.enterRule(localctx, 68, JavaScriptĞ1Parser.RULE_buid);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 335;
        this.match(JavaScriptĞ1Parser.BUID);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function IssuerContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_issuer;
    return this;
}

IssuerContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
IssuerContext.prototype.constructor = IssuerContext;

IssuerContext.prototype.PUBKEY = function() {
    return this.getToken(JavaScriptĞ1Parser.PUBKEY, 0);
};

IssuerContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterIssuer(this);
	}
};

IssuerContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitIssuer(this);
	}
};




JavaScriptĞ1Parser.IssuerContext = IssuerContext;

JavaScriptĞ1Parser.prototype.issuer = function() {

    var localctx = new IssuerContext(this, this._ctx, this.state);
    this.enterRule(localctx, 70, JavaScriptĞ1Parser.RULE_issuer);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 337;
        this.match(JavaScriptĞ1Parser.PUBKEY);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function SignatureContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_signature;
    return this;
}

SignatureContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
SignatureContext.prototype.constructor = SignatureContext;

SignatureContext.prototype.Signature_ = function() {
    return this.getToken(JavaScriptĞ1Parser.Signature_, 0);
};

SignatureContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterSignature(this);
	}
};

SignatureContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitSignature(this);
	}
};




JavaScriptĞ1Parser.SignatureContext = SignatureContext;

JavaScriptĞ1Parser.prototype.signature = function() {

    var localctx = new SignatureContext(this, this._ctx, this.state);
    this.enterRule(localctx, 72, JavaScriptĞ1Parser.RULE_signature);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 339;
        this.match(JavaScriptĞ1Parser.Signature_);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function Input_Context(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_input_;
    return this;
}

Input_Context.prototype = Object.create(antlr4.ParserRuleContext.prototype);
Input_Context.prototype.constructor = Input_Context;

Input_Context.prototype.INT = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.INT);
    } else {
        return this.getToken(JavaScriptĞ1Parser.INT, i);
    }
};


Input_Context.prototype.COLON = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.COLON);
    } else {
        return this.getToken(JavaScriptĞ1Parser.COLON, i);
    }
};


Input_Context.prototype.HASH = function() {
    return this.getToken(JavaScriptĞ1Parser.HASH, 0);
};

Input_Context.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterInput_(this);
	}
};

Input_Context.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitInput_(this);
	}
};




JavaScriptĞ1Parser.Input_Context = Input_Context;

JavaScriptĞ1Parser.prototype.input_ = function() {

    var localctx = new Input_Context(this, this._ctx, this.state);
    this.enterRule(localctx, 74, JavaScriptĞ1Parser.RULE_input_);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 341;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 342;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 343;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 356;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,11,this._ctx);
        switch(la_) {
        case 1:
            this.state = 344;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 345;
            this.match(JavaScriptĞ1Parser.T__18);
            this.state = 346;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 347;
            this.match(JavaScriptĞ1Parser.HASH);
            this.state = 348;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 349;
            this.match(JavaScriptĞ1Parser.INT);
            break;

        case 2:
            this.state = 350;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 351;
            this.match(JavaScriptĞ1Parser.T__19);
            this.state = 352;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 353;
            this.match(JavaScriptĞ1Parser.HASH);
            this.state = 354;
            this.match(JavaScriptĞ1Parser.COLON);
            this.state = 355;
            this.match(JavaScriptĞ1Parser.INT);
            break;

        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UnlockContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_unlock;
    return this;
}

UnlockContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UnlockContext.prototype.constructor = UnlockContext;

UnlockContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

UnlockContext.prototype.COLON = function() {
    return this.getToken(JavaScriptĞ1Parser.COLON, 0);
};

UnlockContext.prototype.unsig = function() {
    return this.getTypedRuleContext(UnsigContext,0);
};

UnlockContext.prototype.unxhx = function() {
    return this.getTypedRuleContext(UnxhxContext,0);
};

UnlockContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUnlock(this);
	}
};

UnlockContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUnlock(this);
	}
};




JavaScriptĞ1Parser.UnlockContext = UnlockContext;

JavaScriptĞ1Parser.prototype.unlock = function() {

    var localctx = new UnlockContext(this, this._ctx, this.state);
    this.enterRule(localctx, 76, JavaScriptĞ1Parser.RULE_unlock);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 358;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 359;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 362;
        this._errHandler.sync(this);
        switch(this._input.LA(1)) {
        case JavaScriptĞ1Parser.T__20:
            this.state = 360;
            this.unsig();
            break;
        case JavaScriptĞ1Parser.T__21:
            this.state = 361;
            this.unxhx();
            break;
        default:
            throw new antlr4.error.NoViableAltException(this);
        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UnsigContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_unsig;
    return this;
}

UnsigContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UnsigContext.prototype.constructor = UnsigContext;

UnsigContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

UnsigContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

UnsigContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

UnsigContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUnsig(this);
	}
};

UnsigContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUnsig(this);
	}
};




JavaScriptĞ1Parser.UnsigContext = UnsigContext;

JavaScriptĞ1Parser.prototype.unsig = function() {

    var localctx = new UnsigContext(this, this._ctx, this.state);
    this.enterRule(localctx, 78, JavaScriptĞ1Parser.RULE_unsig);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 364;
        this.match(JavaScriptĞ1Parser.T__20);
        this.state = 365;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 366;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 367;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function UnxhxContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_unxhx;
    return this;
}

UnxhxContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
UnxhxContext.prototype.constructor = UnxhxContext;

UnxhxContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

UnxhxContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

UnxhxContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

UnxhxContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterUnxhx(this);
	}
};

UnxhxContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitUnxhx(this);
	}
};




JavaScriptĞ1Parser.UnxhxContext = UnxhxContext;

JavaScriptĞ1Parser.prototype.unxhx = function() {

    var localctx = new UnxhxContext(this, this._ctx, this.state);
    this.enterRule(localctx, 80, JavaScriptĞ1Parser.RULE_unxhx);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 369;
        this.match(JavaScriptĞ1Parser.T__21);
        this.state = 370;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 371;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 372;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function OutputContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_output;
    return this;
}

OutputContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
OutputContext.prototype.constructor = OutputContext;

OutputContext.prototype.INT = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.INT);
    } else {
        return this.getToken(JavaScriptĞ1Parser.INT, i);
    }
};


OutputContext.prototype.COLON = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.COLON);
    } else {
        return this.getToken(JavaScriptĞ1Parser.COLON, i);
    }
};


OutputContext.prototype.cond = function() {
    return this.getTypedRuleContext(CondContext,0);
};

OutputContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterOutput(this);
	}
};

OutputContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitOutput(this);
	}
};




JavaScriptĞ1Parser.OutputContext = OutputContext;

JavaScriptĞ1Parser.prototype.output = function() {

    var localctx = new OutputContext(this, this._ctx, this.state);
    this.enterRule(localctx, 82, JavaScriptĞ1Parser.RULE_output);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 374;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 375;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 376;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 377;
        this.match(JavaScriptĞ1Parser.COLON);
        this.state = 378;
        this.cond();
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function EndpointContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_endpoint;
    return this;
}

EndpointContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
EndpointContext.prototype.constructor = EndpointContext;

EndpointContext.prototype.STR = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.STR);
    } else {
        return this.getToken(JavaScriptĞ1Parser.STR, i);
    }
};


EndpointContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

EndpointContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterEndpoint(this);
	}
};

EndpointContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitEndpoint(this);
	}
};




JavaScriptĞ1Parser.EndpointContext = EndpointContext;

JavaScriptĞ1Parser.prototype.endpoint = function() {

    var localctx = new EndpointContext(this, this._ctx, this.state);
    this.enterRule(localctx, 84, JavaScriptĞ1Parser.RULE_endpoint);
    var _la = 0; // Token type
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 380;
        this.match(JavaScriptĞ1Parser.STR);
        this.state = 383; 
        this._errHandler.sync(this);
        _la = this._input.LA(1);
        do {
            this.state = 381;
            this.match(JavaScriptĞ1Parser.WS);
            this.state = 382;
            this.match(JavaScriptĞ1Parser.STR);
            this.state = 385; 
            this._errHandler.sync(this);
            _la = this._input.LA(1);
        } while(_la===JavaScriptĞ1Parser.WS);
        this.state = 387;
        this.match(JavaScriptĞ1Parser.INT);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CondContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_cond;
    return this;
}

CondContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CondContext.prototype.constructor = CondContext;

CondContext.prototype.sig = function() {
    return this.getTypedRuleContext(SigContext,0);
};

CondContext.prototype.xhx = function() {
    return this.getTypedRuleContext(XhxContext,0);
};

CondContext.prototype.csv = function() {
    return this.getTypedRuleContext(CsvContext,0);
};

CondContext.prototype.cltv = function() {
    return this.getTypedRuleContext(CltvContext,0);
};

CondContext.prototype.or = function() {
    return this.getTypedRuleContext(OrContext,0);
};

CondContext.prototype.and = function() {
    return this.getTypedRuleContext(AndContext,0);
};

CondContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCond(this);
	}
};

CondContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCond(this);
	}
};




JavaScriptĞ1Parser.CondContext = CondContext;

JavaScriptĞ1Parser.prototype.cond = function() {

    var localctx = new CondContext(this, this._ctx, this.state);
    this.enterRule(localctx, 86, JavaScriptĞ1Parser.RULE_cond);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 395;
        this._errHandler.sync(this);
        var la_ = this._interp.adaptivePredict(this._input,14,this._ctx);
        switch(la_) {
        case 1:
            this.state = 389;
            this.sig();
            break;

        case 2:
            this.state = 390;
            this.xhx();
            break;

        case 3:
            this.state = 391;
            this.csv();
            break;

        case 4:
            this.state = 392;
            this.cltv();
            break;

        case 5:
            this.state = 393;
            this.or();
            break;

        case 6:
            this.state = 394;
            this.and();
            break;

        }
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function AndContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_and;
    return this;
}

AndContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
AndContext.prototype.constructor = AndContext;

AndContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

AndContext.prototype.cond = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(CondContext);
    } else {
        return this.getTypedRuleContext(CondContext,i);
    }
};

AndContext.prototype.WS = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.WS);
    } else {
        return this.getToken(JavaScriptĞ1Parser.WS, i);
    }
};


AndContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

AndContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterAnd(this);
	}
};

AndContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitAnd(this);
	}
};




JavaScriptĞ1Parser.AndContext = AndContext;

JavaScriptĞ1Parser.prototype.and = function() {

    var localctx = new AndContext(this, this._ctx, this.state);
    this.enterRule(localctx, 88, JavaScriptĞ1Parser.RULE_and);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 397;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 398;
        this.cond();
        this.state = 399;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 400;
        this.match(JavaScriptĞ1Parser.T__22);
        this.state = 401;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 402;
        this.cond();
        this.state = 403;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function OrContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_or;
    return this;
}

OrContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
OrContext.prototype.constructor = OrContext;

OrContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

OrContext.prototype.cond = function(i) {
    if(i===undefined) {
        i = null;
    }
    if(i===null) {
        return this.getTypedRuleContexts(CondContext);
    } else {
        return this.getTypedRuleContext(CondContext,i);
    }
};

OrContext.prototype.WS = function(i) {
	if(i===undefined) {
		i = null;
	}
    if(i===null) {
        return this.getTokens(JavaScriptĞ1Parser.WS);
    } else {
        return this.getToken(JavaScriptĞ1Parser.WS, i);
    }
};


OrContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

OrContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterOr(this);
	}
};

OrContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitOr(this);
	}
};




JavaScriptĞ1Parser.OrContext = OrContext;

JavaScriptĞ1Parser.prototype.or = function() {

    var localctx = new OrContext(this, this._ctx, this.state);
    this.enterRule(localctx, 90, JavaScriptĞ1Parser.RULE_or);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 405;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 406;
        this.cond();
        this.state = 407;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 408;
        this.match(JavaScriptĞ1Parser.T__23);
        this.state = 409;
        this.match(JavaScriptĞ1Parser.WS);
        this.state = 410;
        this.cond();
        this.state = 411;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function SigContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_sig;
    return this;
}

SigContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
SigContext.prototype.constructor = SigContext;

SigContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

SigContext.prototype.PUBKEY = function() {
    return this.getToken(JavaScriptĞ1Parser.PUBKEY, 0);
};

SigContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

SigContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterSig(this);
	}
};

SigContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitSig(this);
	}
};




JavaScriptĞ1Parser.SigContext = SigContext;

JavaScriptĞ1Parser.prototype.sig = function() {

    var localctx = new SigContext(this, this._ctx, this.state);
    this.enterRule(localctx, 92, JavaScriptĞ1Parser.RULE_sig);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 413;
        this.match(JavaScriptĞ1Parser.T__20);
        this.state = 414;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 415;
        this.match(JavaScriptĞ1Parser.PUBKEY);
        this.state = 416;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function XhxContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_xhx;
    return this;
}

XhxContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
XhxContext.prototype.constructor = XhxContext;

XhxContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

XhxContext.prototype.HASH = function() {
    return this.getToken(JavaScriptĞ1Parser.HASH, 0);
};

XhxContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

XhxContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterXhx(this);
	}
};

XhxContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitXhx(this);
	}
};




JavaScriptĞ1Parser.XhxContext = XhxContext;

JavaScriptĞ1Parser.prototype.xhx = function() {

    var localctx = new XhxContext(this, this._ctx, this.state);
    this.enterRule(localctx, 94, JavaScriptĞ1Parser.RULE_xhx);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 418;
        this.match(JavaScriptĞ1Parser.T__21);
        this.state = 419;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 420;
        this.match(JavaScriptĞ1Parser.HASH);
        this.state = 421;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CsvContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_csv;
    return this;
}

CsvContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CsvContext.prototype.constructor = CsvContext;

CsvContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

CsvContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

CsvContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

CsvContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCsv(this);
	}
};

CsvContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCsv(this);
	}
};




JavaScriptĞ1Parser.CsvContext = CsvContext;

JavaScriptĞ1Parser.prototype.csv = function() {

    var localctx = new CsvContext(this, this._ctx, this.state);
    this.enterRule(localctx, 96, JavaScriptĞ1Parser.RULE_csv);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 423;
        this.match(JavaScriptĞ1Parser.T__24);
        this.state = 424;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 425;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 426;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};

function CltvContext(parser, parent, invokingState) {
	if(parent===undefined) {
	    parent = null;
	}
	if(invokingState===undefined || invokingState===null) {
		invokingState = -1;
	}
	antlr4.ParserRuleContext.call(this, parent, invokingState);
    this.parser = parser;
    this.ruleIndex = JavaScriptĞ1Parser.RULE_cltv;
    return this;
}

CltvContext.prototype = Object.create(antlr4.ParserRuleContext.prototype);
CltvContext.prototype.constructor = CltvContext;

CltvContext.prototype.LP = function() {
    return this.getToken(JavaScriptĞ1Parser.LP, 0);
};

CltvContext.prototype.INT = function() {
    return this.getToken(JavaScriptĞ1Parser.INT, 0);
};

CltvContext.prototype.RP = function() {
    return this.getToken(JavaScriptĞ1Parser.RP, 0);
};

CltvContext.prototype.enterRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.enterCltv(this);
	}
};

CltvContext.prototype.exitRule = function(listener) {
    if(listener instanceof JavaScriptĞ1Listener ) {
        listener.exitCltv(this);
	}
};




JavaScriptĞ1Parser.CltvContext = CltvContext;

JavaScriptĞ1Parser.prototype.cltv = function() {

    var localctx = new CltvContext(this, this._ctx, this.state);
    this.enterRule(localctx, 98, JavaScriptĞ1Parser.RULE_cltv);
    try {
        this.enterOuterAlt(localctx, 1);
        this.state = 428;
        this.match(JavaScriptĞ1Parser.T__25);
        this.state = 429;
        this.match(JavaScriptĞ1Parser.LP);
        this.state = 430;
        this.match(JavaScriptĞ1Parser.INT);
        this.state = 431;
        this.match(JavaScriptĞ1Parser.RP);
    } catch (re) {
    	if(re instanceof antlr4.error.RecognitionException) {
	        localctx.exception = re;
	        this._errHandler.reportError(this, re);
	        this._errHandler.recover(this, re);
	    } else {
	    	throw re;
	    }
    } finally {
        this.exitRule();
    }
    return localctx;
};


exports.JavaScriptĞ1Parser = JavaScriptĞ1Parser;
