package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.common.exceptions.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.common.exceptions.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final List<ReadStatus> data = new ArrayList<>();
    private final DataWriter dataWriter;

    public FileReadStatusRepository(DataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    @Override
    public void save(ReadStatus readStatus) {
        if (data.stream()
                .anyMatch(r ->
                        r.getUser().equals(readStatus.getUser())
                                && r.getChannel().equals(readStatus.getChannel()))) {
            throw new ReadStatusAlreadyExistsException(readStatus);
        }

        data.add(readStatus);
        write();
    }

    @Override
    public Optional<ReadStatus> find(User user, Channel channel) {
        return data.stream()
                .filter(r -> r.getUser().equals(user))
                .filter(r -> r.getChannel().equals(channel))
                .findFirst(); // 1개 이하가 보장됨
    }

    @Override
    public Optional<ReadStatus> findById(UUID uuid) {
        return data.stream()
                .filter(r -> r.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void delete(ReadStatus readStatus) {
        if (!data.remove(readStatus)) {
            throw new ReadStatusNotFoundException(readStatus);
        }
        write();
    }

    @Override
    public void deleteAllByUser(User user) {
        data.removeIf(d -> d.getUser().equals(user));
        write();
    }

    @Override
    public void deleteAllByChannel(Channel channel) {
        data.removeIf(d -> d.getChannel().equals(channel));
        write();
    }

    @Override
    public void delete(UUID uuid) {
        data.removeIf(d -> d.getId().equals(uuid));
        write();
    }

    @Override
    public List<ReadStatus> findAllByUser(User user) {
        return data.stream()
                .filter(d -> d.getUser().equals(user))
                .toList();
    }

    @Override
    public void update(ReadStatus readStatus) {
        data.removeIf(r -> r.equals(readStatus));
        data.add(readStatus);
        write();
    }

    private void write() {
        dataWriter.writeReadStatus(data);
    }
}
