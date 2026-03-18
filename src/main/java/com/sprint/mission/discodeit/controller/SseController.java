package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService  sseService;
    @GetMapping
    public ResponseEntity<SseEmitter> connect(
            @RequestParam UUID userId,
            @RequestParam (required = false) UUID lastEventId
    ){
        SseEmitter emitter = sseService.connect(userId, lastEventId);
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }
}
