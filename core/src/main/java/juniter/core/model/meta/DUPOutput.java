package juniter.core.model.meta;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface DUPOutput extends DUPComponent {

    @Positive
    Integer getAmount();

    @PositiveOrZero
    Integer getBase();

    String getCondition();

    @Override
    default String toDUP() {
        return getAmount() + ":" + getBase() + ":" + getConditionString();
    }

    String getConditionString();
}
