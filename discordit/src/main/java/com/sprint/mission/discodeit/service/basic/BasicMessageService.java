package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
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
    public void sendMessage(SendMessageDto dto) {
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

        if (dto.fileUrls() != null) {
            List<BinaryContent> contents = dto.fileUrls().stream()
                    .map(url -> new BinaryContent(sender, url))
                    .toList();
            binaryContentRepository.saveAll(contents);
        }

        messageRepository.save(new Message(
                sender,
                type,
                receiver,
                dto.message(),
                contents
                ));
    }

    @Override
    public List<MessageResponse> getBySender(User sender) {
        return messageRepository.findBySender(sender).stream()
                .map(MessageResponse::toDto)
                .toList();
    }

    @Override
    public List<MessageResponse> getByReceiver(Receivable receiver) {
        return messageRepository.findByReceiver(receiver).stream()
                .map(MessageResponse::toDto)
                .toList();
    }

    @Override
    public void delete(Message message) {
        binaryContentRepository.deleteAll(message.getAttachments());
        messageRepository.delete(message);
    }

    @Override
    public List<MessageResponse> getBySenderAndReceiver(User sender, Receivable receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver).stream()
                .map(MessageResponse::toDto)
                .toList();
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
