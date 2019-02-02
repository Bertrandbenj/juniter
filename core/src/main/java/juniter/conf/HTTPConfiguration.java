package juniter.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class HTTPConfiguration {

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(15000);
		factory.setConnectTimeout(15000);
		// factory.setHttpClient(httpClient);

		return factory;
	}

}