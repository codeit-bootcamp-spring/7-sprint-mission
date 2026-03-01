package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.channel.InvalidChannelException;
import com.sprint.mission.discodeit.common.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.security.SessionOnlineChecker;
import com.sprint.mission.discodeit.common.security.jwt.JwtOnlineChecker;
import com.sprint.mission.discodeit.dto.request.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final SessionOnlineChecker sessionOnlineChecker;
    private final JwtOnlineChecker jwtOnlineChecker;

    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        ChannelType channelType =
                channelCreateRequestDto.type() == null ?
                        ChannelType.PUBLIC : channelCreateRequestDto.type();

        log.debug("Creating Public Channel. name = {}, type = {}, slowMode = {}, hasDescription = {}",
                channelCreateRequestDto.name(), channelType, slowMode,
                channelCreateRequestDto.description() != null);


        Channel channel = new Channel(
                channelType,
                channelCreateRequestDto.name(),
                false,
                slowMode,
                channelCreateRequestDto.description()
        );

        Channel save = channelRepository.save(channel);
        List<User> userList = participants(save);

        log.info("채널이 생성되었습니다. channelId = {}", save.getId());
        return channelMapper.toDto(save,lastMessageAt(save),userList,userOnlineMap(userList));
    }

    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @Transactional
    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto channelCreateRequestDto) {
        int slowMode = channelCreateRequestDto.slowModeSeconds() == null
                ? 0 :  channelCreateRequestDto.slowModeSeconds();

        ChannelType channelType =
                channelCreateRequestDto.type() == null ?
                        ChannelType.PRIVATE : channelCreateRequestDto.type();

        log.debug("Creating Private Channel: type = {}, slowMode = {}", channelType, slowMode);

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
                        .orElseThrow(() -> new UserNotFoundException(id));

                readStatusRepository.save(new ReadStatus(user, save, save.getCreatedAt()));
            }
        }

        List<User> userList = participants(save);

        log.info("1:1 채널이 생성되었습니다.  channelId = {}", save.getId());
        return channelMapper.toDto(save,lastMessageAt(save), userList, userOnlineMap(userList));
    }

    @Override
    public ChannelResponseDto get(UUID channelId) {
        log.debug("Getting Channel: channelId = {}", channelId);
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        List<User> userList = participants(channel);
        return channelMapper.toDto(channel,lastMessageAt(channel), userList, userOnlineMap(userList));
    }

    @Override
    public List<ChannelResponseDto> getAll() {
        log.debug("Getting All Channels");
        return channelRepository.findAll().stream()
                .map(channel -> channelMapper.toDto(channel,
                        lastMessageAt(channel),
                        participants(channel),
                        userOnlineMap(participants(channel))))
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "userChannelsCache", key = "#userId")
    public List<ChannelResponseDto> getAllByUserId(UUID userId) {
        log.debug("Getting All By User Id: userId = {}", userId);
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
                .map(channel -> channelMapper.toDto(channel,
                        lastMessageAt(channel),
                        participants(channel),
                        userOnlineMap(participants(channel))))
                .toList();
    }

    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public ChannelResponseDto update(UUID channelId, ChannelUpdateRequestDto channelUpdateRequestDto) {
        log.debug("Updating Channel: channelId = {}, hasName = {}, hasDescription = {}, hasSlowMode = {}",
                channelId, channelUpdateRequestDto.newName() != null,
                channelUpdateRequestDto.newDescription() != null,
                channelUpdateRequestDto.slowModeSeconds() != null);
        Channel channel = channelRepository.findById(Objects.requireNonNull(channelId))
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        if(channel.isPrivateChannel()) {
            log.warn("Update channel rejected: Private channel. channelId = {}", channelId);
            throw new PrivateChannelUpdateException(channelId);
        }

        if(channelUpdateRequestDto.newName() != null) {
            channel.rename(channelUpdateRequestDto.newName());
            log.debug("Updating Channel name {} ", channel.getName());
        }

        if(channelUpdateRequestDto.newDescription() != null) {
            channel.changeChannelDescription(channelUpdateRequestDto.newDescription());
            log.debug("Updating Channel description {} ", channel.getDescription());
        }

        if(channelUpdateRequestDto.slowModeSeconds() != null) {
            channel.changeSlowModeSeconds(channelUpdateRequestDto.slowModeSeconds());
            log.debug("Updating Channel description {} ", channel.getDescription());
        }

        Channel save = channelRepository.save(channel);
        List<User> userList = participants(channel);

        log.info("채널이 수정되었습니다. channelId = {}", save.getId());
        return channelMapper.toDto(save,lastMessageAt(save),userList,userOnlineMap(userList));
    }

    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @PreAuthorize("hasRole('CHANNEL_MANAGER')")
    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (channelId == null) {
            throw new InvalidChannelException("channelId is null");
        }
        log.debug("Deleting Channel: channelId = {}", channelId);
        if(!channelRepository.existsById(channelId)) {
            throw new ChannelNotFoundException(channelId);
        }
        // 메세지 제거 + 첨부 파일 제거
        List<Message> message = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        messageRepository.deleteAll(message);

        // status 제거
        readStatusRepository.deleteAllByChannelId(channelId);

        // 채널 제거
        channelRepository.deleteById(channelId);
        log.info("채널이 제거되었습니다. channelId = {}", channelId);
    }


    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @Transactional
    @Override
    public boolean join(UUID channelId, UUID userId) {
        log.debug("Joining Channel: channelId = {}, userId = {}", channelId, userId);
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if(readStatusRepository.findByUserIdAndChannelId(userId, channelId).isPresent()) {
             return false;
         }

         readStatusRepository.save(new ReadStatus(user, channel, channel.getCreatedAt()));
        log.info("유저가 채널에 입장하였습니다.  channelId = {}", channelId);
        return true;
    }

    @CacheEvict(cacheNames = "userChannelsCache", allEntries = true)
    @Transactional
    @Override
    public boolean leave(UUID channelId, UUID userId) {
        log.debug("Leaving Channel: channelId = {}, userId = {}", channelId, userId);
        return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .map(readStatus -> {
                    readStatusRepository.delete(readStatus);
                    log.info("유저가 채널을 떠났습니다.");
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    @Override
    public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {
        log.debug("Setting Slow Mode Seconds: channelId = {}, slowMode = {}",channelId, slowModeSeconds);
        if(slowModeSeconds < 0) {
            log.warn("Slow mode rejected: channelId = {}, slowModeSeconds = {}", channelId, slowModeSeconds);
            throw new  IllegalStateException("slowModeSeconds cannot be negative");
        }
        Channel channel = channelRepository.findById(channelId)
                        .orElseThrow(() -> new ChannelNotFoundException(channelId));

        channel.changeSlowModeSeconds(slowModeSeconds);
        channelRepository.save(channel);

        log.info("슬로우모드 설정 완료");
    }

    private Instant lastMessageAt(Channel channel) {
        return messageRepository.findByChannelId(channel.getId())
                .stream()
                .map(message -> message.getCreatedAt())
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    private List<User> participants(Channel channel) {
        return readStatusRepository.findAllByChannelId(channel.getId())
                .stream()
                .map(readStatus -> readStatus.getUser())
                .filter(user -> user != null)
                .distinct()
                .toList();
    }

    private Map<UUID, Boolean> userOnlineMap(List<User> participants) {
        if (participants == null || participants.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<UUID> userIds = participants.stream()
                .map(user -> user.getId())
                .collect(Collectors.toSet());

        return jwtOnlineChecker.onlineMap(userIds);
    }
}
