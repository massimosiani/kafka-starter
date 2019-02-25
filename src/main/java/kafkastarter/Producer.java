package kafkastarter;

import kafkastarter.avro.model.KeyValue;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {

    private static Logger log = LoggerFactory.getLogger(Producer.class);

    private Producer() {}

    public static <V> KafkaProducer<String, V> producer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-starter:9092");
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        properties.put("schema.registry.url", "http://localhost:8081");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        return new KafkaProducer<>(properties);
    }

    public static void produce(org.apache.kafka.clients.producer.Producer<String, KeyValue> producer) throws Exception {
        Callback callback = (RecordMetadata rm, Exception e) -> {
            if (e != null) {
                log.error("Error: {}", e);
            }
        };

        ProducerRecord<String, KeyValue> record1 = new ProducerRecord<>(
                "test",
                "Key F&F",
                KeyValue.newBuilder().setId(1).setKey("Key F&F AAA").setValue("Value F&F!").build());
        producer.send(record1).get();

        ProducerRecord<String, KeyValue> record2 = new ProducerRecord<>(
                "test",
                "Key Sync",
                KeyValue.newBuilder().setId(1).setKey("Key Sync AAA").setValue("Value Sync!").build());
        producer.send(record2);

        ProducerRecord<String, KeyValue> record3 = new ProducerRecord<>(
                "test",
                "Key Async",
                KeyValue.newBuilder().setId(1).setKey("Key Async AAA").setValue("Value Async!").build());
        producer.send(record3, callback);
    }
}
