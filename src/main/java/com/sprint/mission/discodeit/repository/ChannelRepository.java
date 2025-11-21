package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {


//    public Optional<Channel> getChannelById(UUID channelId);
//    public Optional<Channel> getChannelByName(String channelName);
//    public Optional<Channel> getChannel(Channel channel);
//
//    public Channel saveChannel(Channel channel);
//    public void deleteChannel(Channel channel);
//    public void updateChannel(Channel channel);
//
//    public List<Channel> getAllChannel();
//    public List<Channel> getUpdatedChannel();
////    public DeletedChannelDto[] getDeletedChannel();
////    public void addUserToChannel(User userDto, Channel channelDto);
////    public void deleteUserFromChannel(User userDto, Channel channelDto);
//
//    public void resetChannelRepository();
//    public boolean isChannelExit(UUID channelId);
}
