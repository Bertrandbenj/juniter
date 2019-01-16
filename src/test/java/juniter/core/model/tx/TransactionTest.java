package juniter.core.model.tx;

import juniter.core.crypto.Crypto;
import juniter.core.model.BStamp;
import org.junit.Test;

import java.util.List;

public class TransactionTest {

    @Test
    public void toDUPdoc() {
        var tx = new Transaction(
                10,
                "g1",
                0,
                null,
                new BStamp("58-00005B9167EBA1E32C6EAD42AE7F72D8F14B765D3C9E47D233B553D47C5AEE0C"),
                0,
                List.of("FVUFRrk1K5TQGsY7PRLwqHgdHRoHrwb1hcucp4C2N5tD"),
                List.of(new TxInput("1000:0:D:FVUFRrk1K5TQGsY7PRLwqHgdHRoHrwb1hcucp4C2N5tD:1")),
                List.of(new TxOutput("3:0:SIG(7vU9BMDhN6fBuRa2iK3JRbC6pqQKb4qDMGsFcQuT5cz)"),
                        new TxOutput("997:0:SIG(FVUFRrk1K5TQGsY7PRLwqHgdHRoHrwb1hcucp4C2N5tD)")),
                List.of(new TxUnlock("0:SIG(0)")),
                List.of("VWbvsiybM4L2X5+o+6lIiuKNw5KrD1yGZqmV+lHtA28XoRUFzochSIgfoUqBsTAaYEHY45vSX917LDXudTEzBg==") ,
                "Un petit cafe ;-)") ;
        var testHash = Crypto.hash(tx.toDUPdoc(true));


            assert "F98BF7A8BF82E76F5B69E70CEF0A07A08BFDB03561955EC57B254DB1E958529C".equals(testHash):
                    "nope " + testHash + "\n"+ tx.toDUPdoc(true);

        }

    @Test
    public void toDUPshort() {
    }
}