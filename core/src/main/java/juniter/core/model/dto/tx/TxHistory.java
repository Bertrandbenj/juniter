package juniter.core.model.dto.tx;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
public class TxHistory   {

	private String currency ;
	private String pubkey ; 
	private History history ;


}
