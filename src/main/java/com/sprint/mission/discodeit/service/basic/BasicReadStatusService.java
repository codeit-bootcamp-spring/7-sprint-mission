package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        UUID userId = Objects.requireNonNull(readStatusCreateRequestDto.userId());
        UUID channelId = Objects.requireNonNull(readStatusCreateRequestDto.channelId());

        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if(!channel.getMembers().containsKey(userId)) {
            throw new NoSuchElementException("Channel is private");
        }

        if(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new IllegalStateException("User already has read status");
        }

        Instant readAt = readStatusCreateRequestDto.lastReadAt() != null
                ? readStatusCreateRequestDto.lastReadAt() :  Instant.now();

        ReadStatus readstatus = new ReadStatus(userId, channelId, readAt);

        return readStatusRepository.save(readstatus);
    }

    @Override
    public ReadStatus get(UUID id) {
        return readStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
    }

    @Override
    public List<ReadStatus> getAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(Objects.requireNonNull(userId));
    }

    @Override
    public ReadStatus update(ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        ReadStatus readStatus = readStatusRepository.findById(Objects.requireNonNull(readStatusUpdateRequestDto
                .id())).orElseThrow(() -> new NoSuchElementException("ReadStatus not found"));

        if (readStatusUpdateRequestDto.lastReadAt() != null) {
            if(readStatus.getLastReadAt() == null || readStatusUpdateRequestDto.lastReadAt().isAfter(readStatus.getLastReadAt())) {
                readStatus.readAt(readStatusUpdateRequestDto.lastReadAt());
            }
        }

        return readStatusRepository.save(readStatus);
    }

    @Override
    public boolean delete(UUID id) {
        return readStatusRepository.deleteById(Objects.requireNonNull(id));
    }
}
