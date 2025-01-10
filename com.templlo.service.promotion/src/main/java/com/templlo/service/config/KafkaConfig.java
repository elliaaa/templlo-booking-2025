package com.templlo.service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.templlo.service.coupon.dto.CouponStatusEvent;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CouponStatusEvent> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, CouponStatusEvent> factory =
			new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties()
			.setAckMode(org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL); // 수동 ACK 설정
		return factory;
	}

	@Bean
	public DefaultKafkaConsumerFactory<String, CouponStatusEvent> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
			new StringDeserializer(),
			new JsonDeserializer<>(CouponStatusEvent.class));
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "coupon-consumer-group");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 수동 커밋을 위해 설정
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // 모든 패키지 신뢰
		return props;
	}
}
