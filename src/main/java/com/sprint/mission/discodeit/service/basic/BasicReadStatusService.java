package com.sprint.mission.discodeit.service.basic;

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

    @Override
    public ReadStatus create(ReadStatus readStatus) {
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id);
    }

    @Override
    public void update(UUID id) {
        readStatusRepository.update(id);
    }

    @Override
    public ReadStatus delete(UUID id) {
        return readStatusRepository.delete(id);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId){
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }
}
