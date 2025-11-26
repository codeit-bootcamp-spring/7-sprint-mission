package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {
    private MessagesRepository messageRepository;
    private ReadStatusesRepository readStatusRepository;
    private UserMapper userMapper;

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
            .orElseThrow(() -> new IllegalArgumentException("🐳 messageRepository.findLatestMessage"));

        return ChannelDto.builder()
            .id(channel.getId())
            .type(channel.getType())
            .name(channel.getName())
            .description(channel.getDescription())
            .participants(userDtoList)
            .lastMessageAt(message.getUpdatedAt())
            .build();
    }
}
