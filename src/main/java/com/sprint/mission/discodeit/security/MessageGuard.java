package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("messageGuard") // 이 이름이 SpEL에서 사용
@RequiredArgsConstructor
public class MessageGuard {

    private final MessageRepository messageRepository;

    public boolean isAuthor(UUID messageId, UUID authenticationId) {
        return messageRepository.findByIdWithUser(messageId)
                .map(message -> message.getUser().getId().equals(authenticationId))
                .orElse(false);
    }
}
