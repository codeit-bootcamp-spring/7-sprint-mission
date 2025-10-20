package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface JCFChannelService {
    Channel insert(List<User> users);
    Channel update(UUID id,
                   String name);
    Channel delete(UUID id);
    List<Channel> findAll();
    List<User> findAllByChannel(Channel channel);
    Channel findById(UUID id);
}
