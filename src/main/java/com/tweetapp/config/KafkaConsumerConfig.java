//package com.tweetapp.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import com.tweetapp.model.TweetsDTO;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@EnableKafka
//@Configuration
//public class KafkaConsumerConfig {
//	    
//	    @Bean
//	    public ConsumerFactory<String, TweetsDTO> userConsumerFactory() {
//	        Map<String, Object> config = new HashMap<>();
//
//	        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//	        //  config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
//	        
//	        config.put(ConsumerConfig.GROUP_ID_CONFIG, "tweetapp_id");
//	        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//	        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//	        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
//	                new JsonDeserializer<>(TweetsDTO.class));
//	    }
//
//	    @Bean
//	    public ConcurrentKafkaListenerContainerFactory<String, TweetsDTO> userKafkaListenerFactory() {
//	        ConcurrentKafkaListenerContainerFactory<String, TweetsDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
//	        factory.setConsumerFactory(userConsumerFactory());
//	        return factory;
//	    }
//
//}
