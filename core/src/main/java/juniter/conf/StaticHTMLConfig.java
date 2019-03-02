package juniter.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticHTMLConfig implements WebMvcConfigurer {

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        final var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(15000);
        factory.setConnectTimeout(15000);
        // factory.setHttpClient(httpClient);

        return factory;
    }

}
