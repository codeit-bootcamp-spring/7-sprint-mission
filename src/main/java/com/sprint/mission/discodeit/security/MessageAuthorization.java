package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("messageAuth")
@RequiredArgsConstructor
public class MessageAuthorization {

    private final MessagesRepository messageRepository;

    public boolean isOwner(UUID messageId, Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails)) {
            return false;
        }

        return messageRepository.findById(messageId)
            .map(message -> message.getAuthor().getId().equals(userDetails.getUser().id())
            )
            .orElse(false);
    }
}
