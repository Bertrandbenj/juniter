package juniter.core.model.wso;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Body {
    private String name;
    private Map<String, Object> params = new HashMap<>();

    public Body(String string) {
        name = string;
    }

}