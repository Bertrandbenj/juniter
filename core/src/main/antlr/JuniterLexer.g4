lexer grammar JuniterLexer ;

options {
	language = Java;
}
@header { 
package generated.antlr;
} 

fragment SIGNATURE:	BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 ( (BASE64 BASE64) || '=='); // 88
//J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci

//HASH: 				BASE16+;
HASH:				BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16
					BASE16 BASE16 BASE16 BASE16   ;	// 64
					
TXHASH:				BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16   ;	// 64

EXACTPUBKEY:        BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
                    BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
                    BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
                    BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
                    BASE58 BASE58 BASE58 BASE58 ;

BSTAMP :     ( BASE10 | ( BASE9 BASE10+ ) )	'-' HASH;

Timestamp_: 		'Timestamp' VALUE_START 	-> skip, pushMode(SIGN_INLINED),pushMode(BUID_INLINED) ;
UniqueID_:   		'UniqueID' VALUE_START 		-> skip, pushMode(USER_INLINED) ; 
Issuer_:       		'Issuer' VALUE_START 		-> skip, pushMode(PUBK_INLINED) ;
Currency_:     		'Currency' VALUE_START  	-> skip, pushMode(CURR_INLINED) ;
Type_:         		'Type' VALUE_START 			-> skip, pushMode(TYPE_INLINED) ;
Version_:      		'Version' VALUE_START 		-> skip, pushMode(VERS_INLINED) ;
Block_:       		'Block' VALUE_START 		-> skip, pushMode(BUID_INLINED) ;
Member_:   			'Membership' VALUE_START 	-> skip, pushMode(MEMB_INLINED) ;
CertTS_:       		'CertTS' VALUE_START	 	-> skip, pushMode(SIGN_INLINED), pushMode(BUID_INLINED) ;

UserID_:       		'UserID' VALUE_START		-> skip, pushMode(USER_INLINED) ;
IdtySignature_:		'IdtySignature' VALUE_START ->  pushMode(SIGN_INLINED), pushMode(SIGN_INLINED) ;
IdtyTimestamp_:		'IdtyTimestamp' VALUE_START -> skip, pushMode(BUID_INLINED) ;
IdtyUniqueID_: 		'IdtyUniqueID' VALUE_START 	-> skip, pushMode(USER_INLINED) ;
IdtyIssuer_:		'IdtyIssuer'VALUE_START		-> skip, pushMode(PUBK_INLINED) ;
PublicKey_:			'PublicKey'VALUE_START		-> skip, pushMode(PUBK_INLINED) ;

Unlocks_: 			'Unlocks' ARRAY_START		-> skip, pushMode(ULCK_MULTILN) ;
Signatures_: 		'Signatures' ARRAY_START	-> skip, pushMode(SIGN_MULTILN) ;
Comment_: 			'Comment' VALUE_START		-> skip, pushMode(COMM_INLINED) ;
Inputs_: 			'Inputs' ARRAY_START		-> skip, pushMode(INPT_MULTILN) ;
Issuers_: 			'Issuers' ARRAY_START		-> skip, pushMode(ISSU_MULTILN) ;
Locktime_: 			'Locktime' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
Blockstamp_: 		'Blockstamp' VALUE_START	-> skip, pushMode(NUMB_INLINED) ;
Outputs_: 			'Outputs' ARRAY_START		-> skip, pushMode(OUTP_MULTILN) ;
Endpoints_: 		'Endpoints' ARRAY_START		-> skip, pushMode(ENDPT_MULTILN) ;


Number_: 			'Number' VALUE_START			-> skip, pushMode(NUMB_INLINED) ;
PoWMin_: 			'PoWMin' VALUE_START			-> skip, pushMode(NUMB_INLINED) ;
Time_: 				'Time' VALUE_START				-> skip, pushMode(NUMB_INLINED) ;
MedianTime_: 		'MedianTime' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
UniversalDividend_: 'UniversalDividend' VALUE_START	-> skip, pushMode(NUMB_INLINED) ;
UnitBase_: 			'UnitBase' VALUE_START			-> skip, pushMode(NUMB_INLINED) ;
IssuersFrame_: 		'IssuersFrame' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
IssuersFrameVar_: 	'IssuersFrameVar' VALUE_START	-> skip, pushMode(NUMB_INLINED) ;
DiffIssuersCount_: 	'DifferentIssuersCount' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
PreviousHash_: 		'PreviousHash' VALUE_START		-> skip, pushMode(BUID_INLINED) ;
PreviousIssuer_: 	'PreviousIssuer' VALUE_START	-> skip, pushMode(PUBK_INLINED) ;
Parameters_: 		'Parameters' VALUE_START		-> skip, pushMode(BLOCK_FIELD) ;
MembersCount_: 		'MembersCount' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
Nonce_: 			'Nonce' VALUE_START				-> skip, pushMode(NUMB_INLINED) ;
InnerHash_: 		'InnerHash' VALUE_START			-> skip, pushMode(BUID_INLINED) ;
Transactions_:		'Transactions' ARRAY_START		-> skip, pushMode(BLOCK_FIELD);
Certifications_:	'Certifications' ARRAY_START	-> skip, pushMode(WOT_EXCL) ;
Excluded_:			'Excluded' ARRAY_START			;//-> pushMode(WOT_MULTILN); //-> pushMode(ISSU_MULTILN) ;
Revoked_:			'Revoked' ARRAY_START			;//-> pushMode(WOT_MULTILN);
Leavers_:			'Leaver' ARRAY_START			;//-> pushMode(WOT_MULTILN);
Actives_:			'Actives' ARRAY_START			;//-> pushMode(WOT_MULTILN);
Joiners_:			'Joiners' ARRAY_START		    ;//-> pushMode(WOT_MULTILN);
Identities_:		'Identities' ARRAY_START		-> skip, pushMode(WOT_IDTIES) ;
TX: 				'TX:' 							-> pushMode(BLOCK_GRP), pushMode(COMPACT_TX),pushMode(BUID_INLINED),pushMode(BLOCK_FIELD);





//SIGN_INLINE_START:	INLINE_START 			{ System.out.println("PUSH SIGN_INLINED"); } -> pushMode(SIGN_INLINED) ;
//INLINED_START:		INLINE_START 			{ System.out.println("PUSH BUID_INLINED"); } -> pushMode(BUID_INLINED) ;

VALUE_START:		COLON WS ;
ARRAY_START:		COLON NL ;
fragment LP: 		'('; 
fragment RP: 		')';
fragment NL:		'\n';
fragment WS: 		' ';
fragment COLON: 	':'  ;
fragment BASE2: 	[12];
fragment BASE9: 	[123456789];
fragment BASE10: 	[0123456789];
fragment BASE16: 	[0123456789ABCDEF];
fragment BASE16LC: 	[0123456789abcdef];	
fragment BASE58: 	[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz];
fragment BASE64: 	[0-9a-zA-Z/+-];
fragment CURCY:		[0-9a-zA-Z_-];
fragment INT: 		BASE10 | ( BASE9 BASE10+ );
fragment INT256: 	BASE10 | ( BASE9 BASE10 ) | ( BASE2 BASE10 BASE10 );

fragment SIGNTRE:	BASE64+  '=='? ;

SIGNATURETK: SIGNATURE;

EODOC: NL;
//
mode BLOCK_GRP;
BLG_NUM : BASE10+;

POPONE:             NL                      -> popMode ;

mode WOT_EXCL;
  WOTBUID:          BSTAMP ;
  WOTNUMB:			INT;
  WOTPUBK:			EXACTPUBKEY;
  WOTSIGN:			SIGNATURE ;
  WOTSEP:			COLON                   -> skip;
  WOTUID:			BASE64+ ;
  WOTNL:			NL                      -> skip;
EOWOT:              Certifications_ 	    -> skip, popMode, pushMode(WOT_CERT);


mode WOT_CERT;
  CERTNUMB:			    INT;
  CERTPUBK:			    EXACTPUBKEY;
  CERTSIGN:			    SIGNATURE ;
  CERTSEP:			    COLON               -> skip;
  CERTNL:			    NL                  -> skip;
EOWOT2: 			    Transactions_	 	-> popMode, more;

mode WOT_IDTIES;
  EOIDTIES:		        Joiners_ 	        -> skip, popMode, pushMode(WOT_JOIN);

  IDTIESBUID:           BSTAMP ;
  IDTIESNUMB:		    INT;
  IDTIESPUBK:	        EXACTPUBKEY;
  IDTIESSIGN:		    SIGNATURE;
  IDTIESSEP:			COLON               -> skip;
  IDTIESUID:			BASE64+ ;
  IDTIESNL:			    NL                  -> skip;

mode WOT_JOIN;
  EOJOIN:				 Actives_ 	        -> skip, popMode, pushMode(WOT_ACTIVE);

  JOINBUID:        BSTAMP ;
  JOINNUMB:			INT;
  JOINPUBK:			EXACTPUBKEY;
  JOINSIGN:			SIGNATURE;
  JOINSEP:			COLON                   -> skip;
  JOINUID:			BASE64+ ;
  JOINNL:			    NL                  -> skip;

mode WOT_ACTIVE;
  EOACTIVE:				 Leavers_ 	        -> skip, popMode, pushMode(WOT_LEAVERS);

  ACTIVEBUID:        BSTAMP ;
  ACTIVENUMB:			INT;
  ACTIVEPUBK:			EXACTPUBKEY;
  ACTIVESIGN:			SIGNATURE;
  ACTIVESEP:			COLON               -> skip;
  ACTIVEUID:			BASE64+ ;
  ACTIVENL:			    NL                  -> skip;

mode WOT_LEAVERS;
  EOLEAV:		    Revoked_ 	            -> skip, popMode, pushMode(WOT_REV);
  LEAVBUID:         BSTAMP ;
  LEAVPUBK:		    EXACTPUBKEY;
  LEAVSIGN:			SIGNATURE;
  LEAVSEP:			COLON                   -> skip;
  LEAVUID:			BASE64+ ;
  LEAVNL:			NL                      -> skip;

mode WOT_REV;
  EOREV:		    Excluded_ 	            -> skip, popMode, pushMode(WOT_EXCL);
  REVBUID:          BSTAMP ;
  REVPUBK:		    EXACTPUBKEY;
  REVSIGN:			SIGNATURE;
  REVSEP:			COLON                   -> skip;
  REVUID:			BASE64+ ;
  REVNL:			NL                      -> skip;

mode BUID_INLINED;
  NUMBER : 			BASE10 | ( BASE9 BASE10+ );
  DASH_INLINED: 	'-' ;
  HASH_INLINED: 	BASE16+ ;
EOBUID:				NL 						        -> skip,popMode ;

mode TYPE_INLINED;
  DOCTYPE_IDTY:     		'Identity' 				;//-> skip;
  DOCTYPE_CERT:     		'Certification' 		;//-> skip;
  DOCTYPE_MEMB:     		'Membership' 			;//-> skip;
  DOCTYPE_REVO:     		'Revocation' 			;//-> skip;
  DOCTYPE_PEER:     		'Peer' 					;//-> skip;
  DOCTYPE_TRAN:     		'Transaction' 			;//-> skip;
  DOCTYPE_BLCK:     		'Block' 			;//-> skip;
//  DOCTYPE: 			DT_IDTY | DT_CERT | 'MembershipDTO' | 'Revocation' | 'Peer' | 'Transaction' ;
  
EOTYPE:				NL 						 -> skip,popMode ;

mode SIGN_INLINED;
  SIGN:				SIGNTRE;
  CertTimestamp_:     'CertTimestamp' VALUE_START	->  pushMode(BUID_INLINED) ; // FIXME certif & revocation idtySignature issue (end of document require sign mode-> CertTimestamp happen to be parsed with sign mode ) = PAIN POINT = need refactor = dont ask, it just works

EOSIGN:				NL 						-> skip,popMode ;

mode VERS_INLINED;
  VERSION:			INT;
EOVERS:				NL 						 -> skip,popMode ;

mode PUBK_INLINED;
  PUBKEY_INLINED:	EXACTPUBKEY;
EOPUBK:				NL 						 -> skip,popMode ;

mode CURR_INLINED;
  CURRENCY:			(BASE64 | '_' )+; 
EOCURR:				NL 						-> skip,popMode ;

mode USER_INLINED;
  USERID:    		(BASE64 | '_' )+; 
EOUSER:				NL 						 -> skip,popMode ;

mode NUMB_INLINED;
  NUMB:    			INT; 
EONUMB:				NL 						 -> skip,popMode ;


mode MEMB_INLINED;
  MEMBER_TYPE:    	'IN' | 'OUT'; 
EOMEMB:				NL 						 -> skip,popMode ;



mode COMM_INLINED;
  COMMENT: 			(BASE64 | '_' )+;
EOCOMM:				NL 						 -> skip,popMode ;


mode SIGN_MULTILN;
MULTISIGN:			SIGNTRE;
SIGN_SEP:			NL;
EOSIGNS:			Comment_				->  skip,popMode, pushMode(COMM_INLINED) ;

mode OUTP_MULTILN;
OUTNUMB:			BASE10+;
SIG:				'SIG';
XHX:				'XHX' ;
CSV:				'CSV';
CLTV:				'CLTV';
OR:					' || ';
AND:				' && ';
OUTLP:				'(' ;
OUTRP:				')';
OUTHASH: 			BASE16+; //TXHASH;
//OUTINT:				INT;
OUTPUT_FIELD_SEP:	COLON -> skip;
OUTPUT_SEP:			NL;
OUTPUBK:			EXACTPUBKEY;
//OUT_AMOUT_BASE:		OUTNUMB;
EOOUTP:				Signatures_				-> skip, popMode, pushMode(SIGN_MULTILN) ;

mode ULCK_MULTILN;
UNLOCK_SEP:			NL;
UNSIG:				'SIG';
UNXHX:				'XHX';
UNLP:				'(' -> skip;
UNRP:				')' -> skip;
UNNUMB: 			BASE10+;
UNLOCK_FIELD_SEP:	COLON -> skip;
EOULK:				Outputs_				-> skip, popMode, pushMode(OUTP_MULTILN) ;


mode BLOCK_FIELD;

BLNUMB: 			INT;
BLPERCENT: 			'0.' BASE10+;
BLOCK_FIELD_SEP:	COLON -> skip;
EOBLK:				NL						-> skip, popMode ;


mode INPT_MULTILN;
INNUMB:				BASE10+;
INHASH:				BASE16+;
INFIELD_SEP:		COLON ;
DIVIDEND_TYPE:		':D:';
TRANSACTION_TYPE:	':T:';
INPUT_SEP:			NL;
EOINPT:				Unlocks_				 ->  skip, popMode, pushMode(ULCK_MULTILN) ;

    //

mode ISSU_MULTILN;
  PUBKEY_MULTILN:	EXACTPUBKEY;
  ISSUER_SEP:		NL  -> skip ;
  //
  //  ISSUER_STOP:		ISSUER_SEP 				{System.out.println("POP ISSU_MULTILN");}-> more, popMode;
EOISSU:				 Inputs_		{System.out.println("POP ISSU_MULTILN");} -> skip, popMode, pushMode(INPT_MULTILN) ;


mode COMPACT_TX;
//CPT_OUT:    CPT_NUM CPT_COL CPT_NUM CPT_COL -> pushMode(OUTP_MULTILN);

CPT_COL:	COLON  ;
CPT_DIV:    ':D:';
CPT_TXS:    ':T:';
BREAK:      NL;
CPT_SIG:    'SIG';
CPT_NUM:    BASE10+;
CPT_HASH:   BASE16+;

CPT_OP:    '(' -> skip;
CPT_CP:    ')' -> skip;
CPT_ISS:    BASE58+;
CPT_SIGN:   SIGNTRE -> popMode;
//:  popMode ;
//EOTXS:      InnerHash_ -> popMode, pushMode(NUMB_INLINED), pushMode(BUID_INLINED);
EOTXCPT:    TX -> more;
CPT_COMM:   (BASE64+ |  (BASE64 | '_' | ' ' )+) ;



mode ENDPT_MULTILN;
  PORT:				BASE10+  ;

  IP4:				INT256 '.' INT256 '.' INT256 '.' INT256 ; 
  IP6:				OCT ':' OCT ':' OCT ':' OCT ':' OCT ':' OCT ':' OCT;
  OCT:				BASE16LC BASE16LC BASE16LC BASE16LC;
  SESSID:           OCT OCT;
  DNS: 				[a-z0-9]+ ('.' [a-z-]+)+;
  FIELD_SEP:		WS;
  ENDPOINT_TYPE:	'OTHER_PROTOCOL' | 'BASIC_MERKLED_API' | [0-9A-Z_]+ ;
  ENDPT_SEP:		NL;
//EOENDPT:			Inputs_					{System.out.println("POP ISSU_MULTILN");} -> skip, popMode, pushMode(INPT_MULTILN) ;
