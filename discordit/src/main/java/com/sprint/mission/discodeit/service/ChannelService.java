package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ChannelService {
    UUID createChannel(String name, Channel.ChannelType type, User... moderators); // 초기 운영자 한명일때
    void setChannelName(UUID uuid, String name);
    Channel getChannelById(UUID uuid);

    Set<User> getAllMembers(UUID uuid);

    Set<User> getAllModerators(UUID uuid);
    void addMember(UUID uuid, User user);

    void addModerator(UUID uuid, User user);
    void deleteMember(UUID uuid, User user);

    void deleteModerator(UUID uuid, User user);

    List<UUID> getAllChannelIds();
    UUID getNthChannel(int index);

    List<UUID> getRegisteredChannels(User user);
    List<UUID> getNotRegisteredChannels(User user);

}
