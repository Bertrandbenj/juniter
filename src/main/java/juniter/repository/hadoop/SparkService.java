package juniter.repository.hadoop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 *
 * @author BnimajneB
 *
 */
@Configuration
public class SparkService {

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

	//	@Bean
	//	public JavaSparkContext javaSparkContext() {
	//		return new JavaSparkContext(sparkConf());
	//	}
	//
	//	@Bean
	//	public SparkConf sparkConf() {
	//		final SparkConf sparkConf = new SparkConf()
	//				.setAppName(appName)
	//				.setSparkHome(sparkHome)
	//				.setMaster(masterUri);
	//
	//		return sparkConf;
	//	}
	//
	//	@Bean
	//	public SparkSession sparkSession() {
	//		return SparkSession
	//				.builder()
	//				.sparkContext(javaSparkContext().sc())
	//				.appName("Java Spark SQL basic example")
	//				.getOrCreate();
	//	}

}
