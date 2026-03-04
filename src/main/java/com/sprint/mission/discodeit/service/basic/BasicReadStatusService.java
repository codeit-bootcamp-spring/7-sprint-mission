package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateCommand;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusDuplicatedException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserReader userReader;
    private final ChannelReader channelReader;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto requestDto) {
        if (requestDto.userId() == null || requestDto.channelId() == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }

        log.debug("읽음 상태 생성 시도 - userId={}, channelId={}", requestDto.userId(), requestDto.channelId());
        User user = userReader.findUserOrThrow(requestDto.userId());
        Channel channel = channelReader.findChannelOrThrow(requestDto.channelId());


        if (readStatusRepository.existsByUserIdAndChannelId(requestDto.userId(), requestDto.channelId())) {
            throw new ReadStatusDuplicatedException(requestDto.userId(), requestDto.channelId());
        }


        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .build();

        ReadStatus saved = readStatusRepository.save(readStatus);

        log.info("읽음 상태 생성 완료 - readStatusId={}, userId={}, channelId={}",
                saved.getId(), requestDto.userId(), requestDto.channelId());

        return readStatusMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusResponseDto getReadStatus(UUID readStatusId) {
        log.debug("읽음 상태 단건 조회 - readStatusId={}", readStatusId);
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponseDto> getAllReadStatusesByUserId(UUID userId) {
        log.debug("유저별 읽음 상태 목록 조회 - userId={}", userId);
        List<ReadStatus> allByUserId = readStatusRepository.findAllByUserId(userId);
        log.debug("유저별 읽음 상태 목록 조회 결과 - userId={}, count={}", userId, allByUserId.size());
        return allByUserId.stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ReadStatusUpdateResponseDto updateReadStatus(ReadStatusUpdateCommand command) {
        log.debug("읽음 상태 수정 시도 - readStatusId={}", command.id());
        ReadStatus readStatusById = readStatusRepository.findById(command.id()).orElseThrow();

        boolean isUpdated = readStatusById.applyPatch(command.readAt(), command.notificationEnabled());

        if (isUpdated) {
            ReadStatus saved = readStatusRepository.save(readStatusById);
            log.info("읽음 상태 수정 완료 - readStatusId={}, readAt={}", saved.getId(), command.readAt());

            return ReadStatusUpdateResponseDto.from(saved);
        }

        log.debug("읽음 상태 수정 없음 - readStatusId={}, reason=no_change", command.id());
        return null;
    }

    @Override
    @Transactional
    public void deleteReadStatus(UUID id) {
        log.debug("읽음 상태 삭제 시도 - readStatusId={}", id);
        readStatusRepository.deleteById(id);
        log.info("읽음 상태 삭제 완료 - readStatusId={}", id);
    }

}
