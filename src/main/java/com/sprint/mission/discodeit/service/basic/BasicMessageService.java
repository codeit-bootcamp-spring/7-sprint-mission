package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
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



    @Override
    public MessageResponse create(CreateMessageRequest request,List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        //둘의 uuid가 존재유무판단
        System.out.println(request.channelId());
        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널UUID가없어 :" + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("유저UUID가 없어 :" + request.authorId());
        }

        List<UUID> attachmentIds = binaryContentCreateRequests.stream()
                .map(attachmentRequest -> {

                    byte[] bytes = attachmentRequest.contentByte();

                    BinaryContent binaryContent = new BinaryContent(ContentsType.MESSAGE_ATTACHMENT, attachmentRequest.contentByte(),"null");
                    BinaryContent createdBinaryContent = binaryRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                })
                .toList();

        Message message = new Message(
                request.content(),
                request.channelId(),
                request.authorId(),
                attachmentIds
        );

            return MessageResponse.from(messageRepository.save(message));
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + messageId));
        return MessageResponse.from(message);
    }

    @Override
    public List<MessageResponse> findAllByChannelId(FindAllByChannelIdMessageRequest request) {
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(request.channelId()))
                .map(MessageResponse::from)
                .toList();

    }

    @Override
    public MessageResponse update(UpdateMessageRequest request) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new NoSuchElementException("매시지아이디가 없어 " + request.messageId()));
        message.update(request.newContent());
        Message updateMessage = messageRepository.save(message);
        return MessageResponse.from(updateMessage);
    } 

    @Override
    public void delete(DeleteMessageRequest request) {
        if (!messageRepository.existsById(request.messageId())) {
            throw new NoSuchElementException("메시지 아이디가 없어" + request.messageId());
        }
        List<UUID> attachmentIds = messageRepository
                .findById(request.messageId())
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + request.messageId()))
                .getAttachmentIds();

        if(attachmentIds != null){
            attachmentIds.forEach(binaryRepository::delete);
        }
        messageRepository.deleteById(request.messageId());
    }
}
