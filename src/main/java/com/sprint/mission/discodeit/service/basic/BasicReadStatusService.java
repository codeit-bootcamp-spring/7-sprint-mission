package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.request.UpdateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.discodietException.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    @Transactional
    public ReadStatusResponseDto createReadStatus(CreateReadStatusDto createReadStatusDto) {
        User user = userRepository.findById(createReadStatusDto.userId())
                .orElseThrow(() -> UserNotFoundException.byId(createReadStatusDto.userId()));

        Channel channel = channelRepository.findById(createReadStatusDto.channelId())
                .orElseThrow(() -> ChannelNotFoundException.byId(createReadStatusDto.channelId()));

        // TODO: 성능 낭비로 코드 개선
        if (readStatusRepository.findAllByUserId(createReadStatusDto.userId())
                .stream().anyMatch(
                        readStatus -> readStatus.getChannel().getId()
                                .equals(createReadStatusDto.channelId()))) {
            throw ReadStatusAlreadyExistsException.byUserAndChannelId(createReadStatusDto.userId(), createReadStatusDto.channelId());
        }

        ReadStatus readStatus = ReadStatus.builder()
                .user(user)
                .channel(channel)
                .lastReadAt(createReadStatusDto.lastReadAt())
                .build();

        readStatusRepository.save(readStatus);

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    @Transactional
    public ReadStatusResponseDto getReadStatus(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReadStatusResponseDto> getAllReadStatusByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatusResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public ReadStatusResponseDto updateReadStatus(UUID readStatusId, UpdateReadStatusDto updateReadStatusDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> ReadStatusNotFoundException.byId(readStatusId));
        readStatus.update(updateReadStatusDto.newLastReadAt());

        return ReadStatusResponseDto.from(readStatus);
    }

    @Override
    @Transactional
    public void deleteReadStatus(UUID readStatusId) {
        if (!readStatusRepository.existsById(readStatusId)) {
            throw ReadStatusNotFoundException.byId(readStatusId);
        }
        readStatusRepository.deleteById(readStatusId);
    }
}
