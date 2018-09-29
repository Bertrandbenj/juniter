package juniter.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import juniter.model.persistence.BStamp;

/**
 *
 * <pre>
 * <h1>Global Validation</h1>
 *
 * verifies the coherence of a locally-validated block, in the context of the whole blockchain, including the block.
 *
 *
 * INDEX GENERATION
 *
 * Definitions,
 *
 * BINDEX references:
 *    * HEAD: the BINDEX top entry (generated for incoming block, precisely)
 *    * HEAD~1: the BINDEX 1st entry before HEAD (= entry where ENTRY.number  = HEAD.number - 1)
 *    * HEAD~n: the BINDEX nth entry before HEAD (= entry where ENTRY.number = HEAD.number - n)
 *    * HEAD~n[field=value, ...]: the BINDEX entry at HEAD~n if it fulfills the condition, null otherwise
 *    * HEAD~n..m[field=value, ...]: the BINDEX entries between HEAD~n and HEAD~m, included, where each entry fulfills the condition
 *    * HEAD~n.property: get a BINDEX entry property. Ex.: HEAD~1.hash looks at the hash of the entry preceding HEAD.
 *    * (HEAD~n..m).property: get all the values of property in BINDEX for  entries between HEAD~n and HEAD~m, included.
 *    * (HEAD~..).property: same, but and are variables to be computed
 *
 *
 * Function references:
 *    * COUNT returns the number of values in a list of values
 *    * AVG computes the average value in a list of values, floor rounded.
 *    * MEDIAN computes the median value in a list of values
 *    * MAX computes the maximum value in a list of values
 *
 *   If values count is even, the median is computed over the 2 centered values by an arithmetical median on them, NOT rounded.
 *
 * 	  * UNIQ returns a list of the unique values in a list of values
 * 	  * PICK returns a list of the values by picking a particular property on each record
 * 	  * INTEGER_PART return the integer part of a number
 * 	  * FIRST return the first element in a list of values matching the given condition
 * 	  * REDUCE merges a set of elements into a single one, by extending the non-null properties from each record into the resulting record.
 * 	  * REDUCE_BY merges a set of elements into a new set, where each new element is the reduction of the first set sharing a given key.
 * 	  * CONCAT concatenates two sets of elements into a new one
 *
 * If there is no elements, all its properties are null.
 *
 * 	  * NUMBER get the number part of blockstamp
 * 	  * HASH get the hash part of blockstamp
 *
 * The block produces 1 new @BINDEX entry
 *
 *
 * </pre>
 *
 *
 *
 * @author bnimajneb (the copist monk)
 * @author cgeek (the author)
 * @see https://git.duniter.org/nodes/typescript/duniter/blob/dev/doc/Protocol.md#br_g01-headnumber
 *
 */
public interface GlobalValid {

	public class BINDEX implements Comparable<BINDEX> {

		Integer version;
		Integer size;
		String hash;
		String issuer;
		Long time;
		Integer number;
		String currency;
		String previousHash = null;
		String previousIssuer = null;
		Integer membersCount = null;
		boolean issuerIsMember;
		Integer issuersCount = null;
		Integer issuersFrame = null;
		Integer issuersFrameVar = null;
		Integer issuerDiff = null;
		Integer avgBlockSize = null;
		Long medianTime = null;
		Long dividend = null;
		Long mass = null;
		Long massReeval = null;
		Integer unitBase = null;
		Long powMin;

		Long udTime = null;
		Long diffTime = null;
		Long speed = null;

		// FIXME
		public long udReevalTime;
		public long new_dividend;
		public Object diffNumber;
		public Long maxSpeed;
		public Long minSpeed;
		public int powRemainder;
		public int powZeros;

		@Override
		public int compareTo(BINDEX o) {
			return number.compareTo(o.number);
		}

	}

	/**
	 * <pre>
	 * * Certifications
	 *
	 *
	 * The local CINDEX has a unicity constraint on PUBKEY_FROM, PUBKEY_TO
	 *
	 * The local CINDEX has a unicity constraint on PUBKEY_FROM, except for block#0
	 * The local CINDEX must not match a MINDEX operation on PUBLIC_KEY =
	 * PUBKEY_FROM, member = false or PUBLIC_KEY = PUBKEY_FROM, leaving = true
	 *
	 *
	 * Functionally:
	 *
	 *
	 * a block cannot have 2 identical certifications (A -> B) a block cannot have 2
	 * certifications from a same public key, except in block#0 a block cannot have
	 * a certification to a leaver or an excluded
	 * </pre>
	 */
	public class CINDEX {

		String op;
		String issuer;
		String receiver;

		BStamp created_on;
		String written_on;
		String sig;

		Long expires_on;
		Long expired_on;
		Long chainable_on;

		Object from_wid;
		Object to_wid;

		Integer writtenOn;

		// FIXME
		public long unchainables;
		public long age;
		public long stock;
		public boolean fromMember;
		public boolean toMember;
		public boolean toNewcomer;
		public boolean toLeaver;
		public boolean isReplay;
		public boolean sigOK;

	}

	/**
	 * <pre>
	 * * UserID and PublicKey unicity
	 *
	 *
	 * The local IINDEX has a unicity constraint on USER_ID. The local IINDEX has a
	 * unicity constraint on PUBLIC_KEY. Each local IINDEX op = 'CREATE' operation
	 * must match a single local MINDEX op = 'CREATE', pub = PUBLIC_KEY operation.
	 *
	 *
	 *
	 * Functionally: UserID and public key must be unique in a block, an each new
	 * identity must have an opt-in document attached.
	 * </pre>
	 */
	public class IINDEX {

		String op;
		String uid;
		String pub;
		String hash;
		String sig;
		BStamp created_on;
		String written_on;
		boolean member;
		boolean wasMember;
		boolean kick;
		Integer wotbid;
		Integer writtenOn;

		// FIXME just transient ?
		public long age;
		// FIXME
		public boolean uidUnique;
		public boolean pubUnique;
		public boolean excludedIsMember;
		public boolean isBeingKicked;
		public Stream<IINDEX> hasToBeExcluded;
		public boolean leaving;

	}

	/**
	 * <pre>
	 * * Membership unicity
	 *
	 *
	 * The local MINDEX has a unicity constraint on PUBLIC_KEY
	 *
	 *
	 *
	 *
	 * Functionally: a user has only 1 status change allowed per block.
	 *
	 *
	 *
	 * Revocation implies exclusion
	 *
	 *
	 * Each local MINDEX ̀op = 'UPDATE', revoked_on = BLOCKSTAMP operations must
	 * match a single local IINDEX op = 'UPDATE', pub = PUBLIC_KEY, member = false
	 * operation.
	 *
	 *
	 *
	 * Functionally: a revoked member must be immediately excluded.
	 *
	 * </pre>
	 */
	public class MINDEX {

		String op;
		String pub;

		BStamp created_on;
		String written_on;

		Long expires_on;
		Long expired_on;

		Long revokes_on;
		String revoked_on;

		Integer leaving;
		String revocation;

		Long chainable_on;
		Integer writtenOn;

		// FIXME
		public Object age;
		public boolean numberFollowing;
		public String type;
		public boolean distanceOK;
		public boolean onRevoked;
		public boolean joinsTwice;
		public boolean enoughCerts;
		public boolean leaverIsMember;
		public boolean activeIsMember;
		public boolean revokedIsMember;
		public boolean alreadyRevoked;
		public boolean revocationSigOK;
		public boolean excludedIsMember;
		public boolean isBeingRevoked;
	}

	/**
	 * <pre>
	 * * Sources
	 *
	 * The local SINDEX has a unicity constraint on UPDATE, IDENTIFIER, POS
	 *
	 * The local SINDEX has a unicity constraint on CREATE, IDENTIFIER, POS
	 *
	 *
	 * * Functionally:
	 *
	 *
	 * a same source cannot be consumed twice by the block a same output cannot be
	 * produced twice by block
	 *
	 *
	 * But a source can be both created and consumed in the same block, so a chain
	 * of transactions can be stored at once.
	 *
	<h2>Double-spending control</h2>
	Definitions:

	For each SINDEX unique tx:

	- inputs are the SINDEX row matching UPDATE, tx

	- outputs are the SINDEX row matching CREATE, tx


	Functionally: we gather the sources for each transaction, in order to check them.



	CommonBase

	Each input has an InputBase, and each output has an OutputBase. These bases are to be called AmountBase.

	The CommonBase is the lowest base value among all AmountBase of the transaction.

	For any amount comparison, the respective amounts must be translated into CommonBase using the following rule:

	AMOUNT(CommonBase) = AMOUNT(AmountBase) x POW(10, AmountBase - CommonBase)
	So if a transaction only carries amounts with the same AmountBase, no conversion is required. But if a transaction carries:


	input_0 of value 45 with AmountBase = 5

	input_1 of value 75 with AmountBase = 5

	input_2 of value 3 with AmountBase = 6

	output_0 of value 15 with AmountBase = 6



	Then the output value has to be converted before being compared:

	CommonBase = 5

	output_0(5) = output_0(6) x POW(10, 6 - 5)
	output_0(5) = output_0(6) x POW(10, 1)
	output_0(5) = output_0(6) x 10
	output_0(5) = 15 x 10
	output_0(5) = 150

	input_0(5) = input_0(5)
	input_0(5) = 45

	input_1(5) = input_1(5)
	input_1(5) = 75

	input_2(5) = input_2(6) x POW(10, 6 - 5)
	input_2(5) = input_2(6) x POW(10, 1)
	input_2(5) = input_2(6) x 10
	input_2(5) = 3 x 10
	input_2(5) = 30
	The equality of inputs and outputs is then verified because:

	output_0(5) = 150
	input_0(5) = 45
	input_1(5) = 75
	input_2(5) = 30

	output_0(5) = input_0(5) + input_1(5) + input_2(5)
	150 = 45 + 75 + 30
	TRUE

	Amounts



	Def.: InputBaseSum is the sum of amounts with the same InputBase.

	Def.: OutputBaseSum is the sum of amounts with the same OutputBase.

	Def.: BaseDelta = OutputBaseSum - InputBaseSum, expressed in CommonBase


	Rule: For each OutputBase:


	if BaseDelta > 0, then it must be inferior or equal to the sum of all preceding BaseDelta



	Rule: The sum of all inputs in CommonBase must equal the sum of all outputs in CommonBase


	Functionally: we cannot create nor lose money through transactions. We can only transfer coins we own.
	Functionally: also, we cannot convert a superiod unit base into a lower one.
	 *
	 *
	 * </pre>
	 */
	public class SINDEX {

		String op;
		String tx;
		String identifier;
		Integer pos;
		String created_on;
		String written_on;
		Long written_time;

		Integer amount;
		Integer base;

		Integer locktime;
		Integer consumed;

		String condition;
		Integer writtenOn;
	}

	Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * <pre>
	 * BR_G01 - HEAD.number
	 *
	 * If HEAD~1 is defined
	 *     HEAD.number = HEAD~1.number + 1
	 * Else
	 *     HEAD.number = 0
	 * </pre>
	 */
	default void BR_G01(BINDEX head) {
		if (prevHead() != null) {
			head.number = prevHead().number + 1;
		} else {
			head.number = 0;
		}
	}

	/**
	 * <pre>
	 * BR_G02 - HEAD.previousHash
	 *
	 * If HEAD.number > 0
	 *     HEAD.previousHash = HEAD~1.hash
	 * Else:
	 *     HEAD.previousHash = null
	 *
	 * </pre>
	 */
	default void BR_G02(BINDEX head) {
		if (head.number > 0) {
			head.previousHash = prevHead().hash;
		} else {
			head.previousHash = null;
		}
	}

	/**
	 * <pre>
	 * BR_G03 - HEAD.previousIssuer
	 *
	 * If HEAD.number > 0
	 *     HEAD.previousIssuer = HEAD~1.issuer
	 * Else:
	 *     HEAD.previousIssuer = null
	 * </pre>
	 */
	default void BR_G03(BINDEX head) {
		if (head.number > 0) {
			head.previousIssuer = prevHead().issuer;
		} else {
			head.previousIssuer = null;
		}
	}

	/**
	 * <pre>
	 * BR_G04 - HEAD.issuersCount
	 *
	 * If HEAD.number == 0
	 *     HEAD.issuersCount = 0
	 * Else
	 *     HEAD.issuersCount = COUNT(UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer))
	 * </pre>
	 *
	 */
	default void BR_G04(BINDEX head) {
		if (head.number > 0) {
			head.issuersCount = 0;
		} else {
			head.issuersCount = computeIssuersCount();
		}
	}

	/**
	 * <pre>
	 * BR_G05 - HEAD.issuersFrame
	 *
	 * If HEAD.number == 0
	 *     HEAD.issuersFrame = 1
	 *
	 * Else if HEAD~1.issuersFrameVar > 0
	 *     HEAD.issuersFrame = HEAD~1.issuersFrame + 1
	 *
	 * Else if HEAD~1.issuersFrameVar < 0
	 *     HEAD.issuersFrame = HEAD~1.issuersFrame - 1
	 *
	 * Else
	 *     HEAD.issuersFrame = HEAD~1.issuersFrame
	 * </pre>
	 *
	 */
	default void BR_G05(BINDEX head) {
		if (head.number == 0) {
			head.issuersFrame = 1;
		}

		if (prevHead().issuersFrameVar > 0) {
			head.issuersFrame = prevHead().issuersFrame + 1;
		}

		if (prevHead().issuersFrameVar < 0) {
			head.issuersFrame = prevHead().issuersFrame - 1;
		}

		head.issuersFrame = prevHead().issuersFrame;
	}

	/**
	 * <pre>
	 * BR_G06 - HEAD.issuersFrameVar
	 *
	 * If HEAD.number == 0
	 *     HEAD.issuersFrameVar = 0
	 *
	 * Else if HEAD~1.issuersFrameVar > 0
	 *     HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount - HEAD~1.issuersCount) - 1
	 *
	 * Else if HEAD~1.issuersFrameVar < 0
	 *     HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount - HEAD~1.issuersCount) + 1
	 *
	 * Else:
	 *     HEAD.issuersFrameVar = HEAD~1.issuersFrameVar + 5*(HEAD.issuersCount - HEAD~1.issuersCount)
	 *
	 * </pre>
	 *
	 */
	default void BR_G06(BINDEX head) {
		if (head.number == 0) {
			head.issuersFrameVar = 0;
		} else {
			final var delta = head.issuersCount - prevHead().issuersCount;

			if (prevHead().issuersFrameVar > 0) {
				head.issuersFrameVar = prevHead().issuersFrameVar + 5 * delta - 1;
			} else if (prevHead().issuersFrameVar < 0) {
				head.issuersFrameVar = prevHead().issuersFrameVar + 5 * delta + 1;
			} else {
				head.issuersFrameVar = prevHead().issuersFrameVar + 5 * delta;
			}
		}

	}

	/**
	 * <pre>
	 * BR_G07 - HEAD.avgBlockSize
	 *
	 * HEAD.avgBlockSize = AVG((HEAD~1..<HEAD.issuersCount>).size)
	 * </pre>
	 */
	default void BR_G07(BINDEX head) {
		head.avgBlockSize = (int) range(head.issuersCount).mapToInt(h -> h.size).average().getAsDouble();
	}

	/**
	 * <pre>
	 * BR_G08 - HEAD.medianTime
	 *
	 * If HEAD.number > 0
	 *     HEAD.medianTime = MEDIAN((HEAD~1..<MIN(medianTimeBlocks, HEAD.number)>).time)
	 * Else
	 *     HEAD.medianTime = HEAD.time
	 * </pre>
	 */
	default void BR_G08(BINDEX head) {
		// FIXME
		final int medianTimeBlocks = 0;
		final var size = Math.min(medianTimeBlocks, head.number);

		if (head.number > 0) {

			head.medianTime = (long) range(size) // fetch bindices
					.mapToLong(h -> h.time) //
					.sorted().skip((size - 1) / 2).limit(2 - size % 2) // get median
					.average().orElse(Double.NaN);
		} else {
			head.medianTime = head.time;
		}
	}

	/**
	 * <pre>
	 * BR_G09 - HEAD.diffNumber
	 *
	 * If HEAD.number == 0
	 *     HEAD.diffNumber = HEAD.number + dtDiffEval
	 *
	 * Else if HEAD~1.diffNumber <= HEAD.number
	 *     HEAD.diffNumber = HEAD~1.diffNumber + dtDiffEval
	 *
	 * Else:
	 *     HEAD.diffNumber = HEAD~1.diffNumber
	 * </pre>
	 */
	default void BR_G09(BINDEX head) {
		// FIXME
		final Long dtDiffEval = 0L;

		if (head.number == 0) {
			head.diffTime = head.number + dtDiffEval;
		} else if (head.number <= 0) {
			head.diffTime = prevHead().diffTime + dtDiffEval;
		} else {
			head.diffTime = prevHead().time;
		}
	}

	/**
	 * <pre>
	 * BR_G10 - HEAD.membersCount
	 *
	 * If HEAD.number == 0
	 *     HEAD.membersCount = COUNT(LOCAL_IINDEX[member=true])
	 * Else:
	 *     HEAD.membersCount = HEAD~1.membersCount + COUNT(LOCAL_IINDEX[member=true]) - COUNT(LOCAL_IINDEX[member=false])
	 * </pre>
	 *
	 * Rather than using two counts use a single reduction
	 *
	 */
	default void BR_G10(BINDEX head) {
		final int member = Stream.of(indexI()).reduce( // iterate and reduce all IINDEX
				0, // initial value
				(t, u) -> t + (u.member ? 1 : -1), // +/- 1 for membership
				(a, b) -> a + b) // sum up -> good for parallelism
				.intValue();

		if (head.number == 0) {
			head.membersCount = member;
		} else {
			head.membersCount = member + prevHead().membersCount;
		}
	}

	/**
	 * <pre>
	 * BR_G100 - HEAD.issuerIsMember
	 *
	 * If HEAD.number > 0
	 *     HEAD.issuerIsMember = REDUCE(GLOBAL_IINDEX[pub=HEAD.issuer]).member
	 * Else
	 *     HEAD.issuerIsMember = REDUCE(LOCAL_IINDEX[pub=HEAD.issuer]).member
	 * </pre>
	 */
	default void BR_G100(BINDEX head) {
		// FIXME local vs global IINDEX ?
		// FIXME BINDEX..issuerIsMember ? IINDEX.wasMember?
		final boolean member = Stream.of(indexI()) // iterate
				.filter(i -> i.pub == head.issuer) // select expecting unicity on pubkey
				.map(i -> i.member) // extract value
				.findFirst().orElse(false); // clean return
		if (head.number == 0) {
			head.issuerIsMember = member;
		} else {
			head.issuerIsMember = member;
		}
	}

	/**
	 * <pre>
	 * BR_G101 - Issuer
	 *
	 * Rule:
	 *
	 * HEAD.issuerIsMember == true
	 *
	 * </pre>
	 */
	default boolean BR_G101(BINDEX head) {
		return head.issuerIsMember;
	}

	/**
	 * <pre>
	 * BR_G102 - ENTRY.age
	 *
	 * For each ENTRY in local SINDEX where op = 'UPDATE':
	 *
	 *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.hash)>[hash=HASH(ENTRY.created_on)]
	 *
	 *     If HEAD.number == 0 && ENTRY.created_on == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *         ENTRY.age = 0
	 *
	 *     Else if REF_BLOC != null
	 *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
	 *
	 *     Else
	 *         ENTRY.age = conf.txWindow + 1 EndIf
	 *
	 * </pre>
	 */
	default boolean BR_G102() {

		return false;
	}

	/**
	 * <pre>
	 * BR_G103 - Trancation writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [txWindow]
	 */
	default boolean BR_G103() {
		return false;
	}

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
	default boolean BR_G104() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G105 - Certification expiry date correction
	 *
	 * For each LOCAL_CINDEX as CERT:
	 *
	 * CERT.expires_on = CERT.expires_on - CERT.age
	 */
	default boolean BR_G105() {
		return false;
	}

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
	default boolean BR_G106() {
		return false;
	}

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
	default boolean BR_G107() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G108 - Membership period
	 *
	 * Rule:
	 *
	 * ENTRY.unchainables == 0
	 */
	default boolean BR_G108() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G11 - HEAD.udTime and HEAD.udReevalTime
	 *
	 * If HEAD.number == 0
	 *     HEAD.udTime = udTime0
	 * Else if HEAD~1.udTime <= HEAD.medianTime:
	 *     HEAD.udTime = HEAD~1.udTime + dt
	 * Else
	 *     HEAD.udTime = HEAD~1.udTime EndIf
	 *
	 * If HEAD.number == 0
	 *     HEAD.udReevalTime = udReevalTime0
	 * Else if HEAD~1.udReevalTime <= HEAD.medianTime
	 *     HEAD.udReevalTime = HEAD~1.udReevalTime + dtReeval
	 * Else:
	 *     HEAD.udReevalTime = HEAD~1.udReevalTime EndIf
	 * </pre>
	 */
	default void BR_G11(BINDEX head) {

		// FIXME explain variables
		final Long udTime0 = null;
		final Long dt = null;
		final Long udReevalTime0 = null;

		if (head.number == 0) {
			head.udTime = udTime0;

			head.udReevalTime = udReevalTime0;
		} else if (prevHead().udTime <= head.medianTime) {
			head.udTime = prevHead().udTime + dt;
		} else {
			head.udTime = prevHead().udTime;
		}

	}

	/**
	 * <pre>
	 * BR_G12 - HEAD.unitBase
	 *
	 * If HEAD.number == 0:
	 *
	 * HEAD.unitBase = 0 Else:
	 *
	 * HEAD.unitBase = HEAD~1.unitBase
	 * </pre>
	 */
	default void BR_G12(BINDEX head) {
		if (head.number == 0) {
			head.unitBase = 0;
		} else {
			head.unitBase = prevHead().unitBase;
		}
	}

	/**
	 * <pre>
	 * BR_G13 - HEAD.dividend and HEAD.new_dividend
	 *
	 * If HEAD.number == 0
	 *     HEAD.dividend = ud0
	 * Else If HEAD.udReevalTime != HEAD~1.udReevalTime
	 *     HEAD.dividend = HEAD_1.dividend + c² * CEIL(HEAD~1.massReeval / POW(10, HEAD~1.unitbase)) / HEAD.membersCount)
	 * Else
	 *     HEAD.dividend = HEAD~1.dividend EndIf
	 *
	 *
	 * If HEAD.number == 0
	 *     HEAD.new_dividend = null
	 * Else If HEAD.udTime != HEAD~1.udTime
	 *     HEAD.new_dividend = HEAD.dividend
	 * Else
	 *     HEAD.new_dividend = null
	 * </pre>
	 */
	default void BR_G13(BINDEX head) {
		// FIXME
		final Long ud0 = null;
		final int c = 0;

		if (head.number == 0) {
			head.dividend = ud0;
		} else if (head.udReevalTime != prevHead().udReevalTime) {
			head.dividend = (long) (prevHead().dividend + c * c
					+ Math.ceil(prevHead().massReeval / Math.pow(10, prevHead().unitBase)) / head.membersCount);

		}
	}

	/**
	 * <pre>
	 * BR_G14 - HEAD.dividend and HEAD.unitbase and HEAD.new_dividend
	 *
	 * If HEAD.dividend >= 1000000
	 *     HEAD.dividend = CEIL(HEAD.dividend / 10)
	 *     HEAD.new_dividend = HEAD.dividend
	 *     HEAD.unitBase = HEAD.unitBase + 1
	 *
	 * </pre>
	 *
	 */
	default void BR_G14(BINDEX head) {

		if (head.dividend >= 1000000) {
			head.dividend = (long) Math.ceil(head.dividend / 10);
			head.new_dividend = head.dividend;
			head.unitBase = head.unitBase + 1;
		}
	}

	/**
	 * <pre>
	 * BR_G15 - HEAD.mass and HEAD.massReeval
	 *
	 * If HEAD.number == 0
	 *     HEAD.mass = 0
	 * Else if HEAD.udTime != HEAD~1.udTime
	 *     HEAD.mass = HEAD~1.mass + HEAD.dividend * POWER(10, HEAD.unitBase) * HEAD.membersCount
	 * Else:
	 *     HEAD.mass = HEAD~1.mass
	 *
	 *
	 * If HEAD.number == 0
	 *     HEAD.massReeval = 0
	 * Else if HEAD.udReevalTime != HEAD~1.udReevalTime
	 *     HEAD.massReeval = HEAD~1.mass
	 * Else:
	 *     HEAD.massReeval = HEAD~1.massReeval EndIf
	 *
	 * Functionnally: the UD is reevaluated on the preceding monetary mass  (important!)
	 * </pre>
	 *
	 */
	default void BR_G15(BINDEX head) {
		if (head.number == 0) {
			head.mass = 0L;
			head.massReeval = 0L;
		} else {

			if (head.udTime != prevHead().udTime) {
				head.mass = (long) (prevHead().mass + head.dividend * Math.pow(10, head.unitBase) * head.membersCount);
			} else {
				head.mass = prevHead().mass;
			}

			if (head.udReevalTime != prevHead().udReevalTime) {
				head.massReeval = prevHead().mass;
			} else {
				head.massReeval = prevHead().massReeval;
			}
		}

	}

	/**
	 * <pre>
	 * BR_G16 - HEAD.speed
	 *
	 * If HEAD.number == 0
	 *     speed = 0      FIXME head.speed ?
	 * Else
	 *     range = MIN(dtDiffEval, HEAD.number)
	 *     elapsed = (HEAD.medianTime - HEAD~<range>.medianTime)
	 *
	 *     If elapsed == 0:
	 *         speed = 100
	 *     Else
	 *         speed = range / elapsed
	 *
	 * </pre>
	 */
	default void BR_G16(BINDEX head) {
		final int dtDiffEval = 0;

		if (head.number == 0) {
			head.speed = 0L;
		} else {
			final var range = Math.min(dtDiffEval, head.number);
			final var elapsed = head.medianTime - indexB()[range].medianTime;

			if (elapsed == 0) {
				head.speed = 100L;
			} else {
				head.speed = range / elapsed;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G17 - HEAD.powMin
	 *
	 *
	 * If      HEAD.number > 0 AND HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed >= maxSpeed AND (HEAD~1.powMin + 2) % 16 == 0:
	 *     HEAD.powMin = HEAD~1.powMin + 2
	 *
	 * Else if HEAD.number > 0 AND HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed >= maxSpeed:
	 *     HEAD.powMin = HEAD~1.powMin + 1
	 *
	 * Else if HEAD.number > 0 AND HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed <= minSpeed AND (HEAD~1.powMin) % 16 == 0
	 *     HEAD.powMin = MAX(0, HEAD~1.powMin - 2)
	 *
	 * Else if HEAD.number > 0 AND HEAD.diffNumber != HEAD~1.diffNumber AND HEAD.speed <= minSpeed:
	 *     HEAD.powMin = MAX(0, HEAD~1.powMin - 1)
	 *
	 * Else if HEAD.number > 0
	 *     HEAD.powMin = HEAD~1.powMin
	 * </pre>
	 */
	default void BR_G17(BINDEX head) {
		// FIXME head.maxSpeed ?

		if (head.number > 0 && head.diffNumber != prevHead().diffNumber && head.speed >= head.maxSpeed
				&& (prevHead().powMin + 2) % 16 == 0) {
			head.powMin = prevHead().powMin + 2;
		} else if (head.number > 0 && head.diffNumber != prevHead().diffNumber && head.speed >= head.maxSpeed) {
			head.powMin = prevHead().powMin + 1;
		} else if (head.number > 0 && head.diffNumber != prevHead().diffNumber && head.speed <= head.maxSpeed
				&& prevHead().powMin % 16 == 0) {
			head.powMin = Math.max(0, prevHead().powMin - 2);
		} else if (head.number > 0 && head.diffNumber != prevHead().diffNumber && head.speed <= head.minSpeed) {
			head.powMin = Math.max(0, prevHead().powMin - 1);

		} else if (head.number > 0) {
			head.powMin = prevHead().powMin;

		}
	}

	/**
	 * <pre>
	 * BR_G18 - HEAD.powZeros and HEAD.powRemainder
	 *
	 * If HEAD.number == 0:
	 *     nbPersonalBlocksInFrame = 0
	 *     medianOfBlocksInFrame = 1
	 * Else:
	 *     blocksOfIssuer = HEAD~1..<HEAD~1.issuersFrame>[issuer=HEAD.issuer]
	 *     nbPersonalBlocksInFrame = COUNT(blocksOfIssuer)
	 *     blocksPerIssuerInFrame = MAP( UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer) => ISSUER: COUNT(HEAD~1..<HEAD~1.issuersFrame>[issuer=ISSUER]))
	 *     medianOfBlocksInFrame = MEDIAN(blocksPerIssuerInFrame)
	 *
	 *
	 * If nbPersonalBlocksInFrame == 0
	 *     nbPreviousIssuers = 0
	 *     nbBlocksSince = 0
	 * Else:
	 *     last = FIRST(blocksOfIssuer)
	 *     nbPreviousIssuers = last.issuersCount
	 *     nbBlocksSince = HEAD~1.number - last.number
	 *
	 * PERSONAL_EXCESS = MAX(0, ( (nbPersonalBlocksInFrame + 1) / medianOfBlocksInFrame) - 1)
	 * PERSONAL_HANDICAP = FLOOR(LN(1 + PERSONAL_EXCESS) / LN(1.189))
	 * HEAD.issuerDiff = MAX [ HEAD.powMin ;  HEAD.powMin * FLOOR (percentRot * nbPreviousIssuers / (1 + nbBlocksSince)) ] + PERSONAL_HANDICAP
	 *
	 * If (HEAD.issuerDiff + 1) % 16 == 0:
	 *     HEAD.issuerDiff = HEAD.issuerDiff + 1 EndIf
	 *
	 * Finally:
	 *     HEAD.powRemainder = HEAD.issuerDiff % 16
	 *     HEAD.powZeros = (HEAD.issuerDiff - HEAD.powRemainder) / 16
	 *
	 * </pre>
	 *
	 */
	default void BR_G18(BINDEX head) {
		// FIXME quesaco ?
		final int percentRot = 0;
		long nbPersonalBlocksInFrame;
		int medianOfBlocksInFrame;
		Stream<BINDEX> blocksOfIssuer = null;
		if (head.number == 0) {
			nbPersonalBlocksInFrame = 0;
			medianOfBlocksInFrame = 1;
		} else {
			blocksOfIssuer = range(head.issuersFrame).filter(i -> i.issuer.equals(head.issuer));
			nbPersonalBlocksInFrame = blocksOfIssuer.count();
			final Object blocksPerIssuerInFrame = null;

		}

		var nbPreviousIssuers = 0;
		var nbBlocksSince = 0;
		if (nbPersonalBlocksInFrame != 0) {
			final var first = blocksOfIssuer.findFirst().get();
			nbPreviousIssuers = first.issuersCount;
			nbBlocksSince = prevHead().number - first.number;
		}

		final var personalExcess = Math.max(0, 1);
		final var personalHandicap = Math.floor(Math.log(1 + personalExcess) / Math.log(1.189));

		head.issuerDiff = (int) Math.max(head.powMin,
				head.powMin * Math.floor(percentRot * nbPreviousIssuers / (1 + nbBlocksSince)));

		if ((head.issuerDiff + 1) % 16 == 0) {
			head.issuerDiff = head.issuerDiff + 1;
		}

		head.powRemainder = head.issuerDiff % 16;
		head.powZeros = (head.issuerDiff - head.powRemainder) / 16;
	}

	/**
	 * <pre>
	 * Local IINDEX augmentation
	 *
	 * BR_G19 - ENTRY.age
	 *
	 * For each ENTRY in local IINDEX where op = 'CREATE':
	 *
	 *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)]
	 *
	 *     If HEAD.number == 0 && ENTRY.created_on == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *         ENTRY.age = 0
	 *     Else if REF_BLOC != null:
	 *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
	 *     Else:
	 *         ENTRY.age = conf.idtyWindow + 1
	 * </pre>
	 */
	default void BR_G19(BINDEX head) {

		// FIXME
		final long idtyWindow = 100;

		for (final var entry : indexI()) {
			if ("CREATE".equals(entry.op)) {
				if (head.number == 0 && entry.created_on
						.toString() == "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855") {
					entry.age = 0;
				} else {
					entry.age = Stream.of(indexB()) //
							.filter(b -> b.hash.equals(entry.created_on.getHash())) //
							.filter(b -> b.number.equals(entry.created_on.getNumber())) //
							.findAny() //
							.map(refb -> head.medianTime - refb.medianTime) //
							.orElse(idtyWindow + 1);
				}
			}
		}
	}

	/**
	 * <pre>
	 * BR_G20 - Identity UserID unicity
	 *
	 * For each ENTRY in local IINDEX:
	 *
	 *     If op = 'CREATE':
	 *         ENTRY.uidUnique = COUNT(GLOBAL_IINDEX[uid=ENTRY.uid) == 0
	 *     Else:
	 *         ENTRY.uidUnique = true
	 * </pre>
	 */
	default void BR_G20() {
		for (final var entry : indexI()) {
			if ("CREATE".equals(entry.op)) {
				entry.uidUnique = Stream.of(indexI()).noneMatch(i -> i.uid.equals(entry.uid));
			} else {
				entry.uidUnique = true;
			}
		}
	}

	/**
	 * <pre>
	 * For each ENTRY in local IINDEX:
	 *     If op = 'CREATE'
	 *         ENTRY.pubUnique = COUNT(GLOBAL_IINDEX[pub=ENTRY.pub) == 0
	 *     Else
	 *         ENTRY.pubUnique = true
	 * </pre>
	 */
	default void BR_G21() {
		for (final var entry : indexI()) {
			if ("CREATE".equals(entry.op)) {
				entry.pubUnique = Stream.of(indexI()).noneMatch(i -> i.sig.equals(entry.sig));
			} else {
				entry.pubUnique = true;
			}
		}
	}

	/**
	 *
	 * <pre>
	 * BR_G22 - ENTRY.age
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)]
	 *
	 *     If HEAD.number == 0 && ENTRY.created_on == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *         ENTRY.age = 0
	 *     Else if REF_BLOC != null
	 *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
	 *     Else:
	 *         ENTRY.age = conf.msWindow + 1
	 * </pre>
	 */
	default void BR_G22(BINDEX head) {
		// FIXME
		final long msWindow = 0;

		for (final var entry : indexM()) {
			if (entry.revoked_on == null) {

				if (head.number == 0) {
					entry.age = 0;
				} else {

					entry.age = Stream.of(indexB()) //
							.filter(b -> b.hash.equals(entry.created_on.getHash())) //
							.filter(b -> b.number.equals(entry.created_on.getNumber())) //
							.findAny() //
							.map(refb -> head.medianTime - refb.medianTime) //
							.orElse(msWindow + 1);
				}
			}
		}
	}

	/**
	 * <pre>
	 * BR_G23 - ENTRY.numberFollowing
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null
	 *
	 *     created_on = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).created_on
	 *
	 *     If created_on != null:
	 *         ENTRY.numberFollowing = NUMBER(ENTRY.created_ON) > NUMBER(created_on)
	 *     Else:
	 *         ENTRY.numberFollowing = true EndIf
	 *
	 *
	 * For each ENTRY in local MINDEX where revoked_on != null
	 *     ENTRY.numberFollowing = true
	 * </pre>
	 */
	default void BR_G23() {
		for (final var entry : indexM()) {
			if (entry.revoked_on == null) {
				final var created_on = indexMGlobal().filter(m -> m.pub.equals(entry.pub)).findFirst()
						.map(m -> m.created_on).get();
				entry.numberFollowing = created_on == null ? true
						: entry.created_on.getNumber() > created_on.getNumber();

			} else { // if revoked_on exists
				entry.numberFollowing = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G24 - ENTRY.distanceOK
	 *
	 * Functionally: checks if it exists, for at least xpercent% of the sentries, a
	 * path using GLOBAL_CINDEX + LOCAL_CINDEX leading to the key PUBLIC_KEY with a
	 * maximum count of [stepMax] hops.
	 *
	 * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'ACTIVE':
	 *
	 *     dSen = CEIL(HEAD.membersCount ^ (1 / stepMax))
	 *     GRAPH = SET(LOCAL_CINDEX, 'issuer', 'receiver') + SET(GLOBAL_CINDEX, 'issuer', 'receiver')
	 *     SENTRIES = SUBSET(GRAPH, dSen, 'issuer')
	 *     ENTRY.distanceOK = EXISTS_PATH(xpercent, SENTRIES, GRAPH, ENTRY.pub, stepMax)
	 *
	 * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'ACTIVE')
	 *     ENTRY.distanceOK = true
	 * </pre>
	 */
	default void BR_G24(BINDEX head) {
		for (final var entry : indexM()) {
			if (entry.type.equals("JOIN") || entry.type.equals("ACTIVE")) {
				final int stepMax = 0;
				final var dSen = Math.ceil(Math.pow(head.membersCount, 1 / stepMax));
				// TODO: complete

			} else {
				entry.distanceOK = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G25 - ENTRY.onRevoked
	 *
	 * For each ENTRY in local MINDEX:
	 *     ENTRY.onRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
	 * </pre>
	 */
	default void BR_G25() {
		for (final var entry : indexM()) {
			entry.onRevoked = indexMGlobal().anyMatch(m -> m.pub.equals(entry.pub) && m.revoked_on != null);
		}
	}

	/**
	 * <pre>
	 * BR_G26 - ENTRY.joinsTwice
	 *
	 * This rule ensures that someone who is in the Joiners field isn't already a
	 * member.
	 *
	 * For each ENTRY in local MINDEX where op = 'UPDATE', expired_on = 0
	 *
	 *     ENTRY.joinsTwice = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member == true
	 *
	 *
	 * </pre>
	 */
	default void BR_G26() {
		for (final var entry : indexM()) {
			if (entry.op.equals("UPDATE") || entry.expired_on.equals(0L)) {
				entry.joinsTwice = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
			}
		}
	}

	/**
	 * <pre>
	 * BR_G27 - ENTRY.enoughCerts
	 *
	 * Functionally: any member or newcomer needs [sigQty] certifications coming to
	 * him to be in the WoT
	 *
	 * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'ACTIVE'
	 *     ENTRY.enoughCerts = COUNT(GLOBAL_CINDEX[receiver=ENTRY.pub,expired_on=null])
	 *     					 + COUNT(LOCAL_CINDEX[receiver=ENTRY.pub,expired_on=null]) >= sigQty
	 *
	 * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'ACTIVE'):
	 *     ENTRY.enoughCerts = true
	 * </pre>
	 */
	default void BR_G27() {

		final long sigQty = 0; // FIXME
		for (final var entry : indexM()) {
			if (entry.type.equals("JOIN") || entry.type.equals("ACTIVE")) {

				entry.enoughCerts = indexCGlobal().filter(c -> c.receiver == entry.pub && c.expired_on == null).count()
						+ Stream.of(indexC()).filter(c -> c.receiver == entry.pub && c.expired_on == null)
								.count() >= sigQty;
			} else {
				entry.enoughCerts = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G28 - ENTRY.leaverIsMember
	 *
	 * For each ENTRY in local MINDEX where type == 'LEAVE':
	 *     ENTRY.leaverIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 *
	 * For each ENTRY in local MINDEX where type != 'LEAVE':
	 *     ENTRY.leaverIsMember = true
	 * </pre>
	 */
	default void BR_G28() {
		for (final var entry : indexM()) {
			if (entry.type.equals("LEAVE")) {
				entry.leaverIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
			} else {
				entry.leaverIsMember = true;
			}
		}

	}

	/**
	 * <pre>
	 * BR_G29 - ENTRY.activeIsMember
	 *
	 * For each ENTRY in local MINDEX where type == 'ACTIVE'
	 *     ENTRY.activeIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 *
	 * For each ENTRY in local MINDEX where type != 'ACTIVE':
	 *     ENTRY.activeIsMember = true
	 * </pre>
	 */
	default void BR_G29() {
		for (final var entry : indexM()) {
			if (entry.type.equals("ACTIVE")) {
				entry.activeIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
			} else {
				entry.activeIsMember = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G30 - ENTRY.revokedIsMember
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *     ENTRY.revokedIsMember = true
	 *
	 * For each ENTRY in local MINDEX where revoked_on != null:
	 *     ENTRY.revokedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 * </pre>
	 */
	default void BR_G30() {
		for (final var entry : indexM()) {
			if (entry.revoked_on != null) {
				entry.revokedIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
			} else {
				entry.revokedIsMember = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G31 - ENTRY.alreadyRevoked
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *     ENTRY.alreadyRevoked = false
	 *
	 * For each ENTRY in local MINDEX where revoked_on != null:
	 *     ENTRY.alreadyRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
	 * </pre>
	 */
	default void BR_G31() {
		for (final var entry : indexM()) {
			if (entry.revoked_on != null) {
				entry.alreadyRevoked = indexMGlobal().anyMatch(m -> m.pub.equals(entry.pub) && m.revoked_on != null);
			} else {
				entry.alreadyRevoked = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G32 - ENTRY.revocationSigOK
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *     ENTRY.revocationSigOK = true
	 *
	 * For each ENTRY in local MINDEX where revoked_on != null:
	 *     ENTRY.revocationSigOK = SIG_CHECK_REVOKE(REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]), ENTRY)
	 * </pre>
	 */
	default void BR_G32() {
		for (final var entry : indexM()) {
			if (entry.revoked_on != null) {
				entry.revocationSigOK = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub));
				// TODO sigcheck
			} else {
				entry.revocationSigOK = true;
			}
		}
	}

	/**
	 * <pre>
	 * BR_G33 - ENTRY.excludedIsMember
	 *
	 * For each ENTRY in local IINDEX where member != false:
	 *     ENTRY.excludedIsMember = true
	 *
	 * For each ENTRY in local IINDEX where member == false:
	 *     ENTRY.excludedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
	 * </pre>
	 */
	default void BR_G33() {
		for (final var entry : indexI()) {
			if (entry.member) {
				entry.excludedIsMember = true;
			} else {
				entry.excludedIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
			}
		}
	}

	/**
	 * <pre>
	 * BR_G34 - ENTRY.isBeingRevoked
	 *
	 * For each ENTRY in local MINDEX where revoked_on == null:
	 *     ENTRY.isBeingRevoked = false
	 * For each ENTRY in local MINDEX where revoked_on != null:
	 *     ENTRY.isBeingRevoked = true
	 * </pre>
	 *
	 */
	default void BR_G34() {
		for (final var entry : indexM()) {
			entry.isBeingRevoked = entry.revoked_on != null;
		}
	}

	/**
	 * <pre>
	 * BR_G35 - ENTRY.isBeingKicked
	 *
	 * For each ENTRY in local IINDEX where member != false:
	 *     ENTRY.isBeingKicked = false
	 * For each ENTRY in local IINDEX where member == false:
	 *     ENTRY.isBeingKicked = true
	 * </pre>
	 */
	default void BR_G35() {
		for (final var entry : indexI()) {
			entry.isBeingKicked = !entry.member;
		}
	}

	/**
	 * <pre>
	 * BR_G36 - ENTRY.hasToBeExcluded
	 *
	 * For each ENTRY in local IINDEX:
	 *     ENTRY.hasToBeExcluded = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).kick
	 *
	 * </pre>
	 */
	default void BR_G36() {
		for (final var entry : indexI()) {
			entry.hasToBeExcluded = indexIGlobal().filter(i -> i.pub.equals(entry.pub) && i.kick);
		}
	}

	/**
	 * <pre>
	 *
	 * BR_G37 - ENTRY.age
	 *
	 * REF_BLOCK = HEAD~<HEAD~1.number + 1 -  NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)]
	 *
	 * If HEAD.number == 0 && ENTRY.created_on == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
	 *     ENTRY.age = 0
	 *
	 * Else if REF_BLOC != null:
	 *     ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
	 *
	 * Else:
	 *     ENTRY.age = conf.sigWindow + 1
	 *
	 * </pre>
	 *
	 * @param entry
	 */
	default void BR_G37(BINDEX head, CINDEX entry) {

		final long sigWindow = 0;

		if (head.number == 0 && entry.created_on
				.toString() == "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855") {
			entry.age = 0;
		} else {
			entry.age = Stream.of(indexB()) //
					.filter(b -> b.hash.equals(entry.created_on.getHash())) //
					.filter(b -> b.number.equals(entry.created_on.getNumber())) //
					.findAny() //
					.map(refb -> prevHead().medianTime - refb.medianTime) //
					.orElse(sigWindow + 1);
		}

	}

	/**
	 * <pre>
	 * BR_G38 - ENTRY.unchainables // FIXME entry of what ?
	 *
	 * If HEAD.number > 0:
	 *     ENTRY.unchainables = COUNT(GLOBAL_CINDEX[issuer=ENTRY.issuer, chainable_on > HEAD~1.medianTime]))
	 * </pre>
	 */
	default void BR_G38(BINDEX head, CINDEX entry) {
		if (head.number > 0) {
			entry.unchainables = indexCGlobal()//
					.filter(c -> c.issuer.equals(entry.issuer) && c.chainable_on > prevHead().medianTime)//
					.count();
		}

	}

	/**
	 * <pre>
	 * BR_G39 - ENTRY.stock
	 *
	 * ENTRY.stock = COUNT(REDUCE_BY(GLOBAL_CINDEX[issuer=ENTRY.issuer], 'receiver', 'created_on')[expired_on=0])
	 *
	 * </pre>
	 */
	default void BR_G39(BINDEX head, CINDEX entry) {
		// TODO REDUCE BY recevier ??
		entry.stock = indexCGlobal().filter(c -> c.issuer.equals(entry.issuer)).count();

	}

	/**
	 * <pre>
	 * BR_G40 - ENTRY.fromMember
	 *
	 * ENTRY.fromMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.issuer]).member
	 * </pre>
	 */
	default void BR_G40(BINDEX head, CINDEX entry) {
		entry.fromMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.issuer) && i.member);
	}

	/**
	 * <pre>
	 * BR_G41 - ENTRY.toMember
	 *
	 * ENTRY.toMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]).member
	 *
	 * </pre>
	 */
	default void BR_G41(BINDEX head, CINDEX entry) {
		entry.toMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.receiver) && i.member);

	}

	/**
	 * <pre>
	 * BR_G42 - ENTRY.toNewcomer
	 *
	 * ENTRY.toNewcomer = COUNT(LOCAL_IINDEX[member=true,pub=ENTRY.receiver]) > 0
	 * </pre>
	 */
	default void BR_G42(BINDEX head, CINDEX entry) {
		entry.toNewcomer = indexIGlobal().anyMatch(i -> i.pub.equals(entry.receiver) && i.member);

	}

	/**
	 * <pre>
	 * BR_G43 - ENTRY.toLeaver
	 *
	 * ENTRY.toLeaver = REDUCE(GLOBAL_MINDEX[pub=ENTRY.receiver]).leaving
	 * </pre>
	 */
	default void BR_G43(BINDEX head, CINDEX entry) {
		entry.toLeaver = indexIGlobal().anyMatch(i -> i.pub.equals(entry.receiver) && i.leaving);

	}

	/**
	 * <pre>
	 * BR_G44 - ENTRY.isReplay
	 *
	 * reducible = GLOBAL_CINDEX[issuer=ENTRY.issuer,receiver=ENTRY.receiver,expired_on=0]
	 *
	 * If count(reducible) == 0:
	 *     ENTRY.isReplay = false
	 * Else:
	 *     ENTRY.isReplay = reduce(reducible).expired_on == 0
	 * </pre>
	 */
	default void BR_G44(BINDEX head, CINDEX entry) {
		entry.isReplay = indexCGlobal()
				.anyMatch(c -> c.issuer.equals(entry.issuer) && c.receiver.equals(entry.receiver) && c.expired_on == 0);
	}

	/**
	 * <pre>
	 *
	 * BR_G45 - ENTRY.sigOK
	 *
	 * ENTRY.sigOK = SIG_CHECK_CERT(REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]), ENTRY)
	 *
	 * </pre>
	 *
	 */
	default void BR_G45(BINDEX head, CINDEX entry) {
		// TODO: signature
		entry.sigOK = indexIGlobal().anyMatch(i -> i.pub.equals(entry.receiver));
	}

	/**
	 * <pre>
	 * Local SINDEX augmentation
	 *
	 * BR_G46 - ENTRY.available and ENTRY.conditions
	 *
	 * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
	 *     INPUT_ENTRIES = LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 *
	 *     If COUNT(INPUT_ENTRIES) == 0
	 *         INPUT_ENTRIES = GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 *
	 *     INPUT = REDUCE(INPUT_ENTRIES)
	 *     ENTRY.conditions = INPUT.conditions
	 *     ENTRY.available = INPUT.consumed == false
	 *
	 * </pre>
	 */
	default boolean BR_G46() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G47 - ENTRY.isLocked
	 *
	 * INPUT_ENTRIES = LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * If COUNT(INPUT_ENTRIES) == 0
	 *     INPUT_ENTRIES = GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 *
	 * INPUT = REDUCE(INPUT_ENTRIES)
	 * ENTRY.isLocked = TX_SOURCE_UNLOCK(INPUT.conditions, ENTRY)
	 * </pre>
	 */
	default boolean BR_G47() {
		return false;
	}

	/**
	 * <pre>
	 *
	 * BR_G48 - ENTRY.isTimeLocked
	 *
	 * INPUT_ENTRIES = LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * If COUNT(INPUT_ENTRIES) == 0
	 *      INPUT_ENTRIES = GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
	 * EndIf
	 *
	 * INPUT = REDUCE(INPUT_ENTRIES)
	 * ENTRY.isTimeLocked = ENTRY.written_time - INPUT.written_time < ENTRY.locktime
	 * </pre>
	 */
	default boolean BR_G48() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G49 - Version
	 *
	 *  If `HEAD.number > 0`
	 *      HEAD.version == (HEAD~1.version OR HEAD~1.version + 1)
	 *
	 * </pre>
	 */
	default boolean BR_G49(BINDEX head) {
		return head.number > 0 && (head.version == prevHead().version || head.version == prevHead().version + 1);
	}

	/**
	 * <pre>
	 * BR_G50 - Block size
	 *
	 * Rule:
	 *
	 * If HEAD.number > 0:
	 *     HEAD.size < MAX(500 ; CEIL(1.10 * HEAD.avgBlockSize))
	 * </pre>
	 */
	default boolean BR_G50(BINDEX head) {
		return head.number > 0 && head.size < Math.max(500, Math.ceil(1.1 * head.avgBlockSize));
	}

	/**
	 * <pre>
	 * BR_G51 - Number
	 *
	 * Number = HEAD.number
	 * </pre>
	 */
	default boolean BR_G51() {
		// TODO
		return true;
	}

	/**
	 * <pre>
	 * BR_G52 - PreviousHash
	 *
	 * PreviousHash = HEAD.previousHash
	 * </pre>
	 */
	default boolean BR_G52() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G53 - PreviousIssuer
	 *
	 * PreviousHash = HEAD.previousHash
	 * </pre>
	 */
	default boolean BR_G53() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G54 - DifferentIssuersCount
	 *
	 * DifferentIssuersCount = HEAD.issuersCount
	 * </pre>
	 */
	default boolean BR_G54() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G55 - IssuersFrame
	 *
	 * IssuersFrame = HEAD.issuersFrame
	 * </pre>
	 */
	default boolean BR_G55() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G56 - IssuersFrameVar
	 *
	 * IssuersFrameVar = HEAD.issuersFrameVar
	 * </pre>
	 */
	default boolean BR_G56() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G57 - MedianTime
	 *
	 * MedianTime = HEAD.medianTime
	 * </pre>
	 */
	default boolean BR_G57() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G58 - UniversalDividend
	 *
	 * UniversalDividend = HEAD.new_dividend
	 * </pre>
	 */
	default boolean BR_G58() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G59 - UnitBase
	 *
	 * UnitBase = HEAD.unitBase
	 * </pre>
	 */
	default boolean BR_G59() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G60 - MembersCount
	 *
	 * MembersCount = HEAD.membersCount
	 * </pre>
	 */
	default boolean BR_G60() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G61 - PowMin
	 *
	 * If HEAD.number > 0
	 *     PowMin = HEAD.powMin
	 * </pre>
	 */
	default boolean BR_G61() {
		return false;
	}

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
	default boolean BR_G62() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G63 - Identity writability
	 *
	 * ENTRY.age <= [idtyWindow]
	 */
	default boolean BR_G63() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G64 - Membership writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [msWindow]
	 */
	default boolean BR_G64() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G65 - Certification writability
	 *
	 * Rule:
	 *
	 * ENTRY.age <= [sigWindow]
	 */
	default boolean BR_G65() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G66 - Certification stock
	 *
	 * Rule:
	 *
	 * ENTRY.stock <= sigStock
	 */
	default boolean BR_G66() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G67 - Certification period
	 *
	 * Rule:
	 *
	 * ENTRY.unchainables == 0
	 */
	default boolean BR_G67() {
		return false;
	}

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
	default boolean BR_G68() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G69 - Certification to member or newcomer
	 *
	 * Rule:
	 *
	 * ENTRY.toMember == true OR ENTRY.toNewcomer == true
	 */
	default boolean BR_G69() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G70 - Certification to non-leaver
	 *
	 * Rule:
	 *
	 * ENTRY.toLeaver == false
	 */
	default boolean BR_G70() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G71 - Certification replay
	 *
	 * Rule:
	 *
	 * ENTRY.isReplay == false
	 */
	default boolean BR_G71() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G72 - Certification signature
	 *
	 * Rule:
	 *
	 * ENTRY.sigOK == true
	 */
	default boolean BR_G72() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G73 - Identity UserID unicity
	 *
	 * Rule:
	 *
	 * ENTRY.uidUnique == true
	 */
	default boolean BR_G73() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G74 - Identity pubkey unicity
	 *
	 * Rule:
	 *
	 * ENTRY.pubUnique == true
	 */
	default boolean BR_G74() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G75 - Membership succession
	 *
	 * Rule:
	 *
	 * ENTRY.numberFollowing == true
	 */
	default boolean BR_G75() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G76 - Membership distance check
	 *
	 * Rule:
	 *
	 * ENTRY.distanceOK == true
	 */
	default boolean BR_G76() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G77 - Membership on revoked
	 *
	 * Rule:
	 *
	 * ENTRY.onRevoked == false
	 */
	default boolean BR_G77() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G78 - Membership joins twice
	 *
	 * Rule:
	 *
	 * ENTRY.joinsTwice == false
	 */
	default boolean BR_G78() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G79 - Membership enough certifications
	 *
	 * Rule:
	 *
	 * ENTRY.enoughCerts == true
	 */
	default boolean BR_G79() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G80 - Membership leaver
	 *
	 * Rule:
	 *
	 * ENTRY.leaverIsMember == true
	 */
	default boolean BR_G80() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G81 - Membership active
	 *
	 * Rule:
	 *
	 * ENTRY.activeIsMember == true
	 */
	default boolean BR_G81() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G82 - Revocation by a member
	 *
	 * Rule:
	 *
	 * ENTRY.revokedIsMember == true
	 */
	default boolean BR_G82() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G83 - Revocation singleton
	 *
	 * Rule:
	 *
	 * ENTRY.alreadyRevoked == false
	 */
	default boolean BR_G83() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G84 - Revocation signature
	 *
	 * Rule:
	 *
	 * ENTRY.revocationSigOK == true
	 */
	default boolean BR_G84() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G85 - Excluded is a member
	 *
	 * Rule:
	 *
	 * ENTRY.excludedIsMember == true
	 */
	default boolean BR_G85() {
		return false;
	}

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
	default boolean BR_G86() {
		return false;
	}

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
	default boolean BR_G87() {
		return false;
	}

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
	default boolean BR_G88() {
		return false;
	}

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
	default boolean BR_G89() {
		return false;
	}

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
	default boolean BR_G90() {
		return false;
	}

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
	default boolean BR_G91() {
		return false;
	}

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
	default boolean BR_G92() {
		return false;
	}

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
	default boolean BR_G93() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G94 - Exclusion by membership
	 *
	 * For each LOCAL_MINDEX[expired_on!=0] as MS, add a new LOCAL_IINDEX entry:
	 *
	 * IINDEX ( op = 'UPDATE' pub = MS.pub written_on = BLOCKSTAMP kick = true )
	 */
	default boolean BR_G94() {
		return false;
	}

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
	default boolean BR_G95() {
		return false;
	}

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
	default boolean BR_G96() {
		return false;
	}

	/**
	 * <pre>
	 * BR_G97 - Final INDEX operations
	 *
	 * If all the rules pass, then all the LOCAL INDEX values (IINDEX, MINDEX,
	 * CINDEX, SINDEX, BINDEX) have to be appended to the GLOBAL INDEX.
	 */
	default boolean BR_G97() {
		return false;
	}

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
	default boolean BR_G98(BINDEX head) {

		return head.number > 0 && head.currency == params.get("currency");
	}

	/**
	 * <pre>
	 * BR_G99 - HEAD.currency
	 *
	 * If HEAD.number > 0
	 *     HEAD.currency = HEAD~1.currency
	 * Else
	 *     HEAD.currency = null
	 *
	 * </pre>
	 */
	default void BR_G99(BINDEX head) {
		if (head.number > 0) {
			head.currency = prevHead().currency;
		} else {
			head.currency = null;
		}
	}

	/**
	 *
	 *
	 * @return COUNT(UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer))
	 */
	Integer computeIssuersCount();

	/**
	 * <pre>
	 * Transactions chaining max depth
	 *
	 * FUNCTION `getTransactionDepth(txHash, LOCAL_DEPTH)`:
	 *
	 * INPUTS = LOCAL_SINDEX[op='UPDATE',tx=txHash] DEPTH = LOCAL_DEPTH
	 *
	 * FOR EACH `INPUT` OF `INPUTS` CONSUMED =
	 * LOCAL_SINDEX[op='CREATE',identifier=INPUT.identifier,pos=INPUT.pos] IF
	 * (CONSUMED != NULL) IF (LOCAL_DEPTH < 5) DEPTH = MAX(DEPTH,
	 * getTransactionDepth(CONSUMED.tx, LOCAL_DEPTH +1) ELSE DEPTH++ END_IF END_IF
	 * END_FOR
	 *
	 * RETURN DEPTH
	 *
	 * END_FUNCTION Then:
	 *
	 * maxTxChainingDepth = 0 For each TX_HASH of UNIQ(PICK(LOCAL_SINDEX, 'tx)):
	 *
	 * maxTxChainingDepth = MAX(maxTxChainingDepth, getTransactionDepth(TX_HASH, 0))
	 * Rule:
	 *
	 * maxTxChainingDepth <= 5
	 * </pre>
	 */
	void getTransactionDepth();

	default BINDEX head() {
		return indexB()[0];
	}

	BINDEX[] indexB();

	CINDEX[] indexC();

	Stream<CINDEX> indexCGlobal();

	IINDEX[] indexI();

	Stream<IINDEX> indexIGlobal();

	MINDEX[] indexM();

	Stream<MINDEX> indexMGlobal();

	SINDEX[] indexS();

	default void localAugmentationBINDEX() {
		final var entry = new BINDEX();
		BR_G01(entry);
		BR_G02(entry);
		BR_G03(entry);
		BR_G04(entry);
		BR_G05(entry);
		BR_G06(entry);
		BR_G07(entry);
		BR_G08(entry);
		BR_G09(entry);
		BR_G10(entry);
		BR_G11(entry);
		BR_G12(entry);
		BR_G13(entry);
		BR_G14(entry);
		BR_G15(entry);
		BR_G16(entry);
		BR_G17(entry);
		BR_G18(entry);
	}

	default void localAugmentationCINDEX(BINDEX head) {
		for (final var entry : indexC()) {
			BR_G37(head, entry);
			BR_G38(head, entry);
			BR_G39(head, entry);
			BR_G40(head, entry);
			BR_G41(head, entry);
			BR_G42(head, entry);
			BR_G43(head, entry);
			BR_G44(head, entry);
			BR_G45(head, entry);
		}
	}

	default void localAugmentationIINDEX(BINDEX head) {
		BR_G19(head);
		BR_G20();
		BR_G21();
		BR_G33();
		BR_G35();
		BR_G36();
	}

	default void localAugmentationMINDEX() {
		final var head = new BINDEX();

		BR_G22(head);
		BR_G23();
		BR_G24(head);
		BR_G25();
		BR_G26();
		BR_G27();
		BR_G28();
		BR_G29();
		BR_G30();
		BR_G31();
		BR_G32();
		BR_G34();
		BR_G107();
	}

	default void localAugmentationSINDEX() {
		BR_G102();
		BR_G46();
		BR_G47();
		BR_G48();
	}

	default BINDEX prevHead() {
		return indexB()[1];
	}

	/**
	 * fetches few last block index
	 *
	 * @param m
	 * @return
	 */
	default Stream<BINDEX> range(long m) {
		final var i = head().number - m;
		return Stream.of(indexB()).sorted().filter(h -> h.number > i);
	}
}
