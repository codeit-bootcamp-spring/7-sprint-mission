package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Message create(MessageCreateRequestDto messageCreateRequestDto) {
        UUID channelId = Objects.requireNonNull(messageCreateRequestDto.channelId());
        UUID authorId = Objects.requireNonNull(messageCreateRequestDto.authorId());
        String content = Objects.requireNonNull(messageCreateRequestDto.content());

        Channel ch =  channelRepository.findById(messageCreateRequestDto.channelId())
                .orElseThrow(()-> new NoSuchElementException("Channel not found"));
        userRepository.findById(messageCreateRequestDto.authorId()).orElseThrow(()
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

        List<UUID> attachmentIds = messageCreateRequestDto.attachmentIds() == null
                ? new ArrayList<>() : new ArrayList<>(messageCreateRequestDto.attachmentIds());

        for(UUID attachmentId : attachmentIds) {
            if(binaryContentRepository.findById(attachmentId).isEmpty()) {
                throw new NoSuchElementException("Attachment not found");
            }
        }

        Message message = new Message(
                content,
                messageCreateRequestDto.userName(),
                authorId,
                channelId,
                attachmentIds
        );

        return messageRepository.save(message);
    }

    @Override
    public Message get(UUID messageId) {
        return messageRepository.findById(Objects.requireNonNull(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message not found"));
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getAllByChannelId(UUID channelId) {
        List<Message> channel = messageRepository.findByChannelId(Objects.requireNonNull(channelId));
        channel.removeIf(m -> m.isDeleted());
        channel.sort(Comparator.comparing(m -> m.getCreatedAt()));

        return channel;
    }

    @Override
    public List<Message> getAllByChannelForUser(UUID channelId, UUID userId ) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found"));

        if(!channel.getMembers().containsKey(userId)) {
            throw new NoSuchElementException("User not found");
        }

        List<Message> channels = messageRepository.findByChannelId(channelId);
        channels.removeIf(m -> m.isDeleted());
        channels.sort(Comparator.comparing(m -> m.getCreatedAt()));
        return channels;
    }

    @Override
    public Message update(MessageUpdateRequestDto messageUpdateRequestDto) {
        UUID messageId = Objects.requireNonNull(messageUpdateRequestDto.messageId());
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message not found"));

        if(messageUpdateRequestDto.isDeleted()) {
            if(message.getAttachmentIds() != null) {
                for(UUID attachmentId : message.getAttachmentIds()) {
                    binaryContentRepository.deleteById(attachmentId);
                }
            }
            message.delete();
            return messageRepository.save(message);
        }

        if(messageUpdateRequestDto.content() != null) {
            message.setContent(messageUpdateRequestDto.content());
        }

        if(messageUpdateRequestDto.attachmentIds() != null) {
            for(UUID attachmentId : messageUpdateRequestDto.attachmentIds()) {
                if(binaryContentRepository.findById(attachmentId).isEmpty()) {
                    throw new NoSuchElementException("Attachment not found");
                }
            }
            message.setAttachmentIds(messageUpdateRequestDto.attachmentIds());
        }

        return messageRepository.save(message);
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
    public List<Message> getMessagesByAuthor(UUID authorId) {
        return messageRepository.findByAuthorId(Objects.requireNonNull(authorId));
    }

    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        return messageRepository.findByChannelIdAndAuthorId(
                Objects.requireNonNull(channelId),
                Objects.requireNonNull(authorId)
        );
    }

    // 특정 키워드 검색
    @Override
    public List<Message> searchByKeyword(String keyword) {
        return messageRepository.searchByKeyword(Objects.requireNonNull(keyword));
    }
}
