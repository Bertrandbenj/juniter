package conf;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
class PersistanceConfig {


    private static final String DIALECT = "hibernate.dialect";

	private static final String SHOW_SQL = "hibernate.show_sql";

	private static final String FORMAT_SQL = "hibernate.format_sql";

	private static final String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

	@Autowired
    private Environment environment;

//    @Autowired
//    private DataSource dataSource;

    @Autowired
//    private MultiTenantConnectionProvider multiTenantConnectionProvider;
//
//    @Autowired
//    private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

    public PersistanceConfig() {
		// TODO Auto-generated constructor stub
	}



    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(DIALECT, environment.getRequiredProperty(DIALECT));
        properties.put(SHOW_SQL, environment.getRequiredProperty(SHOW_SQL));
        properties.put(FORMAT_SQL, environment.getRequiredProperty(FORMAT_SQL));
        properties.put(HBM2DDL_AUTO, environment.getRequiredProperty(HBM2DDL_AUTO));

        return properties;
    }



}