package com.sprint.mission.entity.dto.mapper;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.entity.dto.MessageDTO;
import com.sprint.mission.exceptions.UserNotFoundException;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.UserRepository;

import java.util.UUID;

final class MessageMapper {
    private MessageMapper() {}

    public static MessageDTO toDto(Message<? extends Receivable> message) {
        String receiverType;
        UUID receiverUuid;
        if (message.getReceiver() instanceof User u) {
            receiverType = "USER";
            receiverUuid = u.getUuid();
        } else if (message.getReceiver() instanceof Channel c) {
            receiverType = "CHANNEL";
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
            case "CHANNEL" -> receiver = channelRepository.findById(dto.getReceiverUuid());
            case "USER" -> receiver = userRepository.findAll().stream()
                    .filter(u -> u.getUuid().equals(dto.getReceiverUuid()))
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException(dto.getReceiverUuid().toString()));
            default -> throw new IllegalArgumentException("Unknown receiverType: " + dto.getReceiverType());
        }

        return Message.rehydrate(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                sender,
                receiver,
                dto.getMessage()
        );
    }
}

