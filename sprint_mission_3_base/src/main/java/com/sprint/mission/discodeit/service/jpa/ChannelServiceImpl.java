package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.event.ChannelChangedEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public ChannelDto create(ChannelCreateRequest request) {
        if (request.type() == ChannelType.PRIVATE) {
            if (request.participantIds() == null || request.participantIds().isEmpty()) {
                throw new ChannelException(
                        ErrorCode.BAD_REQUEST,
                        Map.of("reason", "PRIVATE channel requires participantIds")
                );
            }
        }

        Channel channel = new Channel(
                request.type(),
                request.name(),
                null
        );

        channelRepository.save(channel);
        ChannelDto dto = ChannelDto.from(channel);
        eventPublisher.publishEvent(new ChannelChangedEvent("channels.created", dto));
        return dto;
    }

    @Override
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new PrivateChannelUpdateException(channelId);
        }

        channel.update(request.name(), null);
        ChannelDto dto = ChannelDto.from(channel);
        eventPublisher.publishEvent(new ChannelChangedEvent("channels.updated", dto));
        return dto;
    }

    @Override
    @CacheEvict(cacheNames = "userChannels", allEntries = true)
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
        ChannelDto dto = ChannelDto.from(channel);
        channelRepository.delete(channel);
        eventPublisher.publishEvent(new ChannelChangedEvent("channels.deleted", dto));
    }

    @Override
    public ChannelDto find(UUID id) {
        return channelRepository.findById(id)
                .map(ChannelDto::from)
                .orElseThrow(() -> new ChannelNotFoundException(id));
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(ChannelDto::from)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "userChannels", key = "#userId")
    public List<ChannelDto> findByUserId(UUID userId) {
        if (userId == null) {
            throw new ChannelException(ErrorCode.BAD_REQUEST, Map.of("reason", "userId is null"));
        }
        return channelRepository.findAllByType(ChannelType.PUBLIC)
                .stream()
                .map(ChannelDto::from)
                .toList();
    }
}