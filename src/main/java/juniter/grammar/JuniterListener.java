package juniter.grammar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.grammar.antlr4.JuniterBaseListener;
import juniter.grammar.antlr4.JuniterParser.AndContext;
import juniter.grammar.antlr4.JuniterParser.BuidContext;
import juniter.grammar.antlr4.JuniterParser.Currency_Context;
import juniter.grammar.antlr4.JuniterParser.DoctypeContext;
import juniter.grammar.antlr4.JuniterParser.IdentityContext;
import juniter.grammar.antlr4.JuniterParser.IssuerContext;
import juniter.grammar.antlr4.JuniterParser.SignatureContext;
import juniter.grammar.antlr4.JuniterParser.Timestamp_Context;
import juniter.grammar.antlr4.JuniterParser.Version_Context;

public class JuniterListener extends JuniterBaseListener {

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
	public void exitCurrency_(Currency_Context ctx) {
		super.exitCurrency_(ctx);
		logger.info("exitCurrency " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitDoctype(DoctypeContext ctx) {
		super.exitDoctype(ctx);
		logger.info("exitDoctype " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitIdentity(IdentityContext ctx) {
		logger.info("exitIdentity " + ctx.toStringTree() + ctx);
		super.exitIdentity(ctx);
	}

	@Override
	public void exitIssuer(IssuerContext ctx) {
		super.exitIssuer(ctx);
		logger.info("exitIssuer " + ctx + " " + ctx.toStringTree());
	}

	@Override
	public void exitSignature(SignatureContext ctx) {
		super.exitSignature(ctx);
		logger.info("exitSignature " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitTimestamp_(Timestamp_Context ctx) {
		super.exitTimestamp_(ctx);
		logger.info("exitTimestamp_ " + ctx + " " + ctx.toStringTree());

	}

	@Override
	public void exitVersion_(Version_Context ctx) {
		super.exitVersion_(ctx);

		logger.info("exitVersion_ " + ctx.toStringTree() + ctx);
	}
}
