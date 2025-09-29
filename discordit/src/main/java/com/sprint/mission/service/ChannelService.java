package com.sprint.mission.service;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    public UUID createChannel(String name, Channel.ChannelType type, User... moderators); // 초기 운영자 한명일때

    public void setChannelName(UUID uuid, String name);

    public Set<User> getAllMembers(UUID uuid);
    public Set<User> getAllModerators(UUID uuid);

    public void addMember(UUID uuid, User user);
    public void addModerator(UUID uuid, User user);

    public void deleteMember(UUID uuid, User user);
    public void deleteModerator(UUID uuid, User user);

}
