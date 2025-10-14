package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    // 데이터 저장 필드
    private final Map<UUID, Message> messages = new LinkedHashMap<>();

    // 싱글톤 패턴
    private static JCFMessageService instance;
    public static JCFMessageService getInstance(){
        if (instance == null){
            instance = new JCFMessageService();
        }
        return instance;
    }

    @Override
    public Message createMessage(UUID senderId, UUID receiverId, String content) {
        Message message = new Message(senderId, receiverId, content);
        messages.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findMessage(UUID id) {
        return messages.get(id);
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(messages.values());
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = messages.get(id);
        if (message != null) {
            message.setContent(content);
        }
        return message;
    }

    @Override
    public void deleteMessage(UUID id) {
        messages.remove(id);
    }
}
