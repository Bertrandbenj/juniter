package juniter.core.validation;

import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.meta.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Original TypeScript here
 * https://git.duniter.org/nodes/typescript/duniter/blob/dev/app/lib/rules/local_rules.ts
 */
public interface BlockLocalValid extends LocalValid {

    Logger LOG = LogManager.getLogger(BlockLocalValid.class);
    ChainParameters conf = new ChainParameters();

    List<Short> AVAILABLE_VERSIONS = List.of((short) 10, (short) 11);

    /**
     * Exception safe validation to use as boolean
     *
     * @param block to completeGlobalScope
     * @return true if the node is valid
     */
    default boolean silentCheck(DUPBlock block) {

        try {
            assertBlockLocalValid(block, false);
            return true;
        } catch (final AssertionError ea) {
            LOG.error("check Block " + block.getNumber() + " for local Validity - " + ea.getMessage());
        }

        return false;
    }


    /**
     * @param block                : the block to validate
     * @param checkPowAndSignature : whether or not we should check the PoW and Signatures
     */
    default void assertBlockLocalValid(DUPBlock block, boolean checkPowAndSignature) {

        assertBlockHash(block.signedPartSigned(), block.getHash());

        assertSignature(block.signedPart(), block.getSignature(), block.getIssuer());

        block.getTransactions().forEach(tx -> {
            checkTxAmounts(tx);
            checkTxSources(tx);
            checkTxIssuers(tx);
            checkTxRecipients(tx);
            if (checkPowAndSignature)
                checkTxSignature(tx);
            checkTxVersion(tx);
        });

        checkParameters(block);

        checkUniversalDividend(block);

        if (checkPowAndSignature)
            checkProofOfWork(block);

        if (checkPowAndSignature)
            checkInnerHash(block);

        checkPreviousHash(block);

        checkPreviousIssuer(block);

        checkUnitBase(block);

        if (checkPowAndSignature)
            checkBlockSignature(block);

        checkBlockTimes(block);

        if (checkPowAndSignature)
            checkIdentitiesSignature(block);

        checkIdentitiesUserIDConflict(block);

        checkIdentitiesPubkeyConflict(block);

        //checkIdentitiesMatchJoin(block);

        checkMembershipUnicity(block);

        checkRevokedUnicity(block);

        checkRevokedAreExcluded(block);

        if (checkPowAndSignature)
            checkMembershipsSignature(block);

        checkCertificationOneByIssuer(block);

        checkCertificationUnicity(block);

        checkCertificationIsntForLeaverOrExcluded(block);

        checkMaxTransactionChainingDepth(block);

    }

    default void checkUniversalDividend(DUPBlock block) {
        assert block.getNumber() != 0 || block.getDividend() == null
                : "Dividend MUST BE null on Block 0 ";
    }


    default void checkInnerHash(DUPBlock block) {
        final var hash = Crypto.hash(block.toDUP(false, false));

        assert hash.equals(block.getInner_hash()) : //
                "assert Block InnerHash #" + block.getNumber() + " - " +
                        "\niss      : " + block.getIssuer() +
                        "\nsign     : " + block.getSignature() +
                        "\nexpected : " + block.getInner_hash() +
                        "\n but got : " + hash +
                        "\n on      : " + block.toDUP(false, false);
    }


    default void checkParameters(DUPBlock block) {
        assert block.params() == null ^ block.getNumber() == 0
                : "Chain Parameters MUST be present on block 0  &  MUST be null when block > 0 ";

    }

    default void checkProofOfWork(DUPBlock block) {
        assert block.getHash().startsWith("0000")
                : "check Proof of Work ";
    }

    default void checkPreviousHash(DUPBlock block) {
        assert block.getNumber() == 0 ^ block.getPreviousHash() != null
                : "check Previous Hash";
    }

    default void checkPreviousIssuer(DUPBlock block) {
        assert block.getNumber() == 0 ^ block.getPreviousIssuer() != null
                : "check Previous Issuer";
    }

    default void checkUnitBase(DUPBlock block) {
        assert block.getUnitbase().equals(0) || block.getNumber() > 0 : " checkUnitBase " + block.getUnitbase();
    }

    default void checkBlockSignature(DUPBlock block) {
        matchSignature(block.getSignature());
    }

    default void checkBlockTimes(DUPBlock block) {
        if (block.getNumber() > 0) {
            assert block.getTime() >= block.getMedianTime() && block.getTime() <= block.getMedianTime() + conf.maxAcceleration()
                    : "A block must have its Time field be between [MedianTime ; MedianTime + maxAcceleration]." + block.getMedianTime() + " <= " + block.getTime() + " <= " + block.getMedianTime() + conf.maxAcceleration();
        } else {
            assert block.getTime().equals(block.getMedianTime())
                    : "Root block's Time & MedianTime must be equal.";
        }
    }

    default void checkIdentitiesSignature(DUPBlock block) {
        block.getIdentities().forEach(i -> {
            assert Crypto.verify(i.toDUPdoc(false), i.getSignature(), i.getPubkey())
                    : "A block cannot contain identities whose signature does not match the identity's content at " + block + "\n" + i;
        });

    }

    default void checkIdentitiesUserIDConflict(DUPBlock block) {
        assert block.getIdentities().size() == block.getIdentities().stream()
                .map(HasPubkey::getPubkey)
                .distinct()
                .count()
                : " Block must not contain twice same identity userid at " + block + " " + block.getIdentities();
    }

    default void checkIdentitiesPubkeyConflict(DUPBlock block) {
        assert block.getIdentities().stream().map(HasPubkey::getPubkey).distinct().count() == block.getIdentities().size()
                : " Block must not contain twice same identity pubkey  at " + block + " " + block.getIdentities();
    }

    default void checkIdentitiesMatchJoin(DUPBlock block) {
        block.getJoiners().stream().map(DUPMember::getPubkey).forEach(j -> {
            assert block.getIdentities().stream().map(HasPubkey::getPubkey).anyMatch(i -> i.equals(j))
                    : "check Identities Match Join";
        });
    }

    default void checkMembershipUnicity(DUPBlock block) {
        assert block.getMembers().stream().map(DUPMember::getPubkey).distinct().count() == block.getMembers().size()
                : "check Membership Unicity ";
    }

    default void checkRevokedUnicity(DUPBlock block) {
        assert block.getRevoked().stream().map(DUPMember::getPubkey).distinct().count() == block.getRevoked().size()
                : "check Revoked Unicity ";
    }

    default void checkRevokedAreExcluded(DUPBlock block) {
        assert block.getRevoked().stream().map(DUPMember::getPubkey)
                .allMatch(p -> block.getExcluded().stream().map(DUPMember::getPubkey).anyMatch(x -> x.equals(p)))
                : "check Revoked Are Excluded";
    }

    default void checkMembershipsSignature(DUPBlock block) {
        block.getMembers().forEach(m -> {
            assert Crypto.verify(m.toDUPdoc(false), m.getSignature(), m.getPubkey()) : "check Memberships Signature on " + m;
        });
    }


    default void checkCertificationOneByIssuer(DUPBlock block) {

        assert block.getNumber() == 0 || block.getCertifications().stream().map(DUPCertification::getCertifier).distinct().count() == block.getCertifications().size()
                : "checkCertificationOneByIssuer";
    }

    default void checkCertificationUnicity(DUPBlock block) {
        assert block.getCertifications().stream().map(c -> c.getCertified() + c.getCertifier()).distinct().count() == block.getCertifications().size()
                : "check Certification Unicity ";
    }

    default void checkCertificationIsntForLeaverOrExcluded(DUPBlock block) {
        var excAndLeav = Stream.concat(block.getExcluded().stream().map(DUPMember::getPubkey), block.getLeavers().stream().map(DUPMember::getPubkey)).collect(Collectors.toList());

        block.getCertifications().stream().map(DUPCertification::getCertified).forEach(c -> {
            assert !excAndLeav.contains(c) : "check Certification Isnt For Leaver Or Excluded";
        });
    }

    default void checkTxVersion(DUPTransaction tx) {
        assert AVAILABLE_VERSIONS.stream().anyMatch(v -> v.equals(tx.getVersion())) : "Tx must be of getVersion 10, 11";
    }

    default void checkTxIssuers(DUPTransaction tx) {
        assert tx.getIssuers().size() > 0 : "Tx must have at least one Issuer";
    }

    default void checkTxSources(DUPTransaction tx) {
        assert tx.getInputs().size() > 0 : "Tx must have at least one Source";
    }

    default void checkTxRecipients(DUPTransaction tx) {
        assert tx.getOutputs().size() > 0 : "Tx must have at least one recipient";
    }

    default void checkTxAmounts(DUPTransaction tx) {
        assert tx.getInputs().stream().mapToInt(DUPInput::getAmount).sum() ==
                tx.getOutputs().stream().mapToInt(DUPOutput::getAmount).sum()
                : "Tx input and output amounts must match ";
    }

    default void checkTxSignature(DUPTransaction tx) {
        for (int i = 0; i < tx.getSignatures().size(); i++) {
            final var sign = tx.getSignatures().get(i);
            final var iss = tx.getIssuers().get(i);

            assert Crypto.verify(tx.toDUPdoc(false), sign, iss) : //
                    "TX Signature wrong " + sign
                            + "\n  for issuer : " + iss
                            + "\n  in transaction : " + tx.toDUPdoc(false);
        }
    }

    /**
     * <pre>
     * Transactions chaining max depth
     * FUNCTION `getTransactionDepth(txHash, LOCAL_DEPTH)`:
     *
     *     INPUTS = LOCAL_SINDEX[op='UPDATE',tx=txHash]
     *     DEPTH = LOCAL_DEPTH
     *
     *     FOR EACH `INPUT` OF `INPUTS`
     *         CONSUMED = LOCAL_SINDEX[op='CREATE',identifier=INPUT.identifier,pos=INPUT.pos]
     *         IF (CONSUMED != NULL)
     *             IF (LOCAL_DEPTH < 5)
     *                 DEPTH = MAX(DEPTH, getTransactionDepth(CONSUMED.tx, LOCAL_DEPTH +1)
     *             ELSE
     *                 DEPTH++
     *             END_IF
     *         END_IF
     *     END_FOR
     *
     *     RETURN DEPTH
     *
     * END_FUNCTION
     * Then:
     * maxTxChainingDepth = 0
     * For each TX_HASH of UNIQ(PICK(LOCAL_SINDEX, 'tx)):
     * maxTxChainingDepth = MAX(maxTxChainingDepth, getTransactionDepth(TX_HASH, 0))
     * Rule:
     * maxTxChainingDepth <= 5
     * </pre>
     *
     * @param block the input node
     */
    default void checkMaxTransactionChainingDepth(DUPBlock block) {
        assert block.getTransactions().size() <= 50 : "check Max Transaction Chaining Depth"; // FIXME implements
    }


    class Static implements BlockLocalValid {

        static Static singletons = make();

        private static Static make() {
            return new Static();
        }

        private Static() {
        }

        public static void assertBlock(DUPBlock b) throws AssertionError {
            singletons.assertBlockLocalValid(b, true);
        }

        public static boolean silentChecks(DUPBlock b) {
            return singletons.silentCheck(b);
        }
    }


}
