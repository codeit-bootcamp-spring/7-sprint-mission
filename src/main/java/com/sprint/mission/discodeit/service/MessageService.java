package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Message;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;


    public MessageDto sendMessage(MessageCreateRequest form, List<MultipartFile> attachments) {
        Message message;

        if (attachments == null || attachments.isEmpty()) {
            message = new Message(form.userId(), form.content(), form.channelId(), null);
        } else {
            List<String> attachmentsIds = new ArrayList<>();
            for (MultipartFile file : attachments) {
                BinaryContent content = binaryContentService.saveMessageFile(form.userId(), file);
                attachmentsIds.add(content.getId());
            }
            message = new Message(form.userId(), form.content(), form.channelId(), attachmentsIds);
        }
        messageRepository.save(message);
        return MessageDto.from(message);
    }

    public MessageDto updateMessage(String messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
        if (messageUpdateRequest.newContent() != null) {
            message.updateContent(messageUpdateRequest.newContent());
        }

        messageRepository.save(message);
        return MessageDto.from(message);
    }

    public void deleteMessage(String messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
        if (!message.getAttachmentIds().isEmpty()) {
            for (String attachmentId : message.getAttachmentIds()) {
                binaryContentService.deleteMessageImage(attachmentId);
            }
        }
        messageRepository.delete(message);

    }


    public List<MessageDto> getAllMessage(String channelId) {
        return messageRepository.findByChannelId(channelId).stream().map(message -> MessageDto.from(message)).toList();
    }


}
