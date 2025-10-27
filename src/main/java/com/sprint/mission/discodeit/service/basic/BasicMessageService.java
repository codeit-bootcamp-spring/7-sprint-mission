package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message createMessage(CreateMessageDto createMessageDto) {
        Message newMessage = new Message(createMessageDto.getContent(), createMessageDto.getChannelID(), createMessageDto.getUserId());
        messageRepository.save(newMessage);
        return newMessage;
    }

    @Override
    public Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 메시지: " + messageId));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(updateMessageDto.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("삭제할 메시지를 찾을 수 없습니다: " + updateMessageDto.getMessageId()));
        message.messageUpdate(updateMessageDto.getContent());
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 메시지를 찾을 수 없습니다: " + messageId));

        messageRepository.deleteById(messageId);
    }

    @Override
    public void deleteMessagesByChannel(UUID channelId) {
        for (Message message : getAllMessages()) {
            if (message.getChannelId().equals(channelId)) {
                this.deleteMessage(message.getId());
            }
        }
    }

    @Override
    public void deleteMessagesByUser(UUID userId) {
        for (Message message : getAllMessages()) {
            if (message.getUserId().equals(userId)) {
                this.deleteMessage(message.getId());
            }
        }
    }
}
