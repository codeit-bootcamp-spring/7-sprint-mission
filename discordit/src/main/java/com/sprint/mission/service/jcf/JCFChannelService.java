package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.ChannelIdNotFoundException;
import com.sprint.mission.service.ChannelService;

import java.util.*;
import java.util.function.Predicate;

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
    public List<UUID> getAllChannels() {
        return data.values().stream()
                .sorted(Comparator.comparing(Channel::getDisplayName))
                .map(Channel::getUuid)
                .toList();
    }

    @Override
    public List<UUID> getRegisteredChannels(User user) {
        List<UUID> registered = new ArrayList<>();
        for (Channel c : data.values()) {
            if (c.getMembers().contains(user))
                registered.add(c.getUuid());
        }
        Collections.sort(registered);
        return registered;
    }

    @Override
    public List<UUID> getNotRegisteredChannels(User user) {
        List<UUID> registered = getRegisteredChannels(user);
        // TODO 작성중
        return new ArrayList<>();
    }

    @Override
    public void setChannelName(UUID uuid, String name) {
        getChannelByUuid(uuid).setChannelName(name);
    }

    @Override
    public Channel getChannelById(UUID uuid) {
        return data.get(uuid);
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
        if (channel == null)
            throw new ChannelIdNotFoundException(uuid);
        return channel;
    }
}
