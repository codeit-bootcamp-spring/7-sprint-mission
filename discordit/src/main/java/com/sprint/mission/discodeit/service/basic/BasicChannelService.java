package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.ReadStatus;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Primary
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public UUID createPublicChannel(PublicChannelCreateRequestDto dto) {
        Channel channel = new Channel(dto.channelName(),
                dto.scope(),
                dto.type(),
                dto.moderatorIds().stream()
                        .map(userRepository::findByUserId)
                        .collect(Collectors.toSet()));
        channel.setDescription(dto.description());
        channelRepository.save(channel);
        return channel.getUuid();
    }

    @Override
    public UUID createPrivateChannel(PrivateChannelCreateRequestDto dto) {
        Channel channel = new Channel("",
                dto.scope(),
                dto.type(),
                dto.moderatorIds().stream()
                        .map(userRepository::findByUserId)
                        .collect(Collectors.toSet()));
        dto.memberIds().forEach(id -> {
            User user = userRepository.findByUserId(id);
            channel.addMember(user);
            ReadStatus r = new ReadStatus(user, channel);
            readStatusRepository.save(r);
        });
        channelRepository.save(channel);
        return channel.getUuid();
    }

    @Override
    public void update(ChannelUpdateRequestDto dto) {
        Channel channel = channelRepository.findById(dto.id());

        if (dto.channelName() != null) {
            channel.setChannelName(dto.channelName());
        }

        if (dto.description() != null) {
            channel.setDescription(dto.description());
        }

        channelRepository.update(channel);
    }

    @Override
    public void delete(UUID uuid) {
        Channel channel = channelRepository.findById(uuid);
        channelRepository.deleteById(uuid);
        messageRepository.deleteAllByReceiver(channel);
        readStatusRepository.deleteAllByChannel(channel);
    }

    @Override
    public List<ChannelResponseDto> getAll() {
        return channelRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public ChannelResponseDto getNth(int index) {
        return toDto(channelRepository.findAll().get(index));
    }

    @Override
    public List<ChannelResponseDto> getAllByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelResponseDto getById(UUID uuid) {
        return toDto(channelRepository.findById(uuid));
    }

    @Override
    public List<UserResponseDto> getAllMembers(UUID uuid) {
        return channelRepository.findById(uuid).getMembers().stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    @Override
    public List<UserResponseDto> getAllModerators(UUID uuid) {
        return channelRepository.findById(uuid).getModerators().stream()
                .map(UserResponseDto::toDto)
                .toList();
    }

    @Override
    public void addMember(UUID uuid, User user) {
        Channel channel = channelRepository.findById(uuid);
        channel.addMember(user);
    }

    @Override
    public void addModerator(UUID uuid, User user) {
        channelRepository.findById(uuid).addModerator(user);
    }

    @Override
    public void deleteMember(UUID uuid, User user) {
        channelRepository.findById(uuid).deleteMember(user);
    }

    @Override
    public void deleteModerator(UUID uuid, User user) {
        channelRepository.findById(uuid).deleteModerator(user);
    }

    private ChannelResponseDto toDto(Channel channel) {
        return ChannelResponseDto.toDto(channel, messageRepository.findLast(channel).getCreatedAt());
    }
}
