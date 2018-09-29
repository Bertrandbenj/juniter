package juniter.model.persistence.tx;

import juniter.utils.Constants;

public interface OutCondition {

	/**
	 *
	 * @author ben
	 */
	public class And implements OutCondition {
		OutCondition left, right;

		public And(OutCondition cond1, OutCondition cond2) {
			left = cond1;
			right = cond2;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof And))
				return false;
			return left.equals(((And) obj).left) //
					&& right.equals(((And) obj).right);
		}

		@Override
		public String toString() {
			return "(" + left + " && " + right + ")";
		}

	}

	public class CLTV implements OutCondition {
		protected Integer deadline;

		public CLTV(String deadline) {
			this.deadline = Integer.parseInt(deadline);
		}

		public Integer getDeadline() {
			return deadline;
		}

		@Override
		public String toString() {
			return "CLTV(" + deadline + ")";
		}
	}

	public class CSV implements OutCondition {
		protected Integer amountToWait;

		public CSV(String amountToWait) {
			this.amountToWait = Integer.parseInt(amountToWait);
		}

		public Integer getAmountToWait() {
			return amountToWait;
		}

		@Override
		public String toString() {
			return "CSV(" + amountToWait + ")";
		}
	}

	public class Or implements OutCondition {
		OutCondition left, right;

		public Or(OutCondition cond1, OutCondition cond2) {
			left = cond1;
			right = cond2;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Or))
				return false;
			return left.equals(((Or) obj).left) //
					&& right.equals(((Or) obj).right);
		}

		@Override
		public String toString() {
			return "(" + left + " || " + right + ")";
		}

	}

	public class SIG implements OutCondition {

		protected String pubkey;

		public SIG(String pubkey) throws Exception {
			this.pubkey = pubkey;
			if (!pubkey.matches(Constants.Regex.PUBKEY))
				throw new Exception("parsing " + pubkey);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof SIG))
				return false;
			return pubkey.equals(((SIG) obj).pubkey);
		}

		String getPubkey() {
			return pubkey;
		}

		@Override
		public String toString() {
			return "SIG(" + pubkey + ")";
		}
	}

	public class XHX implements OutCondition {

		protected String hash;

		public XHX(String cond) throws Exception {
			hash = cond;
			if (!cond.matches(Constants.Regex.HASH))
				throw new Exception("parsing " + cond);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof XHX))
				return false;
			return hash.equals(((XHX) obj).hash);
		}

		String getHash() {
			return hash;
		}

		@Override
		public String toString() {
			return "XHX(" + hash + ")";
		}
	}

	static OutCondition parse(String cond) throws Exception {

		OutCondition res = null;

		// Simple cases SIG first because its the most frequent
		if ("SIG".equals(cond.substring(0, 3))) {
			res = new SIG(cond.substring(4, cond.length() - 1));
			TxOutput.logger.debug("Parsed SIG " + cond);
		}
		if ("XHX".equals(cond.substring(0, 3))) {
			res = new XHX(cond.substring(4, cond.length() - 1));
			TxOutput.logger.debug("Parsed XHX " + cond);
		}
		if ("CSV".equals(cond.substring(0, 3))) {
			res = new CSV(cond.substring(4, cond.length() - 1));
			TxOutput.logger.debug("Parsed CSV " + cond);
		}
		if ("CLTV".equals(cond.substring(0, 4))) {
			res = new CSV(cond.substring(5, cond.length() - 1));
			TxOutput.logger.debug("Parsed CLTV " + cond);
		}

		// recursive cases
		if (cond.startsWith("(")) {
			final String subcond = cond.substring(1, cond.length() - 1);
			TxOutput.logger.debug(" - subcond " + subcond + "\nwhen parsing " + cond);
			var i = 0;

			while ((i = subcond.indexOf("&&", i)) != -1) { // for each occurence of operator

				final var right = subcond.substring(i + 2).trim(); // take right
				final var left = subcond.substring(0, i).trim(); // take left
				TxOutput.logger.info(" - tryAnd " + i + " " + left + " ___________ " + right);
				And parsed = null;
				try {
					parsed = new And(parse(left), parse(right));
					TxOutput.logger.info("Parsed And " + parsed);

					return parsed;
				} catch (final Exception e) {
					i++;
					TxOutput.logger.error(e.getMessage());
				}
			}

			while ((i = subcond.indexOf("||", i)) != -1) { // for each occurence of operator

				final var right = subcond.substring(i + 2).trim(); // take right
				final var left = subcond.substring(0, i).trim(); // take left
				TxOutput.logger.info(" - tryOr " + i + " " + left + " - " + right);

				Or parsed = null;
				try {
					parsed = new Or(parse(left), parse(right));
					TxOutput.logger.info("Parsed Or " + parsed);

					return parsed;
				} catch (final Exception e) {
					i++;
					TxOutput.logger.error(e.getMessage());
				}

			}

		}
		if (res == null)
			throw new Exception("while Parsing " + cond);

		return res;
	}

	default String getCondition() {
		return toString();
	}

}
