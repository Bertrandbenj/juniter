package juniter.core.model.meta;

public interface DUPJoiner extends DUPMember {

    @Override
    default String toDUP() {
        return getPubkey() + ":" + getSignature() + ":" + getSigned() + ":" + getI_block_uid() + ":" + getUid();
    }
}
