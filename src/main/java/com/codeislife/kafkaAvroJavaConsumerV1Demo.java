package com.codeislife;

import com.example.Customer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

public class kafkaAvroJavaConsumerV1Demo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        // normal consumer
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.put("group.id", "my-avro-consumer");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "earliest");

        // Avro part(serlization)
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", KafkaAvroDeserializer.class.getName());
        properties.setProperty("schema.registry.url", "http://127.0.0.1:8081");
        properties.setProperty("specific.avro.reader", "true");

        KafkaConsumer<String, Customer> consumer = new KafkaConsumer<>(properties);
        String topic = "customer-avro";

        consumer.subscribe(Collections.singleton(topic));

        while (true) {
            ConsumerRecords<String, Customer> records = consumer.poll(500);
            for (ConsumerRecord<String, Customer> record : records) {
                Customer customer = record.value();
                System.out.println(customer);
            }
            consumer.commitAsync();
        }

    }
}
