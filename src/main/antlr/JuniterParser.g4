parser grammar JuniterParser;

options {
	tokenVocab = JuniterLexer;
	language = Java;
}

@header { 
package antlr.main; 
import juniter.crypto.CryptoUtils;
}

@members {
	int nbIssu=0;
	int nbSign=0;
	int maxEndpoints=3;
	String indent="          ";	
	int count = 0;  
	String val = "_";
	String issuerPK = "";
	String docSignature="";
	boolean isValid = true;  
	boolean quickCheck = CryptoUtils.verify("testSignature", "i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ==", "3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa");
	public String rawUnsigned (int lastChar){
		CharStream cs = _ctx.start.getTokenSource().getInputStream();
		return cs.getText(
		  	Interval.of(
		  		0, 
		  		lastChar
		  	));
	}
}

doc
:
	{System.out.println("doc: " );}

	wot
	| transaction
	| peer
	| block_
;

block_
:
	version DOCTYPE_BLCK currency number powMin time medianTime universalDividend
	unitBase issuer issuersFrame issuersFrameVar differentIssuersCount
	previousHash previousIssuer parameters membersCount identities joiners actives
	leavers revoked excluded certifications transactions innerHash nonce signature
;

nonce
:
;

innerHash
:
	bhash
;

transactions
:
//Transactions:
//COMPACT_TRANSACTION

;

certifications
:
//Certifications:
//PUBKEY_FROM:PUBKEY_TO:BLOCK_ID:SIGNATURE

;

excluded
:

//Excluded:
//PUBLIC_KEY

;

revoked
:
//Revoked:
//PUBLIC_KEY:SIGNATURE

;

leavers
:
//Leavers:
//PUBLIC_KEY:SIGNATURE:M_BLOCK_UID:I_BLOCK_UID:USER_ID

;

actives
:
//Actives:
//PUBLIC_KEY:SIGNATURE:M_BLOCK_UID:I_BLOCK_UID:USER_ID

;

joiners
:

//Joiners:
//PUBLIC_KEY:SIGNATURE:M_BLOCK_UID:I_BLOCK_UID:USER_ID

;

identities
:
//Identities:
//PUBLIC_KEY:SIGNATURE:I_BLOCK_UID:USER_ID

;

membersCount
:
	NUMB
;

parameters
:
//Parameters: PARAMETERS

;

previousIssuer
:
	pubkey
;

previousHash
:
	bhash
;

differentIssuersCount
:
	pubkey
;

issuersFrameVar
:
	NUMB
;

issuersFrame
:
	NUMB
;

unitBase
:
	NUMB
;

universalDividend
:
	NUMB
;

medianTime
:
	NUMB
;

time
:
	NUMB
;

powMin
:
	NUMB
;

number
:
	NUMB
;

peer
:
	{System.out.println("  peer: ");}

	version
	{System.out.println("    version: "+$version.text );}

	DOCTYPE_PEER currency
	{System.out.println("    currency: "+$currency.text);}

	pubkey
	{System.out.println("    pubkey: "+$pubkey.text);}

	{System.out.println("    block: ");indent="      ";}

	block endpoints
;

transaction
:
	{System.out.println("  transaction:");}

	version
	{System.out.println("    version: "+$version.text );}

	DOCTYPE_TRAN currency
	{System.out.println("    currency: "+$currency.text);}

	blockstamp
	{System.out.println("    blockstamp: "+$blockstamp.text);}

	locktime
	{System.out.println("    locktime: "+$locktime.text);}

	issuers inputs unlocks outputs signatures comment
	{System.out.println("    comment: "+$comment.text);}

;

wot
:
	{System.out.println("  wot: 		#  Identity,Certification, Membership, Revocation"); }

	(
		identity
		| membership
		| revocation
		| certification
	) signature
	{System.out.println("    signature: "+$signature.text + " # isValid? " + CryptoUtils.verify(rawUnsigned(_localctx.signature.start.getStartIndex()-1), $signature.text, issuerPK)) ;} //+  }

;

identity
:
	{System.out.println("    identity:");}

	version
	{System.out.println("      version: "+$version.text );}

	DOCTYPE_IDTY currency
	{System.out.println("      currency: "+$currency.text);}

	issuer
	{System.out.println("      issuer: "+$issuer.text); }

	userid
	{System.out.println("      userid: "+$userid.text);}

	{System.out.println("      timestamp: ");}

	timestamp
;

certification
:
	{System.out.println("    certification: ");}

	version
	{System.out.println("      version: "+$version.text );}

	DOCTYPE_CERT currency
	{System.out.println("      currency: "+$currency.text);}

	iss = issuer
	{System.out.println("      issuer: "+$iss.text);}

	idi = idtyIssuer
	{System.out.println("      idtyIssuer: "+$idi.text);}

	uid = idtyUniqueID
	{System.out.println("      idtyUniqueID: "+$uid.text);}

	{System.out.println("      idtyTimestamp: ");}

	idtyTimestamp ids = idtySignature
	{System.out.println("      idtySignature: "+$ids.text);}

	{System.out.println("      certTimestamp: ");}

	certTimestamp
;

membership
:
	{System.out.println("    membership:");}

	version
	{System.out.println("      version: "+$version.text );}

	DOCTYPE_MEMB currency
	{System.out.println("      currency: "+$currency.text);}

	issuer
	{System.out.println("      issuer: "+$issuer.text);}

	{System.out.println("      block: "); indent="        ";}

	block member
	{System.out.println("      member: "+$member.text);}

	userID
	{System.out.println("      userID: "+$userID.text);}

	{System.out.println("      certTS: ");}

	certTS
;

revocation
:
	{System.out.println("    revocation:");}

	version
	{System.out.println("      version: "+$version.text );}

	DOCTYPE_REVO currency
	{System.out.println("      currency: "+$currency.text);}

	issuer
	{System.out.println("      issuer: "+$issuer.text);}

	idtyUniqueID
	{System.out.println("      idtyUniqueID: "+$idtyUniqueID.text);}

	{System.out.println("      idtyTimestamp: "); indent="        ";}

	idtyTimestamp idtySignature
	{System.out.println("      idtySignature: "+$idtySignature.text);}

;

certTimestamp
:
	buid
;

idtyIssuer
:
	pubkey
;

endpoints
locals [int i=0]
:
	{System.out.println("    endpoints: ");}

	(
		{$i<maxEndpoints}?

		{System.out.println("      "+ $i++ +": ");}

		enpoint
	)+
	| EOF
;

enpoint
:
	endpointType
	{System.out.println("        type: "+$endpointType.text);}

	(
		dns
		{System.out.println("        dns: "+$dns.text);}

		| ip4
		{System.out.println("        ip4: "+$ip4.text);}

		| ip6
		{System.out.println("        ip6: "+$ip6.text);}

	)+ port
	{System.out.println("        port: "+$port.text);}

;

port
:
	PORT
;

ip6
:
	IP6
;

ip4
:
	IP4
;

dns
:
	DNS
;

endpointType
:
	ENDPOINT_TYPE
;

idtySignature
:
	signature
;

idtyTimestamp
:
	{indent="        ";}

	buid
;

idtyUniqueID
:
	USERID
;

signatures
locals [int i=0]
:
	{System.out.println("    signatures: ");}

	(
		{ $i < nbIssu }?

		signature
		{System.out.println("      "+ $i++ +": "+$signature.text);}

	)+
;

comment
:
	COMMENT
;

issuers
:
	{System.out.println("    issuers: ");}

	(
		pubkey
		{System.out.println("      "+ nbIssu++ +": "+ $pubkey.text);}

	)+
;

locktime
:
	NUMB
;

blockstamp
:
	NUMB
;

inputs
locals [int i=0]
:
	{System.out.println("    inputs: ");}

	(
		{System.out.println("      "+ $i++ +": ");}

		input
	)+
;

input
:
	amount
	{System.out.println("        amount: "+$amount.text);}

	base
	{System.out.println("        base: "+$base.text);}

	(
		(
			DIVIDEND_TYPE ubi
		)
		|
		(
			TRANSACTION_TYPE tx
		)
	)
;

ubi
:
	{System.out.println("        ubi:  # Universal Basic Income");}

	pubkey
	{System.out.println("          pubkey: "+$pubkey.text+" # of this pubkey");}

	bnum
	{System.out.println("          bnum: "+$bnum.text+ " # at that block number ");}

;

tx
:
	{System.out.println("        tx:  # a previous Transaction");}

	bhash
	{System.out.println("          bhash: "+$bhash.text+" # in this block hash");}

	tindex
	{System.out.println("          tindex: "+$tindex.text+" # at that index");}

;

amount
:
	INNUMB
	| OUT_AMOUT_BASE
;

base
:
	INNUMB
	| OUT_AMOUT_BASE
;

tindex
:
	INNUMB
;

unlocks
locals [int i=0]
:
	{System.out.println("    unlocks: ");}

	(
		{System.out.println("      "+ $i++ +": ");}

		unlock
	)+
;

unlock
:
	in_index
	{System.out.println("        in_index: "+$in_index.text+"");}

	{System.out.println("        ul_condition: ");}

	(
		UNSIG unsig
		{System.out.println("          unsig: "+$unsig.text+"");}

		| UNXHX unxhx
		{System.out.println("          unxhx: "+$unxhx.text+"");}

	)
;

in_index
:
	UNNUMB
;

unsig
:
	UNNUMB
;

unxhx
:
	UNNUMB
;

//SIG(PUBLIC_KEY), XHX(SHA256_HASH), CLTV(INTEGER), CSV(INTEGER)

outputs
locals [int i=0]
:
	{System.out.println("    outputs: ");}

	(
		{System.out.println("      "+$i++ +": ");}

		output
	)+
;

output
:
	amount
	{System.out.println("        amount: "+$amount.text+"");}

	base
	{System.out.println("        base: "+$base.text+"");}

	{System.out.println("        condition: ");}

	cond
;

cond
:
	(
		sig
		{System.out.println(indent+"sig: "+$sig.text+"");}

		| xhx
		{System.out.println(indent+"xhx: "+$xhx.text+"");}

		| csv
		{System.out.println(indent+"csv: "+$csv.text+"");}

		| cltv
		{System.out.println(indent+"cltv: "+$cltv.text+"");}

		|
		{System.out.println(indent+"or: ");}

		or
		|
		{System.out.println(indent+"and: ");}

		and
	)
;

and
:
	{indent+="  ";}

	OUTLP cond AND cond OUTRP
	{indent = indent.substring(2);}

;

or
:
	{indent+="  ";}

	OUTLP cond OR cond OUTRP
	{indent = indent.substring(2);}

;

sig
:
	pubkey
;

xhx
:
	bhash
;

csv
:
	CSV outParam
;

cltv
:
	CLTV outParam
;

outParam
:
	OUTNUMB
;

block
:
	bl = buid
;

issuer
:
	iss =
	(
		PUBKEY_INLINED
		| PUBKEY_MULTILN
	)
	{ issuerPK = $iss.text; }

;

member
:
	mem = MEMBER_TYPE
;

certTS
:
	{indent="        ";}

	buid
;

userID
:
	uid = USERID
;

timestamp
:
	{indent="      ";}

	buid
;

signature
:
	SIGN
	| MULTISIGN
	|
	(
		SIGN EOSIGN
	)
	{nbSign++;}

;

userid
:
	USERID
;

pubkey
:
	pk = PUBKEY_INLINED
	| PUBKEY_MULTILN
	| OUTPUBK
	| INHASH
;

currency
:
	CURRENCY
;

version
:
	v = VERSION
;

testpubk
:
	Issuer_ pubkey EOPUBK?
; // (INLINE_SEP buids)* EOL ;

testsign
:
	UniqueID_ signature EOSIGN?
; // (INLINE_SEP buids)* EOL ;

testbuid
:
	Timestamp_ buid EOBUID?
; // (INLINE_SEP buids)* EOL ;

buid
:
	bnum
	{System.out.println(indent+"buid.bnum: "+$bnum.text);}

	d = DASH_INLINED bhash
	{System.out.println(indent+"buid.bhash: "+$bhash.text);}

;

bnum
:
	a = NUMBER
	| INNUMB
;

bhash
:
	HASH_INLINED
	| INHASH
	| OUTHASH
;

//endpoint:			STR (' ' STR)+ INT;

