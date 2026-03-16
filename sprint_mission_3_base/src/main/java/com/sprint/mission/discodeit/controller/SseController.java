package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.sse.SseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(path = "/api/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(
            Authentication authentication,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
    ) {
        if (authentication == null || !(authentication.getPrincipal() instanceof DiscodeitUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        UUID parsedLastEventId = null;
        if (lastEventId != null && !lastEventId.isBlank()) {
            parsedLastEventId = UUID.fromString(lastEventId);
        }

        return sseService.connect(details.getId(), parsedLastEventId);
    }
}