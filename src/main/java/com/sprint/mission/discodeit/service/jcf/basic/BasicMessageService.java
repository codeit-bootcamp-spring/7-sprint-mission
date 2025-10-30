package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.repository.BinaryRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryRepository binaryRepository;

    //의존성 주입이긴한데 이거 일커지면 더 늘어나는데
  /*  public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;

    }*/

    @Override
    public Message create(CreateMessageRequest request) {
        //둘의 uuid가 존재유무판단
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널UUID가없어 :" + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("매시지UUID가 없어 :" + request.authorId());
        }
        Message message = new Message(request.content(), request.channelId(), request.authorId());
        //첨부파일이 없으면 그냥 저장
        if(request.attachmentIds() == null || request.attachmentIds().isEmpty()){
            return messageRepository.save(message);
        }
        //첨부파일이 있으면
        //만들고 첨부파일 추가 저장

        request.attachmentIds()
                .forEach(id->
                        binaryRepository.save(new BinaryContent(message.getId(), ContentsType.MESSAGE_ATTACHMENT,id)));
        return messageRepository.save(message);


    }

    @Override
    public Message find(UUID messageId) {
        return  messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
         return  messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();

    }

    @Override
    public Message update(UpdateMessageRequest request) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new NoSuchElementException("매시지아이디가 없어 " + request.messageId()));
        message.update(request.newContent());

        return messageRepository.save(message);
    } 

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("메시지 아이디가 없어" + messageId);
        }
        binaryRepository.deleteByUuid(messageId, ContentsType.MESSAGE_ATTACHMENT);
        messageRepository.deleteById(messageId);
    }
}
