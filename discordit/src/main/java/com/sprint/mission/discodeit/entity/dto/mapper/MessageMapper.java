package com.sprint.mission.discodeit.entity.dto.mapper;

import com.sprint.mission.discodeit.entity.base.*;
import com.sprint.mission.discodeit.entity.dto.MessageDTO;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.UUID;

final class MessageMapper {
    private MessageMapper() {}

    public static MessageDTO toDto(Message<? extends Receivable> message) {
        ReceiverType receiverType;
        UUID receiverUuid;
        if (message.getReceiver() instanceof User u) {
            receiverType = ReceiverType.USER;
            receiverUuid = u.getUuid();
        } else if (message.getReceiver() instanceof Channel c) {
            receiverType = ReceiverType.CHANNEL;
            receiverUuid = c.getUuid();
        } else {
            throw new IllegalArgumentException("Unsupported receiver type: " + message.getReceiver().getClass());
        }

        return new MessageDTO(
                message.getUuid(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getSender().getUuid(),
                message.getSender().getUserId(),
                receiverUuid,
                receiverType,
                message.getMessage()
        );
    }

    public static Message<Receivable> toMessage(MessageDTO dto,
                                                UserRepository userRepository,
                                                ChannelRepository channelRepository) {
        User sender = userRepository.findById(dto.getSenderUserId());

        Receivable receiver;
        switch (dto.getReceiverType()) {
            case CHANNEL-> receiver = channelRepository.findById(dto.getReceiverUuid());
            case USER -> receiver = userRepository.findAll().stream()
                    .filter(u -> u.getUuid().equals(dto.getReceiverUuid()))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException(dto.getReceiverUuid().toString()));
            default -> throw new IllegalArgumentException("Unknown receiverType: " + dto.getReceiverType());
        }

        return Message.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                sender,
                receiver,
                dto.getMessage()
        );
    }
}

