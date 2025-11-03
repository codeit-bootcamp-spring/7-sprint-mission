package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    private MessageDto toDto(Message m) {
        return new MessageDto(
                m.getId(),
                m.getCreatedAt(),
                m.getUpdatedAt(),
                m.getContent(),
                m.getAuthorId(),
                m.getChannelId(),
                m.getAttachments()

        );
    }

    public MessageDto create(MessageCreateRequest request) {
        UUID channelId = request.channelId();
        UUID authorId  = request.authorId();

        // 채널/유저 존재 여부만 검증(채널 엔티티를 굳이 로드할 필요 없음)
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found: " + channelId);
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("User not found: " + authorId);
        }

        Message m = Message.createText(
                request.content(),
                authorId,
                channelId,
                request.attachments()
        );
        messageRepository.save(m);
        return toDto(m);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {
        return null;
    }

    @Override
    public void delete(UUID messageId) {

    }
}
