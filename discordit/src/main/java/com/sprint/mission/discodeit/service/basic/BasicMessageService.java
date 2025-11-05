package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.MessageDeleteRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.exceptions.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.MessageNotFoundException;
import com.sprint.mission.discodeit.exceptions.ReceiverNotFoundException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Primary
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponse send(MessageSendRequest dto) {
        User sender = getUserAndHandleException(dto.senderUserId());
        Receivable receiver;
        ReceiverType type = dto.type();

        if (type == ReceiverType.USER) {
            receiver = getUserAndHandleException(dto.receiverId());
        } else if (type == ReceiverType.CHANNEL) {
            UUID channelId = UUID.fromString(dto.receiverId());
            receiver = channelRepository.findById(channelId)
                    .orElseThrow(() -> new ChannelNotFoundException(channelId));
        } else {
            throw new ReceiverNotFoundException(dto.receiverId());
        }

        Message message = new Message(
                sender,
                type,
                receiver,
                dto.message()
        );

        if (dto.fileUrls() != null) {
            List<BinaryContent> contents = dto.fileUrls().stream()
                    .map(BinaryContent::new)
                    .toList();
            binaryContentRepository.saveAll(contents);
            message.addAttachments(contents);
        }

        messageRepository.save(message);
        return MessageResponse.toDto(message);
    }

    private User getUserAndHandleException(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    // 이거 너무 어지러운데 한 메서드에서 너무 많은걸 하고 있는걸까요
    @Override
    public List<MessageResponse> get(MessageGetRequest dto) {
        List<Message> messages;
        if (dto.senderId() != null && dto.receiverId() != null) {
            messages = messageRepository.findBySenderAndReceiver(
                    findUser(dto.senderId()),
                    dto.type() == ReceiverType.USER ?
                            findUser(dto.receiverId()) : findChannel(UUID.fromString(dto.receiverId())));
        } else if (dto.senderId() != null) {
            messages = messageRepository.findBySender(findUser(dto.senderId()));
        } else {
            if (dto.type() == ReceiverType.USER) {
                messages = messageRepository.findByReceiver(findUser(dto.receiverId()));
            } else {
                messages = messageRepository.findByReceiver(findChannel(UUID.fromString(dto.receiverId())));
            }
        }
        return messages.stream()
                .map(MessageResponse::toDto)
                .toList();
    }

    private Channel findChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }

    private User findUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void delete(MessageDeleteRequest dto) {
        Message message = messageRepository.find(dto.id())
                .orElseThrow(() -> new MessageNotFoundException(dto.id()));
        if (!message.getAttachments().isEmpty()) {
            binaryContentRepository.deleteAll(message.getAttachments());
        }
        messageRepository.delete(message);
    }

    /**
     * 테스트용 메서드: 마지막으로 추가된 메시지를 반환합니다.
     *
     * @return 마지막 메시지
     */
    @Override
    public Message getLastMessage() {
        return messageRepository.findLast().orElseThrow(() -> new MessageNotFoundException("메세지 데이터가 존재하지 않습니다."));
    }

    @Override
    public MessageResponse editMessage(MessageEditRequest dto) {
        Message message = messageRepository.find(dto.uuid())
                .orElseThrow(() -> new MessageNotFoundException(dto.uuid()));
        message.setContent(dto.content());
        messageRepository.update(message);
        return MessageResponse.toDto(message);
    }
}
