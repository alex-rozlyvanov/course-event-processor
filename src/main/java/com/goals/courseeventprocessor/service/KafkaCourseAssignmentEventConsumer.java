package com.goals.courseeventprocessor.service;

import com.goals.course.avro.CourseAssignmentEventAvro;
import com.goals.courseeventprocessor.mapper.CourseAssignmentEventMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaCourseAssignmentEventConsumer implements Consumer<ConsumerRecord<String, CourseAssignmentEventAvro>> {
    private final CourseAssignmentEventMapper courseAssignmentEventMapper;

    @Override
    @KafkaListener(
            topics = "${app.kafka.course-assignment.topic.name}",
            groupId = "#{T(java.util.UUID).randomUUID().toString()}",
            containerFactory = "courseAssignmentKafkaListenerContainerFactory"
    )
    public void accept(final ConsumerRecord<String, CourseAssignmentEventAvro> message) {
        final var courseAssignmentEvent = courseAssignmentEventMapper.fromCourseAssignmentEventAvro(message.value());
        log.info("Received event key: " + message.key());
        log.info("Received event details: " + courseAssignmentEvent);
    }
}
