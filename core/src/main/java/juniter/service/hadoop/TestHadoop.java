package juniter.service.hadoop;

import juniter.service.jpa.ForkHead;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@ConditionalOnExpression("${juniter.useHadoop:false}")
@Service
public class TestHadoop {
    private static final Logger LOG = LogManager.getLogger(ForkHead.class);

    // https://blogs.sap.com/2019/09/09/spring-boot-app-to-covert-json-to-parquet-orc-csv-and-text-file-format-using-apache-spark-library/
    // https://github.com/spring-projects/spring-hadoop-samples/blob/master/hbase/src/main/resources/META-INF/spring/application-context.xml
    @Autowired
    private UserRepository userRepo;


    @PostConstruct
    public void test(){
        LOG.info("test");

        userRepo.save(12345,"0000017DBC637722FAEE89AA519566203CF4DE10351722A178B925B3D389477F",123456789L);
    }

}
