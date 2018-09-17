parser grammar PGParser  ; // when messing up with parser keyword remember to rm *.java
//import PGLexer; 
options {tokenVocab = PGLexer;}


@members{
	String acceptVersion="10";
	String acceptCurrency="g1";
	int nbIssu=0;
	int nbSign=0;
	int maxEndpoints=3;
	String indent="          ";
}

doc: 										{System.out.println("doc:");}
	version 								{System.out.println("  version: "+$version.text);}
	(   	
		DOCTYPE_TRAN transaction 	
		| DOCTYPE_PEER peer 		
		| wot 										
	) 										
;



peer: 										{System.out.println("  peer: ");}
	currency								
	pubkey 									{System.out.println("    pubkey: "+$pubkey.text);}
											{System.out.println("    block: ");} {indent="      ";}
	block 									
	endpoints  								
;


transaction:								{System.out.println("  transaction:");}
	currency
	blockstamp 								{System.out.println("    blockstamp: "+$blockstamp.text);}
	locktime 								{System.out.println("    locktime: "+$locktime.text);}
	issuers									
	inputs									
	unlocks									
	outputs									
	signatures								
	comment 								{System.out.println("    comment: "+$comment.text);}
;


wot: 										{System.out.println("  wot: #  Identity, Certification, Membership, Revocation");}
	(
		DOCTYPE_IDTY currency identity		 
		| DOCTYPE_MEMB currency membership			
		| DOCTYPE_REVO currency revocation 
		| DOCTYPE_CERT currency certification
	)
	signature								{System.out.println("    signature: "+$signature.text);}
;

identity:									{System.out.println("    identity:");}
	issuer 									{System.out.println("      issuer: "+$issuer.text);}
	userid 									{System.out.println("      userid: "+$userid.text);}
											{System.out.println("      timestamp: ");}
	timestamp 								
;

certification: 								{System.out.println("    certification: ");}
	iss=issuer 								{System.out.println("      issuer: "+$iss.text);}
	idi=idtyIssuer 							{System.out.println("      idtyIssuer: "+$idi.text);}
	uid=idtyUniqueID 						{System.out.println("      idtyUniqueID: "+$uid.text);}
	{System.out.println("      idtyTimestamp: ");}
	idtyTimestamp 						
	ids=idtySignature 						{System.out.println("      idtySignature: "+$ids.text);}
	{System.out.println("      certTimestamp: ");}
	certTimestamp 						
;

membership:									{System.out.println("    membership:");}
	issuer									{System.out.println("      issuer: "+$issuer.text);}
	{System.out.println("      block: ");}
	{indent="        ";} 
	block									
	member									{System.out.println("      member: "+$member.text);}
	userID									{System.out.println("      userID: "+$userID.text);}
	{System.out.println("      certTS: ");}
	certTS									
;

revocation:									{System.out.println("    revocation:");}
	issuer									{System.out.println("      issuer: "+$issuer.text);}
	idtyUniqueID							{System.out.println("      idtyUniqueID: "+$idtyUniqueID.text);}
	{System.out.println("      idtyTimestamp: ");}
	idtyTimestamp							
	idtySignature							{System.out.println("      idtySignature: "+$idtySignature.text);}
;


certTimestamp: 		buid;
idtyIssuer: 		pubkey;

endpoints 
locals [int i=0]: 
											{System.out.println("    endpoints: ");}
( 
	{$i<maxEndpoints}? 
											{System.out.println("      "+ $i++ +": ");}
	enpoint 						
)+
| EOF 
;

enpoint: 					
	endpointType 							{System.out.println("        type: "+$endpointType.text);}
	( 
		dns 								{System.out.println("        dns: "+$dns.text);}
		| ip4 								{System.out.println("        ip4: "+$ip4.text);}
		| ip6								{System.out.println("        ip6: "+$ip6.text);}
	)+ 
	port 									{System.out.println("        port: "+$port.text);}
;

port: 				PORT;
ip6: 				IP6;
ip4: 				IP4;
dns: 				DNS;
endpointType: 		ENDPOINT_TYPE;



idtySignature: 		signature;
idtyTimestamp: {indent="        ";} 		
	buid
;
idtyUniqueID: 		USERID;


signatures
locals[int i=0]
:{System.out.println("    signatures: ");}
(
	{ $i < nbIssu }? 
	signature 				 
	{System.out.println("      "+ $i++ +": "+$signature.text);}

)+;
comment: 			COMMENT;


issuers
: 	{System.out.println("    issuers: ");}		
	(
		pubkey
		{System.out.println("      "+ nbIssu++ +": "+ $pubkey.text);}
	)+
;
locktime: 			NUMB;
blockstamp: 		NUMB;


inputs
locals[int i=0]
: 	
											{System.out.println("    inputs: ");}
	(
											{System.out.println("      "+ $i++ +": ");}
		input								
	)+
;

input:										
	amount  								{System.out.println("        amount: "+$amount.text);}
	base  									{System.out.println("        base: "+$base.text);}
	( 
		(DIVIDEND_TYPE ubi)					
		|
		(TRANSACTION_TYPE tx)
	);	

ubi:										{System.out.println("        ubi:  # Universal Basic Income");}
  	pubkey 									{System.out.println("          pubkey: "+$pubkey.text+" # of this pubkey");}
  	bnum									{System.out.println("          bnum: "+$bnum.text+ " # at that block number ");}
;						

tx:											{System.out.println("        tx:  # a previous Transaction");}
  	bhash 									{System.out.println("          bhash: "+$bhash.text+" # in this block hash");}
  	tindex									{System.out.println("          tindex: "+$tindex.text+" # at that index");}
;

amount: 			INNUMB | OUT_AMOUT_BASE;
base: 				INNUMB | OUT_AMOUT_BASE;
tindex: 			INNUMB;

unlocks
locals[int i=0]
: 			
											{System.out.println("    unlocks: ");}
	(
											{System.out.println("      "+ $i++ +": ");}	
		unlock 								
	)+				
;

unlock
: 											
	in_index 								{System.out.println("        in_index: "+$in_index.text+"");}
											{System.out.println("        ul_condition: ");}
	(
		UNSIG unsig 						{System.out.println("          unsig: "+$unsig.text+"");}
		| UNXHX unxhx						{System.out.println("          unxhx: "+$unxhx.text+"");}
	)
;
in_index:		UNNUMB;
unsig:			UNNUMB ;
unxhx:			UNNUMB ;


//SIG(PUBLIC_KEY), XHX(SHA256_HASH), CLTV(INTEGER), CSV(INTEGER)
outputs
locals[int i=0]
: {System.out.println("    outputs: ");}
( {System.out.println("      "+$i++ +": ");}
	output 
)+						
;
output:				
	amount 									{System.out.println("        amount: "+$amount.text+"");}
	base 									{System.out.println("        base: "+$base.text+"");}
											{System.out.println("        condition: ");}
	cond								
;
 
cond:										
(
	sig 									{System.out.println(indent+"sig: "+$sig.text+"");}
  	| xhx 									{System.out.println(indent+"xhx: "+$xhx.text+"");}	
  	| csv 									{System.out.println(indent+"csv: "+$csv.text+"");}
  	| cltv 									{System.out.println(indent+"cltv: "+$cltv.text+"");}
  	| 										{System.out.println(indent+"or: ");}
  	 or  									
  	| 										{System.out.println(indent+"and: ");} 
  	and 									
);

and: 
	{indent+="  ";}
	OUTLP cond AND cond  OUTRP
	{indent = indent.substring(2);}
;
or:	{indent+="  ";}
	OUTLP 
	cond 
	OR 
	cond 
	OUTRP
	{indent = indent.substring(2);}
;
sig:			 pubkey ;
xhx:			 bhash  ;
csv:			CSV  outParam  ;
cltv:			CLTV  outParam  ; 
outParam:		OUTNUMB ;


block: 				
	bl=buid					 
; 
issuer:				
(	
	PUBKEY_INLINED 
	| PUBKEY_MULTILN
) 	
; 
member: 			mem=MEMBER_TYPE			;
certTS: {indent="        ";}			buid					;
userID: 			uid=USERID				;

timestamp: {indent="        ";}			buid					;	

signature: 			SIGN 
					| MULTISIGN
					| (SIGN EOSIGN)
					 {nbSign++;} 
					 ; 
userid: 			USERID					; 
pubkey: 			pk=PUBKEY_INLINED 
					| PUBKEY_MULTILN 
					| OUTPUBK 
					| INHASH 				; 
					
			
currency: 			c=CURRENCY				{System.out.println("    currency: "+$c.text);}; 
version: 			v=VERSION				; 

testpubk: 			Issuer_ pubkey EOPUBK?; // (INLINE_SEP buids)* EOL ;
testsign: 			UniqueID_ signature EOSIGN?; // (INLINE_SEP buids)* EOL ;
testbuid: 			Timestamp_ buid  EOBUID?; // (INLINE_SEP buids)* EOL ;

buid:										
	bnum 									{System.out.println(indent+"buid.bnum: "+$bnum.text);}
	d=DASH_INLINED 
	bhash 	 								{System.out.println(indent+"buid.bhash: "+$bhash.text);}
;  
  bnum: 			a=NUMBER 
  					| INNUMB				 ;
  bhash: 			HASH_INLINED
  					| INHASH
  					| OUTHASH				;

  
//endpoint:			STR (' ' STR)+ INT;

  