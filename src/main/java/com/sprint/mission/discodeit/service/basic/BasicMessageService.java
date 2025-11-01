package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponseDto createMessage(CreateMessageRequestDto request) {
        userRepository.findById(request.authorId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        channelRepository.findById(request.channelId())
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        List<UUID> attachment = request.attachmentIds();
        // 만약 첨부파일이 빈값이면, 빈리스트로 반환하기
        if(attachment == null){
            attachment = List.of();
        }

        Message message = new Message(
                request.content(),
                request.authorId(),
                request.channelId(),
                attachment
                );

        Message save = messageRepository.save(message);
        return MessageResponseDto.from(save);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        List<Message> messages = messageRepository.findByChannelId(channelId);
        List<MessageResponseDto> dtoList = new ArrayList<>();
        for(Message message : messages){
            dtoList.add(MessageResponseDto.from(message));
        }
        return dtoList;
    }

    @Override
    public MessageResponseDto updateMessage(UpdateMessageDto newContents) {
        Message message = messageRepository.findById(newContents.messageId())
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        String newContent = newContents.newContent();

        message.updateContent(newContent);
        Message save = messageRepository.save(message);
        return MessageResponseDto.from(save);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));

        List<UUID> attachmentIds = message.getAttachmentIds();

        for(UUID attachmentId : attachmentIds){
            binaryContentRepository.delete(attachmentId);
        }
        messageRepository.delete(messageId);
    }
}
