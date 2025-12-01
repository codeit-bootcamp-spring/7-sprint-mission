package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        UUID userId = Objects.requireNonNull(readStatusCreateRequestDto.userId());
        UUID channelId = Objects.requireNonNull(readStatusCreateRequestDto.channelId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        Instant readAt = readStatusCreateRequestDto.lastReadAt() != null
                ? readStatusCreateRequestDto.lastReadAt() :  Instant.now();

        if(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new IllegalStateException("User already has read status");
        }


        ReadStatus readstatus = new ReadStatus(user, channel, readAt);
        ReadStatus save = readStatusRepository.save(readstatus);

        return readStatusMapper.toDto(save);
    }

    @Override
    public ReadStatusResponseDto get(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> getAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(Objects.requireNonNull(userId))
                .stream()
                .map(rs -> readStatusMapper.toDto(rs))
                .toList();
    }

    @Transactional
    @Override
    public ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        ReadStatus readStatus = readStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));

        if (readStatusUpdateRequestDto.newLastReadAt() != null) {
            if(readStatus.getLastReadAt() == null ||
                    readStatusUpdateRequestDto.newLastReadAt().isAfter(readStatus.getLastReadAt())) {
                readStatus.readAt(readStatusUpdateRequestDto.newLastReadAt());
            }
        }

        ReadStatus save = readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));
        readStatusRepository.delete(readStatus);
        return true;
    }
}
