package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private static final List<ReadStatus> data = new ArrayList<>();

    @Override
    public void save(ReadStatus readStatus) {
        if (data.stream()
                .anyMatch(r ->
                        r.getUser().equals(readStatus.getUser())
                                && r.getChannel().equals(readStatus.getChannel()))) {
            throw new ReadStatusAlreadyExistsException(readStatus);
        }

        data.add(readStatus);
    }

    @Override
    public ReadStatus find(User user, Channel channel) {
        return data.stream()
                .filter(r -> r.getUser().equals(user))
                .filter(r -> r.getChannel().equals(channel))
                .findFirst() // 1개 이하가 보장됨
                .orElseThrow(() -> new ReadStatusNotFoundException(user, channel));
    }

    @Override
    public ReadStatus findById(UUID uuid) {
        return data.stream()
                .filter(r -> r.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new ReadStatusNotFoundException(uuid));
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
    }

    @Override
    public void deleteAllByUser(User user) {
        data.removeIf(d -> d.getUser().equals(user));
    }

    @Override
    public void deleteAllByChannel(Channel channel) {
        data.removeIf(d -> d.getChannel().equals(channel));
    }

    @Override
    public void deleteById(UUID uuid) {
        data.removeIf(d -> d.getId().equals(uuid));
    }

    @Override
    public List<ReadStatus> findAllByUser(User user) {
        return data.stream()
                .filter(d -> d.getUser().equals(user))
                .toList();
    }
}
