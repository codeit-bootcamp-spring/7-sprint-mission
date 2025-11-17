package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    /**
     * 채널 생성
     */
    @Override
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        Channel channel = new Channel(
            ChannelType.PUBLIC,
            request.name(),
            request.description()
        );

        Channel saved = channelRepository.save(channel);
        return toDto(saved);
    }

    @Override
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {

        Channel channel = new Channel(
            ChannelType.PRIVATE,
            null,
            null
        );

        Channel saved = channelRepository.save(channel);

        // 참여자 읽음 상태 초기화
        if (request.participantIds() != null) {
            request.participantIds().forEach(userId -> {
                readStatusRepository.save(
                    new ReadStatus(userId, saved.getId(), Instant.MIN)
                );
            });
        }

        return toDto(saved);
    }

    /**
     * 채널 조회
     */
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
            .map(this::toDto)
            .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));
    }

    /**
     * 사용자별 채널 전체 조회
     */
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
            .map(ReadStatus::getChannelId)
            .toList();

        return channelRepository.findAll().stream()
            .filter(channel ->
                channel.getType() == ChannelType.PUBLIC ||
                    mySubscribedChannelIds.contains(channel.getId())
            )
            .map(this::toDto)
            .toList();
    }

    @Override
    public ChannelDto updatePublicChannel(UUID channelId, PublicChannelUpdateRequest request) {
        return null;
    }

    @Override
    public void deleteChannel(UUID channelId) {

    }

    /**
     * 채널 수정
     */
    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("비공개 채널은 수정할 수 없습니다.");
        }

        channel.update(request.newName(), request.newDescription());
        return channelRepository.save(channel);
    }

    /**
     * 채널 삭제
     */
    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new NoSuchElementException("Channel not found: " + channelId));

        messageRepository.deleteAllByChannelId(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }

    /**
     * Entity → DTO 변환
     */
    private ChannelDto toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
            .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
            .map(Message::getCreatedAt)
            .findFirst()
            .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType() == ChannelType.PRIVATE) {
            readStatusRepository.findAllByChannelId(channel.getId()).stream()
                .map(ReadStatus::getUserId)
                .forEach(participantIds::add);
        }

        return new ChannelDto(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            participantIds,
            lastMessageAt
        );
    }
}
