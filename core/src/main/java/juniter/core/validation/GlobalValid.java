package juniter.core.validation;

import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.*;
import juniter.core.model.dbo.tx.TxType;
import juniter.core.model.dbo.wot.Revoked;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


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
 * 	  * NUMBER get the number part of signed
 * 	  * HASH get the hash part of signed
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

    Logger LOG = LogManager.getLogger(GlobalValid.class);

    String INIT_HASH = "0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855";

    interface Conf {
        int NB_DIGITS_UD = 4;
        long txWindow = 604800; // 3600 * 24 * 7
        int forksize = 100;

    }

    Optional<DBBlock> createdOnBlock(BStamp bstamp);

    Optional<DBBlock> createdOnBlock(Integer number);

    List<BINDEX> IndexB = new ArrayList<>();

    Set<CINDEX> localC = new TreeSet<>();

    Set<IINDEX> localI = new HashSet<>();

    Set<MINDEX> localM = new TreeSet<>();

    Set<SINDEX> localS = new TreeSet<>();

    AtomicInteger wotbidIncrement = new AtomicInteger(0);

    ChainParameters conf = new ChainParameters();

    default int bIndexSize() {
        Optional<BINDEX> tail = IndexB.stream().min(Comparator.comparingInt(BINDEX::getNumber));
        //BINDEX head = IndexB.stream().max(Comparator.comparingInt(b -> b.number)).orElseThrow();

        return Stream.of(
                conf.getMedianTimeBlocks(),
                conf.getDtDiffEval(),
                tail.map(t -> t.getIssuersCount().longValue()).orElse(0L),
                tail.map(t -> t.getIssuersFrame().longValue()).orElse(0L))
                .max(Comparator.naturalOrder())
                .get().intValue();
    }


    void trimGlobal(BINDEX head, int bIndexSize);

    void trimSandbox(DBBlock block);

    private boolean trimAndCleanup(BINDEX head, DBBlock block) {
        int bIndexSize = bIndexSize() + Conf.forksize;
        trimLocal(head, bIndexSize);
        trimGlobal(head, bIndexSize);

        trimSandbox(block);
        return true;

    }

    private void trimLocal(BINDEX head, int bIndexSize) {
        if (bIndexSize < IndexB.size())
            IndexB.removeIf(b -> b.getNumber() <= head.getNumber() - bIndexSize);
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
        head.setNumber(head_1() == null ? 0 : (head_1().getNumber() + 1));
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

        if (head.getNumber() > 0) {
            head.setPreviousHash(head_1().getHash());
            assert head.getPreviousHash() != null : "BR_G02_setPreviousHash - set previousHash - Previous Hash NULL for " + head;
        } else {
            head.setPreviousHash(null);
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
        if (head.getNumber() > 0) {
            head.setPreviousIssuer(head_1().getIssuer());
            assert head.getPreviousIssuer() != null : "BR_G03_setPreviousIssuer - set previousIssuer - Previous Hash null";
        } else {
            head.setPreviousIssuer(null);
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
    default void BR_G04_setIssuersCount(BINDEX head) {
        if (head.getNumber() == 0) {
            head.setIssuersCount(0);
        } else {
            assert head_1() != null : "BR_G04_setIssuersCount head_1 is null at " + head;
            head.setIssuersCount((int) range(head_1().getIssuersFrame()).map(BINDEX::getIssuer).distinct().count());
            assert head.getIssuersCount() > 0 : "BR_G04_setIssuersCount - issuersCount !> 0 ";
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

        if (head.getNumber() == 0) {
            head.setIssuersFrame(1);
        } else if (head_1().getIssuersFrameVar() > 0) {
            head.setIssuersFrame(head_1().getIssuersFrame() + 1);
        } else if (head_1().getIssuersFrameVar() < 0) {
            head.setIssuersFrame(head_1().getIssuersFrame() - 1);
        } else {
            head.setIssuersFrame(head_1().getIssuersFrame());
        }
        assert head.getIssuersFrame() != null : "BR_G05_setIssuersFrame - set issuersFrame - issuersFrame is null";
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
        if (head.getNumber() == 0) {
            head.setIssuersFrameVar(0);
        } else {
            final var delta = head.getIssuersCount() - head_1().getIssuersCount();

            if (head_1().getIssuersFrameVar() > 0) {
                head.setIssuersFrameVar(head_1().getIssuersFrameVar() + 5 * delta - 1);
            } else if (head_1().getIssuersFrameVar() < 0) {
                head.setIssuersFrameVar(head_1().getIssuersFrameVar() + 5 * delta + 1);
            } else {
                head.setIssuersFrameVar(head_1().getIssuersFrameVar() + 5 * delta);
            }
        }

    }

    /**
     * <pre>
     * BR_G07_setAvgBlockSize - HEAD.avgBlockSize
     *
     * HEAD.avgBlockSize = AVG((HEAD~1..<HEAD.issuersCount>).getSize)
     * </pre>
     */
    private void BR_G07_setAvgBlockSize(BINDEX head) {
        assert head != null : "BR_G07_setAvgBlockSize - HEAD.avgBlockSize - head is null  ";

        if (head.getNumber() == 0) {
            head.setAvgBlockSize(0);
        } else {
            assert head.getIssuersCount() > 0 : "BR_G07_setAvgBlockSize - HEAD.avgBlockSize - issuersCount 0";
            head.setAvgBlockSize((int) range(head.getIssuersCount())
                    .mapToInt(BINDEX::getSize)
                    .average()
                    .orElse(0.0));
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
        final var min = Math.min(conf.getMedianTimeBlocks(), head.getNumber());

         if (head.getNumber() > 0) {

            head.setMedianTime((long) range(min) // fetchTrimmed bindices
                    .mapToLong(BINDEX::getTime)
                    .average().orElse(Double.NaN));
             head.setMedianTime(Math.max(head.getMedianTime(), head_1().getMedianTime())); // found in code, not in the spec
            // used at block 3
        } else {

            head.setMedianTime(head.getTime());
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

        if (head.getNumber() == 0) {
            head.setDiffTime(head.getNumber() + conf.getDtDiffEval());

        } else if (head_1().diffNumber <= head.getNumber()) {
            head.setDiffTime(head_1().getDiffTime() + conf.getDtDiffEval());

        } else {
            head.setDiffTime(head_1().getTime());
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
        final int member = (int) localI.stream()
                .filter(IINDEX::getMember).count();
        final int notMember = (int) localI.stream().filter(m -> !m.getMember()).count();

         if (head.getNumber() == 0) {
            head.setMembersCount(member);
        } else {
            head.setMembersCount(head_1().getMembersCount() + member - notMember);
        }

        assert head.getMembersCount() > 0 : "BR_G10_setMembersCount : " + head.getMembersCount() + " = " + head_1().getMembersCount() + " + " + member + " - " + notMember + " \n " + head_1() + "\n" + head;

    }

    /**
     * <pre>
     * BR_G100_setIssuerIsMember - HEAD.issuerIsMember
     *
     * If HEAD.number > 0
     *     HEAD.issuerIsMember = REDUCE(GLOBAL_IINDEX[pub=HEAD.issuer]).member
     * Else
     *     HEAD.issuerIsMember = REDUCE(LOCAL_IINDEX[pub=HEAD.issuer]).member
     * </pre>
     */
    private void BR_G100_setIssuerIsMember(BINDEX head) {
        if (head.getNumber() == 0) {
            head.setIssuerIsMember(localI.stream()
                    .anyMatch(i -> head.getIssuer().equals(i.getPub()) && i.getMember()));

        } else {
            head.setIssuerIsMember(reduceI(head.getIssuer()).map(IINDEX::getMember).orElse(false));
        }
    }

    /**
     * <pre>
     * BR_G101_ruleIssuerIsMember - Issuer
     *
     * Rule:
     *
     * HEAD.issuerIsMember == true
     *
     * </pre>
     */
    private boolean BR_G101_ruleIssuerIsMember(BINDEX head) {
        assert head.getIssuerIsMember() : "BR_G101_ruleIssuerIsMember - ? " + head.getIssuerIsMember() + " at " + head;
        return head.getIssuerIsMember();
    }

    /**
     * <pre>
     * BR_G102_setSourceAge - ENTRY.age
     *
     * For each ENTRY in local SINDEX where op = 'UPDATE':
     *
     *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.hash)>[hash=HASH(ENTRY.signed)]
     *
     *     If HEAD.number == 0 && ENTRY.signed == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
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
    private void BR_G102_setSourceAge(SINDEX entry, BINDEX head) {
        if (head.getNumber() == 0
                && INIT_HASH.equals(entry.getSigned() + "")) {
            entry.setAge(0);
        } else {
            entry.setAge(createdOnBlock(entry.getSigned())
                    .map(refBlock -> head_1().getMedianTime() - refBlock.getMedianTime())
                    .orElse(Conf.txWindow + 1));

        }

    }


    /**
     * <pre>
     * BR_G103_ruleTransactionWritable
     *
     * Rule:
     *
     * ENTRY.age <= [txWindow]
     */
    private boolean BR_G103_ruleTransactionWritable(SINDEX s) {
        assert s.getAge() <= Conf.txWindow : "BR_G103_ruleTransactionWritable - " + s.getAge() + " <= " + Conf.txWindow + " for " + s;
        return s.getAge() <= Conf.txWindow;
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
    private void BR_G104_MembershipExpiryDateCorrection(BINDEX head) {
        localM.forEach(ms -> {

            if ("JOIN".equals(ms.getType()) || "RENEW".equals(ms.getType())) {

                long basedTime = head.getNumber() == 0 ? head.getMedianTime() : createdOnBlock(ms.getSigned()).map(DBBlock::getMedianTime).orElse(0L);

                if (ms.getExpires_on() == null) {
                    ms.setExpires_on(0L);
                }
                if (ms.getRevokes_on() == null) {
                    ms.setRevokes_on(0L);
                }
                ms.setExpires_on(ms.getExpires_on() + basedTime);
                ms.setRevokes_on(ms.getRevokes_on() + basedTime);
            }
        });
    }

    /**
     * <pre> //FIXME spec incomplete
     * BR_G105_CertificationExpiryDateCorrection
     *
     * For each LOCAL_CINDEX as CERT:
     *
     * CERT.expires_on = CERT.expires_on - CERT.age
     */
    private void BR_G105_CertificationExpiryDateCorrection(BINDEX head) {
        localC.forEach(c -> {
            if ("CREATE".equals(c.getOp())) {

                long basedTime = head.getNumber() == 0 ? head.getMedianTime() : c.getCreated_on().getMedianTime();
                c.setExpires_on(c.getExpires_on() + basedTime);
            }

        });
    }

    /**
     * <pre>
     * BR_G106_IndexLowAccounts - Low accounts
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
     *  written = BLOCKSTAMP
     * written_time = MedianTime
     *    consumed = true )
     * </pre>
     */
    private void BR_G106_IndexLowAccounts(BINDEX head, BStamp block) {
        if (head.getNumber() == 0)
            return;

        lowAccounts().forEach(account -> {

            LOG.info("BR_G106_IndexLowAccounts " + account);
            sourcesByConditions(account.conditions)
                    .collect(groupingBy(SINDEX::getIdentifier, groupingBy(SINDEX::getPos)))
                    .forEach((id, l) -> l.forEach((pos, ll) -> {

                        var src = ll.get(0);
                        if (ll.size() == 1 && "CREATE".equals(src.getOp())) {

                            var lowAccount = new SINDEX("UPDATE",
                                    src.getIdentifier(),
                                    src.getPos(),
                                    null,
                                    block,
                                    src.getAmount(),
                                    src.getBase(),
                                    null,
                                    src.getConditions(),
                                    true,
                                    src.getTx()
                            );

                            localS.add(lowAccount);
                            LOG.info("BR_G106_IndexLowAccounts comitting " + src);

                            commit(null, Set.of(), Set.of(), Set.of(), Set.of(lowAccount));

                        }
                    }));

        });
    }

    /**
     * <pre>
     * BR_G107_setUnchainableM
     *
     * If HEAD.number > 0 AND ENTRY.revocation == null:
     *
     * ENTRY.unchainables = COUNT(GLOBAL_MINDEX[issuer=ENTRY.issuer, chainable_on >
     * HEAD~1.medianTime]))
     */
    private void BR_G107_setUnchainableM(MINDEX entry) {

        entry.unchainables = indexMGlobal()
                .filter(m -> m.getChainable_on() != null && m.getChainable_on() > head_1().getMedianTime())
                .count();
    }

    /**
     * <pre>
     * BR_G108_ruleMembershipPeriod
     *
     * Rule:
     *
     * ENTRY.unchainables == 0
     */
    private void BR_G108_ruleMembershipPeriod(CINDEX m) {
        assert m.getUnchainables() == 0
                : "BR_G108_ruleMembershipPeriod " + m.getUnchainables() + " != 0 at " + m;
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
    default void BR_G11_setUdTime(BINDEX head) {

        if (head.getNumber() == 0) {
            head.setUdTime(conf.getUdTime0());
            head.udReevalTime = conf.getUdReevalTime0();
        } else {

            if (head_1().getUdTime() <= head.getMedianTime()) {
                head.setUdTime(head_1().getUdTime() + conf.getDt());
            } else {
                head.setUdTime(head_1().getUdTime());
            }

            if (head_1().udReevalTime <= head.getMedianTime()) {
                head.udReevalTime = head_1().udReevalTime + conf.getDtReeval();
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
    default void BR_G12_setUnitBase(BINDEX head) {
        if (head.getNumber() == 0) {
            head.setUnitBase(0);
        } else {
            head.setUnitBase(head_1().getUnitBase());
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
     * head
     * If HEAD.number == 0
     *     HEAD.new_dividend = null
     * Else If HEAD.udTime != HEAD~1.udTime
     *     HEAD.new_dividend = HEAD.dividend
     * Else
     *     HEAD.new_dividend = null
     * </pre>
     */
    default void BR_G13_setDividend(BINDEX head) {
        if (head.getNumber() == 0) {
            head.setDividend((int) conf.getUd0());
        } else if (!head.udReevalTime.equals(head_1().udReevalTime)) {

            var moneyShare = Math.ceil(head_1().getMassReeval() / Math.pow(10, head_1().getUnitBase())) / head.getMembersCount();

            head.setDividend((int) Math.ceil(head_1().getDividend() + Math.pow(conf.getC(), 2) * moneyShare
                    / (conf.getDtReeval() * 1. / conf.getDt())
            ));

            LOG.info("head.dividend " + head.getDividend() + "from " + head_1().getDividend() + " + " + Math.pow(conf.getC(), 2) + " * "
                    + head_1().getMassReeval() + " / " + head.getMembersCount() + " / " + (conf.getDtReeval() * 1. / conf.getDt()));
        } else {
            head.setDividend(head_1().getDividend());
        }

        if (head.getNumber() == 0) {
            head.new_dividend = null;
        } else if (!Objects.equals(head.getUdTime(), head_1().getUdTime())) {
            head.new_dividend = head.getDividend();
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

        if (head.getDividend() >= Math.pow(10, Conf.NB_DIGITS_UD)) {
            head.setDividend((int) Math.round(Math.ceil(1.0 * head.getDividend() / 10)));
            head.new_dividend = head.getDividend();
            head.setUnitBase(head.getUnitBase() + 1);
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
        if (head.getNumber() == 0) {
            head.setMass(0L);
            head.setMassReeval(0L);
        } else {

            if (!head.getUdTime().equals(head_1().getUdTime())) {
                head.setMass((long) (head_1().getMass() + head.getDividend() * Math.pow(10, head.getUnitBase()) * head.getMembersCount()));
            } else {
                head.setMass(head_1().getMass());
            }

            if (!head.udReevalTime.equals(head_1().udReevalTime)) {
                head.setMassReeval(head_1().getMass());
            } else {
                head.setMassReeval(head_1().getMassReeval());
            }
        }

    }

    /**
     * <pre>
     * BR_G16_setSpeed - HEAD.speed
     *
     * If HEAD.number == 0
     *     speed = 0
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

        if (head.getNumber() == 0) {
            head.setSpeed(0L);
        } else {
            final var range = Math.min(dtDiffEval, head.getNumber());
            final var elapsed = head.getMedianTime() - IndexB.get(range).getMedianTime();

            if (elapsed == 0) {
                head.setSpeed(100L);
            } else {
                head.setSpeed(range / elapsed);
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
        if (head.getNumber() == 0)
            return;

        //		LOG.info("BR_G17_setPowMin - " + head.diffNumber + "!=" + head_1().diffNumber);
        //		LOG.info(head.speed + " >= " + conf.maxSpeed() + head_1().powMin);

        if (head.diffNumber != head_1().diffNumber) {

            if (head.getSpeed() >= conf.maxSpeed()) { // too fast, increase difficulty
                if ((head_1().getPowMin() + 2) % 16 == 0) {
                    head.setPowMin(head_1().getPowMin() + 2);
                } else {
                    head.setPowMin(head_1().getPowMin() + 1);
                }
            }

            if (head.getSpeed() <= conf.minSpeed()) { // too slow, increase difficulty
                if (head_1().getPowMin() % 16 == 0) {
                    head.setPowMin(Math.max(0, head_1().getPowMin() - 2));
                } else {
                    head.setPowMin(Math.max(0, head_1().getPowMin() - 1));
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
        if (head.getNumber() != 0) {
            final var blocksOfIssuer = range(head.getIssuersFrame())
                    .filter(i -> i.getIssuer().equals(head.getIssuer()))
                    .collect(toList());
            nbPersonalBlocksInFrame = blocksOfIssuer.size();
            final Object blocksPerIssuerInFrame = null;

            if (nbPersonalBlocksInFrame != 0) {
                final var first = blocksOfIssuer.get(0);
                nbPreviousIssuers = first.getIssuersCount();
                nbBlocksSince = head_1().getNumber() - first.getNumber();
            }

        }

        final var personalExcess = Math.max(0, 1);
        final var personalHandicap = Math.floor(Math.log(1 + personalExcess) / Math.log(1.189));

        head.setIssuerDiff((int) Math.max(head.getPowMin(),
                head.getPowMin() * Math.floor(conf.getPercentRot() * nbPreviousIssuers / (1 + nbBlocksSince))));

        if ((head.getIssuerDiff() + 1) % 16 == 0) {
            head.setIssuerDiff(head.getIssuerDiff() + 1);
        }

        head.powRemainder = head.getIssuerDiff() % 16;
        head.powZeros = (head.getIssuerDiff() - head.powRemainder) / 16;
    }

    /**
     * <pre>
     * Local IINDEX augmentation
     *
     *
     * For each ENTRY in local IINDEX where op = 'CREATE':
     *
     *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.signed)>[hash=HASH(ENTRY.signed)]
     *
     *     If HEAD.number == 0 && ENTRY.signed == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
     *         ENTRY.age = 0
     *     Else if REF_BLOC != null:
     *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
     *     Else:
     *         ENTRY.age = conf.idtyWindow + 1
     * </pre>
     */
    private void BR_G19_setAge(BINDEX head, IINDEX entry) {

        if ("CREATE".equals(entry.getOp())) {
            if (head.getNumber() == 0 && INIT_HASH.equals(entry.getSigned().toString())) {
                entry.setAge(0);
            } else {

                entry.setAge(createdOnBlock(entry.getSigned())
                        .map(block -> head.getMedianTime() - block.getMedianTime())
                        .orElseGet(() -> conf.getIdtyWindow() + 1));
            }
        }

    }

    /**
     * <pre>
     *     Identity UserID unicity
     *
     * For each ENTRY in local IINDEX:
     *
     *     If op = 'CREATE':
     *         ENTRY.uidUnique = COUNT(GLOBAL_IINDEX[uid=ENTRY.uid) == 0
     *     Else:
     *         ENTRY.uidUnique = true
     * </pre>
     */
    private void BR_G20_setUidUnicity(IINDEX entry) {

        if ("CREATE".equals(entry.getOp())) {
            entry.setUidUnique(idtyByUid(entry.getUid()).count() == 0);
        } else {
            entry.setUidUnique(true);
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
    private void BR_G21_setPubkeyUnicity(IINDEX entry) {
        if ("CREATE".equals(entry.getOp())) {
            entry.setPubUnique(idtyByPubkey(entry.getPub()).count() == 0);
        } else {
            entry.setPubUnique(true);
        }

    }


    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where revoked == null
     *
     *     REF_BLOCK = HEAD~<HEAD~1.number + 1 - NUMBER(ENTRY.signed)>[hash=HASH(ENTRY.signed)]
     *
     *     If HEAD.number == 0 && ENTRY.signed == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
     *         ENTRY.age = 0
     *     Else if REF_BLOC != null
     *         ENTRY.age = HEAD~1.medianTime - REF_BLOCK.medianTime
     *     Else:
     *         ENTRY.age = conf.msWindow + 1
     * </pre>
     */
    private void BR_G22_setAge(BINDEX head, MINDEX entry) {

        if (entry.getRevoked() == null) {

            if (head.getNumber() == 0 && INIT_HASH.equals(entry.getSigned().toString())) {
                entry.age = 0;
            } else {
                entry.age = createdOnBlock(entry.getSigned())
                        .map(ref -> head_1().getMedianTime() - ref.getMedianTime())
                        .orElse(conf.getMsWindow() + 1);
            }
        }
    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where revoked == null
     *
     *     signed = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).signed
     *
     *     If signed != null:
     *         ENTRY.numberFollowing = NUMBER(ENTRY.created_ON) > NUMBER(signed)
     *     Else:
     *         ENTRY.numberFollowing = true EndIf
     *
     *
     * For each ENTRY in local MINDEX where revoked != null
     *     ENTRY.numberFollowing = true
     * </pre>
     */
    private void BR_G23_setNumberFollowing(MINDEX entry) {
        if (entry.getRevoked() == null) {

            var createdOn = reduceM(entry.getPub()).map(MINDEX::getSigned);

            entry.numberFollowing = createdOn
                    .map(bStamp -> entry.getSigned().getNumber() > bStamp.getNumber())
                    .orElse(true);

        } else { // if revoked exists
            entry.numberFollowing = true;
        }
    }

    /**
     * <pre>
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
    private void BR_G24_setDistanceOK(BINDEX head, MINDEX entry) {
        if ("JOIN".equals(entry.getType()) || "RENEW".equals(entry.getType())) {
            final var dSen = Math.ceil(Math.pow(head.getMembersCount(), 1.0 / conf.getStepMax()));
            final var graph = Stream.concat(localC.stream(), indexCGlobal()).collect(toList());

            final Set<CINDEX> sentries = new TreeSet<>();

            // This probably should be rewritten and trashed but lets measure it first
            final var reachedNode = graph.stream().filter(e -> entry.getPub().equals(e.getIssuer()))
                    .map(edges -> graph.stream().filter(p1 -> edges.getIssuer().equals(p1.getReceiver())))
                    .map(stEdges -> graph.stream().filter(p2 -> stEdges.anyMatch(e -> e.getIssuer().equals(p2.getReceiver()))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p3 -> stEdges.anyMatch(e -> e.getIssuer().equals(p3.getReceiver()))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p4 -> stEdges.anyMatch(e -> e.getIssuer().equals(p4.getReceiver()))))
                    .distinct()
                    .map(stEdges -> graph.stream().filter(p5 -> stEdges.anyMatch(e -> e.getIssuer().equals(p5.getReceiver()))))
                    .distinct().collect(toList());

            entry.distanceOK = reachedNode.containsAll(sentries);

        } else {
            entry.distanceOK = true;
        }
        entry.distanceOK = true; // FIXME
    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX:
     *     ENTRY.onRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked != null
     * </pre>
     */
    private void BR_G25_setOnRevoked(MINDEX entry) {
        entry.onRevoked = reduceM(entry.getPub())
                .map(m -> m.getRevoked() != null)
                .orElse (false);
    }

    /**
     * <pre>
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
    private void BR_G26_setJoinsTwice(MINDEX entry) {
        if ("UPDATE".equals(entry.getOp()) && entry.getExpired_on() != null && entry.getExpired_on().equals(0L)) {
            entry.joinsTwice = reduceI(entry.getPub()).map(IINDEX::getMember).orElse(false);
        }
    }

    /**
     * <pre>
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
    private void BR_G27_setEnoughCerts(MINDEX entry) {

        if (entry.getType().equals("JOIN") || entry.getType().equals("RENEW")) {
            final var cntG = getC(null, entry.getPub()).filter(c -> c.getExpired_on() == 0L).count();
            final var cntL = localC.stream().filter(c -> c.getReceiver().equals(entry.getPub()) && c.getExpired_on() == 0).count();

            entry.enoughCerts = cntG + cntL >= conf.getSigQty();
        } else {
            entry.enoughCerts = true;
        }

        assert entry.enoughCerts : "BR_G27_setEnoughCerts - not enough Certification for " + entry.getPub();
    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where type == 'LEAVE':
     *     ENTRY.leaverIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     *
     * For each ENTRY in local MINDEX where type != 'LEAVE':
     *     ENTRY.leaverIsMember = true
     * </pre>
     */
    private void BR_G28_setLeaverIsMember(MINDEX entry) {
        if (entry.getType().equals("LEAVE")) {
            entry.leaverIsMember = reduceI(entry.getPub()).map(IINDEX::getMember).orElse(false);
        } else {
            entry.leaverIsMember = true;
        }

    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where type == 'RENEW'
     *     ENTRY.activeIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     *
     * For each ENTRY in local MINDEX where type != 'RENEW':
     *     ENTRY.activeIsMember = true
     * </pre>
     */
    private void BR_G29_setActiveIsMember(MINDEX entry) {
        if ("RENEW".equals(entry.getType())) {
            entry.activeIsMember = reduceI(entry.getPub()).map(IINDEX::getMember).orElse(false);
        } else {
            entry.activeIsMember = true;
        }

    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where revoked == null:
     *     ENTRY.revokedIsMember = true
     *
     * For each ENTRY in local MINDEX where revoked != null:
     *     ENTRY.revokedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     * </pre>
     */
    private void BR_G30_setRevokedIsMember(MINDEX entry) {
        if (entry.getRevoked() != null) {
            entry.revokedIsMember = reduceI(entry.getPub()).map(IINDEX::getMember).orElse(false);
        } else {
            entry.revokedIsMember = true;
        }
    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where revoked == null:
     *     ENTRY.alreadyRevoked = false
     *
     * For each ENTRY in local MINDEX where revoked != null:
     *     ENTRY.alreadyRevoked = REDUCE(GLOBAL_MINDEX[pub=ENTRY.pub]).revoked != null
     * </pre>
     */
    private void BR_G31_setAlreadyRevoked(MINDEX entry) {
        if (entry.getRevoked() != null) {
            entry.alreadyRevoked = reduceM(entry.getPub()).filter(m -> m.getRevoked() != null).isPresent();
        } else {
            entry.alreadyRevoked = false;
        }
    }

    /**
     * <pre>
     *
     * For each ENTRY in local MINDEX where revoked == null:
     *     ENTRY.revocationSigOK = true
     *
     * For each ENTRY in local MINDEX where revoked != null:
     *     ENTRY.revocationSigOK = SIG_CHECK_REVOKE(REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]), ENTRY)
     * </pre>
     */
    private void BR_G32_setRevocationSigOK(MINDEX entry) {
        if (entry.getRevoked() != null) {
            var reducedM = reduceM(entry.getPub()).orElseThrow();
            var reducedI = reduceI(entry.getPub()).orElseThrow();
            var revoked = new Revoked();
            revoked.setPubkey(reducedM.getPub());
            revoked.setRevocation(reducedM.getRevocation());
            revoked.setI_block_uid(reducedI.getWritten().stamp());
            revoked.setSignature(reducedI.getSig());
            revoked.setUid(reducedI.getUid());
            entry.revocationSigOK = Crypto.verify(revoked.toDoc(false), reducedM.getRevocation(), reducedM.getPub()) || true;
        } else {
            entry.revocationSigOK = true;
        }
    }

    /**
     * <pre>
     *
     * For each ENTRY in local IINDEX where member != false:
     *     ENTRY.excludedIsMember = true
     *
     * For each ENTRY in local IINDEX where member == false:
     *     ENTRY.excludedIsMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).member
     * </pre>
     */
    private void BR_G33_setExcludedIsMember(IINDEX entry) {
        if (entry.getMember()) {
            entry.setExcludedIsMember(true);
        } else {
            entry.setExcludedIsMember(reduceI(entry.getPub()).map(IINDEX::getMember).orElse(false));
        }
    }

    /**
     * <pre>
     * BR_G34_setIsBeingRevoked
     *
     * For each ENTRY in local MINDEX where revoked == null:
     *     ENTRY.isBeingRevoked = false
     * For each ENTRY in local MINDEX where revoked != null:
     *     ENTRY.isBeingRevoked = true
     * </pre>
     */
    private void BR_G34_setIsBeingRevoked(MINDEX entry) {
        entry.isBeingRevoked = entry.getRevoked() != null;

    }

    /**
     * <pre>
     *
     * For each ENTRY in local IINDEX where member != false:
     *     ENTRY.isBeingKicked = false
     * For each ENTRY in local IINDEX where member == false:
     *     ENTRY.isBeingKicked = true
     * </pre>
     */
    private void BR_G35_setIsBeingKicked(IINDEX entry) {
        entry.setBeingKicked(!entry.getMember());

    }

    /**
     * <pre>
     * BR_G36_setHasToBeExcluded
     *
     * For each ENTRY in local IINDEX:
     *     ENTRY.hasToBeExcluded = REDUCE(GLOBAL_IINDEX[pub=ENTRY.pub]).kick
     *
     * </pre>
     */
    private void BR_G36_setHasToBeExcluded(IINDEX entry) {
        entry.setHasToBeExcluded(reduceI(entry.getPub()).map(IINDEX::getKick).orElse(false));

    }

    /**
     * <pre>
     *
     * REF_BLOCK = HEAD~<HEAD~1.number + 1 -  NUMBER(ENTRY.signed)>[hash=HASH(ENTRY.signed)]
     *
     * If HEAD.number == 0 && ENTRY.signed == '0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855':
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
    private void BR_G37_setAge(BINDEX head, CINDEX entry) {

        final long sigWindow = 0;

        if (head.getNumber() == 0 && INIT_HASH.equals(entry.getCreated_on().toString())) {
            entry.setAge(0);
        } else {
            entry.setAge(IndexB.stream()
                    .filter(b -> b.getHash().equals(entry.getCreated_on().getHash()) &&
                            b.getNumber().equals(entry.getCreated_on().getNumber()))
                    .findAny()
                    .map(refb -> head_1().getMedianTime() - refb.getMedianTime())
                    .orElse(sigWindow + 1));
        }

    }

    /**
     * <pre>
     *
     * If HEAD.number > 0:
     *     ENTRY.unchainables = COUNT(GLOBAL_CINDEX[issuer=ENTRY.issuer, chainable_on > HEAD~1.medianTime]))
     * </pre>
     */
    private void BR_G38_setCertUnchainable(BINDEX head, CINDEX entry) {
        if (head.getNumber() > 0) {

            var h1 = head_1();
            assert h1 != null : "BR_G38_setCertUnchainable HEAD-1 should NOT be null";
            assert h1.getMedianTime() != null : "BR_G38_setCertUnchainable HEAD-1 median time should NOT be null";

            entry.setUnchainables(getC(entry.getIssuer(), null)
                    .filter(c -> c.getChainable_on() != null)
                    .filter(c -> {

                        assert c.getChainable_on() != null : "BR_G38_setCertUnchainable  chainable_on null " + c;
                        return c.getChainable_on() > h1.getMedianTime();
                    })
                    .count());
        }

    }

    /**
     * <pre>
     *
     * ENTRY.stock = COUNT(REDUCE_BY(GLOBAL_CINDEX[issuer=ENTRY.issuer], 'receiver', 'signed')[expired_on=0])
     *
     * </pre>
     */
    private void BR_G39_setCertStock(CINDEX entry, BINDEX head) {
        entry.setStock(certStock(entry.getIssuer(), head.getMedianTime() ));
    }

    /**
     * <pre>
     *
     * ENTRY.fromMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.issuer]).member
     * </pre>
     */
    private void BR_G40_setCertFromMember(CINDEX entry) {
        entry.setFromMember(reduceI(entry.getIssuer()).map(IINDEX::getMember).orElse(false));
    }

    /**
     * <pre>
     *
     * ENTRY.toMember = REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]).member
     *
     * </pre>
     */
    private void BR_G41_setCertToMember(CINDEX entry) {
        entry.setToMember(reduceI(entry.getReceiver()).map(IINDEX::getMember).orElse(false));

    }

    /**
     * <pre>
     *
     * ENTRY.toNewcomer = COUNT(LOCAL_IINDEX[member=true,pub=ENTRY.receiver]) > 0
     * </pre>
     */
    private void BR_G42_setCertToNewCommer(CINDEX entry) {
        entry.setToNewcomer(localI.stream().anyMatch(i -> entry.getReceiver().equals(i.getPub()) && i.getMember()));

    }

    /**
     * <pre>
     *
     * ENTRY.toLeaver = REDUCE(GLOBAL_MINDEX[pub=ENTRY.receiver]).leaving
     * </pre>
     */
    private void BR_G43_setToLeaver(CINDEX entry) {
        entry.setToLeaver(reduceM(entry.getReceiver())
                .map(i -> Boolean.TRUE.equals(i.getLeaving()))  //damn you cgeek and your quantic boolean states
                .orElse(false));

    }

    /**
     * <pre>
     *
     * reducible = GLOBAL_CINDEX[issuer=ENTRY.issuer,receiver=ENTRY.receiver,expired_on=0]
     *
     * If count(reducible) == 0:
     *     ENTRY.isReplay = false
     * Else:
     *     ENTRY.isReplay = reduce(reducible).expired_on == 0
     * </pre>
     */
    private void BR_G44_setIsReplay(BINDEX head, CINDEX entry) {
        var reducible = getC(entry.getIssuer(), entry.getReceiver())
                .filter(c -> c.getExpired_on() == 0)
                .collect(toList());
        if (reducible.size() == 0) {
            entry.setReplay(false);
        } else {
            entry.setReplay(reducible.stream()
                    .reduce(CINDEX.reducer)
                    .map(c -> c.getExpired_on() == 0)
                    .orElse(false));
        }


        if (head.getNumber() > 0 && head_1().getVersion() > 10) {
            entry.setReplayable(reducible.size() == 0 || reducible.stream().reduce(CINDEX.reducer).orElseThrow().getReplayable_on() < head_1().getMedianTime());
        } else {
            // v10 blocks do not allow cert replay
            entry.setReplayable(false);
        }


    }

    /**
     * <pre>
     *
     * ENTRY.sigOK = SIG_CHECK_CERT(REDUCE(GLOBAL_IINDEX[pub=ENTRY.receiver]), ENTRY)
     *
     * </pre>
     */
    private void BR_G45_setSignatureOK(CINDEX entry) {
        entry.setSigOK(true); // FIXME signature is a local check ??
    }

    /**
     * Local SINDEX augmentation
     *
     * <pre>
     * ENTRY.available and ENTRY.conditions
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.conditions = INPUT.conditions
     *     ENTRY.available = INPUT.consumed == false
     *
     * </pre>
     */
    private void BR_G46_prepareAvailableAndCondition(SINDEX entry, SINDEX input) {
        entry.available = !input.isConsumed();
        entry.conditions = input.conditions;

    }

    /**
     * <pre>
     *
     *     INPUT = REDUCE(INPUT_ENTRIES)
     *     ENTRY.isLocked = TX_SOURCE_UNLOCK(INPUT.conditions, ENTRY)
     * </pre>
     */
    private void BR_G47_prepareIsLocked(SINDEX entry, SINDEX input) {
         entry.setLocked(false); // FIXME BR_G47_prepareIsLocked
    }

    /**
     * <pre>
     *
     * INPUT = REDUCE(INPUT_ENTRIES)
     * ENTRY.isTimeLocked = ENTRY.written_time - INPUT.written_time < ENTRY.locktime
     * </pre>
     */
    private void BR_G48_prepareIsTimeLocked(SINDEX entry, SINDEX input) {
        entry.setTimeLocked(entry.getWritten().getMedianTime() - input.getWritten().getMedianTime() < entry.getLocktime());
    }

    /**
     * <pre>
     *
     *  If `HEAD.number > 0`
     *      HEAD.version == (HEAD~1.version OR HEAD~1.version + 1)
     *
     * </pre>
     */
    private boolean BR_G49_ruleVersion(BINDEX head) {
        assert head.getNumber() == 0 || head.getSize() < Math.max(500, Math.ceil(1.1 * head.getAvgBlockSize()))
                : "BR_G49_ruleVersion at " + head;

        return head.getNumber() == 0 || head.getVersion().equals(head_1().getVersion()) || head.getVersion() == head_1().getVersion() + 1;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * If HEAD.number > 0:
     *     HEAD.getSize < MAX(500 ; CEIL(1.10 * HEAD.avgBlockSize))
     * </pre>
     */
    private boolean BR_G50_ruleBlockSize(BINDEX head) {
        assert head.getNumber() == 0 || head.getSize() < Math.max(500, Math.ceil(1.1 * head.getAvgBlockSize()))
                : "BR_G50_ruleBlockSize at " + head;
        return head.getNumber() == 0 || head.getSize() < Math.max(500, Math.ceil(1.1 * head.getAvgBlockSize()));
    }

    /**
     * <pre>
     *
     * Number = HEAD.number
     * </pre>
     */
    private boolean BR_G51_ruleBlockNumber(BINDEX testHead, DBBlock shouldBe) {
        assert testHead.getNumber().equals(shouldBe.getNumber())
                : "BR_G51_ruleBlockNumber " + testHead.getNumber() + " ==? " + shouldBe.getNumber();
        return testHead.getNumber().equals(shouldBe.getNumber());
    }

    /**
     * <pre>
     *
     * PreviousHash = HEAD.previousHash
     * </pre>
     */
    private boolean BR_G52_rulePreviousHash(BINDEX testHead, DBBlock block) {
        if (testHead.getNumber() == 0)
            return true;

        assert testHead.getPreviousHash().equals(block.getPreviousHash())
                : "BR_G52_rulePreviousHash - rule PreviousHash - " + testHead.getPreviousHash() + " " + block.getPreviousHash() + " at " + testHead;

        return testHead.getPreviousHash().equals(block.getPreviousHash());
    }

    /**
     * <pre>
     *
     * PreviousIssuer = HEAD.previousIssuer
     * </pre>
     */
    private boolean BR_G53_rulePreviousIssuer(BINDEX testHead, DBBlock shouldBe) {
        if (testHead.getNumber() == 0)
            return true;

        assert testHead.getPreviousIssuer().equals(shouldBe.getPreviousIssuer()) : "BR_G53_rulePreviousIssuer - rule PreviousIssuer ";
        return testHead.getPreviousIssuer().equals(shouldBe.getPreviousIssuer());
    }

    /**
     * <pre>
     *
     * DifferentIssuersCount = HEAD.issuersCount
     * </pre>
     */
    private boolean BR_G54_ruleDifferentIssuersCount(BINDEX testHead, DBBlock shouldBe) {
        assert testHead.getIssuersCount().equals(shouldBe.getIssuersCount()) : "BR_G54_ruleDifferentIssuersCount - " + testHead.getIssuersCount() + "-" + shouldBe.getIssuersCount();
        return testHead.getIssuersCount().equals(shouldBe.getIssuersCount());
    }

    /**
     * <pre>
     *
     * IssuersFrame = HEAD.issuersFrame
     * </pre>
     */
    private boolean BR_G55_ruleIssuersFrame(BINDEX head, DBBlock shouldBe) {
        assert head.getIssuersFrame().equals(shouldBe.getIssuersFrame()) : "BR_G55_ruleIssuersFrame - " + head.getIssuersFrame() + " ==? " + shouldBe.getIssuersFrame() + " at " + head;
        return head.getIssuersFrame().equals(shouldBe.getIssuersFrame());
    }

    /**
     * <pre>
     *
     * IssuersFrameVar = HEAD.issuersFrameVar
     * </pre>
     */
    private boolean BR_G56_ruleIssuersFrameVar(BINDEX head, DBBlock block) {
        assert head.getIssuersFrameVar().equals(block.getIssuersFrameVar()) : "BR_G56_ruleIssuersFrameVar - " + head.getIssuersFrameVar() + " ==? " + block.getIssuersFrameVar();
        return head.getIssuersFrameVar().equals(block.getIssuersFrameVar());
    }

    /**
     * <pre>
     *
     * MedianTime = HEAD.medianTime
     * </pre>
     */
    private boolean BR_G57_ruleMedianTime(BINDEX head, DBBlock block) {
        assert head.getMedianTime().equals(block.getMedianTime()) : "BR_G57_ruleMedianTime - rule Median Time - " + head.getMedianTime() + " ==? " + block.getMedianTime();
        return head.getMedianTime().equals(block.getMedianTime());
    }


    /**
     * <pre>
     *
     * UniversalDividend = HEAD.new_dividend
     * </pre>
     *
     * @param testHead BINDEX
     * @param block    the block we're comparing
     * @return ud equality accepting null
     */
    private boolean BR_G58_ruleUniversalDividend(BINDEX testHead, DBBlock block) {

        if (testHead.getNumber() == 0)
            return true;

        if (testHead.new_dividend == null)
            return block.getDividend() == null;
        else {
            assert testHead.new_dividend.equals(block.getDividend()) : "BR_G58_ruleUniversalDividend - " + testHead.new_dividend + " ==? " + block.getDividend();
            return testHead.new_dividend.equals(block.getDividend());
        }


    }

    /**
     * <pre>
     *
     * UnitBase = HEAD.unitBase
     * </pre>
     */
    private boolean BR_G59_ruleUnitBase(BINDEX head, DBBlock block) {
        assert head.getUnitBase().equals(block.getUnitbase()) : "BR_G59_ruleUnitBase - UnitBase - " + head.getUnitBase() + " - " + block.getDividend();
        return head.getUnitBase().equals(block.getUnitbase());
    }

    /**
     * <pre>
     *
     * MembersCount = HEAD.membersCount
     * </pre>
     */
    private boolean BR_G60_ruleMembersCount(BINDEX head, DBBlock block) {
        assert block.getMembersCount().equals(head.getMembersCount()) :
                "BR_G60_ruleMembersCount - " + head.getMembersCount() + " - " + block.getMembersCount();
        return block.getMembersCount().equals(head.getMembersCount());

    }

    /**
     * <pre>
     *
     * If HEAD.number > 0
     *     PowMin = HEAD.powMin
     * </pre>
     */
    private boolean BR_G61_rulePowMin(BINDEX head, DBBlock block) {
        assert block.getNumber() == 0 || head.getPowMin().equals(block.getPowMin()) : "BR_G61_rulePowMin - rule PowMin - " + head.getPowMin() + " != " + block.getPowMin();
        return block.getNumber() == 0 || head.getPowMin().equals(block.getPowMin());
    }

    /**
     * <pre>
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
    private boolean BR_G62_ruleProofOfWork(BINDEX head) {
        final String expectedStart = head.getHash().substring(0, head.powZeros + 1);

        for (int i = 0; i <= head.powZeros; i++) {
            if (expectedStart.charAt(i) != '0') {
                LOG.info("missing zeros at  " + i);
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
     *
     * ENTRY.age <= [idtyWindow]
     */
    private void BR_G63_ruleIdentityWritability(IINDEX i) {
        assert i.getAge() <= conf.getIdtyWindow() : "BR_G63_ruleIdentityWritability - age: " + i.getAge() + " <=? " + conf.getIdtyWindow();
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.age <= [msWindow]
     */
    private void BR_G64_ruleMembershipMsWindow(MINDEX m) {
        assert m.age <= conf.getMsWindow() : "BR_G64_ruleMembershipMsWindow - age:" + m.age + " <= " + conf.getMsWindow() + " for " + m;

    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.age <= [sigWindow]
     */
    private void BR_G65_ruleCertificationSigWindow(CINDEX c) {
        assert c.getAge() <= conf.getSigWindow() : "BR_G65_ruleCertificationSigWindow - age " + c.getAge() + " <= " + conf.getSigWindow() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.stock <= sigStock
     */
    private void BR_G66_ruleCertificationStock(CINDEX c) {
        assert c.getStock() <= conf.getSigStock() : "BR_G66_ruleCertificationStock - stock " + c.getStock() + " <= " + conf.getSigStock() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.unchainables == 0
     */
    private void BR_G67_ruleCertificationPeriod(CINDEX c) {
        assert c.getUnchainables() == 0 : "BR_G67_ruleCertificationPeriod - " + c.getUnchainables() + " ==? 0 for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * If HEAD.number > 0:
     *
     * ENTRY.fromMember == true
     */
    private void BR_G68_ruleCertificationFromMember(BINDEX head, CINDEX c) {
        assert head.getNumber() == 0 || c.isFromMember() : "BR_G68_ruleCertificationFromMember -  at " + head.getNumber() + " || " + c.isFromMember() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.toMember == true OR ENTRY.toNewcomer == true
     */
    private void BR_G69_ruleCertificationToMemberOrNewComer(CINDEX c) {
        assert c.isToMember() || c.isToNewcomer() : "BR_G69_ruleCertificationToMemberOrNewComer - member? " + c.isToMember() + " - newComer?" + c.isToNewcomer() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.toLeaver == false
     */
    private void BR_G70_ruleCertificationToNonLeaver(CINDEX c) {
        assert !c.isToLeaver() : "BR_G70_ruleCertificationToNonLeaver - toLeaver? " + c.isToLeaver() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.isReplay == false
     */
    private void BR_G71_ruleCertificationReplay(CINDEX c) {
        assert !c.isReplay() || c.isReplayable() : "BR_G71_ruleCertificationReplay - isReplay? " + c.isReplay() + " isReplayable? " + c.isReplayable() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.sigOK == true
     */
    private void BR_G72_ruleCertificationSignature(CINDEX c) {
        assert c.isSigOK() : "BR_G72_ruleCertificationSignature - sigOk? " + c.isSigOK() + " for " + c;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.uidUnique == true
     */
    private void BR_G73_ruleIdentityUserIdUnicity(IINDEX i) {
        assert i.isUidUnique() : "BR_G73_ruleIdentityUserIdUnicity - uidUnique? " + i.isUidUnique() + " for " + i;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.pubUnique == true
     */
    private void BR_G74_ruleIdentityPubkeyUnicity(IINDEX i) {
        assert i.isPubUnique() : "BR_G74_ruleIdentityPubkeyUnicity - pubUnique? " + i.isPubUnique() + " for " + i;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.numberFollowing == true
     */
    private void BR_G75_ruleMembershipSuccession(MINDEX m) {
        assert m.numberFollowing : "BR_G75_ruleMembershipSuccession - numberFollowing? " + m.numberFollowing + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.distanceOK == true
     */
    private void BR_G76_ruleMembershipDistance(MINDEX m) {
        assert m.distanceOK : "BR_G76_ruleMembershipDistance - " + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.onRevoked == false
     */
    private void BR_G77_ruleMembershipOnRevoked(MINDEX m) {
        assert !m.onRevoked : "BR_G77_ruleMembershipOnRevoked - " + m.getPub() + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.joinsTwice == false
     */
    private void BR_G78_ruleMembershipJoinsTwice(MINDEX m) {
        assert !m.joinsTwice : "BR_G78_ruleMembershipJoinsTwice - " + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.enoughCerts == true
     */
    private void BR_G79_ruleMembershipEnoughCertifications(MINDEX m) {
        assert m.enoughCerts : "BR_G79_ruleMembershipEnoughCertifications - " + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.leaverIsMember == true
     */
    private void BR_G80_ruleMembershipLeaver(MINDEX m) {
        assert m.leaverIsMember : "BR_G80_ruleMembershipLeaver - " + " for " + m;
    }

    /**
     * <pre>
     *
     * Rule:
     *
     * ENTRY.activeIsMember == true
     */
    private void BR_G81_ruleMembershipActive(MINDEX m) {
        assert m.activeIsMember : "BR_G81_ruleMembershipActive - " + " for " + m;
    }

    /**
     * <pre>
     * BR_G82_ruleRevokedIsMember
     *
     * Rule:
     *
     * ENTRY.revokedIsMember == true
     */
    private void BR_G82_ruleRevokedIsMember(MINDEX m) {
        assert m.revokedIsMember : "BR_G82_ruleRevokedIsMember - " + " for " + m;
    }

    /**
     * <pre>
     * BR_G83_ruleRevocationSingleton
     *
     * Rule:
     *
     * ENTRY.alreadyRevoked == false
     */
    private void BR_G83_ruleRevocationSingleton(MINDEX m) {
        assert !m.alreadyRevoked : "BR_G83_ruleRevocationSingleton - " + " for " + m;
    }

    /**
     * <pre>
     * BR_G84_ruleRevocationSignature
     *
     * Rule:
     *
     * ENTRY.revocationSigOK == true
     */
    private void BR_G84_ruleRevocationSignature(MINDEX m) {
        assert m.revocationSigOK : "BR_G84_ruleRevocationSignature - " + " for " + m;
    }

    /**
     * <pre>
     * BR_G85_ruleExcludedIsMember
     *
     * Rule:
     *
     * ENTRY.excludedIsMember == true
     */
    private void BR_G85_ruleExcludedIsMember(IINDEX i) {
        assert i.isExcludedIsMember() : "BR_G85_ruleExcludedIsMember - " + i.isExcludedIsMember() + " for " + i;
    }

    /**
     * <pre>
     * BR_G86_ruleExcludedContainsExactlyThoseKicked
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
    private void BR_G86_ruleExcludedContainsExactlyThoseKicked() {

        boolean ExcludedContainsExactlyThoseToBeKicked =
                indexIGlobal().filter(IINDEX::getKick)
                        .map(toKick -> reduceI(toKick.getPub()).map(IINDEX::getKick).orElse(false))
                        .filter(i -> i)
                        .count() == 1;

        if (ExcludedContainsExactlyThoseToBeKicked) {
            localI.forEach(i -> {
                if (!i.getMember()) {
                    i.setHasToBeExcluded(true);
                }
            });
        }

        ExcludedContainsExactlyThoseToBeKicked = true; // FIXME

        assert ExcludedContainsExactlyThoseToBeKicked : "BR_G86_ruleExcludedContainsExactlyThoseKicked - ";
    }

    /**
     * <pre>
     * BR_G87_ruleInputIsAvailable
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.available == true
     * </pre>
     */
    private boolean BR_G87_ruleInputIsAvailable(SINDEX entry) {
        if ("UPDATE".equals(entry.getOp())) {
            assert true : "BR_G87_ruleInputIsAvailable - rule Input is not available " + entry.isAvailable() + " " + entry.isConsumed();
            return true; // FIXME complete
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G88_ruleInputIsUnlocked
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.isLocked == false
     */
    private boolean BR_G88_ruleInputIsUnlocked(SINDEX entry) {
        if ("UPDATE".equals(entry.getOp())) {
            assert !entry.isLocked() : "BR_G88_ruleInputIsUnlocked - " + entry;
            return !entry.isLocked();
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G89_ruleInputIsTimeUnlocked
     *
     * For each LOCAL_SINDEX[op='UPDATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.isTimeLocked == false
     * </pre>
     */
    private boolean BR_G89_ruleInputIsTimeUnlocked(SINDEX entry) {
        if ("UPDATE".equals(entry.getOp())) {
            assert !entry.isTimeLocked() : "BR_G89_ruleInputIsTimeUnlocked - " + entry;
            return !entry.isTimeLocked();
        } else
            return true;
    }

    /**
     * <pre>
     * BR_G90_ruleOutputBase
     *
     * For each LOCAL_SINDEX[op='CREATE'] as ENTRY:
     *
     * Rule:
     *
     * ENTRY.unitBase <= HEAD~1.unitBase
     * </pre>
     */
    private boolean BR_G90_ruleOutputBase(SINDEX entry) {
        assert entry != null : "BR_G90_ruleOutputBase - rule Output Base - entry is null";

        if ("CREATE".equals(entry.getOp())) {

            assert head_1() != null : "BR_G90_ruleOutputBase - rule Output base - HEAD-1 is null ";

            assert entry.getBase() <= head_1().getUnitBase()
                    : "BR_G90_ruleOutputBase - rule Output base - " + entry.getBase() + " " + head_1().getUnitBase();

            return entry.getBase() <= head_1().getUnitBase();
        } else
            return true;

    }

    /**
     * <pre>
     * BR_G91_IndexDividend
     *
     * If HEAD.new_dividend != null:
     *
     * For each REDUCE_BY(GLOBAL_IINDEX[member=true], 'pub') as IDTY then if IDTY.member, add a new LOCAL_SINDEX entry:
     *
     * SINDEX (
     *          op = 'CREATE'
     *  identifier = IDTY.pub
     *         pos = HEAD.number
     *  written = BLOCKSTAMP
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
     *  written = BLOCKSTAMP
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
    private void BR_G91_IndexDividend(BINDEX head, BStamp block) {
        if (head.new_dividend == null)
            return;
         Stream.concat(indexIGlobal(), localI.stream())
                .filter(i -> i.getMember() != null && i.getMember())
                //.peek(i ->  LOG.info("BR_G91_IndexDividend"+i))
                .map(i -> new SINDEX("CREATE",
                        i.getPub(),
                        head.getNumber(),
                        null,
                        block,
                        head.getDividend(),
                        head.getUnitBase(),
                        null,
                        "SIG(" + i.getPub() + ")",
                        false, // consumed
                        null))

                .forEach(localS::add);


        //		assert localI.getSize() > 0 : "BR_G91_IndexDividend - Dividend - localS shouldnt be empty";
    }

    /**
     * <pre>
     * BR_G92_IndexCertificationExpiry
     *
     * For each GLOBAL_CINDEX[expires_on<=HEAD.medianTime] as CERT, add a new LOCAL_CINDEX entry:
     *
     * If reduce(GLOBAL_CINDEX[issuer=CERT.issuer,receiver=CERT.receiver,signed=CERT.signed]).expired_on == 0:
     *
     * CINDEX (
     *         op = 'UPDATE'
     *     issuer = CERT.issuer
     *   receiver = CERT.receiver
     * signed = CERT.signed
     * expired_on = HEAD.medianTime
     * )
     * </pre>
     */
    private void BR_G92_IndexCertificationExpiry(BINDEX head) {
//FIXME very ineficient way of doing it !!!
        findCertsThatShouldExpire(head.getMedianTime())
                //.peek(x -> LOG.info("findCertsThatShouldExpire " + x))
                .map(cert -> reduceC(cert.getIssuer(), cert.getReceiver()).orElseThrow())
                //.peek(x -> LOG.info("reduced " + x))

                .filter(c -> c.getExpired_on() == 0)
                //.peek(x -> LOG.info("filtered " + x))
                .map(cert -> new CINDEX("UPDATE",
                        cert.getIssuer(),
                        cert.getReceiver(),
                        cert.getCreatedOn(),
                        head.getMedianTime(),
                        head.bstamp()))
                .forEach(localC::add);

    }

    /**
     * <pre>
     * BR_G93_IndexMembershipExpiry
     *
     * For each REDUCE_BY(GLOBAL_MINDEX[expires_on<=HEAD.medianTime AND revokes_on>HEAD.medianTime], 'pub') as POTENTIAL then consider
     *     REDUCE(GLOBAL_MINDEX[pub=POTENTIAL.pub]) AS MS.
     *
     *     If MS.expired_on == null OR MS.expired_on == 0, add a new LOCAL_MINDEX entry:
     *
     *         MINDEX ( op = 'UPDATE'
     *                 pub = MS.pub
     *          written = BLOCKSTAMP
     *          expired_on = HEAD.medianTime )
     * </pre>
     */

    private void BR_G93_IndexMembershipExpiry(BINDEX head) {
        findPubkeysThatShouldExpire(head.getMedianTime())
                .map(potential -> reduceM(potential).orElseThrow())
                .filter(ms -> (ms.getExpired_on() == null || ms.getExpired_on() == 0) && ms.getExpires_on() > head.getMedianTime())
                .forEach(ms -> {
                    LOG.info("BR_G93_IndexMembershipExpiry " + ms);
                    localM.add(
                            new MINDEX("UPDATE",
                                    ms.getPub(),
                                    ms.getSigned(),
                                    head.bstamp(),
                                    "BR_G93",
                                    null,
                                    head.getMedianTime(),
                                    null,
                                    null,
                                    null,
                                    false,
                                    null));
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
     *  written = BLOCKSTAMP
     *        kick = true )
     * </pre>
     */
    private void BR_G94_IndexExclusionByMembership(BStamp blockstamp) {

        for (final MINDEX m : localM) {
             if (m.getExpired_on() != null && m.getExpired_on() != 0) {
                localI.add(new IINDEX("UPDATE",
                        m.getPub(),
                        blockstamp,
                        true
                ));
            }
        }
    }

    /**
     * <pre>
     * BR_G95_IndexExclusionByCertification  //FIXME REDUCE BY ...
     *
     * For each LOCAL_CINDEX[expired_on!=0] as CERT:
     *
     * Set:
     *
     * CURRENT_VALID_CERTS = REDUCE_BY(GLOBAL_CINDEX[receiver=CERT.receiver], 'issuer', 'receiver', 'signed')[expired_on=0]
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
     *      written = BLOCKSTAMP
     *            kick = true )
     *
     * </pre>
     */
    private void BR_G95_IndexExclusionByCertification(BStamp bstamp) {
        localC.forEach(cert -> {

            final var cntLocal = localC.stream()
                    .filter(c -> cert.getReceiver().equals(c.getReceiver()) && c.getExpired_on() == 0)
                    .count();
            final var cntExpired = localC.stream()
                    .filter(c -> cert.getReceiver().equals(c.getReceiver()) && c.getExpired_on() != 0)
                    .count();
            final var cntValid = getC(null, cert.getReceiver()).count();

            if (cntValid + cntLocal - cntExpired < conf.getSigQty()) {
                localI.add(new IINDEX("UPDATE",
                        cert.getReceiver(),
                        bstamp,
                        true));
            }

        });

    }

    /**
     * <pre>
     * BR_G96_IndexImplicitRevocation
     *
     * For each GLOBAL_MINDEX[revokes_on<=HEAD.medianTime,revoked=null] as MS:
     *
     *     REDUCED = REDUCE(GLOBAL_MINDEX[pub=MS.pub])
     *
     *      If REDUCED.revokes_on<=HEAD.medianTime AND REDUCED.revoked==null,
     *
     *      add a new LOCAL_MINDEX entry:
     *
     * MINDEX ( op = 'UPDATE'
     *         pub = MS.pub
     *  written = BLOCKSTAMP
     *  revoked =  HEAD.medianTime ) // FIXME spec revoked is not a time but a Block_UID
     *
     * </pre>
     */
    private void BR_G96_IndexImplicitRevocation(BINDEX head) {
        findRevokesOnLteAndRevokedOnIsNull(head.getMedianTime())
                //.filter(m -> m.getRevokes_on() != null && m.getRevokes_on() <= head.getMedianTime() && m.getRevoked()  == null)
                .map(ms -> reduceM(ms).orElseThrow())
                .filter(ms -> ms.getRevokes_on() <= head.getMedianTime() || ms.getRevoked() == null)
                .forEach(ms -> {
                    LOG.info("BR_G96_IndexImplicitRevocation " + ms);

                    localM.add(
                            new MINDEX("UPDATE",
                                    ms.getPub(),
                                    ms.getSigned(),
                                    head.bstamp(),
                                    "BR_G96",
                                    null,
                                    null,
                                    null,
                                    head.bstamp(),
                                    null,
                                    null,
                                    null
                            ));
                });
    }

    /**
     * <pre>
     * BR_G97_TestIndex - Final INDEX operations
     *
     * @see <a href="https://git.duniter.org/nodes/typescript/duniter/blob/dev/app/lib/blockchain/DuniterBlockchain.ts#L45">DuniterBlockchain</a>
     *
     * If all the rules pass, then all the LOCAL INDEX values (IINDEX, MINDEX,
     * CINDEX, SINDEX, BINDEX) have to be appended to the GLOBAL INDEX.
     *
     * </pre>
     */
    default boolean BR_G97_TestIndex(BINDEX testHead, DBBlock block, boolean checkPow) {

        // ===========   TEST BINDEX VARIABLES    ===========
        var valid = BR_G49_ruleVersion(testHead) &&
                BR_G50_ruleBlockSize(testHead) &&
                BR_G98_ruleCurrency(testHead, block) &&
                BR_G51_ruleBlockNumber(testHead, block) &&
                BR_G52_rulePreviousHash(testHead, block) &&
                BR_G53_rulePreviousIssuer(testHead, block) &&
                BR_G54_ruleDifferentIssuersCount(testHead, block) &&
                BR_G55_ruleIssuersFrame(testHead, block) &&
                BR_G56_ruleIssuersFrameVar(testHead, block) &&
                BR_G57_ruleMedianTime(testHead, block) &&
                BR_G58_ruleUniversalDividend(testHead, block) &&
                BR_G59_ruleUnitBase(testHead, block) &&
                BR_G60_ruleMembersCount(testHead, block) &&
                BR_G61_rulePowMin(testHead, block) &&
                BR_G101_ruleIssuerIsMember(testHead) &&
                (!checkPow || BR_G62_ruleProofOfWork(testHead));

        // ===========   TEST SINDEX VARIABLES    ===========
        valid &= localS.stream()
                .allMatch(tx -> BR_G103_ruleTransactionWritable(tx)
                        && BR_G87_ruleInputIsAvailable(tx)
                        && BR_G88_ruleInputIsUnlocked(tx)
                        && BR_G89_ruleInputIsTimeUnlocked(tx)
                        && BR_G90_ruleOutputBase(tx));

        // ===========   TEST MINDEX VARIABLES    ===========

        localM.forEach(m -> {
            BR_G64_ruleMembershipMsWindow(m);
            BR_G75_ruleMembershipSuccession(m);
            BR_G76_ruleMembershipDistance(m);
            BR_G77_ruleMembershipOnRevoked(m);
            BR_G78_ruleMembershipJoinsTwice(m);
            BR_G79_ruleMembershipEnoughCertifications(m);
            BR_G80_ruleMembershipLeaver(m);
            BR_G81_ruleMembershipActive(m);
            BR_G82_ruleRevokedIsMember(m);
            BR_G83_ruleRevocationSingleton(m);
            BR_G84_ruleRevocationSignature(m);
        });

        // ===========   TEST CINDEX VARIABLES    ===========

        localC.forEach(c -> {
            BR_G65_ruleCertificationSigWindow(c);
            BR_G66_ruleCertificationStock(c);
            BR_G67_ruleCertificationPeriod(c);
            BR_G68_ruleCertificationFromMember(testHead, c);
            BR_G69_ruleCertificationToMemberOrNewComer(c);
            BR_G70_ruleCertificationToNonLeaver(c);
            BR_G71_ruleCertificationReplay(c);
            BR_G72_ruleCertificationSignature(c);
            BR_G108_ruleMembershipPeriod(c);
        });

        // ===========   TEST IINDEX VARIABLES    ===========

        for (IINDEX i : localI) {
            BR_G63_ruleIdentityWritability(i);
            BR_G73_ruleIdentityUserIdUnicity(i);
            BR_G74_ruleIdentityPubkeyUnicity(i);
            BR_G85_ruleExcludedIsMember(i);
        }

        BR_G86_ruleExcludedContainsExactlyThoseKicked();

        if (valid) {
            IndexB.add(testHead);
        } else {
            LOG.info("BR_G97_TestIndex did not pass at block " + block + " (ICM : " + localI.size() + " " + localC.size() + " " + localM.size() + ") " + testHead);
        }
        return valid;
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
    private boolean BR_G98_ruleCurrency(BINDEX head, DBBlock ccy) {
        assert head != null : "BR_G98_ruleCurrency - rule currency - head is null ";

        if (head.getNumber() == 0)
            return true;
        else {
            assert head.getCurrency() != null : "BR_G98_ruleCurrency - rule currency - BINDEX.currency is null ";
            assert ccy != null : "BR_G98_ruleCurrency - rule currency - Block.currency is null ";
            assert head.getCurrency().equals(ccy.getCurrency()) : "BR_G98_ruleCurrency - rule currency - Block.currency is different ";
            return head.getCurrency().equals(ccy.getCurrency());
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
        if (head.getNumber() > 0) {
            head.setCurrency(head_1().getCurrency());
            assert head_1() != null : "BR_G99_setCurrency - HEAD-1 is null ";
        } else {
            head.setCurrency(null);
        }
    }

    /**
     * Commit the arguments to the GLOBAL_INDEX sources (to database or to simple
     * java structure).
     * <p>
     * This operation must be Transactional
     *
     * @param indexB :
     * @param indexI :
     * @param indexM :
     * @param indexC :
     * @param indexS :
     */
    boolean commit(BINDEX indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS);


    default Optional<BINDEX> head() {
        if (IndexB.size() > 1)
            return Optional.of(IndexB.get(IndexB.size() - 1));

        return Optional.empty();
    }

    default BINDEX head_() {
        return head().orElseThrow();
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
         * signed = null
         * written = BLOCKSTAMP
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
         * signed = BLOCK_UID
         * written = BLOCKSTAMP
         * type = 'REV'
         * expires_on = null
         * revokes_on = null
         * revoked = BLOCKSTAMP
         * revocation = REVOCATION_SIG
         * leaving = false
         * )
         *
         *
         *
         * Leaver
         *
         * Each leaver produces 1 new entry:
         *
         * MINDEX (
         * op = 'UPDATE'
         * pub = PUBLIC_KEY
         * signed = BLOCK_UID
         * written = BLOCKSTAMP
         * type = 'LEAVE'
         * expires_on = null
         * revokes_on = null
         * revoked = null
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
         * signed = TX_BLOCKSTAMP
         * written = BLOCKSTAMP
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
         * written = BLOCKSTAMP
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
         * signed = BLOCK_UID
         * written = BLOCKSTAMP
         * expires_on = MedianTime + msValidity
         * revokes_on = MedianTime + msValidity*2
         * chainable_on = MedianTime + msPeriod
         * type = 'RENEW'
         * revoked = null
         * leaving = null
         * )
         *
         * </pre>
         *
         * @param block :
         */
    default void indexBlock(DBBlock block) {

        final var written_on = block.bStamp();
        final var writtenTime = block.getMedianTime();


        if (block.getNumber().equals(0)) {
            conf.accept(block.getParameters());
        }


        block.getExcluded().forEach(excluded ->
                localI.add(new IINDEX("UPDATE",
                        null,
                        excluded.getPubkey(),
                        null,
                        written_on,
                        false,
                        null,
                        false,
                        null))
        );

        block.getRevoked().forEach(revoked -> {
            var idtyRef = reduceM(revoked.getPubkey()).orElseThrow();

            localM.add(new MINDEX("UPDATE",
                    revoked.getPubkey(),
                    written_on,
                    written_on,
                    "REV",
                    idtyRef.getExpires_on(),
                    null,
                    idtyRef.getRevokes_on(),
                    written_on,
                    revoked.getRevocation(),
                    false,
                    writtenTime + conf.getMsWindow()));
        });

        block.getLeavers().forEach(leaver -> {

            var idtyRef = reduceM(leaver.getPubkey()).orElseThrow();
            localM.add(new MINDEX("UPDATE",
                    leaver.getPubkey(),
                    leaver.getSigned(),
                    written_on,
                    "LEAVE",
                    idtyRef.getExpires_on(),
                    null,
                    idtyRef.getRevokes_on(),
                    null,
                    null,
                    true,
                    null));
        });

        block.getRenewed().forEach(renew -> {
            var median = createdOnBlock(renew.getSigned()).orElse(block).getMedianTime();
            localM.add(new MINDEX("UPDATE",
                    renew.getPubkey(),
                    renew.getSigned(),
                    written_on,
                    "RENEW",
                    median + conf.getMsValidity(),
                    null,
                    median + conf.getMsValidity() * 2,
                    null,
                    null,
                    false,
                    writtenTime + conf.getMsWindow()

            ));
        });

        for (int ind = 0; ind < block.getIdentities().size(); ind++) {
            var idty = block.getIdentities().get(ind);
            var median = createdOnBlock(idty.getSigned()).orElse(block).getMedianTime();

            localI.add(new IINDEX("CREATE",
                    idty.getUid(),
                    idty.getPubkey(),
                    idty.getSigned(),
                    written_on,
                    true,
                    true,
                    false,
                    idty.getSignature()));

            localM.add(new MINDEX("CREATE",
                    idty.getPubkey(),
                    idty.getSigned(),
                    written_on,
                    "JOIN",
                    median + conf.getMsValidity(),
                    null,
                    median + conf.getMsValidity() * 2,
                    null,
                    null,
                    false,
                    writtenTime + conf.msPeriod)
            );
        }

        block.getJoiners().stream()
                .filter(join -> localM.stream().noneMatch(m ->
                        "CREATE".equals(m.getOp()) && m.getPub().equals(join.getPubkey())))
                .forEach(joiner -> {
                    var median = createdOnBlock(joiner.getSigned()).orElse(block).getMedianTime();

                    localI.add(new IINDEX("UPDATE",
                            joiner.getUid(),
                            joiner.getPubkey(),
                            joiner.getSigned(),
                            written_on,
                            true,
                            true,
                            false,
                            joiner.getSignature()));

                    localM.add(new MINDEX("UPDATE",
                            joiner.getPubkey(),
                            joiner.getSigned(), // FIXME note to spec here BLOCK_UID is ambiguous joiners have 2 => i_block_uid
                            written_on,
                            "JOIN",
                            median + conf.getMsValidity(),
                            null,
                            median + conf.getMsValidity() * 2,
                            null,
                            null,
                            null,
                            writtenTime + conf.msPeriod)
                    );

                });


        block.getCertifications().forEach(cert -> {

            var createOn = createdOnBlock(cert.getSignedOn()).orElse(block);

            localC.add(new CINDEX("CREATE",
                    cert.getCertifier(),
                    cert.getCertified(),
                    cert.getSignedOn(),
                    written_on,
                    cert.getSignature(),
                    createOn.getMedianTime() + conf.getSigValidity(),
                    writtenTime + conf.getSigPeriod(),
                    0L,
                    writtenTime + conf.getSigReplay()
            ).putCreatedOn(createOn));
        });

        for (int indTx = 0; indTx < block.getTransactions().size(); indTx++) {
            var tx = block.getTransactions().get(indTx);

            final var created_on = tx.getBlockstamp();

            for (int indIn = 0; indIn < tx.getInputs().size(); indIn++) {
                var input = tx.getInputs().get(indIn);

                localS.add(new SINDEX("UPDATE", // op
                        input.getType().equals(TxType.D) ? input.getDsource() : input.getTHash(), // identifier
                        input.getType().equals(TxType.D) ? input.getDBlockID() : input.getTIndex(), // pos
                        created_on,
                        written_on,
                        input.getAmount(),
                        input.getBase(),
                        (long) tx.getLocktime(),
                        "SIG(" + tx.getIssuers().get(0) + ")",
                        true, // consumed
                        tx.getHash()
                ));
            }

            for (int indOut = 0; indOut < tx.getOutputs().size(); indOut++) {
                var output = tx.getOutputs().get(indOut);

                localS.add(new SINDEX("CREATE",
                        tx.getHash(),
                        indOut,
                        null,
                        written_on,
                        output.getAmount(),
                        output.getBase(),
                        (long) tx.getLocktime(),
                        output.getOutputCondition(),
                        false, // consumed
                        tx.getHash()
                ));
            }

        }

    }

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return CINDEX all
     */
    Stream<CINDEX> indexCGlobal();

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return IINDEX all
     */
    Stream<IINDEX> indexIGlobal();


    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return MINDEX all
     */
    Stream<MINDEX> indexMGlobal();

    /**
     * read access to the GLOBAL_INDEX
     * <p>
     * provide a simple placeholder for indexes
     *
     * @return SINDEX all
     */
    Stream<SINDEX> indexSGlobal();


    Integer certStock(String issuer,Long asOf);

    Stream<String> findPubkeysThatShouldExpire(Long mTime);

    Stream<CINDEX> findCertsThatShouldExpire(Long mTime);

    Stream<String> findRevokesOnLteAndRevokedOnIsNull(Long mTime);

    default Stream<IINDEX> idtyByUid(String uid) {
        return indexIGlobal().filter(i -> i.getUid().equals(uid));
    }

    default Stream<IINDEX> idtyByPubkey(String pub) {
        return indexIGlobal().filter(i -> i.getPub().equals(pub));
    }

    default Optional<MINDEX> reduceM(String pub) {
        return indexMGlobal().filter(i -> i.getPub().equals(pub)).reduce(MINDEX.reducer);
    }

    default Optional<IINDEX> reduceI(String pub) {
        return idtyByPubkey(pub).reduce(IINDEX.reducer);
    }


    default Stream<CINDEX> getC(String issuer, String receiver) {
        return indexCGlobal().filter(i ->
                (receiver == null || i.getReceiver().equals(receiver))
                        &&
                        (issuer == null || i.getIssuer().equals(issuer)));
    }

    default Optional<CINDEX> reduceC(String issuer, String receiver) {
        return getC(issuer, receiver).reduce(CINDEX.reducer);
    }

    Stream<Account> lowAccounts();

    default Stream<SINDEX> sourcesByConditions(String conditions) {
        return indexSGlobal().filter(i -> i.getConditions().equals(conditions));
    }

    //Stream<SINDEX> sourcesByConditions(String conditions, String amount );


    default Stream<SINDEX> sourcesByConditions(String identifier, Integer pos) {
        return indexSGlobal().filter(i -> i.getIdentifier().equals(identifier) && i.getPos().equals(pos));
    }


    default void resetLocalIndex() {
        localC.clear();
        localM.clear();
        localI.clear();
        localS.clear();

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
        assert entry != null : "inputEntry SINDEX null ";
        var inputEntries = localS.stream()
                .filter(s -> {
                    assert s != null : "inputEntry s null ";
                    //assert s.identifier !=null : "inputEntry s.identifier null "+s+" - " + entry;
                    return "CREATE".equals(s.getOp())
                            && Objects.equals(s.getIdentifier(), entry.getIdentifier()) // accept null equality ?
                            && s.getPos().equals(entry.getPos())
                            && s.getAmount() == entry.getAmount()
                            && s.getBase() == entry.getBase();
                })

                .collect(toList());

        if (inputEntries.size() == 0) {
            inputEntries = sourcesByConditions(entry.getIdentifier(), entry.getPos())
                    //.peek(consumedS::add)
                    .peek(s -> LOG.info("pings " + s))
                    .collect(toList());
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
                .filter(h -> h.getNumber() >= head_1().getNumber() - m + 1)
                .sorted((b1, b2) -> Integer.compare(b2.getNumber(), b1.getNumber()))
                .collect(toList());


        return bheads.stream();
    }

    /**
     * @param block to completeGlobalScope
     * @return true or false or perhaps true
     */
    @Transactional
    default boolean completeGlobalScope(DBBlock block, boolean complete) {
        var quick = !complete;
        resetLocalIndex();

        final var newHead = new BINDEX();

        newHead.setVersion((int) block.getVersion());
        newHead.setSize(block.getSize());
        newHead.setHash(block.getHash());
        newHead.setIssuer(block.getIssuer());
        newHead.setTime(block.getTime());
        newHead.setPowMin(block.getPowMin());

        if (quick) {
            newHead.setCurrency(block.getCurrency());
            newHead.setMedianTime(block.getMedianTime());
            newHead.setNumber(block.getNumber());
            newHead.setUnitBase(block.getUnitbase());
            newHead.setMembersCount(block.getMembersCount());
            newHead.setDividend(block.getDividend() == null ? 0 : block.getDividend());
            newHead.new_dividend = null;
        }


        indexBlock(block);

        //  ==================  SET LOCAL BINDEX VARIABLES  ==================
        //
        if (complete) {
            BR_G01_setNumber(newHead);
            BR_G02_setPreviousHash(newHead);
            BR_G03_setPreviousIssuer(newHead);
            BR_G08_setMedianTime(newHead);
        }

        BR_G04_setIssuersCount(newHead);
        BR_G05_setIssuersFrame(newHead);
        BR_G06_setIssuersFrameVar(newHead);
        BR_G07_setAvgBlockSize(newHead);


        BR_G09_setDiffNumber(newHead);
        BR_G10_setMembersCount(newHead);
        BR_G11_setUdTime(newHead);
        BR_G12_setUnitBase(newHead);

        if (complete) BR_G13_setDividend(newHead);

        BR_G14_setUnitBase(newHead);
        BR_G15_setMassAndMassReeval(newHead);
        BR_G16_setSpeed(newHead);

        if (complete) BR_G17_setPowMin(newHead);
        if (complete) BR_G18_setPowZero(newHead);

        if (complete) BR_G99_setCurrency(newHead);
        BR_G100_setIssuerIsMember(newHead);


        newHead.setCurrency(block.getCurrency()); // because BR_G99_setCurrency set it to null

        //  ==================  SET OTHER LOCAL INDEX VARIABLES  ==================
        //
        for (final var cert : localC) {
            BR_G37_setAge(newHead, cert);
            BR_G38_setCertUnchainable(newHead, cert);
            BR_G39_setCertStock(cert,newHead);
            BR_G40_setCertFromMember(cert);
            BR_G41_setCertToMember(cert);
            BR_G42_setCertToNewCommer(cert);
            BR_G43_setToLeaver(cert);
            BR_G44_setIsReplay(newHead, cert);
            BR_G45_setSignatureOK(cert);
        }

        for (final var idty : localI) {
            BR_G19_setAge(newHead, idty);
            BR_G20_setUidUnicity(idty);
            BR_G21_setPubkeyUnicity(idty);
            BR_G33_setExcludedIsMember(idty);
            BR_G35_setIsBeingKicked(idty);
            BR_G36_setHasToBeExcluded(idty);
        }


        for (final var entry : localM) {
            BR_G22_setAge(newHead, entry);
            BR_G23_setNumberFollowing(entry);
            BR_G24_setDistanceOK(newHead, entry);
            BR_G25_setOnRevoked(entry);
            BR_G26_setJoinsTwice(entry);
            BR_G27_setEnoughCerts(entry);
            BR_G28_setLeaverIsMember(entry);
            BR_G29_setActiveIsMember(entry);
            BR_G30_setRevokedIsMember(entry);
            BR_G31_setAlreadyRevoked(entry);
            BR_G32_setRevocationSigOK(entry);
            BR_G34_setIsBeingRevoked(entry);
            BR_G107_setUnchainableM(entry);
        }

        for (final SINDEX entry : localS) {
            if ("CREATE".equals(entry.getOp())) {
                inputEntries(entry)
                        .forEach(input -> {
                            BR_G46_prepareAvailableAndCondition(entry, input);
                            BR_G47_prepareIsLocked(entry, input);
                            BR_G48_prepareIsTimeLocked(entry, input);
                        });

            } else if ("UPDATE".equals(entry.getOp())) {
                BR_G102_setSourceAge(entry, newHead);
            }
        }

        if (quick) BR_G104_MembershipExpiryDateCorrection(newHead);
        if (quick) BR_G105_CertificationExpiryDateCorrection(newHead);


        boolean success = true;

        //  ==================  TEST   ==================
        //
        if (complete)
            success = BR_G97_TestIndex(newHead, block, false);


        //  ==================  Index implicit rules   ==================
        //
        final var bstamp = new BStamp(block.getNumber(), block.getHash(), block.getMedianTime());
        BR_G91_IndexDividend(newHead, bstamp);
        BR_G106_IndexLowAccounts(newHead, bstamp);
        BR_G92_IndexCertificationExpiry(newHead);
        BR_G93_IndexMembershipExpiry(newHead);
        BR_G94_IndexExclusionByMembership(bstamp);
        BR_G95_IndexExclusionByCertification(bstamp);
        BR_G96_IndexImplicitRevocation(newHead);

        success &= commit(newHead, localI, localM, localC, localS);

        success &= trimAndCleanup(newHead, block);

        return success;

    }
}
