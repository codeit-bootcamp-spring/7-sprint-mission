package com.sprint.mission.discodeit.global.config.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("messageSecurity")
@RequiredArgsConstructor
@Slf4j
public class MessageSecurity {

    private final MessageRepository messageRepository;

    public boolean isOwner(UUID messageId, Authentication authentication) {
        DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();
        UUID userId = principal.getUserResponseDto().id();
        log.info("isOwner: userId = {}", userId);

        return messageRepository.existsByIdAndAuthorId(messageId, userId);
    }
}
