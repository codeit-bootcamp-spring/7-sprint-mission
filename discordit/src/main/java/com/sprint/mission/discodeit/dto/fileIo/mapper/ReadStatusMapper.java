package com.sprint.mission.discodeit.dto.fileIo.mapper;

import com.sprint.mission.discodeit.dto.fileIo.ReadStatusIoDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;

final class ReadStatusMapper {
    private ReadStatusMapper() {}

    public static ReadStatusIoDTO toDto(ReadStatus readStatus) {
        return new ReadStatusIoDTO(
                readStatus.getId(),
                readStatus.getUser().getUuid(),
                readStatus.getChannel().getUuid(),
                readStatus.getLastReadAt()
        );
    }

    public static ReadStatus toReadStatus(ReadStatusIoDTO dto,
                                          UserRepository userRepository,
                                          ChannelRepository channelRepository) {
        User user = userRepository.findById(dto.getUserUuid());
        Channel channel = channelRepository.findById(dto.getChannelUuid());
        return ReadStatus.fromDto(dto.getUuid(), user, channel, dto.getLastReadAt());
    }
}

