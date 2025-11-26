package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Message;

import com.sprint.mission.discodeit.entity.MessageAttachmentEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final MessageMapper mapper;
    private final BinaryContentService binaryContentService;
    private final UserRepository userRepository;


    @Transactional
    public MessageDto sendMessage(MessageCreateRequest form, List<MultipartFile> attachments) {

        Message message = new Message(
                form.authorId(),
                form.content(),
                form.channelId());

        MessageEntity messageEntity = mapper.toMessageEntity(message);
        MessageEntity save = messageRepository.save(messageEntity);

        UserEntity userEntity = userRepository.findById(save.getUserId()).orElseThrow(() -> new NoSuchElementException("메세지의 유저를 찾을 수 없음"));


        MessageDto messageDto = mapper.toMessageDto(save);

        if (!attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                BinaryContentDto content = binaryContentService.put(form.authorId(), file);
                MessageAttachmentEntity messageAttachmentEntity = new MessageAttachmentEntity();
                messageAttachmentEntity.setMessageId(save.getId());
                messageAttachmentEntity.setAttachmentId(content.getId());
                attachmentRepository.save(messageAttachmentEntity);
                messageDto.addAttachment(content);
            }
        }

        return messageDto;
    }

    public Message updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {

        if (messageUpdateRequest.newContent() != null) {
            MessageEntity messageEntity = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("수정하고자 하는 메세지를 찾을 수 없습니다."));
            messageEntity.setContent(messageUpdateRequest.newContent());
        }

        return mapper
    }

    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
        if (!message.getAttachmentIds().isEmpty()) {
            for (String attachmentId : message.getAttachmentIds()) {
                binaryContentService.deleteFile(attachmentId);
            }
        }
        messageRepository.delete(message);

    }


    public List<MessageDto> getAllMessage(UUID channelId) {
        return messageRepository.findByChannelId(channelId).stream().map(message -> MessageDto.from(message)).toList();
    }


}
