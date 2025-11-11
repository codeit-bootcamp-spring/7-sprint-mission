package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public ReadStatus create(ReadStatus readStatus) {
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void update(UUID id) {
        readStatusRepository.update(id);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId){
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    // ===== 🎯 Controller Direct (DTO 반환) =====
    @Override
    public ReadStatusInfoRes findById(UUID id) {
        ReadStatus readStatus=readStatusRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.READSTATUS_NOT_FOUND));
        return ReadStatusInfoRes.from(readStatus);
    }
}
