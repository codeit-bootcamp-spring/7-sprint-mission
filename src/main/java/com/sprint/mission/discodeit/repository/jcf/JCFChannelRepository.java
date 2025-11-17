package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> channels = new ConcurrentHashMap<>();

  @Override
  public Channel save(Channel channel) {
    channels.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID uuid) {

    return Optional.ofNullable(channels.get(uuid));
  }

  @Override
  public Optional<Channel> findByName(String channelName, ChannelType channelType) {
    return channels.values()
        .stream()
        .filter(
            channel -> channelType == channel.getType() && channelName.equals(channel.getName()))
        .findFirst();
  }

  @Override
  public List<Channel> findAll() {
    return new ArrayList<>(channels.values());
  }

  @Override
  public void delete(UUID id) {
    channels.remove(id);
  }
}
