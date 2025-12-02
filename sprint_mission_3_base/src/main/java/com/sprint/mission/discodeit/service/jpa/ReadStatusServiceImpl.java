package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadStatusServiceImpl implements ReadStatusService {

    private final ReadStatusRepository readRepo;
    private final UserRepository userRepo;
    private final ChannelRepository channelRepo;

    @Override
    public ReadStatusDto create(ReadStatusCreateRequest req) {

        User user = userRepo.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Channel channel = channelRepo.findById(req.channelId())
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        // 기존 기록이 있으면 update
        return readRepo.findByUserIdAndChannelId(req.userId(), req.channelId())
                .map(existing -> {
                    existing.updateReadTime();
                    return ReadStatusDto.from(existing);
                })
                .orElseGet(() -> {
                    ReadStatus newStatus = ReadStatus.builder()
                            .user(user)
                            .channel(channel)
                            .build();

                    readRepo.save(newStatus);
                    return ReadStatusDto.from(newStatus);
                });
    }

    @Override
    public ReadStatusDto find(UUID id) {
        return readRepo.findById(id)
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
    public void delete(UUID id) {
        readRepo.deleteById(id);
    }
}
