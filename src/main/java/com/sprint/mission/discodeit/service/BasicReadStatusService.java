package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatusResponse createReadStatus(ReadStatusCreateRequest request){
        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
        readStatusRepository.save(readStatus);
        return ReadStatusResponse.from(readStatus);
    }

    public ReadStatusResponse updateReadStatus(UUID id){
        ReadStatus readStatus = getById(id);
        readStatus.read();
        readStatusRepository.save(readStatus);
        return ReadStatusResponse.from(readStatus);
    }

    public Long getTimeSinceLastRead(UUID id){
        ReadStatus readStatus = getById(id);
        return readStatus.timeSinceLastRead();

    }

    public ReadStatusResponse getReadStatusByIds(ReadStatusRequest request){
        ReadStatus readStatus1 = readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(request.userId())
                        && readStatus.getChannelId().equals(request.channelId())).findAny().orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 존재하지 않습니다."));

        return ReadStatusResponse.from(readStatus1);

    }



    public List<ReadStatusResponse> getAllByUserId(UUID id){
        return readStatusRepository.findAll().stream()
                .filter(rs-> rs.getUserId().equals(id))
                .map(rs-> ReadStatusResponse.from(rs))
                .toList();
    }
    private ReadStatus getById(UUID id){
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 없습니다."));
    }
}
