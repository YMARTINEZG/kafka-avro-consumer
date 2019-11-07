package com.confluent.example.kafkaavroconsumer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class KafkaAvroConsumerApplication implements CommandLineRunner {

	@Value("${topic.name}")
	private String topicName;
	private static final Logger logger = LoggerFactory.getLogger(KafkaAvroConsumerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkaAvroConsumerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		props.put("group.id", "group_id");
		props.put("auto.commit.enable", "false");
		props.put("auto.offset.reset", "earliest");
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", KafkaAvroDeserializer.class.getName());
		props.put("schema.registry.url", "http://127.0.0.1:8081");
		props.put("specific.avro.reader", "true");

		Thread kafkaConsumerThread = new Thread(() -> {
			logger.info("Starting Kafka consumer thread.");

			Consumer avroKafkaConsumer = new Consumer(
					topicName,
					props
			);

			avroKafkaConsumer.runTask();
		});
		kafkaConsumerThread.start();
	}
}
