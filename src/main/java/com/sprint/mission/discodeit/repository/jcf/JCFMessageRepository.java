package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    //파이널 필드
    private  final List<Message> messages;

    //유일 인스턴스  스태틱파이널로 불변생성
    private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();

    //생성자로 필드 초기화
    private JCFMessageRepository() {
        messages  = new LinkedList<>();
    }
    //그 인스턴스 가지고오는 용도
    public static JCFMessageRepository getInstance() {
        return INSTANCE;
    }


    @Override
    public Message create(User sender, User receiver, String message) {
        Message newMessage = new Message(sender,receiver,message);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public Message read(UUID messageId) {

        Message message = messages.stream()
                .filter(u -> u.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        if (message == null) {
            System.out.println("해당 메시지 없습니다: " + messageId);
        } else {
            System.out.println(message);
        }
        return message;
    }



    @Override
    public List<Message> readAll() {

        System.out.printf("%d개의 메시지@@@\n",messages.size());
        messages
                .forEach(System.out::println);
        return messages;
    }


    @Override
    public Message update(UUID messageId,String content) {
        Message m = messages.stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("찾는 메시지가 없오: " + messageId));

        m.setContent(content);
        m.setUpdatedAt(System.currentTimeMillis());
        return m;

    }



    @Override
    public boolean delete(UUID messageId) {
        return messages.removeIf(u -> u.getId().equals(messageId));

    }

}
