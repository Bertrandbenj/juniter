package juniter.core.model.dbo.index;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "acc", types = { SINDEX.class })
public interface ExProjection {

    @Value("#{target.op}")
    String getOp();

    @Value("#{target.identifier}")
    String getIdentifier();
}


