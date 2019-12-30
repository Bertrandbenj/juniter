package juniter.service.hadoop;

import juniter.core.model.dbo.BStamp;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

import java.util.List;

@ConditionalOnExpression("${juniter.useHadoop:false}")
@Repository
public class UserRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;


    private String tableName = "users";

    public static byte[] CF_INFO = Bytes.toBytes("cfInfo");

    private byte[] qUser = Bytes.toBytes("number");
    private byte[] qEmail = Bytes.toBytes("hash");
    private byte[] qPassword = Bytes.toBytes("medianTime");

    public List<BStamp> findAll() {
        return hbaseTemplate.find(tableName, "cfInfo", new RowMapper<BStamp>() {
            @Override
            public BStamp mapRow(Result result, int rowNum) throws Exception {
                return new BStamp(Bytes.toInt(result.getValue(CF_INFO, qUser)),
                        Bytes.toString(result.getValue(CF_INFO, qEmail)),
                        Bytes.toLong(result.getValue(CF_INFO, qPassword)));
            }
        });

    }

    public BStamp save(final Integer number, final String hash,
                       final Long mediantTime) {
        return hbaseTemplate.execute(tableName, new TableCallback<BStamp>() {

            @Override
            public BStamp doInTable(HTableInterface table) throws Throwable {
                BStamp user = new BStamp(number, hash, mediantTime);
                Put p = new Put(Bytes.toBytes(user.getNumber()));
                p.add(CF_INFO, qUser, Bytes.toBytes(user.getNumber()));
                p.add(CF_INFO, qEmail, Bytes.toBytes(user.getHash()));
                p.add(CF_INFO, qPassword, Bytes.toBytes(user.getMedianTime()));
                table.put(p);
                return user;

            }
        });
    }

}