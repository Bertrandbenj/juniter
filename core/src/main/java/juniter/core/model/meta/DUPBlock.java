package juniter.core.model.meta;

import juniter.core.model.dbo.ChainParameters;
import juniter.core.validation.meta.BlockConstraint;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

@BlockConstraint
public interface DUPBlock extends DUPDocument, HasIssuer, HasTime, HasMedianTime, HasSignedPart, HasPrevious {

    Validator validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

    @Override
    default String issuer() {
        return getIssuer();
    }

    default String signature() {
        return getSignature();
    }

    List<? extends DUPIdentity> getIdentities();

    Integer getNumber();

    Integer getDividend();

    @Override
    default String type() {
        return "Block";
    }


    @Valid
    @BlockConstraint
    default DUPBlock check(@BlockConstraint DUPBlock bl) {
        @BlockConstraint DUPBlock x = bl;
        System.out.println("checking ");
        return x;
    }

    default Set<ConstraintViolation<DUPBlock>> checked() {
        return validator.validate(this);
    }

    String getHash();

    List<? extends DUPTransaction> getTransactions();

    ChainParameters getParameters();

    ChainParameters params();

    Integer getUnitbase();

    List<? extends DUPJoiner> getJoiners();

    List<? extends DUPMember> getMembers();

    List<? extends DUPRevoked> getRevoked();

    List<? extends DUPExcluded> getExcluded();

    List<? extends DUPLeaver> getLeavers();

    List<? extends DUPRenew> getRenewed();

    List<? extends DUPCertification> getCertifications();


    /**
     * Method returning node as a Raw format
     *
     * @return the DUPComponent as text format
     */
    default String toDUP(boolean signed, boolean innerHash) {


        String paramOrPrevious = "";
        if (getNumber().equals(0)) {
            paramOrPrevious += "\nParameters: " + params().toDUP();
        } else {
            paramOrPrevious += "\nPreviousHash: " + getPreviousHash();
            paramOrPrevious += "\nPreviousIssuer: " + getPreviousIssuer();
        }

        //
        var idtis = new StringJoiner("\n");
        for (DUPIdentity identity : getIdentities()) {
            String toDUP = identity.toDUP();
            idtis.add(toDUP);
        }
        var joins = new StringJoiner("\n");
        for (DUPJoiner joiner1 : getJoiners()) {
            String toDUP = joiner1.toDUP();
            joins.add(toDUP);
        }
        var renews = new StringJoiner("\n");
        for (DUPRenew renew : getRenewed()) {
            String toDUP = renew.toDUP();
            renews.add(toDUP);
        }
        var leavs = new StringJoiner("\n");
        for (DUPLeaver leaver : getLeavers()) {
            String toDUP = leaver.toDUP();
            leavs.add(toDUP);
        }
        var revks = new StringJoiner("\n");
        for (DUPRevoked revoked1 : getRevoked()) {
            String toDUP = revoked1.toDUP();
            revks.add(toDUP);
        }
        var excls = new StringJoiner("\n");
        for (DUPExcluded excluded1 : getExcluded()) {
            String toDUP = excluded1.toDUP();
            excls.add(toDUP);
        }
        var certs = new StringJoiner("\n");
        for (DUPCertification certification : getCertifications()) {
            String toDUP = certification.toDUP();
            certs.add(toDUP);
        }
        var txs = new StringJoiner("\n");
        for (DUPTransaction transaction : getTransactions()) {
            String toDUPshort = transaction.toDUPshort();
            txs.add(toDUPshort);
        }
        return new StringBuilder()
                .append("Version: ").append(getVersion())
                .append("\nType: Block")
                .append("\nCurrency: ").append(getCurrency())
                .append("\nNumber: ").append(getNumber())
                .append("\nPoWMin: ").append(getPowMin())
                .append("\nTime: ").append(getTime())
                .append("\nMedianTime: ").append(getMedianTime())
                .append((getDividend() != null && getDividend() >= 1000) ? "\nUniversalDividend: " + getDividend() : "")
                .append("\nUnitBase: ").append(getUnitbase())
                .append("\nIssuer: ").append(getIssuer())
                .append("\nIssuersFrame: ").append(getIssuersFrame())
                .append("\nIssuersFrameVar: ").append(getIssuersFrameVar())
                .append("\nDifferentIssuersCount: ").append(getIssuersCount())
                .append(paramOrPrevious)
                .append("\nMembersCount: ").append(getMembersCount())
                .append("\nIdentities:\n").append(idtis.toString()).append(getIdentities().isEmpty() ? "" : "\n")
                .append("Joiners:\n").append(joins.toString()).append(getJoiners().isEmpty() ? "" : "\n")
                .append("Actives:\n").append(renews.toString()).append(getRenewed().isEmpty() ? "" : "\n")
                .append("Leavers:\n").append(leavs.toString()).append(getLeavers().isEmpty() ? "" : "\n")
                .append("Revoked:\n").append(revks.toString()).append(getRevoked().isEmpty() ? "" : "\n")
                .append("Excluded:\n").append(excls.toString()).append(getExcluded().isEmpty() ? "" : "\n")
                .append("Certifications:\n").append(certs.toString()).append(getCertifications().isEmpty() ? "" : "\n")
                .append("Transactions:\n").append(txs.toString()).append(getTransactions().isEmpty() ? "" : "\n")
                .append(innerHash ? "InnerHash: " + getInner_hash() : "")
                .append(signed || innerHash ? "\nNonce: " + getNonce() + "\n" : "")
                .append(signed ? getSignature() : "")
                .toString();
    }

    Integer getIssuersCount();

    Integer getIssuersFrameVar();

    Integer getIssuersFrame();

    Integer getMembersCount();

    Integer getPowMin();
}
