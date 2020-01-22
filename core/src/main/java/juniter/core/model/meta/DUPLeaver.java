package juniter.core.model.meta;

public interface DUPLeaver extends DUPMember {

    @Override
    default String type(){
        return "Leaver";
    }
}
