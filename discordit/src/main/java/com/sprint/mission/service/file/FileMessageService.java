package com.sprint.mission.service.file;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.file.FileMessageRepository;
import com.sprint.mission.repository.jcf.JCFMessageRepository;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.JCFMessageService;

import java.util.List;

public class FileMessageService implements MessageService {
    private static final FileMessageService instance = new FileMessageService();
    private static final FileMessageRepository repository = FileMessageRepository.getInstance();

    private FileMessageService() {
    }

    public static FileMessageService getInstance() {
        return instance;
    }

    @Override
    public void sendMessage(User sender, Receivable receiver, String message){
        repository.save(new Message<>(sender, receiver, message));
    }

    @Override
    public List<Message<Receivable>> getBySender(User sender){
        return repository.findBySender(sender);
    }

    @Override
    public <T extends Receivable> List<Message<T>> getByReceiver(T receiver) {
        return repository.findByReceiver(receiver);
    }

    @Override
    public <T extends  Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver) {
        return repository.findBySenderAndReceiver(sender, receiver);
    }

    public void update(Message<Receivable> message) {
        repository.update(message);
    }

    @Override
    public Message<Receivable> getLastMessage() {
        return repository.getLastMessage();
    }
}
