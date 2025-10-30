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

        Channel channel = Channel.builder()
                .channelName(channelCreateRequestDto.channelName())
                .type(channelCreateRequestDto.channelType())
                .slowModeSeconds(slowMode)
                .channelDescription(channelCreateRequestDto.description())
                .privateChannel(false)
                .build();

        channel = channelRepository.save(channel);

        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelDescription(),
                slowMode,
                null,
                false,
                List.of(),
                channel.getType()
        );
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        Channel channel = Channel.builder()
                .channelName(null)
                .channelDescription(null)
                .type(channelCreateRequestDto.channelType())
                .slowModeSeconds(slowMode)
                .privateChannel(true)
                .build();

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

        return new ChannelResponseDto(
                channel.getId(),
                null,
                null,
                slowMode,
                null,
                true,
                channelCreateRequestDto.userIds() == null ?
                List.of() : channelCreateRequestDto.userIds(),
                channel.getType()
        );
    }

    @Override
    public ChannelResponseDto get(UUID channelId) {
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));
        Instant lastMessage = messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(m -> m.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<UUID> userId = channel.isPrivateChannel()
                ? new ArrayList<>(channel.getMembers().keySet()) : List.of();

        return new ChannelResponseDto(
                channel.getId(),
                channel.isPrivateChannel() ? null : channel.getChannelName(),
                channel.isPrivateChannel() ? null : channel.getChannelDescription(),
                channel.getSlowModeSeconds(),
                lastMessage,
                channel.isPrivateChannel(),
                userId,
                channel.getType()
        );
    }

    @Override
    public List<ChannelResponseDto> getAll() {
        List<Channel> all = channelRepository.findAll();
        List<ChannelResponseDto> channelResponseDtoList = new ArrayList<>();
        for (Channel channel : all) {
            Instant lastMessage = messageRepository.findByChannelId(channel.getId())
                    .stream()
                    .map(m -> m.getCreatedAt())
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            List<UUID> userIds = channel.isPrivateChannel()
                    ? new ArrayList<>(channel.getMembers().keySet()) : List.of();

            channelResponseDtoList.add(new ChannelResponseDto(
                    channel.getId(),
                    channel.isPrivateChannel() ? null : channel.getChannelName(),
                    channel.isPrivateChannel() ? null : channel.getChannelDescription(),
                    channel.getSlowModeSeconds(),
                    lastMessage,
                    channel.isPrivateChannel(),
                    userIds,
                    channel.getType()
            ));
        }
        return channelResponseDtoList;
    }

    @Override
    public List<ChannelResponseDto> getAllByUserId(UUID userId) {
        List<Channel> all = channelRepository.findAll();
        List<ChannelResponseDto> responseDto = new ArrayList<>();
        for (Channel channel : all) {
            if(!channel.isPrivateChannel() || channel.getMembers().containsKey(userId)) {
                Instant lastMessage = messageRepository.findByChannelId(channel.getId())
                        .stream()
                        .map(m -> m.getCreatedAt())
                        .max(Comparator.naturalOrder())
                        .orElse(null);

                List<UUID> userIds = channel.isPrivateChannel()
                        ? new ArrayList<>(channel.getMembers().keySet()) : List.of();

                responseDto.add(new ChannelResponseDto(
                        channel.getId(),
                        channel.isPrivateChannel() ? null : channel.getChannelName(),
                        channel.isPrivateChannel() ? null : channel.getChannelDescription(),
                        channel.getSlowModeSeconds(),
                        lastMessage,
                        channel.isPrivateChannel(),
                        userIds,
                        channel.getType()
                ));
            }
        }
        return responseDto;
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateRequestDto channelUpdateRequestDto) {
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
        channelRepository.save(channel);

        Instant lastMessage = messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(m -> m.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);

        return new ChannelResponseDto(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelDescription(),
                channel.getSlowModeSeconds(),
                lastMessage,
                false,
                List.of(),
                channel.getType()
        );
    }

    @Override
    public boolean delete(UUID channelId) {
        Objects.requireNonNull(channelId);
        List<Message> message = messageRepository.findByChannelId(channelId);
        for(Message m : message) {
            if(m.getAttachmentIds() != null) {
                for(UUID attachmentId : m.getAttachmentIds()) {
                    binaryContentRepository.deleteById(attachmentId);
                }
            }
            messageRepository.deleteById(m.getId());
        }
        readStatusRepository.deleteAllByChannelId(channelId);

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
}
