package com.sprint.mission.service;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;

import java.util.List;

public interface ChannelService {
    public Channel createChannel(Channel.ChannelType type, User moderators); // 초기 운영자 한명일때
    public Channel createChannel(Channel.ChannelType type, List<User> moderators); // 초기 운영자가 여러명일때

    public List<User> getAllMembers();
    public List<User> getAllModerators();

    public void addMember(User user);
    public void addModerator(User user);

    public void deleteMember(User user);
    public void deleteModerator(User user);

    public void addOnlineUser(User user);
    public void removeOnlineUser(User user);

}
