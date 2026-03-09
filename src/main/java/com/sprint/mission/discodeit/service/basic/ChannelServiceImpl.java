package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private static final int CREATED = 0;
    private static final int UPDATED = 1;
    private static final int DELETED = 2;

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;
    private final SseService sseService;

    @Override
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @CacheEvict(value = "userChannels", allEntries = true)
    public ChannelDto createPublicChannel(PublicChannelCreateRequest requestDto) {

        log.debug("공개 채널 생성 요청 - name: {}, description: {}",
                requestDto.name(), requestDto.description());

        Channel newChannel = new Channel(
                requestDto.name(),
                requestDto.description(),
                ChannelType.PUBLIC
        );

        channelRepository.save(newChannel);
        log.info("공개 채널 생성 완료");
        ChannelDto dto = channelMapper.toDto(newChannel);

        SseBroadcast(CREATED, dto);

        return dto;
    }

    @Override
    @Transactional
    @CacheEvict(value = "userChannels", allEntries = true)
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest requestDto) {
        log.info("프라이빗 채널 생성 요청");

        if (requestDto.participantIds() == null || !requestDto.participantIds().isEmpty()) {
            log.warn("프라이빗 채널 생성 실패 - 참여자가 없습니다.");
            throw new IllegalArgumentException("프라이빗 채널은 1명 이상의 참여자가 필요합니다.");
        }

        Channel newChannel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(newChannel);

        List<User> users = userRepository.findAllById(requestDto.participantIds());
        List<ReadStatus> readStatuses = users.stream()
                .map(user -> new ReadStatus(user, newChannel))
                .collect(Collectors.toList());
        newChannel.getReadStatuses().addAll(readStatuses);
        readStatusRepository.saveAll(readStatuses);

        log.info("프라이빗 채널 생성 완료");
        ChannelDto dto = channelMapper.toDto(newChannel);

        sseService.send(
                requestDto.participantIds(),
                "channels.created",
                dto
        );

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "channel", key = "#id")
    public ChannelDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));

        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userChannels", key = "#userId")
    public List<ChannelDto> findAllByUserId(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<Channel> allChannels = channelRepository.findAll();    // 모든 채널

        // 유저 수신 정보로부터 채널Id 가져오기
        Set<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId)
                .stream().map(readStatus -> readStatus.getChannel().getId())
                .collect(Collectors.toSet());

        return allChannels.stream().filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    } else {
                        return myChannelIds.contains(channel.getId());
                    }
                })
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Caching(evict = {
            @CacheEvict(value = "channel", key = "#channelId"),
            @CacheEvict(value = "userChannels", allEntries = true)
    })
    public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest updateDto) {

        log.debug("채널 수정 요청 - channelId: {}", channelId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> {
                    log.warn("채널 수정 실패 - 채널을 찾을 수 없음: {}", channelId);
                    return new ChannelNotFoundException(channelId);
                });

        if (channel.getType() == ChannelType.PRIVATE) {
            log.warn("채널 수정 실패 - 프라이빗 채널 수정 불가: {}", channelId);
            throw new PrivateChannelUpdateException(channelId);
        }

        channel.updateChannelInfo(updateDto.newName(), updateDto.newDescription());

        channelRepository.save(channel);
        log.info("채널 수정 성공: {}", channelId);
        ChannelDto dto = channelMapper.toDto(channel);

        SseBroadcast(UPDATED, dto);

        return dto;

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Caching(evict = {
            @CacheEvict(value = "channel", key = "#id"),
            @CacheEvict(value = "userChannels", allEntries = true)
    })
    public void deleteChannel(UUID id) {
        log.info("채널 삭제 요청: {}", id);
        channelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("채널 삭제 실패 - 채널을 찾을 수 없음: {}", id);
                    return new ChannelNotFoundException(id);
                });
        log.info("채널 삭제 성공: {}", id);
        SseBroadcast(DELETED, id);
        channelRepository.deleteById(id);
    }

    private void SseBroadcast(int type, Object data) {

        String eventName = switch (type) {
            case CREATED -> "channels.created";
            case UPDATED -> "channels.updated";
            case DELETED -> "channels.deleted";
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };

        sseService.broadcast(eventName, data);
    }
}
