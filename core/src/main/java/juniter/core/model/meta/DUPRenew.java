package juniter.core.model.meta;

public interface DUPRenew extends DUPMember{

    @Override
    default String type() {
        return "Active";
    }


}
