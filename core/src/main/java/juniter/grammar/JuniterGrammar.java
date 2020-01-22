package juniter.grammar;

import generated.antlr.JuniterParser;
import generated.antlr.JuniterParser.IdentityContext;
import generated.antlr.JuniterParser.PeerContext;
import generated.antlr.JuniterParserBaseVisitor;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.net.Peer;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dbo.wot.Revoked;
import juniter.core.model.meta.DUPDocument;
import juniter.core.model.meta.DUPPeer;
import juniter.core.model.meta.SimpleIssuer;
import juniter.core.validation.LocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JuniterGrammar extends JuniterParserBaseVisitor<DUPDocument> implements LocalValid {

    private static final Logger LOG = LogManager.getLogger(JuniterGrammar.class);


    /**
     * FIXME for some reason, I manually visit children here, there must be some missing piece
     *
     * @param ctx context
     * @return the Wot related document including signature
     */
    @Override
    public DUPDocument visitWot(JuniterParser.WotContext ctx) {
        DUPDocument tmp = null;
        try {
            tmp = visitIdentity(ctx.identity());
        } catch (Exception e) {
            try {
                tmp = visitCertification(ctx.certification());
            } catch (Exception e1) {
                try {
                    tmp = visitMembership(ctx.membership());
                } catch (Exception e2) {
                    try {
                        tmp = visitRevocation(ctx.revocation());
                    } catch (Exception e3) {
                        LOG.info("Really couldn't parse that sorry, its probably not a WoT Document ");
                    }
                }
            }
        }

        ((SimpleIssuer)tmp).setSignature(ctx.signature.getText());
        LOG.info("visiting visitWot " + tmp);

        return tmp;
    }

    @Override
    public DUPDocument visitIdentity(IdentityContext ctx) {
        return new Identity(
                ctx.version().getText(),
                ctx.currency().getText(),
                ctx.issuer().getText(),
                ctx.userid().getText(),
                ctx.timestamp().buid().getText()
        );
    }

    @Override
    public DUPDocument visitCertification(JuniterParser.CertificationContext ctx) {
        var res =  new Certification();
        res.setVersion(Short.valueOf(ctx.version().getText()));
        res.setSignature(ctx.idtySignature().getText());
        res.setSignedOn(Integer.valueOf(ctx.certTimestamp().buid().bnum().getText()));
        res.setCertifier( ctx.issuer().getText());
        res.setCertified(ctx.idtyIssuer().pubkey().getText());


        return res ;
    }

    @Override
    public DUPDocument visitMembership(JuniterParser.MembershipContext ctx) {
        var res = new Member();
        res.setPubkey(ctx.issuer().getText());
        res.setWritten(new BStamp(ctx.block().getText()));
        res.setUid(ctx.userID().getText());


        return new Member();
//                ctx.getVersion.getText(),
//                ctx.currency.getText(),
//                ctx.issuer().getText(),
//                ctx.block().getText(),
//                ctx.member().getText(),
//                ctx.userID().getText(),
//                ctx.certTS().getText()
//        );
    }

    @Override
    public DUPDocument visitRevocation(JuniterParser.RevocationContext ctx) {
        var res = new Revoked();
        res.setSignature( ctx.idtySignature().getText());
        res.setPubkey(ctx.issuer().getText());
        res.setI_block_uid(ctx.idtyTimestamp().buid().getText());
        res.setCurrency(ctx.currency().getText());

        return res;
    }

    @Override
    public DUPDocument visitPeer(PeerContext ctx) {

        DUPPeer res = new Peer();
        res.setCurrency(ctx.currency().getText());
        res.setPubkey(ctx.pubkey().getText());
        res.setBlock(new BStamp(ctx.block().buid().getText()));
        return res;

    }


    @Override
    public DUPDocument visitBlock_(JuniterParser.Block_Context ctx) {
        return new DBBlock();
    }
}
