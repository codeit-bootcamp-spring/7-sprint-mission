package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.MessageService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {

    //파이널 필드
    private  final List<Message> messages;

    //유일 인스턴스  스태틱파이널로 불변생성
    private static final JCFMessageService INSTANCE = new JCFMessageService();

    //생성자로 필드 초기화
    private JCFMessageService() {
         messages  = new LinkedList<>();
    }
   //그 인스턴스 가지고오는 용도
    public static JCFMessageService getInstance() {
        return INSTANCE;
    }


    @Override
    public void create(User sender, User receiver, String message) {
           messages.add(new Message(sender,receiver,message));
    }

    @Override
    public void read(UUID messageId) {
        messages.stream()
                .filter(message -> message.getId().equals(messageId))
                .forEach(message -> {
                    System.out.println("보낸 사람 : " + message.getSender());
                    System.out.println("보낸 사람 : " + message.getSender());
                    System.out.println("받는 사람 : " + message.getReceiver());
                    System.out.println("보낸 시각 : " + message.getTime());
                    System.out.println("메시지 내용 : " + message.getContent());
                });

    }

    @Override
    public void readAll() {
        System.out.printf("%d개의 메시지",messages.size());
        messages.stream()
                        .forEach(message -> {
                            System.out.println("보낸 사람 : " + message.getSender());
                            System.out.println("보낸 사람 : " + message.getSender());
                            System.out.println("받는 사람 : " + message.getReceiver());
                            System.out.println("보낸 시각 : " + message.getTime());
                            System.out.println("메시지 내용 : " + message.getContent());

                        });
    }

    @Override
    public void update(UUID messageId,String content) {
        messages.stream()
                .filter(msg -> msg.getId().equals(messageId))
                .forEach(msg -> msg.setContent(content));

        // 찾아보니 안전하게 수정이 있다 uuid라
        // 하나일것같은데 그냥 if present는 안되더라
             //     .findFirst()
             //   .ifPresent(msg -> msg.setContent(content));
    }

    @Override
    public void delete(UUID messageId) {
        messages.stream() .filter(u -> u.equals(messageId))
                .toList()
                .forEach(m-> messages.remove(m));

    }

}
