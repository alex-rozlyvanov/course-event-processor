package com.goals.courseeventprocessor.service;

import com.goals.course.avro.CourseAssignmentEventAvro;
import com.goals.courseeventprocessor.dto.CourseAssignmentEvent;
import com.goals.courseeventprocessor.mapper.CourseAssignmentEventMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@AllArgsConstructor
public class CourseAssignmentEventStreamService {
    private final KafkaReceiver<String, CourseAssignmentEventAvro> kafkaReceiver;
    private final CourseAssignmentEventMapper courseAssignmentEventMapper;

    public Flux<CourseAssignmentEvent> courseAssignmentStream() {
        return kafkaReceiver.receive()
                .map(receivedRecord -> {
                    final var courseAssignmentEventAvro = receivedRecord.value();
                    return courseAssignmentEventMapper.fromCourseAssignmentEventAvro(courseAssignmentEventAvro);
                });
    }
}
