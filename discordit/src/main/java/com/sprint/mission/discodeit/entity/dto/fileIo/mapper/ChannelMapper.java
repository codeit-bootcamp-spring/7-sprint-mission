package com.sprint.mission.discodeit.entity.dto.fileIo.mapper;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashSet;


final class ChannelMapper {
    private ChannelMapper() {}

    public static Channel toChannel(ChannelIoDTO dto, UserRepository userRepository){
        return Channel.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getChannelName(),
                Channel.ChannelType.valueOf(dto.getType()),
                new HashSet<>(userRepository.findByIds(dto.getModeratorUserIds().toArray(String[]::new))),
                new HashSet<>(userRepository.findByIds(dto.getMemberUserIds().toArray(String[]::new)))
        );
    }

    public static ChannelIoDTO toDto(Channel channel) {
        return new ChannelIoDTO(
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
