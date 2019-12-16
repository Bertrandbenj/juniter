package juniter.service;

// https://docs.spring.io/spring-kafka/reference/htmlsingle/#_even_quicker_with_spring_boot

public class KafkaTest {

	//	private static final Logger LOG = LogManager.getLogger();
	//	private String group = "juniter";
	//	private String topicPool = "pool";
	//	private String topicBlockchain = "blockchain";
	//
	//	@Autowired
	//	private KafkaTemplate<String, String> kafkaTemplate;
	//
	//	private Map<String, Object> consumerProps() {
	//		final Map<String, Object> props = new HashMap<>();
	//		props.queue(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	//		props.queue(ConsumerConfig.GROUP_ID_CONFIG, group);
	//		props.queue(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
	//		props.queue(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
	//		props.queue(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
	//		props.queue(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
	//		props.queue(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	//		return props;
	//	}
	//
	//	private KafkaMessageListenerContainer<Integer, String> createContainer(
	//			ContainerProperties containerProps) {
	//		final Map<String, Object> props = consumerProps();
	//		final DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<Integer, String>(props);
	//		final KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf,
	//				containerProps);
	//		return container;
	//	}
	//
	//	private KafkaTemplate<Integer, String> createTemplate() {
	//		final Map<String, Object> senderProps = senderProps();
	//		final ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<Integer, String>(senderProps);
	//		final KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
	//		return template;
	//	}
	//
	//	private Map<String, Object> senderProps() {
	//		final Map<String, Object> props = new HashMap<>();
	//		props.queue(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	//		props.queue(ProducerConfig.RETRIES_CONFIG, 0);
	//		props.queue(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
	//		props.queue(ProducerConfig.LINGER_MS_CONFIG, 1);
	//		props.queue(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
	//		props.queue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
	//		props.queue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	//		return props;
	//	}
	//
	//	@Test
	//	public void testAutoCommit() throws Exception {
	//		LOG.info("Start auto");
	//		final ContainerProperties containerProps = new ContainerProperties("topic1", "topic2");
	//		final CountDownLatch latch = new CountDownLatch(4);
	//		containerProps.setMessageListener((MessageListener<Integer, String>) message -> {
	//			LOG.info("received: " + message);
	//			latch.countDown();
	//		});
	//		final KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
	//		container.setBeanName("testAuto");
	//		container.start();
	//		Thread.sleep(1000); // wait a bit for the container to start
	//		final KafkaTemplate<Integer, String> template = createTemplate();
	//		template.setDefaultTopic(topicPool);
	//		template.sendDefault(0, "foo");
	//		template.sendDefault(2, "bar");
	//		template.sendDefault(0, "baz");
	//		template.sendDefault(2, "qux");
	//		template.flush();
	//		assertTrue(latch.await(60, TimeUnit.SECONDS));
	//		container.stop();
	//		LOG.info("Stop auto");
	//
	//	}

}
