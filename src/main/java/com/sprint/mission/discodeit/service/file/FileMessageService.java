package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageService implements MessageRepository {

    private static final String filename = "messages";

    private   List<Message> messages;

    //유일 인스턴스  스태틱파이널로 불변생성
    private static final FileMessageService INSTANCE = new FileMessageService();

    //생성자로 필드 초기화
    private FileMessageService() {
        messages  = new LinkedList<>();
    }
    //그 인스턴스 가지고오는 용도
    public static FileMessageService getInstance() {
        return INSTANCE;
    }


    @Override
    public Message create(User sender, User receiver, String message) {
        Message newMessage = new Message(sender,receiver,message);
        messages = ReadService.read(filename,Message.class);

        messages.add(newMessage);

        LoadService.load(filename,messages);
        return newMessage;
    }

    @Override
    public Message read(UUID messageId) {
        messages = ReadService.read(filename,Message.class);
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
        messages = ReadService.read(filename,Message.class);
        System.out.printf("%d개의 메시지@@@\n",messages.size());
        messages
                .forEach(System.out::println);
        return messages;
    }


    @Override
    public Message update(UUID messageId,String content) {
        messages = ReadService.read(filename,Message.class);
        Message m = messages.stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("찾는 메시지가 없오: " + messageId));

        m.setContent(content);
        m.setUpdatedAt(System.currentTimeMillis());
        LoadService.load(filename,messages);
        return m;

    }



    @Override
    public boolean delete(UUID messageId) {
        messages = ReadService.read(filename,Message.class);
        return messages.removeIf(u -> u.getId().equals(messageId));

    }

}
