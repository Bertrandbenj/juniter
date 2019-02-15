package juniter.core.model.dto;

import juniter.core.model.business.net.Peer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeafDTO implements Serializable {
    private static final long serialVersionUID = -4464547074954356696L;

    private String hash ;

    private Peer value;

}
