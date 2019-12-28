package juniter.core.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@EqualsAndHashCode(callSuper = false)
public class CoreEvent<T> extends ApplicationEvent {

    @Getter
    @Setter
    private T what;

    @Getter
    @Setter
    protected String name = "CORE";

    @Getter
    @Setter
    private String message;

    public CoreEvent(T what, String message) {
        super(message);
        this.what = what;
        this.message = message;
    }

}