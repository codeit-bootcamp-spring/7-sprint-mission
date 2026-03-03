package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.service.mapper.ReadStatusMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper mapper;


    public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {
        User user =
                userRepository.findById(request.userId()).orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,new HashMap<>()));

        Channel channel =
                channelRepository.findById(request.channelId()).orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, new HashMap<>()));



        ReadStatus readStatus =
                new ReadStatus(
                        user,
                        channel,
                        request.lastReadAt(),
                        channel.getType() == ChannelType.PRIVATE
                );
        ReadStatus saved
                = readStatusRepository.save(readStatus);

        return mapper.toDto(saved);
    }

    public ReadStatusDto updateReadStatus(UUID readStatusId) {
        ReadStatus readStatus =
                readStatusRepository.findById(readStatusId).orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND, new HashMap<>()));
        readStatus.updateLastReadAt(Instant.now());


        return mapper.toDto(readStatus);
    }

    public List<ReadStatusDto> getAllByUserId(UUID id) {


        return readStatusRepository.findAllByUserId(id)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
