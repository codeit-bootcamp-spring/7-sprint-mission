package com.sprint.mission.discodeit.entity.status.service;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.entity.status.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.status.dto.ReadStatusInfoDto;
import com.sprint.mission.discodeit.entity.status.dto.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.status.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ReadStatusInfoDto createReadStatus(ReadStatusCreateDto createDto) {
        // User, Channel Id 입력
        userRepository.findById(createDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));
        channelRepository.findById(createDto.channelId())
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없음"));
        // 중복검사
        readStatusRepository.findByUserIdAndChannelId(createDto.userId(), createDto.channelId())
                .ifPresent(readStatus -> {
                    throw new DuplicateRequestException("이미 존재함");
                });

        ReadStatus readStatus = new ReadStatus(createDto.userId(), createDto.channelId());
        readStatusRepository.save(readStatus);

        return ReadStatusInfoDto.from(readStatus);
    }

    @Override
    public Optional<ReadStatusInfoDto> findReadStatusById(UUID id) {
        return readStatusRepository.findById(id).map(ReadStatusInfoDto::from);
    }

    @Override
    public List<ReadStatusInfoDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId)
                .stream().map(ReadStatusInfoDto::from).collect(Collectors.toList());
    }

    @Override
    public Optional<ReadStatusInfoDto> updateReadStatus(ReadStatusUpdateDto updateDto) {
        return readStatusRepository.findById(updateDto.readStatusId()).map(readStatus -> {
            readStatus.updateReadStatus();
            readStatusRepository.save(readStatus);
            return ReadStatusInfoDto.from(readStatus);
        });
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
