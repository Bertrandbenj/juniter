package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeerBMA implements Serializable {

    private static final long serialVersionUID = -6400215527778830671L;

    private String peer ;


}