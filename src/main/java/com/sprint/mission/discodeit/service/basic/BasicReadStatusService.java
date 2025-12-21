package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.readstatus.InvalidReadStatusRequestException;
import com.sprint.mission.discodeit.common.exception.readstatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        if (readStatusCreateRequestDto == null) {
            throw new InvalidReadStatusRequestException("readStatusCreateRequestDto is null");
        }
        if (readStatusCreateRequestDto.userId() == null) {
            throw new InvalidReadStatusRequestException("userId is null");
        }
        if (readStatusCreateRequestDto.channelId() == null) {
            throw new InvalidReadStatusRequestException("channelId is null");
        }

        UUID userId = readStatusCreateRequestDto.userId();
        UUID channelId = readStatusCreateRequestDto.channelId();

        log.debug("Creating read status: userId {} ", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        Instant readAt = readStatusCreateRequestDto.lastReadAt() != null
                ? readStatusCreateRequestDto.lastReadAt() :  Instant.now();

        if(readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            log.warn("Read status already exists for read status: userId {}, channelId = {} ", userId, channelId);
            throw new ReadStatusAlreadyExistsException(userId, channelId);
        }


        ReadStatus readstatus = new ReadStatus(user, channel, readAt);
        ReadStatus save = readStatusRepository.save(readstatus);
        log.info("유저 읽기 상태가 생성되었습니다. readStatusId = {}", save.getId());

        return readStatusMapper.toDto(save);
    }

    @Override
    public ReadStatusResponseDto get(UUID id) {
        if (id == null) {
            throw new InvalidReadStatusRequestException("id is null");
        }
        log.debug("Getting read status: readStatus Id = {} ", id);
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> getAllByUserId(UUID userId) {
        if (userId == null) {
            throw new InvalidReadStatusRequestException("userId is null");
        }
        log.debug("Getting read status by userId: userId = {} ", userId);
        return readStatusRepository.findAllByUserId(userId)
                .stream()
                .map(rs -> readStatusMapper.toDto(rs))
                .toList();
    }

    @Transactional
    @Override
    public ReadStatusResponseDto update(UUID id, ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        if (readStatusUpdateRequestDto == null) {
            throw new InvalidReadStatusRequestException("readStatusUpdateRequestDto is null");
        }
        if (id == null) {
            throw new InvalidReadStatusRequestException("id is null");
        }
        log.debug("Updating read status: readStatus Id = {} ", id);
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));

        if (readStatusUpdateRequestDto.newLastReadAt() != null) {
            if(readStatus.getLastReadAt() == null ||
                    readStatusUpdateRequestDto.newLastReadAt().isAfter(readStatus.getLastReadAt())) {
                readStatus.readAt(readStatusUpdateRequestDto.newLastReadAt());
            }
        }

        ReadStatus save = readStatusRepository.save(readStatus);
        log.info("유저 읽기 상태가 수정되었습니다. readStatusId = {}", save.getId());
        return readStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        if (id == null) {
            throw new InvalidReadStatusRequestException("id is null");
        }
        log.debug("Deleting read status: readStatus Id = {} ", id);
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));
        readStatusRepository.delete(readStatus);
        log.info("유저 읽기 상태가 제거되었습니다. readStatusId = {}", readStatus.getId());
        return true;
    }
}
