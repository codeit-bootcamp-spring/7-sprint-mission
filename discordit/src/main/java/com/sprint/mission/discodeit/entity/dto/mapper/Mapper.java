package com.sprint.mission.discodeit.entity.dto.mapper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Receivable;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.ChannelDTO;
import com.sprint.mission.discodeit.entity.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.dto.UserDTO;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

public final class Mapper {
    private Mapper(){}

    public static Channel toChannel(ChannelDTO dto, UserRepository repository){
        return ChannelMapper.toChannel(dto, repository);
    }

    public static ChannelDTO toChannelDTO(Channel channel) {
        return ChannelMapper.toDto(channel);
    }

    public static User toUser(UserDTO dto){
        return UserMapper.toUser(dto);
    }

    public static UserDTO toUserDto (User user) {
        return UserMapper.toDto(user);
    }

    public static Message<Receivable> toMessage (MessageDTO dto,
                                                 UserRepository userRepository,
                                                 ChannelRepository channelRepository){
        return MessageMapper.toMessage(dto, userRepository, channelRepository);
    }

    public static MessageDTO toMessageDto(Message<? extends Receivable> message) {
        return MessageMapper.toDto(message);
    }


}
