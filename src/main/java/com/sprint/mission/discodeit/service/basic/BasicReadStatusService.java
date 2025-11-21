package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.converter.ReadStatusDtoConverter;
import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelVisibility;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatusResponseDto create(CreateReadStatusRequestDto request) {
        ReadStatus newStatus;

        // 유저가 존재하지 않으면 예외 발생
        userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 채널이 존재하지 않으면 예외 발생
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 공개 채널에 생성하는 경우 예외 발생
        if(channel.getVisibility() == ChannelVisibility.PUBLIC) {
            throw new CustomException(ErrorCode.PUBLIC_CHANNEL_ADD_READSTATUS_FORBIDDEN);
        }

        if(!existsByUserIdAndChannelId(request.userId(),request.channelId())) {
            newStatus = new ReadStatus(request.userId(),request.channelId(), request.lastReadAt());
            readStatusRepository.save(newStatus);
        } else {
            throw new CustomException(ErrorCode.CHANNEL_MEMBER_ALREADY_EXISTS);
        }

        return toDto(newStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().stream()
                .filter(r -> userId.equals(r.getUserId()))
                .map(r -> toDto(r))
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponseDto update(UUID readStatusId, UpdateReadStatusRequestDto request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
        readStatus.update(request.newLastReadAt());
        readStatusRepository.update(readStatus);
        return toDto(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
        readStatusRepository.deleteById(readStatusId);
    }

    private ReadStatusResponseDto toDto(ReadStatus readStatus) {
        return ReadStatusDtoConverter.toResponseDto(readStatus);
    }

    private boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusRepository.findAll().stream()
                .anyMatch(r -> userId.equals(r.getUserId()) && channelId.equals(r.getChannelId()));
    }
}
