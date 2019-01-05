package juniter.grammar;

import antlr.main.JuniterParser;
import antlr.main.JuniterParser.IdentityContext;
import antlr.main.JuniterParser.PeerContext;
import antlr.main.JuniterParserBaseVisitor;
import juniter.core.validation.LocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JuniterGrammar extends JuniterParserBaseVisitor<Document> implements LocalValid {

	private static final Logger LOG = LogManager.getLogger();


	/**
	 * FIXME for some reason, I manually visit children here, there must be some missing piece
	 * @param ctx
	 * @return the Wot related document including signature
	 */
	@Override
	public Document visitWot(JuniterParser.WotContext ctx) {
		Document tmp = null;
		try{
			tmp = visitIdentity(ctx.identity());
		}catch (Exception e){
			try{
				tmp = visitCertification(ctx.certification());
			}catch (Exception e1){
				try{
					tmp = visitMembership(ctx.membership());
				}catch (Exception e2){
					try{
						tmp =  visitRevocation(ctx.revocation());
					}catch (Exception e3){
						LOG.info("Really couldn't parse that sorry, its probably not a WoT Document " );
					}
				}
			}
		}

		((WotDocument)tmp).setSignature(ctx.signature.getText());
		LOG.info("visiting visitWot " + tmp);

		return tmp;
	}

	@Override
	public Document visitIdentity(IdentityContext ctx) {
		return new IdentityDoc(
				ctx.version().getText(),
				ctx.currency().getText(),
				ctx.issuer().getText(),
				ctx.userid().getText(),
				ctx.timestamp().buid().getText()
		);
	}

	@Override
	public Document visitCertification(JuniterParser.CertificationContext ctx) {
		return new CertificationDoc(
				ctx.version.getText(),
				ctx.currency.getText(),
				ctx.issuer().getText(),
				ctx.idtyIssuer().pubkey().getText(),
				ctx.idtyUniqueID().USERID().getText(),
				ctx.idtyTimestamp().buid().getText(),
				ctx.idtySignature().getText(),
				ctx.certTimestamp().buid().getText()
		);
	}

	@Override
	public Document visitMembership(JuniterParser.MembershipContext ctx) {
		return new MembershipDoc(
				ctx.version.getText(),
				ctx.currency.getText(),
				ctx.issuer().getText(),
				ctx.block().getText(),
				ctx.member().getText(),
				ctx.userID().getText(),
				ctx.certTS().getText()
		);
	}

	@Override
	public Document visitRevocation(JuniterParser.RevocationContext ctx) {
		return new RevocationDoc(
				ctx.version.getText(),
				ctx.currency.getText(),
				ctx.issuer().getText(),
				ctx.idtyUniqueID().getText(),
				ctx.idtyTimestamp().buid().getText(),
				ctx.idtySignature().getText()
		);
	}

	@Override
	public Document visitPeer(PeerContext ctx) {
		return new PeerDoc(
				ctx.currency().getText(),
				ctx.pubkey().getText(),
				ctx.block().buid().getText()
		);
		//final var bnum = Integer.parseInt(ctx.block().buid().bnum().getText());
		//final var bhash = ctx.block().buid().bhash().getText();
	}

}
