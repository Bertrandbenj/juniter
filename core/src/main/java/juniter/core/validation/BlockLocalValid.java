package juniter.core.validation;

import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.tx.TxInput;
import juniter.core.model.dbo.tx.TxOutput;
import juniter.core.model.dbo.wot.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Original TypeScript here
 * https://git.duniter.org/nodes/typescript/duniter/blob/dev/app/lib/rules/local_rules.ts
 */
public interface BlockLocalValid extends LocalValid {

    Logger LOG = LogManager.getLogger(BlockLocalValid.class);
    ChainParameters conf = new ChainParameters();

    /**
     * Exception safe validation to use as boolean
     *
     * @param block to completeGlobalScope
     * @return true if the node is valid
     */
    default boolean checkBlockIsLocalValid(DBBlock block) {

        try {
            assertBlockLocalValid(block, false);
            return true;
        } catch (final AssertionError ea) {
            LOG.error("check Block " + block.getNumber() + " is LocalValid - " + ea.getMessage());
        }

        return false;
    }


    /**
     * @param block                : the block to validate
     * @param checkPowAndSignature : whether or not we should check the PoW and Signatures
     */
    default void assertBlockLocalValid(DBBlock block, boolean checkPowAndSignature) {

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

        //checkCertificationIsntForLeaverOrExcluded(block);

        checkMaxTransactionChainingDepth(block);

    }

    private void checkUniversalDividend(DBBlock block) {
        assert block.getNumber() != 0 || block.getDividend() == null
                : "Dividend MUST BE null on Block 0 ";
    }


    private void checkInnerHash(DBBlock block) {
        final var hash = Crypto.hash(block.toDUP(false, false));

        assert hash.equals(block.getInner_hash()) : //
                "assert Block InnerHash #" + block.getNumber() + " - " +
                        "\niss      : " + block.getIssuer() +
                        "\nsign     : " + block.getSignature() +
                        "\nexpected : " + block.getInner_hash() +
                        "\n but got : " + hash +
                        "\n on      : " + block.toDUP(false, false);
    }


    private void checkParameters(DBBlock block) {
        assert block.getParameters() == null ^ block.getNumber() == 0
                : "Chain Parameters MUST be present on block 0  & Chain Parameters MUST be null when block > 0 ";

    }

    private void checkProofOfWork(DBBlock block) {
        assert block.getHash().startsWith("0000")
                : "check Proof of Work ";
    }

    private void checkPreviousHash(DBBlock block) {
        assert block.getNumber() == 0 ^ block.getPreviousHash() != null
                : "check Previous Hash";
    }

    private void checkPreviousIssuer(DBBlock block) {
        assert block.getNumber() == 0 ^ block.getPreviousIssuer() != null
                : "check Previous Issuer";
    }

    private void checkUnitBase(DBBlock block) {
        assert block.getUnitbase().equals(0) || block.getNumber() > 0 : " checkUnitBase " + block.getUnitbase();
    }

    private void checkBlockSignature(DBBlock block) {
        matchSignature(block.getSignature());
    }

    private void checkBlockTimes(DBBlock block) {
        if (block.getNumber() > 0) {
            assert block.getTime() >= block.getMedianTime() && block.getTime() <= block.getMedianTime() + conf.maxAcceleration()
                    : "A block must have its Time field be between [MedianTime ; MedianTime + maxAcceleration]." + block.getMedianTime() + " <= " + block.getTime() + " <= " + block.getMedianTime() + conf.maxAcceleration();
        } else {
            assert block.getTime().equals(block.getMedianTime())
                    : "Root block's Time & MedianTime must be equal.";
        }
    }

    private void checkIdentitiesSignature(DBBlock block) {
        block.getIdentities().forEach(i -> {
            assert Crypto.verify(i.toDUPDoc(), i.getSignature(), i.getPubkey())
                    : "A block cannot contain identities whose signature does not match the identity's content ";
        });

    }

    private void checkIdentitiesUserIDConflict(DBBlock block) {
        assert block.getIdentities().stream().map(Identity::getUid).distinct().count() == block.getIdentities().size()
                : " Block must not contain twice same identity userid ";
    }

    private void checkIdentitiesPubkeyConflict(DBBlock block) {
        assert block.getIdentities().stream().map(Identity::getPubkey).distinct().count() == block.getIdentities().size()
                : " Block must not contain twice same identity pubkey ";
    }

    private void checkIdentitiesMatchJoin(DBBlock block) {
        block.getJoiners().stream().map(Member::getPubkey).forEach(j -> {
            assert block.getIdentities().stream().map(Identity::getPubkey).anyMatch(i -> i.equals(j))
                    : "check Identities Match Join";
        });
    }

    private void checkMembershipUnicity(DBBlock block) {
        assert block.getMembers().stream().map(Member::getPubkey).distinct().count() == block.getMembers().size()
                : "check Membership Unicity ";
    }

    private void checkRevokedUnicity(DBBlock block) {
        assert block.getRevoked().stream().map(Member::getPubkey).distinct().count() == block.getRevoked().size()
                : "check Revoked Unicity ";
    }

    private void checkRevokedAreExcluded(DBBlock block) {
        assert block.getRevoked().stream().map(Member::getPubkey)
                .allMatch(p -> block.getExcluded().stream().map(Member::getPubkey).anyMatch(x -> x.equals(p)))
                : "check Revoked Are Excluded";
    }

    private void checkMembershipsSignature(DBBlock block) {
        block.getMembers().forEach(m -> {
            assert Crypto.verify(m.toDUPDoc(), m.getSignature(), m.getPubkey()) : "check Memberships Signature on " + m;
        });
    }


    private void checkCertificationOneByIssuer(DBBlock block) {

        assert block.getNumber() == 0 || block.getCertifications().stream().map(Certification::getCertifier).distinct().count() == block.getCertifications().size()
                : "checkCertificationOneByIssuer";
    }

    private void checkCertificationUnicity(DBBlock block) {
        assert block.getCertifications().stream().map(c -> c.getCertified() + c.getCertifier()).distinct().count() == block.getCertifications().size()
                : "check Certification Unicity ";
    }

    private void checkCertificationIsntForLeaverOrExcluded(DBBlock block) {
        var excAndLeav = Stream.concat(block.getExcluded().stream().map(Excluded::getPubkey), block.getLeavers().stream().map(Leaver::getPubkey)).collect(Collectors.toList());

        block.getCertifications().stream().map(Certification::getCertified).forEach(c -> {
            assert excAndLeav.contains(c) : "check Certification Isnt For Leaver Or Excluded";
        });
    }

    private void checkTxVersion(Transaction tx) {
        assert Stream.of(10, 11).anyMatch(v -> v.equals(tx.getVersion())) : "Tx must be of version 10, 11";
    }

    private void checkTxIssuers(Transaction tx) {
        assert tx.getIssuers().size() > 0 : "Tx must have at least one Issuer";
    }

    private void checkTxSources(Transaction tx) {
        assert tx.getInputs().size() > 0 : "Tx must have at least one Source";
    }

    private void checkTxRecipients(Transaction tx) {
        assert tx.getOutputs().size() > 0 : "Tx must have at least one recipient";
    }

    private void checkTxAmounts(Transaction tx) {
        assert tx.getInputs().stream().mapToInt(TxInput::getAmount).sum() == tx.getOutputs().stream().mapToInt(TxOutput::getAmount).sum()
                : "Tx input and output amounts must match ";
    }

    private void checkTxSignature(Transaction tx) {
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
    private void checkMaxTransactionChainingDepth(DBBlock block) {
        assert block.getTransactions().size() <= 50 : "check Max Transaction Chaining Depth"; // FIXME implements
    }


    class Static implements BlockLocalValid {

        static Static singletons = make();

        private static Static make() {
            return new Static();
        }

        private Static() {
        }

        public static void assertBlock(DBBlock b) throws AssertionError {
            singletons.assertBlockLocalValid(b, true);
        }
    }


}
