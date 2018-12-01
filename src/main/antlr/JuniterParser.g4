parser grammar JuniterParser;

options {
	tokenVocab = JuniterLexer;
	language = Java;
}

@header { 
//package antlr.main;
//import juniter.crypto.CryptoUtils;
import java.lang.Integer;
}

@members {
	int nbIssu=0, nbInputs=0;int nbIssuersExpected=0;
	int nbSign=0;
	int maxEndpoints=3;
	int nbComm=0;
	String indent="          ";	
	int count = 0;  
	String val = "_";
	String issuerPK = "";
	String docSignature="";
	boolean isValid = true;  
	//boolean quickCheck = CryptoUtils.verify("testSignature", "i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ==", "3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa");
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
	{System.out.println("  block: ");}

	version
	{System.out.println("    version: "+$version.text );}

	DOCTYPE_BLCK currency
	{System.out.println("    currency: "+$version.text );}

	number
	{System.out.println("    number: "+$number.text );}

	powMin
	{System.out.println("    powMin: "+$powMin.text );}

	time
	{System.out.println("    time: "+$time.text );}

	medianTime
	{System.out.println("    medianTime: "+$medianTime.text );}

	(
		universalDividend
		{System.out.println("    universalDividend: "+$universalDividend.text );}

	)? unitBase
	{System.out.println("    unitBase: "+$unitBase.text );}

	issuer
	{System.out.println("    issuer: "+$issuer.text );}

	issuersFrame
	{System.out.println("    issuersFrame: "+$issuersFrame.text );}

	issuersFrameVar
	{System.out.println("    issuersFrameVar: "+$issuersFrameVar.text );}

	differentIssuersCount
	{System.out.println("    differentIssuersCount: "+$differentIssuersCount.text );}

	(previousHash
	{System.out.println("    previousHash: "+$previousHash.text );}

	previousIssuer
	{System.out.println("    previousIssuer: "+$previousIssuer.text );}

	|parameters)

	membersCount
	{System.out.println("    membersCount: "+$membersCount.text );}

	identities

	joiners

	actives

	leavers

	revoked

	excluded

	certifications

	cpt_transactions

	innerHash
	{System.out.println("    innerHash: "+$innerHash.text );}

	nonce
	{System.out.println("    nonce: "+$nonce.text );}

	(signature
	{System.out.println("    signature: "+$signature.text+ " # isValid? " );} //+ CryptoUtils.verify(rawUnsigned(_localctx.signature.start.getStartIndex()-1), $signature.text, issuerPK)) ;}
    )?
;

cpt_transactions:
	{System.out.println("    transactions: ");}

    (compactTransaction POPONE)*
;

transactions
locals [int i=0]
:
	{System.out.println("    transactions: ");}


	(
	{
			nbIssu=0;
			System.out.println("      "+ $i++ +": ");
		}
//COMPACT_TRANSACTION
		compactTransaction
	)*
;

compactTransaction
locals [int nbIn=0, int nbUn=0, int nbOut=0; ]
:
	TX version
	{System.out.println("        version: " + Integer.parseInt($version.text));}

	nbIssuer
	{ 
		nbIssuersExpected = Integer.parseInt($nbIssuer.text);
		System.out.println("        nbIssuer: " + nbIssuersExpected );
	}

	nbInput
	{$nbIn= Integer.parseInt($nbInput.text); System.out.println("        nbInput: " +$nbIn);}

	nbUnlock
	{System.out.println("        nbUnlock: " + Integer.parseInt($version.text));}

	nbOutput
	{System.out.println("        nbOutput: " + Integer.parseInt($version.text));}

	nbComment
	{nbComm = Integer.parseInt($nbComment.text); System.out.println("        nbComment: " + nbComm);}

	blockstampTime
	{System.out.println("        blockstampTime: " + Integer.parseInt($version.text));}

	{
		indent="          ";
		nbIssu=0; 
		System.out.println("        bstamp: ");
	}

	buid


	//issuers_compact
   issuers_compact+

	//{nbInputs <= 6}?
	//inputs
    cpt_in+


    cpt_unlock+

	{$nbOut++ < 2}?
	//outputs
    cpt_out+

    cpt_comm?

	cpt_signature


;

nbComment:
    BLNUMB
;

cpt_comm:
	{System.out.println("cpt_comm:  " + nbComm);}

     CPT_COMM BREAK
;

cpt_signature:
    CPT_SIGN {System.out.println("cpt_signature:  " + nbComm);}
;

cpt_out:
    CPT_NUM CPT_COL CPT_NUM CPT_COL CPT_SIG CPT_ISS BREAK
;

cpt_in:
	CPT_NUM CPT_COL CPT_NUM
	 ( CPT_DIV  CPT_ISS CPT_COL CPT_NUM
	 | CPT_TXS CPT_HASH CPT_COL CPT_NUM )

	 BREAK
;

cpt_unlock:
    CPT_NUM CPT_COL CPT_SIG CPT_NUM BREAK
;

issuers
:
	{System.out.println("    issuers: ");}

	(
		pubkey
		{System.out.println("      "+ nbIssu++ +": "+ $pubkey.text);}

	)+
;

issuers_compact
:
	{System.out.println("        issuers: ");}
	(CPT_ISS BREAK)
;

modeSwitch :
    ( {nbIssu == nbIssuersExpected}?  )
   // | ( {nbIssu < nbIssuersExpected}?  )
;

certifications
locals [int i=0]
:
	{System.out.println("    certifications: ");}

	Certifications_
	(
		{System.out.println("      "+ $i++ +": ");}

		fromPK
		{System.out.println("        from: " +$fromPK.text);}

		toPK
		{System.out.println("        to: "+$toPK.text);}

		bnum
		{System.out.println("        bnum: "+$bnum.text);}

		signature
		{System.out.println("        signature: "+$signature.text);}

	)*
;

excluded
locals [int i=0]
:
	{System.out.println("    excluded: ");}

	Excluded_
	(
		pubkey
		{System.out.println("      "+ $i++ +".pubkey: "+$pubkey.text);}

	)*
;

revoked
locals [int i=0]
:
	{System.out.println("    revoked: ");}

	Revoked_
	(
		pubkey signature
	)*
;

leavers
locals [int i=0]
:
	{System.out.println("    leavers: ");}

	Leavers_
	(
		pubkey signature mBlockUid iBlockUid userid
	)*
;

actives
locals [int i=0]
:
	{System.out.println("    actives: ");}

	Actives_
	(
		pubkey signature mBlockUid iBlockUid userid
	)*
;

joiners
locals [int i=0]
:
	{System.out.println("    joiners: ");}

	Joiners_
	(
		 cpt_joiner WOTNL
	)*
;

cpt_joiner:
    pubkey WOTSEP signature  WOTSEP mBlockUid WOTSEP iBlockUid WOTSEP userid
;

identities
locals [int i=0]
:
	{System.out.println("    identities: ");}

	Identities_
	(
		 cpt_idty
	)*
	EOWOT
;

cpt_idty:
    pubkey WOTSEP signature WOTSEP iBlockUid WOTSEP userid WOTNL
;

membersCount
:
	NUMB
;

parameters
:

	{System.out.println("    parameters: ");}

	c
	{System.out.println("      c: "+$c.text+" 		# The %growth of the UD every [dt] period ");}

	dt
	{System.out.println("      dt: "+$dt.text+" 		# Time period between two UD. ");}

	ud0
	{System.out.println("      ud0: "+$ud0.text+" # UD(0), i.e. initial Universal Dividend ");}

	sigPeriod
	{System.out.println("      sigPeriod: "+$sigPeriod.text+" # Minimum delay between 2 certifications of a same issuer, in seconds. Must be positive or zero. ");}

	sigStock
	{System.out.println("      sigStock: "+$sigStock.text+" # Maximum quantity of active certifications made by member. ");}

	sigWindow
	{System.out.println("      sigWindow: "+$sigWindow.text+" # Maximum delay a certification can wait before being expired for non-writing. ");}

    sigValidity
	{System.out.println("      sigValidity: "+$sigValidity.text+" # Maximum age of an active signature (in seconds) ");}

	sigQty
	{System.out.println("      sigQty: "+$sigQty.text+" # Minimum quantity of signatures to be part of the WoT ");}

	idtyWindow
	{System.out.println("      idtyWindow: "+$idtyWindow.text+" # Maximum delay an identity can wait before being expired for non-writing. ");}

	msWindow
	{System.out.println("      msWindow: "+$msWindow.text+" # Maximum delay a membership can wait before being expired for non-writing. ");}

    xpercent
	{System.out.println("      xpercent: "+$xpercent.text+" # Minimum % of sentries to reach to match the distance rule ");}

	msValidity
	{System.out.println("      msValidity: "+$msValidity.text+" # Maximum age of an active membership (in seconds) ");}

	stepMax
	{System.out.println("      stepMax: "+$stepMax.text+" # Maximum distance between each WoT member and a newcomer ");}

	medianTimeBlocks
	{System.out.println("      medianTimeBlocks: "+$medianTimeBlocks.text+" # Number of blocks used for calculating median time. ");}

	avgGenTime
	{System.out.println("      avgGenTime: "+$avgGenTime.text+" # The average time for writing 1 block (wished time) ");}

	dtDiffEval
	{System.out.println("      dtDiffEval: "+$dtDiffEval.text+" # The number of blocks required to evaluate again PoWMin value ");}

	percentRot
	{System.out.println("      percentRot: "+$percentRot.text+" # The % of previous issuers to reach for personalized difficulty ");}

	udTime0
	{System.out.println("      udTime0: "+$udTime0.text+" # Time of first UD. ");}

	udReevalTime0
	{System.out.println("      udReevalTime0: "+$udReevalTime0.text+" # Time of first reevaluation of the UD. ");}

	dtReeval
	{System.out.println("      dtReeval: "+$dtReeval.text+" 		# Time period between two re-evaluation of the UD. ");}

	//msPeriod
	//{System.out.println("      msPeriod: "+$msPeriod.text+" # Minimum delay between 2 memberships of a same issuer, in seconds. Must be positive or zero. ");}





	//txWindow
	//{System.out.println("      txWindow: "+$txWindow.text+" # = 3600 * 24 * 7. Maximum delay a transaction can wait before being expired for non-writing. ");}

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
	{System.out.println("    signature: "+$signature.text + " # isValid? " );} //+ CryptoUtils.verify(rawUnsigned(_localctx.signature.start.getStartIndex()-1), $signature.text, issuerPK)) ;} //+  }

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

	idtyTimestamp

	IdtySignature_ ids = idtySignature
	{System.out.println("      idtySignature: "+$ids.text);}

	{System.out.println("      certTimestamp: ");}

	CertTimestamp_ certTimestamp
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

	{System.out.println("      certTS: ");indent="        ";}

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

	idtyTimestamp IdtySignature_ idtySignature
	{System.out.println("      idtySignature: "+$idtySignature.text);}

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
	endpointType FIELD_SEP
	{System.out.println("        type: "+$endpointType.text);}

	(sessionid FIELD_SEP)?

	(
		dns FIELD_SEP
		{System.out.println("        dns: "+$dns.text);}

		| ip4 FIELD_SEP
		{System.out.println("        ip4: "+$ip4.text);}

		| ip6 FIELD_SEP
		{System.out.println("        ip6: "+$ip6.text);}

	)+ port
	{System.out.println("        port: "+$port.text);}

;

sessionid:
    SESSID
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
        SIGN_SEP
	)+
;

comment
:
	COMMENT
;


locktime
:
	NUMB
	| BLNUMB
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
		( INPUT_SEP | BREAK )
	)+
;

input
:
	amount
	{System.out.println("        amount: "+$amount.text);}

    (CPT_COL | INFIELD_SEP )

	base
	{System.out.println("        base: "+$base.text);}

	(
		(
			(DIVIDEND_TYPE | CPT_DIV)  ud
		)
		|
		(
			(TRANSACTION_TYPE  | CPT_DIV)  tx
		)
	)
;

ud
:
	{System.out.println("        ud:  # Universal Basic Income");}

	pubkey
	{System.out.println("          pubkey: "+$pubkey.text+" # of this pubkey");}

    ( CPT_COL | INFIELD_SEP )

	bnum
	{System.out.println("          bnum: "+$bnum.text+ " # at that block number ");}

;

tx
:
	{System.out.println("        tx:  # a previous Transaction");}

	bhash
	{System.out.println("          bhash: "+$bhash.text+" # in this block hash");}

    ( CPT_COL | INFIELD_SEP )

	tindex
	{System.out.println("          tindex: "+$tindex.text+" # at that index");}

;

amount
:
	INNUMB
	| OUTNUMB
	| CPT_NUM
;

base
:
	INNUMB
	| OUTNUMB
	| CPT_NUM
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
		UNLOCK_SEP
	)+
;

unlock
:
	in_index
	{System.out.println("        in_index: "+$in_index.text+"");}

	{System.out.println("        ul_condition: ");}

	(
		(UNSIG ) unsig
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
		OUTPUT_SEP
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
	SIG  OUTLP pubkey OUTRP
;

xhx
:
	XHX  OUTLP bhash OUTRP
;

csv
:
	CSV OUTLP outParam OUTRP
;

cltv
:
	CLTV OUTLP outParam OUTRP
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
	buid
;

userID
:
	uid = USERID
;

timestamp
:
	buid
;

signature
:
	SIGN
	| MULTISIGN
	| WOTSIGN

	{nbSign++;}

;

userid
:   USERID | WOTUID
;

pubkey
:
	pk = PUBKEY_INLINED
	| PUBKEY_MULTILN
	| OUTPUBK
	| WOTPUBK
	| CPT_ISS
;

currency
:
	CURRENCY
;

version
:
	VERSION
	| WOTNUMB
	| BLNUMB
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
	| WOTNUMB
	| CPT_NUM
;

bhash
:
	HASH_INLINED
	| INHASH
	| OUTHASH
;

txWindow
:
	BLNUMB
;

percentRot
:
	BLPERCENT
;

dtDiffEval
:
	BLNUMB
;

avgGenTime
:
	BLNUMB
;

medianTimeBlocks:	BLNUMB;

stepMax:            BLNUMB;

msValidity:         BLNUMB;

xpercent:	        BLPERCENT;

msWindow:       	BLNUMB;

idtyWindow:     	BLNUMB;

sigQty
:
	BLNUMB
;

sigValidity
:
	BLNUMB
;

sigWindow
:
	BLNUMB
;

sigStock
:
	BLNUMB
;

msPeriod
:
	BLNUMB
;

sigPeriod
:
	BLNUMB
;

udReevalTime0
:
	BLNUMB
;

udTime0
:
	BLNUMB
;

dtReeval
:
	BLNUMB
;

dt
:
	BLNUMB
;

c
:
	BLPERCENT
;

ud0 : BLNUMB
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
	NUMB
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

nonce
:
	BLNUMB | NUMB
;

innerHash
:
	bhash
;

certTimestamp
:
	buid
;

idtyIssuer
:
	pubkey
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

iBlockUid
:
    WOTBUID
;

mBlockUid
:
    WOTBUID
;

endpointType
:
	ENDPOINT_TYPE
;

toPK
:
	pubkey
;

blockstampTime
:
	BLNUMB
;

nbOutput
:
	BLNUMB
;

nbUnlock
:
	BLNUMB
;

nbInput
:
	BLNUMB
;

nbIssuer
:
	BLNUMB
;

fromPK
:
	pubkey
;

idtySignature
:
	signature
;