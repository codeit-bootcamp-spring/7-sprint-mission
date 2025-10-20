package com.sprint.mission.service.file;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.file.FileMessageRepository;
import com.sprint.mission.service.MessageService;

import java.util.List;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void sendMessage(User sender, Receivable receiver, String message){
        messageRepository.save(new Message<>(sender, receiver, message));
    }

    @Override
    public List<Message<Receivable>> getBySender(User sender){
        return messageRepository.findBySender(sender);
    }

    @Override
    public <T extends Receivable> List<Message<T>> getByReceiver(T receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    @Override
    public <T extends  Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }

    @Override
    public Message<Receivable> getLastMessage() {
        return messageRepository.getLast();
    }
}
