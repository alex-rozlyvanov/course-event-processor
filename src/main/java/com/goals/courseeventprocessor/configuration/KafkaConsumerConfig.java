package com.goals.courseeventprocessor.configuration;

import com.goals.course.avro.CourseAssignmentEventAvro;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${app.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${app.kafka.schema-registry}")
    private String schemaRegistry;

    @Value(value = "${app.kafka.course-assignment.topic.name}")
    private String courseAssignmentTopic;

    @Bean
    public ConsumerFactory<String, CourseAssignmentEventAvro> consumerFactoryWithJsonSchema() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put("schema.registry.url", schemaRegistry);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        configProps.put("key.subject.name.strategy", io.confluent.kafka.serializers.subject.TopicRecordNameStrategy.class);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourseAssignmentEventAvro> courseAssignmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CourseAssignmentEventAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryWithJsonSchema());
        return factory;
    }

    @Bean
    public ReceiverOptions<String, CourseAssignmentEventAvro> receiverOptions() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put("schema.registry.url", schemaRegistry);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        configProps.put("key.subject.name.strategy", io.confluent.kafka.serializers.subject.TopicRecordNameStrategy.class);

        return ReceiverOptions.create(configProps);
    }

    @Bean
    public KafkaReceiver<String, CourseAssignmentEventAvro> kafkaReceiver() {
        final var options = receiverOptions()
                .subscription(List.of(courseAssignmentTopic));
        return KafkaReceiver.create(options);
    }
}
