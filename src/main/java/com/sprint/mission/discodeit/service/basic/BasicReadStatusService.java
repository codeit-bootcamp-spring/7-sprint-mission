package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(() ->
                new RuntimeException("ReadStatus not found. id: " + id));
    }

    @Override
    public void update(UUID id) {
        readStatusRepository.update(id);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }

    // ===== 🎯 Controller Direct (DTO 반환) =====
    @Override
    public List<ReadStatusInfoRes> findAllByChannelId(UUID channelId){
        return readStatusRepository.findAllByChannelId(channelId).stream()
                .map(ReadStatusInfoRes::from)
                .toList();
    }

    @Override
    public List<ReadStatusInfoRes> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatusInfoRes::from)
                .toList();
    }
}
