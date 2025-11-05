package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public MessageResponseDto createMessage(CreateMessageDto createMessageDto) {
        User user = userRepository.findById(createMessageDto.userId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저입니다." + createMessageDto.userId()));
        Channel channel = channelRepository.findById(createMessageDto.channelId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 채널입니다." + createMessageDto.channelId()));

        List<UUID> binaryContentIds;
        if (createMessageDto.createBinaryContentDtos() == null) {
            binaryContentIds = new ArrayList<>();
        } else {
            binaryContentIds = createMessageDto.createBinaryContentDtos().stream().map(
                    createBinaryContentDto -> {
                        BinaryContent binaryContent = new BinaryContent(
                                createBinaryContentDto.fileName(),
                                createBinaryContentDto.contentType(),
                                createBinaryContentDto.bytes());
                        binaryContentRepository.save(binaryContent);
                        return binaryContent.getId();
                    }
            ).toList();
        }

        Message message = new Message(createMessageDto.content(), createMessageDto.channelId(), createMessageDto.userId(), binaryContentIds);
        messageRepository.save(message);

        channel.addParticipant(user);
        user.joinChannel(channel);

        return MessageResponseDto.from(message);
    }

    @Override
    public MessageResponseDto getMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 메시지: " + messageId));

        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    @Override
    public List<MessageResponseDto> getAllMessageByChannelId(UUID channelID) {
        return messageRepository.findAllByChannelId(channelID).stream()
                .map(MessageResponseDto::from)
                .toList();
    }

    @Override
    public MessageResponseDto updateMessage(UUID messageId, UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + messageId));
        message.messageUpdate(updateMessageDto.content());
        messageRepository.save(message);

        return MessageResponseDto.from(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("삭제할 메시지를 찾을 수 없습니다: " + messageId));
        message.getAttachmentIds().
                forEach(binaryContentRepository::deleteById);

        messageRepository.deleteById(messageId);
    }

}
