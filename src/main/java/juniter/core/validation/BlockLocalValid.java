package juniter.core.validation;

import juniter.core.crypto.Crypto;
import juniter.core.model.Block;
import juniter.core.model.tx.Transaction;

public interface BlockLocalValid extends LocalValid {

	default void assertBlockInnerHash(Block block) {
		final var hash = Crypto.hash(block.toDUP(false, false));

		assert hash.equals(block.getInner_hash()) : //
			"assert Block InnerHash #" + block.getNumber() + " - " +
			"\niss      : " + block.getIssuer() +
			"\nsign     : " + block.getSignature() +
			"\nexpected : " + block.getInner_hash() +
			"\n but got : " + hash +
			"\n on      : " + block.toDUP(false, false);
	}

	default void assertBlockLocalValid(Block block)  {

		assertBlockInnerHash(block);
		assertBlockHash(block.signedPartSigned(), block.getHash());
		assertSignature(block.signedPart(), block.getSignature().toString(), block.getIssuer());

		block.getTransactions().forEach(tx -> {
			assertValidTxSignatures(tx);
		});
	}


	default void assertValidTxSignatures(Transaction tx) {

		for (int i = 0; i < tx.getSignatures().size(); i++) {
			final var sign = tx.getSignatures().get(i);
			final var iss = tx.getIssuers().get(i).toString();

			assert Crypto.verify(tx.toDUPdoc(false), sign, iss) : //
				"Signature isn't verified  " + sign
				+ "\n  for issuer : " + iss
				+ "\n  in transaction : " + tx.toDUPdoc(false);
		}

	}


	/**
	 * Exception safe validation to use as boolean
	 *
	 * @param block to validate
	 * @return true if the block is valid
	 */
	default boolean checkBlockisLocalValid(Block block) {

		try {
			assertBlockLocalValid(block);
			return true;
		}catch(final AssertionError ea){
			System.out.println("checkBlockisLocalValid At block " + block.getNumber() + ea. getMessage());
		}

		return false;
	}

	default void tryPermutations(Block block) {
		permutation(block.signedPart()).forEach(sign -> {
			System.out.println("testing " + sign);

			assert !Crypto.verify(sign, block.getSignature().toString(),
					block.getIssuer()) : "dfsdfdsf";
		});

		permutation(block.getSignature()).forEach(sign -> {
			System.out.println("testing " + sign);

			assert !Crypto.verify(sign, block.getSignature().toString(),
					block.getIssuer()) : "dfsdfdsf";
		});
	}

}
