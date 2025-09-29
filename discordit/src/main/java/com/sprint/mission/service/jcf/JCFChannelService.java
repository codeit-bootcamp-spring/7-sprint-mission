package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.ChannelIdNotFoundException;
import com.sprint.mission.exceptions.UserNotFoundException;
import com.sprint.mission.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static final Map<UUID, Channel> data = new HashMap<>();


    @Override
    public UUID createChannel(String name, Channel.ChannelType type, User... moderators) {
        HashSet<User> moderatorSet = new HashSet<>(Arrays.asList(moderators));
        Channel channel = new Channel(name, type, moderatorSet);
        data.put(channel.getUuid(), channel);
        return channel.getUuid();
    }

    @Override
    public void setChannelName(UUID uuid, String name) {
        getChannelByUuid(uuid).setChannelName(name);
    }

    @Override
    public Set<User> getAllMembers(UUID uuid) {
        return getChannelByUuid(uuid).getMembers();
    }

    @Override
    public Set<User> getAllModerators(UUID uuid) {
        return getChannelByUuid(uuid).getModerators();
    }

    @Override
    public void addMember(UUID uuid, User user) {
        getChannelByUuid(uuid).addMember(user);
    }

    @Override
    public void addModerator(UUID uuid, User user) {
        getChannelByUuid(uuid).addModerator(user);
    }

    @Override
    public void deleteMember(UUID uuid, User user) {
        getChannelByUuid(uuid).deleteMember(user);
    }

    @Override
    public void deleteModerator(UUID uuid, User user) {
        getChannelByUuid(uuid).deleteModerator(user);
    }

    private Channel getChannelByUuid(UUID uuid) {
        Channel channel = data.get(uuid);
        if(channel == null)
            throw new ChannelIdNotFoundException(uuid);
        return channel;
    }
}
