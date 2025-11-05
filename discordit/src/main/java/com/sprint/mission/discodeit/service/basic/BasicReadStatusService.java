package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusGetByUserRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public ReadStatusResponse create(ReadStatusCreateRequest dto) {
        ReadStatus readStatus = new ReadStatus(
                userRepository.findByUserId(dto.userId())
                        .orElseThrow(() -> new UserNotFoundException(dto.userId())),
                channelRepository.findById(dto.ChannelId())
                        .orElseThrow(() -> new ChannelNotFoundException(dto.ChannelId()))
        );
        readStatusRepository.save(readStatus);
        return ReadStatusResponse.toDto(readStatus);
    }

    @Override
    public ReadStatusResponse update(ReadStatusUpdateRequest dto) {
        User user = userRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        ReadStatus readStatus = readStatusRepository.find(user, channel)
                .orElseThrow(() -> new ReadStatusNotFoundException(user, channel));
        readStatus.setLastReadAt(Instant.from(dto.readTime()));
        readStatusRepository.update(readStatus);
        return ReadStatusResponse.toDto(readStatus);
    }

    public ReadStatusResponse get(UUID uuid) {
        return ReadStatusResponse.toDto(readStatusRepository.findById(uuid)
                .orElseThrow(() -> new ReadStatusNotFoundException(uuid)));
    }

    public List<ReadStatusResponse> getAllByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return readStatusRepository.findAllByUser(user).stream()
                .map(ReadStatusResponse::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        readStatusRepository.delete(uuid);
    }


}


