package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.User;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    // TODO: 동일 유저 채널 존재시 예외 반환
    void save(ReadStatus readStatus);

    ReadStatus find(User user, Channel channel);
    ReadStatus findById(UUID uuid);

    void delete(ReadStatus readStatus);
    void deleteAllByUser(User user);
    void deleteAllByChannel(Channel channel);
    void deleteById(UUID uuid);

    List<ReadStatus> findAllByUser(User user);
}
