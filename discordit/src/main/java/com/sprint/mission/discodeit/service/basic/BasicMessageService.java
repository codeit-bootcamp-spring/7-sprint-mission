package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.MessageGetByChannelIdRequest;
import com.sprint.mission.discodeit.dto.message.request.*;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseV2;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.message.MessageNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.ReceiverNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public List<MessageResponseV2> get(MessageGetRequest dto) {
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
                .map(MessageResponseV2::toDto)
                .toList();
    }

    private Channel findChannel(UUID channelId) {
        return channelRepository.find(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));
    }

    private User findUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void remove(UUID id) {
        Message message = messageRepository.find(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
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
    public MessageResponseV2 editMessage(UUID id, MessageEditRequest dto) {
        Message message = messageRepository.find(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        message.setContent(dto.newContent());
        messageRepository.update(message);
        return MessageResponseV2.toDto(message);
    }

    @Override
    public List<MessageResponseV2> getAll() {
        return messageRepository.findAll().stream()
                .map(MessageResponseV2::toDto)
                .toList();
    }

    @Override
    public List<MessageResponseV2> getAllByChannelId(MessageGetByChannelIdRequest request) {
        return messageRepository.findByReceiver(channelRepository.find(request.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(request.channelId())))
                .stream()
                .map(MessageResponseV2::toDto)
                .toList();
    }

    @Override
    public MessageResponseV2 send(MessageCreateRequestV2 messageCreateRequest, List<MultipartFile> attachments) {
        Message message = new Message(
                userRepository.find(messageCreateRequest.authorId())
                        .orElseThrow(() -> new UserNotFoundException(messageCreateRequest.authorId())),
                ReceiverType.CHANNEL,
                channelRepository.find(messageCreateRequest.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(messageCreateRequest.channelId())),
                messageCreateRequest.content()

        );
        messageRepository.save(message);
        // 파일 저장은 다음 미션때 구현...
        return MessageResponseV2.toDto(message);
    }
}
