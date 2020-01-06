package juniter.core.model.dbo.index;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertRecord {

    private CINDEX cert;
    private IINDEX issuer;
    private IINDEX receiver;

}
