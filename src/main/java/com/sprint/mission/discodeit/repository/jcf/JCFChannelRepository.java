package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new HashMap<>();

    private static JCFChannelRepository instance;
    private JCFChannelRepository() {}

    public static JCFChannelRepository getInstance(){
        if(instance == null){
            instance = new JCFChannelRepository();
        }
        return instance;
    }

    @Override
    public Channel save(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findById(UUID uuid) {
        return channels.get(uuid);
    }

    @Override
    public Channel findByName(String channelName) {
        return channels.values()
                .stream()
                .filter(c -> c.getChannelName().equals(channelName))
                .findFirst()
                .orElse(null);
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
