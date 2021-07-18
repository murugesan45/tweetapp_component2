package com.tweetapp.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.tweetapp.model.TweetsDTO;

/*
 * Contains kafka producer configuration replace ip with the name kafka if
 * running with docker
 * @author Murugesan D
 */
@Configuration
public class KafkaProducerConfig {

	KafkaProducerConfig() {
		// Default constructor
	}

	@Bean
	public ProducerFactory<String, TweetsDTO> producerFactory() {

		final Map<String, Object> config = new ConcurrentHashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);

	}

	@Bean
	public KafkaTemplate<String, TweetsDTO> kafkaTemplate() {

		return new KafkaTemplate<String, TweetsDTO>(producerFactory());

	}

}
