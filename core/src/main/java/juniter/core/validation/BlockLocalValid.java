package juniter.core.validation;

import juniter.core.crypto.Crypto;
import juniter.core.model.DBBlock;
import juniter.core.model.tx.Transaction;
import juniter.core.model.wot.Identity;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Original TypeScript here
 * https://git.duniter.org/nodes/typescript/duniter/blob/dev/app/lib/rules/local_rules.ts
 */
public interface BlockLocalValid extends LocalValid {

    /**
     * Exception safe validation to use as boolean
     *
     * @param block to completeGlobalScope
     * @return true if the block is valid
     */
    default boolean checkBlockIsLocalValid(DBBlock block) {

        try {
            assertBlockLocalValid(block, true);
            return true;
        } catch (final AssertionError ea) {
            System.out.println("checkBlockIsLocalValid At block " + block.getNumber() +" - "+ ea.getMessage());
        }

        return false;
    }


    /**
     * @param block
     */
    default void assertBlockLocalValid(DBBlock block, boolean checkPowAndSignature) {

        assertBlockHash(block.signedPartSigned(), block.getHash());
        assertSignature(block.signedPart(), block.getSignature(), block.getIssuer());

        block.getTransactions().forEach(tx -> {
            assertValidTxSignatures(tx);
        });

        checkParameters(block);
        if (checkPowAndSignature)
            checkProofOfWork(block);
        checkInnerHash(block);
        checkPreviousHash(block);
        checkPreviousIssuer(block);
        checkUnitBase(block);
        if (checkPowAndSignature)
            checkBlockSignature(block);
        checkBlockTimes(block);
        checkIdentitiesSignature(block);
        checkIdentitiesUserIDConflict(block);
        checkIdentitiesPubkeyConflict(block);
        checkIdentitiesMatchJoin(block);
        checkMembershipUnicity(block);
        checkRevokedUnicity(block);
        checkRevokedAreExcluded(block);
        checkMembershipsSignature(block);
        checkPubkeyUnicity(block);
        checkCertificationOneByIssuer(block);
        checkCertificationUnicity(block);
        checkCertificationIsntForLeaverOrExcluded(block);
        checkTxVersion(block);
        checkTxIssuers(block);
        checkTxSources(block);
        checkTxRecipients(block);
        checkTxAmounts(block);
        checkTxSignature(block);
        checkMaxTransactionChainingDepth(block);

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


    private void assertValidTxSignatures(Transaction tx) {

        for (int i = 0; i < tx.getSignatures().size(); i++) {
            final var sign = tx.getSignatures().get(i);
            final var iss = tx.getIssuers().get(i);

            assert Crypto.verify(tx.toDUPdoc(false), sign, iss) : //
                    "TX Signature wrong " + sign
                            + "\n  for issuer : " + iss
                            + "\n  in transaction : " + tx.toDUPdoc(false);
        }

    }

    private void checkParameters(DBBlock block) {
        if(block.getNumber()==0 ){
            assert  block.getParameters() != null : "Chain Parameters MUST be present on block 0 ";
        }else{
            assert  block.getParameters() == null : "Chain Parameters MUST be null when block > 0 ";
        }
    }

    private void checkProofOfWork(DBBlock block) {
    }

    private void checkPreviousHash(DBBlock block) {
        matchHash(block.getPreviousHash());

    }

    private void checkPreviousIssuer(DBBlock block) {
        matchPubkey(block.getPreviousIssuer());
    }

    private void checkUnitBase(DBBlock block) {
        assert block.getUnitbase().equals(0) || block.getNumber() > 0 : " checkUnitBase " + block.getUnitbase();
    }

    private void checkBlockSignature(DBBlock block) {
        matchSignature(block.getSignature());
    }

    private void checkBlockTimes(DBBlock block) {

    }

    private void checkIdentitiesSignature(DBBlock block) {
        block.getIdentities().forEach(i ->{

        });

    }

    private void checkIdentitiesUserIDConflict(DBBlock block) {
        assert block.getIdentities().stream().map(Identity::getUid).distinct().count() == block.getIdentities().size()
                :" Block must not contain twice same identity uid ";
    }

    private void checkIdentitiesPubkeyConflict(DBBlock block) {
        assert block.getIdentities().stream().map(Identity::getPubkey).distinct().count() == block.getIdentities().size()
                :" Block must not contain twice same identity pubkey ";
    }

    private void checkIdentitiesMatchJoin(DBBlock block) {

    }

    private void checkMembershipUnicity(DBBlock block) {

    }

    private void checkRevokedUnicity(DBBlock block) {

    }

    private void checkRevokedAreExcluded(DBBlock block) {
    }

    private void checkMembershipsSignature(DBBlock block) {
    }

    private void checkPubkeyUnicity(DBBlock block) {
    }

    private void checkCertificationOneByIssuer(DBBlock block) {
    }

    private void checkCertificationUnicity(DBBlock block) {
    }

    private void checkCertificationIsntForLeaverOrExcluded(DBBlock block) {
    }

    private void checkTxVersion(DBBlock block) {
    }

    private void checkTxIssuers(DBBlock block) {
    }

    private void checkTxSources(DBBlock block) {
    }

    private void checkTxRecipients(DBBlock block) {
    }

    private void checkTxAmounts(DBBlock block) {
    }

    private void checkTxSignature(DBBlock block) {
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
     * @param block the input block
     */
    private void checkMaxTransactionChainingDepth(DBBlock block) {

    }


    class Functional implements BlockLocalValid {

        public static Function<DBBlock, Boolean> funBlockValid = block -> new Functional().checkBlockIsLocalValid(block);

        public static Predicate<DBBlock> predBlockValid = (block) -> new Functional().checkBlockIsLocalValid(block);

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
