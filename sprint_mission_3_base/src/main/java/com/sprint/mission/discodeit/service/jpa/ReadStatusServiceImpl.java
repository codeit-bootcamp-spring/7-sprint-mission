package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class ReadStatusServiceImpl implements ReadStatusService {

    private final ReadStatusRepository readRepo;
    private final UserRepository userRepo;
    private final ChannelRepository channelRepo;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest request) {
        User user = userRepo.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Channel channel = channelRepo.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        ReadStatus readStatus = new ReadStatus(user, channel, request.lastReadAt());
        readRepo.save(readStatus);

        return ReadStatusDto.from(readStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        return readRepo.findById(readStatusId)
                .map(ReadStatusDto::from)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readRepo.findAllByUserId(userId)
                .stream()
                .map(ReadStatusDto::from)
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readRepo.findById(readStatusId)
                .orElseThrow(() -> new IllegalArgumentException("ReadStatus not found"));

        readStatus.update(request.newLastReadAt(), request.newNotificationEnabled());
        return ReadStatusDto.from(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        readRepo.deleteById(readStatusId);
    }
}