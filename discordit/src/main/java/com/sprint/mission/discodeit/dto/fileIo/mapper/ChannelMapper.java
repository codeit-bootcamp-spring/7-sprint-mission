package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.ChannelIoDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.common.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.HashSet;

final class ChannelMapper {
    private ChannelMapper() {
    }

    public static Channel toChannel(ChannelIoDTO dto, UserRepository userRepository) {
        return Channel.fromDto(
                dto.getUuid(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getChannelName(),
                dto.getDescription(),
                dto.getScope(),
                dto.getType(),
                new HashSet<>(dto.getModeratorUuids().stream()
                        .map(id -> userRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException(id)))
                        .toList()),
                new HashSet<>(dto.getMemberUuids().stream()
                        .map(id -> userRepository.findById(id)
                                .orElseThrow(() -> new UserNotFoundException(id)))
                        .toList())
        );
    }

    public static ChannelIoDTO toDto(Channel channel) {
        return new ChannelIoDTO(
                channel.getUuid(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getChannelName(),
                channel.getType(),
                channel.getScope(),
                channel.getDescription(),
                channel.getMembers().stream()
                        .map(User::getUuid)
                        .toList(),
                channel.getModerators().stream()
                        .map(User::getUuid)
                        .toList());
    }
}
