package kafkastarter;

import kafkastarter.avro.model.KeyValue;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    private static Logger log = LoggerFactory.getLogger(Consumer.class);

    private Consumer() {}

    public static <V> KafkaConsumer<String, V> consumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-starter:9092");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "Kafka Consumer 1");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-starter");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", "http://localhost:8081");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroDeserializer.class);
        return new KafkaConsumer<>(properties);
    }

    public static void consume(org.apache.kafka.clients.consumer.Consumer<String, KeyValue> consumer) {
        consumer.subscribe(Collections.singletonList("test"));

        int count = 0;
        try (org.apache.kafka.clients.consumer.Consumer<String, KeyValue> consumerTry = consumer) {
            while (true) {
                consumerTry
                        .poll(Duration.ofMillis(100))
                        .forEach(record -> log.info("Record consumed by {}: {}", consumer, record));
                count++;
                if (count > 50) {
                    consumerTry.wakeup();
                }
            }
        } catch (WakeupException wuex) {
            // nothing
        }
    }
}
