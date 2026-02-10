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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Transactional
    public ReadStatusDto create(ReadStatusCreateRequest dto) {
        log.info("읽음 상태 요청 들어옴. \n\t유저\t : {}\n\t채널\t : {}\n\t", dto.userId(), dto.channelId());
        ReadStatus readStatus = new ReadStatus(
                userRepository.findById(dto.userId())
                        .orElseThrow(() -> new UserNotFoundException(dto.userId())),
                channelRepository.findById(dto.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
        );
        readStatusRepository.save(readStatus);
        log.info("읽음 상태 생성 완료. ");
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID id, ReadStatusUpdateRequest dto) {
        log.info("읽음 상태 업데이트 요청 들어옴.");
        ReadStatus readStatus = readStatusRepository.findById(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));
        readStatus.updateLastReadAt(dto.newLastReadAt());
        log.info("읽음 상태 업데이트 완료.");
        return readStatusMapper.toDto(readStatus);
    }

    public ReadStatusDto get(UUID uuid) {
        log.info("읽음 상태 조회 요청 들어옴.");
        return readStatusMapper.toDto(readStatusRepository.findById(uuid)
                .orElseThrow(() -> new ReadStatusNotFoundException(uuid)));
    }

    public List<ReadStatusDto> getAllByUserId(UUID userId) {
        log.info("유저 전체 읽음 상태 조회 요청 들어옴 - user : {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return readStatusRepository.findAllByUser(user).stream()
                .map(readStatusMapper::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        log.warn("읽음 상태 제거 요청 들어옴 - {}", uuid);
        readStatusRepository.deleteById(uuid);
        log.info("읽음 상태 제거 완료");
    }


}


