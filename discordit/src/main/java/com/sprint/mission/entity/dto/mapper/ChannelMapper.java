package com.sprint.mission.entity.dto.mapper;

import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.entity.dto.ChannelDTO;
import com.sprint.mission.repository.UserRepository;

import java.util.HashSet;


final class ChannelMapper {
    private ChannelMapper() {}

    public static Channel toChannel(ChannelDTO dto, UserRepository userRepository){
        return Channel.rehydrate(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getChannelName(),
                Channel.ChannelType.valueOf(dto.getType()),
                new HashSet<>(userRepository.findByIds(dto.getModeratorUserIds().toArray(String[]::new))),
                new HashSet<>(userRepository.findByIds(dto.getMemberUserIds().toArray(String[]::new)))
        );
    }

    public static ChannelDTO toDto(Channel channel) {
        return new ChannelDTO(
                channel.getUuid(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelName(),
                channel.getType().name(),
                channel.getMembers().stream()
                        .map(User::getUserId)
                        .toList(),
                channel.getModerators().stream()
                        .map(User::getUserId)
                        .toList());
    }
}
