package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public UUID create(ReadStatusCreateRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User not found: " + request.userId());
        }
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("Channel not found: " + request.channelId());
        }
        if (readStatusRepository.existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            throw new IllegalStateException("ReadStatus already exists for (user,channel)");
        }
        var rs = new ReadStatus(request.userId(), request.channelId(), request.lastReadAt());
        readStatusRepository.save(rs);
        return rs.getId();
    }

    @Override
    public Optional<ReadStatus> find(UUID id) {
        return readStatusRepository.findById(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void update(ReadStatusUpdateRequest request) {
        var rs = readStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus not found: " + request.id()));
        rs.update(request.lastReadAt()); // 엔티티에 update(Instant) 메서드가 있다고 가정
        readStatusRepository.save(rs);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);
    }
}
