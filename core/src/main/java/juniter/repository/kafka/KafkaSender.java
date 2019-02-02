package juniter.repository.kafka;

import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

	//	@Autowired
	//	private KafkaTemplate<String, String> kafkaTemplate;

	public void send(String topic, String payload) {
		//		kafkaTemplate.send(topic, payload);
		System.out.println("Message: " + payload + " sent to topic: " + topic);
	}


}
