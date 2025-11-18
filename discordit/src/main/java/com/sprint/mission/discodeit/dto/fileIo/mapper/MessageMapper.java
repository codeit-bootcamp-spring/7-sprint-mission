package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.MessageIoDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.UUID;

final class MessageMapper {
    private MessageMapper() {
    }

    public static MessageIoDTO toDto(Message message) {
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

        List<UUID> attachmentIds = message.getAttachments().stream()
                .map(BinaryContent::getUuid)
                .toList();

        return new MessageIoDTO(
                message.getUuid(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getSender().getUuid(),
                receiverUuid,
                receiverType,
                message.getContent(),
                attachmentIds
        );
    }

    public static Message toMessage(MessageIoDTO dto,
                                    UserRepository userRepository,
                                    ChannelRepository channelRepository,
                                    BinaryContentRepository contentRepository) {
        User sender = userRepository.find(dto.getSenderUuid())
                .orElseThrow(() -> new UserNotFoundException(dto.getSenderUuid()));

        Receivable receiver;
        switch (dto.getReceiverType()) {
            case CHANNEL -> receiver = channelRepository.find(dto.getReceiverUuid())
                    .orElseThrow(() -> new ChannelNotFoundException(dto.getReceiverUuid()));
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
                dto.getReceiverType(),
                receiver,
                dto.getMessage(),
                dto.getAttachmentIds().stream()
                        .map(id -> contentRepository.findById(id)
                                .orElseThrow(() -> new BinaryContentNotFoundException(id)))
                        .toList()
        );
    }
}

