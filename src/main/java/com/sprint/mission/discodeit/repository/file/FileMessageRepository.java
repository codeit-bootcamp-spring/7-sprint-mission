package com.sprint.mission.discodeit.repository.file;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import com.sprint.mission.discodeit.service.file.ReadService;
import com.sprint.mission.discodeit.service.file.LoadService;


public class FileMessageRepository implements MessageRepository {

    private static final String filename = "messages";


    private List<Message> loadAll() {
        List<Message> list = ReadService.read(filename, Message.class);
        return (list != null) ? list : new LinkedList<>();
    }
    private void saveAll(List<Message> list) {
        LoadService.load(filename, list);
    }

    @Override
    public Message create(User sender, User receiver, String message) {
        Message newMessage = new Message(sender, receiver, message);

        List<Message> messages = loadAll();
        messages.add(newMessage);
        saveAll(messages);
        return newMessage;
    }

    @Override
    public Message read(UUID messageId) {
        List<Message> messages = loadAll();
        Message m = messages.stream()
                .filter(x -> x.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        if (m == null) System.out.println("해당 메시지 없습니다: " + messageId);
        else System.out.println(m);
        return m;
    }

    @Override
    public List<Message> readAll() {
        List<Message> messages = loadAll();
        System.out.printf("%d개의 메시지@@@\n", messages.size());
        messages.forEach(System.out::println);
        return messages;
    }

    @Override
    public Message update(UUID messageId, String content) {
        List<Message> messages = loadAll();
        Message m = messages.stream()
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("찾는 메시지가 없오: " + messageId));

        m.setContent(content);
        m.setUpdatedAt(System.currentTimeMillis());
        saveAll(messages);
        return m;
    }

    @Override
    public boolean delete(UUID messageId) {
        List<Message> messages = loadAll();
        boolean removed = messages.removeIf(u -> u.getId().equals(messageId));
        if (removed) {
            saveAll(messages);
        }
        return removed;
    }
}