package juniter.core.validation.meta;

import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.meta.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@SupportedValidationTarget({ValidationTarget.ANNOTATED_ELEMENT })
public class BlockValidator implements ConstraintValidator<BlockConstraint, DUPBlock> {
    private static final Logger LOG = LogManager.getLogger(BlockValidator.class);

    private ChainParameters conf = new ChainParameters();

    private List<Short> AVAILABLE_VERSIONS = List.of((short) 10, (short) 11);


    private boolean checkPow = true;
    private boolean checkSignature = true;
    private boolean checkHash = true;


    @Override
    public void initialize(BlockConstraint constraintAnnotation) {
        checkPow = constraintAnnotation.checkPow();
        checkSignature = constraintAnnotation.checkSignature();
        checkHash = constraintAnnotation.checkHash();
    }

    @Override
    public boolean isValid(DUPBlock block, ConstraintValidatorContext context) {
        boolean res = true;
        LOG.debug("BlockValidator.isValid(" + block + ")  ");


        if (block == null) {
            return true;
        }

        if (!Crypto.hash(block.signedPartSigned()).equals(block.getHash())) {
            context.buildConstraintViolationWithTemplate("Hash invalid ").addConstraintViolation();
            res = false;
        }

        if (!Crypto.verify(block.signedPart(), block.getSignature(), block.getIssuer())) {
            context.buildConstraintViolationWithTemplate("Signature invalid ").addConstraintViolation();
            res = false;
        }


        for (DUPTransaction tx : block.getTransactions()) {
            if (tx.getInputs().stream().mapToInt(DUPInput::getAmount).sum() !=
                    tx.getOutputs().stream().mapToInt(DUPOutput::getAmount).sum()) {
                context.buildConstraintViolationWithTemplate("Input sum must match Output sum ").addConstraintViolation();
                res = false;
            }

            if (tx.getInputs().size() <= 0) {
                context.buildConstraintViolationWithTemplate("Tx must have at least one Source").addConstraintViolation();
                res = false;
            }

            if (tx.getIssuers().size() <= 0) {
                context.buildConstraintViolationWithTemplate("Tx must have at least one Issuer").addConstraintViolation();
                res = false;
            }

            if (tx.getOutputs().size() <= 0) {
                context.buildConstraintViolationWithTemplate("Tx must have at least one recipient").addConstraintViolation();
                res = false;
            }

            if (checkSignature) {
                for (int i = 0; i < tx.getSignatures().size(); i++) {
                    final var sign = tx.getSignatures().get(i);
                    final var iss = tx.getIssuers().get(i);

                    if (!Crypto.verify(tx.toDUPdoc(false), sign, iss)) {
                        context.buildConstraintViolationWithTemplate("TX Signature wrong ").addConstraintViolation();
                        res = false;
                    }
                }
            }

            if (AVAILABLE_VERSIONS.stream().noneMatch(v -> v.equals(tx.getVersion()))) {
                context.buildConstraintViolationWithTemplate("Tx must be of getVersion 10, 11").addConstraintViolation();
                res = false;
            }
        }


        if ((block.params() == null) == (block.getNumber() == 0)) {
            context.buildConstraintViolationWithTemplate("Chain Parameters MUST be present on block 0  &  MUST be null when block > 0 ").addConstraintViolation();
            res = false;
        }

        if (!(block.getNumber() != 0 || block.getDividend() == null)) {
            context.buildConstraintViolationWithTemplate("Dividend MUST BE null on Block 0 ").addConstraintViolation();
            res = false;
        }


        if (checkPow) {
            if (!block.getHash().startsWith("0000")) {
                context.buildConstraintViolationWithTemplate("check Proof of Work ").addConstraintViolation();
                res = false;
            }
        }

        if (checkHash) {
            if (!Crypto.hash(block.toDUP(false, false)).equals(block.getInner_hash())) {
                context.buildConstraintViolationWithTemplate("block inner hash invalid at " + block).addConstraintViolation();
                res = false;
            }
        }


        if ((block.getNumber() == 0) == (block.getPreviousHash() != null)) {
            context.buildConstraintViolationWithTemplate("check Previous Hash").addConstraintViolation();
            res = false;
        }

        if ((block.getNumber() == 0) == (block.getPreviousIssuer() != null)) {
            context.buildConstraintViolationWithTemplate("check Previous Issuer").addConstraintViolation();
            res = false;
        }

        if (!(block.getUnitbase().equals(0) || block.getNumber() > 0)) {
            context.buildConstraintViolationWithTemplate("check Unit base").addConstraintViolation();
            res = false;
        }


        if (block.getNumber() == 0) {
            if (!block.getTime().equals(block.getMedianTime())) {
                context.buildConstraintViolationWithTemplate("Root block's Time & MedianTime must be equal.").addConstraintViolation();
                res = false;
            }
        } else {
            if (!(block.getTime() >= block.getMedianTime() && block.getTime() <= block.getMedianTime() + conf.maxAcceleration())) {
                context.buildConstraintViolationWithTemplate("A block must have its Time field be between [MedianTime ; MedianTime + maxAcceleration]." + block.getMedianTime() + " <= " + block.getTime() + " <= " + block.getMedianTime() + conf.maxAcceleration()).addConstraintViolation();
                res = false;
            }
        }

        if (checkSignature) {
            if (!Crypto.verify(block.signedPart(), block.signature(), block.getIssuer())) {
                context.buildConstraintViolationWithTemplate("A block must have its Signature match the DUP protocol ")
                        .addConstraintViolation();
                res = false;
            }

            for (DUPIdentity i : block.getIdentities()) {
                if (!Crypto.verify(i.toDUPdoc(false), i.getSignature(), i.getPubkey())) {
                    context.buildConstraintViolationWithTemplate("A block cannot contain identities whose signature does not match the identity's content at " + block + "\n" + i)
                            .addConstraintViolation();
                    res = false;
                }
            }

            for (DUPMember m : block.getMembers()) {
                if (!Crypto.verify(m.toDUPdoc(false), m.getSignature(), m.getPubkey())) {
                    context.buildConstraintViolationWithTemplate("check Memberships Signature on " + m)
                            .addConstraintViolation();
                    res = false;
                }
            }
        }


        if (block.getIdentities().size() != block.getIdentities().stream()
                .map(DUPIdentity::getUid)
                .distinct()
                .count()) {
            context.buildConstraintViolationWithTemplate(" Block must not contain twice same identity userid at " + block + " " + block.getIdentities()).addConstraintViolation();
            res = false;
        }

        if (block.getIdentities().size() != block.getIdentities().stream()
                .map(DUPIdentity::getPubkey)
                .distinct()
                .count()) {
            context.buildConstraintViolationWithTemplate(" Block must not contain twice same identity pubkey at " + block + " " + block.getIdentities()).addConstraintViolation();
            res = false;
        }


        if (block.getMembers().stream().map(DUPMember::getPubkey).distinct().count() != block.getMembers().size()) {
            context.buildConstraintViolationWithTemplate("check Membership Unicity ").addConstraintViolation();
            res = false;
        }

        if (block.getRevoked().stream().map(DUPMember::getPubkey).distinct().count() != block.getRevoked().size()) {
            context.buildConstraintViolationWithTemplate("check Revoked Unicity ").addConstraintViolation();
            res = false;
        }


        if (!block.getRevoked().stream()
                .map(DUPMember::getPubkey)
                .allMatch(p -> block.getExcluded().stream().map(DUPMember::getPubkey).anyMatch(x -> x.equals(p)))) {
            context.buildConstraintViolationWithTemplate("check Excluded Unicity ").addConstraintViolation();
            res = false;
        }


        for (DUPMember dupMember : block.getJoiners()) {
            String j = dupMember.getPubkey();
            if (block.getIdentities().stream().map(HasPubkey::getPubkey).noneMatch(i -> i.equals(j))) {
                context.buildConstraintViolationWithTemplate("check Identities Match Join").addConstraintViolation();
                res = false;
            }
        }

        if (!(block.getNumber() == 0 || block.getCertifications().stream().map(DUPCertification::getCertifier).distinct().count() == block.getCertifications().size())) {
            context.buildConstraintViolationWithTemplate("check Certification One By Issuer").addConstraintViolation();
            res = false;
        }

        if (!(block.getCertifications().stream().map(c -> c.getCertified() + c.getCertifier()).distinct().count() == block.getCertifications().size())) {
            context.buildConstraintViolationWithTemplate("check Certification Unicity").addConstraintViolation();
            res = false;
        }

        var excAndLeav = Stream.concat(block.getExcluded().stream().map(DUPMember::getPubkey), block.getLeavers().stream().map(DUPMember::getPubkey)).collect(Collectors.toList());
        for (DUPCertification dupCertification : block.getCertifications()) {
            String c = dupCertification.getCertified();
            if (excAndLeav.contains(c)) {
                context.buildConstraintViolationWithTemplate("check Certification Isnt For Leaver Or Excluded").addConstraintViolation();
                res = false;
            }
        }

        if (block.getTransactions().size() > 50) { // FIXME TODO
            context.buildConstraintViolationWithTemplate("checkMaxTransactionChainingDepth")
                    .addConstraintViolation();
            res = false;
        }

        LOG.debug(" finished isValid, result:" + res);
        return res;
    }
}