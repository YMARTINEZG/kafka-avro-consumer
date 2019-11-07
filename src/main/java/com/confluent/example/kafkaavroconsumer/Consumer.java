package com.confluent.example.kafkaavroconsumer;

import com.confluent.example.Customer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private KafkaConsumer<String, Customer> kafkaConsumer;
    private String topicName;

    public Consumer(String topicName, Properties properties) {
        kafkaConsumer = new KafkaConsumer<>(properties);
        this.topicName = topicName;
    }
    public void runTask() {
        try {
            kafkaConsumer.subscribe(Arrays.asList(topicName));
            while (true) {
                ConsumerRecords<String, Customer> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, Customer> record : records) {
                    Customer customer = record.value();
                    logger.info("Received customer: " + customer.toString());
                    //kafkaConsumer.commitSync();
                }
            }
        } catch(WakeupException e){

        } finally {
            kafkaConsumer.close();
        }
    }
}
