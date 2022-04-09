package com.goals.courseeventprocessor.service;

import com.goals.course.avro.CourseAssignmentEventAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaCourseAssignmentEventConsumerTest {

    @InjectMocks
    private KafkaCourseAssignmentEventConsumer service;

    @SuppressWarnings("unchecked")
    @Test
    void accept_success() {
        // GIVEN
        final var mockConsumerRecord = (ConsumerRecord<String, CourseAssignmentEventAvro>) mock(ConsumerRecord.class);
        when(mockConsumerRecord.key()).thenReturn("test_key");
        final var courseAssignmentEventAvro = CourseAssignmentEventAvro.newBuilder()
                .setId("test_id")
                .setUserId("test_id")
                .setRole("test")
                .setCourseId("test_id")
                .build();
        when(mockConsumerRecord.value()).thenReturn(courseAssignmentEventAvro);

        // WHEN
        service.accept(mockConsumerRecord);

        // THEN
        // stub
        verify(mockConsumerRecord).key();
        verify(mockConsumerRecord).value();
    }
}
