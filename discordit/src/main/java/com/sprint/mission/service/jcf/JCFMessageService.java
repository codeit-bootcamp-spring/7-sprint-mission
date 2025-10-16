package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.jcf.JCFMessageRepository;
import com.sprint.mission.service.MessageService;

import java.util.List;

public class JCFMessageService implements MessageService {
    private final MessageRepository repository;

    public JCFMessageService(MessageRepository repository) {
        this.repository = repository;
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

    /**
     * 테스트용 메서드: 마지막으로 추가된 메시지를 반환합니다.
     * @return 마지막 메시지
     */
    @Override
    public Message<Receivable> getLastMessage() {
        return repository.getLast();
    }
}
