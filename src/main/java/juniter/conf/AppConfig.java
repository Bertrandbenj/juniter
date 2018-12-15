package juniter.conf;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("juniter")
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		final var res = new ModelMapper();
		res.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

		return res;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
	{
		return restTemplateBuilder
				.setConnectTimeout(15*1000)
           		.setReadTimeout(5*1000)
				.build();
	}
}
