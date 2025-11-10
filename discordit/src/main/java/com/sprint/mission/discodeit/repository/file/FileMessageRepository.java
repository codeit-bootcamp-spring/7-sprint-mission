package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileMessageRepository implements MessageRepository {

    private final Map<UUID, Message> data = new HashMap<>();
    private final DataWriter dataWriter;

    public FileMessageRepository(DataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    @Override
    public void save(Message message) {
        data.put(message.getUuid(), message);
        write();
    }

    @Override
    public Optional<Message> find(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .toList();
    }

    @Override
    public List<Message> findBySender(User user) {
        return data.values().stream()
                .filter(m -> m.getSender().equals(user))
                .toList();
    }

    @Override
    public List<Message> findByReceiver(Receivable receiver) {
        return data.values().stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .toList();
    }

    @Override
    public List<Message> findBySenderAndReceiver(User user, Receivable receiver) {
        return data.values().stream()
                .filter(m ->
                        m.getSender().equals(user)
                                && m.getReceiver().equals(receiver))
                .toList();
    }

    @Override
    public void deleteAllByReceiver(Receivable receiver) {
        data.entrySet().removeIf(
                e -> e.getValue().getReceiver().equals(receiver));
        write();
    }

    @Override
    public void delete(Message message) {
        data.remove(message.getUuid());
        write();
    }

    @Override
    public Optional<Message> update(Message message) {
        if (!data.containsKey(message.getUuid())) {
            return Optional.empty();
        }
        data.put(message.getUuid(), message);
        write();
        return Optional.of(message);
    }

    @Override
    public Optional<Message> findLast(Receivable receiver) {
        return data.values().stream()
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    /**
     * 테스트용 임시 메서드: 마지막으로 저장된(가장 최근 생성된) 메시지를 반환합니다.
     */
    @Override
    public Optional<Message> findLast() {
        return data.values().stream()
                .max(Comparator.comparing(Message::getCreatedAt));
    }

    private void write() {
        dataWriter.writeMessage(List.copyOf(data.values()));
    }

}
