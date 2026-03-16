package com.sprint.mission.discodeit.sse;


import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterRepository repository;

    @GetMapping("/api/sse")
    public SseEmitter connect(Authentication authentication) {

        UUID userId = ((DiscodeitUserDetails) authentication.getPrincipal()).getUserDto().getId();

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        repository.save(userId, emitter);

        emitter.onCompletion(() -> repository.delete(userId, emitter));
        emitter.onTimeout(() -> repository.delete(userId, emitter));

        return emitter;
    }
}