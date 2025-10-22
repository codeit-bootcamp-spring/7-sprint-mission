package com.sprint.mission.discodeit.repository.jcf;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


public class JCFMessageRepository implements MessageRepository {

    private final List<Message> messages = new LinkedList<>();


    private static final JCFMessageRepository INSTANCE = new JCFMessageRepository();
    public static JCFMessageRepository getInstance() { return INSTANCE; }
    private JCFMessageRepository() {}

    @Override
    public Message create(User sender, User receiver, String message) {
        Message newMessage = new Message(sender, receiver, message);
        messages.add(newMessage);
        return newMessage;
    }

    @Override
    public Message read(UUID messageId) {
        Message m = messages.stream()
                .filter(x -> x.getId().equals(messageId))
                .findFirst()
                .orElse(null);

        // 필요하면 로그 유지
        if (m == null) System.out.println("해당 메시지 없습니다: " + messageId);
        else System.out.println(m);
        return m;
    }

    @Override
    public List<Message> readAll() {
        System.out.printf("%d개의 메시지@@@\n", messages.size());
        messages.forEach(System.out::println);
        return new LinkedList<>(messages);
    }

    @Override
    public Message update(UUID messageId, String content) {
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