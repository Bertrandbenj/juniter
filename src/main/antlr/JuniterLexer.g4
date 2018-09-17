lexer grammar JuniterLexer ;

options {
	language = Java;
}
@header { 
package antlr.main; 

} 

 SIGNATURE:			BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64; // 88  
//J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci

INT : 				BASE9 BASE10+;
//HASH: 				BASE16+;
HASH:				BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16  ;	// 41   
					
TXHASH:				BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
					BASE16 BASE16 BASE16 BASE16   ;	// 64   


Timestamp_: 		'Timestamp' VALUE_START 	-> skip, pushMode(SIGN_INLINED),pushMode(BUID_INLINED) ;
UniqueID_:   		'UniqueID' VALUE_START 		-> skip, pushMode(USER_INLINED) ; 
Issuer_:       		'Issuer' VALUE_START 		-> skip, pushMode(PUBK_INLINED) ;
Currency_:     		'Currency' VALUE_START  	-> skip, pushMode(CURR_INLINED) ;
Type_:         		'Type' VALUE_START 			-> skip, pushMode(TYPE_INLINED) ;
Version_:      		'Version' VALUE_START 		-> skip, pushMode(VERS_INLINED) ;
Block_:       		'Block' VALUE_START 		-> skip, pushMode(BUID_INLINED) ;
Member_:   			'Membership' VALUE_START 	-> skip, pushMode(MEMB_INLINED) ;
CertTS_:       		'CertTS' VALUE_START	 	-> skip, pushMode(SIGN_INLINED),pushMode(BUID_INLINED) ;
CertTimestamp_:     'CertTimestamp' VALUE_START	-> skip, pushMode(BUID_INLINED) ;

UserID_:       		'UserID' VALUE_START		-> skip, pushMode(USER_INLINED) ;
IdtySignature_:		'IdtySignature' VALUE_START -> skip, pushMode(SIGN_INLINED) ;
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


Number_: 			'Number' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
PoWMin_: 			'PoWMin' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
Time_: 				'Time' VALUE_START			-> skip, pushMode(NUMB_INLINED) ;
MedianTime_: 		'MedianTime' VALUE_START	-> skip, pushMode(NUMB_INLINED) ;
UniversalDividend_: 'UniversalDividend' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
UnitBase_: 			'UnitBase' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
IssuersFrame_: 		'IssuersFrame' VALUE_START	-> skip, pushMode(NUMB_INLINED) ;
IssuersFrameVar_: 	'IssuersFrameVar' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
DifferentIssuersCount_: 'differentIssuersCount' VALUE_START		-> skip, pushMode(PUBK_INLINED) ;
PreviousHash_: 		'PreviousHash' VALUE_START		-> skip, pushMode(HASH_INLINED) ;
PreviousIssuer_: 	'PreviousIssuer' VALUE_START		-> skip, pushMode(PUBK_INLINED) ;
Parameters_: 		'Parameters' VALUE_START		-> skip, pushMode() ;
MembersCount_: 		'MembersCount' VALUE_START		-> skip, pushMode(NUMB_INLINED) ;
Nonce_: 			'Nonce' VALUE_START			-> skip, pushMode(NUMB_INLINED) ;
InnerHash_: 		'InnerHash' VALUE_START		-> skip, pushMode(HASH_INLINED) ;


//SIGN_INLINE_START:	INLINE_START 			{ System.out.println("PUSH SIGN_INLINED"); } -> pushMode(SIGN_INLINED) ;
//INLINED_START:		INLINE_START 			{ System.out.println("PUSH BUID_INLINED"); } -> pushMode(BUID_INLINED) ;

VALUE_START:		COLON WS ;
ARRAY_START:		COLON NL ;
fragment LP: 		'('; 
fragment RP: 		')';
fragment NL:		'\n';
fragment WS: 		' ';
fragment COLON: 	':'  ;
fragment BASE9: 	[123456789];
fragment BASE10: 	[0123456789];
fragment BASE16: 	[0123456789ABCDEF];
fragment BASE16LC: 	[0123456789abcdef];	
fragment BASE58: 	[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz];
fragment BASE64: 	[0-9a-zA-Z/+-];
fragment SIGN_CHAR:	BASE64+ '='*;

mode BUID_INLINED;
  NUMBER : 			BASE10 | ( BASE9 BASE10+ );
  DASH_INLINED: 	'-' ;
  HASH_INLINED: 	BASE16+ ;
EOBUID:				NL 						 -> skip,popMode ;

mode TYPE_INLINED;
  DOCTYPE_IDTY:     		'Identity' 				;//-> skip;
  DOCTYPE_CERT:     		'Certification' 		;//-> skip;
  DOCTYPE_MEMB:     		'Membership' 			;//-> skip;
  DOCTYPE_REVO:     		'Revocation' 			;//-> skip;
  DOCTYPE_PEER:     		'Peer' 					;//-> skip;
  DOCTYPE_TRAN:     		'Transaction' 			;//-> skip;
//  DOCTYPE: 			DT_IDTY | DT_CERT | 'Membership' | 'Revocation' | 'Peer' | 'Transaction' ;
  
EOTYPE:				NL 						 -> skip,popMode ;

mode SIGN_INLINED;
  SIGN:				SIGN_CHAR;
EOSIGN:				NL 						-> skip,popMode ;

mode VERS_INLINED;
  VERSION:			BASE9 BASE10+;
EOVERS:				NL 						 -> skip,popMode ;

mode PUBK_INLINED;
  PUBKEY_INLINED:	BASE58+; 
EOPUBK:				NL 						 -> skip,popMode ;

mode CURR_INLINED;
  CURRENCY:			(BASE64 | '_' )+; 
EOCURR:				NL 						-> skip,popMode ;

mode USER_INLINED;
  USERID:    		(BASE64 | '_' )+; 
EOUSER:				NL 						 -> skip,popMode ;

mode NUMB_INLINED;
  NUMB:    			BASE9 BASE10+; 
EONUMB:				NL 						 -> skip,popMode ;


mode MEMB_INLINED;
  MEMBER_TYPE:    	'IN' | 'OUT'; 
EOMEMB:				NL 						 -> skip,popMode ;



mode COMM_INLINED;
COMMENT: 			(BASE64 | '_' )+;
EOCOMM:				NL 						 -> skip,popMode ;


mode SIGN_MULTILN;
MULTISIGN:			SIGN_CHAR+;
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
OUTLP:				'(';
OUTRP:				')';
OUTHASH: 			BASE16+; //TXHASH;
OUTINT:				INT;
OUTPUT_FIELD_SEP:	COLON ;
OUTPUT_SEP:			NL;
OUTPUBK:			BASE58+;
OUT_AMOUT_BASE:		INT;
EOOUTP:				Signatures_				-> skip, popMode, pushMode(SIGN_MULTILN) ;

mode ULCK_MULTILN;
UNLOCK_SEP:			NL;
UNSIG:				'SIG';
UNXHX:				'XHX';
UNLP:				'(';
UNRP:				')';
UNNUMB: 			BASE10+;
UNLOCK_FIELD_SEP:	COLON;
EOULK:				Outputs_				-> skip, popMode, pushMode(OUTP_MULTILN) ;

mode INPT_MULTILN;
INNUMB:				BASE10+;
INHASH:				BASE16+;
INFIELD_SEP:		COLON;
DIVIDEND_TYPE:		'D';
TRANSACTION_TYPE:	'T';
INPUT_SEP:			NL;
EOINPT:				Unlocks_				 -> skip, popMode, pushMode(ULCK_MULTILN) ;

mode ISSU_MULTILN;
  PUBKEY_MULTILN:	BASE58+; 
ISSUER_SEP:			NL 						;
EOISSU:				Inputs_					-> skip, popMode, pushMode(INPT_MULTILN) ;


mode ENDPT_MULTILN;
  IP4:				BASE10+ '.' BASE10+ '.' BASE10+ '.' BASE10+ ; 
  IP6:				OCT ':' OCT ':' OCT ':' OCT ':' OCT ':' OCT ':' OCT; 
  OCT:				BASE16LC+;
  DNS: 				[a-z]+ ('.' [a-z]+)+;
  FIELD_SEP:		' ' 					-> skip;
  PORT:				PORT_NUMBER ENDPT_SEP;
  PORT_NUMBER:		BASE10 BASE10 BASE10 BASE10;
  ENDPOINT_TYPE:	[A-Z_]+	;
  ENDPT_SEP:		NL 						 -> skip, popMode; 
//EOENDPT:			Inputs_					{System.out.println("POP ISSU_MULTILN");} -> skip, popMode, pushMode(INPT_MULTILN) ;
