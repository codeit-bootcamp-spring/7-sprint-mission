package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        ChannelType channelType =
                channelCreateRequestDto.type() == null ?
                        ChannelType.PUBLIC : channelCreateRequestDto.type();

        Channel channel = new Channel(
                channelType,
                channelCreateRequestDto.name(),
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

    @Transactional
    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        ChannelType channelType =
                channelCreateRequestDto.type() == null ?
                        ChannelType.PRIVATE : channelCreateRequestDto.type();

        Channel channel = new Channel(
                channelType,
                null,
                true,
                slowMode,
                null
        );

        Channel save = channelRepository.save(channel);

        if(channelCreateRequestDto.participantIds() != null) {
            for(UUID id : channelCreateRequestDto.participantIds()) {
                User user = userRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                readStatusRepository.save(new ReadStatus(user, save, save.getCreatedAt()));
            }
        }

        Instant lastMessageAt = messageRepository.findByChannelId(save.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return ChannelResponseDto.from(save,lastMessageAt);
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
        List<ReadStatus> users = readStatusRepository.findAllByUserId(userId);
        Set<UUID> joinedChannelIds = new HashSet<>();
        for (ReadStatus readStatus : users) {
            if(readStatus.getChannel() != null) {
                joinedChannelIds.add(readStatus.getChannel().getId());
            }
        }

        return channelRepository.findAll()
                .stream()
                .filter(channel ->
                        !channel.isPrivateChannel()
                                || joinedChannelIds.contains(channel.getId()))
                .map(channel -> ChannelResponseDto.from(channel,
                        messageRepository.findByChannelId(channel.getId())
                                .stream()
                                .map(message -> message.getCreatedAt())
                                .max(Comparator.naturalOrder())
                                .orElse(null)))
                .toList();
    }

    @Transactional
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

    @Transactional
    @Override
    public void delete(UUID channelId) {
        // 메세지 제거 + 첨부 파일 제거
        List<Message> message = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        messageRepository.deleteAll(message);

        // status 제거
        readStatusRepository.deleteAllByChannelId(channelId);

        // 채널 제거
        channelRepository.deleteById(channelId);
    }


    @Transactional
    @Override
    public boolean join(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if(readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
             return false;
         }

         readStatusRepository.save(new ReadStatus(user, channel, channel.getCreatedAt()));
        return true;
    }

    @Transactional
    @Override
    public boolean leave(UUID channelId, UUID userId) {
        return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .map(readStatus -> {
                    readStatusRepository.delete(readStatus);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
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
