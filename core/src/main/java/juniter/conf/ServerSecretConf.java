package juniter.conf;

import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ServerSecretConf {

    @Value("${juniter.dataPath:${user.home}/.config/juniter/data/}")
    private String dataPath;

    @Bean
    public SecretBox serverSecret() {

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new FileSystemResource(dataPath + "keyring.yml"));
        var properties = yaml.getObject();

        var pub = properties.getProperty("pub", "3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa");
        var sec = properties.getProperty("sec", "5jf8kATgxw8QxFugnh8cEPYYf8BVzPQLWcEq1vHhQHyUTLL5CHd1qbhJmwEsWEeBQSfLYGQU7dhbuyLizyHb28Kx");

        return new SecretBox(pub, Crypto.decodeBase58(sec));
    }
}
