package service.jcf;

import entity.Message;
import service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static JCFMessageService instance;
    private final Map<UUID, Message> data;

    private JCFMessageService() {
        this.data = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }
        return instance;
    }

    @Override
    public void create(Message message) {
        data.put(message.getId(), message);
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    // 메시지는 수정/삭제 없음
}
