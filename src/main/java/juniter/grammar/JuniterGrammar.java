package juniter.grammar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import antlr.main.JuniterParser.DocContext;
import antlr.main.JuniterParser.IdentityContext;
import antlr.main.JuniterParser.PeerContext;
import antlr.main.JuniterParser.WotContext;
import antlr.main.JuniterParserBaseVisitor;
import juniter.core.model.BStamp;
import juniter.core.validation.LocalValid;

public class JuniterGrammar extends JuniterParserBaseVisitor<Document> implements LocalValid {

	private static final Logger LOG = LogManager.getLogger();

	@Override
	public Document visitDoc(DocContext ctx) {

		final var res = super.visitDoc(ctx);

		return res.isValid() ? res : null;
	}

	@Override
	public Document visitIdentity(IdentityContext ctx) {

		final var res = new IdentityDocument();
		res.setIssuer(ctx.issuer().getText());
		res.setCurrency(ctx.currency().getText());
		res.setUniqueID(ctx.userid().getText());

		final var bstamp = ctx.timestamp().buid();
		res.setTimestamp(new BStamp( //
				Integer.parseInt(bstamp.bnum().getText()), //
				bstamp.bhash().getText()));
		res.setVersion(ctx.version().getText());
		res.setSignature(((WotContext) ctx.getParent()).signature().getText());
		LOG.info("parsed: \n" + res);
		return res;
	}

	@Override
	public Document visitPeer(PeerContext ctx) {

		final var res = new PeerDocument();
		res.setPubkey(ctx.pubkey().getText());
		res.setCurrency(ctx.currency().getText());
		final var bnum = Integer.parseInt(ctx.block().buid().bnum().getText());
		final var bhash = ctx.block().buid().bhash().getText();
		res.setBlock(new BStamp(bnum, bhash));
		LOG.info("parsed: \n" + res);
		return res;
	}

}
