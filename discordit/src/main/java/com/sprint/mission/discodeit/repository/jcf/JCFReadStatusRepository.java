package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.exceptions.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
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
        return null;
    }

    @Override
    public void delete(ReadStatus readStatus) {

    }

    @Override
    public void deleteAllByUser(User user) {

    }

    @Override
    public void deleteAllByChannel(Channel channel) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }

    @Override
    public List<ReadStatus> findAllByUser(User user) {
        return List.of();
    }
}
