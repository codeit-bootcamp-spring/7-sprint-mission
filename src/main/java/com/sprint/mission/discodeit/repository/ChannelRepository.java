package com.sprint.mission.discodeit.repository;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface ChannelRepository {

    Channel create(User user, String channelName);
    Channel read(UUID channelId);
    List<Channel> readAll();
    Channel update(UUID channelId, Consumer<Channel> updater);
    boolean delete(UUID channelId);
}
