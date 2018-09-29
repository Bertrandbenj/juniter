parser grammar FullParser  ; // when messing up with parser keyword remember to rm *.java
//import PGLexer; 
options {tokenVocab = PGLexer;}


@members{
	String acceptVersion="10";
	String acceptCurrency="g1";
}

doc: 				version 
					 (( DOCTYPE_IDTY currency identity )  
					| ( DOCTYPE_MEMB currency membership ) 
					| ( DOCTYPE_REVO currency revocation )  
					| ( DOCTYPE_TRAN currency transaction ) 
					| ( DOCTYPE_PEER currency peer )
					| ( DOCTYPE_CERT currency certification ))  ;


certification: 		issuer 
					idtyIssuer 
					idtyUniqueID 
					idtyTimestamp 
					idtySignature 
					certTimestamp 
					signature ;


peer: 				pubkey 
					block 
					endpoints  ;


transaction:		blockstamp 
					locktime 
					issuers
					inputs
					unlocks
					outputs
					signatures
					comment ;


identity:			pubkey 
					userid 
					timestamp 
					signature;


membership:			issuer
					block
					member
					userID
					certTS
					signature;


revocation:			issuer
					idtyUniqueID
					idtyTimestamp
					idtySignature
					signature;


certTimestamp: 		buid;
idtyIssuer: 		pubkey;

endpoints 
locals [int i=0]
					: ( {$i<3}? enpoint {$i++;} )+
					| EOF 
					;

enpoint: 			endpointType ( dns | ip4 | ip6 )+ port 	;
  port: 			PORT;
  ip6: 				IP6;
  ip4: 				IP4;
  dns: 				DNS;
  endpointType: 	ENDPOINT_TYPE;



idtySignature: 		signature;
idtyTimestamp: 		buid;
idtyUniqueID: 		USERID;

unlocks: 			(unlock UNLOCK_SEP)+;
signatures: 		(signature SIGN_SEP)+;
comment: 			COMMENT;
outputs: 			(output OUTPUT_SEP)+;
inputs: 			(input INPUT_SEP)+;
issuers: 			(issuer ISSUER_SEP)+;
locktime: 			NUMB;
blockstamp: 		NUMB;



input:				inAmount INFIELD_SEP inBase INFIELD_SEP 
					( 
						(DIVIDEND_TYPE INFIELD_SEP inDHash INFIELD_SEP inDBlock)
						|
						(TRANSACTION_TYPE INFIELD_SEP inTHash INFIELD_SEP inTBlock)
					);	
  inTHash: 			INHASH;
  inTBlock: 		INNUMB;
  inDHash: 			INHASH;
  inDBlock: 		INNUMB;
  inBase: 			INNUMB;
  inAmount: 		INNUMB;

unlock:				unIndex UNLOCK_FIELD_SEP (unsig | unxhx);
  unIndex:			UNNUMB;
  unsig:			UNSIG UNLP unlockParam UNRP;
  unxhx:			UNXHX UNLP unlockParam UNRP;
  unlockParam: 		UNNUMB;

output:				outAmount OUTPUT_FIELD_SEP outBase OUTPUT_FIELD_SEP cond;
  outBase: 			OUTNUMB;
  outAmount: 		OUTNUMB;
  outParam:			OUTHASH | OUTNUMB ;
  cond:				(sig | xhx | csv | cltv | or | and);
    and:			OUTLP cond  AND  cond OUTRP;
    or:				OUTLP cond  OR  cond OUTRP;
    sig:			SIG OUTLP pubkey OUTRP;
    xhx:			XHX OUTLP outParam OUTRP;
    csv:			CSV OUTLP outParam OUTRP;
    cltv:			CLTV OUTLP outParam OUTRP; 
  

block: 				bl=buid					 ; 
issuer: 			i=(	PUBKEY_INLINED 
					| 	PUBKEY_MULTILN)		{System.out.println("issuer "+":"+$i.text+";");} ; 
member: 			mem=MEMBER_TYPE			{System.out.println("membership "+":"+$mem+";");};
certTS: 			buid					;
userID: 			uid=USERID				{System.out.println("userId "+":"+$uid+";");};

timestamp: 			buid					;	

signature: 			s=(SIGN 
					| MULTISIGN
					
					)						{System.out.println("signature "+":"+$s.text+";");} ; 
userid: 			USERID					{System.out.println("userid "+":"+$USERID+";");} ; 
pubkey: 			pk=PUBKEY_INLINED 		{System.out.println("pubkey "+":"+$pk+";");} ; 
currency: 			c=CURRENCY				{System.out.println("currency "+acceptCurrency+" .equals?"+(acceptVersion.equals($c.text))+";");} ; 
version: 			v=VERSION				{System.out.println("version "+acceptVersion+" .equals?"+(acceptVersion.equals($v.text)));} ; 

testpubk: 			Issuer_ pubkey EOPUBK?; // (INLINE_SEP buids)* EOL ;
testsign: 			UniqueID_ signature EOSIGN?; // (INLINE_SEP buids)* EOL ;
testbuid: 			Timestamp_ buid  EOBUID?; // (INLINE_SEP buids)* EOL ;

buid:				bnum d=DASH_INLINED bhash 	{System.out.println("buid "+$bnum.text+$d.text+$bhash.text+";");} ;  // BUID; 
  bnum: 			a=NUMBER 					{System.out.println("bnum "+":"+$a+";");} ; 
  bhash: 			HASH_INLINED				{System.out.println("bhash "+$HASH_INLINED+":"+";");};

  
//endpoint:			STR (' ' STR)+ INT;

  