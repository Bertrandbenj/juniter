package juniter.core.model.dbo.index;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CertRecords {
    private List<CertRecord> records;
}

