package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.MessageGetRequest;
import com.sprint.mission.discodeit.dto.message.request.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.exceptions.ReceiverNotFoundException;
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
    public void send(MessageSendRequest dto) {
        User sender = userRepository.findByUserId(dto.senderUserId());

        Receivable receiver;
        ReceiverType type = dto.type();

        if (type == ReceiverType.USER) {
            receiver = userRepository.findByUserId(dto.receiverId());
        } else if (type == ReceiverType.CHANNEL) {
            receiver = channelRepository.findById(UUID.fromString(dto.receiverId()));
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
                    .map(url -> new BinaryContent(sender, url))
                    .toList();
            binaryContentRepository.saveAll(contents);
            message.addAttachments(contents);
        }

        messageRepository.save(message);
    }

    @Override
    public List<MessageResponse> get(MessageGetRequest dto) {
        List<Message> messages;
        if (dto.senderId() != null && dto.receiverId() != null) {
            messages = messageRepository.findBySenderAndReceiver(userRepository.findByUserId(dto.senderId()),
                    dto.type() == ReceiverType.USER?
                            userRepository.findByUserId(dto.receiverId()) :
                            channelRepository.findById(UUID.fromString(dto.receiverId())));
        } else if (dto.senderId() != null) {
            messages = messageRepository.findBySender(userRepository.findByUserId(dto.senderId()));
        } else {
            if (dto.type() == ReceiverType.USER) {
                messages = messageRepository.findByReceiver(userRepository.findByUserId(dto.receiverId()));
            } else {
                messages = messageRepository.findByReceiver(channelRepository.findById(UUID.fromString(dto.receiverId())));
            }
        }
        return messages.stream()
                .map(MessageResponse::toDto)
                .toList();
    }

    @Override
    public void delete(Message message) {
        binaryContentRepository.deleteAll(message.getAttachments());
        messageRepository.delete(message);
    }

    /**
     * 테스트용 메서드: 마지막으로 추가된 메시지를 반환합니다.
     *
     * @return 마지막 메시지
     */
    @Override
    public Message getLastMessage() {
        return messageRepository.findLast();
    }
}
