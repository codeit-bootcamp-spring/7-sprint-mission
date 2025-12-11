package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.exception.NotFoundUserException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadStatusServiceImpl implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusDto createReadStatus(ReadStatusCreateRequest requestDto) {
        // User, Channel Id 입력
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new NotFoundUserException("사용자를 찾을 수 없음"));
        Channel channel = channelRepository.findById(requestDto.channelId())
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없음"));
        // 중복검사
        readStatusRepository.findByUserIdAndChannelId(requestDto.userId(), requestDto.channelId())
                .ifPresent(readStatus -> {
                    throw new DuplicateRequestException("이미 존재함");
                });

        ReadStatus readStatus = new ReadStatus(user, channel);
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadStatusDto findReadStatusById(UUID id) {
        ReadStatus status = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        return readStatusMapper.toDto(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId)
                .stream().map(readStatusMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReadStatusDto updateReadStatus(UUID id, ReadStatusUpdateRequest updateDto) {
        ReadStatus status = readStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));

        status.updateReadStatus(updateDto.newLastReadAt());
        readStatusRepository.save(status);
        return readStatusMapper.toDto(status);
    }

    @Override
    @Transactional
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
