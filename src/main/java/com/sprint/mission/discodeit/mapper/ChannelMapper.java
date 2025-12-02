package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    private final UserMapper userMapper;

    public ChannelDto toResponseDto(Channel channel) {
        Instant lastMessageAt = null;

        Message message = messageRepository.findTop1ByChannelIdOrderByCreatedAtDesc(channel.getId())
                .orElse(null);

        if(message != null) {
            lastMessageAt = message.getCreatedAt();
        }

        // fetch join 적용
        List<UserResponseDto> participants = readStatusRepository.findAllByChannelWithUser(channel).stream()
                .map(rs -> userMapper.toResponseDto(rs.getUser()))
                .toList();

        return new ChannelDto(
                channel.getId(),
                channel.getChannelType(),
                channel.getChannelName(),
                channel.getDescription(),
                participants,
                lastMessageAt
        );
    }

}
