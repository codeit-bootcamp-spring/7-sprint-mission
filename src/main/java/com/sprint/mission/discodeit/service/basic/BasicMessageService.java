package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.page.PageResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;
    private final MessageMapper messageMapper;
    private final UserStatusRepository userStatusRepository;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments) {
        UUID channelId = Objects.requireNonNull(messageCreateRequestDto.channelId());
        UUID authorId = Objects.requireNonNull(messageCreateRequestDto.authorId());
        String content = Objects.requireNonNull(messageCreateRequestDto.content());

        log.debug("Creating message: channelId = {}, authorId = {}, cotentLength = {} ",
                channelId, authorId, content.length());

        Channel channel =  channelRepository.findById(messageCreateRequestDto.channelId())
                .orElseThrow(()-> new NoSuchElementException("Channel not found"));

        User user = userRepository.findById(messageCreateRequestDto.authorId()).orElseThrow(()
                -> new NoSuchElementException("User not found"));

        boolean exist = readStatusRepository.existsByUserIdAndChannelId(authorId, channelId);

        if(channel.isPrivateChannel() && !exist) {
            log.warn("Create message rejected: not a member of a private channel. channelId = {}, authorId = {}",
                    channelId, authorId);
            throw new NoSuchElementException("Member not found");
        }

        if(!channel.isPrivateChannel() && !exist) {
            Instant readAt = channel.getCreatedAt() != null
                    ? channel.getCreatedAt() : Instant.now();
            readStatusRepository.save(new ReadStatus(user,channel, readAt));
            log.debug("ReadStatus created for public channel. channelId = {}, userId = {}",
                    channelId, authorId);
        }


        int slow = channel.getSlowModeSeconds();
        if ( slow < 0 ) {
            log.warn("Create message rejected: slow mode = {} ", slow);
            throw new IllegalStateException("SlowModeSeconds must be >= 0");
        }

        if ( slow > 0 ) {
            Instant timeNow = Instant.now();

            Optional<Instant> last = messageRepository
                    .findByChannelIdAndAuthorId(messageCreateRequestDto.channelId(),messageCreateRequestDto.authorId())
                    .stream()
                    .filter(m -> !m.isDeleted())
                    .map(m -> m.getCreatedAt())
                    .max(Comparator.naturalOrder());

            if (last.isPresent()) {
                Instant nextAllowed = last.get().plusSeconds(slow);
                if(timeNow.isBefore(nextAllowed)) {
                    long waitSeconds = Duration.between(timeNow, nextAllowed).toSeconds();
                    log.warn("Create message rejected: slow mode = {} ", waitSeconds);
                    throw new IllegalStateException("SlowMode wait : " + waitSeconds + "s");
                }
            }
        }

        List<BinaryContent> attachmentList = new ArrayList<>();
        if(attachments != null) {
            log.debug("Create message attachment, channelId = {}, authorId = {}", channelId, authorId);
            for(MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    BinaryContentCreateRequestDto dto = BinaryContentCreateRequestDto.from(file);
                    BinaryContentResponseDto save = binaryContentService.create(dto);

                    BinaryContent attachment = binaryContentRepository.getReferenceById(save.id());
                    attachmentList.add(attachment);
                }
            }
        }

        Message message = new Message(
                content,
                user,
                channel,
                attachmentList
        );

        Message save = messageRepository.save(message);
        log.info("메세지가 생성되었습니다.");

        return messageMapper.toDto(save, isAuthorOnline(user));
    }

    @Override
    public MessageResponseDto get(UUID messageId) {
        log.debug("Getting message by id : messageId = {}", messageId);
        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        return messageMapper.toDto(message, isAuthorOnline(message.getAuthor()));
    }

    @Override
    public List<MessageResponseDto> getAll() {
        log.debug("Getting all messages");
        List<Message> messages = messageRepository.findAll();
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @Override
    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        log.debug("Getting all messages by channel id: channelId = {}", channelId);
        List<Message> messages = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @Transactional
    @Override
    public MessageResponseDto update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto) {
        log.debug("Updating message. messageId = {}, hasNewContent = {}",
                messageId, messageUpdateRequestDto.newContent() != null);

        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        if(messageUpdateRequestDto.newContent() != null) {
            message.setContent(messageUpdateRequestDto.newContent());
        }

        Message save = messageRepository.save(message);
        log.info("메세지가 수정되었습니다.");
        return messageMapper.toDto(save, isAuthorOnline(message.getAuthor()));
    }

    @Transactional
    @Override
    public boolean delete(UUID messageId) {
        log.debug("Deleting message: messageId = {}", messageId);
        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        messageRepository.delete(message);
        log.info("메세지가 제거되었습니다.");
        return true;
    }

    // 특정 작성자별 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByAuthor(UUID authorId) {
        log.debug("Getting messages by author id: authorId = {}", authorId);
        List<Message> messages = messageRepository.findByAuthorId(Objects.requireNonNull(authorId));
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        log.debug("Getting messages by channel and author id: channelId = {}, authorId = {}",
                channelId, authorId);
        List<Message> messages = messageRepository.findByChannelIdAndAuthorId(
                Objects.requireNonNull(channelId),
                Objects.requireNonNull(authorId));
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    // 특정 키워드 검색
    @Override
    public List<MessageResponseDto> searchByKeyword(String keyword) {
        log.debug("Getting messages by keyword: keyword = {}", keyword);
        List<Message> messages = messageRepository
                .findByContentContainingIgnoreCase(Objects.requireNonNull(keyword));
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @Override
    public PageResponseDto<MessageResponseDto> getPageByChannelId(UUID channelId, Pageable pageable) {
        log.debug("Getting Page messages: channelId = {}, pageable = {}", channelId, pageable);
        Objects.requireNonNull(channelId);

        Slice<Message> slice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);

        Slice<MessageResponseDto> sliceDto = slice.map(message -> messageMapper.toDto(message, isAuthorOnline(message.getAuthor())));

        return pageResponseMapper.fromSlice(sliceDto);
    }

    private Map<UUID, Boolean> authorOnlineMap(List<Message> messages) {
        log.debug("Getting author online map");
        Set<UUID> authorIds = messages.stream()
                .map(m -> m.getAuthor())
                .filter(u -> u != null)
                .map(author -> author.getId())
                .collect(Collectors.toSet());

        if (authorIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<UserStatus> statuses = userStatusRepository.findAllByUserIdIn(authorIds);

        return statuses.stream()
                .collect(Collectors.toMap(
                        status -> status.getUser().getId(),
                        us -> us.isOnlineNow()
                ));
    }

    private boolean isAuthorOnline(User author) {
        log.debug("Checking if author is online");
        if (author == null) {
            return false;
        }
        return userStatusRepository.findByUserId(author.getId())
                .map(userStatus -> userStatus.isOnlineNow())
                .orElse(false);
    }
}
