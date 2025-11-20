package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entityElement.ReadStatusElement;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static java.time.Instant.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public void resetReadStatus() {

    }

    @Override
    public ReadStatusResponseDto createReadStatus(ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        UUID channelId = readStatusCreateRequestDto.getChannelId();
        UUID userId = readStatusCreateRequestDto.getUserId();
        if(!(channelRepository.isChannelExit(channelId)
                && userRepository.isUserExit(userId))
        )
        {
            throw new IllegalArgumentException("존재하지 않은 Channel 혹은 User 입니다.");
        }
        if(readStatusRepository.isReadStatusExist(userId,channelId)){
            throw new IllegalArgumentException("이미 존재하는 readStatus 입니다.");
        }
        ReadStatus readStatus = ReadStatus.builder()
                .readLastTime(now())
                .userId(readStatusCreateRequestDto.getUserId())
                .channelId(readStatusCreateRequestDto.getChannelId())
                .readLastTime(readStatusCreateRequestDto.getLastReadAt())
                .build();
        return ReadStatusResponseDto.from(
                readStatusRepository.createReadStatus(readStatus)
        );
    }

    @Override
    public void deleteReadStatus(UUID readStatusID) {
        List<ReadStatus>readStatusList = readStatusRepository.readAllReadStatus();
        if(readStatusList.stream().noneMatch(x->x.getId().equals(readStatusID))){
            throw new IllegalArgumentException("존재하지 않는 readStatus 입니다.");
        }
        readStatusRepository.deleteReadStatus(readStatusID);

    }

    @Override
    public <T>void updateReadStatus(ReadStatusUpdateRequestDto<T> readStatusUpdateRequestDto) {

        ReadStatus readStatus = readStatusRepository.readReadStatus(readStatusUpdateRequestDto.getReadStatusId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다."));
        Channel channel = channelRepository.getChannelById(readStatus.getChannelId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        ReadStatusElement readStatusElement = readStatusUpdateRequestDto.getType();
        BiConsumer<ReadStatus ,T> biConsumer = (BiConsumer<ReadStatus, T>) readStatusElement.setter;
        biConsumer.accept(readStatus, readStatusUpdateRequestDto.getSafeUpdateValue());
        readStatus.updateEntity();
        readStatusRepository.updateReadStatus(readStatus);
    }
    @Override
    public ReadStatusResponseDto readReadStatus(UUID readStatusID) {
        return ReadStatusResponseDto.from(
                readStatusRepository.readReadStatus(readStatusID).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다."))
        );
    }
    @Override
    public List<ReadStatusResponseDto> findAllyByUserId(UUID userID) {
        return
                readStatusRepository.readAllReadStatus().stream()
                        .filter(x->x.getUserId().equals(userID))
                        .map(ReadStatusResponseDto::from).toList();
    }

    @Override
    public List<ReadStatusResponseDto> readAllReadStatus() {
        return readStatusRepository.readAllReadStatus().stream().map(ReadStatusResponseDto::from).toList();
    }

    @Override
    public ReadStatusResponseDto patchReadStatus(UUID readStatusId, ReadStatusPatchRequestDto readStatusPatchRequestDto) {
        ReadStatus readStatus = readStatusRepository.readReadStatus(readStatusId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 readStatus 입니다."));
        readStatus.setReadLastTime(readStatusPatchRequestDto.newLastReadAt());
        readStatus.updateEntity();
        readStatusRepository.updateReadStatus(readStatus);
        return ReadStatusResponseDto.from(readStatus);
    }
}
