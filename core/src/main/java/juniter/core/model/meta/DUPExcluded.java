package juniter.core.model.meta;

public interface DUPExcluded extends DUPMember {

    String getPubkey();

    @Override
    default String toDUP() {
        return getPubkey();
    }
}
