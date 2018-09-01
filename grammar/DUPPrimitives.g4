lexer grammar DUPPrimitives;
//@header { 
//	package juniter.grammar.antlr4; 
//} 

// ===== Document's Properties Header ===== 

Blockstamp:    		'Blockstamp' COLON WS ;
Block_:       		'Block' COLON WS -> pushMode(INLINED) ;
CertTimestamp_:		'CertTimestamp' COLON WS -> pushMode(INLINED) ;
CertTS_:       		'CertTS' COLON WS -> pushMode(INLINED) ;
Comment_:      		'Comment' COLON WS ;
Currency_:     		'Currency' COLON WS ;
Endpoints_:    		'Endpoints' COLON EOL ;
Idtyissuer_:   		'Idtyissuer' COLON WS ;
IdtyUniqueID_: 		'IdtyUniqueID_' COLON WS;
IdtySignature_:		'IdtySignature' COLON WS ;
IdtyTimestamp_:		'IdtyTimestamp' COLON WS -> pushMode(INLINED) ;
Inputs_:       		'Inputs' COLON EOL ;
Issuer_:       		'Issuer' COLON WS ;
Issuers_:      		'Issuers' COLON EOL ;
Locktime_:     		'Locktime' COLON WS ;
Member_:   			'Membership' COLON WS ;
Number_:       		'Number' COLON WS ;
Outputs_:      		'Outputs' COLON EOL ;
PoWMin_:       		'PoWMin' COLON WS;
PublicKey_:    		'PublicKey' COLON WS;
Signatures_:   		'Signatures' COLON EOL ;
Timestamp_: 		'Timestamp' COLON WS -> pushMode(INLINED) ;
Type_:         		'Type' COLON WS;
UniqueID_:   		'UniqueID' COLON WS ;
Unlocks_:      		'Unlocks' COLON EOL ;
UserID_:       		'UserID' COLON WS  ;
Version_:      		'Version' COLON WS ;
 



//BB: 				INT DASH HASH;
 
STR:				CHAR+ (' '+ CHAR+)+; 
INT : 				[1-9] [0-9]*;
SIGNATURE:			BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64
					BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64 BASE64; // 88  
HASH: 				BASE16+;					
//HASH:				BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
//					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
//					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
//					BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 BASE16 
//					BASE16 ;	// 41  
					
PUBKEY:				BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
					BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
					BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
					BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58 BASE58
					BASE58 BASE58 BASE58 BASE58 BASE58?; // 44,45 
VERSION:			INT;
CURRENCY:			BASE64+;
USERID:    			CHAR+;
SIG:				'SIG';
XHX:				'XHX';
CSV:				'CSV';
CLTV:				'CLTV';
OR:					'||';
AND:				'&&';
DIVIDEND_TYPE:		':D:';
TRANSACTION_TYPE:	':T:';
DOCTYPE:           'Identity' | 'Certification' | 'Membership' | 'Revocation' | 'Peer' | 'Transaction' ;
//BUID:  				INT '-' HASH;

fragment BASE64: 	[0-9a-zA-Z/+];
fragment BASE58: 	[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz];
fragment BASE16: 	[0123456789ABCDEF];
fragment BASE10: 	[0123456789];
fragment BASE9: 	[123456789];
fragment CHAR:		BASE64 | '_' | DASH; 


LP: 				'('; 
RP: 				')';
WS:					' ' ;
DASH:				'-';
EOL:				'\n';
COLON:				':';


mode INLINED;

NUMBER : 			BASE9 BASE10+;
DASH_INLINED: 		'-' ;
HASH_INLINED: 		BASE16+ ;
EOL_INLINED:		'\n' ->  popMode ;