package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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
                userRepository.findById(request.userId()).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        Channel channel =
                channelRepository.findById(request.channelId()).orElseThrow(() -> new NoSuchElementException("해당 채널이 존재하지 않습니다."));


        ReadStatus readStatus =
                new ReadStatus(
                        user,
                        channel,
                        request.lastReadAt()
                );
        ReadStatus saved
                = readStatusRepository.save(readStatus);

        return mapper.toDto(saved);
    }

    public ReadStatusDto updateReadStatus(UUID readStatusId) {
        ReadStatus readStatus =
                readStatusRepository.findById(readStatusId).orElseThrow(() -> new NoSuchElementException("해당 채널에 대한 수신 정보가 존재하지 않습니다."));
        readStatus.updateLastReadAt(Instant.now());


        return mapper.toDto(readStatus);
    }

    public List<ReadStatusDto> getAllByUserId(UUID id) {


        return readStatusRepository.findAllByUser_Id(id)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
