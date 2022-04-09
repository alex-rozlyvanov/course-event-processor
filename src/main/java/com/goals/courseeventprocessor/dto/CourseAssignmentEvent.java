package com.goals.courseeventprocessor.dto;

import com.goals.courseeventprocessor.enums.Roles;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseAssignmentEvent {
    private UUID id;
    private UUID userId;
    private Roles role;
    private UUID courseId;
}
