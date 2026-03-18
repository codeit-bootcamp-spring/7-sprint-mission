package com.sprint.mission.discodeit.sse.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

    private final SseService sseService;

    @GetMapping
    public SseEmitter connect(
            @AuthenticationPrincipal DiscodeitUserDetails user,
            @RequestParam(required = false) String lastEventId
            ) {

        UUID receiverId = user.getUserDto().id();
        UUID lastEvent = lastEventId == null ? null : UUID.fromString(lastEventId);

        return sseService.connect(receiverId, lastEvent);
    }
}
