package io.fission.kafka;

import io.fission.Context;
import io.fission.Function;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Properties;
import java.util.logging.Logger;

public class VesselIoTProducer implements Function {
    private static Logger logger = Logger.getGlobal();

    @Override
    public ResponseEntity call(RequestEntity requestEntity, Context context) {
        String brokerList = System.getenv("KAFKA_ADDR");
        String topic = System.getenv("TOPIC_NAME");
        if (brokerList == null || topic == null) {
            return ResponseEntity.badRequest().build();
        }

        // Related issue: https://stackoverflow.com/questions/37363119/kafka-producer-org-apache-kafka-common-serialization-stringserializer-could-no
        Thread.currentThread().setContextClassLoader(null);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
        properties.put("acks", "all");
        properties.put("value.serializer", "io.fission.kafka.IoTDataEncoder");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, IoTData> producer = new KafkaProducer<String, IoTData>(properties);
        IotProducer iotProducer = new IotProducer();
        try {
            iotProducer.generateIoTEvent(producer,topic);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.severe("Failed to send events to Kafka"+ e);
        }
        producer.close();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Methods", "*");
        headers.add("Access-Control-Expose-Headers", "*");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();

        return null;
    }
}
