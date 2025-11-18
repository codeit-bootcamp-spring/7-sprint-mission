package com.sprint.mission.discodeit.data;

import com.sprint.mission.discodeit.common.constants.DataPath;
import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.channel.request.ChannelMemberRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseV2;
import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseV2;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.enums.ReceiverType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "dummy-data", name = "enabled", havingValue = "true")
public class DummyDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DummyDataSeeder.class);

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;
    private final ReadStatusService readStatusService;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final DataPath dataPath;

    @Value("${dummy-data.skip-if-present:true}")
    private boolean skipIfPresent;

    @Value("${dummy-data.clean-before-seed:false}")
    private boolean cleanBeforeSeed;

    @Override
    public void run(String... args) {
        if (cleanBeforeSeed) {
            log.info("[DummyDataSeeder] Cleaning persisted files and repositories");
            cleanPersistedFiles();
            clearRepositories();
            log.info("[DummyDataSeeder] Cleanup completed");
        }

        if (skipIfPresent && !userRepository.findAll().isEmpty()) {
            log.info("[DummyDataSeeder] Existing data detected. Skip seeding.");
            return;
        }

        log.info("[DummyDataSeeder] Start seeding dummy data");
        Map<String, User> userMap = seedUsers();
        Map<String, BinaryContent> profileImages = seedProfileImages(userMap);
        updateUserProfiles(userMap, profileImages);
        Map<String, Channel> channelMap = seedChannels(userMap);
        seedMessages(userMap, channelMap);
        seedReadStatuses(userMap, channelMap);
        seedUserStatuses(userMap);
        log.info("[DummyDataSeeder] Seeding done - users={}, userStatuses={}, channels={}, messages={}, readStatuses={}, binaryContents={}",
                userRepository.findAll().size(),
                userStatusRepository.findAll().size(),
                channelRepository.findAll().size(),
                messageRepository.findAll().size(),
                readStatusRepository.findAll().size(),
                binaryContentRepository.findAll().size());
    }

    private void cleanPersistedFiles() {
        List<String> files = List.of(
                dataPath.CHANNEL_FILE_PATH(),
                dataPath.USER_FILE_PATH(),
                dataPath.MESSAGE_FILE_PATH(),
                dataPath.BINARY_CONTENT_FILE_PATH(),
                dataPath.READ_STATUS_FILE_PATH(),
                dataPath.USER_STATUS_FILE_PATH()
        );

        files.forEach(path -> {
            try {
                Files.deleteIfExists(Path.of(path));
            } catch (IOException e) {
                log.warn("[DummyDataSeeder] Failed to delete file {}", path, e);
            }
        });
    }

    private void clearRepositories() {
        messageRepository.findAll().forEach(messageRepository::delete);
        readStatusRepository.findAll().forEach(readStatusRepository::delete);
        channelRepository.findAll().forEach(channel -> channelRepository.deleteById(channel.getUuid()));
        userStatusRepository.findAll().forEach(status -> userStatusRepository.deleteById(status.getUuid()));
        binaryContentRepository.findAll().forEach(binaryContentRepository::delete);
        userRepository.findAll().forEach(user -> userRepository.delete(user.getUuid()));
    }

    private Map<String, User> seedUsers() {
        Map<String, User> userMap = new LinkedHashMap<>();
        Instant now = Instant.now();
        for (UserSeed seed : USER_SEEDS) {
            // 고정된 UUID 생성 (userId 기반)
            UUID fixedUuid = UUID.nameUUIDFromBytes(("user-" + seed.userId()).getBytes());
            User user = User.fromDto(
                    fixedUuid,
                    now,
                    now,
                    seed.userId(),
                    seed.password(),
                    seed.email(),
                    seed.displayName(),
                    null,  // bio
                    com.sprint.mission.discodeit.enums.OnlineStatus.OFFLINE,
                    null   // profileImage (나중에 설정)
            );
            userRepository.save(user);
            userMap.put(seed.userId(), user);
            log.debug("[DummyDataSeeder] Created user: {} with UUID: {}", seed.userId(), fixedUuid);
        }
        return userMap;
    }

    private Map<String, BinaryContent> seedProfileImages(Map<String, User> userMap) {
        Map<String, BinaryContent> profileImageMap = new LinkedHashMap<>();
        Instant now = Instant.now();
        for (ProfileImageSeed seed : PROFILE_IMAGE_SEEDS) {
            // 고정된 UUID 생성
            UUID fixedUuid = UUID.nameUUIDFromBytes(("profile-" + seed.key()).getBytes());
            BinaryContent binaryContent = BinaryContent.fromDto(
                    fixedUuid,
                    now,
                    now,
                    now,
                    seed.fileUrl()
            );
            binaryContentRepository.save(binaryContent);
            profileImageMap.put(seed.key(), binaryContent);
            log.debug("[DummyDataSeeder] Created profile image: {} with UUID: {}", seed.key(), fixedUuid);
        }
        return profileImageMap;
    }

    private void updateUserProfiles(Map<String, User> userMap, Map<String, BinaryContent> profileImages) {
        for (UserSeed seed : USER_SEEDS) {
            if (seed.profileImageKey() != null) {
                User user = userMap.get(seed.userId());
                BinaryContent profileImage = profileImages.get(seed.profileImageKey());
                if (user != null && profileImage != null) {
                    user.setProfileImage(profileImage);
                    userRepository.update(user);
                    log.debug("[DummyDataSeeder] Updated profile image for user: {}", seed.userId());
                }
            }
        }
    }

    private Map<String, Channel> seedChannels(Map<String, User> userMap) {
        Map<String, Channel> channelMap = new LinkedHashMap<>();
        Instant now = Instant.now();
        for (ChannelSeed seed : CHANNEL_SEEDS) {
            // 고정된 UUID 생성
            UUID fixedUuid = UUID.nameUUIDFromBytes(("channel-" + seed.key()).getBytes());

            // 멤버 수집
            java.util.Set<User> members = new java.util.HashSet<>();
            for (String userId : seed.memberUserIds()) {
                User member = requireUser(userMap, userId);
                members.add(member);
            }

            // Channel.fromDto로 UUID를 지정하여 생성
            Channel channel = Channel.fromDto(
                    fixedUuid,
                    now,
                    now,
                    seed.name(),
                    seed.description(),
                    com.sprint.mission.discodeit.enums.ChannelScope.PUBLIC,
                    null,  // type
                    new java.util.HashSet<>(),  // moderators
                    members
            );

            channelRepository.save(channel);
            channelMap.put(seed.key(), channel);
            log.debug("[DummyDataSeeder] Created channel: {} with UUID: {} and {} members",
                    seed.name(), fixedUuid, seed.memberUserIds().size());
        }
        return channelMap;
    }

    private void seedMessages(Map<String, User> userMap, Map<String, Channel> channelMap) {
        Instant now = Instant.now();
        int messageIndex = 0;
        for (MessageSeed seed : MESSAGE_SEEDS) {
            User sender = requireUser(userMap, seed.senderId());
            Receivable receiver;

            if (seed.type() == ReceiverType.USER) {
                receiver = requireUser(userMap, seed.receiverKey());
            } else {
                receiver = requireChannel(channelMap, seed.receiverKey());
            }

            // 고정된 UUID 생성
            UUID fixedUuid = UUID.nameUUIDFromBytes(("message-" + messageIndex).getBytes());

            // 첨부파일 처리
            List<BinaryContent> attachments = new java.util.ArrayList<>();
            if (seed.attachments() != null && !seed.attachments().isEmpty()) {
                int attachIndex = 0;
                for (String fileUrl : seed.attachments()) {
                    UUID attachUuid = UUID.nameUUIDFromBytes(("attachment-" + messageIndex + "-" + attachIndex).getBytes());
                    BinaryContent attachment = BinaryContent.fromDto(attachUuid, now, now, now, fileUrl);
                    binaryContentRepository.save(attachment);
                    attachments.add(attachment);
                    attachIndex++;
                }
            }

            // Message.fromDto로 UUID 지정하여 생성
            Message message = Message.fromDto(
                    fixedUuid,
                    now.minusSeconds(MESSAGE_SEEDS.size() - messageIndex),  // 오래된 메시지부터 시간 설정
                    now.minusSeconds(MESSAGE_SEEDS.size() - messageIndex),
                    sender,
                    seed.type(),
                    receiver,
                    seed.content(),
                    attachments
            );

            messageRepository.save(message);
            log.debug("[DummyDataSeeder] Created message from {} to {} with UUID: {}",
                    seed.senderId(), seed.receiverKey(), fixedUuid);
            messageIndex++;
        }
    }

    private void seedReadStatuses(Map<String, User> userMap, Map<String, Channel> channelMap) {
        Instant now = Instant.now();
        for (ChannelSeed seed : CHANNEL_SEEDS) {
            Channel channel = requireChannel(channelMap, seed.key());
            List<String> uniqueMembers = seed.memberUserIds().stream()
                    .distinct()
                    .toList();

            for (int idx = 0; idx < uniqueMembers.size(); idx++) {
                User user = requireUser(userMap, uniqueMembers.get(idx));
                Instant lastReadAt = now.minusSeconds((long) (idx + 1) * 600L);

                // 고정된 UUID 생성
                UUID fixedUuid = UUID.nameUUIDFromBytes(("readstatus-" + user.getUserId() + "-" + seed.key()).getBytes());

                ReadStatus readStatus = ReadStatus.fromDto(
                        fixedUuid,
                        user,
                        channel,
                        now,
                        now,
                        lastReadAt
                );
                readStatusRepository.save(readStatus);

                log.debug("[DummyDataSeeder] Created read status for user {} in channel {} with UUID: {}",
                        user.getUserId(), channel.getChannelName(), fixedUuid);
            }
        }
    }

    private void seedUserStatuses(Map<String, User> userMap) {
        Instant now = Instant.now();
        for (UserStatusSeed seed : USER_STATUS_SEEDS) {
            User user = requireUser(userMap, seed.userId());
            Instant lastActive = now.minusSeconds(seed.lastActiveSecondsAgo());

            // 고정된 UUID 생성
            UUID fixedUuid = UUID.nameUUIDFromBytes(("userstatus-" + seed.userId()).getBytes());

            UserStatus userStatus = UserStatus.fromDto(
                    fixedUuid,
                    now,
                    now,
                    user,
                    lastActive,
                    com.sprint.mission.discodeit.enums.OnlineStatus.OFFLINE
            );
            userStatusRepository.save(userStatus);

            log.debug("[DummyDataSeeder] Created user status for user: {} with UUID: {}", seed.userId(), fixedUuid);
        }
    }

    private User requireUser(Map<String, User> userMap, String userId) {
        return Objects.requireNonNull(userMap.get(userId),
                () -> "Undefined user id: " + userId);
    }

    private Channel requireChannel(Map<String, Channel> channelMap, String key) {
        return Objects.requireNonNull(channelMap.get(key),
                () -> "Undefined channel key: " + key);
    }

    private record UserSeed(String userId, String password, String email, String displayName, String profileImageKey) {
    }

    private record ChannelSeed(String key, String name, String description, List<String> memberUserIds) {
    }

    private record MessageSeed(String senderId, ReceiverType type, String receiverKey, String content, List<String> attachments) {
    }

    private record ProfileImageSeed(String key, String ownerUserId, String fileUrl) {
    }

    private record UserStatusSeed(String userId, long lastActiveSecondsAgo) {
    }

    private static final List<UserSeed> USER_SEEDS = List.of(
            new UserSeed("happy", "pancake", "happy@example.com", "HeeYeon", "profile-happy"),
            new UserSeed("jung123", "greeny", "jung@example.com", "Garden", "profile-jung"),
            new UserSeed("npnp9671", "something", "npnp@example.com", "Drawing", null),
            new UserSeed("alice", "pass1234", "alice@example.com", "Alice", "profile-alice"),
            new UserSeed("bob1234", "pass1234", "bob@example.com", "Bob", null),
            new UserSeed("charlie", "pass1234", "charlie@example.com", "Charlie", "profile-charlie"),
            new UserSeed("david12", "pass1234", "david@example.com", "David", null),
            new UserSeed("emma123", "pass1234", "emma@example.com", "Emma", "profile-emma")
    );

    private static final List<ChannelSeed> CHANNEL_SEEDS = List.of(
            new ChannelSeed("game", "Game Night", "Casual gaming meetups",
                    List.of("happy", "alice", "bob1234", "charlie")),
            new ChannelSeed("photo", "Photo Studio", "Share your latest shots",
                    List.of("jung123", "npnp9671", "emma123", "alice")),
            new ChannelSeed("study", "Study Hall", "Co-study sessions and Q&A",
                    List.of("alice", "bob1234", "charlie", "david12")),
            new ChannelSeed("music", "Music Lounge", "Track recommendations",
                    List.of("bob1234", "alice", "happy")),
            new ChannelSeed("general", "General Chat", "Casual talks and updates",
                    List.of("happy", "alice", "jung123", "npnp9671", "bob1234", "charlie", "david12", "emma123"))
    );

    private static final List<ProfileImageSeed> PROFILE_IMAGE_SEEDS = List.of(
            new ProfileImageSeed("profile-happy", "happy", "https://cdn.example.com/profile/happy.png"),
            new ProfileImageSeed("profile-jung", "jung123", "https://cdn.example.com/profile/jung.png"),
            new ProfileImageSeed("profile-alice", "alice", "https://cdn.example.com/profile/alice.png"),
            new ProfileImageSeed("profile-charlie", "charlie", "https://cdn.example.com/profile/charlie.png"),
            new ProfileImageSeed("profile-emma", "emma123", "https://cdn.example.com/profile/emma.png")
    );

    private static final List<UserStatusSeed> USER_STATUS_SEEDS = List.of(
            new UserStatusSeed("happy", 60),
            new UserStatusSeed("jung123", 120),
            new UserStatusSeed("npnp9671", 600),
            new UserStatusSeed("alice", 30),
            new UserStatusSeed("bob1234", 300),
            new UserStatusSeed("charlie", 900),
            new UserStatusSeed("david12", 1800),
            new UserStatusSeed("emma123", 45)
    );

    private static final List<MessageSeed> MESSAGE_SEEDS = List.of(
            new MessageSeed("alice", ReceiverType.USER, "bob1234",
                    "Hey Bob, can you join tonight's session?", List.of("https://cdn.example.com/files/agenda.pdf")),
            new MessageSeed("bob1234", ReceiverType.USER, "alice",
                    "Sure thing, I will upload the slides soon.", List.of("https://cdn.example.com/files/diagram.png")),
            new MessageSeed("happy", ReceiverType.CHANNEL, "general",
                    "Good morning team!", List.of()),
            new MessageSeed("alice", ReceiverType.CHANNEL, "general",
                    "Let's have a great day!", List.of()),
            new MessageSeed("charlie", ReceiverType.CHANNEL, "general",
                    "Weather looks perfect for a walk.", List.of()),
            new MessageSeed("emma123", ReceiverType.CHANNEL, "general",
                    "Agreed! I might go out later.", List.of()),
            new MessageSeed("happy", ReceiverType.CHANNEL, "game",
                    "Anyone up for a quick match tonight?", List.of()),
            new MessageSeed("bob1234", ReceiverType.CHANNEL, "game",
                    "Count me in at 9 PM.", List.of()),
            new MessageSeed("jung123", ReceiverType.CHANNEL, "photo",
                    "Sharing a new landscape shot!", List.of()),
            new MessageSeed("npnp9671", ReceiverType.CHANNEL, "photo",
                    "The lighting is fantastic?what lens did you use?", List.of()),
            new MessageSeed("alice", ReceiverType.CHANNEL, "study",
                    "Shall we tackle HTTP caching this week?", List.of()),
            new MessageSeed("david12", ReceiverType.CHANNEL, "study",
                    "I'm in. Preparing some notes now.", List.of()),
            new MessageSeed("bob1234", ReceiverType.CHANNEL, "music",
                    "Latest chillhop playlist drop!", List.of()),
            new MessageSeed("happy", ReceiverType.CHANNEL, "music",
                    "Thanks! Adding it to my queue.", List.of()),
            new MessageSeed("emma123", ReceiverType.USER, "happy",
                    "Lunch walk today?", List.of("https://cdn.example.com/files/park-route.jpg")),
            new MessageSeed("happy", ReceiverType.USER, "emma123",
                    "Sounds great. 12:30 in the lobby?", List.of()),
            new MessageSeed("charlie", ReceiverType.CHANNEL, "study",
                    "Let's finalize the presentation order by tomorrow.", List.of()),
            new MessageSeed("alice", ReceiverType.CHANNEL, "study",
                    "Good idea. Please drop your availability.", List.of())
    );
}
