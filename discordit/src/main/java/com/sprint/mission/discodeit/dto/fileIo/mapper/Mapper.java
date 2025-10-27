package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.Message;
import com.sprint.mission.discodeit.entity.base.Receivable;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.MessageIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

public final class Mapper {
    private Mapper(){}

    public static Channel toChannel(ChannelIoDTO dto, UserRepository repository){
        return ChannelMapper.toChannel(dto, repository);
    }

    public static ChannelIoDTO toChannelDTO(Channel channel) {
        return ChannelMapper.toDto(channel);
    }

    public static User toUser(UserIoDTO dto){
        return UserMapper.toUser(dto);
    }

    public static UserIoDTO toUserDto (User user) {
        return UserMapper.toDto(user);
    }

    public static Message<Receivable> toMessage (MessageIoDTO dto,
                                                 UserRepository userRepository,
                                                 ChannelRepository channelRepository){
        return MessageMapper.toMessage(dto, userRepository, channelRepository);
    }

    public static MessageIoDTO toMessageDto(Message<? extends Receivable> message) {
        return MessageMapper.toDto(message);
    }


}
