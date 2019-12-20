package juniter.core.model.dto.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class History {
    private List<TransactionDTO> sending;
    private List<TransactionDTO> received;
    private List<TransactionDTO> receiving;
    private List<TransactionDTO> sent;
    private List<TransactionDTO> pending;

}
