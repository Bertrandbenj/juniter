package juniter.service.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@ConditionalOnExpression("${juniter.useHadoop:false}")
@Component
public class UserUtils implements InitializingBean {

    private String tableName = "users";
    private byte[] tableNameAsBytes = Bytes.toBytes("users");

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private UserRepository userRepository;

    private HBaseAdmin admin;

    public void initialize() throws IOException {

        if (admin.tableExists(tableNameAsBytes)) {
            if (!admin.isTableDisabled(tableNameAsBytes)) {
                System.out.printf("Disabling %s\n", tableName);
                admin.disableTable(tableNameAsBytes);
            }
            System.out.printf("Deleting %s\n", tableName);
            admin.deleteTable(tableNameAsBytes);
        }

        HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
        HColumnDescriptor columnDescriptor = new HColumnDescriptor(
                UserRepository.CF_INFO);
        tableDescriptor.addFamily(columnDescriptor);

        admin.createTable(tableDescriptor);

    }

    public void addUsers() {
        for (int i = 0; i < 10; i++) {
            userRepository.save(  i,"user" + i + "@yahoo.com", (long) i);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        admin = new HBaseAdmin(config);
    }

}