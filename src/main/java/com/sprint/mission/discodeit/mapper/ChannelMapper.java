package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final AuthService authService;

    public ChannelDto toDto(Channel channel)
    {
        Optional<Message> lastMessage = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId());
        Set<UUID> onlineUserIds = authService.getOnlineUserIds();

        List<UserDto> participants =
                channel.getReadStatuses()
                .stream().map(ReadStatus::getUser)
                        .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
                .collect(Collectors.toList());

        return ChannelDto.from(channel, participants, lastMessage.orElse(null));
    }

}
