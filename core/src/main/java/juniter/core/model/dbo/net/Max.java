package juniter.core.model.dbo.net;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Max {
    private String pubkey;
    private Integer max;
}