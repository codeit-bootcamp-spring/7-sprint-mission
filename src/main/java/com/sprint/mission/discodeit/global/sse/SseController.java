package com.sprint.mission.discodeit.global.sse;

import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            @AuthenticationPrincipal DiscodeitUserDetails userDetails,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
    ) {
        UUID userId = userDetails.getUserResponseDto().id();

        // String을 UUID로 변환 (값이 있을 때만)
        UUID lastId = null;
        if (StringUtils.hasText(lastEventId)) {
            try {
                lastId = UUID.fromString(lastEventId);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid UUID format in Last-Event-ID: {}", lastEventId);
            }
        }

        log.debug("SSE Connecting to user {}", userId);
        return sseService.connect(userId, lastId);
    }
}
