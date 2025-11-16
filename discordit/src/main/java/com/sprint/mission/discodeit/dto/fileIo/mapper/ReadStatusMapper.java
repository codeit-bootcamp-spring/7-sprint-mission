package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.ReadStatusIoDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

final class ReadStatusMapper {
    private ReadStatusMapper() {
    }

    public static ReadStatusIoDTO toDto(ReadStatus readStatus) {
        return new ReadStatusIoDTO(
                readStatus.getUuid(),
                readStatus.getUser().getUuid(),
                readStatus.getChannel().getUuid(),
                readStatus.getLastReadAt(),
                readStatus.getCreatedAt(),
                readStatus.getUpdatedAt()
        );
    }

    public static ReadStatus toReadStatus(ReadStatusIoDTO dto,
                                          UserRepository userRepository,
                                          ChannelRepository channelRepository) {
        User user = userRepository.find(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserUuid()));
        Channel channel = channelRepository.find(dto.getChannelUuid())
                .orElseThrow(() -> new ChannelNotFoundException(dto.getChannelUuid()));
        return ReadStatus.fromDto(dto.getUuid(), user, channel, dto.getCreatedAt(), dto.getUpdatedAt(), dto.getLastReadAt());
    }
}

