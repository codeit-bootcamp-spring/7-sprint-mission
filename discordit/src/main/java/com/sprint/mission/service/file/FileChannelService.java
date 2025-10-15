package com.sprint.mission.service.file;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.service.ChannelService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    @Override
    public UUID createChannel(String name, Channel.ChannelType type, User... moderators) {
        return null;
    }

    @Override
    public void setChannelName(UUID uuid, String name) {

    }

    @Override
    public Channel getChannelById(UUID uuid) {
        return null;
    }

    @Override
    public Set<User> getAllMembers(UUID uuid) {
        return Set.of();
    }

    @Override
    public Set<User> getAllModerators(UUID uuid) {
        return Set.of();
    }

    @Override
    public void addMember(UUID uuid, User user) {

    }

    @Override
    public void addModerator(UUID uuid, User user) {

    }

    @Override
    public void deleteMember(UUID uuid, User user) {

    }

    @Override
    public void deleteModerator(UUID uuid, User user) {

    }

    @Override
    public List<UUID> getAllChannels() {
        return List.of();
    }

    @Override
    public UUID getNthChannel(int index) {
        return null;
    }

    @Override
    public List<UUID> getRegisteredChannels(User user) {
        return List.of();
    }

    @Override
    public List<UUID> getNotRegisteredChannels(User user) {
        return List.of();
    }
}
