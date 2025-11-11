package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;

    @Override
    public MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments) {
        UUID channelId = Objects.requireNonNull(messageCreateRequestDto.channelId());
        UUID authorId = Objects.requireNonNull(messageCreateRequestDto.authorId());
        String content = Objects.requireNonNull(messageCreateRequestDto.content());

        Channel ch =  channelRepository.findById(messageCreateRequestDto.channelId())
                .orElseThrow(()-> new NoSuchElementException("Channel not found"));

        User user = userRepository.findById(messageCreateRequestDto.authorId()).orElseThrow(()
                -> new NoSuchElementException("User not found"));

        if(!ch.getMembers().containsKey(authorId)) {
            throw new NoSuchElementException("Member not found");
        }


        int slow = ch.getSlowModeSeconds();
        if ( slow < 0 ) {
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
                    throw new IllegalStateException("SlowMode wait : " + waitSeconds + "s");
                }
            }
        }

        List<UUID> attachmentIds = new ArrayList<>();
        if(attachments != null) {
            for(MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    BinaryContentCreateRequestDto dto = BinaryContentCreateRequestDto.from(file);
                    BinaryContentResponseDto save = binaryContentService.create(dto);
                    attachmentIds.add(save.id());
                }
            }
        }

        Message message = new Message(
                content,
                user.getUsername(),
                authorId,
                channelId,
                attachmentIds
        );

        Message save = messageRepository.save(message);

        return MessageResponseDto.from(save);
    }

    @Override
    public MessageResponseDto get(UUID messageId) {
        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> getAll() {
        return messageRepository.findAll().stream()
                .map(message -> MessageResponseDto.from(message))
                .toList();
    }

    @Override
    public List<MessageResponseDto> getAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        messages.removeIf(m -> m.isDeleted());
        messages.sort(Comparator.comparing(m -> m.getCreatedAt()));

        return messages.stream()
                .map(message -> MessageResponseDto.from(message))
                .toList();
    }

    @Override
    public MessageResponseDto update(UUID messageId, MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        if(messageUpdateRequestDto.newContent() != null) {
            message.setContent(messageUpdateRequestDto.newContent());
        }

        Message save = messageRepository.save(message);
        return MessageResponseDto.from(save);
    }

    @Override
    public boolean delete(UUID messageId) {
        Message message = messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
        if(message.getAttachmentIds() != null) {
            for(UUID attachmentId : message.getAttachmentIds()) {
                binaryContentRepository.deleteById(attachmentId);
            }
        }
        return messageRepository.deleteById(messageId);
    }

    // 특정 작성자별 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByAuthor(UUID authorId) {
        return messageRepository.findByAuthorId(Objects.requireNonNull(authorId))
                .stream().map(message -> MessageResponseDto.from(message))
                .toList();
    }

    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<MessageResponseDto> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        return messageRepository.findByChannelIdAndAuthorId(
                Objects.requireNonNull(channelId),
                Objects.requireNonNull(authorId))
                .stream()
                .map(message -> MessageResponseDto.from(message))
                .toList();
    }

    // 특정 키워드 검색
    @Override
    public List<MessageResponseDto> searchByKeyword(String keyword) {
        return messageRepository.searchByKeyword(Objects.requireNonNull(keyword))
                .stream()
                .map(message -> MessageResponseDto.from(message))
                .toList();
    }
}
