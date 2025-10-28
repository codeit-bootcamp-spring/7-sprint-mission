package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = false
)
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID,Channel> channelRepo ;


    public JCFChannelRepository() {
        this.channelRepo = new HashMap<>();
        resetChannelRepository();

    }

    @Override
    public Optional<Channel> getChannelById(UUID channelId) {

        return Optional.ofNullable(channelRepo.get(channelId));
    }

    @Override
    public Optional<Channel> getChannelByName(String channelName) {
        return channelRepo.values().stream().filter(x->x.getName().equals(channelName)).findFirst();
    }

    @Override
    public Optional<Channel> getChannel(Channel channel) {
        return getChannelById(channel.getId());
    }

    @Override
    public Channel saveChannel(Channel channel) {
        channelRepo.put(channel.getId(),channel);
        return channel;
    }

    @Override
    public void deleteChannel(Channel channel) {

        channelRepo.remove(channel.getId());


    }

    @Override
    public <T> void updateChannel(Channel channel) {

        channelRepo.remove(channel.getId());
        channelRepo.put(channel.getId(),channel);
    }

    @Override
    public List<Channel> getAllChannel() {
        return channelRepo.values().stream().toList();
    }

    @Override
    public List<Channel>getUpdatedChannel() {
        return channelRepo.values().stream().filter(x->x.getUpdatedAt()!=x.getCreatedAt()).toList();
    }



    @Override
    public void resetChannelRepository() {
        channelRepo.clear();


    }

    @Override
    public boolean isChannelExit(UUID channelId) {
        return channelRepo.containsKey(channelId);
    }
}
