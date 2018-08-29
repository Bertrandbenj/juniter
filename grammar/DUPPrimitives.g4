lexer grammar DUPPrimitives;

Iss_: 				BASE58+; 
Signature_:			SIGNATURE EOL?; 
Version:			VersionHeader VERSION EOL ;
Curr: 				Currency_ CURRENCY EOL;
Timestamp_: 		'Timestamp' COLON WS ;
Type_:         		'Type' COLON  ;
Currency_:     		'Currency' COLON WS ;
VersionHeader:      'Version' COLON WS ;
UniqueIDHeader:     'UniqueID' COLON WS ;
IdtyTimestampHeader:'IdtyTimestamp' COLON WS ;
Issuer_:       		'Issuer' COLON WS ;
Blockstamp:    		'Blockstamp' COLON WS INT EOL ;
 
STR:				CHAR+ (' ' CHAR+)+; 
INT : 				[1-9] [0-9]*;
SIGNATURE:			BASE64+;
HASH:				HEX+;
PUBKEY:				BASE58+;
VERSION:			INT;
CURRENCY:			BASE64+;
USERID:    			CHAR+;
DOCTYPE:           'Identity' | 'Certification' | 'Membership' | 'Revocation' | 'Peer' | 'Transaction' ;
//BUID:  				INT '-' HASH;

fragment BASE58: 	[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz];
fragment HEX: 		[0123456789ABCDEF];
fragment CHAR:		BASE64; 
fragment BASE64: 	[0-9a-zA-Z_/+];

LP: 				'('; 
RP: 				')';
WS:					' ' ;
DASH:				'-';
EOL:				'\n';
COLON:				':';