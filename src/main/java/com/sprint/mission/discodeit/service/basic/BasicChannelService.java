package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        Channel channel = new Channel(
                channelCreateRequestDto.channelType(),
                channelCreateRequestDto.channelName(),
                false,
                slowMode,
                channelCreateRequestDto.description()
        );

        Channel save = channelRepository.save(channel);
        Instant lastMessageAt = messageRepository.findByChannelId(save.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return ChannelResponseDto.from(save,lastMessageAt);
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        Channel channel = new Channel(
                channelCreateRequestDto.channelType(),
                null,
                true,
                slowMode,
                null
        );

        if (channelCreateRequestDto.userIds() != null) {
            for(UUID id : channelCreateRequestDto.userIds()) {
                channel.join(id);
            }
        }

        channel = channelRepository.save(channel);

        if(channelCreateRequestDto.userIds() != null) {
            for(UUID id : channelCreateRequestDto.userIds()) {
                readStatusRepository.save(new ReadStatus(id, channel.getId(), channel.getCreatedAt()));
            }
        }

        Instant lastMessageAt = messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return ChannelResponseDto.from(channel,lastMessageAt);
    }

    @Override
    public ChannelResponseDto get(UUID channelId) {
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        Instant lastMessageAt = messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);
        return ChannelResponseDto.from(channel,lastMessageAt);
    }

    @Override
    public List<ChannelResponseDto> getAll() {
        return channelRepository.findAll().stream()
                .map(channel -> ChannelResponseDto.from(channel,
                        messageRepository.findByChannelId(channel.getId())
                                .stream()
                                .map(message -> message.getCreatedAt())
                                .max(Comparator.naturalOrder())
                                .orElse(null)))
                .toList();
    }

    @Override
    public List<ChannelResponseDto> getAllByUserId(UUID userId) {
        return channelRepository.findAll()
                .stream()
                .filter(channel -> channel.getMembers().containsKey(userId))
                .map(channel -> ChannelResponseDto.from(channel,
                        messageRepository.findByChannelId(channel.getId())
                                .stream()
                                .map(message -> message.getCreatedAt())
                                .max(Comparator.naturalOrder())
                                .orElse(null)))
                .toList();
    }

    @Override
    public ChannelResponseDto update(UUID channelId, ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        if(channel.isPrivateChannel()) {
            throw new IllegalArgumentException("Private channel is not allowed to update");
        }
        if(channelUpdateRequestDto.newName() != null) {
            channel.rename(channelUpdateRequestDto.newName());
        }
        if(channelUpdateRequestDto.newDescription() != null) {
            channel.changeChannelDescription(channelUpdateRequestDto.newDescription());
        }
        if(channelUpdateRequestDto.slowModeSeconds() != null) {
            channel.changeSlowModeSeconds(channelUpdateRequestDto.slowModeSeconds());
        }

        Channel save = channelRepository.save(channel);
        Instant lastMessageAt = messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return ChannelResponseDto.from(save,lastMessageAt);
    }

    @Override
    public void delete(UUID channelId) {
        // 메세지 제거 + 첨부 파일 제거
        List<Message> message = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        for(Message m : message) {
            if(m.getAttachmentIds() != null) {
                for(UUID attachmentId : m.getAttachmentIds()) {
                    binaryContentRepository.deleteById(attachmentId);
                }
            }
            messageRepository.deleteById(m.getId());
        }
        // status 제거
        readStatusRepository.deleteAllByChannelId(channelId);

        // 채널 제거
        channelRepository.deleteById(channelId);
    }


    @Override
    public boolean join(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        boolean changed = channel.join(userId);
        if(changed) {
            channelRepository.save(channel);
        }
        return changed;
    }

    @Override
    public boolean leave(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        boolean changed  = channel.leave(userId);
        if(changed) {
            channelRepository.save(channel);
        }
        return changed;
    }

    @Override
    public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {
        if(slowModeSeconds < 0) {
            throw new  IllegalStateException("slowModeSeconds cannot be negative");
        }
        Channel channel = channelRepository.findById(channelId)
                        .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        channel.changeSlowModeSeconds(slowModeSeconds);
        channelRepository.save(channel);
    }
}
