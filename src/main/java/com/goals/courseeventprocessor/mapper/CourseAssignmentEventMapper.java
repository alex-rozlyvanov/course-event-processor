package com.goals.courseeventprocessor.mapper;

import com.goals.course.avro.CourseAssignmentEventAvro;
import com.goals.courseeventprocessor.dto.CourseAssignmentEvent;
import com.goals.courseeventprocessor.enums.Roles;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CourseAssignmentEventMapper {
    public CourseAssignmentEvent fromCourseAssignmentEventAvro(final CourseAssignmentEventAvro courseAssignmentEventAvro) {
        return CourseAssignmentEvent.builder()
                .id(UUID.fromString(courseAssignmentEventAvro.getId()))
                .courseId(UUID.fromString(courseAssignmentEventAvro.getCourseId()))
                .userId(UUID.fromString(courseAssignmentEventAvro.getUserId()))
                .role(Roles.valueOf(courseAssignmentEventAvro.getRole()))
                .build();
    }
}
