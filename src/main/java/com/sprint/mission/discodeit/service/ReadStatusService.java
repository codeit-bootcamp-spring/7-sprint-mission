package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.ReadStatusEntity;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.service.mapper.ReadStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusMapper mapper;

    public ReadStatusDto createReadStatus(ReadStatusCreateRequest request){
        ReadStatus readStatus =
                new ReadStatus(
                        request.userId(),
                        request.channelId(),
                        request.lastReadAt()
                );
        ReadStatusEntity readStatusEntity = mapper.toReadStatusEntity(readStatus);
        ReadStatusEntity savedReadStatusEntity
                = readStatusRepository.save(readStatusEntity);

        return mapper.toReadStatusDto(savedReadStatusEntity);
    }

    public ReadStatusDto updateReadStatus(UUID readStatusId){
        ReadStatusEntity readStatusEntity =
                readStatusRepository.findById(readStatusId).orElseThrow(() -> new NoSuchElementException("해당 채널에 대한 수신 정보가 존재하지 않습니다."));
        readStatusEntity.setLastReadAt(Instant.now());

        return mapper.toReadStatusDto(readStatusEntity);
    }

    public List<ReadStatusDto> getAllByUserId(UUID id){
        return readStatusRepository.findAllByUserId(id)
                .stream()
                .map(mapper::toReadStatusDto)
                .toList();
    }

}
