package juniter.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author BnimajneB
 */
@Configuration
public class SparkConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    @Value("${spark.app.name:juniter}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Value("${spark.master:local[*]}")
    private String masterUri;

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkConf sparkConf() {
        return new org.apache.spark.SparkConf()
                .setAppName(appName)
                .setSparkHome(sparkHome)
                .setMaster(masterUri)
                .set("spark.scheduler.mode", "FAIR")
                .set("spark.hadoop.mapred.output.compress", "true")
                .set("spark.hadoop.mapred.output.compression.codec", "org.apache.hadoop.io.compress.GzipCodec")
                .set("spark.hadoop.mapred.output.compression.type", "BLOCK")
                .setJars(new String[] {"/home/ben/ws/juniter/lib/sqlite-jdbc-3.23.1.jar"})
                //.set("spark.eventLog.enabled", "true")
                ;
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession
                .builder()
                .sparkContext(javaSparkContext().sc())
                .appName("Java Spark SQL basic example")
                .getOrCreate();
    }

}
