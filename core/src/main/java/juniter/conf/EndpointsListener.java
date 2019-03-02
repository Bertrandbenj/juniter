package juniter.conf;

//public class EndpointsListener implements ApplicationListener {
//
//	private static final Logger LOG = LogManager.getLogger();
//
//	@Override
//	public void onApplicationEvent(ApplicationEvent event) {
//
//		if (event instanceof ContextRefreshedEvent) {
//			ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
//			applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().forEach((r, h) -> {
//				LOG.info("onApplicationEvent - " + r + ", " + h);
//			});
//		}
//	}
//}