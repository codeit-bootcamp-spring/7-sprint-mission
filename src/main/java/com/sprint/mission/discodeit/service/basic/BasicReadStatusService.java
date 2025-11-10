package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.dto.readStatusDto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponseDto createReadStatus(ReadStatusRequestDto requestDto) {
        // User, Channel Id 입력
        userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));
        channelRepository.findById(requestDto.channelId())
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없음"));
        // 중복검사
        readStatusRepository.findByUserIdAndChannelId(requestDto.userId(), requestDto.channelId())
                .ifPresent(readStatus -> {
                    throw new DuplicateRequestException("이미 존재함");
                });

        ReadStatus readStatus = new ReadStatus(requestDto.userId(), requestDto.channelId());
        readStatusRepository.save(readStatus);

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public ReadStatusResponseDto findReadStatusById(UUID id) {
        ReadStatus status = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        return ReadStatusResponseDto.from(status);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId)
                .stream().map(ReadStatusResponseDto::from).collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponseDto updateReadStatus(ReadStatusUpdateDto updateDto) {
        ReadStatus status = readStatusRepository.findByUserIdAndChannelId(updateDto.userId(), updateDto.channelId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        status.updateReadStatus();
        readStatusRepository.save(status);
        return ReadStatusResponseDto.from(status);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
