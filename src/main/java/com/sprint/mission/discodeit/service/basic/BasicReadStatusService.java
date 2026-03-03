package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.exception.domain.readStatus.ReadStatusException;
import com.sprint.mission.discodeit.exception.domain.readStatus.ReadStatusNotExistException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
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
        UUID channelId = readStatusCreateRequestDto.channelId();
        UUID userId =readStatusCreateRequestDto.userId();
        Channel targetChannel = channelRepository.findById(channelId)
                .orElseThrow(()->new ChannelNotExistException(channelId));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(()->new UserNotExistException(userId));
        ReadStatus readStatus = ReadStatus.createReadStatusWithDtoFactory
                (targetUser,targetChannel,readStatusCreateRequestDto.lastReadAt());
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
        ReadStatus readStatus = readStatusRepository.findById(readStatusId).orElseThrow(()->new ReadStatusNotExistException(readStatusId));
        if(readStatusPatchRequestDto.newLastReadAt()!=null)readStatus.setReadLastTime(readStatusPatchRequestDto.newLastReadAt());
        readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(readStatus);
    }
}
