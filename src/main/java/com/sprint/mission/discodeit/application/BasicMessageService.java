package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.application.dto.request.MessageForm;
import com.sprint.mission.discodeit.application.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.application.dto.response.MessageResponse;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;

    private final FileService fileService;


    public MessageResponse sendMessage(MessageForm form) throws IOException {
        Message message;
        if(form.image().isEmpty()){
            message = new Message(form.userId(), form.content(), form.channelId(), null);
        } else {
            BinaryContent content = fileService.saveMessageFile(form.userId(), form.image());
            message = new Message(form.userId(), form.content(), form.channelId(), content.getId());
        }
        messageRepository.save(message);
        UUID messageId = message.getId();
        Channel channel = channelRepository.findById(form.channelId()).orElseThrow(()->new NoSuchElementException("채널이 없습니다."));
        channel.sendMessage(messageId);
        log.info("message send complete: {}",message.getContent());
        channelRepository.save(channel);
        return MessageResponse.from(message);
    }

    public MessageResponse updateMessage(MessageUpdate messageUpdate) throws IOException {
        Message message = findById(messageUpdate.id());
        if(messageUpdate.content()!=null){
            message.updateContent(messageUpdate.content());
        }
        if(messageUpdate.image()!=null){
            if(message.getImage()!=null) {
                BinaryContent content = fileService.findById(message.getImage());
                fileService.deleteMessageImage(content);
            }
            BinaryContent content = fileService.saveMessageFile(message.getSenderId(), messageUpdate.image());
            message.updateImage(content.getId());
        }
        messageRepository.save(message);
        log.info("MessageService updateMessage");
        return MessageResponse.from(message);
    }

    public void deleteMessage(UUID messageId) throws IOException {
        Message message = findById(messageId);
        if(message.getImage()!=null){
            BinaryContent content = fileService.findById(message.getImage());
            fileService.deleteMessageImage(content);
        }
        messageRepository.remove(messageId);
        Channel channel = channelRepository.findById(message.getChannelId()).orElseThrow(() -> new NoSuchElementException("채널 없음"));
        channel.deleteMessage(messageId);
    }

    public UUID findMessageImageId(UUID messageId){
        Message message = findById(messageId);
        return message.getImage();

    }

    public Message findById(UUID messageId){
        return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
    }
}
