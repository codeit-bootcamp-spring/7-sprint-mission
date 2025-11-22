package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;    // 요구사항에 있으나 안써도 될 거 같음
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel)
    {
        Optional<Message> lastMessage = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId());

        List<UserDto> participants =// readStatusRepository.findAllByChannelId(channel.getId())
                channel.getReadStatuses()
                .stream().map(ReadStatus::getUser).map(userMapper::toDto)
                .collect(Collectors.toList());

        return ChannelDto.from(channel, participants, lastMessage.orElse(null));
    }

}
