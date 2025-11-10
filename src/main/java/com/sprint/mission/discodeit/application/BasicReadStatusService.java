package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.ReadStatusRequest;
import com.sprint.mission.discodeit.application.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
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

    public void createReadStatus(UUID userId, UUID channelId){
        ReadStatus readStatus = new ReadStatus(userId, channelId);
        readStatusRepository.save(readStatus);
    }

    public void updateReadStatus(UUID id){
        ReadStatus readStatus = getById(id);
        readStatus.read();
    }

    public Long getTimeSinceLastRead(UUID id){
        ReadStatus readStatus = getById(id);
        return readStatus.timeSinceLastRead();

    }

    public ReadStatusResponse getReadStatusByIds(ReadStatusRequest request){
        ReadStatus readStatus1 = readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(request.userId())
                        && readStatus.getChannelId().equals(request.channelId())).findAny().orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 존재하지 않습니다."));

        return new ReadStatusResponse(readStatus1.getUserId(),readStatus1.getChannelId(),readStatus1.timeSinceLastRead());

    }

    public ReadStatus getById(UUID id){
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 없습니다."));
    }

    public List<ReadStatusResponse> getAllByUserId(UUID id){
        return readStatusRepository.findAll().stream()
                .filter(rs-> rs.getUserId().equals(id))
                .map(rs-> new ReadStatusResponse(rs.getUserId(),rs.getChannelId(),rs.timeSinceLastRead()))
                .toList();
    }
}
