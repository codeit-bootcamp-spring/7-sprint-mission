package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.domain.ReadStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatusDto createReadStatus(ReadStatusCreateRequest request){
        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
        readStatusRepository.save(readStatus);
        return ReadStatusDto.from(readStatus);
    }

    public ReadStatusDto updateReadStatus(UUID id){
        ReadStatus readStatus = getById(id);
        readStatus.read();
        readStatusRepository.save(readStatus);
        return ReadStatusDto.from(readStatus);
    }

    public List<ReadStatusDto> getAllByUserId(UUID id){
        return readStatusRepository.findAll().stream()
                .filter(rs-> rs.getUserId().equals(id))
                .map(ReadStatusDto::from)
                .toList();
    }
    private ReadStatus getById(UUID id){
        return readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 메시지 수신 정보가 없습니다."));
    }
}
