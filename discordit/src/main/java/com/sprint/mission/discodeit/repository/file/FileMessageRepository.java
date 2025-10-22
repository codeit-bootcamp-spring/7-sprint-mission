package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.DataPath;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.dto.mapper.Mapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
public class FileMessageRepository implements MessageRepository {

    private final List<Message<Receivable>> data = new ArrayList<>();

    @Override
    public void save(Message<Receivable> message) {
        data.add(message);
        write();
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
     * 테스트용 임시 메서드
     */
    private Message<Receivable> findById(UUID uuid) {
        return data.stream()
                .filter(m -> m.getUuid().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id: " + uuid));
    }


    /**
     * 테스트용 임시 메서드
     */
    public <T extends Receivable> void update(Message<T> message) {
        Message<Receivable> existing = findById(message.getUuid());
        int index = data.indexOf(existing);
        if (index != -1) {
            data.set(index, (Message<Receivable>) message);
            write();
        }
    }

    /**
     * 테스트용 임시 메서드: 마지막으로 저장된 메시지를 반환합니다.
     */
    public Message<Receivable> getLast() {
        if (data.isEmpty()) {
            throw new IllegalStateException("저장된 메시지가 없습니다.");
        }
        return data.get(data.size() - 1);
    }

    private void write() {
        DataWriter.writeMessage(data);
    }

}
