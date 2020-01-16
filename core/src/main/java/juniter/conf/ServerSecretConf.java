package juniter.conf;

import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ServerSecretConf {
    private static final Logger LOG = LogManager.getLogger(ServerSecretConf.class);

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;

    @Bean
    public SecretBox serverSecret() {

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        try {
            yaml.setResources(new FileSystemResource(dataPath + "keyring.yml"));
            var properties = yaml.getObject();
            var pub = properties.getProperty("pub");
            var sec = properties.getProperty("sec");
            return new SecretBox(pub, Crypto.decodeBase58(sec));
        } catch (Exception e) {
            LOG.error("no keyring found for server start " + e.getMessage());
        }
        return new SecretBox("3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa", Crypto.decodeBase58("5jf8kATgxw8QxFugnh8cEPYYf8BVzPQLWcEq1vHhQHyUTLL5CHd1qbhJmwEsWEeBQSfLYGQU7dhbuyLizyHb28Kx"));
    }
}
