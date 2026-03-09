package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.basic.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/sse")
@Slf4j
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    // SSE 연결 생성 요청 (Content Type을 text/event-stream으로 설정)
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestParam UUID userId,
                              @RequestHeader(value = "Last-Event-ID", required = false) UUID lastEventId) {
        log.info("SSE 연결 - 사용자={}, 마지막 수신={}", userId, lastEventId);
        return sseService.connect(userId, lastEventId);
    }

}
