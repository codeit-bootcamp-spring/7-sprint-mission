package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    // 중복 메서드 만들기
    private ReadStatus getReadStatus(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus를 찾을 수 없습니다."));
    }

    @Override
    public ReadStatusResponseDto createReadStatus(CreateReadStatusRequestDto request) {
        channelRepository.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Optional<ReadStatus> exist = readStatusRepository.findByUserIdAndChannelId(
                request.userId(),
                request.channelId());

        if(exist.isPresent()) {
            throw new IllegalArgumentException("이미 존재합니다.");
        }

        ReadStatus readStatus = new ReadStatus(
                request.userId(),
                request.channelId()
        );

        ReadStatus save = readStatusRepository.save(readStatus);
        return ReadStatusResponseDto.from(save);
    }

    @Override
    public ReadStatusResponseDto find(UUID readStatusId) {
        ReadStatus readStatus = getReadStatus(readStatusId);
        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);
        List<ReadStatusResponseDto> dtoList = new ArrayList<>();
        for(ReadStatus readStatus : readStatuses){
            dtoList.add(ReadStatusResponseDto.from(readStatus));
        }
        return dtoList;
    }

    @Override
    public ReadStatusResponseDto updateReadStatus(UUID readStatusId, UpdateReadStatusDto updateReadStatus) {
        ReadStatus readStatus = getReadStatus(readStatusId);

        readStatus.updateReadTime();

        ReadStatus save = readStatusRepository.save(readStatus);
        return ReadStatusResponseDto.from(save);
    }

    @Override
    public void deleteReadStatus(UUID readStatusId) {
        getReadStatus(readStatusId);

        readStatusRepository.delete(readStatusId);
    }
}
