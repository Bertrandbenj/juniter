package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MemberVO implements Serializable {

    private static final long serialVersionUID = -1234585527778830671L;

    private String pub;
    private String uid;
}