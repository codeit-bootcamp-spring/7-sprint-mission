package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
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
    public void createMessage(CreateMessageDto createMessageDto) {
        User user = userRepository.findById(createMessageDto.getUserId())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저입니다." + createMessageDto.getUserId()));
        Channel channel = channelRepository.findById(createMessageDto.getChannelid())
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 채널입니다." + createMessageDto.getChannelid()));

        List<UUID> binaryContentIds;
        if (createMessageDto.getCreateBinaryContentDtos() == null) {
            binaryContentIds = new ArrayList<>();
        } else {
            binaryContentIds = createMessageDto.getCreateBinaryContentDtos().stream().map(
                    createBinaryContentDto -> {
                        BinaryContent binaryContent = new BinaryContent(
                                createBinaryContentDto.getFileName(),
                                createBinaryContentDto.getContentType(),
                                createBinaryContentDto.getBytes());
                        binaryContentRepository.save(binaryContent);
                        return binaryContent.getId();
                    }
            ).toList();
        }

        Message message = new Message(createMessageDto.getContent(), createMessageDto.getChannelid(), createMessageDto.getUserId(), binaryContentIds);
        messageRepository.save(message);

        channel.addParticipant(user);
        user.joinChannel(channel);

    }

    @Override
    public Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 메시지: " + messageId));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> getAllMessageByChannelId(UUID channelID) {
        return messageRepository.findAllByChannelId(channelID);
    }

    @Override
    public void updateMessage(UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(updateMessageDto.getMessageId())
                .orElseThrow(() -> new NoSuchElementException("메시지를 찾을 수 없습니다: " + updateMessageDto.getMessageId()));
        message.messageUpdate(updateMessageDto.getContent());
        messageRepository.save(message);
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
