package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageSecurityService {

    private final MessageRepository messageRepository;

    public boolean isOwner(UUID messageId, UUID userId) {
        return messageRepository.findById(messageId)
                .map(message -> message.getAuthor().getId().equals(userId))
                .orElse(false);
    }
}
