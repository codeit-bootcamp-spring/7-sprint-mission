package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.request.Attachments;
import com.sprint.mission.discodeit.service.dto.request.MessageForm;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.service.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentService binaryContentService;


    public MessageResponse sendMessage(MessageForm form, Attachments attachments) {
        Message message;
        List<MultipartFile> files = attachments.files().stream().filter(f->!f.isEmpty()).toList();
        if(files == null || files.isEmpty()){
            message = new Message(form.authorId(), form.content(), form.channelId(), null);
        } else {
            List<UUID> attachmentsIds= new ArrayList<>();
            for (MultipartFile file : attachments.files()) {
                BinaryContent content = binaryContentService.saveMessageFile(form.authorId(), file);
                attachmentsIds.add(content.getId());
            }
            message = new Message(form.authorId(), form.content(), form.channelId(), attachmentsIds);
        }
        messageRepository.save(message);
        UUID messageId = message.getId();
        Channel channel = channelRepository.findById(form.channelId()).orElseThrow(()->new NoSuchElementException("채널이 없습니다."));
        channel.sendMessage(messageId);
        channelRepository.save(channel);
        return MessageResponse.from(message);
    }

    public MessageResponse updateMessage(UUID messageId,MessageUpdate messageUpdate) {
        Message message = getById(messageId);
        if(messageUpdate.newContent()!=null){
            message.updateContent(messageUpdate.newContent());
        }

        messageRepository.save(message);
        return MessageResponse.from(message);
    }

    public void deleteMessage(UUID messageId) {
        Message message = getById(messageId);
        if(message.getAttachmentIds().size()>0){
            for (UUID attachmentId : message.getAttachmentIds()) {
                BinaryContent content = binaryContentService.getById(attachmentId);
                binaryContentService.deleteMessageImage(content);
            }
        }
        messageRepository.remove(messageId);
        Channel channel = channelRepository.findById(message.getChannelId()).orElseThrow(() -> new NoSuchElementException("채널 없음"));
        channel.deleteMessage(messageId);
    }

    public List<UUID> getMessageImageId(UUID messageId){
        Message message = getById(messageId);
        return message.getAttachmentIds();

    }

    private Message getById(UUID messageId){
        return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
    }

    public List<MessageResponse> getAllMessage(UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("해당 채널에는 메세지가 없습니다."));
        return channel.getHistory().stream()
                .map(id -> messageRepository.findById(id).orElse(null))
                .map(message -> MessageResponse.from(message))
                .toList();
    }

}
