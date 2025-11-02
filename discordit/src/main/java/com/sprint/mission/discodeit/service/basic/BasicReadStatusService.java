package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequestDto;
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

    public ReadStatusResponse create(ReadStatusCreateRequestDto dto) {
        ReadStatus readStatus = new ReadStatus(
                userRepository.findByUserId(dto.userId()),
                channelRepository.findById(dto.ChannelId())
        );
        readStatusRepository.save(readStatus);
        return ReadStatusResponse.toDto(readStatus);
    }

    public ReadStatusResponse get(UUID uuid) {
        return ReadStatusResponse.toDto(readStatusRepository.findById(uuid));
    }

    public List<ReadStatusResponse> getAllByUserId(String userId) {
        return readStatusRepository.findAllByUser(userRepository.findByUserId(userId)).stream()
                .map(ReadStatusResponse::toDto)
                .toList();
    }


    public void read(UUID uuid) {
        readStatusRepository.findById(uuid).read();
    }

    public void delete(UUID uuid) {
        readStatusRepository.deleteById(uuid);
    }


}


