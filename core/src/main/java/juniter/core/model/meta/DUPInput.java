package juniter.core.model.meta;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface DUPInput extends DUPComponent {

    @Positive
    Integer getAmount();

    @PositiveOrZero
    Integer getBase();

    @Enumerated(EnumType.ORDINAL)
    SourceType type();

    String getReference();

    Integer getIndex();

    @Override
    default String toDUP() {
        return getAmount() + ":" + getBase() + ":" + type()  + ":" + getReference() + ":" + getIndex();
    }

}
