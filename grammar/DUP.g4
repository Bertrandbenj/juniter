parser grammar DUP ; 
options {
	tokenVocab = DUPPrimitives;
}
//@header { 
//package juniter.grammar.antlr4; 
//} 

document: ( identityDoc | membershipDoc | certificationDoc | revocationDoc | peerDoc | transactionDoc );

// ====== OUTUTS DOCUMENTS
transactionDoc:		version_ type_ currency_ blockstamp_ locktime issuers inputs unlocks outputs signatures comment;
revocationDoc:		version_ type_ currency_ issuer_ locktime idtyTimestamp idtySignature signature_? ;
certificationDoc:	version_ type_ currency_ issuer_ idtyissuer locktime idtyTimestamp idtySignature certTimestamp SIGNATURE?; 
membershipDoc:		version_ type_ currency_ issuer_ block_ member userID certTS SIGNATURE? ;
peerDoc:			version_ type_ currency_ publicKey block_ endpoints ;
identityDoc:		version_ type_ currency_ issuer_ uniqueID_ timestamp_ signature_? ;
					


// ====== DOCUMENTS PROPERTIES
blockstamp_:		Blockstamp INT EOL;
//currency:			CURRENCY;
version_:			Version_ version EOL;//		{sSystem.out.println("version "+$version.text+":"+";");}		;
type_:         		Type_ WS DOCTYPE EOL;//		{if ( $DOCTYPE!=null ) System.out.println("found a doctype");}  ;    
currency_:     		Currency_ currency EOL ;
issuer_:       		Issuer_ issuer EOL ;
timestamp_:    		Timestamp_ buid EOL ;
uniqueID_:     		UniqueID_ USERID  EOL ;
block_:       		Block_ buid EOL ;
certTimestamp:		CertTimestamp_ buid EOL ;
certTS:       		CertTS_ buid EOL ;
comment:      		Comment_ STR EOL ;
endpoints:    		Endpoints_ (endpoint EOL)+ ;
idtyissuer:   		Idtyissuer_ PUBKEY EOL ;
idtyUniqueID: 		IdtyUniqueID_ USERID EOL ;
idtyTimestamp:		IdtyTimestamp_ buid EOL ;
idtySignature:		IdtySignature_ SIGNATURE EOL ;
inputs:       		Inputs_ (input_ EOL)+ ;
issuers:      		Issuers_ (issuer EOL)+ ;
locktime:     		Locktime_ INT EOL ;
member:   			Member_ STR EOL ;
number:       		Number_ INT EOL ;
outputs:      		Outputs_ (output EOL)+ ;
poWMin:       		PoWMin_ INT EOL ;
publicKey:    		PublicKey_ PUBKEY EOL ;
signatures:   		Signatures_ (signature EOL)+ ;
userID:       		UserID_ USERID EOL ;
unlocks:      		Unlocks_ (unlock EOL)+ ;

signature_:			SIGNATURE EOL?; 

// ======  SINGLE  types 
signature: 			SIGNATURE;
doctype: 			DOCTYPE;
version:			VERSION ;
currency: 			CURRENCY;
issuer:				PUBKEY;


// ====== COMPOSED types 
buid:				bnum d=DASH_INLINED bhash 	{System.out.println("version "+$bnum.text+"=="+$d+"=="+$bhash.text+";");} ;  // BUID; 
  bnum: 			a=NUMBER  					{System.out.println("int "+$a+":"+$a+";");};
  bhash: 			HASH_INLINED				{System.out.println("hash "+$HASH_INLINED+":"+";");};  

input_:				INT COLON INT 
					(
						(DIVIDEND_TYPE HASH COLON INT) 
						| 	
						(TRANSACTION_TYPE HASH COLON INT)
					);
unlock:				INT COLON (unsig | unxhx);
unsig:				SIG LP INT RP;
unxhx:				XHX LP INT RP;
output:				INT COLON INT COLON cond;
endpoint:			STR (' ' STR)+ INT;




// ====== OUTUTS CONDITION 
cond:				(sig | xhx | csv | cltv | or | and);
  and:				LP cond WS AND WS cond RP;
  or:				LP cond WS OR WS cond RP;
  sig:				SIG LP PUBKEY RP;
  xhx:				XHX LP HASH RP;
  csv:				CSV LP INT RP;
  cltv:				CLTV LP INT RP; 


// ========= Junk 

//dassh:				DASH 						{System.out.println("dash "+$DASH.text+":"+";");};
//intt: 				INT							{System.out.println("int "+$INT.text+":"+";");};
//hashh: 				HASH						{System.out.println("hash "+$HASH.text+":"+";");};  
					
//buid:				INT DASH HASH	;//			{System.out.println("version "+$intt.text+":"+$hashh.text+";");} ;  // BUID; 
	
//iss_: 				BASE58+; 
					