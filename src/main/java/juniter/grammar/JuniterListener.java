package juniter.grammar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JuniterListener extends JuniterListener {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void exitAnd(AndContext ctx) {
		super.exitAnd(ctx);
		logger.info("exitAnd " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitBuid(BuidContext ctx) {
		super.exitBuid(ctx);
		logger.info("exitBuid " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitCurrency(CurrencyContext ctx) {
		super.exitCurrency(ctx);
		logger.info("exitCurrency " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitDoctype(DoctypeContext ctx) {
		super.exitDoctype(ctx);
		logger.info("exitDoctype " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitIssuer(IssuerContext ctx) {
		super.exitIssuer(ctx);
		logger.info("exitIssuer " + ctx + " " + ctx.toStringTree());
	}

	@Override
	public void exitTimestamp_(Timestamp_Context ctx) {
		super.exitTimestamp_(ctx);
		logger.info("exitTimestamp_ " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitVersion_(Version_Context ctx) {
		super.exitVersion_(ctx);
		logger.info("Exiting Version " + ctx + " " + ctx.toStringTree());
	}

}
