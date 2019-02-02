package juniter.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class EndpointsListener implements ApplicationListener {

	private static final Logger LOG = LogManager.getLogger();

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		LOG.info("onApplicationEvent");
		if (event instanceof ContextRefreshedEvent) {
			ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
			applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach((r, h) -> {
				LOG.info(" - " + r + ", " + h);
			});
		}
	}
}