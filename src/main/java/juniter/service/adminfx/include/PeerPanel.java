package juniter.service.adminfx.include;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@ConditionalOnExpression("${juniter.useJavaFX:false}")
@Component
public class PeerPanel {


}
