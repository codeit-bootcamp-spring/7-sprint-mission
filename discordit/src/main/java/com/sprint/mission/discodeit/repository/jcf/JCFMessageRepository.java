package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageRepository implements MessageRepository {
    private static final List<Message<Receivable>> data = new ArrayList<>();
    private static final JCFMessageRepository instance = new JCFMessageRepository();

    private JCFMessageRepository() {
    }

    public static JCFMessageRepository getInstance() {
        return instance;
    }

    @Override
    public void save(Message<Receivable> message) {
        data.add(message);
    }

    @Override
    public List<Message<Receivable>> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<Message<Receivable>> findBySender(User user) {
        return data.stream()
                .filter(m -> m.getSender().equals(user))
                .toList();
    }

    @Override
    public <T extends Receivable> List<Message<T>> findByReceiver(T receiver) {
        return data.stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .map(m -> (Message<T>) m)
                .toList();
    }

    @Override
    public <T extends Receivable> List<Message<T>> findBySenderAndReceiver(User user, T receiver) {
        return data.stream()
                .filter(m ->
                        m.getSender().equals(user)
                                && m.getReceiver().equals(receiver))
                .map(m -> (Message<T>) m)
                .toList();
    }

    /**
     * 테스트 데이터 세팅용 메서드
     * @return 마지막에 저장된 메세지
     */
    public Message<Receivable> getLast(){
        if (data.isEmpty()) {
            throw new IllegalStateException("저장된 메시지가 없습니다.");
        }
        return data.get(data.size() - 1);
    }
}
