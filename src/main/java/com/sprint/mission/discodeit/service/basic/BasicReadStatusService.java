package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.global.exception.channel.ChannelMemberAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusResponseDto create(CreateReadStatusRequestDto request) {
        ReadStatus newStatus;

        // 유저가 존재하지 않으면 예외 발생
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", request.userId())
                ));

        // 채널이 존재하지 않으면 예외 발생
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(
                        ErrorCode.CHANNEL_NOT_FOUND,
                        Map.of("channelId", request.channelId())
                ));

        if(!existsByUserIdAndChannelId(request.userId(), request.channelId())) {
            if(channel.getChannelType().equals(ChannelType.PUBLIC)) {
                newStatus = new ReadStatus(user, channel, request.lastReadAt(), false);
            } else {
                newStatus = new ReadStatus(user, channel, request.lastReadAt(), true);
            }
            readStatusRepository.save(newStatus);
        } else {
            throw new ChannelMemberAlreadyExistsException(
                    ErrorCode.CHANNEL_MEMBER_ALREADY_EXISTS,
                    Map.of("userId", request.userId(), "channelId", request.channelId())
            );
        }

        return readStatusMapper.toResponseDto(newStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(
                        ErrorCode.READSTATUS_NOT_FOUND,
                        Map.of("readStatusId", readStatusId)
                ));
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserIdWithUserAndChannel(userId).stream()
                .map(r -> readStatusMapper.toResponseDto(r))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReadStatusResponseDto update(UUID readStatusId, UpdateReadStatusRequestDto request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(
                        ErrorCode.READSTATUS_NOT_FOUND,
                        Map.of("readStatusId", readStatusId)
                ));
        readStatus.update(request.newLastReadAt(), request.newNotificationEnabled());
        readStatusRepository.save(readStatus);
        return readStatusMapper.toResponseDto(readStatus);
    }

    @Override
    @Transactional
    public void delete(UUID readStatusId) {
        readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new ReadStatusNotFoundException(
                        ErrorCode.READSTATUS_NOT_FOUND,
                        Map.of("readStatusId", readStatusId)
                ));
        readStatusRepository.deleteById(readStatusId);
    }

    private boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusRepository.findAll().stream()
                .anyMatch(r -> userId.equals(r.getUser().getId()) && channelId.equals(r.getChannel().getId()));
    }
}
