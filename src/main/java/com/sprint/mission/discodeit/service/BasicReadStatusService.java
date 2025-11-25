package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.domain.ReadStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatusDto createReadStatus(ReadStatusCreateRequest request){
        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
        readStatusRepository.save(readStatus);
        return ReadStatusDto.from(readStatus);
    }

    public ReadStatusDto updateReadStatus(String id){
        ReadStatus readStatus = getById(id);
        readStatus.read();
        readStatusRepository.save(readStatus);
        return ReadStatusDto.from(readStatus);
    }

    public List<ReadStatusDto> getAllByUserId(String id){
        return readStatusRepository.findAll().stream()
                .filter(rs-> rs.getUserId().equals(id))
                .map(ReadStatusDto::from)
                .toList();
    }
    private ReadStatus getById(String id){
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 없습니다."));
    }
}
