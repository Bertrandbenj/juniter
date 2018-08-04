package juniter.conf;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
		res.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

		return res;
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
