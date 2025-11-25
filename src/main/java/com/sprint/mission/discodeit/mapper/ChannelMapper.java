package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ChannelMapper {
    private final UserMapper userMapper;

    public ChannelResponseDto toDto(
            Channel channel,
            Instant lastMessageAt,
            List<User> participants,
            Map<UUID, Boolean> userOnlineMap) {
        String name = channel.isPrivateChannel() ? null : channel.getName();
        String description = channel.isPrivateChannel() ? null : channel.getDescription();

        List<User> participantList = (participants != null) ? participants : Collections.emptyList();
        Map<UUID, Boolean> userOnlineList = (userOnlineMap != null) ? userOnlineMap : Collections.emptyMap();

        List<UserResponseDto> participantDtos = participantList.stream()
                .map(user -> {
                    UUID userId = user.getId();
                    boolean online = Boolean.TRUE.equals(userOnlineMap.get(userId));
                    return userMapper.toDto(user, online);
                })
                .toList();

        return new ChannelResponseDto(
                channel.getId(),
                name,
                description,
                channel.getSlowModeSeconds(),
                lastMessageAt,
                channel.isPrivateChannel(),
                participantDtos,
                channel.getType()
        );
    }
}
