package juniter.core.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import juniter.core.crypto.Crypto;
import juniter.core.model.BStamp;
import juniter.core.model.Block;
import juniter.core.model.tx.TxType;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import com.google.common.collect.Lists;

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
 *    * HEAD~n[field=value, ...]: the BINDEX entry at HEAD~n if it fulfills the conditions, null otherwise
 *    * HEAD~n..m[field=value, ...]: the BINDEX entries between HEAD~n and HEAD~m, included, where each entry fulfills the conditions
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
 * 	  * FIRST return the first element in a list of values matching the given conditions
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

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @EqualsAndHashCode
    @JsonIgnoreProperties(ignoreUnknown = true)
    class BINDEX implements Comparable<BINDEX> {

        Integer version;
        Integer size;
        String hash;
        String issuer;
        Long time;
        public int number;
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
            return Integer.compare(number, o.number);
        }


        BStamp bstamp() {
            return new BStamp(number, hash);
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
        public long sigPeriod = 432000; // 5 days signature period
        long sigStock = 100;
        long sigWindow = 5259600; // 60 days, 21 hours
        public long sigValidity = 63115200; // 730 days, 12 hours,
        long sigQty = 5;
        long idtyWindow = 5259600;
        long msWindow = 5259600;
        double xpercent = 0.8;
        public long msValidity = 31557600; // 365 days, 6 hours,
        long stepMax = 5;
        long medianTimeBlocks = 24;
        long avgGenTime = 300;
        long dtDiffEval = 12;
        double percentRot = 0.67;
        long udTime0 = 1488970800; // GMT: Wednesday, March 8, 2017 11:00:00 AM
        long udReevalTime0 = 1490094000; // GMT: Tuesday, March 21, 2017 11:00:00 AM ??
        long dtReeval = 15778800; // 182 days, 15 hours

        // New parameter, defaults to msWindow
        public long msPeriod = msWindow;
        long sigReplay = msWindow;

        /**
         * c:dt:ud0:sigPeriod:sigStock:sigWindow:sigValidity:sigQty:idtyWindow:msWindow:xpercent:msValidity:
         * stepMax:medianTimeBlocks:avgGenTime:dtDiffEval:percentRot:udTime0:udReevalTime0:dtReeval
         * <p>
         * 0.0488:86400:1000:432000:100:5259600:63115200:5:5259600:5259600:0.8:31557600:5:24:300:12:0.67:1488970800:1490094000:15778800
         * 0.007376575:3600:1200:0:40:604800:31536000:1:604800:604800:0.9:31536000:3:20:960:10:0.6666666666666666:1489675722:1489675722:3600",
         *
         * @param DUPParams:
         */
        void accept(String DUPParams) {
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
            // New parameter, defaults to msWindow
            msPeriod = msWindow;
            sigReplay = msWindow;
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
    @NoArgsConstructor
    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    class CINDEX implements Comparable<CINDEX> {

        Integer createdOn;
        String op;
        String issuer;
        String receiver;
        BStamp written_on;
        String sig;
        Long expires_on;
        long expired_on;
        Long chainable_on;
        String from_wid;
        String to_wid;
        int writtenOn;

        // transient variable
        long unchainables;
        long age;
        long stock;
        boolean toMember;
        boolean toNewcomer;
        boolean toLeaver;
        boolean isReplay;
        boolean sigOK;
        boolean fromMember;
        Block created_on;


        CINDEX(String op, String issuer, String receiver, Integer createdOn, BStamp written_on, String sig,
               long expires_on, long chainable_on, Long expired_on) {

            this.op = op;
            this.issuer = issuer;
            this.receiver = receiver;
            this.createdOn = createdOn;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
            this.sig = sig;
            this.expires_on = expires_on;
            this.chainable_on = chainable_on;
            this.expired_on = expired_on;
        }

        public CINDEX putCreatedOn(Block b) {
            created_on = b;
            return this;
        }

        CINDEX(String op, String issuer, String receiver, Integer created_on, Long expired_on) {

            this.op = op;
            this.issuer = issuer;
            this.receiver = receiver;
            this.createdOn = created_on;
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
        int NB_DIGITS_UD = 4;
        long txWindow = 604800; // 3600 * 24 * 7
        long forksize = 100;

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
    @NoArgsConstructor
    @EqualsAndHashCode
    @Setter
    @Getter
            //@JsonIgnoreProperties(ignoreUnknown = true)
    class IINDEX implements Comparable<IINDEX> {

        String op;
        String uid;
        String pub;
        String hash;
        String sig;
        BStamp created_on;
        BStamp written_on;
        Boolean member;
        boolean wasMember;
        boolean kick;
        int wotbid;
        int writtenOn;

        // transient variable
        transient long age;
        transient boolean uidUnique;
        transient boolean pubUnique;
        transient boolean excludedIsMember;
        transient boolean isBeingKicked;
        transient boolean hasToBeExcluded;
        transient boolean leaving;

        IINDEX(String op, String pub, BStamp written_on, boolean kick) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
            this.kick = kick;
        }

        IINDEX(String op, String uid, String pub, BStamp created_on, BStamp written_on, boolean member,
               boolean wasMember, boolean kick, String sig) {
            this.op = op;
            this.uid = uid;
            this.pub = pub;
            this.created_on = created_on;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
            this.member = member;
            this.wasMember = wasMember;
            this.kick = kick;
            this.sig = sig;
            this.hash = Crypto.hash(uid + created_on + pub);
            this.wotbid = wotbidIncrement.getAndIncrement();
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
     * * MembershipDTO unicity
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
    @NoArgsConstructor
    @EqualsAndHashCode
    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    class MINDEX implements Comparable<MINDEX> {

        String op;

        String pub;
        BStamp created_on;
        BStamp written_on;
        Long expires_on;
        Long expired_on;
        Long revokes_on;
        Long revoked_on;
        boolean leaving;
        String revocation;
        Long chainable_on;
        int writtenOn;
        String type;

        // transient variable
        long age;
        boolean numberFollowing;
        boolean distanceOK;
        boolean onRevoked;
        boolean joinsTwice;
        boolean enoughCerts;
        boolean leaverIsMember;
        boolean activeIsMember;
        boolean revokedIsMember;
        boolean alreadyRevoked;
        boolean revocationSigOK;
        boolean excludedIsMember;
        boolean isBeingRevoked;
        long unchainables;


        MINDEX(String op, String pub, BStamp created_on, BStamp written_on, String type, Long expires_on,
               Long revokes_on, Long revoked_on, String revocation_sig, boolean leaving, Long chainable_on) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
            this.revoked_on = revoked_on;
            this.created_on = created_on;
            this.type = type;
            this.expires_on = expires_on;
            this.revokes_on = revokes_on;
            this.revocation = revocation_sig;
            this.leaving = leaving;
            this.chainable_on = chainable_on;
        }

        MINDEX(String op, String pub, BStamp written_on, Long revoked_on) {
            this.op = op;
            this.pub = pub;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
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


    default Optional<BINDEX> createdOnRef(BStamp bstamp) {
        return IndexB.stream().filter(b -> b.bstamp().equals(bstamp)).findAny();
    }


    Optional<Block> createdOnBlock(BStamp bstamp);

    Optional<Block> createdOnBlock(Integer number);

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
     * a same source cannot be consumed twice by the block a same output cannot be
     * produced twice by block
     *
     *
     * But a source can be both created and consumed in the same block, so a tree
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
    @NoArgsConstructor
    @ToString
    @Setter
    @Getter
    class SINDEX implements Comparable<SINDEX>, Serializable {

        private static final long serialVersionUID = -6400219111111110671L;


        Long id;
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
        String conditions;
        Integer writtenOn;

        // transient
        long age;
        boolean available;
        boolean isLocked;
        boolean isTimeLocked;

        public void setCreated_on(String created_on) {
            this.created_on = new BStamp(created_on);
        }

        public void setWritten_on(String written_on) {
            this.written_on = new BStamp(written_on);
        }


        SINDEX(String op, String identifier, Integer pos, BStamp created_on, BStamp written_on,
               long written_time, int amount, int base, Long locktime, String conditions, boolean consumed, String tx) {
            this.op = op;
            this.identifier = identifier;
            this.pos = pos;
            this.created_on = created_on;
            this.written_on = written_on;
            this.writtenOn = written_on.getNumber();
            this.written_time = written_time;
            this.amount = amount;
            this.base = base;
            this.locktime = locktime;
            this.conditions = conditions;
            this.consumed = consumed;
            this.tx = tx;
        }

        @Override
        public int compareTo(SINDEX o) {
            return (op + "-" + identifier + "-" + pos + "-" + written_on)
                    .compareTo(o.op + "-" + o.identifier + "-" + o.pos + "-" + o.written_on);
        }

        /**
         * Be aware, that function is more essential than it looks, keep it public
         *
         * @return conditions
         */
        public String getConditions() {
            return conditions;
        }


    }

    List<BINDEX> IndexB = new ArrayList<>();

    Set<CINDEX> localC = new TreeSet<>();
    List<CINDEX> consumedC = Lists.newArrayList();

    Set<IINDEX> localI = new HashSet<>();
    List<IINDEX> consumedI = Lists.newArrayList();

    Set<MINDEX> localM = new TreeSet<>();
    List<MINDEX> consumedM = Lists.newArrayList();

    Set<SINDEX> localS = new TreeSet<>();
    List<SINDEX> consumedS = Lists.newArrayList();

    AtomicInteger wotbidIncrement = new AtomicInteger(0);
    ChainParameters blockChainParams = new ChainParameters();


    default Long bIndexSize() {
        BINDEX tail = IndexB.stream().min(Comparator.comparingInt(b -> b.number)).orElseThrow();
        BINDEX head = IndexB.stream().max(Comparator.comparingInt(b -> b.number)).orElseThrow();

        var res = Stream.of(
                blockChainParams.medianTimeBlocks,
                blockChainParams.dtDiffEval,
                tail.issuersCount.longValue(),
                tail.issuersFrame.longValue())
                .max(Comparator.naturalOrder())
                .get();

        //System.out.println("At block  " + head.number + " bIndexSize: max(" + tail.issuersCount + ", " + tail.issuersFrame + ", " + 24 + ") => size : " + res);

        return res + conf().forksize;
    }

    default Long trimIndexes() {
        Long bIndexSize = bIndexSize();
        if (bIndexSize < IndexB.size())
            IndexB.removeIf(b -> b.number <= head().get().number - bIndexSize);
        return bIndexSize;
    }


    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        final Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }



    default void augmentLocal(BINDEX head, Block block) {
        final var bstamp = new BStamp(block.getNumber(), block.getHash());

        BR_G94_ExclusionByMembership(bstamp);
        BR_G95_ExclusionByCertification(bstamp);

    }




    /**
     * <pre>
     * BR_G01_setNumber - HEAD.number
     *
     * If HEAD~1 is defined
     *     HEAD.number = HEAD~1.number + 1
     * Else
     *     HEAD.number = 0
     * </pre>
     */
    private void BR_G01_setNumber(BINDEX head) {
        head.number = head_1() == null ? 0 : (head_1().number + 1);
        // System.out.println("Set number " + head.number + " " + head_1());
    }

    /**
     * <pre>
     * BR_G02_setPreviousHash - HEAD.previousHash
     *
     * If HEAD.number > 0
     *     HEAD.previousHash = HEAD~1.hash
     * Else:
     *     HEAD.previousHash = null
     *
     * </pre>
     */
    private void BR_G02_setPreviousHash(BINDEX head) {

        assert head != null : "BR_G02_setPreviousHash - set previousHash - BINDEX null";

        if (head.number > 0) {
            head.previousHash = head_1().hash;
            assert head.previousHash != null : "BR_G02_setPreviousHash - set previousHash - Previous Hash NULL for " + head;
        } else {
            head.previousHash = null;
        }
    }

    /**
     * <pre>
     * BR_G03_setPreviousIssuer - HEAD.previousIssuer
     *
     * If HEAD.number > 0
     *     HEAD.previousIssuer = HEAD~1.issuer
     * Else:
     *     HEAD.previousIssuer = null
     * </pre>
     */
    private void BR_G03_setPreviousIssuer(BINDEX head) {
        if (head.number > 0) {
            head.previousIssuer = head_1().issuer;
            assert head.previousIssuer != null : "BR_G03_setPreviousIssuer - set previousIssuer - Previous Hash null";
        } else {
            head.previousIssuer = null;
        }
    }

    /**
     * <pre>
     * BR_G04_setIssuersCount - HEAD.issuersCount
     *
     * If HEAD.number == 0
     *     HEAD.issuersCount = 0
     * Else
     *     HEAD.issuersCount = COUNT(UNIQ((HEAD~1..<HEAD~1.issuersFrame>).issuer))
     * </pre>
     */
    private void BR_G04_setIssuersCount(BINDEX head) {
        if (head.number == 0) {
            head.issuersCount = 0;
        } else {
            head.issuersCount = (int) range(head_1().issuersFrame).map(b -> b.issuer).distinct().count();
            assert head.issuersCount > 0 : "BR_G04_setIssuersCount - set issuersCount - issuersCount !> 0 ";
        }
    }

    /**
     * <pre>
     * BR_G05_setIssuersFrame - HEAD.issuersFrame
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
    private void BR_G05_setIssuersFrame(BINDEX head) {
        if (head.number == 0) {
            head.issuersFrame = 1;
        } else if (head_1().issuersFrameVar > 0) {
            head.issuersFrame = head_1().issuersFrame + 1;
        } else if (head_1().issuersFrameVar < 0) {
            head.issuersFrame = head_1().issuersFrame - 1;
        } else {
            head.issuersFrame = head_1().issuersFrame;
        }
        assert head.issuersFrame != null : "BR_G05_setIssuersFrame - set issuersFrame - issuersFrame is null";
    }

    /**
     * <pre>
     * BR_G06_setIssuersFrameVar - HEAD.issuersFrameVar
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
    private void BR_G06_setIssuersFrameVar(BINDEX head) {
        if (head.number == 0) {
            head.issuersFrameVar = 0;
        } else {
            final var delta = head.issuersCount - head_1().issuersCount;

            if (head_1().issuersFrameVar > 0) {
                head.issuersFrameVar = head_1().issuersFrameVar + 5 * delta - 1;
            } else if (head_1().issuersFrameVar < 0) {
                head.issuersFrameVar = head_1().issuersFrameVar + 5 * delta + 1;
            } else {
                head.issuersFrameVar = head_1().issuersFrameVar + 5 * delta;
            }
        }

    }

    /**
     * <pre>
     * BR_G07_setAvgBlockSize - HEAD.avgBlockSize
     *
     * HEAD.avgBlockSize = AVG((HEAD~1..<HEAD.issuersCount>).size)
     * </pre>
     */
    private void BR_G07_setAvgBlockSize(BINDEX head) {
        assert head != null : "BR_G07_setAvgBlockSize - HEAD.avgBlockSize - head is null  ";

        if (head.number == 0) {
            head.avgBlockSize = 0;
        } else {
            assert head.issuersCount > 0 : "BR_G07_setAvgBlockSize - HEAD.avgBlockSize - issuersCount 0";
            head.avgBlockSize = (int) range(head.issuersCount)
                    .mapToInt(h -> h.size)
                    .average()
                    .orElse(0.0);
        }
    }

    /**
     * <pre>
     * BR_G08_setMedianTime - HEAD.medianTime
     *
     * If HEAD.number > 0
     *     HEAD.medianTime = MEDIAN((HEAD~1..<MIN(medianTimeBlocks, HEAD.number)>).time)
     * Else
     *     HEAD.medianTime = HEAD.time
     * </pre>
     */
    private void BR_G08_setMedianTime(BINDEX head) {
        final var min = Math.min(blockChainParams.medianTimeBlocks, head.number);

        //		System.out.println(
        //				"BR_G08_setMedianTime : set medianTime " + min + " " + blockChainParams.medianTimeBlocks + " " + head.number);
        if (head.number > 0) {

            head.medianTime = (long) range(min) // fetch bindices
                    .mapToLong(h -> h.time)
                    .average().orElse(Double.NaN);
            //			System.out.println("BR_G08_setMedianTime : ==> max  " + head.medianTime + " " + head_1().medianTime);
            head.medianTime = Math.max(head.medianTime, head_1().medianTime); // FIXME found in code, not in the spec
            // used at block 3
        } else {

            head.medianTime = head.time;
        }

    }

    /**
     * <pre>
     * BR_G09_setDiffNumber - HEAD.diffNumber
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
    private void BR_G09_setDiffNumber(BINDEX head) {

        if (head.number == 0) {
            head.diffTime = head.number + blockChainParams.dtDiffEval;

        } else if (head_1().diffNumber <= head.number) {
            head.diffTime = head_1().diffTime + blockChainParams.dtDiffEval;

        } else {
            head.diffTime = head_1().time;
        }
    }

    /**
     * <pre>
     * BR_G10_setMembersCount
     *
     * If HEAD.number == 0
     *     HEAD.membersCount = COUNT(LOCAL_IINDEX[member=true])
     * Else:
     *     HEAD.membersCount = HEAD~1.membersCount + COUNT(LOCAL_IINDEX[member=true]) - COUNT(LOCAL_IINDEX[member=false])
     * </pre>
     * <p>
     * Rather than using two counts use a single reduction
     */
    private void BR_G10_setMembersCount(BINDEX head) {
        final int member = (int) localI.stream().filter(m -> "CREATE".equals(m.op) && m.member).count();
        final int notMember = (int) localI.stream().filter(m -> !m.member).count();

        //		System.out.println("BR_G10_setMembersCount " + localI.size() + " " + member + " " + notMember);
        if (head.number == 0) {
            head.membersCount = member;
        } else {
            head.membersCount = head_1().membersCount + member - notMember;
        }

        assert head.membersCount > 0 : "BR_G10_setMembersCount  set membersCount - " + head.membersCount;

    }

    /**
     * <pre>
     * BR_G100_issuerIsMember - HEAD.issuerIsMember
     *
     * If HEAD.number > 0
     *     HEAD.issuerIsMember = REDUCE(GLOBAL_IINDEX[pub=HEAD.issuer]).member
     * Else
     *     HEAD.issuerIsMember = REDUCE(LOCAL_IINDEX[pub=HEAD.issuer]).member
     * </pre>
     */
    private void BR_G100_issuerIsMember(BINDEX head) {
        if (head.number == 0) {
            head.issuerIsMember = localI.stream()
                    .anyMatch(i -> head.issuer.equals(i.pub) && i.member);

        } else {
            head.issuerIsMember = reduceI(head.issuer).anyMatch(i -> i.member);
        }
    }

    /**
     * <pre>
     * BR_G101_IssuerIsMember - Issuer
     *
     * Rule:
     *
     * HEAD.issuerIsMember == true
     *
     * </pre>
     */
    private boolean BR_G101_IssuerIsMember(BINDEX head) {
        return head.issuerIsMember;
    }

    /**
     * <pre>
     * BR_G102_setAge - ENTRY.age
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
    private void BR_G102_setAge(SINDEX entry, BINDEX head) {
        //		System.out.println("BR_G102_setAge - set Age " + entry.created_on + " " + entry.op + " " + entry.amount);
        if (head.number == 0
                && "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855".equals(entry.created_on + "")) {
            entry.age = 0;
        } else {

            entry.age = createdOnBlock(entry.created_on)
                    .map(block -> head_1().medianTime - block.getMedianTime())
                    .orElseGet(() -> conf().txWindow + 1);

        }

    }

    /**
     * <pre>
     * BR_G103_TransactionWritable
     *
     * Rule:
     *
     * ENTRY.age <= [txWindow]
     */
    private boolean BR_G103_TransactionWritable(SINDEX s) {
        assert s.age <= conf().txWindow : "BR_G103_TransactionWritable -  TransactionDTO writability - " + s.age + " " + conf().txWindow;
        return s.age <= conf().txWindow;
    }

    /**
     * <pre>
     * BR_G104_MembershipExpiryDateCorrection
     *
     * FIXME : I think spec is incomplete here, huh duniter's not just a theory its running code so maybe code can tell us
     *
     * For each LOCAL_MINDEX[type='JOIN'] as MS:
     *     MS.expires_on = MS.expires_on - MS.age
     *     MS.revokes_on = MS.revokes_on - MS.age
     *
     * For each LOCAL_MINDEX[type='RENEW'] as MS:
     *     MS.expires_on = MS.expires_on - MS.age
     *     MS.revokes_on = MS.revokes_on - MS.age
     * </pre>
     */
    private void BR_G104_MembershipExpiryDateCorrection(MINDEX ms) {
        if ("JOINN".equals(ms.type) || "RENEWW".equals(ms.type)) {
            ms.expires_on = ms.expires_on - ms.age;
            ms.revokes_on = ms.revokes_on - ms.age;
        }

    }

    /**
     * <pre>
     * BR_G105_CertificationExpiryDateCorrection
     *
     * For each LOCAL_CINDEX as CERT:
     *
     * CERT.expires_on = CERT.expires_on - CERT.age
     */
    private void BR_G105_CertificationExpiryDateCorrection(CINDEX c) {
        // c.expires_on = c.expires_on - c.age;

    }

    /**
     * <pre>
     * BR_G106_LowAccounts - Low accounts
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
    private void BR_G106_LowAccounts(BINDEX head, BStamp block) {
        if (head.number >= 0)
            return; // FIXME

        indexSGlobal().filter(distinctByKey(SINDEX::getConditions)).forEach(account -> {
            final var sources = Stream
                    .concat(reduceS(account.conditions), localS.stream().filter(s -> s.conditions.equals(account.conditions)))
                    .filter(s -> !s.consumed)
                    .peek(consumedS::add)
                    .peek(s -> System.out.println(" BR_G106_LowAccounts  " + s))
                    .collect(Collectors.toList());

            final var balance = sources.stream()
                    .map(src -> src.amount * Math.pow(src.base, 10))
                    .reduce((bal1, bal2) -> bal1 + bal2)
                    .orElse(Double.MAX_VALUE);

            if (balance < 100 * Math.pow(head.unitBase, 10)) {

                //Integer pos, BStamp created_on, BStamp written_on,
                //                      long written_time, int amount, int base, Long locktime, String conditions, boolean consumed, String tx)
                sources.forEach(src -> {
                    localS.add(new SINDEX("UPDATE",
                            src.identifier,
                            src.pos,
                            null,
                            block,
                            0L,
                            1,
                            src.base,
                            0L,
                            "LOW_ACCOUNT" + "wxc" + ")",
                            true,
                            null
                    )); // TODO BLOCKSTAMP &
                });

            }
        });
    }

    /**
     * <pre>
     * BR_G107_unchainableM
     *
     * If HEAD.number > 0 AND ENTRY.revocation == null:
     *
     * ENTRY.unchainables = COUNT(GLOBAL_MINDEX[issuer=ENTRY.issuer, chainable_on >
     * HEAD~1.medianTime]))
     */
    private void BR_G107_unchainableM(MINDEX entry) {

        entry.unchainables = indexMGlobal()
                .filter(m -> m.chainable_on != null && m.chainable_on > head_1().medianTime)
                .count();
    }

    /**
     * <pre>
     * BR_G108_MembershipPeriod
     *
     * Rule:
     *
     * ENTRY.unchainables == 0
     */
    private boolean BR_G108_MembershipPeriod(CINDEX m) {
        return m.unchainables == 0;
    }

    /**
     * <pre>
     * BR_G11_setUdTime - HEAD.udTime and HEAD.udReevalTime
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
    private void BR_G11_setUdTime(BINDEX head) {

        if (head.number == 0) {
            head.udTime = blockChainParams.udTime0;
            head.udReevalTime = blockChainParams.udReevalTime0;
        } else {

            if (head_1().udTime <= head.medianTime) {
                head.udTime = head_1().udTime + blockChainParams.dt;
            } else {
                head.udTime = head_1().udTime;
            }

            if (head_1().udReevalTime <= head.medianTime) {
                head.udReevalTime = head_1().udReevalTime + blockChainParams.dtReeval;
            } else {
                head.udReevalTime = head_1().udReevalTime;

            }
        }

    }

    /**
     * <pre>
     * BR_G12_setUnitBase - HEAD.unitBase
     *
     * If HEAD.number == 0:
     *     HEAD.unitBase = 0
     * Else:
     *     HEAD.unitBase = HEAD~1.unitBase
     * </pre>
     */
    private void BR_G12_setUnitBase(BINDEX head) {
        if (head.number == 0) {
            head.unitBase = 0;
        } else {
            head.unitBase = head_1().unitBase;
        }
    }

    /**
     * <pre>
     * BR_G13_setDividend -  HEAD.new_dividend
     *
     * If HEAD.number == 0
     *     HEAD.dividend = ud0
     * Else If HEAD.udReevalTime != HEAD~1.udReevalTime
     *     HEAD.dividend = HEAD_1.dividend + c² * CEIL(HEAD~1.massReeval / POW(10, HEAD~1.unitbase)) / HEAD.membersCount)
     * Else
     *     HEAD.dividend = HEAD~1.dividend EndIf
     *
     *head
     * If HEAD.number == 0
     *     HEAD.new_dividend = null
     * Else If HEAD.udTime != HEAD~1.udTime
     *     HEAD.new_dividend = HEAD.dividend
     * Else
     *     HEAD.new_dividend = null
     * </pre>
     */
    private void BR_G13_setDividend(BINDEX head) {


        if (head.number == 0) {
            head.dividend = (int) blockChainParams.ud0;
        } else if (head.udReevalTime != head_1().udReevalTime) {

            var moneyShare = Math.ceil(head_1().massReeval / Math.pow(10, head_1().unitBase)) / head.membersCount;

            head.dividend = (int) Math.ceil(head_1().dividend + Math.pow(blockChainParams.c, 2) * moneyShare
                    / (blockChainParams.dtReeval * 1. / blockChainParams.dt) // FIXME add  "/(dtReeval/dt)" to spec  ?
            );

            System.out.println("head.dividend " + head.dividend + "from " + head_1().dividend + " + " + Math.pow(blockChainParams.c, 2) + " * " + head_1().massReeval + " / " + head.membersCount + " / " + (blockChainParams.dtReeval * 1. / blockChainParams.dt));
        } else {
            head.dividend = head_1().dividend;
        }

        if (head.number == 0) {
            head.new_dividend = null;
        } else if (!Objects.equals(head.udTime, head_1().udTime)) {
            head.new_dividend = head.dividend;
        } else {
            head.new_dividend = null;
        }

    }

    /**
     * <pre>
     * BR_G14_setUnitBase - HEAD.dividend and HEAD.unitbase and HEAD.new_dividend
     *
     * If HEAD.dividend >= 1000000
     *     HEAD.dividend = CEIL(HEAD.dividend / 10)
     *     HEAD.new_dividend = HEAD.dividend
     *     HEAD.unitBase = HEAD.unitBase + 1
     *
     * </pre>
     */
    private void BR_G14_setUnitBase(BINDEX head) {

        if (head.dividend >= Math.pow(10, conf().NB_DIGITS_UD)) {
            head.dividend = (int) Math.round(Math.ceil(1.0 * head.dividend / 10));
            head.new_dividend = head.dividend;
            head.unitBase = head.unitBase + 1;
        }
    }

    /**
     * <pre>
     * BR_G15_setMassAndMassReeval - HEAD.mass and HEAD.massReeval
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
    private void BR_G15_setMassAndMassReeval(BINDEX head) {
        if (head.number == 0) {
            head.mass = 0L;
            head.massReeval = 0L;
        } else {

            if (!head.udTime.equals(head_1().udTime)) {
                head.mass = (long) (head_1().mass + head.dividend * Math.pow(10, head.unitBase) * head.membersCount);
            } else {
                head.mass = head_1().mass;
            }

            if (head.udReevalTime != head_1().udReevalTime) {
                head.massReeval = head_1().mass;
            } else {
                head.massReeval = head_1().massReeval;
            }
        }

    }

    /**
     * <pre>
     * BR_G16_setSpeed - HEAD.speed
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
    private void BR_G16_setSpeed(BINDEX head) {
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
     * BR_G17_setPowMin
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
    private void BR_G17_setPowMin(BINDEX head) {
        if (head.number == 0)
            return;

        //		System.out.println("BR_G17_setPowMin - " + head.diffNumber + "!=" + head_1().diffNumber);
        //		System.out.println(head.speed + " >= " + blockChainParams.maxSpeed() + head_1().powMin);

        if (head.diffNumber != head_1().diffNumber) {

            if (head.speed >= blockChainParams.maxSpeed()) { // too fast, increase difficulty
                if ((head_1().powMin + 2) % 16 == 0) {
                    head.powMin = head_1().powMin + 2;
                } else {
                    head.powMin = head_1().powMin + 1;
                }
            }

            if (head.speed <= blockChainParams.minSpeed()) { // too slow, increase difficulty
                if (head_1().powMin % 16 == 0) {
                    head.powMin = Math.max(0, head_1().powMin - 2);
                } else {
                    head.powMin = Math.max(0, head_1().powMin - 1);
                }
            }
        }

    }

    /**
     * <pre>
     * BR_G18_setPowZero - and HEAD.powRemainder
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
    private void BR_G18_setPowZero(BINDEX head) {

        long nbPersonalBlocksInFrame; // FIXME quesaco ?
        final int medianOfBlocksInFrame = 1;
        int nbPreviousIssuers = 0;
        int nbBlocksSince = 0;

        //		Stream<BINDEX> blocksOfIssuer = null;
        if (head.number != 0) {
            final var blocksOfIssuer = range(head.issuersFrame)
                    .filter(i -> i.issuer.equals(head.issuer))
                    .collect(Collectors.toList());
            nbPersonalBlocksInFrame = blocksOfIssuer.size();
            final Object blocksPerIssuerInFrame = null;

            if (nbPersonalBlocksInFrame != 0) {
                final var first = blocksOfIssuer.get(0);
                nbPreviousIssuers = first.issuersCount;
                nbBlocksSince = head_1().number - first.number;
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
     * BR_G19_setAge
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
    private void BR_G19_setAge(BINDEX head, IINDEX entry) {

        if ("CREATE".equals(entry.op)) {
            if (head.number == 0 && "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855".equals(entry.created_on.toString())) {
                entry.age = 0;
            } else {

                entry.age = createdOnBlock(entry.created_on)
                        .map(block -> head.medianTime - block.getMedianTime())
                        .orElseGet(() -> blockChainParams.idtyWindow + 1);
            }
        }

    }

    /**
     * <pre>
     * BR_G20_UidUnicity - Identity UserID unicity
     *
     * For each ENTRY in local IINDEX:
     *
     *     If op = 'CREATE':
     *         ENTRY.uidUnique = COUNT(GLOBAL_IINDEX[uid=ENTRY.uid) == 0
     *     Else:
     *         ENTRY.uidUnique = true
     * </pre>
     */
    private void BR_G20_UidUnicity(IINDEX entry) {

        if ("CREATE".equals(entry.op)) {
            entry.uidUnique = idtyByUid(entry.uid).count() == 0;
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
    private void BR_G21_pubkeyUnicity(IINDEX entry) {
        if ("CREATE".equals(entry.op)) {
            entry.pubUnique = reduceI(entry.pub).count() == 0;
        } else {
            entry.pubUnique = true;
        }

    }

    /**
     * <pre>
     * BR_G22_setAge - prepare ENTRY.age
     *
     * For each ENTRY in local MINDEX where revoked_on == null
     *
     *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.created_on)>[hash=HASH(ENTRY.created_on)]
     *
     *     If HEAD.number == 0 && ENTRY.created_on == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
     *         ENTRY.age = 0
     *     Else if REF_BLOC != null
     *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
     *     Else:
     *         ENTRY.age = conf.msWindow + 1
     * </pre>
     */
    private void BR_G22_setAge(BINDEX head, MINDEX entry) {

        if (entry.revoked_on == null) {

            if (head.number == 0 && "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855".equals(entry.created_on.toString())) {
                entry.age = 0;
            } else {
                entry.age = createdOnBlock(entry.created_on)
                        .map(ref -> head_1().medianTime - ref.getMedianTime())
                        .orElse(blockChainParams.msWindow + 1);
            }
        }
    }

    /**
     * <pre>
     * BR_G23_numberFollowing
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
    private void BR_G23_numberFollowing(MINDEX entry) {
        if (entry.revoked_on == null) {
            entry.numberFollowing = reduceM(entry.pub)
                    .findAny() // suppose unicity, allow parallel
                    .map(m -> m.created_on)
                    .map(on -> entry.created_on.getNumber() > on.getNumber())
                    .orElse(true);

        } else { // if revoked_on exists
            entry.numberFollowing = true;
        }
    }

    /**
     * <pre>
     * BR_G24_distanceOK
     *
     * Functionally: checks if it exists, for at least xpercent% of the sentries, a
     * path using GLOBAL_CINDEX + LOCAL_CINDEX leading to the key PUBLIC_KEY with a
     * maximum count of [stepMax] hops.
     *
     * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'RENEW':
     *
     *     dSen = CEIL(HEAD.membersCount ^ (1 / stepMax))
     *     GRAPH = SET(LOCAL_CINDEX, 'issuer', 'receiver') + SET(GLOBAL_CINDEX, 'issuer', 'receiver')
     *     SENTRIES = SUBSET(GRAPH, dSen, 'issuer')
     *     ENTRY.distanceOK = EXISTS_PATH(xpercent, SENTRIES, GRAPH, ENTRY.pub, stepMax)
     *
     * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'RENEW')
     *     ENTRY.distanceOK = true
     * </pre>
     */
    private void BR_G24_distanceOK(BINDEX head, MINDEX entry) {
        if (entry.type.equals("JOIN") || entry.type.equals("RENEW")) {
            final var dSen = Math.ceil(Math.pow(head.membersCount, 1.0 / blockChainParams.stepMax));
            final var graph = Stream.concat(localC.stream(), indexCGlobal()).collect(Collectors.toList());

            final Set<CINDEX> sentries = new TreeSet<>();

            // This probably should be rewritten and trashed but lets measure it first
            final var reachedNode = graph.stream().filter(e -> entry.pub.equals(e.issuer))
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
     * BR_G25_onRevoked
     *
     * For each ENTRY in local MINDEX:
     *     ENTRY.onRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
     * </pre>
     */
    private void BR_G25_onRevoked(MINDEX entry) {
        entry.onRevoked = reduceM(entry.pub).anyMatch(m -> m.revoked_on != null);

    }

    /**
     * <pre>
     * BR_G26_joinsTwice
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
    private void BR_G26_joinsTwice(MINDEX entry) {
        if (entry.op.equals("UPDATE") && entry.expired_on != null && entry.expired_on.equals(0L)) {
            entry.joinsTwice = reduceI(entry.pub).anyMatch(i -> i.member);
        }
    }

    /**
     * <pre>
     * BR_G27_enoughCerts
     *
     * Functionally: any member or newcomer needs [sigQty] certifications coming to
     * him to be in the WoT
     *
     * For each ENTRY in local MINDEX where type == 'JOIN' OR type == 'RENEW'
     *     ENTRY.enoughCerts = COUNT(GLOBAL_CINDEX[receiver=ENTRY.pub,expired_on=null])
     *     					 + COUNT(LOCAL_CINDEX[receiver=ENTRY.pub,expired_on=null]) >= sigQty
     *
     * For each ENTRY in local MINDEX where !(type == 'JOIN' OR type == 'RENEW'):
     *     ENTRY.enoughCerts = true
     * </pre>
     *
     * @param entry .
     */
    private void BR_G27_enoughCerts(MINDEX entry) {

        if (entry.type.equals("JOIN") || entry.type.equals("RENEW")) {
            final var cntG = reduceC(null, entry.pub).filter(c -> c.expired_on == 0L).count();
            final var cntL = localC.stream().filter(c -> c.receiver.equals(entry.pub) && c.expired_on == 0).count();

            entry.enoughCerts = cntG + cntL >= blockChainParams.sigQty;
        } else {
            entry.enoughCerts = true;
        }

        assert entry.enoughCerts : "BR_G27_enoughCerts - not enough Certification for " + entry.pub;
    }

    /**
     * <pre>
     * BR_G28_leaverIsMember
     *
     * For each ENTRY in local MINDEX where type == 'LEAVE':
     *     ENTRY.leaverIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     *
     * For each ENTRY in local MINDEX where type != 'LEAVE':
     *     ENTRY.leaverIsMember = true
     * </pre>
     */
    private void BR_G28_leaverIsMember(MINDEX entry) {
        if (entry.type.equals("LEAVE")) {
            entry.leaverIsMember = reduceI(entry.pub).anyMatch(i -> i.member);
        } else {
            entry.leaverIsMember = true;
        }

    }

    /**
     * <pre>
     * BR_G29_activeIsMember
     *
     * For each ENTRY in local MINDEX where type == 'RENEW'
     *     ENTRY.activeIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     *
     * For each ENTRY in local MINDEX where type != 'RENEW':
     *     ENTRY.activeIsMember = true
     * </pre>
     */
    private void BR_G29_activeIsMember(MINDEX entry) {
        if (entry.type.equals("RENEW")) {
            entry.activeIsMember = reduceI(entry.pub).anyMatch(i -> i.member);
        } else {
            entry.activeIsMember = true;
        }

    }

    /**
     * <pre>
     * BR_G30_revokedIsMember
     *
     * For each ENTRY in local MINDEX where revoked_on == null:
     *     ENTRY.revokedIsMember = true
     *
     * For each ENTRY in local MINDEX where revoked_on != null:
     *     ENTRY.revokedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     * </pre>
     */
    private void BR_G30_revokedIsMember(MINDEX entry) {
        if (entry.revoked_on != null) {
            System.out.println("BR_G30_revokedIsMember - ENTRY.revokedIsMember - " + entry.pub);
            entry.revokedIsMember = reduceI(entry.pub).anyMatch(i -> i.member);
        } else {
            entry.revokedIsMember = true;
        }
    }

    /**
     * <pre>
     * BR_G31_alreadyRevoked
     *
     * For each ENTRY in local MINDEX where revoked_on == null:
     *     ENTRY.alreadyRevoked = false
     *
     * For each ENTRY in local MINDEX where revoked_on != null:
     *     ENTRY.alreadyRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked_on != null
     * </pre>
     */
    private void BR_G31_alreadyRevoked(MINDEX entry) {
        if (entry.revoked_on != null) {
            entry.alreadyRevoked = reduceM(entry.pub).anyMatch(m -> m.revoked_on != null);
        } else {
            entry.alreadyRevoked = false;
        }

        //		System.out.println("BR_G31_alreadyRevoked set alreadyRevoked " + entry.alreadyRevoked + " " + entry.revoked_on);
    }

    /**
     * <pre>
     * BR_G32_revocationSigOK
     *
     * For each ENTRY in local MINDEX where revoked_on == null:
     *     ENTRY.revocationSigOK = true
     *
     * For each ENTRY in local MINDEX where revoked_on != null:
     *     ENTRY.revocationSigOK = SIG_CHECK_REVOKE(REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]), ENTRY)
     * </pre>
     */
    private void BR_G32_revocationSigOK(MINDEX entry) {
        if (entry.revoked_on != null) {
            entry.revocationSigOK = reduceM(entry.pub).allMatch(m -> Crypto.verify("", entry.revocation, entry.pub) || true);
            // TODO sigcheck requires Membership Document?
        } else {
            entry.revocationSigOK = true;
        }
    }

    /**
     * <pre>
     * BR_G33_excludedIsMember
     *
     * For each ENTRY in local IINDEX where member != false:
     *     ENTRY.excludedIsMember = true
     *
     * For each ENTRY in local IINDEX where member == false:
     *     ENTRY.excludedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     * </pre>
     */
    private void BR_G33_excludedIsMember(IINDEX entry) {
        if (entry.member) {
            entry.excludedIsMember = true;
        } else {
            entry.excludedIsMember = reduceI(entry.pub).anyMatch(i -> i.member);
        }
    }

    /**
     * <pre>
     * BR_G34_isBeingRevoked
     *
     * For each ENTRY in local MINDEX where revoked_on == null:
     *     ENTRY.isBeingRevoked = false
     * For each ENTRY in local MINDEX where revoked_on != null:
     *     ENTRY.isBeingRevoked = true
     * </pre>
     */
    private void BR_G34_isBeingRevoked(MINDEX entry) {
        entry.isBeingRevoked = entry.revoked_on != null;

    }

    /**
     * <pre>
     * BR_G35_isBeingKicked
     *
     * For each ENTRY in local IINDEX where member != false:
     *     ENTRY.isBeingKicked = false
     * For each ENTRY in local IINDEX where member == false:
     *     ENTRY.isBeingKicked = true
     * </pre>
     */
    private void BR_G35_isBeingKicked(IINDEX entry) {
        entry.isBeingKicked = !entry.member;

    }

    /**
     * <pre>
     * BR_G36_hasToBeExcluded
     *
     * For each ENTRY in local IINDEX:
     *     ENTRY.hasToBeExcluded = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).kick
     *
     * </pre>
     */
    private void BR_G36_hasToBeExcluded(IINDEX entry) {
        entry.hasToBeExcluded = reduceI(entry.pub).anyMatch(i -> i.kick);

    }

    /**
     * <pre>
     *
     * BR_G37_age
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
     * @param head  : qsdq
     * @param entry : qssd
     */
    private void BR_G37_age(BINDEX head, CINDEX entry) {

        final long sigWindow = 0;

        if (head.number == 0 && "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855".equals(entry.created_on.toString())) {
            entry.age = 0;
        } else {
            entry.age = IndexB.stream()
                    .filter(b -> b.hash.equals(entry.created_on.getHash()))
                    .filter(b -> b.number == entry.created_on.getNumber())
                    .findAny()
                    .map(refb -> head_1().medianTime - refb.medianTime)
                    .orElse(sigWindow + 1);
        }

    }

    /**
     * <pre>
     * BR_G38_CertUnchainable
     *
     * If HEAD.number > 0:
     *     ENTRY.unchainables = COUNT(GLOBAL_CINDEX[issuer=ENTRY.issuer, chainable_on > HEAD~1.medianTime]))
     * </pre>
     */
    private void BR_G38_CertUnchainable(BINDEX head, CINDEX entry) {
        if (head.number > 0) {
            entry.unchainables = reduceC(entry.issuer, null)
                    .filter(c -> c.chainable_on > head_1().medianTime)
                    .count();
        }

    }

    /**
     * <pre>
     * BR_G39_CertStock
     *
     * ENTRY.stock = COUNT(REDUCE_BY(GLOBAL_CINDEX[issuer=ENTRY.issuer], 'receiver', 'created_on')[expired_on=0])
     *
     * </pre>
     */
    private void BR_G39_CertStock(CINDEX entry) {
        // TODO REDUCE BY recevier, created_on ??

        assert indexCGlobal() != null : "BR_G39_CertStock - index null ";
        entry.stock = indexCGlobal().filter(c -> {
                    assert entry.issuer != null : "BR_G39_CertStock -  issuer null " + entry + "\n" + c;
                    return entry.issuer.equals(c.issuer);
                }
        ).count();

    }

    /**
     * <pre>
     * BR_G40_CertFromMember
     *
     * ENTRY.fromMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.issuer]).member
     * </pre>
     */
    private void BR_G40_CertFromMember(CINDEX entry) {
        entry.fromMember = reduceI(entry.issuer).anyMatch(i -> i.member);
    }

    /**
     * <pre>
     * BR_G41_CertToMember
     *
     * ENTRY.toMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]).member
     *
     * </pre>
     */
    private void BR_G41_CertToMember(CINDEX entry) {
        entry.toMember = reduceI(entry.receiver).allMatch(i -> i.member);

    }

    /**
     * <pre>
     * BR_G42_CertToNewCommer
     *
     * ENTRY.toNewcomer = COUNT(LOCAL_IINDEX[member=true,pub=ENTRY.receiver]) > 0
     * </pre>
     */
    private void BR_G42_CertToNewCommer(CINDEX entry) {
        entry.toNewcomer = localI.stream().anyMatch(i -> entry.receiver.equals(i.pub) && i.member);

    }

    /**
     * <pre>
     * BR_G43_ToLeaver
     *
     * ENTRY.toLeaver = REDUCE(GLOBAL_MINDEX[pub=ENTRY.receiver]).leaving
     * </pre>
     */
    private void BR_G43_ToLeaver(CINDEX entry) {
        entry.toLeaver = reduceM(
                entry
                        .receiver)
                .anyMatch(i -> i
                        .leaving);

    }

    /**
     * <pre>
     * BR_G44_IsReplay
     *
     * reducible = GLOBAL_CINDEX[issuer=ENTRY.issuer,receiver=ENTRY.receiver,expired_on=0]
     *
     * If count(reducible) == 0:
     *     ENTRY.isReplay = false
     * Else:
     *     ENTRY.isReplay = reduce(reducible).expired_on == 0
     * </pre>
     */
    private void BR_G44_IsReplay(CINDEX entry) {
        entry.isReplay = reduceC(entry.issuer, entry.receiver)
                .anyMatch(c -> c.expired_on == 0);
    }

    /**
     * <pre>
     *
     * BR_G45_sigOK
     *
     * ENTRY.sigOK = SIG_CHECK_CERT(REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]), ENTRY)
     *
     * </pre>
     */
    private void BR_G45_sigOK(CINDEX entry) {
        entry.sigOK = true; // FIXME signature is a local check ??
    }

    /**
     * Local SINDEX augmentation
     *
     * <pre>
     *
     * BR_G46_prepareAvailableAndCondition - ENTRY.available and ENTRY.conditions
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.conditions = INPUT.conditions
     *     ENTRY.available = INPUT.consumed == false
     *
     * </pre>
     */
    private void BR_G46_prepareAvailableAndCondition(SINDEX entry, SINDEX input) {
        entry.available = !input.consumed;
        entry.conditions = input.conditions;

    }

    /**
     * <pre>
     * BR_G47_prepareIsLocked
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.isLocked = TX_SOURCE_UNLOCK(INPUT.conditions, ENTRY)
     * </pre>
     */
    private void BR_G47_prepareIsLocked(SINDEX entry, SINDEX input) {

        //System.out.println( "BR_G47_prepareIsLocked - ENTRY.isLocked" + entry.identifier + " - " + input.consumed + " " + input.conditions);
        entry.isLocked = false; // FIXME BR_G47_prepareIsLocked
    }

    /**
     * <pre>
     *
     * BR_G48_prepareIsTimeLocked
     *
     * INPUT = REDUCE(INPUT_ENTRIES)
     * ENTRY.isTimeLocked = ENTRY.written_time - INPUT.written_time < ENTRY.locktime
     * </pre>
     */
    private void BR_G48_prepareIsTimeLocked(SINDEX entry, SINDEX input) {

        entry.isTimeLocked = entry.written_time - input.written_time < entry.locktime;

    }

    /**
     * <pre>
     * BR_G49_Version
     *
     *  If `HEAD.number > 0`
     *      HEAD.version == (HEAD~1.version OR HEAD~1.version + 1)
     *
     * </pre>
     */
    private boolean BR_G49_Version(BINDEX head) {
        return head.number == 0 || head.version.equals(head_1().version) || head.version == head_1().version + 1;
    }

    /**
     * <pre>
     * BR_G50_BlockSize
     *
     * Rule:
     *
     * If HEAD.number > 0:
     *     HEAD.size < MAX(500 ; CEIL(1.10 * HEAD.avgBlockSize))
     * </pre>
     */
    private boolean BR_G50_BlockSize(BINDEX head) {
        return head.number == 0 || head.size < Math.max(500, Math.ceil(1.1 * head.avgBlockSize));
    }

    /**
     * <pre>
     * BR_G51_BlockNumber
     *
     * Number = HEAD.number
     * </pre>
     */
    private boolean BR_G51_BlockNumber(BINDEX testHead, Block shouldBe) {
        return testHead.number == shouldBe.getNumber();
    }

    /**
     * <pre>
     * BR_G52_PreviousHash
     *
     * PreviousHash = HEAD.previousHash
     * </pre>
     */
    private boolean BR_G52_PreviousHash(BINDEX testHead, Block block) {
        if (testHead.number == 0)
            return true;

        assert testHead.previousHash.equals(block.getPreviousHash())
                : "BR_G52_PreviousHash - rule PreviousHash - " + testHead.previousHash + " " + block.getPreviousHash();

        return testHead.previousHash.equals(block.getPreviousHash());
    }

    /**
     * <pre>
     * BR_G53_PreviousIssuer
     *
     * PreviousIssuer = HEAD.previousIssuer
     * </pre>
     */
    private boolean BR_G53_PreviousIssuer(BINDEX testHead, Block shouldBe) {
        if (testHead.number == 0)
            return true;

        assert testHead.previousIssuer.equals(shouldBe.getPreviousIssuer()) : "BR_G53_PreviousIssuer - rule PreviousIssuer ";

        return testHead.previousIssuer.equals(shouldBe.getPreviousIssuer());
    }

    /**
     * <pre>
     * BR_G54_DifferentIssuersCount
     *
     * DifferentIssuersCount = HEAD.issuersCount
     * </pre>
     */
    private boolean BR_G54_DifferentIssuersCount(BINDEX testHead, Block shouldBe) {
        assert testHead.issuersCount.equals(shouldBe.getIssuersCount()) : "BR_G54_DifferentIssuersCount - " + testHead.issuersCount + "-" + shouldBe.getIssuersCount();
        return testHead.issuersCount.equals(shouldBe.getIssuersCount());
    }

    /**
     * <pre>
     * BR_G55_IssuersFrame
     *
     * IssuersFrame = HEAD.issuersFrame
     * </pre>
     */
    private boolean BR_G55_IssuersFrame(BINDEX testHead, Block shouldBe) {
        return testHead.issuersFrame.equals(shouldBe.getIssuersFrame());
    }

    /**
     * <pre>
     * BR_G56_IssuersFrameVar
     *
     * IssuersFrameVar = HEAD.issuersFrameVar
     * </pre>
     */
    private boolean BR_G56_IssuersFrameVar(BINDEX testHead, Block block) {
        return testHead.issuersFrameVar.equals(block.getIssuersFrameVar());
    }

    /**
     * <pre>
     * BR_G57_MedianTime
     *
     * MedianTime = HEAD.medianTime
     * </pre>
     */
    private boolean BR_G57_MedianTime(BINDEX head, Block block) {
        assert head.medianTime.equals(block.getMedianTime()) : "BR_G57_MedianTime - rule Median Time - " + head.medianTime + " ==? " + block.getMedianTime();
        return head.medianTime.equals(block.getMedianTime());
    }


    /**
     * <pre>
     * BR_G58_UniversalDividend
     *
     * UniversalDividend = HEAD.new_dividend
     * </pre>
     *
     * @param testHead BINDEX
     * @param block    the block we're comparing
     * @return ud equality accepting null
     */
    private boolean BR_G58_UniversalDividend(BINDEX testHead, Block block) {

        if (testHead.number == 0)
            return true;

        if (testHead.new_dividend == null)
            return block.getDividend() == null;
        else {
            assert testHead.new_dividend.equals(block.getDividend()) : "BR_G58_UniversalDividend - " + testHead.new_dividend + " ==? " + block.getDividend();
            return testHead.new_dividend.equals(block.getDividend());
        }


    }

    /**
     * <pre>
     * BR_G59_UnitBase
     *
     * UnitBase = HEAD.unitBase
     * </pre>
     */
    private boolean BR_G59_UnitBase(BINDEX head, Block block) {
        assert head.unitBase.equals(block.getUnitbase()) : "BR_G59_UnitBase - UnitBase - " + head.unitBase + " "
                + block.getDividend();
        return head.unitBase.equals(block.getUnitbase());
    }

    /**
     * <pre>
     * BR_G60_ruleMembersCount
     *
     * MembersCount = HEAD.membersCount
     * </pre>
     */
    private boolean BR_G60_ruleMembersCount(BINDEX head, Block block) {
        assert block.getMembersCount().equals(head.membersCount) : "BR_G60_ruleMembersCount MembersCount " + head.membersCount + "-"
                + block.getMembersCount();
        return block.getMembersCount().equals(head.membersCount);

    }

    /**
     * <pre>
     * BR_G61_rulePowMin
     *
     * If HEAD.number > 0
     *     PowMin = HEAD.powMin
     * </pre>
     */
    private boolean BR_G61_rulePowMin(BINDEX head, Block block) {
        assert block.getNumber() == 0 || head.powMin == block.getPowMin() : "BR_G61_rulePowMin - rule PowMin - " + head.powMin
                + " != " + block.getPowMin();
        return block.getNumber() == 0 || head.powMin == block.getPowMin();
    }

    /**
     * <pre>
     * BR_G62_ProofOfWork
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
    private boolean BR_G62_ProofOfWork(BINDEX head) {
        final String expectedStart = head.hash.substring(0, head.powZeros + 1);

        //System.out.println("BR_G62_ProofOfWork - Proof-of-work " + expectedStart + " expected " + head.powZeros + " zeros with remainder " + head.powRemainder);

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
     * BR_G63_IdentityWritability
     *
     * ENTRY.age <= [idtyWindow]
     */
    private boolean BR_G63_IdentityWritability(IINDEX i) {
        assert i.age <= blockChainParams.idtyWindow : "BR_G63_IdentityWritability - age: " + i.age + " <=? " + blockChainParams.idtyWindow;
        return i.age <= blockChainParams.idtyWindow;
    }

    /**
     * <pre>
     * BR_G64_MembershipMsWindow
     *
     * Rule:
     *
     * ENTRY.age <= [msWindow]
     */
    private boolean BR_G64_MembershipMsWindow(MINDEX m) {
        assert m.age <= blockChainParams.msWindow : "BR_G64_MembershipMsWindow - age:" + m.age + " for " + m;
        return m.age <= blockChainParams.msWindow;
    }

    /**
     * <pre>
     * BR_G65_CertificationSigWindow
     *
     * Rule:
     *
     * ENTRY.age <= [sigWindow]
     */
    private boolean BR_G65_CertificationSigWindow(CINDEX c) {
        //assert c.age <= blockChainParams.sigWindow : "BR_G65_CertificationSigWindow - Certification sigWindow";
        return c.age <= blockChainParams.sigWindow;
    }

    /**
     * <pre>
     * BR_G66_CertificationStock
     *
     * Rule:
     *
     * ENTRY.stock <= sigStock
     */
    private boolean BR_G66_CertificationStock(CINDEX c) {
        //assert c.stock <= blockChainParams.sigStock : "BR_G66_CertificationStock - Certification stock";
        return c.stock <= blockChainParams.sigStock;
    }

    /**
     * <pre>
     * BR_G67_CertificationPeriod
     *
     * Rule:
     *
     * ENTRY.unchainables == 0
     */
    private boolean BR_G67_CertificationPeriod(CINDEX c) {
        //assert c.unchainables == 0 : "BR_G67_CertificationPeriod - Certification period";
        return c.unchainables == 0;
    }

    /**
     * <pre>
     * BR_G68_CertificationFromMember
     *
     * Rule:
     *
     * If HEAD.number > 0:
     *
     * ENTRY.fromMember == true
     */
    private boolean BR_G68_CertificationFromMember(BINDEX head, CINDEX entry) {
        //assert head.number == 0 || entry.fromMember : "BR_G68_CertificationFromMember - Certification from member";
        return head.number == 0 || entry.fromMember;
    }

    /**
     * <pre>
     * BR_G69_CertificationToMemberOrNewComer
     *
     * Rule:
     *
     * ENTRY.toMember == true OR ENTRY.toNewcomer == true
     */
    private boolean BR_G69_CertificationToMemberOrNewComer(CINDEX c) {
        //assert c.toMember || c.toNewcomer : "BR_G69_CertificationToMemberOrNewComer - Certification to member or newcomer";
        return c.toMember || c.toNewcomer;
    }

    /**
     * <pre>
     * BR_G70_CertificationToNonLeaver
     *
     * Rule:
     *
     * ENTRY.toLeaver == false
     */
    private boolean BR_G70_CertificationToNonLeaver(CINDEX c) {
        //assert !c.toLeaver : "BR_G70_CertificationToNonLeaver - Certification to non-leaver";
        return !c.toLeaver;
    }

    /**
     * <pre>
     * BR_G71_CertificationReplay
     *
     * Rule:
     *
     * ENTRY.isReplay == false
     */
    private boolean BR_G71_CertificationReplay(CINDEX c) {
        assert !c.isReplay : "BR_G71_CertificationReplay - Certification replay";
        return !c.isReplay;
    }

    /**
     * <pre>
     * BR_G72_CertificationSignature
     *
     * Rule:
     *
     * ENTRY.sigOK == true
     */
    private boolean BR_G72_CertificationSignature(CINDEX c) {
        assert c.sigOK : "BR_G72_CertificationSignature - Certification signature ";
        return c.sigOK;
    }

    /**
     * <pre>
     * BR_G73_IdentityUserIdUnicity
     *
     * Rule:
     *
     * ENTRY.uidUnique == true
     */
    private boolean BR_G73_IdentityUserIdUnicity(IINDEX i) {
        assert i.uidUnique : "BR_G73_IdentityUserIdUnicity - Identity UserID unicity ";
        return i.uidUnique;
    }

    /**
     * <pre>
     * BR_G74_IdentityPubkeyUnicity
     *
     * Rule:
     *
     * ENTRY.pubUnique == true
     */
    private boolean BR_G74_IdentityPubkeyUnicity(IINDEX i) {
        assert i.pubUnique : "BR_G74_IdentityPubkeyUnicity - Identity pubkey unicity ";
        return i.pubUnique;
    }

    /**
     * <pre>
     * BR_G75_MembershipSuccession
     *
     * Rule:
     *
     * ENTRY.numberFollowing == true
     */
    private boolean BR_G75_MembershipSuccession(MINDEX m) {
        assert m.numberFollowing : "BR_G75_MembershipSuccession - " + m;
        return m.numberFollowing;
    }

    /**
     * <pre>
     * BR_G76_MembershipDistance
     *
     * Rule:
     *
     * ENTRY.distanceOK == true
     */
    private boolean BR_G76_MembershipDistance(MINDEX m) {
        assert m.distanceOK : "BR_G76_MembershipDistance - " + m;
        return m.distanceOK;
    }

    /**
     * <pre>
     * BR_G77_MembershipOnRevoked
     *
     * Rule:
     *
     * ENTRY.onRevoked == false
     */
    private boolean BR_G77_MembershipOnRevoked(MINDEX m) {
        assert !m.onRevoked : "BR_G77_MembershipOnRevoked - " + m.pub;
        return !m.onRevoked;
    }

    /**
     * <pre>
     * BR_G78_MembershipJoinsTwice
     *
     * Rule:
     *
     * ENTRY.joinsTwice == false
     */
    private boolean BR_G78_MembershipJoinsTwice(MINDEX m) {
        assert !m.joinsTwice : "BR_G78_MembershipJoinsTwice - " + m;
        return !m.joinsTwice;
    }

    /**
     * <pre>
     * BR_G79_MembershipEnoughCertifications
     *
     * Rule:
     *
     * ENTRY.enoughCerts == true
     */
    private boolean BR_G79_MembershipEnoughCertifications(MINDEX m) {
        assert m.enoughCerts : "BR_G79_MembershipEnoughCertifications - " + m;
        return m.enoughCerts;
    }

    /**
     * <pre>
     * BR_G80_MembershipLeaver -
     *
     * Rule:
     *
     * ENTRY.leaverIsMember == true
     */
    private boolean BR_G80_MembershipLeaver(MINDEX m) {
        assert m.leaverIsMember : "BR_G80_MembershipLeaver - ";
        return m.leaverIsMember;
    }

    /**
     * <pre>
     * BR_G81_MembershipActive
     *
     * Rule:
     *
     * ENTRY.activeIsMember == true
     */
    private boolean BR_G81_MembershipActive(MINDEX m) {
        assert m.activeIsMember : "BR_G81_MembershipActive - ";
        return m.activeIsMember;
    }

    /**
     * <pre>
     * BR_G82_RevokedIsMember
     *
     * Rule:
     *
     * ENTRY.revokedIsMember == true
     */
    private boolean BR_G82_RevokedIsMember(MINDEX m) {
        assert m.revokedIsMember : "BR_G82_RevokedIsMember - ";
        return m.revokedIsMember;
    }

    /**
     * <pre>
     * BR_G83_RevocationSingleton
     *
     * Rule:
     *
     * ENTRY.alreadyRevoked == false
     */
    private boolean BR_G83_RevocationSingleton(MINDEX m) {
        assert !m.alreadyRevoked : "BR_G83_RevocationSingleton - ";
        return !m.alreadyRevoked;
    }

    /**
     * <pre>
     * BR_G84_RevocationSignature
     *
     * Rule:
     *
     * ENTRY.revocationSigOK == true
     */
    private boolean BR_G84_RevocationSignature(MINDEX m) {
        assert m.revocationSigOK : "BR_G84_RevocationSignature - ";
        return m.revocationSigOK;
    }

    /**
     * <pre>
     * BR_G85_ExcludedIsMember
     *
     * Rule:
     *
     * ENTRY.excludedIsMember == true
     */
    private boolean BR_G85_ExcludedIsMember(IINDEX i) {
        assert i.excludedIsMember : "BR_G85_ExcludedIsMember - " + i.excludedIsMember;
        return i.excludedIsMember;
    }

    /**
     * <pre>
     * BR_G86_ExcludedContainsExactlyThoseKicked
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
    private boolean BR_G86_ExcludedContainsExactlyThoseKicked() {

        boolean ExcludedContainsExactlyThoseToBeKicked =
                indexIGlobal().filter(i -> i.kick)
                        .flatMap(toKick -> reduceI(toKick.pub))
                        .filter(i -> i.kick)
                        .flatMap(reduced -> reduceM(reduced.pub))
                        .count() == 1;

        if (ExcludedContainsExactlyThoseToBeKicked) {
            localI.forEach(i -> {
                if (!i.member) {
                    i.hasToBeExcluded = true;
                }

            });
        }

        ExcludedContainsExactlyThoseToBeKicked = true; // FIXME

        assert ExcludedContainsExactlyThoseToBeKicked : "BR_G86_ExcludedContainsExactlyThoseKicked - ";
        return ExcludedContainsExactlyThoseToBeKicked;
    }

    /**
     * <pre>
     * BR_G87_InputIsAvailable
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.available == true
     * </pre>
     */
    private boolean BR_G87_InputIsAvailable(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert true : "BR_G87_InputIsAvailable - rule Input is not available " + entry.available + " " + entry.consumed;
            return true; // FIXME complete
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G88_InputIsUnlocked
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.isLocked == false
     */
    private boolean BR_G88_InputIsUnlocked(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert !entry.isLocked : "BR_G88_InputIsUnlocked - " + entry;
            return !entry.isLocked;
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G89_InputIsTimeUnlocked
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.isTimeLocked == false
     * </pre>
     */
    private boolean BR_G89_InputIsTimeUnlocked(SINDEX entry) {
        if ("UPDATE".equals(entry.op)) {
            assert !entry.isTimeLocked : "BR_G89_InputIsTimeUnlocked - " + entry;
            return !entry.isTimeLocked;
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G90_OutputBase
     *
     * For each LOCAL_SINDEX[op='CREATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.unitBase <= HEAD~1.unitBase
     * </pre>
     */
    private boolean BR_G90_OutputBase(SINDEX entry) {
        assert entry != null : "BR_G90_OutputBase - rule Output Base - entry is null";

        if ("CREATE".equals(entry.op)) {

            assert head_1() != null : "BR_G90_OutputBase - rule Output base - HEAD-1 is null ";

            assert entry.base <= head_1().unitBase
                    : "BR_G90_OutputBase - rule Output base - " + entry.base + " " + head_1().unitBase;

            return entry.base <= head_1().unitBase;
        } else
            return true;

    }

    /**
     * <pre>
     * BR_G91_Dividend
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
    private void BR_G91_Dividend(BINDEX head, BStamp block) {
        if (head.new_dividend == null)
            return;

        Stream.concat(indexIGlobal(), localI.stream())
                .filter(i -> i.member)
                .map(i -> new SINDEX("CREATE",
                        i.pub,
                        head.number,
                        null,
                        block,
                        head.medianTime,
                        head.dividend,
                        head.unitBase,
                        null,
                        "SIG(" + i.pub + ")",
                        false, // consumed
                        null))

                .forEach(tx -> {
                    //					System.out.println("BR_G91_Dividend adding tx " + tx.base + " from " + head.unitBase);
                    localS.add(tx);
                });


        //		assert localI.size() > 0 : "BR_G91_Dividend - Dividend - localS shouldnt be empty";
    }

    /**
     * <pre>
     * BR_G92_CertificationExpiry
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
    private void BR_G92_CertificationExpiry(BINDEX head) {

        indexCGlobal().filter(c -> c.expires_on != null && c.expires_on <= head.medianTime)
                .map(c -> new CINDEX("UPDATE",
                        c.issuer,
                        c.receiver,
                        c.createdOn,
                        head.medianTime))
                .forEach(localC::add);

    }

    /**
     * <pre>
     * BR_G93_MembershipExpiry
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
    private void BR_G93_MembershipExpiry(BINDEX head) {
        indexMGlobal()
                .filter(m -> {
                    //assert m.expires_on != null : "BR_G93_MembershipExpiry - expires_on cannot be null, cannot compare ";
                    //assert m.revokes_on != null : "BR_G93_MembershipExpiry - revokes_on cannot be null, cannot compare ";

                    return m.revokes_on != null && m.expires_on != null && m.expires_on <= head.medianTime && m.revokes_on > head.medianTime;
                })
                .flatMap(potential -> reduceM(potential.pub))
                .filter(ms -> ms.expired_on == null || ms.expired_on == 0)
                .forEach(ms -> {
                    localM.add(new MINDEX("UPDATE",
                            ms.pub,
                            head.bstamp(),
                            head.medianTime));
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
    private void BR_G94_ExclusionByMembership(BStamp blockstamp) {

        for (final MINDEX m : localM) {
            //			System.out.println(m);
            if (m.expired_on != null && m.expired_on != 0) {
                localI.add(new IINDEX("UPDATE",
                        m.pub,
                        blockstamp, // written_on
                        true // kick
                ));
            }
        }
    }

    /**
     * <pre>
     * BR_G95_ExclusionByCertification  //FIXME REDUCE BY ...
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
    private void BR_G95_ExclusionByCertification(BStamp block) {
        localC.forEach(cert -> {

            final var cntLocal = localC.stream().filter(c -> cert.receiver.equals(c.receiver) && c.expired_on == 0)
                    .count();
            final var cntExpired = localC.stream().filter(c -> cert.receiver.equals(c.receiver) && c.expired_on != 0)
                    .count();
            final var cntValid = reduceC(null, cert.receiver).count();

            if (cntValid + cntLocal - cntExpired < blockChainParams.sigQty) {
                localI.add(new IINDEX("UPDATE", cert.receiver, block, true));
            }

        });

    }

    /**
     * <pre>
     * BR_G96_ImplicitRevocation
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
    private void BR_G96_ImplicitRevocation(BINDEX head) {
        indexMGlobal()
                .filter(m -> m.revokes_on != null && m.revokes_on <= head.medianTime && m.revoked_on == null)
                .flatMap(potential -> reduceM(potential.pub))
                .filter(ms -> ms.revokes_on.equals(head.medianTime) || ms.revoked_on == null)
                .forEach(ms -> {
                    localM.add(new MINDEX("UPDATE",
                            ms.pub,
                            head.bstamp(),
                            head.medianTime));
                });
    }

    /**
     * <pre>
     * BR_G97_TestIndex - Final INDEX operations
     *
     * If all the rules pass, then all the LOCAL INDEX values (IINDEX, MINDEX,
     * CINDEX, SINDEX, BINDEX) have to be appended to the GLOBAL INDEX.
     *
     * </pre>
     */
    private boolean BR_G97_TestIndex(BINDEX newHead, Block block, boolean checkPoW) {

        if (globalCheckBINDEX(newHead, block, checkPoW) &&
                globalCheckSINDEX() &&
                globalCheckMINDEX() &&
                globalCheckCINDEX(newHead) &&
                globalCheckIINDEX()) {

            IndexB.add(newHead);
            return commit(newHead, localI, localM, localC, localS, consumedI, consumedM, consumedC, consumedS);
        } else {
            System.out.println("BR_G97_TestIndex did not pass at block " + block + " (ICM : " + localI.size() + " " + localC.size() + " " + localM.size() + ") " + newHead);
            System.out.println("BR_G97_TestIndex: " + localI.size() + " " + globalCheckBINDEX(newHead, block, checkPoW) + " "
                    + globalCheckSINDEX() + " " + globalCheckMINDEX() + " " + globalCheckCINDEX(newHead) + " "
                    + globalCheckIINDEX());
            return false;
        }

    }

    /**
     * <pre>
     * BR_G98_ruleCurrency
     *
     * Rule: "Currency  has a block to block consistency but has no expectations on block 0"
     *
     * If HEAD.number > 0:
     *     Currency = HEAD.currency
     * else
     *     true
     * </pre>
     */
    private boolean BR_G98_ruleCurrency(BINDEX head, Block ccy) {
        assert head != null : "BR_G98_ruleCurrency - rule currency - head is null ";

        if (head.number == 0)
            return true;
        else {
            assert head.currency != null : "BR_G98_ruleCurrency - rule currency - BINDEX.currency is null ";
            assert ccy != null : "BR_G98_ruleCurrency - rule currency - Block.currency is null ";
            assert head.currency.equals(ccy.getCurrency()) : "BR_G98_ruleCurrency - rule currency - Block.currency is different ";
            return head.currency.equals(ccy.getCurrency());
        }

    }

    /**
     * <pre>
     * BR_G99_setCurrency
     *
     * If HEAD.number > 0
     *     HEAD.currency = HEAD~1.currency
     * Else
     *     HEAD.currency = null
     *
     * </pre>
     */
    private void BR_G99_setCurrency(BINDEX head) {

        if (head.number > 0) {
            head.currency = head_1().currency;
            assert head_1() != null : "BR_G99_setCurrency - HEAD-1 is null ";
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
    boolean commit(BINDEX indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS,
                   List<IINDEX> consumeI, List<MINDEX> consumeM, List<CINDEX> consumeC, List<SINDEX> consumeS);

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
        final var inputs = localS.stream()
                .filter(s -> s.op.equals("UPDATE") && s.tx.equals(txHash))
                .collect(Collectors.toList());
        var depth = localDepth;
        for (final SINDEX input : inputs) {
            final var consumed = localS.stream()
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

        if (BR_G49_Version(testHead) &&
                BR_G50_BlockSize(testHead) &&
                BR_G98_ruleCurrency(testHead, block) &&
                BR_G51_BlockNumber(testHead, block) &&
                BR_G52_PreviousHash(testHead, block) &&
                BR_G53_PreviousIssuer(testHead, block) &&
                BR_G54_DifferentIssuersCount(testHead, block) &&
                BR_G55_IssuersFrame(testHead, block) &&
                BR_G56_IssuersFrameVar(testHead, block) &&
                BR_G57_MedianTime(testHead, block) &&
                BR_G58_UniversalDividend(testHead, block) &&
                BR_G59_UnitBase(testHead, block) &&
                BR_G60_ruleMembersCount(testHead, block) &&
                BR_G61_rulePowMin(testHead, block) &&
                BR_G101_IssuerIsMember(testHead) &&
                (!checkPoW || BR_G62_ProofOfWork(testHead)))
            return true;
        else
            System.out.println("error on globalCheckBINDEX " +
                    BR_G49_Version(testHead) + " BR_G49_Version - " +
                    BR_G50_BlockSize(testHead) + " BR_G50_BlockSize - " +
                    BR_G98_ruleCurrency(testHead, block) + " BR_G98_ruleCurrency - " +
                    BR_G51_BlockNumber(testHead, block) + " BR_G51_BlockNumber - " + //hash
                    BR_G53_PreviousIssuer(testHead, block) + " BR_G53_PreviousIssuer - " +
                    BR_G54_DifferentIssuersCount(testHead, block) + " BR_G54_DifferentIssuersCount - " +
                    BR_G55_IssuersFrame(testHead, block) + " BR_G55_IssuersFrame - " +
                    BR_G56_IssuersFrameVar(testHead, block) + " BR_G56_IssuersFrameVar - " +
                    BR_G57_MedianTime(testHead, block) + " BR_G57_MedianTime - " +
                    BR_G58_UniversalDividend(testHead, block) + " BR_G58_UniversalDividend - " +
                    BR_G59_UnitBase(testHead, block) + " BR_G59_UnitBase - " +
                    BR_G60_ruleMembersCount(testHead, block) + " BR_G60_ruleMembersCount - " +
                    BR_G61_rulePowMin(testHead, block) + " BR_G61_rulePowMin - " +
                    BR_G101_IssuerIsMember(testHead) + " BR_G101_IssuerIsMember - " +
                    (!checkPoW || BR_G62_ProofOfWork(testHead)));

        return false;
    }

    default boolean globalCheckCINDEX(BINDEX head) {
        return localC.stream()
                .allMatch(c -> {

                    if (BR_G65_CertificationSigWindow(c)
                            && BR_G66_CertificationStock(c)
                            && BR_G67_CertificationPeriod(c)
                            && BR_G68_CertificationFromMember(head, c)
                            && BR_G69_CertificationToMemberOrNewComer(c)
                            && BR_G70_CertificationToNonLeaver(c)
                            && BR_G71_CertificationReplay(c)
                            && BR_G72_CertificationSignature(c)
                            && BR_G108_MembershipPeriod(c)) {
                        return true;
                    } else {
                        System.out.println("error on globalCheckCINDEX " +
                                BR_G65_CertificationSigWindow(c) + " writability - " +
                                BR_G66_CertificationStock(c) + " stock - " +
                                BR_G67_CertificationPeriod(c) + " period - " +
                                BR_G68_CertificationFromMember(head, c) + " fromMember - " +
                                BR_G69_CertificationToMemberOrNewComer(c) + " toMember - " +
                                BR_G70_CertificationToNonLeaver(c) + " toLeaver - " +
                                BR_G71_CertificationReplay(c) + " isReplay - " +
                                BR_G72_CertificationSignature(c) + " signature - " +
                                BR_G108_MembershipPeriod(c) + " membership period"
                        );
                        return false;

                    }


                });

    }

    default boolean globalCheckIINDEX() {

        if (!BR_G86_ExcludedContainsExactlyThoseKicked())
            return false;

        return localI.stream()
                .allMatch(i ->
                        BR_G63_IdentityWritability(i)
                                && BR_G73_IdentityUserIdUnicity(i)
                                && BR_G74_IdentityPubkeyUnicity(i)
                                && BR_G85_ExcludedIsMember(i)
                );
    }

    default boolean globalCheckMINDEX() {
        return localM.stream().allMatch(m -> {


            if (BR_G64_MembershipMsWindow(m)
                    && BR_G75_MembershipSuccession(m)
                    && BR_G76_MembershipDistance(m)
                    && BR_G77_MembershipOnRevoked(m)
                    && BR_G78_MembershipJoinsTwice(m)
                    && BR_G79_MembershipEnoughCertifications(m)
                    && BR_G80_MembershipLeaver(m)
                    && BR_G81_MembershipActive(m)
                    && BR_G82_RevokedIsMember(m)
                    && BR_G83_RevocationSingleton(m)
                    && BR_G84_RevocationSignature(m)
            ) {
                return true;
            } else {
                System.out.println("on globalCheckMINDEX " + BR_G64_MembershipMsWindow(m) + " " + BR_G75_MembershipSuccession(m) + " " + BR_G76_MembershipDistance(m) + " " + BR_G77_MembershipOnRevoked(m)
                        + " " + BR_G78_MembershipJoinsTwice(m) + " " + BR_G79_MembershipEnoughCertifications(m) + " " + BR_G80_MembershipLeaver(m) + " " + BR_G81_MembershipActive(m) + " " + BR_G82_RevokedIsMember(m) + " "
                        + BR_G83_RevocationSingleton(m) + " " + BR_G84_RevocationSignature(m) + " ");
            }


            return false;
        });
    }

    default boolean globalCheckSINDEX() {
        return localS.stream().allMatch(tx -> {

            //			System.out.println("globalCheckSINDEX : " + tx + " " + tx.base + " " + tx.op
            //					+ "\n" + BR_G103_TransactionWritable(tx) + BR_G87_InputIsAvailable(tx) + " " + BR_G88_InputIsUnlocked(tx) + " " + BR_G89_InputIsTimeUnlocked(tx) + " " + BR_G90_OutputBase(tx)
            //			);

            return BR_G103_TransactionWritable(tx) && BR_G87_InputIsAvailable(tx) && BR_G88_InputIsUnlocked(tx) && BR_G89_InputIsTimeUnlocked(tx) && BR_G90_OutputBase(tx);

        });
    }

    default Optional<BINDEX> head() {
        if (IndexB.size() > 1)
            return Optional.of(IndexB.get(IndexB.size() - 1));

        return Optional.empty();
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
    default boolean indexBlock(Block block, BINDEX head) {

        final var blStamp = new BStamp(block.getNumber(), block.getHash());
        final var blockMedianTime = block.getMedianTime();
        final var blSignature = block.getSignature().toString();
        final var blHash = block.getHash();

        if (block.getNumber().equals(0)) {

            blockChainParams.accept(block.getParameters());
        }


        block.getExcluded().forEach(excluded ->
                localI.add(new IINDEX("UPDATE",
                        null,
                        excluded.getExcluded(),
                        null,
                        blStamp,
                        false,
                        true,
                        false,
                        null))
        );

        block.getRevoked().forEach(revoked ->
                localM.add(new MINDEX("UPDATE",
                        revoked.revoked(),
                        blStamp,
                        blStamp,
                        "REV",
                        null,
                        null,
                        block.getMedianTime(),
                        revoked.signature(),
                        false,
                        null))
        );

        block.getLeavers().forEach(leaver ->
                localM.add(new MINDEX("UPDATE",
                        leaver.leaver(),
                        leaver.createdOn(),
                        blStamp,
                        "LEAVE",
                        null,
                        null,
                        null,
                        null,
                        true,
                        null))
        );

        block.getActives().forEach(renew -> {
            var median = createdOnBlock(renew.createdOn()).orElse(block).getMedianTime();
            localM.add(new MINDEX("UPDATE",
                    renew.activepk(),
                    renew.createdOn(),
                    blStamp,
                    "RENEW",
                    median + blockChainParams.msValidity,
                    median + blockChainParams.msValidity * 2,
                    null,
                    null,
                    false,
                    block.getMedianTime() + blockChainParams.msWindow

            ));
        });

        for (int ind = 0; ind < block.getIdentities().size(); ind++) {
            var idty = block.getIdentities().get(ind);
            var median = createdOnBlock(idty.createdOn()).orElse(block).getMedianTime();

            //System.out.println("on block "+ block + " applying median "+ median );

            //			System.out.println("    " + reduceI);
            localI.add(new IINDEX("CREATE",
                    idty.pseudo(),
                    idty.pub(),
                    idty.createdOn(),
                    blStamp,
                    true,
                    true,
                    false,
                    idty.signature()));

            localM.add(new MINDEX("CREATE",
                    idty.pub(),
                    idty.createdOn(),
                    blStamp,
                    "JOIN",
                    median + blockChainParams.msValidity,
                    median + blockChainParams.msValidity * 2,
                    null,
                    null,
                    false,
                    block.getMedianTime() + blockChainParams.msPeriod)
            );
        }

        block.getJoiners().stream()
                // FIXME :  specs says :  "Each join whose PUBLIC_KEY does not match a local MINDEX CREATE, PUBLIC_KEY produces 2 new entries"   each joinER?
                .filter(j -> localM.stream().noneMatch(m -> m.op.equals("CREATE") && m.pub.equals(j.pub())))
                .forEach(joiner -> {
                    var median = createdOnBlock(joiner.createdOn()).orElse(block).getMedianTime();

                    localI.add(new IINDEX("UPDATE",
                            joiner.pseudo(),
                            joiner.pub(),
                            joiner.createdOn(),
                            blStamp,
                            true,
                            true,
                            false,
                            joiner.signature()));

                    localM.add(new MINDEX("UPDATE",
                            joiner.pub(),
                            joiner.createdOn(),
                            blStamp,
                            "JOIN",
                            median + blockChainParams.msValidity,
                            median + blockChainParams.msValidity * 2,
                            null,
                            null,
                            false,
                            block.getMedianTime() + blockChainParams.msPeriod)
                    );

                });


        block.getCertifications().forEach(cert -> {

            var createOn = createdOnBlock(cert.getBlockNumber()).orElse(block);


            localC.add(new CINDEX("CREATE",
                    cert.certifier(),
                    cert.certified(),
                    cert.getBlockNumber(),
                    blStamp,
                    cert.signature(),
                    createOn.getMedianTime() + blockChainParams.sigValidity,
                    block.getMedianTime() + blockChainParams.sigPeriod,
                    0L
            ).putCreatedOn(createOn));
        });

        for (int indTx = 0; indTx < block.getTransactions().size(); indTx++) {
            var tx = block.getTransactions().get(indTx);

            final var created_on = tx.getBlockstamp();

            for (int indIn = 0; indIn < tx.getInputs().size(); indIn++) {
                var input = tx.getInputs().get(indIn);

                localS.add(new SINDEX("UPDATE", // op
                        input.getType().equals(TxType.D) ? input.getDsource().toString() : input.getTHash().toString(), // identifier
                        input.getType().equals(TxType.D) ? 1 : indIn, // pos
                        created_on, // created_on
                        blStamp, // written_on
                        block.getMedianTime(), // written_time
                        input.getAmount(),
                        input.getBase(),
                        (long) tx.getLocktime(),
                        "SIG(" + tx.getIssuers().get(0) + ")",
                        true, // consumed
                        tx.getThash()
                ));
            }

            for (int indIn = 0; indIn < tx.getOutputs().size(); indIn++) {
                var output = tx.getOutputs().get(indIn);
                //				System.out.println("output.base " + output.getBase());

                localS.add(new SINDEX("CREATE",
                        tx.getThash(),
                        indIn,
                        null,
                        blStamp, // written_on
                        block.getMedianTime(), // written_time
                        output.getAmount(), // amount
                        output.getBase(), // unitbase
                        (long) tx.getLocktime(), // locktime
                        output.getOutputCondition(), // conditions
                        false, // consumed
                        tx.getThash()
                ));
            }

        }

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


    default Stream<IINDEX> reduceI(String pub) {
        return indexIGlobal().filter(i -> i.pub.equals(pub));
    }

    default Stream<IINDEX> idtyByUid(String uid) {
        return indexIGlobal().filter(i -> i.uid.equals(uid));
    }


    default Stream<MINDEX> reduceM(String pub) {
        return indexMGlobal().filter(i -> i.pub.equals(pub));
    }


    default Stream<CINDEX> reduceC(String issuer, String receiver) {
        return indexCGlobal().filter(i ->

                (receiver == null || i.receiver.equals(receiver))
                        &&
                        (issuer == null || i.issuer.equals(issuer))
        );
    }

    default Stream<SINDEX> reduceS(String conditions) {
        return indexSGlobal().filter(i -> i.conditions.equals(conditions));
    }

    default Stream<SINDEX> reduceS(String identifier, Integer pos) {
        return indexSGlobal().filter(i -> i.identifier.equals(identifier) && i.pos.equals(pos));
    }


    default void resetLocalIndex() {
        localC.clear();
        localM.clear();
        localI.clear();
        localS.clear();

        consumedS.clear();
        consumedI.clear();
        consumedC.clear();
        consumedM.clear();
    }

    /**
     * Used by BR_G46_prepareAvailableAndCondition,BR_G47_prepareIsLocked,BR_G48_prepareIsTimeLocked
     * <pre>
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     *      INPUT_ENTRIES = LOCAL_SINDEX[op='CREATE',identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
     *
     *      If COUNT(INPUT_ENTRIES) == 0
     *            INPUT_ENTRIES = GLOBAL_SINDEX[identifier=ENTRY.identifier,pos=ENTRY.pos,amount=ENTRY.amount,base=ENTRY.base]
     * </pre>
     *
     * @param entry:
     * @return TX inputs, either local or global
     */
    default List<SINDEX> inputEntries(SINDEX entry) {
        var inputEntries = localS.stream()
                .filter(s -> s.op.equals("CREATE")
                        && s.identifier.equals(entry.identifier)
                        && s.pos.equals(entry.pos)
                        && s.amount == entry.amount
                        && s.base == entry.base)

                .collect(Collectors.toList());

        if (inputEntries.size() == 0) {

            inputEntries = reduceS(entry.identifier, entry.pos)
                    .peek(consumedS::add)
                    .peek(s -> System.out.println("huhu " + s))
                    .collect(Collectors.toList());
        }
        return inputEntries;
    }

    default BINDEX head_1() {
        return IndexB.size() > 0 ? IndexB.get(IndexB.size() - 1) : null;
    }

    /**
     * fetches few last block index
     *
     * @param m _
     * @return _
     */
    default Stream<BINDEX> range(long m) {
        final var bheads = IndexB.stream()
                .filter(h -> h.number >= head_1().number - m + 1)
                .sorted((b1, b2) -> Integer.compare(b2.number, b1.number))
                .collect(Collectors.toList());

        //		bheads.forEach(b -> {
        //			System.out.println(" #B " + IndexB.size() + " range " + m + " : " + b);
        //		});

        return bheads.stream();
    }

    /**
     * @param block to validate
     * @return true or false or perhaps true
     */
    @Transactional

    default boolean validate(Block block, boolean quick) {
        resetLocalIndex();

        final var newHead = new BINDEX();

        newHead.version = (int) block.getVersion();
        newHead.size = block.size();
        newHead.hash = block.getHash();
        newHead.issuer = block.getIssuer();
        newHead.time = block.getTime();
        newHead.powMin = block.getPowMin();

        indexBlock(block, newHead);

        //        ===========    Augment BINDEX    ==============

        if(!quick) BR_G01_setNumber(newHead);

        if(!quick) BR_G02_setPreviousHash(newHead);

        if(!quick) BR_G03_setPreviousIssuer(newHead);

        BR_G04_setIssuersCount(newHead);

        BR_G05_setIssuersFrame(newHead);

        BR_G06_setIssuersFrameVar(newHead);

        BR_G07_setAvgBlockSize(newHead);

        if(!quick) BR_G08_setMedianTime(newHead);

        BR_G09_setDiffNumber(newHead);

        BR_G10_setMembersCount(newHead);

        BR_G11_setUdTime(newHead);

        BR_G12_setUnitBase(newHead);

        if(!quick) BR_G13_setDividend(newHead);

        BR_G14_setUnitBase(newHead);

        BR_G15_setMassAndMassReeval(newHead);

        BR_G16_setSpeed(newHead);

        if(!quick) BR_G17_setPowMin(newHead);

        if(!quick) BR_G18_setPowZero(newHead);

        BR_G99_setCurrency(newHead);

        BR_G100_issuerIsMember(newHead);


        newHead.currency = block.getCurrency(); // because BR_G99_setCurrency set it to null

        for (final var entry : localC) {
            BR_G37_age(newHead, entry);
            BR_G38_CertUnchainable(newHead, entry);
            BR_G39_CertStock(entry);
            BR_G40_CertFromMember(entry);
            BR_G41_CertToMember(entry);
            BR_G42_CertToNewCommer(entry);
            BR_G43_ToLeaver(entry);
            BR_G44_IsReplay(entry);
            BR_G45_sigOK(entry);
            BR_G105_CertificationExpiryDateCorrection(entry);
        }

        for (final var entry : localI) {
            BR_G19_setAge(newHead, entry);
            BR_G20_UidUnicity(entry);
            BR_G21_pubkeyUnicity(entry);
            BR_G33_excludedIsMember(entry);
            BR_G35_isBeingKicked(entry);
            BR_G36_hasToBeExcluded(entry);
        }

        for (final var entry : localM) {
            BR_G22_setAge(newHead, entry);
            BR_G23_numberFollowing(entry);
            BR_G24_distanceOK(newHead, entry);
            BR_G25_onRevoked(entry);
            BR_G26_joinsTwice(entry);
            BR_G27_enoughCerts(entry);
            BR_G28_leaverIsMember(entry);
            BR_G29_activeIsMember(entry);
            BR_G30_revokedIsMember(entry);
            BR_G31_alreadyRevoked(entry);
            BR_G32_revocationSigOK(entry);
            BR_G34_isBeingRevoked(entry);
            BR_G104_MembershipExpiryDateCorrection(entry);
            BR_G107_unchainableM(entry);
        }

        for (final SINDEX entry : localS) {
            if (entry.op.equals("CREATE")) {

                inputEntries(entry).stream()
                        .forEach(input -> {
                            BR_G46_prepareAvailableAndCondition(entry, input);
                            BR_G47_prepareIsLocked(entry, input);
                            BR_G48_prepareIsTimeLocked(entry, input);
                        });
            }

            if (entry.op.equals("UPDATE")) {
                BR_G102_setAge(entry, newHead);
            }
        }

        augmentLocal(newHead, block);


        final var bstamp = new BStamp(block.getNumber(), block.getHash());
        BR_G91_Dividend(newHead, bstamp);

        BR_G92_CertificationExpiry(newHead);

        BR_G93_MembershipExpiry(newHead);

        BR_G96_ImplicitRevocation(newHead);

        BR_G106_LowAccounts(newHead, bstamp);

        final boolean commit = BR_G97_TestIndex(newHead, block, false);

        if (commit)
            trimIndexes();

        return commit;
    }

    default void revertTo(Integer blockNumber) {

    }

}
