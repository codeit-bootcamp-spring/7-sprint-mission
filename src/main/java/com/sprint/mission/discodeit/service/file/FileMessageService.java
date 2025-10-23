package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;


    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(String content, UUID channelId, UUID userId) {
        Message newMessage = new Message(content, channelId, userId);
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
    public void updateMessage(UUID messageId, String content) {
        Message message = this.getMessage(messageId);
        message.setContent(content);
        message.touch();
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
