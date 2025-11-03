package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
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
    public UUID createPublicChannel(PublicChannelCreateRequest dto) {
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
    public UUID createPrivateChannel(PrivateChannelCreateRequest dto) {
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
    public void update(ChannelUpdateRequest dto) {
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
    public List<ChannelResponse> getAll() {
        return channelRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public ChannelResponse getNth(int index) {
        return toDto(channelRepository.findAll().get(index));
    }

    @Override
    public List<ChannelResponse> getAllByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelResponse getById(UUID uuid) {
        return toDto(channelRepository.findById(uuid));
    }

    @Override
    public List<UserResponse> getAllMembers(UUID uuid) {
        return channelRepository.findById(uuid).getMembers().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> getAllModerators(UUID uuid) {
        return channelRepository.findById(uuid).getModerators().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public List<UUID> getAllIds() {
        return channelRepository.findAll().stream()
                .map(Channel::getUuid)
                .toList();
    }

    @Override
    public List<UUID> getAllIdsByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        return Stream.concat(
                channelRepository.findAllPublic().stream(),
                channelRepository.findAllPrivateByUser(user).stream()
        ).map(Channel::getUuid).toList();
    }

    @Override
    public String getDisplayName(UUID uuid) {
        return channelRepository.findById(uuid).getDisplayName();
    }

    @Override
    public com.sprint.mission.discodeit.enums.ChannelType getType(UUID uuid) {
        return channelRepository.findById(uuid).getType();
    }

    // 멤버 수정 (userId 기반)
    @Override
    public void addMember(UUID uuid, String userId) {
        User user = userRepository.findByUserId(userId);
        Channel channel = channelRepository.findById(uuid);
        channel.addMember(user);
    }

    @Override
    public void addModerator(UUID uuid, String userId) {
        User user = userRepository.findByUserId(userId);
        channelRepository.findById(uuid).addModerator(user);
    }

    @Override
    public void deleteMember(UUID uuid, String userId) {
        User user = userRepository.findByUserId(userId);
        channelRepository.findById(uuid).deleteMember(user);
    }

    @Override
    public void deleteModerator(UUID uuid, String userId) {
        User user = userRepository.findByUserId(userId);
        channelRepository.findById(uuid).deleteModerator(user);
    }

    private ChannelResponse toDto(Channel channel) {
        return ChannelResponse.toDto(channel, messageRepository.findLast(channel).getCreatedAt());
    }
}
