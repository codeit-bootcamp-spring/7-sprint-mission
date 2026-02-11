package com.sprint.mission.discodeit.common.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authz")
@RequiredArgsConstructor
public class AuthorizationService {
    private final MessageRepository messageRepository;

    public boolean isSelf(Authentication authentication, UUID userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof DiscodeitUserDetails dud)) {
            return false;
        }

        if (dud.getUserDto() == null || dud.getUserDto().id() == null) {
            return false;
        }

        return dud.getUserDto().id().equals(userId);
    }

    public boolean isMessageAuthor(Authentication authentication, UUID messageId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof DiscodeitUserDetails dud)) {
            return false;
        }
        UUID loginUserId = dud.getUserDto() == null ? null : dud.getUserDto().id();
        if (loginUserId == null || messageId == null) {
            return false;
        }

        return messageRepository.findById(messageId)
                .map(m -> m.getAuthor() != null && loginUserId.equals(m.getAuthor().getId()))
                .orElse(false);
    }
}
