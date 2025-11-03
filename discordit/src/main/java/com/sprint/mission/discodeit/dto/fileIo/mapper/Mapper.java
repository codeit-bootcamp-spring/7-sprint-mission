package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.BinaryContentIoDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.MessageIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.ReadStatusIoDTO;
import com.sprint.mission.discodeit.dto.fileIo.UserStatusIoDTO;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

public final class Mapper {
    private Mapper(){}

    public static Channel toChannel(ChannelIoDTO dto, UserRepository repository){
        return ChannelMapper.toChannel(dto, repository);
    }

    public static ChannelIoDTO toChannelDTO(Channel channel) {
        return ChannelMapper.toDto(channel);
    }

    public static UserIoDTO toUserDto (User user) {
        return UserMapper.toDto(user);
    }

    public static User toUser(UserIoDTO dto, BinaryContentRepository contentRepository) {
        return UserMapper.toUser(dto, contentRepository);
    }

    public static Message toMessage (MessageIoDTO dto,
                                     UserRepository userRepository,
                                     ChannelRepository channelRepository,
                                     BinaryContentRepository contentRepository){
        return MessageMapper.toMessage(dto, userRepository, channelRepository, contentRepository);
    }

    public static MessageIoDTO toMessageDto(Message message) {
        return MessageMapper.toDto(message);
    }


    public static BinaryContentIoDTO toDto(BinaryContent binaryContent) {
        return BinaryContentMapper.toDto(binaryContent);
    }

    public static BinaryContent toBinaryContent(BinaryContentIoDTO dto,
                                                UserRepository userRepository) {
        return BinaryContentMapper.toBinaryContent(dto, userRepository);
    }

    public static ReadStatusIoDTO toDto(ReadStatus readStatus) {
        return ReadStatusMapper.toDto(readStatus);
    }

    public static ReadStatus toReadStatus(ReadStatusIoDTO dto,
                                          UserRepository userRepository,
                                          ChannelRepository channelRepository) {
        return ReadStatusMapper.toReadStatus(dto, userRepository, channelRepository);
    }

    public static UserStatusIoDTO toDto(UserStatus userStatus) {
        return UserStatusMapper.toDto(userStatus);
    }

    public static UserStatus toUserStatus(UserStatusIoDTO dto,
                                          UserRepository userRepository) {
        return UserStatusMapper.toUserStatus(dto, userRepository);
    }
}
