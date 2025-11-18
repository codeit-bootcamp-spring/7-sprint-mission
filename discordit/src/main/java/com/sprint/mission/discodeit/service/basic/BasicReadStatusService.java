package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                userRepository.find(dto.userId())
                        .orElseThrow(() -> new UserNotFoundException(dto.userId())),
                channelRepository.find(dto.channelId())
                        .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
        );
        readStatusRepository.save(readStatus);
        return ReadStatusResponse.toDto(readStatus);
    }

    @Override
    public ReadStatusResponse update(UUID id, ReadStatusUpdateRequest dto) {
        ReadStatus readStatus = readStatusRepository.find(id)
                .orElseThrow(() -> new ReadStatusNotFoundException(id));
        readStatus.setLastReadAt(dto.newLastReadAt());
        readStatusRepository.update(readStatus);
        return ReadStatusResponse.toDto(readStatus);
    }

    public ReadStatusResponse get(UUID uuid) {
        return ReadStatusResponse.toDto(readStatusRepository.find(uuid)
                .orElseThrow(() -> new ReadStatusNotFoundException(uuid)));
    }

    public List<ReadStatusResponse> getAllByUserId(UUID id) {
        User user = userRepository.find(id).orElseThrow(() -> new UserNotFoundException(id));
        return readStatusRepository.findAllByUser(user).stream()
                .map(ReadStatusResponse::toDto)
                .toList();
    }

    @Override
    public List<ReadStatusResponse> getAll() {
        return readStatusRepository.findAll().stream()
                .map(ReadStatusResponse::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        readStatusRepository.delete(uuid);
    }


}


