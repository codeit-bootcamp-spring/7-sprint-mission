package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseV2;
import com.sprint.mission.discodeit.dto.channel.response.DetailedChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.common.exceptions.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public ChannelResponseV2 createPublicChannel(PublicChannelCreateRequest dto) {
        Channel channel = Channel.newPublicChannel(
                dto.name(),
                dto.description()
        );
        channelRepository.save(channel);
        return ChannelResponseV2.toDto(channel);
    }

    @Override
    public ChannelResponseV2 createPrivateChannel(PrivateChannelCreateRequest dto) {
        Channel channel = Channel.newPrivateChannel(
                dto.participantIds().stream()
                        .map(u -> userRepository.find(u)
                                .orElseThrow(() -> new UserNotFoundException(u)))
                        .collect(Collectors.toSet())
        );
        return ChannelResponseV2.toDto(channel);
    }

    @Override
    public ChannelResponseV2 update(UUID channelId, ChannelUpdateRequest dto) {
        Channel channel = channelRepository.find(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));
        if (channel.getScope() == ChannelScope.PRIVATE) {
            throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
        }

        if (dto.newName() != null) {
            channel.setChannelName(dto.newName());
        }

        if (dto.newDescription() != null) {
            channel.setDescription(dto.newDescription());
        }

        channelRepository.update(channel);
        return ChannelResponseV2.toDto(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.find(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));
        channelRepository.deleteById(channelId);
        messageRepository.deleteAllByReceiver(channel);
        readStatusRepository.deleteAllByChannel(channel);
    }

    @Override
    public List<ChannelResponseV2> getAll() {
        return channelRepository.findAll().stream()
                .map(ChannelResponseV2::toDto)
                .toList();
    }

    @Override
    public List<DetailedChannelResponse> getAllVisibleByUser(UUID userUuid) {
        User user = userRepository.find(userUuid)
                .orElseThrow(() -> new UserNotFoundException(userUuid));
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(c -> {
                    Optional<Message> last = messageRepository.findLast(c);
                    Instant lastMessageAt = null;
                    if (last.isPresent()) {
                        lastMessageAt = last.get().getCreatedAt();
                    }
                    return DetailedChannelResponse.toDto(c, lastMessageAt);
                })
                .toList();
    }

    @Override
    public ChannelResponseV2 getById(UUID uuid) {
        return ChannelResponseV2.toDto(channelRepository.find(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)));
    }

    @Override
    public List<UserResponse> getAllMembers(UUID uuid) {
        return channelRepository.find(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)).getMembers().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> getAllModerators(UUID uuid) {
        return channelRepository.find(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)).getModerators().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public void addMember(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUuids(dto.userUuids());
        Channel channel = channelRepository.find(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        channel.addMembers(new HashSet<>(users));
        channelRepository.update(channel);
    }

    @Override
    public void addModerator(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUuids(dto.userUuids());
        channelRepository.find(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .addModerators(new HashSet<>(users));
    }

    @Override
    public void deleteMember(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUuids(dto.userUuids());
        channelRepository.find(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .deleteMember(new HashSet<>(users));
    }

    @Override
    public void deleteModerator(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUuids(dto.userUuids());
        channelRepository.find(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .deleteModerator(new HashSet<>(users));
    }
}
