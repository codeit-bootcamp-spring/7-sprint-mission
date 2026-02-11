package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelMemberNotFoundException;
import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.channel.InvalidSlowModeException;
import com.sprint.mission.discodeit.common.exception.message.InvalidMessageRequestException;
import com.sprint.mission.discodeit.common.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.common.exception.message.SlowModeViolationException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.security.SessionOnlineChecker;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final PageResponseMapper pageResponseMapper;
    private final SessionOnlineChecker sessionOnlineChecker;

    @Transactional
    @Override
    public MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments) {
        if (messageCreateRequestDto == null ) {
            throw new InvalidMessageRequestException("messageCreateRequestDto is null");
        }
        if (messageCreateRequestDto.channelId() == null) {
            throw new InvalidMessageRequestException("channelId is null");
        }
        if (messageCreateRequestDto.authorId() == null) {
            throw new InvalidMessageRequestException("authorId is null");
        }
        if (messageCreateRequestDto.content() == null) {
            throw new InvalidMessageRequestException("content is null");
        }
        UUID channelId = messageCreateRequestDto.channelId();
        UUID authorId = messageCreateRequestDto.authorId();
        String content = messageCreateRequestDto.content();

        log.debug("Creating message: channelId = {}, authorId = {}, contentLength = {} ",
                channelId, authorId, content.length());

        Channel channel =  channelRepository.findById(messageCreateRequestDto.channelId())
                .orElseThrow(()-> new ChannelNotFoundException(channelId));

        User user = userRepository.findById(messageCreateRequestDto.authorId()).orElseThrow(()
                -> new UserNotFoundException(authorId));

        boolean exist = readStatusRepository.existsByUserIdAndChannelId(authorId, channelId);

        if(channel.isPrivateChannel() && !exist) {
            log.warn("Create message rejected: not a member of a private channel. channelId = {}, authorId = {}",
                    channelId, authorId);
            throw new ChannelMemberNotFoundException(channelId, authorId);
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
            throw new InvalidSlowModeException(channelId, slow);
        }

        if ( slow > 0 ) {
            Instant timeNow = Instant.now();

            Optional<Instant> last = messageRepository
                    .findByChannelIdAndAuthorId(channelId,authorId)
                    .stream()
                    .filter(m -> !m.isDeleted())
                    .map(m -> m.getCreatedAt())
                    .max(Comparator.naturalOrder());

            if (last.isPresent()) {
                Instant nextAllowed = last.get().plusSeconds(slow);
                if(timeNow.isBefore(nextAllowed)) {
                    long waitSeconds = Duration.between(timeNow, nextAllowed).toSeconds();
                    log.warn("SlowMode violation : waitSeconds = {} ", waitSeconds);
                    throw new SlowModeViolationException(channelId, authorId, waitSeconds);
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
        log.info("메세지가 생성되었습니다. messageId = {}", save.getId());

        return messageMapper.toDto(save, sessionOnlineChecker.isOnline(user.getId()));
    }

    @Override
    public MessageResponseDto get(UUID messageId) {
        if (messageId == null) {
            throw new InvalidMessageRequestException("messageId is null");
        }
        log.debug("Getting message by id : messageId = {}", messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        User author = message.getAuthor();
        boolean online = author != null && sessionOnlineChecker.isOnline(author.getId());

        return messageMapper.toDto(message, online);
    }

    @Override
    public List<MessageResponseDto> getAll() {
        log.debug("Getting all messages");
        List<Message> messages = messageRepository.findAll();
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @Override
    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        if (channelId == null) {
            throw new InvalidMessageRequestException("channelId is null");
        }
        log.debug("Getting all messages by channel id: channelId = {}", channelId);
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @PreAuthorize("@authz.isMessageAuthor(authentication, #messageId)")
    @Transactional
    @Override
    public MessageResponseDto update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto) {
        if (messageId == null) {
            throw new InvalidMessageRequestException("messageId is null");
        }
        if (messageUpdateRequestDto == null) {
            throw new InvalidMessageRequestException("messageUpdateRequestDto is null");
        }

        log.debug("Updating message. messageId = {}, hasNewContent = {}",
                messageId, messageUpdateRequestDto.newContent() != null);

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if(messageUpdateRequestDto.newContent() != null) {
            message.setContent(messageUpdateRequestDto.newContent());
        }

        Message save = messageRepository.save(message);
        User author = save.getAuthor();
        boolean online = author != null && sessionOnlineChecker.isOnline(author.getId());
        log.info("메세지가 수정되었습니다. messageId = {}", message.getId());
        return messageMapper.toDto(save, online);
    }

    @PreAuthorize("@authz.isMessageAuthor(authentication, #messageId)")
    @Transactional
    @Override
    public boolean delete(UUID messageId) {
        if (messageId == null) {
            throw new InvalidMessageRequestException("messageId is null");
        }
        log.debug("Deleting message: messageId = {}", messageId);
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));
        messageRepository.delete(message);
        log.info("메세지가 제거되었습니다. messageId = {}", message.getId());
        return true;
    }

    // 특정 작성자별 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByAuthor(UUID authorId) {
        if (authorId == null) {
            throw new InvalidMessageRequestException("authorId is null");
        }
        log.debug("Getting messages by author id: authorId = {}", authorId);
        List<Message> messages = messageRepository.findByAuthorId(authorId);
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        if (channelId == null) {
            throw new InvalidMessageRequestException("channelId is null");
        }
        if (authorId == null) {
            throw new InvalidMessageRequestException("authorId is null");
        }
        log.debug("Getting messages by channel and author id: channelId = {}, authorId = {}",
                channelId, authorId);
        List<Message> messages = messageRepository.findByChannelIdAndAuthorId(channelId, authorId);
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    // 특정 키워드 검색
    @Override
    public List<MessageResponseDto> searchByKeyword(String keyword) {
        if (keyword == null) {
            throw new InvalidMessageRequestException("keyword is null");
        }
        log.debug("Getting messages by keyword: keyword = {}", keyword);
        List<Message> messages = messageRepository
                .findByContentContainingIgnoreCase(keyword);
        return messageMapper.toDtoList(messages, authorOnlineMap(messages));
    }

    @Override
    public PageResponseDto<MessageResponseDto> getPageByChannelId(UUID channelId, Pageable pageable) {
        if (channelId == null) {
            throw new InvalidMessageRequestException("channelId is null");
        }
        if (pageable == null) {
            throw new InvalidMessageRequestException("pageable is null");
        }

        log.debug("Getting Page messages: channelId = {}, pageable = {}", channelId, pageable);

        Slice<Message> slice = messageRepository.findByChannelIdOrderByCreatedAtDesc(channelId, pageable);

        Map<UUID, Boolean> onlineMap = authorOnlineMap(slice.getContent());

        Slice<MessageResponseDto> sliceDto = slice.map(message -> {
            User author = message.getAuthor();
            UUID authorId = author != null ? author.getId() : null;
            boolean online = authorId != null && onlineMap.getOrDefault(authorId, false);
            return messageMapper.toDto(message, online);
        });

        return pageResponseMapper.fromSlice(sliceDto);
    }

    private Map<UUID, Boolean> authorOnlineMap(List<Message> messages) {
        if (messages == null) {
            throw new InvalidMessageRequestException("messages is null");
        }

        log.debug("Getting author online map");
        Set<UUID> authorIds = messages.stream()
                .map(m -> m.getAuthor())
                .filter(u -> u != null)
                .map(author -> author.getId())
                .filter(authorId -> authorId != null)
                .collect(Collectors.toSet());

        if (authorIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return sessionOnlineChecker.onlineMap(authorIds);
    }
}
