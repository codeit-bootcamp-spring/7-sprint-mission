package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("messageSecurity")
@RequiredArgsConstructor
public class MessageSecurity {

    private final MessageRepository messageRepository;

    public boolean isOwner(UUID messageId, Authentication authentication) {
        DiscodeitUserDetails details = (DiscodeitUserDetails) authentication.getPrincipal();
        UUID currentUserId = details.getUserDto().id();

        return messageRepository.findById(messageId)
                        .map(message -> currentUserId.equals(message.getAuthor().getId()))
                        .orElse(false);
    }
}
