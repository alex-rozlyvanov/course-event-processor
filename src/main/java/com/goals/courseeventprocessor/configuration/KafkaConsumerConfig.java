package com.goals.courseeventprocessor.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.goals.course.avro.CourseAssignmentEventAvro;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
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

    @Bean
    public ConsumerFactory<String, CourseAssignmentEventAvro> courseAssignmentConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);


        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, JsonNode> consumerFactoryWithJsonSchema() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put("schema.registry.url", schemaRegistry);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configProps.put("key.subject.name.strategy", io.confluent.kafka.serializers.subject.TopicRecordNameStrategy.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CourseAssignmentEventAvro> courseAssignmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CourseAssignmentEventAvro> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(courseAssignmentConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JsonNode> courseAssignmentKafkaListenerContainerFactoryWithJsonSchema() {
        ConcurrentKafkaListenerContainerFactory<String, JsonNode> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryWithJsonSchema());
        return factory;
    }


}
