parser grammar PGParser  ; // when messing up with parser keyword remember to rm *.java
//import PGLexer; 
options {tokenVocab = PGLexer;}


@members{
	String acceptVersion="10";
	String acceptCurrency="g1";
}

doc: 				version ( identity | membership | revocation | transaction ) ;
					
					
transaction:		DT_TRAN 
					currency 
					blockstamp 
					locktime 
					issuers
					inputs
					unlocks
					outputs
					signatures
					comment ;

identity:			DT_IDTY 
					currency 
					pubkey 
					userid 
					timestamp 
					signature;

membership:			DT_MEMB
					currency
					issuer
					block
					member
					userID
					certTS
					signature;
					
revocation:			DT_REVO
					currency
					issuer
					idtyUniqueID
					idtyTimestamp
					idtySignature
					signature;


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



input:				inAmount INPUT_FIELD_SEP inBase INPUT_FIELD_SEP
					(
						(DIVIDEND_TYPE INPUT_FIELD_SEP HASH INPUT_FIELD_SEP INT) 
						| 	
						(TRANSACTION_TYPE INPUT_FIELD_SEP inTHash INPUT_FIELD_SEP inTBlock)
					);

inTHash: 			HASH;
inTBlock: 			INNUMB;
inBase: 			INNUMB;
inAmount: 			INNUMB;

unlock:				UNNUMB UNLOCK_FIELD_SEP (unsig | unxhx);
unsig:				SIG LP UNNUMB RP;
unxhx:				XHX LP UNNUMB RP;
output:				outAmount OUTPUT_FIELD_SEP outBase OUTPUT_FIELD_SEP cond;

outBase: 			OUTNUMB;
outAmount: 			OUTNUMB;

//endpoint:			STR (' ' STR)+ INT;




// ====== OUTUTS CONDITION 
cond:				(sig | xhx | csv | cltv | or | and);
  and:				LP cond  AND  cond RP;
  or:				LP cond  OR  cond RP;
  sig:				SIG LP pubkey RP;
  xhx:				XHX LP HASH RP;
  csv:				CSV LP INT RP;
  cltv:				CLTV LP INT RP; 


block: 				bl=buid					 ; 
issuer: 			i=(	PUBKEY_INLINED 
					| 	PUBKEY_MULTILN)		{System.out.println("issuer "+":"+$i+";");} ; 
member: 			mem=MEMBER_TYPE			{System.out.println("membership "+":"+$mem+";");};
certTS: 			buid					;
userID: 			uid=USERID				{System.out.println("userId "+":"+$uid+";");};

timestamp: 			buid					;	
typeIdty: 			DT_IDTY					{System.out.println("type "+":"+$DT_IDTY+";");} ; 
typeMemb: 			DT_MEMB;
signature: 			SIGN					{System.out.println("signature "+":"+$SIGN+";");} ; 
userid: 			USERID					{System.out.println("userid "+":"+$USERID+";");} ; 
pubkey: 			pk=PUBKEY_INLINED 		{System.out.println("pubkey "+":"+$pk+";");} ; 
currency: 			c=CURRENCY				{System.out.println("currency "+acceptCurrency+" .equals?"+(acceptVersion.equals($c.text))+";");} ; 
version: 			v=VERSION				{System.out.println("version "+acceptVersion+" .equals?"+(acceptVersion.equals($v.text)));} ; 

testpubk: 			Issuer_ pubkey EOPUBK?; // (INLINE_SEP buids)* EOL ;
testsign: 			UniqueID_ signature  EOSIGN?; // (INLINE_SEP buids)* EOL ;
testbuid: 			Timestamp_ buid  EOBUID?; // (INLINE_SEP buids)* EOL ;

buid:				bnum d=DASH_INLINED bhash 	{System.out.println("buid "+$bnum.text+$d.text+$bhash.text+";");} ;  // BUID; 
  bnum: 			a=NUMBER 					{System.out.println("bnum "+":"+$a+";");} ; 
  bhash: 			HASH_INLINED				{System.out.println("bhash "+$HASH_INLINED+":"+";");};  
