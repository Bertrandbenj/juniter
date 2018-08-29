grammar DUP ;
import DUPPrimitives;

document: ( identity | membership | certification | revocation | peer | transaction );

// ====== OUTUTS DOCUMENTS
transaction:		version_ type_ currency_ Blockstamp locktime issuers inputs unlocks outputs signatures comment;
revocation:			version_ type_ currency_ issuer_ locktime idtyTimestamp idtySignature signature? ;
certification:		version_ type_ currency_ issuer_ idtyissuer locktime idtyTimestamp idtySignature certTimestamp SIGNATURE?; 
membership:			version_ type_ currency_ issuer_ block_ member userID certTS SIGNATURE? ;
peer:				version_ type_ currency_ publicKey block_ endpoints ;
identity:			version_ type_ currency_ issuer_ uniqueID_ timestamp_ signature? ;
					


// ====== DOCUMENTS PROPERTIES

doctype: 			DOCTYPE;
//currency:			CURRENCY;
version_:			Version 				{System.out.println("version "+$Version.text+":"+";");}		;
type_:         		Type_ WS DOCTYPE EOL		{if ( $DOCTYPE!=null ) System.out.println("found a doctype");}  ;    
currency_:     		Curr ;
issuer_:       		Issuer_ issuer EOL ;
timestamp_:    		Timestamp_ buid EOL ;
uniqueID_:     		UniqueIDHeader USERID  EOL ;


block_:       		'Block' COLON WS buid EOL ;

certTimestamp:		'CertTimestamp' COLON WS buid EOL ;
certTS:       		'CertTS' COLON WS buid EOL ;
comment:      		'Comment' COLON WS STR EOL ;

endpoints:    		'Endpoints' COLON EOL (endpoint EOL)+ ;
idtyissuer:   		'Idtyissuer' COLON WS PUBKEY EOL ;
idtyUniqueID: 		'Locktime' COLON WS USERID EOL ;
idtyTimestamp:		IdtyTimestampHeader buid EOL ;
idtySignature:		'IdtySignature' COLON WS SIGNATURE EOL ;
inputs:       		'Inputs' COLON EOL (input_ EOL)+ ;
issuers:      		'Issuers' COLON EOL (issuer EOL)+ ;


locktime:     		'Locktime' COLON WS INT EOL ;
member:   			'Membership' COLON WS STR EOL ;
number:       		'Number' COLON WS INT EOL ;
outputs:      		'Outputs' COLON EOL (output EOL)+ ;
poWMin:       		'PoWMin' COLON WS INT EOL ;
publicKey:    		'PublicKey' COLON WS PUBKEY EOL ;
signatures:   		'Signatures' COLON EOL (signature EOL)+ ;


userID:       		'UserID' COLON WS USERID EOL ;
unlocks:      		'Unlocks' COLON EOL (unlock EOL)+ ;


// ====== COMPOSED typeS 
buid:				INT DASH HASH	;//			{System.out.println("version "+$intt.text+":"+$hashh.text+";");} ;  // BUID; 

dassh:DASH;
intt: INT;
hashh: HASH;


issuer:				PUBKEY;
signature:			Signature_; 

input_:				INT COLON INT 
					(
						(COLON 'D' COLON HASH COLON INT) 
						| 	
						(COLON 'T' COLON HASH COLON INT)
					);
unlock:				INT COLON (unsig | unxhx);
unsig:				'SIG' LP INT RP;
unxhx:				'XHX' LP INT RP;
output:				INT COLON INT COLON cond;
endpoint:			STR (' ' STR)+ INT;




// ====== OUTUTS CONDITION 
cond:				(sig | xhx | csv | cltv | or | and);
and:				LP cond WS '&&' WS cond RP;
or:					LP cond WS '||' WS cond RP;
sig:				'SIG' LP PUBKEY RP;
xhx:				'XHX' LP HASH RP;
csv:				'CSV' LP INT RP;
cltv:				'CLTV' LP INT RP; 


// ============= ====== =============
// ============= TOKENS =============
// ============= ====== =============


