package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;
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

import java.util.List;
import java.util.UUID;

import static java.time.Instant.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    public void resetReadStatus() {

    }

    @Override
    @Transactional
    public ReadStatusDto createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto) {

        Channel targetChannel = channelRepository.findById(readStatusCreateRequestDto.getChannelId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        User targetUser = userRepository.findById(readStatusCreateRequestDto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));
        ReadStatus readStatus = ReadStatus.builder()
                .readLastTime(now())
                .user(targetUser)
                .channel(targetChannel)
                .readLastTime(readStatusCreateRequestDto.getLastReadAt())
                .build();
        return readStatusMapper.toDto(
                readStatusRepository.save(readStatus)
        );
    }

    @Override
    public void deleteReadStatus(UUID readStatusID) {
        readStatusRepository.deleteById(readStatusID);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> findAllyByUserId(UUID userID) {
        return
                readStatusRepository.findAll().stream()
                        .filter(x->x.getUser().getId().equals(userID))
                        .map(readStatusMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusDto> readAllReadStatus() {
        return readStatusRepository.findAll().stream().map(readStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ReadStatusDto patchReadStatus(UUID readStatusId, ReadStatusPatchRequestDto readStatusPatchRequestDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다."));
        readStatus.setReadLastTime(readStatusPatchRequestDto.newLastReadAt());
        readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(readStatus);
    }
}
