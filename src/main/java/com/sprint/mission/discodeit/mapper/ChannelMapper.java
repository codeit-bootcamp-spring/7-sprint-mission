package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.dto_Neo.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.dto.dto_Neo.UserDto;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChannelMapper {
    private final MessagesRepository messageRepository;
    private final ReadStatusesRepository readStatusRepository;
    private final UserMapper userMapper;

    public ChannelDto toDto(Channel channel) {

        List<UserDto> userDtoList = readStatusRepository
            .findAllByChannelId(channel.getId())
            .stream()
            .map(readStatuses -> userMapper.toDto(readStatuses.getUser()))
            .toList();

        Message message = messageRepository
//            .findLatestMessage(channel.getId(), PageRequest.of(0, 1))
            .findFirstByChannelIdOrderByCreatedAtDesc(channel.getId())
            .stream()
            .findFirst()
            .orElse(null);

        Instant lastMessageAt = (null == message) ? null : message.getUpdatedAt();

        return ChannelDto.builder()
            .id(channel.getId())
            .type(channel.getType())
            .name(channel.getName())
            .description(channel.getDescription())
            .participants(userDtoList)
            .lastMessageAt(lastMessageAt)
            .build();
    }
}
