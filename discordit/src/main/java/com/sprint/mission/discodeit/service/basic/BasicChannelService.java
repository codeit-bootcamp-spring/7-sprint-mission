package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.exceptions.ChannelAlreadyExistsException;
import com.sprint.mission.discodeit.exceptions.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.MessageNotFoundException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
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
    public ChannelResponse createPublicChannel(PublicChannelCreateRequest dto) {
        Channel channel = new Channel(dto.channelName(),
                dto.scope(),
                dto.type(),
                dto.moderatorIds().stream()
                        .map(userId ->
                                userRepository.findByUserId(userId)
                                        .orElseThrow(() -> new UserNotFoundException(userId)))
                        .collect(Collectors.toSet()));
        channel.setDescription(dto.description());
        channelRepository.save(channel);
        return toDto(channel);
    }

    @Override
    public ChannelResponse createPrivateChannel(PrivateChannelCreateRequest dto) {
        Channel channel = new Channel("",
                dto.scope(),
                dto.type(),
                dto.moderatorIds().stream()
                        .map(userId ->
                                userRepository.findByUserId(userId)
                                        .orElseThrow(() -> new UserNotFoundException(userId)))
                        .collect(Collectors.toSet()));
        dto.memberIds().forEach(userId -> {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));
            channel.addMember(user);
            ReadStatus r = new ReadStatus(user, channel);
            readStatusRepository.save(r);
        });
        return toDto(channel);
    }

    @Override
    public ChannelResponse update(ChannelUpdateRequest dto) {
        Channel channel = channelRepository.findById(dto.id())
                .orElseThrow(() -> new ChannelNotFoundException(dto.id()));
        if (channel.getScope() == ChannelScope.PRIVATE) {
            throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
        }

        if (dto.channelName() != null) {
            channel.setChannelName(dto.channelName());
        }

        if (dto.description() != null) {
            channel.setDescription(dto.description());
        }

        channelRepository.update(channel);
        return toDto(channel);
    }

    @Override
    public void delete(ChannelDeleteRequest dto) {
        Channel channel = channelRepository.findById(dto.id())
                .orElseThrow(() -> new ChannelNotFoundException(dto.id()));
        channelRepository.deleteById(dto.id());
        messageRepository.deleteAllByReceiver(channel);
        readStatusRepository.deleteAllByChannel(channel);
    }

    @Override
    public List<ChannelResponse> getAll() {
        return channelRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ChannelResponse> getAllVisibleByUserId(GetVisibleChannelRequest dto) {
        User user = userRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));
        return Stream.concat(channelRepository.findAllPublic().stream(), channelRepository.findAllPrivateByUser(user).stream())
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelResponse getById(UUID uuid) {
        return toDto(channelRepository.findById(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)));
    }

    @Override
    public List<UserResponse> getAllMembers(UUID uuid) {
        return channelRepository.findById(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)).getMembers().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> getAllModerators(UUID uuid) {
        return channelRepository.findById(uuid)
                .orElseThrow(() -> new ChannelNotFoundException(uuid)).getModerators().stream()
                .map(UserResponse::toDto)
                .toList();
    }

    @Override
    public void addMember(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUserIds(dto.userIds());
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()));
        channel.addMembers(new HashSet<>(users));
        channelRepository.update(channel);
    }

    @Override
    public void addModerator(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUserIds(dto.userIds());
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .addModerators(new HashSet<>(users));
    }

    @Override
    public void deleteMember(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUserIds(dto.userIds());
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .deleteMember(new HashSet<>(users));
    }

    @Override
    public void deleteModerator(ChannelMemberRequest dto) {
        List<User> users = userRepository.findAllByUserIds(dto.userIds());
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new ChannelNotFoundException(dto.channelId()))
                .deleteModerator(new HashSet<>(users));
    }

    private ChannelResponse toDto(Channel channel) {
        return ChannelResponse.toDto(channel, messageRepository.findLast(channel)
                .orElseThrow(() -> new MessageNotFoundException("메세지 데이터가 존재하지 않습니다."))
                .getCreatedAt());
    }

    /*
    TODO: 예외처리 물어보기
    private User findUserAndHandleConflict(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Channel findAndHandleConflict(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new ChannelNotFoundException(id));
    }
     */

}