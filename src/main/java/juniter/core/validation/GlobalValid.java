package juniter.core.validation;

import juniter.core.model.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
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
 * LOCAL_INDEX are indexes local to a block (in order to commit once)
 * GLOBAL_INDEX is the index
 *
 * </pre>
 *
 * @author bnimajneb (the copist monk)
 * @author cgeek (the author)
 * @see <a href="https://git.duniter.org/nodes/typescript/duniter/blob/dev/doc/Protocol.md#br_g01-headnumber"></a>
 */
public interface GlobalValid {

    class BINDEX implements Comparable<BINDEX> {

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
        Integer dividend = null;
        Long mass = null;
        Long massReeval = null;
        Integer unitBase = 0;
        int powMin;

        Long udTime = null;
        Long diffTime = null;
        Long speed = null;

        // FIXME these shouldnt be in BINDEX
        Integer new_dividend;
        long diffNumber;
        int powRemainder;
        int powZeros;
        long udReevalTime;

        @Override
        public int compareTo(BINDEX o) {
            return number.compareTo(o.number);
        }

        @Override
        public String toString() {
            return "BINDEX(" + number + "-" + hash + "  "
                    + previousIssuer + ","
                    + previousHash
                    + ", v:" + version
                    + ", size:" + size
                    + "," + issuersCount + ")";
        }

    }

    class BStamp {

        int number;
        String hash;

        public BStamp(Integer number, String hash) {
            this.number = number;
            this.hash = hash;
        }

        public BStamp(String string) {
            final String[] pat = string.split("-");
            number = Integer.valueOf(pat[0]);
            hash = pat[1];
        }

        public Object getHash() {
            return hash;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return number + "-" + hash;
        }

    }

    /**
     * <pre>
     * the DUP format in block 0  :
     * 0.0488:86400:1000:432000:100:5259600:63115200:5:5259600:5259600:0.8:31557600:5:24:300:12:0.67:1488970800:1490094000:15778800
     * </pre>
     *
     * @see <a href="https://git.duniter.org/nodes/typescript/duniter/blob/dev/doc/Protocol.md#protocol-parameters"></a>
     */
    class ChainParameters {

        double c = 0.0488; // growth rate
        long dt = 86400; // 1 day between DU
        long ud0 = 1000; // 10.00 g1
        long sigPeriod = 432000; // 5 days signature period
        long sigStock = 100;
        long sigWindow = 5259600; // 60 days, 21 hours
        long sigValidity = 63115200; // 730 days, 12 hours,
        long sigQty = 5;
        long idtyWindow = 5259600;
        long msWindow = 5259600;
        double xpercent = 0.8;
        long msValidity = 31557600; // 365 days, 6 hours,
        long stepMax = 5;
        long medianTimeBlocks = 24;
        long avgGenTime = 300;
        long dtDiffEval = 12; //
        double percentRot = 0.67;
        long udTime0 = 1488970800; // GMT: Wednesday, March 8, 2017 11:00:00 AM
        long udReevalTime0 = 1490094000; // GMT: Tuesday, March 21, 2017 11:00:00 AM ??
        long dtReeval = 15778800; // 182 days, 15 hours

        /**
         * c:dt:ud0:sigPeriod:sigStock:sigWindow:sigValidity:sigQty:idtyWindow:msWindow:xpercent:msValidity:
         * stepMax:medianTimeBlocks:avgGenTime:dtDiffEval:percentRot:udTime0:udReevalTime0:dtReeval
         * <p>
         * 0.0488:86400:1000:432000:100:5259600:63115200:5:5259600:5259600:0.8:31557600:5:24:300:12:0.67:1488970800:1490094000:15778800
         * 0.007376575:3600:1200:0:40:604800:31536000:1:604800:604800:0.9:31536000:3:20:960:10:0.6666666666666666:1489675722:1489675722:3600",
         *
         * @param DUPParams:
         */
        public void accept(String DUPParams) {
            final var params = DUPParams.split(":");
            c = Double.parseDouble(params[0]);
            dt = Long.parseLong(params[1]);
            ud0 = Long.parseLong(params[2]);
            sigPeriod = Long.parseLong(params[3]);
            sigStock = Long.parseLong(params[4]);
            sigWindow = Long.parseLong(params[5]);
            sigValidity = Long.parseLong(params[6]);
            sigQty = Long.parseLong(params[7]);
            idtyWindow = Long.parseLong(params[8]);
            msWindow = Long.parseLong(params[9]);
            xpercent = Double.parseDouble(params[10]);
            msValidity = Long.parseLong(params[11]);
            stepMax = Long.parseLong(params[12]);
            medianTimeBlocks = Long.parseLong(params[13]);
            avgGenTime = Long.parseLong(params[14]);
            dtDiffEval = Long.parseLong(params[15]);
            percentRot = Double.parseDouble(params[16]);
            udTime0 = Long.parseLong(params[17]);
            udReevalTime0 = Long.parseLong(params[18]);
            dtReeval = Long.parseLong(params[19]);
        }

        double maxAcceleration() {
            return Math.ceil(maxGenTime() * medianTimeBlocks);
        }

        private double maxGenTime() {
            return Math.ceil(avgGenTime * 1.189);
        }

        double maxSpeed() {
            return 1 / minGenTime();
        }

        private double minGenTime() {
            return Math.floor(avgGenTime / 1.189);
        }

        double minSpeed() {
            return 1 / maxGenTime();
        }

    }

    /**
     * <pre>
     * <h1>Certifications</h1>
     *
     * The local CINDEX has a unicity constraint on PUBKEY_FROM, PUBKEY_TO
     *
     * The local CINDEX has a unicity constraint on PUBKEY_FROM, except for block#0
     *
     * The local CINDEX must not match a MINDEX operation on
     *     PUBLIC_KEY = PUBKEY_FROM,
     *     member = false or PUBLIC_KEY = PUBKEY_FROM,
     *     leaving = true
     *
     *
     * <h1>Functionally</h1>
     *
     * a block cannot have 2 identical certifications (A -> B)
     * a block cannot have 2 certifications from a same public key, except in block#0
     * a block cannot have a certification to a leaver or an excluded
     * </pre>
     */
    class CINDEX implements Comparable<CINDEX> {

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

        // FIXME these shouldnt be in the model
        long unchainables;

        public long age;
         long stock;
         boolean toMember;
         boolean toNewcomer;
         boolean toLeaver;
         boolean isReplay;
         boolean sigOK;
         boolean fromMember;

        public CINDEX(String op, String issuer, String receiver, BStamp created_on, BStamp written_on, String sig,
                      long expires_on, long chainable_on, Long expired_on) {

            this.op = op;
            this.issuer = issuer;
            this.receiver = receiver;
            this.created_on = created_on;
            this.written_on = written_on + "";
            this.sig = sig;
            this.expires_on = expires_on;
            this.chainable_on = chainable_on;
            this.expired_on = expired_on;
        }

        public CINDEX(String op, String issuer, String receiver, BStamp created_on, Long expired_on) {

            this.op = op;
            this.issuer = issuer;
            this.receiver = receiver;
            this.created_on = created_on;
            this.expired_on = expired_on;
        }

        @Override
        public int compareTo(CINDEX o) {
            return (receiver + "-" + issuer).compareTo(o.receiver + "-" + o.issuer);
        }

        @Override
        public boolean equals(Object obj) {
            final var c = (CINDEX) obj;
            return c.issuer.equals(issuer) && c.receiver.equals(receiver) && c.receiver.equals(receiver);
        }

        @Override
        public String toString() {
            return "CINDEX[" + op + "," + issuer + "," + receiver + "," + created_on + "," + written_on + "," + sig
                    + "," + expires_on + "," + expired_on + "," + chainable_on + "," + from_wid + "," + to_wid + ","
                    + writtenOn + "]";
        }

    }

    class Conf {
        long txWindow = 604800; // 3600 * 24 * 7
        long msPeriod = 604800;
    }

    /**
     * <pre>
     * * UserID and PublicKey unicity
     *
     * The local IINDEX has a unicity constraint on USER_ID.
     * The local IINDEX has a unicity constraint on PUBLIC_KEY.
     *
     *  Each local IINDEX op = 'CREATE' operation  must match a single local MINDEX
     *      op = 'CREATE',
     *      pub = PUBLIC_KEY operation.
     *
     * Functionally:
     *
     *     UserID and public key must be unique in a block,
     *     each new identity must have an opt-in document attached.
     * </pre>
     */
    class IINDEX implements Comparable<IINDEX> {

        String op;
        String uid;
        String pub;
        String hash;
        String sig;
        BStamp created_on;
        BStamp written_on;
        boolean member;
        boolean wasMember;
        boolean kick;
        Integer wotbid;
        Integer writtenOn;

        // FIXME just transient ?
        public long age;

        boolean uidUnique;
        boolean pubUnique;
        boolean excludedIsMember;
        boolean isBeingKicked;
        boolean hasToBeExcluded;
        boolean leaving;

        public IINDEX(String op, String pub, BStamp written_on, boolean kick) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.kick = kick;
        }

        public IINDEX(String op, String uid, String pub, BStamp created_on, BStamp written_on, boolean member,
                      Boolean wasMember, boolean kick) {
            this.op = op;
            this.uid = uid;
            this.pub = pub;
            this.created_on = created_on;
            this.written_on = written_on;
            this.member = member;
            this.wasMember = wasMember;
            this.kick = kick;
        }

        @Override
        public int compareTo(IINDEX o) {
            return pub.compareTo(o.pub);
        }

        @Override
        public String toString() {
            return "IINDEX[" + op + "," + uid + "," + pub + "," + hash + "," + sig + "," + created_on + "," + written_on
                    + "," + member + "," + wasMember + "," + kick + "," + wotbid + "," + writtenOn + "]";

        }

    }

    /**
     * <pre>
     * * Membership unicity
     *
     * The local MINDEX has a unicity constraint on PUBLIC_KEY
     *
     * Functionally: a user has only 1 status change allowed per block.
     *
     *
     *
     * Revocation implies exclusion
     *
     * Each local MINDEX ̀op = 'UPDATE', revoked_on = BLOCKSTAMP
     *
     * operations must match a single local IINDEX
     *
     * op = 'UPDATE', pub = PUBLIC_KEY, member = false operation.
     *
     * Functionally: a revoked member must be immediately excluded.
     *
     * </pre>
     */
    class MINDEX implements Comparable<MINDEX> {
        String op;

        String pub;
        BStamp created_on;
        BStamp written_on;
        Long expires_on;
        Long expired_on;
        Long revokes_on;
        Long revoked_on;
        Integer leaving;
        String revocation;
        Long chainable_on;
        Integer writtenOn;

        // FIXME these shouldn't be attribute of the model, methods perhaps ?
        public long age;

         boolean numberFollowing;
        public String type;

         boolean distanceOK;
         boolean onRevoked;
         boolean joinsTwice;
         boolean enoughCerts;
         boolean leaverIsMember;
         boolean activeIsMember;
         boolean revokedIsMember;
         boolean alreadyRevoked;
         boolean revocationSigOK;
        public boolean excludedIsMember;
         boolean isBeingRevoked;

        /**
         * @param op: wwxc
         * @param pub : _
         * @param created_on : _
         * @param written_on : _
         * @param type : _
         * @param expires_on : _
         * @param revokes_on : _
         * @param revoked_on : _
         * @param revocation_sig : _
         * @param leaving : _
         */
        public MINDEX(String op, String pub, BStamp created_on, BStamp written_on, String type, Long expires_on,
                      Long revokes_on, Long revoked_on, String revocation_sig, boolean leaving, Long chainable_on) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.revoked_on = revoked_on;
            this.created_on = created_on;
            this.type = type;
            this.expires_on = expires_on;
            this.revokes_on = revokes_on;
            this.revocation = revocation;
            this.chainable_on = chainable_on;
        }

        public MINDEX(String op, String pub, BStamp written_on, Long revoked_on) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.revoked_on = revoked_on;
        }

        @Override
        public int compareTo(MINDEX o) {
            return (pub + type).compareTo(o.pub + o.type);
        }

        @Override
        public String toString() {
            return "MINDEX[" + op + "," + pub + "," + created_on + "," + written_on + "," + expires_on + ","
                    + expired_on + "," + revokes_on + "," + revoked_on + "," + leaving + "," + revocation + ","
                    + chainable_on + "," + writtenOn + "]";
        }

    }

    /**
     * <pre>
     * * Sources
     *
     * The local SINDEX has a unicity constraint on UPDATE, IDENTIFIER, POS
     * The local SINDEX ........................... CREATE, ...............
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
     * 	<h2>Double-spending control</h2>
     * Definitions:
     *
     * For each SINDEX unique tx:
     *  - inputs are the SINDEX row matching UPDATE, tx
     *  - outputs are the SINDEX row matching CREATE, tx
     *
     *
     * Functionally: we gather the sources for each transaction, in order to check them.
     *
     * CommonBase
     *
     * Each input has an InputBase, and each output has an OutputBase. These bases are to be called AmountBase.
     *
     * The CommonBase is the lowest base value among all AmountBase of the transaction.
     *
     * For any amount comparison, the respective amounts must be translated into CommonBase using the following rule:
     *
     * AMOUNT(CommonBase) = AMOUNT(AmountBase) x POW(10, AmountBase - CommonBase)
     *
     * So if a transaction only carries amounts with the same AmountBase, no conversion is required.
     * But if a transaction carries:
     *
     *   input_0 of value 45 with AmountBase = 5
     *   input_1 of value 75 with AmountBase = 5
     *   input_2 of value 3 with AmountBase = 6
     *   output_0 of value 15 with AmountBase = 6
     *
     * Then the output value has to be converted before being compared:
     *
     * CommonBase = 5
     *
     * output_0(5) = output_0(6) x POW(10, 6 - 5)
     * output_0(5) = output_0(6) x POW(10, 1)
     * output_0(5) = output_0(6) x 10
     * output_0(5) = 15 x 10
     * output_0(5) = 150
     * input_0(5) = input_0(5)
     * input_0(5) = 45
     * input_1(5) = input_1(5)
     * input_1(5) = 75
     * input_2(5) = input_2(6) x POW(10, 6 - 5)
     * input_2(5) = input_2(6) x POW(10, 1)
     * input_2(5) = input_2(6) x 10
     * input_2(5) = 3 x 10
     * input_2(5) = 30
     *
     * The equality of inputs and outputs is then verified because:
     *
     * output_0(5) = 150
     * input_0(5) = 45
     * input_1(5) = 75
     * input_2(5) = 30
     *
     * output_0(5) = input_0(5) + input_1(5) + input_2(5)
     * 150 = 45 + 75 + 30
     * TRUE
     *
     * Amounts
     *
     *
     *
     * Def.: InputBaseSum is the sum of amounts with the same InputBase.
     *
     * Def.: OutputBaseSum is the sum of amounts with the same OutputBase.
     *
     * Def.: BaseDelta = OutputBaseSum - InputBaseSum, expressed in CommonBase
     *
     *
     * Rule: For each OutputBase:
     *
     *
     * if BaseDelta > 0, then it must be inferior or equal to the sum of all preceding BaseDelta
     *
     *
     *
     * Rule: The sum of all inputs in CommonBase must equal the sum of all outputs in CommonBase
     *
     *
     * Functionally: we cannot create nor lose money through transactions. We can only transfer coins we own.
     * Functionally: also, we cannot convert a superiod unit base into a lower one.
     *
     *
     * </pre>
     */
    class SINDEX implements Comparable<SINDEX> {

        String op;
        String tx;
        String identifier;
        Integer pos;
        BStamp created_on;
        BStamp written_on;
        Long written_time;
        int amount;
        int base;
        Long locktime;
        boolean consumed;
        String condition;
        Integer writtenOn;

        // FIXME model? transient? function?
        long age;

        boolean available;

         boolean isLocked;
         boolean isTimeLocked;

        public SINDEX() {
        }

        public SINDEX(String op, String identifier, Integer pos, BStamp created_on, BStamp written_on,
                      long written_time, boolean consumed) {

            this(op, identifier, pos, created_on, written_on, written_time, 0, 0, 0L, identifier, consumed);

        }

        public SINDEX(String op, String identifier, Integer pos, BStamp created_on, BStamp written_on,
                      long written_time, int amount, int base, Long locktime, String condition, boolean consumed) {
            this.op = op;
            this.identifier = identifier;
            this.pos = pos;
            this.created_on = created_on;
            this.written_on = written_on;
            this.written_time = written_time;
            this.amount = amount;
            this.base = base;
            this.locktime = locktime;
        }

        @Override
        public int compareTo(SINDEX o) {
            return (op + "-" + identifier + "-" + pos + "-" + written_on)
                    .compareTo(o.op + "-" + o.identifier + "-" + o.pos + "-" + o.written_on);
        }

        String getCondition() {
            return condition;
        }

        @Override
        public String toString() {
            return "SINDEX[" + op + ", " + tx + "," + created_on + "," + written_on + "," + identifier + ","
                    + pos + ", " + written_time + ", " + amount + "," + base + "," + locktime + ","
                    + consumed + ", " + condition + ", " + writtenOn + "]";
        }

    }

    /**
     * Local CINDEX as a set for unicity
     */
    Set<CINDEX> IndexC = new TreeSet<>();

    Set<IINDEX> IndexI = new TreeSet<>();

    List<BINDEX> IndexB = new ArrayList<>();

    Set<MINDEX> IndexM = new TreeSet<>();

    Set<SINDEX> IndexS = new TreeSet<>();

    ChainParameters blockChainParams = new ChainParameters();

    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    default void augmentGlobal(BINDEX head, Block block) {
        final var bstamp = new BStamp(block.getNumber(), block.getHash());
        BR_G91(head, bstamp); // dividend

        BR_G92(head); // certif Expiry

        BR_G93(head); // membership expiry

        BR_G96(head); // Implicit revocation

        BR_G106(head, bstamp); // low accounts

    }

    default void augmentLocal(BINDEX head, Block block) {
        final var bstamp = new BStamp(block.getNumber(), block.getHash());

        BR_G94(bstamp); // Exclusion by Membership
        BR_G95(bstamp); // Exclusion by certification

    }

    /**
     * <pre>
     *
     * In Order :
     *
     * BR_G01 - setNumber
     * BR_G02 - setPreviousHash
     * BR_G03 - setPreviousIssuer
     * BR_G04 - setIssuersCount
     * BR_G05 - setIssuersFrame
     * BR_G06 - setIssuersFrameVar
     * BR_G07 - setAvgBlockSize
     * BR_G08 - setMedianTime
     * BR_G09 - setDiffNUmber
     * BR_G10 - setMembersCount
     * BR_G11 - setUdTime
     * BR_G12 - setUnitBase
     * BR_G13 - setDividend
     * BR_G14 - setDividend NewDividend UnitBase
     * BR_G15 - setMass&MassReval
     * BR_G16 - setSpeed
     * BR_G17 - setPowMin
     * BR_G18 - setPowZero
     * </pre>
     *
     * @param newHead | null
     * @return the BINDEX augmented, if newHead is null, a new BINDEX
     */
    default BINDEX augmentLocalBINDEX(BINDEX newHead) {

        if (newHead == null) {
            newHead = new BINDEX();
        }
        BR_G01(newHead); // setNumber
        BR_G02(newHead); // setPreviousHash
        BR_G03(newHead); // setPreviousIssuer
        BR_G04(newHead); // setIssuersCount
        BR_G05(newHead); // setIssuersFrame
        BR_G06(newHead); // setIssuersFrameVar
        BR_G07(newHead); // setAvgBlockSize
        BR_G08(newHead); // setMedianTime
        BR_G09(newHead); // setDiffNUmber

        BR_G10(newHead); // setMembersCount -> COUNT iindex
        BR_G11(newHead); // setUdTime
        BR_G12(newHead); // setUnitBase
        BR_G13(newHead); // setDividend
        BR_G14(newHead); // seDividend NewDividend UnitBase
        BR_G15(newHead); // setMass&MassReval
        BR_G16(newHead); // setSpeed
        BR_G17(newHead); // setPowMin
        BR_G18(newHead); // setPowZero

        BR_G99(newHead); // set Currency
        BR_G100(newHead); // issuerIsMember

        return newHead;
    }

    default void augmentLocalCINDEX(BINDEX head) {
        for (final var entry : IndexC) {
            BR_G37(head, entry);
            BR_G38(head, entry);
            BR_G39(entry);
            BR_G40(entry);
            BR_G41(entry);
            BR_G42(entry);
            BR_G43(entry);
            BR_G44(entry);
            BR_G45(entry); //
            BR_G105(entry);
        }
    }

    default void augmentLocalIINDEX(BINDEX head) {

        for (final var entry : IndexI) {
            BR_G19(head, entry);
            BR_G20(entry);
            BR_G21(entry);
            BR_G33(entry);
            BR_G35(entry);
            BR_G36(entry);
        }

    }

    default void augmentLocalMINDEX(BINDEX head) {
        //		System.out.println("augmentLocalMINDEX " + IndexM.size());

        for (final var entry : IndexM) {
            BR_G22(head, entry);
            BR_G23(entry);
            BR_G24(head, entry); // distanceOK
            BR_G25(entry);
            BR_G26(entry);
            BR_G27(entry);
            BR_G28(entry);
            BR_G29(entry);
            BR_G30(entry);
            BR_G31(entry);
            BR_G32(entry);
            BR_G34(entry);
            BR_G104(entry);
            BR_G107(entry);
        }
    }

    default void augmentLocalSINDEX(BINDEX head) {
        //		System.out.println("augmentLocalSINDEX " + IndexS.size());

        for (final SINDEX entry : IndexS) {
            if (entry.op.equals("CREATE")) {
                BR_G46(entry);
                BR_G47(entry);
                BR_G48(entry);
            }

            if (entry.op.equals("UPDATE")) {
                BR_G102(entry, head);
            }

        }
    }

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
    private void BR_G01(BINDEX head) {
        head.number = prevHead() != null ? prevHead().number + 1 : 0;
        //		System.out.println("Set number " + head.number + " " + prevHead());
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
    private void BR_G02(BINDEX head) {

        assert head != null : "BR_G02 - set previousHash - BINDEX null";

        if (head.number > 0) {
            head.previousHash = prevHead().hash;
            assert head.previousHash != null : "BR_G02 - set previousHash - Previous Hash null";
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
    private void BR_G03(BINDEX head) {
        if (head.number > 0) {
            head.previousIssuer = prevHead().issuer;
            assert head.previousIssuer != null : "BR_G03 - set previousIssuer - Previous Hash null";
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
     */
    private void BR_G04(BINDEX head) {
        if (head.number == 0) {
            head.issuersCount = 0;
        } else {
            head.issuersCount = (int) range(prevHead().issuersFrame).map(b -> b.issuer).distinct().count();
            assert head.issuersCount > 0 : "BR_G04 - set issuersCount - issuersCount !> 0 ";
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
     */
    private void BR_G05(BINDEX head) {
        if (head.number == 0) {
            head.issuersFrame = 1;
        } else if (prevHead().issuersFrameVar > 0) {
            head.issuersFrame = prevHead().issuersFrame + 1;
        } else if (prevHead().issuersFrameVar < 0) {
            head.issuersFrame = prevHead().issuersFrame - 1;
        } else {
            head.issuersFrame = prevHead().issuersFrame;
        }
        assert head.issuersFrame != null : "BR_G05 - set issuersFrame - issuersFrame is null";
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
     */
    private void BR_G06(BINDEX head) {
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
    private void BR_G07(BINDEX head) {
        assert head != null : "BR_G07 - HEAD.avgBlockSize - head is null  ";

        if (head.number == 0) {
            head.avgBlockSize = 0;
        } else {
            assert head.issuersCount > 0 : "BR_G07 - HEAD.avgBlockSize - issuersCount 0";
            head.avgBlockSize = (int) range(head.issuersCount)//
                    .mapToInt(h -> h.size)//
                    .average()//
                    .getAsDouble();
        }
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
    private void BR_G08(BINDEX head) {
        final var min = Math.min(blockChainParams.medianTimeBlocks, head.number);

        //		System.out.println(
        //				"BR_G08 : set medianTime " + min + " " + blockChainParams.medianTimeBlocks + " " + head.number);
        if (head.number > 0) {

            head.medianTime = (long) range(min) // fetch bindices
                    .mapToLong(h -> h.time) //
                    .average().orElse(Double.NaN);
            //			System.out.println("BR_G08 : ==> max  " + head.medianTime + " " + prevHead().medianTime);
            head.medianTime = Math.max(head.medianTime, prevHead().medianTime); // FIXME found in code, not in the spec
            // used at block 3
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
    private void BR_G09(BINDEX head) {

        if (head.number == 0) {
            head.diffTime = head.number + blockChainParams.dtDiffEval;

        } else if (prevHead().diffNumber <= head.number) {
            head.diffTime = prevHead().diffTime + blockChainParams.dtDiffEval;

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
     * <p>
     * Rather than using two counts use a single reduction
     */
    private void BR_G10(BINDEX head) {
        final int member = (int) IndexI.stream().filter(m -> m.member).count();
        final int notMember = (int) IndexI.stream().filter(m -> !m.member).count();

        //		System.out.println("BR_G10 " + IndexI.size() + " " + member + " " + notMember);
        if (head.number == 0) {
            head.membersCount = member;
        } else {
            head.membersCount = prevHead().membersCount + member - notMember;
        }

        assert head.membersCount > 0 : "BR_G10  set membersCount - " + head.membersCount;

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
    private void BR_G100(BINDEX head) {
        if (head.number == 0) {
            head.issuerIsMember = IndexI.stream().anyMatch(i -> i.pub.equals(head.issuer) && i.member);
        } else {
            head.issuerIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(head.issuer) && i.member);
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
    private boolean BR_G101(BINDEX head) {
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
    private void BR_G102(SINDEX entry, BINDEX head) {
        //		System.out.println("BR_G102 - set Age " + entry.created_on + " " + entry.op + " " + entry.amount);
        if (head.number == 0
                && "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855".equals(entry.created_on + "")) {
            entry.age = 0;
        } else {
            entry.age = range(prevHead().number + 1 - entry.created_on.getNumber()) //
                    .filter(b -> b.hash.equals(entry.created_on.getHash())) //
                    .findAny() //
                    .map(refBlock -> {
                        //						System.out.println("BR_G102 - set Age " + prevHead().medianTime + " " + refBlock.medianTime);
                        return prevHead().medianTime - refBlock.medianTime;
                    }) //
                    .orElse(conf().txWindow + 1);
        }

    }

    /**
     * <pre>
     * BR_G103 - Transaction writability
     *
     * Rule:
     *
     * ENTRY.age <= [txWindow]
     */
    private boolean BR_G103(SINDEX s) {
        assert s.age <= conf().txWindow : "BR_G103 -  Transaction writability - " + s.age + " " + conf().txWindow;
        return s.age <= conf().txWindow;
    }

    /**
     * <pre>
     * BR_G104 - Membership expiry date correction
     *
     * For each LOCAL_MINDEX[type='JOIN'] as MS:
     *     MS.expires_on = MS.expires_on - MS.age
     *     MS.revokes_on = MS.revokes_on - MS.age
     *
     * For each LOCAL_MINDEX[type='ACTIVE'] as MS:
     *     MS.expires_on = MS.expires_on - MS.age
     *     MS.revokes_on = MS.revokes_on - MS.age
     * </pre>
     */
    private void BR_G104(MINDEX ms) {
        if ("JOIN".equals(ms.type) || "ACTIVE".equals(ms.type)) {
            ms.expires_on = ms.expires_on - ms.age;
            ms.revokes_on = ms.revokes_on - ms.age;
        }

    }

    /**
     * <pre>
     * BR_G105 - Certification expiry date correction
     *
     * For each LOCAL_CINDEX as CERT:
     *
     * CERT.expires_on = CERT.expires_on - CERT.age
     */
    private void BR_G105(CINDEX c) {
        c.expires_on = c.expires_on - c.age;

    }

    /**
     * <pre>
     * BR_G106 - Low accounts
     *
     * Set:
     *
     * ACCOUNTS = UNIQ(GLOBAL_SINDEX, 'conditions')
     *
     * For each ACCOUNTS as ACCOUNT then:
     *
     * Set:
     *
     * ALL_SOURCES = CONCAT(GLOBAL_SINDEX[conditions=ACCOUNT.conditions], LOCAL_SINDEX[conditions=ACCOUNT.conditions])
     * SOURCES = REDUCE_BY(ALL_SOURCES, 'identifier', 'pos')[consumed=false]
     * BALANCE = SUM(MAP(SOURCES => SRC: SRC.amount * POW(10, SRC.base)))
     *
     * If BALANCE < 100 * POW(10, HEAD.unitBase), then
     *     for each SOURCES AS SRC add a new LOCAL_SINDEX entry:
     *
     * SINDEX (
     *          op = 'UPDATE'
     *  identifier = SRC.identifier
     *         pos = SRC.pos
     *  written_on = BLOCKSTAMP
     * written_time = MedianTime
     *    consumed = true )
     * </pre>
     */
    private void BR_G106(BINDEX head, BStamp block) {
        if (head.number >= 0)
            return; // FIXME

        indexSGlobal().filter(distinctByKey(SINDEX::getCondition)).forEach(account -> {
            final var allSources = Stream.concat(//
                    indexSGlobal().filter(s -> s.condition == account.condition),
                    IndexS.stream().filter(s -> s.condition == account.condition));
            final var sources = allSources.filter(s -> !s.consumed); // TODO REDUCE BY?
            final var balance = sources//
                    .map(src -> src.amount * Math.pow(src.base, 10))//
                    .reduce((bal1, bal2) -> bal1 + bal2)//
                    .orElse(Double.MAX_VALUE);

            if (balance < 100 * Math.pow(head.unitBase, 10)) {

                sources.forEach(src -> {
                    IndexS.add(new SINDEX("UPDATE", //
                            src.identifier, //
                            src.pos, //
                            null, // created_on
                            block, //
                            0L, //
                            true)); // TODO BLOCKSTAMP &
                });

            }
        });
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
    private boolean BR_G107(MINDEX entry) {
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
    private boolean BR_G108(CINDEX m) {
        return m.unchainables == 0;
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
    private void BR_G11(BINDEX head) {

        if (head.number == 0) {
            head.udTime = blockChainParams.udTime0;
            head.udReevalTime = blockChainParams.udReevalTime0;
        } else {

            if (prevHead().udTime <= head.medianTime) {
                head.udTime = prevHead().udTime + blockChainParams.dt;
            } else {
                head.udTime = prevHead().udTime;
            }

            if (prevHead().udReevalTime <= head.medianTime) {
                head.udReevalTime = prevHead().udReevalTime + blockChainParams.dtReeval;
            } else {
                head.udReevalTime = prevHead().udReevalTime;

            }
        }

    }

    /**
     * <pre>
     * BR_G12 - HEAD.unitBase
     *
     * If HEAD.number == 0:
     *     HEAD.unitBase = 0
     * Else:
     *     HEAD.unitBase = HEAD~1.unitBase
     * </pre>
     */
    private void BR_G12(BINDEX head) {
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
    private void BR_G13(BINDEX head) {
        head.new_dividend = null;
        if (head.number == 0) {
            head.dividend = (int) blockChainParams.ud0;
        } else {
            if (head.udReevalTime != prevHead().udReevalTime) {
                head.dividend = (int) (prevHead().dividend + blockChainParams.c * blockChainParams.c
                        + Math.ceil(prevHead().massReeval / Math.pow(10, prevHead().unitBase)) / head.membersCount);

            } else {
                head.dividend = prevHead().dividend;
            }

            if (head.udTime != prevHead().udTime) {
                head.new_dividend = head.dividend;
            }
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
     */
    private void BR_G14(BINDEX head) {

        if (head.dividend >= 1000000) {
            head.dividend = (int) Math.ceil(head.dividend / 10);
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
     */
    private void BR_G15(BINDEX head) {
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
    private void BR_G16(BINDEX head) {
        final int dtDiffEval = 0;

        if (head.number == 0) {
            head.speed = 0L;
        } else {
            final var range = Math.min(dtDiffEval, head.number);
            final var elapsed = head.medianTime - IndexB.get(range).medianTime;

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
    private void BR_G17(BINDEX head) {
        if (head.number == 0)
            return;

        //		System.out.println("BR_G17 - " + head.diffNumber + "!=" + prevHead().diffNumber);
        //		System.out.println(head.speed + " >= " + blockChainParams.maxSpeed() + prevHead().powMin);

        if (head.diffNumber != prevHead().diffNumber) {

            if (head.speed >= blockChainParams.maxSpeed()) { // too fast, increase difficulty
                if ((prevHead().powMin + 2) % 16 == 0) {
                    head.powMin = prevHead().powMin + 2;
                } else {
                    head.powMin = prevHead().powMin + 1;
                }
            }

            if (head.speed <= blockChainParams.minSpeed()) { // too slow, increase difficulty
                if (prevHead().powMin % 16 == 0) {
                    head.powMin = Math.max(0, prevHead().powMin - 2);
                } else {
                    head.powMin = Math.max(0, prevHead().powMin - 1);
                }
            }
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
     */
    private void BR_G18(BINDEX head) {

        long nbPersonalBlocksInFrame; // FIXME quesaco ?
        final int medianOfBlocksInFrame = 1;
        int nbPreviousIssuers = 0;
        int nbBlocksSince = 0;

        //		Stream<BINDEX> blocksOfIssuer = null;
        if (head.number != 0) {
            final var blocksOfIssuer = range(head.issuersFrame)//
                    .filter(i -> i.issuer.equals(head.issuer)) //
                    .collect(Collectors.toList());
            nbPersonalBlocksInFrame = blocksOfIssuer.size();
            final Object blocksPerIssuerInFrame = null;

            if (nbPersonalBlocksInFrame != 0) {
                final var first = blocksOfIssuer.get(0);
                nbPreviousIssuers = first.issuersCount;
                nbBlocksSince = prevHead().number - first.number;
            }

        }

        final var personalExcess = Math.max(0, 1);
        final var personalHandicap = Math.floor(Math.log(1 + personalExcess) / Math.log(1.189));

        head.issuerDiff = (int) Math.max(head.powMin,
                head.powMin * Math.floor(blockChainParams.percentRot * nbPreviousIssuers / (1 + nbBlocksSince)));

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
    private void BR_G19(BINDEX head, IINDEX entry) {

        if ("CREATE".equals(entry.op)) {
            if (head.number == 0 && entry.created_on
                    .toString() == "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855") {
                entry.age = 0;
            } else {
                entry.age = IndexB.stream() //
                        .filter(b -> b.hash.equals(entry.created_on.getHash())) //
                        .filter(b -> b.number.equals(entry.created_on.getNumber())) //
                        .findAny() //
                        .map(refb -> head.medianTime - refb.medianTime) //
                        .orElse(blockChainParams.idtyWindow + 1);
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
    private void BR_G20(IINDEX entry) {

        if ("CREATE".equals(entry.op)) {
            entry.uidUnique = indexIGlobal().noneMatch(i -> i.uid.equals(entry.uid));
        } else {
            entry.uidUnique = true;
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
    private void BR_G21(IINDEX entry) {
        if ("CREATE".equals(entry.op)) {
            entry.pubUnique = indexIGlobal().noneMatch(i -> i.sig.equals(entry.sig));
        } else {
            entry.pubUnique = true;
        }

    }

    /**
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
    private void BR_G22(BINDEX head, MINDEX entry) {

        if (entry.revoked_on == null) {

            if (head.number == 0) {
                entry.age = 0;
            } else {

                entry.age = IndexB.stream() //
                        .filter(b -> b.hash.equals(entry.created_on.getHash())) //
                        .filter(b -> b.number.equals(entry.created_on.getNumber())) //
                        .findAny() //
                        .map(refb -> head.medianTime - refb.medianTime) //
                        .orElse(blockChainParams.msWindow + 1);
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
    private void BR_G23(MINDEX entry) {
        if (entry.revoked_on == null) {
            entry.numberFollowing = indexMGlobal() //
                    .filter(m -> m.pub.equals(entry.pub)) //
                    .findAny() // suppose unicity, allow parallel
                    .map(m -> m.created_on) //
                    .map(on -> entry.created_on.getNumber() > on.getNumber()) //
                    .orElse(true);

        } else { // if revoked_on exists
            entry.numberFollowing = true;
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
    private void BR_G24(BINDEX head, MINDEX entry) {
        if (entry.type.equals("JOIN") || entry.type.equals("ACTIVE")) {
            final var dSen = Math.ceil(Math.pow(head.membersCount, 1.0 / blockChainParams.stepMax));
            final var graph = Stream.concat(IndexC.stream(), indexCGlobal()).collect(Collectors.toList());

            final Set<CINDEX> sentries = new TreeSet<>();

            final var reachedNode = graph.stream().filter(e -> e.issuer.equals(entry.pub))//
                    .map(edges -> graph.stream().filter(p1 -> edges.issuer.equals(p1.receiver)))
                    .map(stEdges -> graph.stream().filter(p2 -> stEdges.anyMatch(e -> e.issuer.equals(p2.receiver))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p3 -> stEdges.anyMatch(e -> e.issuer.equals(p3.receiver))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p4 -> stEdges.anyMatch(e -> e.issuer.equals(p4.receiver))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p5 -> stEdges.anyMatch(e -> e.issuer.equals(p5.receiver))))
                    .distinct().collect(Collectors.toList());

            entry.distanceOK = reachedNode.containsAll(sentries);

        } else {
            entry.distanceOK = true;
        }
        entry.distanceOK = true; // FIXME
    }

    /**
     * <pre>
     * BR_G25 - ENTRY.onRevoked
     *
     * For each ENTRY in local MINDEX:
     *     ENTRY.onRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
     * </pre>
     */
    private void BR_G25(MINDEX entry) {
        System.out.println("BR_G25 - ENTRY.onRevoked - " + entry);
        final var matches = indexMGlobal().filter(m -> m.pub.equals(entry.pub)).collect(Collectors.toList());

        matches.forEach(m -> {
            System.out.println("BR_G25" + m);
        });

        entry.onRevoked = indexMGlobal().anyMatch(m -> {
            final var res = m.pub.equals(entry.pub) && m.revoked_on != null;
            if (res) {
                System.out.println("BR_G25 - " + res);
            }
            return res;
        });

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
    private void BR_G26(MINDEX entry) {
        if (entry.op.equals("UPDATE") && entry.expired_on != null && entry.expired_on.equals(0L)) {
            entry.joinsTwice = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
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
     *
     * @param entry
     */
    private void BR_G27(MINDEX entry) {

        if (entry.type.equals("JOIN") || entry.type.equals("ACTIVE")) {
            final var cntG = indexCGlobal().filter(c -> c.receiver == entry.pub && c.expired_on.equals(0L)).count();
            final var cntL = IndexC.stream().filter(c -> {
                //				System.out.println(c.receiver + "  " + entry.pub + " " + c.expired_on);

                return c.receiver.equals(entry.pub) && c.expired_on == 0;
            }).count();
            //			System.out.println(cntL + " " + cntG + "  " + entry.pub + "  " + entry.expired_on);

            entry.enoughCerts = cntG + cntL >= blockChainParams.sigQty;
        } else {
            entry.enoughCerts = true;
        }

        assert entry.enoughCerts : "BR_G27 - not enough Certification for " + entry.pub;
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
    private void BR_G28(MINDEX entry) {
        if (entry.type.equals("LEAVE")) {
            entry.leaverIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
        } else {
            entry.leaverIsMember = true;
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
    private void BR_G29(MINDEX entry) {
        if (entry.type.equals("ACTIVE")) {
            entry.activeIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
        } else {
            entry.activeIsMember = true;
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
    private void BR_G30(MINDEX entry) {
        if (entry.revoked_on != null) {
            System.out.println("BR_G30 - ENTRY.revokedIsMember - " + entry.pub);
            entry.revokedIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
        } else {
            entry.revokedIsMember = true;
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
    private void BR_G31(MINDEX entry) {
        if (entry.revoked_on != null) {
            entry.alreadyRevoked = indexMGlobal().anyMatch(m -> m.pub.equals(entry.pub) && m.revoked_on != null);
        } else {
            entry.alreadyRevoked = false;
        }

        //		System.out.println("BR_G31 set alreadyRevoked " + entry.alreadyRevoked + " " + entry.revoked_on);
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
    private void BR_G32(MINDEX entry) {
        if (entry.revoked_on != null) {
            entry.revocationSigOK = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub));
            // TODO sigcheck requires Membership Document?
        } else {
            entry.revocationSigOK = true;
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
    private void BR_G33(IINDEX entry) {
        if (entry.member) {
            entry.excludedIsMember = true;
        } else {
            entry.excludedIsMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.member);
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
     */
    private void BR_G34(MINDEX entry) {
        entry.isBeingRevoked = entry.revoked_on != null;

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
    private void BR_G35(IINDEX entry) {
        entry.isBeingKicked = !entry.member;

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
    private void BR_G36(IINDEX entry) {
        entry.hasToBeExcluded = indexIGlobal().anyMatch(i -> i.pub.equals(entry.pub) && i.kick);

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
     * @param head : qsdq
     * @param entry : qssd
     */
    private void BR_G37(BINDEX head, CINDEX entry) {

        final long sigWindow = 0;

        if (head.number == 0 && entry.created_on
                .toString() == "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855") {
            entry.age = 0;
        } else {
            entry.age = IndexB.stream() //
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
    private void BR_G38(BINDEX head, CINDEX entry) {
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
    private void BR_G39(CINDEX entry) {
        // TODO REDUCE BY recevier, created_on ??
        entry.stock = indexCGlobal().filter(c -> c.issuer.equals(entry.issuer)).count();

    }

    /**
     * <pre>
     * BR_G40 - ENTRY.fromMember
     *
     * ENTRY.fromMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.issuer]).member
     * </pre>
     */
    private void BR_G40(CINDEX entry) {
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
    private void BR_G41(CINDEX entry) {
        entry.toMember = indexIGlobal().anyMatch(i -> i.pub.equals(entry.receiver) && i.member);

    }

    /**
     * <pre>
     * BR_G42 - ENTRY.toNewcomer
     *
     * ENTRY.toNewcomer = COUNT(LOCAL_IINDEX[member=true,pub=ENTRY.receiver]) > 0
     * </pre>
     */
    private void BR_G42(CINDEX entry) {
        entry.toNewcomer = IndexI.stream().anyMatch(i -> i.pub.equals(entry.receiver) && i.member);

    }

    /**
     * <pre>
     * BR_G43 - ENTRY.toLeaver
     *
     * ENTRY.toLeaver = REDUCE(GLOBAL_MINDEX[pub=ENTRY.receiver]).leaving
     * </pre>
     */
    private void BR_G43(CINDEX entry) {
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
    private void BR_G44(CINDEX entry) {
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
     */
    private void BR_G45(CINDEX entry) {
        entry.sigOK = true; // FIXME signature is a local check ??
    }

    /**
     * Local SINDEX augmentation
     *
     * <pre>
     *
     * BR_G46 - ENTRY.available and ENTRY.conditions
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.conditions = INPUT.conditions
     *     ENTRY.available = INPUT.consumed == false
     *
     * </pre>
     */
    private void BR_G46(SINDEX entry) {

        inputEntries(entry).forEach(input -> {
            System.out.println("BR_G46 - ENTRY.available and ENTRY.conditions" + entry.identifier + " - "
                    + input.consumed + " " + input.condition);
            entry.available = !input.consumed;
            entry.condition = input.condition;
        });
    }

    /**
     * <pre>
     * BR_G47 - ENTRY.isLocked
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.isLocked = TX_SOURCE_UNLOCK(INPUT.conditions, ENTRY)
     * </pre>
     */
    private void BR_G47(SINDEX entry) {

        inputEntries(entry).forEach(input -> {
            System.out.println(
                    "BR_G47 - ENTRY.isLocked" + entry.identifier + " - " + input.consumed + " " + input.condition);
            entry.isLocked = false; // FIXME BR_G47
        });
    }

    /**
     * <pre>
     *
     * BR_G48 - ENTRY.isTimeLocked
     *
     * INPUT = REDUCE(INPUT_ENTRIES)
     * ENTRY.isTimeLocked = ENTRY.written_time - INPUT.written_time < ENTRY.locktime
     * </pre>
     */
    private void BR_G48(SINDEX entry) {
        inputEntries(entry).forEach(input -> {
            System.out.println("BR_G48 - ENTRY.isTimeLocked - " + entry.written_time + " - " + input.written_time
                    + " < " + entry.locktime);

            entry.isTimeLocked = entry.written_time - input.written_time < entry.locktime;
        });
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
    private boolean BR_G49(BINDEX head) {
        return head.number == 0 || head.version == prevHead().version || head.version == prevHead().version + 1;
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
    private boolean BR_G50(BINDEX head) {
        return head.number == 0 || head.size < Math.max(500, Math.ceil(1.1 * head.avgBlockSize));
    }

    /**
     * <pre>
     * BR_G51 - Number
     *
     * Number = HEAD.number
     * </pre>
     */
    private boolean BR_G51(BINDEX testHead, Block shouldBe) {
        return testHead.number.equals(shouldBe.getNumber());
    }

    /**
     * <pre>
     * BR_G52 - PreviousHash
     *
     * PreviousHash = HEAD.previousHash
     * </pre>
     */
    private boolean BR_G52(BINDEX testHead, Block block) {
        if (testHead.number == 0)
            return true;

        assert testHead.previousHash.equals(block.getPreviousHash()) //
                : "BR_G52 - rule PreviousHash - " + testHead.previousHash + " " + block.getPreviousHash();

        return testHead.previousHash.equals(block.getPreviousHash());
    }

    /**
     * <pre>
     * BR_G53 - PreviousIssuer
     *
     * PreviousIssuer = HEAD.previousIssuer
     * </pre>
     */
    private boolean BR_G53(BINDEX testHead, Block shouldBe) {
        if (testHead.number == 0)
            return true;

        assert testHead.previousIssuer.equals(shouldBe.getPreviousIssuer()) : "BR_G53 - rule PreviousIssuer ";

        return testHead.previousIssuer.equals(shouldBe.getPreviousIssuer());
    }

    /**
     * <pre>
     * BR_G54 - DifferentIssuersCount // FIXME why Different.. ?
     *
     * DifferentIssuersCount = HEAD.issuersCount
     * </pre>
     */
    private boolean BR_G54(BINDEX testHead, Block shouldBe) {
        return testHead.issuersCount.equals(shouldBe.getIssuersCount());
    }

    /**
     * <pre>
     * BR_G55 - IssuersFrame
     *
     * IssuersFrame = HEAD.issuersFrame
     * </pre>
     */
    private boolean BR_G55(BINDEX testHead, Block shouldBe) {
        return testHead.issuersFrame.equals(shouldBe.getIssuersFrame());
    }

    /**
     * <pre>
     * BR_G56 - IssuersFrameVar
     *
     * IssuersFrameVar = HEAD.issuersFrameVar
     * </pre>
     */
    private boolean BR_G56(BINDEX testHead, Block block) {
        return testHead.issuersFrameVar.equals(block.getIssuersFrameVar());
    }

    /**
     * <pre>
     * BR_G57 - MedianTime
     *
     * MedianTime = HEAD.medianTime
     * </pre>
     */
    private boolean BR_G57(BINDEX head, Block block) {
        assert head.medianTime.equals(block.getMedianTime()) : "BR_G57 - rule Median Time - " + head.medianTime + " "
                + block.getMedianTime();

        return head.medianTime.equals(block.getMedianTime());
    }


    /**
     * <pre>
     * BR_G58 - UniversalDividend
     *
     * UniversalDividend = HEAD.new_dividend
     * </pre>
     *
     * @param testHead BINDEX
     * @param block the block we're comparing
     * @return ud equality accepting null
     */
    private boolean BR_G58(BINDEX testHead, Block block) {

        if (testHead.number == 0)
            return true;

        if (testHead.new_dividend == null)
            return block.getDividend() == null;
        else
            return testHead.new_dividend.equals(block.getDividend());

    }

    /**
     * <pre>
     * BR_G59 - UnitBase
     *
     * UnitBase = HEAD.unitBase
     * </pre>
     */
    private boolean BR_G59(BINDEX head, Block block) {
        assert head.unitBase.equals(block.getUnitbase()) : "BR_G59 - UnitBase - " + head.unitBase + " "
                + block.getDividend();
        return head.unitBase.equals(block.getUnitbase());
    }

    /**
     * <pre>
     * BR_G60 - MembersCount
     *
     * MembersCount = HEAD.membersCount
     * </pre>
     */
    private boolean BR_G60(BINDEX head, Block block) {
        assert head.membersCount.equals(block.getMembersCount()) : "BR_G60 MembersCount " + head.membersCount + "-"
                + block.getMembersCount();
        return head.membersCount.equals(block.getMembersCount());

    }

    /**
     * <pre>
     * BR_G61 - PowMin
     *
     * If HEAD.number > 0
     *     PowMin = HEAD.powMin
     * </pre>
     */
    private boolean BR_G61(BINDEX head, Block block) {
        assert block.getNumber() == 0 || head.powMin == block.getPowMin() : "BR_G61 - rule PowMin - " + head.powMin
                + " != " + block.getPowMin();
        return block.getNumber() == 0 || head.powMin == block.getPowMin();
    }

    /**
     * <pre>
     * BR_G62 - Proof-of-work
     *
     * Rule: the proof is considered valid if:
     *
     * HEAD.hash starts with at least HEAD.powZeros zeros
     *
     * HEAD.hash's HEAD.powZeros + 1th character is:
     *
     *
     * between [0-F] if HEAD.powRemainder = 0
     * between [0-E] if HEAD.powRemainder = 1
     * between [0-D] if HEAD.powRemainder = 2
     * between [0-C] if HEAD.powRemainder = 3
     * between [0-B] if HEAD.powRemainder = 4
     * between [0-A] if HEAD.powRemainder = 5
     * between [0-9] if HEAD.powRemainder = 6
     * between [0-8] if HEAD.powRemainder = 7
     * between [0-7] if HEAD.powRemainder = 8
     * between [0-6] if HEAD.powRemainder = 9
     * between [0-5] if HEAD.powRemainder = 10
     * between [0-4] if HEAD.powRemainder = 11
     * between [0-3] if HEAD.powRemainder = 12
     * between [0-2] if HEAD.powRemainder = 13
     * between [0-1] if HEAD.powRemainder = 14
     *
     * N.B.: it is not possible to have HEAD.powRemainder = 15
     *
     * </pre>
     */
    private boolean BR_G62(BINDEX head) {
        final String expectedStart = head.hash.substring(0, head.powZeros + 1);

        System.out.println("Proof-of-work " + expectedStart + " expected " + head.powZeros + " zeros with remainder "
                + head.powRemainder);

        for (int i = 0; i <= head.powZeros; i++) {
            if (expectedStart.charAt(i) != '0') {
                System.out.println("missing zeros at  " + i);
                return false;
            }
        }
        switch (head.powRemainder) {
            case 0:
                return expectedStart.matches("[0-9A-F]");
            case 1:
                return expectedStart.matches("[0-9A-E]");
            case 2:
                return expectedStart.matches("[0-9A-D]");
            case 3:
                return expectedStart.matches("[0-9A-C]");
            case 4:
                return expectedStart.matches("[0-9A-B]");
            case 5:
                return expectedStart.matches("[0-9A]");
            case 6:
                return expectedStart.matches("[0-9]");
            case 7:
                return expectedStart.matches("[0-8]");
            case 8:
                return expectedStart.matches("[0-7]");
            case 9:
                return expectedStart.matches("[0-6]");
            case 10:
                return expectedStart.matches("[0-5]");
            case 11:
                return expectedStart.matches("[0-4]");
            case 12:
                return expectedStart.matches("[0-3]");
            case 13:
                return expectedStart.matches("[0-2]");
            case 14:
                return expectedStart.matches("[01]");
            default:
                return true;
        }

    }

    /**
     * <pre>
     * BR_G63 - Identity writability
     *
     * ENTRY.age <= [idtyWindow]
     */
    private boolean BR_G63(IINDEX i) {
        assert i.age <= blockChainParams.idtyWindow : "BR_G63 - Identity writability";
        return i.age <= blockChainParams.idtyWindow;
    }

    /**
     * <pre>
     * BR_G64 - Membership writability
     *
     * Rule:
     *
     * ENTRY.age <= [msWindow]
     */
    private boolean BR_G64(MINDEX m) {
        assert m.age <= blockChainParams.msWindow //
                : "BR_G64 - Membership writability window - age:" + m.age + " for " + m;

        return m.age <= blockChainParams.msWindow;
    }

    /**
     * <pre>
     * BR_G65 - Certification writability
     *
     * Rule:
     *
     * ENTRY.age <= [sigWindow]
     */
    private boolean BR_G65(CINDEX c) {
        assert c.age <= blockChainParams.sigWindow : "BR_G65 - Certification writability";
        return c.age <= blockChainParams.sigWindow;
    }

    /**
     * <pre>
     * BR_G66 - Certification stock
     *
     * Rule:
     *
     * ENTRY.stock <= sigStock
     */
    private boolean BR_G66(CINDEX c) {
        assert c.stock <= blockChainParams.sigStock : "BR_G66 - Certification stock";
        return c.stock <= blockChainParams.sigStock;
    }

    /**
     * <pre>
     * BR_G67 - Certification period
     *
     * Rule:
     *
     * ENTRY.unchainables == 0
     */
    private boolean BR_G67(CINDEX c) {
        assert c.unchainables == 0 : "BR_G67 - Certification period";
        return c.unchainables == 0;
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
    private boolean BR_G68(BINDEX head, CINDEX entry) {
        assert head.number == 0 || entry.fromMember : "BR_G68 - Certification from member";
        return head.number == 0 || entry.fromMember;
    }

    /**
     * <pre>
     * BR_G69 - Certification to member or newcomer
     *
     * Rule:
     *
     * ENTRY.toMember == true OR ENTRY.toNewcomer == true
     */
    private boolean BR_G69(CINDEX c) {
        assert c.toMember || c.toNewcomer : "BR_G69 - Certification to member or newcomer";
        return c.toMember || c.toNewcomer;
    }

    /**
     * <pre>
     * BR_G70 - Certification to non-leaver
     *
     * Rule:
     *
     * ENTRY.toLeaver == false
     */
    private boolean BR_G70(CINDEX c) {
        assert !c.toLeaver : "BR_G70 - Certification to non-leaver";
        return !c.toLeaver;
    }

    /**
     * <pre>
     * BR_G71 - Certification replay
     *
     * Rule:
     *
     * ENTRY.isReplay == false
     */
    private boolean BR_G71(CINDEX c) {
        assert !c.isReplay : "BR_G71 - Certification replay";
        return !c.isReplay;
    }

    /**
     * <pre>
     * BR_G72 - Certification signature
     *
     * Rule:
     *
     * ENTRY.sigOK == true
     */
    private boolean BR_G72(CINDEX c) {
        assert c.sigOK : "BR_G72 - Certification signature ";
        return c.sigOK;
    }

    /**
     * <pre>
     * BR_G73 - Identity UserID unicity
     *
     * Rule:
     *
     * ENTRY.uidUnique == true
     */
    private boolean BR_G73(IINDEX i) {
        assert i.uidUnique : "BR_G73 - Identity UserID unicity ";
        return i.uidUnique;
    }

    /**
     * <pre>
     * BR_G74 - Identity pubkey unicity
     *
     * Rule:
     *
     * ENTRY.pubUnique == true
     */
    private boolean BR_G74(IINDEX i) {
        assert i.pubUnique : "BR_G74 - Identity pubkey unicity ";
        return i.pubUnique;
    }

    /**
     * <pre>
     * BR_G75 - Membership succession
     *
     * Rule:
     *
     * ENTRY.numberFollowing == true
     */
    private boolean BR_G75(MINDEX m) {
        assert m.numberFollowing : "BR_G75 - Membership succession ";
        return m.numberFollowing;
    }

    /**
     * <pre>
     * BR_G76 - Membership distance check
     *
     * Rule:
     *
     * ENTRY.distanceOK == true
     */
    private boolean BR_G76(MINDEX m) {
        assert m.distanceOK : "BR_G76 - Membership distance check ";
        return m.distanceOK;
    }

    /**
     * <pre>
     * BR_G77 - Membership on revoked
     *
     * Rule:
     *
     * ENTRY.onRevoked == false
     */
    private boolean BR_G77(MINDEX m) {
        assert !m.onRevoked : "BR_G77 - Membership on revoked " + m.pub;
        return !m.onRevoked;
    }

    /**
     * <pre>
     * BR_G78 - Membership joins twice
     *
     * Rule:
     *
     * ENTRY.joinsTwice == false
     */
    private boolean BR_G78(MINDEX m) {
        assert !m.joinsTwice : "BR_G78 - Membership joins twice ";
        return !m.joinsTwice;
    }

    /**
     * <pre>
     * BR_G79 - Membership enough certifications
     *
     * Rule:
     *
     * ENTRY.enoughCerts == true
     */
    private boolean BR_G79(MINDEX m) {
        assert m.enoughCerts : "BR_G79 - Membership enough certifications ";
        return m.enoughCerts;
    }

    /**
     * <pre>
     * BR_G80 - Membership leaver
     *
     * Rule:
     *
     * ENTRY.leaverIsMember == true
     */
    private boolean BR_G80(MINDEX m) {
        assert m.leaverIsMember : "BR_G80 - Membership leaver";
        return m.leaverIsMember;
    }

    /**
     * <pre>
     * BR_G81 - Membership active
     *
     * Rule:
     *
     * ENTRY.activeIsMember == true
     */
    private boolean BR_G81(MINDEX m) {
        assert m.activeIsMember : "BR_G81 - Membership active";
        return m.activeIsMember;
    }

    /**
     * <pre>
     * BR_G82 - Revocation by a member
     *
     * Rule:
     *
     * ENTRY.revokedIsMember == true
     */
    private boolean BR_G82(MINDEX m) {
        assert m.revokedIsMember : "BR_G82 - Revocation by a member";
        return m.revokedIsMember;
    }

    /**
     * <pre>
     * BR_G83 - Revocation singleton
     *
     * Rule:
     *
     * ENTRY.alreadyRevoked == false
     */
    private boolean BR_G83(MINDEX m) {
        assert !m.alreadyRevoked : "BR_G83 - Revocation singleton";
        return !m.alreadyRevoked;
    }

    /**
     * <pre>
     * BR_G84 - Revocation signature
     *
     * Rule:
     *
     * ENTRY.revocationSigOK == true
     */
    private boolean BR_G84(MINDEX m) {
        assert m.revocationSigOK : "BR_G84 - Revocation signature";
        return m.revocationSigOK;
    }

    /**
     * <pre>
     * BR_G85 - Excluded is a member
     *
     * Rule:
     *
     * ENTRY.excludedIsMember == true
     */
    private boolean BR_G85(IINDEX i) {
        assert i.excludedIsMember : "BR_G85 - Excluded is a member " + i.excludedIsMember;
        return i.excludedIsMember;
    }

    /**
     * <pre>
     * BR_G86 - Excluded contains exactly those to be kicked
     *
     * Rule:
     *
     * For each REDUCE_BY(GLOBAL_IINDEX[kick=true], 'pub') as TO_KICK:
     *
     *     REDUCED = REDUCE(GLOBAL_IINDEX[pub=TO_KICK.pub])
     *
     *     If REDUCED.kick then:
     *         COUNT(LOCAL_MINDEX[pub=REDUCED.pub,isBeingKicked=true]) == 1
     *
     * Rule:
     *
     * For each IINDEX[member=false] as ENTRY: // FIXME: local or global ?
     *
     *     ENTRY.hasToBeExcluded = true
     *
     * </pre>
     */
    private boolean BR_G86() {

        boolean ExcludedContainsExactlyThoseToBeKicked = //
                indexIGlobal() //
                        .filter(i -> i.kick) //
                        .flatMap(toKick -> indexIGlobal().filter(i -> i.pub == toKick.pub)) //
                        .filter(i -> i.kick) //
                        .flatMap(reduced -> indexMGlobal().filter(m -> m.pub.equals(reduced.pub))) //
                        .count() == 1;

        if (ExcludedContainsExactlyThoseToBeKicked) {
            IndexI.forEach(i -> {
                if (!i.member) {
                    i.hasToBeExcluded = true;
                }

            });
        }

        ExcludedContainsExactlyThoseToBeKicked = true; // FIXME

        assert ExcludedContainsExactlyThoseToBeKicked : "BR_G86 - Excluded contains exactly those to be kicked";
        return ExcludedContainsExactlyThoseToBeKicked;
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
     * </pre>
     */
    private boolean BR_G87(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert true : "BR_G87 - rule Input is not available " + entry.available + " " + entry.consumed;
            return true; // FIXME
        } else
            return true;
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
    private boolean BR_G88(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert !entry.isLocked : "BR_G88 - rule Input is locked";
            return !entry.isLocked;
        } else
            return true;
    }

    ;

    /**
     * <pre>
     * BR_G89 - Input is time unlocked
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.isTimeLocked == false
     * </pre>
     */
    private boolean BR_G89(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert !entry.isTimeLocked : "BR_G89 - rule Input is time unlocked - ";
            return !entry.isTimeLocked;
        } else
            return true;
    }

    ;

    /**
     * <pre>
     * BR_G90 - Output base
     *
     * For each LOCAL_SINDEX[op='CREATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.unitBase <= HEAD~1.unitBase
     * </pre>
     */
    private boolean BR_G90(SINDEX entry) {
        assert entry != null : "BR_G90 - rule Output Base - entry is null";

        if ("CREATE".equals(entry.op)) {

            assert prevHead() != null : "BR_G90 - rule Output base - HEAD-1 is null ";

            assert entry.base <= prevHead().unitBase //
                    : "BR_G90 - rule Output base - " + entry.base + " " + prevHead().unitBase;

            return entry.base <= prevHead().unitBase;
        } else
            return true;

    }

    /**
     * <pre>
     * BR_G91 - Dividend
     *
     * If HEAD.new_dividend != null:
     *
     * For each REDUCE_BY(GLOBAL_IINDEX[member=true], 'pub') as IDTY then if IDTY.member, add a new LOCAL_SINDEX entry:
     *
     * SINDEX (
     *          op = 'CREATE'
     *  identifier = IDTY.pub
     *         pos = HEAD.number
     *  written_on = BLOCKSTAMP
     * written_time = MedianTime
     *      amount = HEAD.dividend
     *        base = HEAD.unitBase
     *    locktime = null
     *  conditions = REQUIRE_SIG(MEMBER.pub)
     *    consumed = false
     *    )
     *
     *
     * For each LOCAL_IINDEX[member=true] as IDTY add a new LOCAL_SINDEX entry:
     *
     * SINDEX (
     *          op = 'CREATE'
     *  identifier = IDTY.pub
     *         pos = HEAD.number
     *  written_on = BLOCKSTAMP
     * written_time = MedianTime
     *      amount = HEAD.dividend
     *        base = HEAD.unitBase
     *    locktime = null
     * conditions = REQUIRE_SIG(MEMBER.pub)
     *    consumed = false
     *    )
     *
     * </pre>
     */
    private void BR_G91(BINDEX head, BStamp block) { // TODO sig
        if (head.new_dividend == null)
            return;

        indexIGlobal().filter(i -> i.member)
                .map(i -> new SINDEX("CREATE",
                        i.pub,
                        head.number,
                        null,
                        block,
                        0L,
                        head.dividend,
                        head.unitBase,
                        null,
                        "",
                        false))

                .forEach(tx -> {

                    //					System.out.println("BR_G91 adding tx " + tx.base + " from " + head.unitBase);
                    IndexS.add(tx);
                });

        IndexI.stream().filter(i -> i.member) //
                .map(i -> new SINDEX("CREATE",
                        i.pub,
                        head.number,
                        null,
                        block,
                        0L,
                        head.dividend,
                        head.unitBase,
                        null,
                        "",
                        false))
                .forEach(tx -> IndexS.add(tx) );

        //		assert IndexI.size() > 0 : "BR_G91 - Dividend - IndexS shouldnt be empty";
    }

    /**
     * <pre>
     * BR_G92 - Certification expiry
     *
     * For each GLOBAL_CINDEX[expires_on<=HEAD.medianTime] as CERT, add a new LOCAL_CINDEX entry:
     *
     * If reduce(GLOBAL_CINDEX[issuer=CERT.issuer,receiver=CERT.receiver,created_on=CERT.created_on]).expired_on == 0:
     *
     * CINDEX (
     *         op = 'UPDATE'
     *     issuer = CERT.issuer
     *   receiver = CERT.receiver
     * created_on = CERT.created_on
     * expired_on = HEAD.medianTime
     * )
     * </pre>
     */
    private void BR_G92(BINDEX head) {

        indexCGlobal().filter(c -> c.expires_on <= head.medianTime)
                .map(c -> new CINDEX("UPDATE",
                        c.issuer,
                        c.receiver,
                        c.created_on,
                        head.medianTime))
                .forEach(IndexC::add);

    }

    /**
     * <pre>
     * BR_G93 - Membership expiry
     *
     * For each REDUCE_BY(GLOBAL_MINDEX[expires_on<=HEAD.medianTime AND revokes_on>HEAD.medianTime], 'pub') as POTENTIAL then consider
     *     REDUCE(GLOBAL_MINDEX[pub=POTENTIAL.pub]) AS MS.
     *
     *     If MS.expired_on == null OR MS.expired_on == 0, add a new LOCAL_MINDEX entry:
     *
     *         MINDEX ( op = 'UPDATE'
     *                 pub = MS.pub
     *          written_on = BLOCKSTAMP
     *          expired_on = HEAD.medianTime )
     * </pre>
     */
    private void BR_G93(BINDEX head) {
        indexMGlobal() //
                .filter(m -> {
                    assert m.expires_on != null : "BR_G93 - expires_on cannot be null, cannot compare ";
                    assert m.revokes_on != null : "BR_G93 - revokes_on cannot be null, cannot compare ";

                    return m.expires_on <= head.medianTime && m.revokes_on > head.medianTime;
                }) //
                .flatMap(potential -> indexMGlobal().filter(m -> m.pub.equals(potential.pub))) //
                .filter(ms -> ms.expired_on == null || ms.expired_on == 0) //
                .forEach(ms -> {
                    IndexM.add(new MINDEX("UPDATE", ms.pub, null, head.medianTime));
                });
    }

    /**
     * <pre>
     * BR_G94 - Exclusion by membership
     *
     * For each LOCAL_MINDEX[expired_on!=0] as MS, add a new LOCAL_IINDEX entry:
     *
     * IINDEX ( op = 'UPDATE'
     *         pub = MS.pub
     *  written_on = BLOCKSTAMP
     *        kick = true )
     * </pre>
     */
    private void BR_G94(BStamp blockstamp) {

        for (final MINDEX m : IndexM) {
            //			System.out.println(m);
            if (m.expired_on != null && m.expired_on != 0) {
                IndexI.add(new IINDEX("UPDATE", //
                        m.pub, //
                        blockstamp, // written_on
                        true // kick
                ));
            }
        }
    }

    /**
     * <pre>
     * BR_G95 - Exclusion by certification //FIXME REDUCE BY ...
     *
     * For each LOCAL_CINDEX[expired_on!=0] as CERT:
     *
     * Set:
     *
     * CURRENT_VALID_CERTS = REDUCE_BY(GLOBAL_CINDEX[receiver=CERT.receiver], 'issuer', 'receiver', 'created_on')[expired_on=0]
     *
     * If
     * COUNT(CURRENT_VALID_CERTS) +
     * COUNT(LOCAL_CINDEX[receiver=CERT.receiver,expired_on=0]) -
     * COUNT(LOCAL_CINDEX[receiver=CERT.receiver,expired_on!=0]) < sigQty
     *
     *     add a new LOCAL_IINDEX entry:
     *
     *     IINDEX ( op = 'UPDATE'
     *             pub = CERT.receiver
     *      written_on = BLOCKSTAMP
     *            kick = true )
     *
     * </pre>
     */
    private void BR_G95(BStamp block) {
        IndexC.forEach(cert -> {

            final var cntLocal = IndexC.stream().filter(c -> c.receiver.equals(cert.receiver) && c.expired_on == 0)
                    .count();
            final var cntExpired = IndexC.stream().filter(c -> c.receiver.equals(cert.receiver) && c.expired_on != 0)
                    .count();
            final var cntValid = indexCGlobal().filter(c -> c.receiver.equals(cert.receiver)).count();

            if (cntValid + cntLocal - cntExpired < blockChainParams.sigQty) {
                IndexI.add(new IINDEX("UPDATE", cert.receiver, block, true));
            }

        });

    }

    /**
     * <pre>
     * BR_G96 - Implicit revocation
     *
     * For each GLOBAL_MINDEX[revokes_on<=HEAD.medianTime,revoked_on=null] as MS:
     *
     *     REDUCED = REDUCE(GLOBAL_MINDEX[pub=MS.pub])
     *
     *      If REDUCED.revokes_on<=HEAD.medianTime AND REDUCED.revoked_on==null,
     *
     *      add a new LOCAL_MINDEX entry:
     *
     * MINDEX ( op = 'UPDATE'
     *         pub = MS.pub
     *  written_on = BLOCKSTAMP
     *  revoked_on =  HEAD.medianTime )
     *
     * </pre>
     */
    private void BR_G96(BINDEX head) {
        indexMGlobal() //
                .filter(m -> m.revokes_on <= head.medianTime && m.revoked_on == null) //
                .flatMap(potential -> indexMGlobal()// Join onto MINDEX.pub
                        .filter(m -> m.pub.equals(potential.pub)))
                .filter(ms -> ms.revokes_on.equals(head.medianTime) || ms.revoked_on == null)//
                .forEach(ms -> {
                    IndexM.add(new MINDEX("UPDATE", ms.pub, null, head.medianTime));
                });
    }

    /**
     * <pre>
     * BR_G97 - Final INDEX operations
     *
     * If all the rules pass, then all the LOCAL INDEX values (IINDEX, MINDEX,
     * CINDEX, SINDEX, BINDEX) have to be appended to the GLOBAL INDEX.
     *
     * </pre>
     */
    private boolean BR_G97(BINDEX newHead, Block block, boolean checkPoW) {

        if (globalCheckBINDEX(newHead, block, checkPoW) && //
                globalCheckSINDEX() && //
                globalCheckMINDEX() && //
                globalCheckCINDEX(newHead) && //
                globalCheckIINDEX()) {
            IndexB.add(newHead); // local commit only

            return commit(IndexB, IndexI, IndexM, IndexC, IndexS);
        } else {
            System.out.println("BR_G97 did not pass ");
            System.out.println("BR_G97: " + IndexI.size() + " " + globalCheckBINDEX(newHead, block, checkPoW) + " "
                    + globalCheckSINDEX() + " " + globalCheckMINDEX() + " " + globalCheckCINDEX(newHead) + " "
                    + globalCheckIINDEX());
            return false;
        }

    }

    /**
     * <pre>
     * BR_G98 - Currency
     *
     * Rule: "Currency  has a block to block consistency but has no expectations on block 0"
     *
     * If HEAD.number > 0:
     *     Currency = HEAD.currency
     * else
     *     true
     * </pre>
     */
    private boolean BR_G98(BINDEX head, Block ccy) {
        assert head != null : "BR_G98 - rule currency - head is null ";

        if (head.number == 0)
            return true;
        else {
            assert head.currency != null : "BR_G98 - rule currency - BINDEX.currency is null ";
            assert ccy != null : "BR_G98 - rule currency - Block.currency is null ";
            assert head.currency.equals(ccy.getCurrency()) : "BR_G98 - rule currency - Block.currency is different ";
            return head.currency.equals(ccy.getCurrency());
        }

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
    private void BR_G99(BINDEX head) {

        if (head.number > 0) {
            head.currency = prevHead().currency;
            assert prevHead() != null : "BR_G99 - set Currency - HEAD-1 is null ";
        } else {
            head.currency = null;
        }

    }

    /**
     * Commit the arguments to the GLOBAL_INDEX sources (to database or to simple
     * java structure).
     * <p>
     * This operation is Transactional
     *
     * @param indexB :
     * @param indexI :
     * @param indexM :
     * @param indexC :
     * @param indexS :
     */
    boolean commit(List<BINDEX> indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS);

    default Conf conf() {
        return new Conf();
    }

    /**
     * <pre>
     * Transactions chaining max depth
     *
     * FUNCTION `getTransactionDepth(txHash, LOCAL_DEPTH)`:
     *
     * INPUTS = LOCAL_SINDEX[op='UPDATE',tx=txHash]
     * DEPTH = LOCAL_DEPTH
     *
     * FOR EACH `INPUT` OF `INPUTS`
     *     CONSUMED = LOCAL_SINDEX[op='CREATE',identifier=INPUT.identifier,pos=INPUT.pos]
     *     IF (CONSUMED != NULL)
     *         IF (LOCAL_DEPTH < 5)
     *              DEPTH = MAX(DEPTH, getTransactionDepth(CONSUMED.tx, LOCAL_DEPTH +1)
     *         ELSE
     *              DEPTH++
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
    default int getTransactionDepth(String txHash, int localDepth) {
        final var inputs = IndexS.stream() //
                .filter(s -> s.op.equals("UPDATE") && s.tx.equals(txHash))//
                .collect(Collectors.toList());
        var depth = localDepth;
        for (final SINDEX input : inputs) {
            final var consumed = IndexS.stream()
                    .filter(s -> s.op.equals("CREATE")
                            && s.identifier.equals(input.identifier)
                            && s.pos.equals(input.pos))
                    .findAny();

            if (consumed.isPresent()) {
                if (localDepth < 5) {
                    depth = Math.max(depth, getTransactionDepth(consumed.get().tx, localDepth));
                } else {
                    depth++;
                }
            }
        }

        return depth;
    }

    default boolean globalCheckBINDEX(BINDEX testHead, Block block, boolean checkPoW) {

        //		System.out.println("globalCheckBINDEX " + BR_G49(testHead) + " " + BR_G50(testHead) + " "
        //				+ BR_G98(testHead, block) + " " + BR_G51(testHead, block) + " " + BR_G52(testHead, block) + " "
        //				+ BR_G53(testHead, block) + " " + BR_G54(testHead, block) + " " + BR_G55(testHead, block) + " "
        //				+ BR_G56(testHead, block) + " " + BR_G57(testHead, block) + " " + BR_G58(testHead, block) + " "
        //				+ BR_G59(testHead, block) + " " + BR_G60(testHead, block) + " " + BR_G61(testHead, block) + " "
        //				+ (!checkPoW || BR_G62(testHead)));

        return BR_G49(testHead) && // Version
                BR_G50(testHead) && // blockSize
                BR_G98(testHead, block) && // Currency
                BR_G51(testHead, block) && // number
                BR_G52(testHead, block) && // previous hash
                BR_G53(testHead, block) && // previous issuer
                BR_G54(testHead, block) && // different issuer
                BR_G55(testHead, block) && // issuerFrame
                BR_G56(testHead, block) && // issuerFrameVar
                BR_G57(testHead, block) && // mediantime
                BR_G58(testHead, block) && // ud
                BR_G59(testHead, block) && // unitbase
                BR_G60(testHead, block) && // membersCount
                BR_G61(testHead, block) && // powmin
                BR_G101(testHead) && // issuerIsMember
                (!checkPoW || BR_G62(testHead));
    }

    default boolean globalCheckCINDEX(BINDEX head) {
        return IndexC.stream()//
                .allMatch(c -> {

                    //					System.out.println("globalCheckCINDEX " + //
                    //
                    //					BR_G65(c) + " writability" + //
                    //					BR_G66(c) + " stock" + //
                    //					BR_G67(c) + " period" + //
                    //					BR_G68(head, c) + " fromMember" + //
                    //					BR_G69(c) + " toMember" + //
                    //					BR_G70(c) + " toLeaver" + //
                    //					BR_G71(c) + " isReblay" + //
                    //					BR_G72(c) + " signature" + //
                    //					BR_G108(c));

                    return BR_G65(c) // writability
                            && BR_G66(c) // stock
                            && BR_G67(c) // period
                            && BR_G68(head, c) // fromMember
                            && BR_G69(c) // toMember
                            && BR_G70(c) // toLeaver
                            && BR_G71(c) // isReblay
                            && BR_G72(c) // signature
                            && BR_G108(c); // membership period
                });

    }

    default boolean globalCheckIINDEX() {
        return IndexI.stream()//
                .allMatch(i -> BR_G63(i) // identity writability
                        && BR_G73(i) //
                        && BR_G74(i) //
                        && BR_G85(i) //
                ) && BR_G86();
    }

    default boolean globalCheckMINDEX() {
        return IndexM.stream().allMatch(m -> {
            System.out.println("globalCheckMINDEX " + BR_G64(m) + " " + BR_G75(m) + " " + BR_G76(m) + " " + BR_G77(m)
                    + " " + BR_G78(m) + " " + BR_G79(m) + " " + BR_G80(m) + " " + BR_G81(m) + " " + BR_G82(m) + " "
                    + BR_G83(m) + " " + BR_G84(m) + " ");

            return BR_G64(m) //
                    && BR_G75(m) //
                    && BR_G76(m) //
                    && BR_G77(m) //
                    && BR_G78(m) //
                    && BR_G79(m) //
                    && BR_G80(m) //
                    && BR_G81(m) //
                    && BR_G82(m) //
                    && BR_G83(m) //
                    && BR_G84(m) //

                    ;
        });
    }

    default boolean globalCheckSINDEX() {
        return IndexS.stream().allMatch(tx -> {

            //			System.out.println("globalCheckSINDEX : " + tx + " " + tx.base + " " + tx.op //
            //					+ "\n" + BR_G103(tx) + BR_G87(tx) + " " + BR_G88(tx) + " " + BR_G89(tx) + " " + BR_G90(tx) //
            //			);

            return BR_G103(tx) && BR_G87(tx) && BR_G88(tx) && BR_G89(tx) && BR_G90(tx);

        });
    }

    default BINDEX head() {
        System.err.println(" head() " + IndexB.get(IndexB.size() - 1));
        return IndexB.get(IndexB.size() - 1);
    }

    /**
     * <pre>
     *
     *
     *
     * Each exclusion produces 1 new entry:
     * IINDEX (
     * op = 'UPDATE'
     * uid = null
     * pub = PUBLIC_KEY
     * created_on = null
     * written_on = BLOCKSTAMP
     * member = false
     * wasMember = null
     * kick = false
     * )
     *
     *
     * Each revocation produces 1 new entry:
     *
     * MINDEX (
     * op = 'UPDATE'
     * pub = PUBLIC_KEY
     * created_on = BLOCK_UID
     * written_on = BLOCKSTAMP
     * type = 'REV'
     * expires_on = null
     * revokes_on = null
     * revoked_on = BLOCKSTAMP
     * revocation = REVOCATION_SIG
     * leaving = false
     * )
     *
     *
     *
     * Leavers
     *
     * Each leaver produces 1 new entry:
     *
     * MINDEX (
     * op = 'UPDATE'
     * pub = PUBLIC_KEY
     * created_on = BLOCK_UID
     * written_on = BLOCKSTAMP
     * type = 'LEAVE'
     * expires_on = null
     * revokes_on = null
     * revoked_on = null
     * leaving = true
     * )
     *
     *
     * Sources
     *
     * Each transaction input produces 1 new entry:
     *
     * SINDEX (
     * op = 'UPDATE'
     * tx = TRANSACTION_HASH
     * identifier = INPUT_IDENTIFIER
     * pos = INPUT_INDEX
     * created_on = TX_BLOCKSTAMP
     * written_on = BLOCKSTAMP
     * amount = INPUT_AMOUNT
     * base = INPUT_BASE
     * conditions = null
     * consumed = true
     * )
     * Each transaction output produces 1 new entry:
     *
     * SINDEX (
     * op = 'CREATE'
     * tx = TRANSACTION_HASH
     * identifier = TRANSACTION_HASH
     * pos = OUTPUT_INDEX_IN_TRANSACTION
     * written_on = BLOCKSTAMP
     * written_time = MedianTime
     * amount = OUTPUT_AMOUNT
     * base = OUTPUT_BASE
     * locktime = LOCKTIME
     * conditions = OUTPUT_CONDITIONS
     * consumed = false
     * )
     *
     * Each active produces 1 new entry:
     *
     * MINDEX (
     * op = 'UPDATE'
     * pub = PUBLIC_KEY
     * created_on = BLOCK_UID
     * written_on = BLOCKSTAMP
     * expires_on = MedianTime + msValidity
     * revokes_on = MedianTime + msValidity*2
     * chainable_on = MedianTime + msPeriod
     * type = 'RENEW'
     * revoked_on = null
     * leaving = null
     * )
     *
     * </pre>
     *
     * @param block:
     * @return false
     */
    default boolean indexBlock(Block block) {

        final var bstamp = new BStamp(block.getNumber(), block.getHash());
        final var medianTime = block.getMedianTime();

        if (block.getNumber().equals(0)) {

            blockChainParams.accept(block.getParameters());
        }

        block.getExcluded().forEach(excluded ->
                IndexI.add(new IINDEX("UPDATE",
                        null,
                        excluded.getExcluded(),
                        null, bstamp,
                        false,
                        null,
                        false))
        );

        block.getRevoked().forEach(revoked ->
            IndexM.add(new MINDEX("UPDATE",
                    revoked.revoked(),
                    bstamp,
                    bstamp,
                    "REV", //
                    null, 
                    null, 
                    block.getMedianTime(),
                    "",
                    false,
                    null))
        );

        block.getLeavers().forEach(leaver ->
            IndexM.add(
                    new MINDEX("UPDATE",
                            leaver.leaver(),
                            bstamp, bstamp,
                            "LEAVE",
                            null,
                            null,
                            null,
                            null,
                            true,
                            null))
        );

        block.getActives().forEach(active ->
            IndexM.add(new MINDEX("UPDATE",
                    active.activepk(),
                    bstamp,
                    bstamp, 
                    "RENEW", //
                    medianTime + blockChainParams.msValidity,
                    medianTime + blockChainParams.msValidity * 2,
                    null, // revoked_on
                    null, // revocation
                    false, // leaving
                    medianTime + blockChainParams.msWindow // chainable on
            ))
        );

        //		System.out.println(block.getJoiners().size() + " IINDEX and MINDEX going to be added  Joiners");

        block.getJoiners().forEach(joiner -> {

            IndexI.add(new IINDEX("UPDATE", //
                    joiner.pseudo(), //
                    joiner.pub(), //
                    new BStamp(joiner.createdOn().getNumber(), joiner.createdOn().getHash()), //
                    new BStamp(joiner.writtenOn().getNumber(), joiner.writtenOn().getHash()), //
                    true, //
                    true, //
                    false));
            IndexM.add(new MINDEX("UPDATE", //
                    joiner.pub(), //
                    new BStamp(joiner.createdOn().getNumber(), joiner.createdOn().getHash()), //
                    new BStamp(joiner.writtenOn().getNumber(), joiner.writtenOn().getHash()), //
                    "JOIN", //
                    block.getMedianTime() + blockChainParams.msValidity, // expires_on
                    block.getMedianTime() + blockChainParams.msValidity * 2, // revokes_on
                    null, // revoked_on
                    null, // revoke signature
                    false, // leaving
                    block.getMedianTime() + conf().msPeriod) // chainable_on
            );

        });
        //		System.out.println(IndexI.size() + " IINDEX total ");
        //		System.out.println(IndexM.size() + " MINDEX total ");
        //		System.out.println(block.getIdentities().size() + " IINDEX and MINDEX going to be added ");
        block.getIdentities().forEach(idty -> {

            final var created_on = new BStamp(idty.createdOn().getNumber(), idty.createdOn().getHash());

            //			System.out.println("    " + idty);
            IndexI.add(new IINDEX("UPDATE",
                    idty.pseudo(),
                    idty.pub(), //
                    created_on, //
                    created_on, //

                    true, true, false));
            IndexM.add(new MINDEX("UPDATE", //
                    idty.pub(), //
                    created_on, //
                    created_on, //
                    "JOIN", //
                    block.getMedianTime() + blockChainParams.msValidity, // expires_on
                    block.getMedianTime() + blockChainParams.msValidity * 2, // revokes_on
                    null, // revoked_on
                    null, // revoke signature
                    false, // leaving
                    block.getMedianTime() + conf().msPeriod) // chainable_on
            );
        });
        //

        //		System.out.println(block.getCertifications().size() + " CINDEX going to be added ");

        /*
         * Each certification produces 1 new entry:
         *
         * <pre>
         CINDEX (
         op = 'CREATE'
         issuer = PUBKEY_FROM
         receiver = PUBKEY_TO
         created_on = BLOCK_ID
         written_on = BLOCKSTAMP
         sig = SIGNATURE
         expires_on = MedianTime + sigValidity
         chainable_on = MedianTime + sigPeriod
         expired_on = 0
         )
         * </pre>
         */
        block.getCertifications().forEach(cert -> {
            IndexC.add(new CINDEX("CREATE", //
                    cert.getCertifier() + "", // issuer
                    cert.getCertified() + "", // receiver
                    bstamp, // created_on
                    bstamp, // written_on
                    block.getSignature() + "", //
                    block.getMedianTime() + blockChainParams.sigValidity, // expires_on
                    block.getMedianTime() + blockChainParams.sigPeriod, // chainable_on
                    0L // expired_on
            ));
        });

        block.getTransactions().forEach(tx -> {

            final var created_on = new BStamp(tx.getBlockstamp().getNumber(), tx.getBlockstamp().getHash());

            tx.getInputs().forEach(input -> {
                IndexS.add(new SINDEX("UPDATE", // op
                        input.getTHash() + "", // identifier
                        tx.getBlockstampTime(), // pos
                        created_on, // created_on
                        created_on, // written_on
                        block.getMedianTime(), // written_time
                        input.getAmount(), // amout
                        input.getBase(), // base
                        (long) tx.getLocktime(), // locktime
                        null, // condition
                        true // consumed
                ));
            });

            /*
             * SINDEX ( op = 'CREATE' tx = TRANSACTION_HASH identifier = TRANSACTION_HASH
             * pos = OUTPUT_INDEX_IN_TRANSACTION written_on = BLOCKSTAMP written_time =
             * MedianTime amount = OUTPUT_AMOUNT base = OUTPUT_BASE locktime = LOCKTIME
             * conditions = OUTPUT_CONDITIONS consumed = false )
             */
            tx.getOutputs().forEach(output -> {

                //				System.out.println("output.base " + output.getBase());

                IndexS.add(new SINDEX("CREATE", // op
                        tx.getThash(), // identifier
                        tx.getBlockstampTime(), // pos
                        null, //
                        bstamp, // written_on
                        block.getMedianTime(), // written_time
                        output.getAmount(), // amount
                        output.getBase(), // unitbase
                        (long) tx.getLocktime(), // locktime
                        "", // conditions
                        false // consumed
                ));
            });

        });

        return false;
    }

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return CINDEX
     */
    Stream<CINDEX> indexCGlobal();

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return IINDEX
     */
    Stream<IINDEX> indexIGlobal();

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return MINDEX
     */
    Stream<MINDEX> indexMGlobal();

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return SINDEX
     */
    Stream<SINDEX> indexSGlobal();

    default void initLocalIndex() {
        IndexC.clear();
        IndexM.clear();
        IndexI.clear();
        IndexS.clear();
    }

    /**
     * Used by BR_G46,BR_G47,BR_G48
     * <p>
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY: INPUT_ENTRIES =
     * LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
     * <p>
     * If COUNT(INPUT_ENTRIES) == 0 INPUT_ENTRIES =
     * GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
     *
     * @param entry:
     * @return TX inputs, either local or global
     */
    default List<SINDEX> inputEntries(SINDEX entry) {
        var inputEntries = IndexS.stream()//
                .filter(s -> {
                    System.out.println(s);
                    return s.op.equals("CREATE") && s.identifier.equals(entry.identifier) && s.pos.equals(entry.pos)
                            && s.amount == entry.amount && s.base == entry.base;
                })

                .collect(Collectors.toList());

        if (inputEntries.size() == 0) {
            inputEntries = indexSGlobal()//
                    .filter(s -> s.identifier.equals(entry.identifier) && s.pos.equals(entry.pos)
                            && s.amount == entry.amount && s.base == entry.base)
                    .collect(Collectors.toList());
        }
        return inputEntries;
    }

    default BINDEX prevHead() {
        return IndexB.size() > 0 ? IndexB.get(IndexB.size() - 1) : null;
    }

    /**
     * fetches few last block index
     *
     * @param m _
     * @return _
     */
    default Stream<BINDEX> range(long m) {
        final var bheads = IndexB.stream()//
                .filter(h -> h.number >= prevHead().number - m + 1)//
                .sorted((b1, b2) -> b2.number.compareTo(b1.number))//
                .collect(Collectors.toList());

        //		bheads.forEach(b -> {
        //			System.out.println(" #B " + IndexB.size() + " range " + m + " : " + b);
        //		});

        return bheads.stream();
    }

    // FIXME complete
    default boolean validate(Block block) {
        initLocalIndex();

        final var newHead = new BINDEX();

        newHead.version = (int) block.getVersion();
        newHead.size = block.size();
        newHead.hash = block.getHash();
        newHead.issuer = block.getIssuer();
        newHead.time = block.getTime();
        newHead.powMin = block.getPowMin();

        indexBlock(block);
        //		System.out.println("ran indexblock ");

        augmentLocalBINDEX(newHead);

        newHead.currency = block.getCurrency(); // because BR_G99 set it to null

        augmentLocalCINDEX(newHead);
        //		System.out.println("ran CINDEX augmentation " + newHead);

        augmentLocalIINDEX(newHead);
        //		System.out.println("ran IINDEX augmentation " + newHead);

        augmentLocalMINDEX(newHead);
        //		System.out.println("ran MINDEX augmentation " + newHead);

        augmentLocalSINDEX(newHead);
        //		System.out.println("ran SINDEX augmentation " + newHead);

        augmentLocal(newHead, block);
        augmentGlobal(newHead, block);
        //		System.out.println("ran augmentation");

        final boolean commit = BR_G97(newHead, block, false);

        return commit;
    }

}
