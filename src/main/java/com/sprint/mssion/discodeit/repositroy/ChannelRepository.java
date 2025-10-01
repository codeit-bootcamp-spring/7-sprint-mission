package com.sprint.mssion.discodeit.repositroy;

import com.sprint.mssion.discodeit.entity.Channel;
import com.sprint.mssion.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);

    Optional<Channel> findById(UUID channelId);

    List<Channel> findAll();

    void deleteById(UUID channelId);

    boolean existsById(UUID channelId);


}
