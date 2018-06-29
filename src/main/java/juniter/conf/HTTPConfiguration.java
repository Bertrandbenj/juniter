package juniter.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HTTPConfiguration {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(15000);
		factory.setConnectTimeout(15000);
		//factory.setHttpClient(httpClient);

		
		return factory;
	}

}