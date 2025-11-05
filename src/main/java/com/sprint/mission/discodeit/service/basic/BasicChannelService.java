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
    public Channel createPublic(PublicChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        Channel channel = new Channel(
                channelCreateRequestDto.channelType(),
                channelCreateRequestDto.channelName(),
                false,
                slowMode,
                channelCreateRequestDto.description()
        );

        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivate(PrivateChannelCreateRequestDto channelCreateRequestDto) {
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

        return channel;
    }

    @Override
    public Channel get(UUID channelId) {
        return channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
    }

    @Override
    public List<Channel> getAll() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getAllByUserId(UUID userId) {
        List<Channel> all = channelRepository.findAll();

        return all.stream()
                .filter(channel -> channel.getMembers().containsKey(userId))
                .toList();
    }

    @Override
    public Channel update(ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelUpdateRequestDto.channelId()))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        if(channel.isPrivateChannel()) {
            throw new UnsupportedOperationException("PRIVATE channel cannot be updated");
        }
        if(channelUpdateRequestDto.channelName() != null) {
            channel.rename(channelUpdateRequestDto.channelName());
        }
        if(channelUpdateRequestDto.channelDescription() != null) {
            channel.changeChannelDescription(channelUpdateRequestDto.channelDescription());
        }
        if(channelUpdateRequestDto.slowModeSeconds() != null) {
            channel.changeSlowModeSeconds(channelUpdateRequestDto.slowModeSeconds());
        }

        return channelRepository.save(channel);
    }

    @Override
    public boolean delete(UUID channelId) {
        Objects.requireNonNull(channelId);

        // 메세지 제거 + 첨부 파일 제거
        List<Message> message = messageRepository.findByChannelId(channelId);
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
        return channelRepository.deleteById(channelId);
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

    @Override
    public Instant getLastMessageAt(UUID channelId) {
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        return messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(m -> m.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);
    }
}
