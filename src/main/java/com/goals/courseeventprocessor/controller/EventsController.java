package com.goals.courseeventprocessor.controller;

import com.goals.courseeventprocessor.dto.CourseAssignmentEvent;
import com.goals.courseeventprocessor.service.CourseAssignmentEventStreamService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/events/stream")
@AllArgsConstructor
public class EventsController {
    private final CourseAssignmentEventStreamService courseAssignmentEventStreamService;

    @GetMapping(value = "/course-assignment", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<CourseAssignmentEvent> courseAssignmentStream() {
        return courseAssignmentEventStreamService.courseAssignmentStream();
    }

}
