package juniter.core.validation;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import juniter.core.crypto.Crypto;
import juniter.core.model.Block;
import juniter.core.model.tx.Transaction;

/**
 *
 * @author BnimajneB
 *
 */
public interface LocalValid {

	Pattern CCY_PATTERN = Pattern.compile("[A-Za-z0-9]{2,50}");
	Pattern UID_PATTERN = Pattern.compile("^.{2,100}$");
	Pattern INT_PATTERN = Pattern.compile("[0-9]{1,19}");
	Pattern HASH_PATTERN = Pattern.compile("[A-Z0-9]{64}");
	Pattern PBKY_PATTERN = Pattern.compile("[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{44,45}");
	Pattern SIGN_PATTERN = Pattern.compile("^.{86}==$");


	default void assertBlock(Block block ) {

		assertBlockInnerHash(block);
		assertBlockHash(block);
		assertValidBlockSignature(block);

		block.getTransactions().forEach(tx -> {
			assertValidTxSignatures(tx);
		});
	}

	default void assertBlockHash(Block block){
		final var hash = Crypto.hash(block.signedPartSigned());

		assert hash.equals(block.getHash()) : "test BlockHash " + block.signedPartSigned();

	}

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


	default void assertValidBlockSignature(Block block) {



		assert Crypto.verify(block.signedPart(), block.getSignature().toString(),
				block.getIssuer()) : "test Valid Block Signature #" + block.getNumber() +
		"\n for : " + block.getIssuer() +
		"\n signed : " + block.getSignature() +
		"\n on part :\n" + block.signedPart();
	}

	default void assertValidTxSignatures(Transaction tx) {

		for (int i = 0; i < tx.getSignatures().size(); i++) {
			final var sign = tx.getSignatures().get(i).toString();
			final var iss = tx.getIssuers().get(i).toString();

			assert Crypto.verify(tx.toDUPdoc(false), sign, iss) : //
				"Signature isnt verified  " + sign
				+ "\n  for issuer : " + iss
				+ "\n  in transaction : " + tx.toDUPdoc(false);
		}

	}

	default boolean isG1(String currency) {
		return "g1".equals(currency);
	};

	default boolean isV10(Object x) {
		if (x == null)
			return false;
		if (x instanceof String)
			return "10".equals(x);
		if (x instanceof Integer || x instanceof Short)
			return (Integer) x == 10;
		return false;
	}

	default boolean matchCurrency(String currency) {
		return CCY_PATTERN.matcher(currency).matches();
	}

	default boolean matchHash(String hash) {
		return HASH_PATTERN.matcher(hash).matches();
	}

	default boolean matchInt(String _int) {
		return INT_PATTERN.matcher(_int).matches();
	}

	default boolean matchPubkey(String pubkey) {
		return PBKY_PATTERN.matcher(pubkey).matches();
	}

	default boolean matchSignature(String sign) {
		return SIGN_PATTERN.matcher(sign).matches();
	}

	default boolean matchUserIdentifier(String hash) {
		return UID_PATTERN.matcher(hash).matches();
	}

	private Stream<String> permutation(String str) {

		final char[] charset = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz+-=/ ".toCharArray();

		final var res = new ArrayList<String>();

		for (int i = 0; i < str.length(); i++) {

			final var before = str.substring(0, i);
			final var after = str.substring(i + 1, str.length());

			for (int j = 0; j < charset.length; j++) {

				res.add(before + charset[j] + after);
			}

		}

		return res.stream();
	}

	private void tryPermutations (Block block) {
		permutation(block.signedPart()).forEach(sign -> {
			System.out.println("testing " + sign);

			if(Crypto.verify(sign, block.getSignature().toString(),
					block.getIssuer())) {

				assert false : "dfsdfdsf";
			}
		});

		permutation(block.getSignature().toRaw()).forEach(sign -> {
			System.out.println("testing " + sign);

			if (Crypto.verify(sign, block.getSignature().toString(),
					block.getIssuer())) {

				assert false : "dfsdfdsf";
			}
		});
	}

	default boolean verifySignature(String unSignedDoc, String signature, String pk) {
		return Crypto.verify(unSignedDoc, signature, pk);
	}

}
