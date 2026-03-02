package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

import static java.util.stream.Collectors.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper mapper;
    private final UserMapper userMapper;

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        log.info("ChannelService.createPublicChannel");
        Channel channel = new Channel(
                request.name(),
                request.description(),
                ChannelType.PUBLIC);

        Channel save = channelRepository.save(channel);

        List<ReadStatus> readList = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> {
                    ReadStatus readStatus = new ReadStatus(user, save, Instant.now(), false);
                    readList.add(readStatus);
                });
        readStatusRepository.saveAll(readList);
        return mapper.toDto(save);
    }

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        log.info("ChannelService.createPrivateChannel");
        Channel channel = new Channel("dm", "description", ChannelType.PRIVATE);
        Channel save = channelRepository.save(channel);

        List<User> users = userRepository.findAllById(request.participantIds());
        List<ReadStatus> result = new ArrayList<>();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            ReadStatus readStatus = new ReadStatus(user, save, Instant.now(), true);
            result.add(readStatus);
            userDtos.add(userMapper.toDto(user));
        }

        readStatusRepository.saveAll(result);

        ChannelDto dto = mapper.toDto(save);
        dto.setParticipants(userDtos);

        return dto;
    }

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public ChannelDto updateChannel(UUID channelId, ChannelUpdateRequest requestDto) {
        log.info("ChannelService.updateChannel");
        Channel channel =
                channelRepository.findById(channelId)
                        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, new HashMap<>()));
        if (requestDto.newName() != null) {
            channel.updateName(requestDto.newName());
        }
        if (requestDto.newDescription() != null) {
            channel.updateDescription(requestDto.newDescription());
        }
        return mapper.toDto(channel);
    }

    @PreAuthorize("hasAuthority('ROLE_CHANNEL_MANAGER')")
    @Transactional
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public void deleteChannel(UUID id) {
        log.info("ChannelService.deleteChannel");
        Channel channel = channelRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, new HashMap<>()));
        channelRepository.delete(channel);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "userChannels", key = "#userId")
    public List<ChannelDto> getAllByUser(UUID userId) {
        List<ReadStatus> readStatuses = readStatusRepository.findAllWithChannelByUserId(userId);

        List<Channel> list = readStatuses.stream()
                .map(rs -> rs.getChannel())
                .toList();

        List<UUID> privateChannelIds = list.stream()
                .filter(channel -> channel.getType() == ChannelType.PRIVATE)
                .map(channel -> channel.getId())
                .toList();

        List<ReadStatus> privateParticipants = readStatusRepository.findAllByChannelIds(privateChannelIds);

        Map<UUID, List<User>> usersByChannelId = privateParticipants.stream()
                .collect(groupingBy(rs -> rs.getChannel().getId(),
                        mapping(ReadStatus::getUser, toList())));

        return list.stream()
                .map(channel -> {
                    ChannelDto dto = mapper.toDto(channel);
                    if (channel.getType() == ChannelType.PRIVATE) {
                        List<User> users = usersByChannelId.get(channel.getId());
                        List<UserDto> userDtos = users.stream().map(userMapper::toDto).toList();
                        dto.setParticipants(userDtos);
                    }
                    return dto;
                }).toList();

    }
}
