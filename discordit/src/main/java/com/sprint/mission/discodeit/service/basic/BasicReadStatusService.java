package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.entity.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.entity.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusDto create(ReadStatusCreateRequest dto) {
        ReadStatus readStatus = new ReadStatus(
                userRepository.findById(dto.userId())
                        .orElseThrow(() -> new UserNotFoundException(dto.userId())),
                channelRepository.findById(dto.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
        );
        readStatusRepository.save(readStatus);
        return ReadStatusMapper.toDto(readStatus);
    }

    @Override
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequest dto) {
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));
        readStatus.updateLastReadAt(dto.newLastReadAt());
        return ReadStatusMapper.toDto(readStatus);
    }

    public ReadStatusDto get(UUID uuid) {
        return ReadStatusMapper.toDto(readStatusRepository.findById(uuid)
                .orElseThrow(() -> new ReadStatusNotFoundException(uuid)));
    }

    public List<ReadStatusDto> getAllByUserId(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return readStatusRepository.findAllByUser(user).stream()
                .map(ReadStatusMapper::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        readStatusRepository.deleteById(uuid);
    }


}


