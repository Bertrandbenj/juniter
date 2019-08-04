package juniter.core.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class CoreEvent<T> extends ApplicationEvent {

    private T what;

    protected String name = "CORE";

    private String message;

    public CoreEvent(T what, String message ) {
        super(message);
        this.what = what;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}