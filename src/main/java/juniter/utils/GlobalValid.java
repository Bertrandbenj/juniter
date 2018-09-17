package juniter.utils;

/**
 *
 * @author BnimajneB
 *
 *         <pre>
 *         Global
 *
 *         Global validation verifies the coherence of a locally-validated
 *         block, in the context of the whole blockchain, including the block.
 *
 *
 *         INDEX GENERATION
 *
 *
 *         Definitions
 *
 *         Here are some references that will be used in next sections.
 *
 *         BINDEX references:
 *
 *
 *
 *         HEAD: the BINDEX top entry (generated for incoming block, precisely)
 *
 *         HEAD~1: the BINDEX 1st entry before HEAD (= entry where ENTRY.number
 *         = HEAD.number - 1)
 *
 *         HEAD~n: the BINDEX nth entry before HEAD (= entry where ENTRY.number
 *         = HEAD.number - n)
 *
 *         HEAD~n[field=value, ...]: the BINDEX entry at HEAD~n if it fulfills
 *         the condition, null otherwise
 *
 *         HEAD~n..m[field=value, ...]: the BINDEX entries between HEAD~n and
 *         HEAD~m, included, where each entry fulfills the condition
 *
 *         HEAD~n.property: get a BINDEX entry property. Ex.: HEAD~1.hash looks
 *         at the hash of the entry preceding HEAD.
 *
 *         (HEAD~n..m).property: get all the values of property in BINDEX for
 *         entries between HEAD~n and HEAD~m, included.
 *
 *         (HEAD~..).property: same, but and are variables to be computed
 *
 *
 *         Function references:
 *
 *
 *
 *         COUNT returns the number of values in a list of values
 *
 *         AVG computes the average value in a list of values, floor rounded.
 *
 *         MEDIAN computes the median value in a list of values
 *
 *         MAX computes the maximum value in a list of values
 *
 *
 *
 *         If values count is even, the median is computed over the 2 centered
 *         values by an arithmetical median on them, NOT rounded.
 *
 *
 *
 *
 *         UNIQ returns a list of the unique values in a list of values
 *
 *         PICK returns a list of the values by picking a particular property on
 *         each record
 *
 *         INTEGER_PART return the integer part of a number
 *
 *         FIRST return the first element in a list of values matching the given
 *         condition
 *
 *         REDUCE merges a set of elements into a single one, by extending the
 *         non-null properties from each record into the resulting record.
 *
 *         REDUCE_BY merges a set of elements into a new set, where each new
 *         element is the reduction of the first set sharing a given key.
 *
 *         CONCAT concatenates two sets of elements into a new one
 *
 *
 *
 *         If there is no elements, all its properties are null.
 *
 *
 *
 *
 *         NUMBER get the number part of blockstamp
 *
 *         HASH get the hash part of blockstamp
 *
 *
 *
 *         HEAD
 *
 *         The block produces 1 new entry:
 *
 *         BINDEX ( version = Version size = BlockSize hash = BlockHash issuer =
 *         Issuer time = Time number = null currency = null previousHash = null
 *         previousIssuer = null membersCount = null issuersCount = null
 *         issuersFrame = null issuersFrameVar = null issuerDiff = null
 *         avgBlockSize = null medianTime = null dividend = null mass = null
 *         massReeval = null unitBase = null powMin = PowMin udTime = null
 *         diffTime = null speed = null ) This entry will be refered to as HEAD
 *         in the following sections, and is added on the top of the BINDEX.
 */
public interface GlobalValid {

	/**
	 * <pre>
	 * BR_G01 - HEAD.number
	 *
	 * If HEAD~1 is defined:
	 *
	 * HEAD.number = HEAD~1.number + 1 Else:
	 *
	 * HEAD.number = 0
	 *
	 */
	void BR_G01();

	/**
	 * <pre>
	 * BR_G02 - HEAD.previousHash
	 *
	 * If HEAD.number > 0:
	 *
	 * HEAD.previousHash = HEAD~1.hash Else:
	 *
	 * HEAD.previousHash = null
	 *
	 */
	void BR_G02();

	/**
	 * <pre>
	 *
	 * BR_G03 - HEAD.previousIssuer
	 *
	 * If HEAD.number > 0:
	 *
	 * HEAD.previousIssuer = HEAD~1.issuer Else:
	 *
	 * HEAD.previousIssuer = null
	 */
	void BR_G03();

	/**
	 * <pre>
	 * BR_G04 - HEAD.issuersCount
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.issuersCount = 0 Else:
	 *
	 * HEAD.issuersCount = COUNT(UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer))
	 *
	 */
	void BR_G04();

	/**
	 * <pre>
	 * BR_G05 - HEAD.issuersFrame
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.issuersFrame = 1 Else if HEAD~1.issuersFrameVar > 0:
	 *
	 * HEAD.issuersFrame = HEAD~1.issuersFrame + 1 Else if HEAD~1.issuersFrameVar <
	 * 0:
	 *
	 * HEAD.issuersFrame = HEAD~1.issuersFrame - 1 Else:
	 *
	 * HEAD.issuersFrame = HEAD~1.issuersFrame
	 *
	 */
	void BR_G05();

	/**
	 * <pre>
	 * BR_G06 - HEAD.issuersFrameVar
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.issuersFrameVar = 0 Else if HEAD~1.issuersFrameVar > 0:
	 *
	 * HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount -
	 * HEAD~1.issuersCount) - 1 Else if HEAD~1.issuersFrameVar < 0:
	 *
	 * HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount -
	 * HEAD~1.issuersCount) + 1 Else:
	 *
	 * HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount -
	 * HEAD~1.issuersCount)
	 *
	 */
	void BR_G06();

	/**
	 * <pre>
	 * BR_G07 - HEAD.avgBlockSize
	 *
	 * HEAD.avgBlockSize = AVG((HEAD~1..<HEAD.issuersCount>).size)
	 *
	 */
	void BR_G07();

	/**
	 * <pre>
	 * BR_G08 - HEAD.medianTime
	 *
	 * If HEAD.number > 0:
	 *
	 * HEAD.medianTime = MEDIAN((HEAD~1..<MIN(medianTimeBlocks, HEAD.number)>).time)
	 * Else:
	 *
	 * HEAD.medianTime = HEAD.time
	 *
	 */
	void BR_G08();

	/**
	 * <pre>
	 * BR_G09 - HEAD.diffNumber
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.diffNumber = HEAD.number + dtDiffEval Else if HEAD~1.diffNumber <=
	 * HEAD.number:
	 *
	 * HEAD.diffNumber = HEAD~1.diffNumber + dtDiffEval Else:
	 *
	 * HEAD.diffNumber = HEAD~1.diffNumber
	 *
	 */
	void BR_G09();

	/**
	 * <pre>
	 * BR_G10 - HEAD.membersCount
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.membersCount = COUNT(LOCAL_IINDEX[member=true]) Else:
	 *
	 * HEAD.membersCount = HEAD~1.membersCount + COUNT(LOCAL_IINDEX[member=true]) -
	 * COUNT(LOCAL_IINDEX[member=false])
	 *
	 */
	void BR_G10();

	/**
	 * <pre>
	 * BR_G100 - HEAD.issuerIsMember
	 * 
	 * If HEAD.number > 0:
	 * 
	 * HEAD.issuerIsMember = REDUCE(GLOBAL_IINDEX[pub=HEAD.issuer]).member Else:
	 * 
	 * HEAD.issuerIsMember = REDUCE(LOCAL_IINDEX[pub=HEAD.issuer]).member
	 */
	void BR_G100();

	/**
	 * <pre>
	 * BR_G101 - Issuer
	 *
	 * Rule:
	 *
	 * HEAD.issuerIsMember == true
	 */
	void BR_G101();

	/**
	 * <pre>
	 * BR_G102 - ENTRY.age
	 *
	 * For each ENTRY in local SINDEX where op = 'UPDATE':
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 -
	 * NUMBER(ENTRY.hash)>[hash=HASH(ENTRY.created_on)] If HEAD.number == 0 &&
	 * ENTRY.created_on ==
	 * '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *
	 * ENTRY.age = 0 Else if REF_BLOC != null:
	 *
	 * ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime Else:
	 *
	 * ENTRY.age = conf.txWindow + 1 EndIf
	 */
	void BR_G102();

	/**
	 * <pre>
	 * BR_G103 - Trancation writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [txWindow]
	 */
	void BR_G103();

	/**
	 * <pre>
	 * BR_G104 - Membership expiry date correction
	 *
	 * For each LOCAL_MINDEX[type='JOIN'] as MS:
	 *
	 * MS.expires_on = MS.expires_on - MS.age MS.revokes_on = MS.revokes_on - MS.age
	 * For each LOCAL_MINDEX[type='ACTIVE'] as MS:
	 *
	 * MS.expires_on = MS.expires_on - MS.age MS.revokes_on = MS.revokes_on - MS.age
	 */
	void BR_G104();

	/**
	 * <pre>
	 * BR_G105 - Certification expiry date correction
	 *
	 * For each LOCAL_CINDEX as CERT:
	 *
	 * CERT.expires_on = CERT.expires_on - CERT.age
	 */
	void BR_G105();

	/**
	 * <pre>
	 * BR_G106 - Low accounts
	 *
	 * Set:
	 *
	 * ACCOUNTS = UNIQ(GLOBAL_SINDEX, 'conditions') For each ACCOUNTS as ACCOUNT
	 * then:
	 *
	 * Set:
	 *
	 * ALL_SOURCES = CONCAT(GLOBAL_SINDEX[conditions=ACCOUNT.conditions],
	 * LOCAL_SINDEX[conditions=ACCOUNT.conditions]) SOURCES = REDUCE_BY(ALL_SOURCES,
	 * 'identifier', 'pos')[consumed=false] BALANCE = SUM(MAP(SOURCES => SRC:
	 * SRC.amount * POW(10, SRC.base))) If BALANCE < 100 * POW(10, HEAD.unitBase),
	 * then for each SOURCES AS SRC add a new LOCAL_SINDEX entry:
	 *
	 * SINDEX ( op = 'UPDATE' identifier = SRC.identifier pos = SRC.pos written_on =
	 * BLOCKSTAMP written_time = MedianTime consumed = true )
	 */
	void BR_G106();

	/**
	 * <pre>
	 * BR_G107 - ENTRY.unchainables
	 *
	 * If HEAD.number > 0 AND ENTRY.revocation == null:
	 *
	 * ENTRY.unchainables = COUNT(GLOBAL_MINDEX[issuer=ENTRY.issuer, chainable_on >
	 * HEAD~1.medianTime]))
	 *
	 * Local CINDEX augmentation
	 *
	 * For each ENTRY in local CINDEX:
	 */
	void BR_G107();

	/**
	 * <pre>
	 * BR_G108 - Membership period
	 *
	 * Rule:
	 *
	 * ENTRY.unchainables == 0
	 */
	void BR_G108();

	/**
	 * <pre>
	 * BR_G11 - HEAD.udTime and HEAD.udReevalTime
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.udTime = udTime0 Else if HEAD~1.udTime <= HEAD.medianTime:
	 *
	 * HEAD.udTime = HEAD~1.udTime + dt Else:
	 *
	 * HEAD.udTime = HEAD~1.udTime EndIf
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.udReevalTime = udReevalTime0 Else if HEAD~1.udReevalTime <=
	 * HEAD.medianTime:
	 *
	 * HEAD.udReevalTime = HEAD~1.udReevalTime + dtReeval Else:
	 *
	 * HEAD.udReevalTime = HEAD~1.udReevalTime EndIf
	 *
	 */
	void BR_G11();

	/**
	 * <pre>
	 * BR_G12 - HEAD.unitBase
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.unitBase = 0 Else:
	 *
	 * HEAD.unitBase = HEAD~1.unitBase
	 *
	 */
	void BR_G12();

	/**
	 * <pre>
	 * BR_G13 - HEAD.dividend and HEAD.new_dividend
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.dividend = ud0 Else If HEAD.udReevalTime != HEAD~1.udReevalTime:
	 *
	 * HEAD.dividend = HEAD_1.dividend + c² * CEIL(HEAD~1.massReeval / POW(10,
	 * HEAD~1.unitbase)) / HEAD.membersCount) Else:
	 *
	 * HEAD.dividend = HEAD~1.dividend EndIf
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.new_dividend = null Else If HEAD.udTime != HEAD~1.udTime:
	 *
	 * HEAD.new_dividend = HEAD.dividend Else:
	 *
	 * HEAD.new_dividend = null
	 *
	 */
	void BR_G13();

	/**
	 * <pre>
	 * BR_G14 - HEAD.dividend and HEAD.unitbase and HEAD.new_dividend
	 *
	 * If HEAD.dividend >= 1000000 :
	 *
	 * HEAD.dividend = CEIL(HEAD.dividend / 10) HEAD.new_dividend = HEAD.dividend
	 * HEAD.unitBase = HEAD.unitBase + 1
	 *
	 *
	 *
	 */
	void BR_G14();

	/**
	 * <pre>
	 * BR_G15 - HEAD.mass and HEAD.massReeval
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.mass = 0 Else if HEAD.udTime != HEAD~1.udTime:
	 *
	 * HEAD.mass = HEAD~1.mass + HEAD.dividend * POWER(10, HEAD.unitBase) *
	 * HEAD.membersCount Else:
	 *
	 * HEAD.mass = HEAD~1.mass EndIf
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.massReeval = 0 Else if HEAD.udReevalTime != HEAD~1.udReevalTime:
	 *
	 * HEAD.massReeval = HEAD~1.mass Else:
	 *
	 * HEAD.massReeval = HEAD~1.massReeval EndIf
	 *
	 *
	 * Functionnally: the UD is reevaluated on the preceding monetary mass
	 * (important!)
	 *
	 */
	void BR_G15();

	/**
	 * <pre>
	 * BR_G16 - HEAD.speed
	 *
	 * If HEAD.number == 0:
	 *
	 * speed = 0 Else:
	 *
	 * range = MIN(dtDiffEval, HEAD.number) elapsed = (HEAD.medianTime -
	 * HEAD~<range>.medianTime) EndIf
	 *
	 * If elapsed == 0:
	 *
	 * speed = 100 Else: speed = range / elapsed
	 *
	 */
	void BR_G16();

	/**
	 * <pre>
	 * BR_G17 - HEAD.powMin
	 *
	 * If HEAD.number > 0 AND HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed >=
	 * maxSpeed AND (HEAD~1.powMin + 2) % 16 == 0:
	 *
	 * HEAD.powMin = HEAD~1.powMin + 2 Else if HEAD.number > 0 AND HEAD.diffNumber
	 * != HEAD~1.diffNumber AND HEAD.speed >= maxSpeed:
	 *
	 * HEAD.powMin = HEAD~1.powMin + 1 Else if HEAD.number > 0 AND HEAD.diffNumber
	 * != HEAD~1.diffNumber AND HEAD.speed <= minSpeed AND (HEAD~1.powMin) % 16 ==
	 * 0:
	 *
	 * HEAD.powMin = MAX(0, HEAD~1.powMin - 2) Else if HEAD.number > 0 AND
	 * HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed <= minSpeed:
	 *
	 * HEAD.powMin = MAX(0, HEAD~1.powMin - 1) Else if HEAD.number > 0:
	 *
	 * HEAD.powMin = HEAD~1.powMin
	 */
	void BR_G17();

	/**
	 * <pre>
	 * BR_G18 - HEAD.powZeros and HEAD.powRemainder
	 *
	 * If HEAD.number == 0:
	 *
	 * nbPersonalBlocksInFrame = 0 medianOfBlocksInFrame = 1 Else:
	 *
	 * blocksOfIssuer = HEAD~1..<HEAD~1.issuersFrame>[issuer=HEAD.issuer]
	 * nbPersonalBlocksInFrame = COUNT(blocksOfIssuer) blocksPerIssuerInFrame = MAP(
	 * UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer) => ISSUER:
	 * COUNT(HEAD~1..<HEAD~1.issuersFrame>[issuer=ISSUER])) medianOfBlocksInFrame =
	 * MEDIAN(blocksPerIssuerInFrame) EndIf
	 *
	 * If nbPersonalBlocksInFrame == 0:
	 *
	 * nbPreviousIssuers = 0 nbBlocksSince = 0 Else:
	 *
	 * last = FIRST(blocksOfIssuer) nbPreviousIssuers = last.issuersCount
	 * nbBlocksSince = HEAD~1.number - last.number EndIf
	 *
	 * PERSONAL_EXCESS = MAX(0, ( (nbPersonalBlocksInFrame + 1) /
	 * medianOfBlocksInFrame) - 1) PERSONAL_HANDICAP = FLOOR(LN(1 + PERSONAL_EXCESS)
	 * / LN(1.189)) HEAD.issuerDiff = MAX [ HEAD.powMin ; HEAD.powMin * FLOOR
	 * (percentRot * nbPreviousIssuers / (1 + nbBlocksSince)) ] + PERSONAL_HANDICAP
	 * If (HEAD.issuerDiff + 1) % 16 == 0:
	 *
	 * HEAD.issuerDiff = HEAD.issuerDiff + 1 EndIf
	 *
	 * Finally:
	 *
	 * HEAD.powRemainder = HEAD.issuerDiff % 16 HEAD.powZeros = (HEAD.issuerDiff -
	 * HEAD.powRemainder) / 16
	 *
	 * Local IINDEX augmentation
	 *
	 */
	void BR_G18();

	/**
	 * <pre>
	 * BR_G19 - ENTRY.age
	 *
	 * For each ENTRY in local IINDEX where op = 'CREATE':
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 -
	 * NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)] If HEAD.number == 0 &&
	 * ENTRY.created_on ==
	 * '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *
	 * ENTRY.age = 0 Else if REF_BLOC != null:
	 *
	 * ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime Else:
	 *
	 * ENTRY.age = conf.idtyWindow + 1 EndIf
	 *
	 */
	void BR_G19();

	/**
	 * <pre>
	 * BR_G20 - Identity UserID unicity
	 *
	 * For each ENTRY in local IINDEX:
	 *
	 * If op = 'CREATE':
	 *
	 * ENTRY.uidUnique = COUNT(GLOBAL_IINDEX[uid=ENTRY.uid) == 0 Else:
	 *
	 * ENTRY.uidUnique = true
	 *
	 */
	void BR_G20();

	/**
	 * <pre>
	 * For each ENTRY in local IINDEX:
	 *
	 * If op = 'CREATE':
	 *
	 * ENTRY.pubUnique = COUNT(GLOBAL_IINDEX[pub=ENTRY.pub) == 0 Else:
	 *
	 * ENTRY.pubUnique = true
	 *
	 */
	void BR_G21();

	/**
	 *
	 * <pre>
	 * BR_G22 - ENTRY.age
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 -
	 * NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)] If HEAD.number == 0 &&
	 * ENTRY.created_on ==
	 * '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *
	 * ENTRY.age = 0 Else if REF_BLOC != null:
	 *
	 * ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime Else:
	 *
	 * ENTRY.age = conf.msWindow + 1
	 *
	 */
	void BR_G22();

	/**
	 * <pre>
	 * BR_G23 - ENTRY.numberFollowing
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * created_on = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).created_on If created_on !=
	 * null:
	 *
	 * ENTRY.numberFollowing = NUMBER(ENTRY.created_ON) > NUMBER(created_on) Else:
	 *
	 * ENTRY.numberFollowing = true EndIf
	 *
	 * For each ENTRY in local MINDEX where revoked_on != null:
	 *
	 * ENTRY.numberFollowing = true
	 *
	 */
	void BR_G23();

	/**
	 * <pre>
	 * BR_G24 - ENTRY.distanceOK
	 *
	 * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'ACTIVE':
	 *
	 * dSen = CEIL(HEAD.membersCount ^ (1 / stepMax))
	 *
	 * GRAPH = SET(LOCAL_CINDEX, 'issuer', 'receiver') + SET(GLOBAL_CINDEX,
	 * 'issuer', 'receiver') SENTRIES = SUBSET(GRAPH, dSen, 'issuer')
	 *
	 * ENTRY.distanceOK = EXISTS_PATH(xpercent, SENTRIES, GRAPH, ENTRY.pub, stepMax)
	 *
	 * Functionally: checks if it exists, for at least xpercent% of the sentries, a
	 * path using GLOBAL_CINDEX + LOCAL_CINDEX leading to the key PUBLIC_KEY with a
	 * maximum count of [stepMax] hops.
	 *
	 *
	 * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'ACTIVE'):
	 *
	 * ENTRY.distanceOK = true
	 *
	 */
	void BR_G24();;

	/**
	 * <pre>
	 * BR_G25 - ENTRY.onRevoked
	 *
	 * For each ENTRY in local MINDEX:
	 *
	 * ENTRY.onRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
	 */
	void BR_G25();

	/**
	 * <pre>
	 * BR_G26 - ENTRY.joinsTwice
	 *
	 * For each ENTRY in local MINDEX where op = 'UPDATE', expired_on = 0:
	 *
	 * ENTRY.joinsTwice = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member == true
	 *
	 * This rule ensures that someone who is in the Joiners field isn't already a
	 * member.
	 */
	void BR_G26();

	/**
	 * <pre>
	 * BR_G27 - ENTRY.enoughCerts
	 *
	 * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'ACTIVE':
	 *
	 * ENTRY.enoughCerts = COUNT(GLOBAL_CINDEX[receiver=ENTRY.pub,expired_on=null])
	 * + COUNT(LOCAL_CINDEX[receiver=ENTRY.pub,expired_on=null]) >= sigQty
	 *
	 * Functionally: any member or newcomer needs [sigQty] certifications coming to
	 * him to be in the WoT
	 *
	 *
	 * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'ACTIVE'):
	 *
	 * ENTRY.enoughCerts = true
	 */
	void BR_G27();

	/**
	 * <pre>
	 * BR_G28 - ENTRY.leaverIsMember
	 *
	 * For each ENTRY in local MINDEX where type == 'LEAVE':
	 *
	 * ENTRY.leaverIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member For each
	 * ENTRY in local MINDEX where type != 'LEAVE':
	 *
	 * ENTRY.leaverIsMember = trueBR_G28 - ENTRY.leaverIsMember
	 *
	 * For each ENTRY in local MINDEX where type == 'LEAVE':
	 *
	 * ENTRY.leaverIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member For each
	 * ENTRY in local MINDEX where type != 'LEAVE':
	 *
	 * ENTRY.leaverIsMember = true
	 */
	void BR_G28();

	/**
	 * <pre>
	 * BR_G29 - ENTRY.activeIsMember
	 *
	 * For each ENTRY in local MINDEX where type == 'ACTIVE':
	 *
	 * ENTRY.activeIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member For each
	 * ENTRY in local MINDEX where type != 'ACTIVE':
	 *
	 * ENTRY.activeIsMember = true
	 */
	void BR_G29();

	/**
	 * <pre>
	 * BR_G30 - ENTRY.revokedIsMember
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * ENTRY.revokedIsMember = true For each ENTRY in local MINDEX where revoked_on
	 * != null:
	 *
	 * ENTRY.revokedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 */
	void BR_G30();

	/**
	 * <pre>
	 * BR_G31 - ENTRY.alreadyRevoked
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * ENTRY.alreadyRevoked = false For each ENTRY in local MINDEX where revoked_on
	 * != null:
	 *
	 * ENTRY.alreadyRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on !=
	 * null
	 */
	void BR_G31();

	/**
	 * <pre>
	 * BR_G32 - ENTRY.revocationSigOK
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * ENTRY.revocationSigOK = true For each ENTRY in local MINDEX where revoked_on
	 * != null:
	 *
	 * ENTRY.revocationSigOK =
	 * SIG_CHECK_REVOKE(REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]), ENTRY)
	 */
	void BR_G32();

	/**
	 * <pre>
	 * BR_G33 - ENTRY.excludedIsMember
	 *
	 * For each ENTRY in local IINDEX where member != false:
	 *
	 * ENTRY.excludedIsMember = true For each ENTRY in local IINDEX where member ==
	 * false:
	 *
	 * ENTRY.excludedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 */
	void BR_G33();

	/**
	 * <pre>
	 * BR_G34 - ENTRY.isBeingRevoked
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *
	 * ENTRY.isBeingRevoked = false For each ENTRY in local MINDEX where revoked_on
	 * != null:
	 *
	 * ENTRY.isBeingRevoked = true
	 *
	 */
	void BR_G34();

	/**
	 * <pre>
	 * BR_G35 - ENTRY.isBeingKicked
	 *
	 * For each ENTRY in local IINDEX where member != false:
	 *
	 * ENTRY.isBeingKicked = false For each ENTRY in local IINDEX where member ==
	 * false:
	 *
	 * ENTRY.isBeingKicked = true
	 */
	void BR_G35();

	/**
	 * <pre>
	 * BR_G36 - ENTRY.hasToBeExcluded
	 *
	 * For each ENTRY in local IINDEX:
	 *
	 * ENTRY.hasToBeExcluded = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).kick
	 */
	void BR_G36();

	/**
	 * <pre>
	 *
	 * BR_G37 - ENTRY.age
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 -
	 * NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)] If HEAD.number == 0 &&
	 * ENTRY.created_on ==
	 * '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *
	 * ENTRY.age = 0 Else if REF_BLOC != null:
	 *
	 * ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime Else:
	 *
	 * ENTRY.age = conf.sigWindow + 1 EndIf
	 */
	void BR_G37();

	/**
	 * <pre>
	 * BR_G38 - ENTRY.unchainables
	 *
	 * If HEAD.number > 0:
	 *
	 * ENTRY.unchainables = COUNT(GLOBAL_CINDEX[issuer=ENTRY.issuer, chainable_on >
	 * HEAD~1.medianTime]))
	 */
	void BR_G38();

	/**
	 * <pre>
	 * BR_G39 - ENTRY.stock
	 *
	 * ENTRY.stock = COUNT(REDUCE_BY(GLOBAL_CINDEX[issuer=ENTRY.issuer], 'receiver',
	 * 'created_on')[expired_on=0])
	 */
	void BR_G39();

	/**
	 * <pre>
	 * BR_G40 - ENTRY.fromMember
	 *
	 * ENTRY.fromMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.issuer]).member
	 */
	void BR_G40();

	/**
	 * <pre>
	 * BR_G41 - ENTRY.toMember
	 *
	 * ENTRY.toMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]).member
	 */
	void BR_G41();

	/**
	 * <pre>
	 * BR_G42 - ENTRY.toNewcomer
	 *
	 * ENTRY.toNewcomer = COUNT(LOCAL_IINDEX[member=true,pub=ENTRY.receiver]) > 0
	 */
	void BR_G42();

	/**
	 * <pre>
	 * BR_G43 - ENTRY.toLeaver
	 *
	 * ENTRY.toLeaver = REDUCE(GLOBAL_MINDEX[pub=ENTRY.receiver]).leaving
	 */
	void BR_G43();

	/**
	 * <pre>
	 * BR_G44 - ENTRY.isReplay
	 *
	 * reducable =
	 * GLOBAL_CINDEX[issuer=ENTRY.issuer,receiver=ENTRY.receiver,expired_on=0] If
	 * count(reducable) == 0:
	 *
	 * ENTRY.isReplay = false Else:
	 *
	 * ENTRY.isReplay = reduce(reducable).expired_on == 0
	 */
	void BR_G44();

	/**
	 * <pre>
	 *
	 * BR_G45 - ENTRY.sigOK
	 *
	 * ENTRY.sigOK = SIG_CHECK_CERT(REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]),
	 * ENTRY)
	 *
	 * Local SINDEX augmentation
	 */
	void BR_G45();

	/**
	 * <pre>
	 * BR_G46 - ENTRY.available and ENTRY.conditions
	 *
	 * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
	 *
	 * INPUT_ENTRIES =
	 * LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * If COUNT(INPUT_ENTRIES) == 0 Then INPUT_ENTRIES =
	 * GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * EndIf INPUT = REDUCE(INPUT_ENTRIES) ENTRY.conditions = INPUT.conditions
	 * ENTRY.available = INPUT.consumed == false
	 */
	void BR_G46();

	/**
	 * <pre>
	 * BR_G47 - ENTRY.isLocked
	 *
	 * INPUT_ENTRIES =
	 * LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * If COUNT(INPUT_ENTRIES) == 0 Then INPUT_ENTRIES =
	 * GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * EndIf INPUT = REDUCE(INPUT_ENTRIES) ENTRY.isLocked =
	 * TX_SOURCE_UNLOCK(INPUT.conditions, ENTRY)
	 */
	void BR_G47();

	/**
	 * <pre>
	 *
	 * BR_G48 - ENTRY.isTimeLocked
	 *
	 * INPUT_ENTRIES =
	 * LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * If COUNT(INPUT_ENTRIES) == 0 Then INPUT_ENTRIES =
	 * GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * EndIf INPUT = REDUCE(INPUT_ENTRIES) ENTRY.isTimeLocked = ENTRY.written_time -
	 * INPUT.written_time < ENTRY.locktime
	 */
	void BR_G48();

	/**
	 * <pre>
	 * BR_G50 - Block size
	 *
	 * Rule:
	 *
	 * If HEAD.number > 0:
	 *
	 * HEAD.size < MAX(500 ; CEIL(1.10 * HEAD.avgBlockSize))
	 */
	void BR_G50();

	/**
	 * <pre>
	 * BR_G51 - Number
	 *
	 * Rule:
	 *
	 * Number = HEAD.number
	 */
	void BR_G51();

	/**
	 * <pre>
	 * BR_G52 - PreviousHash
	 *
	 * Rule:
	 *
	 * PreviousHash = HEAD.previousHash
	 */
	void BR_G52();

	/**
	 * <pre>
	 * BR_G53 - PreviousIssuer
	 *
	 * Rule:
	 *
	 * PreviousHash = HEAD.previousHash
	 */
	void BR_G53();

	/**
	 * <pre>
	 * BR_G54 - DifferentIssuersCount
	 *
	 * Rule:
	 *
	 * DifferentIssuersCount = HEAD.issuersCount
	 */
	void BR_G54();

	/**
	 * <pre>
	 * BR_G55 - IssuersFrame
	 *
	 * Rule:
	 *
	 * IssuersFrame = HEAD.issuersFrame
	 */
	void BR_G55();

	/**
	 * <pre>
	 * BR_G56 - IssuersFrameVar
	 *
	 * Rule:
	 *
	 * IssuersFrameVar = HEAD.issuersFrameVar
	 */
	void BR_G56();

	/**
	 * <pre>
	 * BR_G57 - MedianTime
	 *
	 * Rule:
	 *
	 * MedianTime = HEAD.medianTime
	 */
	void BR_G57();

	/**
	 * <pre>
	 * BR_G58 - UniversalDividend
	 *
	 * Rule:
	 *
	 * UniversalDividend = HEAD.new_dividend
	 */
	void BR_G58();

	/**
	 * <pre>
	 * BR_G59 - UnitBase
	 *
	 * Rule:
	 *
	 * UnitBase = HEAD.unitBase
	 */
	void BR_G59();

	/**
	 * <pre>
	 * BR_G60 - MembersCount
	 *
	 * Rule:
	 *
	 * MembersCount = HEAD.membersCount
	 */
	void BR_G60();

	/**
	 * <pre>
	 * BR_G61 - PowMin
	 *
	 * Rule:
	 *
	 * If HEAD.number > 0:
	 *
	 * PowMin = HEAD.powMin
	 */
	void BR_G61();

	/**
	 * <pre>
	 * BR_G62 - Proof-of-work
	 *
	 * Rule: the proof is considered valid if:
	 *
	 *
	 *
	 * HEAD.hash starts with at least HEAD.powZeros zeros
	 *
	 * HEAD.hash's HEAD.powZeros + 1th character is:
	 *
	 *
	 * between [0-F] if HEAD.powRemainder = 0
	 *
	 * between [0-E] if HEAD.powRemainder = 1
	 *
	 * between [0-D] if HEAD.powRemainder = 2
	 *
	 * between [0-C] if HEAD.powRemainder = 3
	 *
	 * between [0-B] if HEAD.powRemainder = 4
	 *
	 * between [0-A] if HEAD.powRemainder = 5
	 *
	 * between [0-9] if HEAD.powRemainder = 6
	 *
	 * between [0-8] if HEAD.powRemainder = 7
	 *
	 * between [0-7] if HEAD.powRemainder = 8
	 *
	 * between [0-6] if HEAD.powRemainder = 9
	 *
	 * between [0-5] if HEAD.powRemainder = 10
	 *
	 * between [0-4] if HEAD.powRemainder = 11
	 *
	 * between [0-3] if HEAD.powRemainder = 12
	 *
	 * between [0-2] if HEAD.powRemainder = 13
	 *
	 * between [0-1] if HEAD.powRemainder = 14
	 *
	 *
	 *
	 *
	 *
	 *
	 * N.B.: it is not possible to have HEAD.powRemainder = 15
	 */
	void BR_G62();

	/**
	 * <pre>
	 * BR_G63 - Identity writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [idtyWindow]
	 */
	void BR_G63();

	/**
	 * <pre>
	 * BR_G64 - Membership writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [msWindow]
	 */
	void BR_G64();

	/**
	 * <pre>
	 * BR_G65 - Certification writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [sigWindow]
	 */
	void BR_G65();

	/**
	 * <pre>
	 * BR_G66 - Certification stock
	 *
	 * Rule:
	 *
	 * ENTRY.stock <= sigStock
	 */
	void BR_G66();

	/**
	 * <pre>
	 * BR_G67 - Certification period
	 *
	 * Rule:
	 *
	 * ENTRY.unchainables == 0
	 */
	void BR_G67();

	/**
	 * <pre>
	 * BR_G68 - Certification from member
	 *
	 * Rule:
	 *
	 * If HEAD.number > 0:
	 *
	 * ENTRY.fromMember == true
	 */
	void BR_G68();

	/**
	 * <pre>
	 * BR_G69 - Certification to member or newcomer
	 *
	 * Rule:
	 *
	 * ENTRY.toMember == true OR ENTRY.toNewcomer == true
	 */
	void BR_G69();

	/**
	 * <pre>
	 * BR_G70 - Certification to non-leaver
	 *
	 * Rule:
	 *
	 * ENTRY.toLeaver == false
	 */
	void BR_G70();

	/**
	 * <pre>
	 * BR_G71 - Certification replay
	 *
	 * Rule:
	 *
	 * ENTRY.isReplay == false
	 */
	void BR_G71();

	/**
	 * <pre>
	 * BR_G72 - Certification signature
	 *
	 * Rule:
	 *
	 * ENTRY.sigOK == true
	 */
	void BR_G72();

	/**
	 * <pre>
	 * BR_G73 - Identity UserID unicity
	 *
	 * Rule:
	 *
	 * ENTRY.uidUnique == true
	 */
	void BR_G73();

	/**
	 * <pre>
	 * BR_G74 - Identity pubkey unicity
	 *
	 * Rule:
	 *
	 * ENTRY.pubUnique == true
	 */
	void BR_G74();

	/**
	 * <pre>
	 * BR_G75 - Membership succession
	 *
	 * Rule:
	 *
	 * ENTRY.numberFollowing == true
	 */
	void BR_G75();

	/**
	 * <pre>
	 * BR_G76 - Membership distance check
	 *
	 * Rule:
	 *
	 * ENTRY.distanceOK == true
	 */
	void BR_G76();

	/**
	 * <pre>
	 * BR_G77 - Membership on revoked
	 *
	 * Rule:
	 *
	 * ENTRY.onRevoked == false
	 */
	void BR_G77();

	/**
	 * <pre>
	 * BR_G78 - Membership joins twice
	 *
	 * Rule:
	 *
	 * ENTRY.joinsTwice == false
	 */
	void BR_G78();

	/**
	 * <pre>
	 * BR_G79 - Membership enough certifications
	 *
	 * Rule:
	 *
	 * ENTRY.enoughCerts == true
	 */
	void BR_G79();

	/**
	 * <pre>
	 * BR_G80 - Membership leaver
	 *
	 * Rule:
	 *
	 * ENTRY.leaverIsMember == true
	 */
	void BR_G80();

	/**
	 * <pre>
	 * BR_G81 - Membership active
	 *
	 * Rule:
	 *
	 * ENTRY.activeIsMember == true
	 */
	void BR_G81();

	/**
	 * <pre>
	 * BR_G82 - Revocation by a member
	 *
	 * Rule:
	 *
	 * ENTRY.revokedIsMember == true
	 */
	void BR_G82();

	/**
	 * <pre>
	 * BR_G83 - Revocation singleton
	 *
	 * Rule:
	 *
	 * ENTRY.alreadyRevoked == false
	 */
	void BR_G83();

	/**
	 * <pre>
	 * BR_G84 - Revocation signature
	 *
	 * Rule:
	 *
	 * ENTRY.revocationSigOK == true
	 */
	void BR_G84();

	/**
	 * <pre>
	 * BR_G85 - Excluded is a member
	 *
	 * Rule:
	 *
	 * ENTRY.excludedIsMember == true
	 */
	void BR_G85();

	/**
	 * <pre>
	 * BR_G86 - Excluded contains exclatly those to be kicked
	 *
	 * Rule:
	 *
	 * For each REDUCE_BY(GLOBAL_IINDEX[kick=true], 'pub') as TO_KICK:
	 *
	 * REDUCED = REDUCE(GLOBAL_IINDEX[pub=TO_KICK.pub]) If REDUCED.kick then:
	 *
	 * COUNT(LOCAL_MINDEX[pub=REDUCED.pub,isBeingKicked=true]) == 1 Rule:
	 *
	 * For each IINDEX[member=false] as ENTRY:
	 *
	 * ENTRY.hasToBeExcluded = true
	 */
	void BR_G86();

	/**
	 * <pre>
	 * BR_G87 - Input is available
	 *
	 * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
	 *
	 * Rule:
	 *
	 * ENTRY.available == true
	 */
	void BR_G87();

	/**
	 * <pre>
	 * BR_G88 - Input is unlocked
	 *
	 * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
	 *
	 * Rule:
	 *
	 * ENTRY.isLocked == false
	 */
	void BR_G88();

	/**
	 * <pre>
	 * BR_G89 - Input is time unlocked
	 *
	 * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
	 *
	 * Rule:
	 *
	 * ENTRY.isTimeLocked == false
	 */
	void BR_G89();

	/**
	 * <pre>
	 * BR_G90 - Output base
	 *
	 * For each LOCAL_SINDEX[op='CREATE'] as ENTRY:
	 *
	 * Rule:
	 *
	 * ENTRY.unitBase <= HEAD~1.unitBase
	 */
	void BR_G90();

	/**
	 * <pre>
	 * BR_G91 - Dividend
	 *
	 * If HEAD.new_dividend != null:
	 *
	 * For each REDUCE_BY(GLOBAL_IINDEX[member=true], 'pub') as IDTY then if
	 * IDTY.member, add a new LOCAL_SINDEX entry:
	 *
	 * SINDEX ( op = 'CREATE' identifier = IDTY.pub pos = HEAD.number written_on =
	 * BLOCKSTAMP written_time = MedianTime amount = HEAD.dividend base =
	 * HEAD.unitBase locktime = null conditions = REQUIRE_SIG(MEMBER.pub) consumed =
	 * false ) For each LOCAL_IINDEX[member=true] as IDTY add a new LOCAL_SINDEX
	 * entry:
	 *
	 * SINDEX ( op = 'CREATE' identifier = IDTY.pub pos = HEAD.number written_on =
	 * BLOCKSTAMP written_time = MedianTime amount = HEAD.dividend base =
	 * HEAD.unitBase locktime = null conditions = REQUIRE_SIG(MEMBER.pub) consumed =
	 * false )
	 */
	void BR_G91();

	/**
	 * <pre>
	 * BR_G92 - Certification expiry
	 *
	 * For each GLOBAL_CINDEX[expires_on<=HEAD.medianTime] as CERT, add a new
	 * LOCAL_CINDEX entry:
	 *
	 * If
	 * reduce(GLOBAL_CINDEX[issuer=CERT.issuer,receiver=CERT.receiver,created_on=CERT.created_on]).expired_on
	 * == 0:
	 *
	 * CINDEX ( op = 'UPDATE' issuer = CERT.issuer receiver = CERT.receiver
	 * created_on = CERT.created_on expired_on = HEAD.medianTime )
	 */
	void BR_G92();

	/**
	 * <pre>
	 * BR_G93 - Membership expiry
	 *
	 * For each REDUCE_BY(GLOBAL_MINDEX[expires_on<=HEAD.medianTime AND
	 * revokes_on>HEAD.medianTime], 'pub') as POTENTIAL then consider
	 * REDUCE(GLOBAL_MINDEX[pub=POTENTIAL.pub]) AS MS.
	 *
	 * If MS.expired_on == null OR MS.expired_on == 0, add a new LOCAL_MINDEX entry:
	 *
	 * MINDEX ( op = 'UPDATE' pub = MS.pub written_on = BLOCKSTAMP expired_on =
	 * HEAD.medianTime )
	 */
	void BR_G93();

	/**
	 * <pre>
	 * BR_G94 - Exclusion by membership
	 *
	 * For each LOCAL_MINDEX[expired_on!=0] as MS, add a new LOCAL_IINDEX entry:
	 *
	 * IINDEX ( op = 'UPDATE' pub = MS.pub written_on = BLOCKSTAMP kick = true )
	 */
	void BR_G94();

	/**
	 * <pre>
	 * BR_G95 - Exclusion by certification
	 *
	 * For each LOCAL_CINDEX[expired_on!=0] as CERT:
	 *
	 * Set:
	 *
	 * CURRENT_VALID_CERTS = REDUCE_BY(GLOBAL_CINDEX[receiver=CERT.receiver],
	 * 'issuer', 'receiver', 'created_on')[expired_on=0] If
	 * COUNT(CURRENT_VALID_CERTS) +
	 * COUNT(LOCAL_CINDEX[receiver=CERT.receiver,expired_on=0]) -
	 * COUNT(LOCAL_CINDEX[receiver=CERT.receiver,expired_on!=0]) < sigQty, add a new
	 * LOCAL_IINDEX entry:
	 *
	 * IINDEX ( op = 'UPDATE' pub = CERT.receiver written_on = BLOCKSTAMP kick =
	 * true )
	 */
	void BR_G95();

	/**
	 * <pre>
	 * BR_G96 - Implicit revocation
	 *
	 * For each GLOBAL_MINDEX[revokes_on<=HEAD.medianTime,revoked_on=null] as MS:
	 *
	 * REDUCED = REDUCE(GLOBAL_MINDEX[pub=MS.pub]) If
	 * REDUCED.revokes_on<=HEAD.medianTime AND REDUCED.revoked_on==null, add a new
	 * LOCAL_MINDEX entry:
	 *
	 * MINDEX ( op = 'UPDATE' pub = MS.pub written_on = BLOCKSTAMP revoked_on =
	 * HEAD.medianTime )
	 */
	void BR_G96();

	/**
	 * <pre>
	 * BR_G97 - Final INDEX operations
	 *
	 * If all the rules [BR_G49 ; BR_G90] pass, then all the LOCAL INDEX values
	 * (IINDEX, MINDEX, CINDEX, SINDEX, BINDEX) have to be appended to the GLOBAL
	 * INDEX.
	 */
	void BR_G97();

	/**
	 * <pre>
	 * BR_G98 - Currency
	 *
	 * Rule:
	 *
	 * If HEAD.number > 0:
	 *
	 * Currency = HEAD.currency
	 */
	void BR_G98();

	/**
	 * <pre>
	 * BR_G99 - HEAD.currency
	 *
	 * If HEAD.number > 0: HEAD.currency = HEAD~1.currency Else: HEAD.currency =
	 * null
	 */
	void BR_G99();
}
