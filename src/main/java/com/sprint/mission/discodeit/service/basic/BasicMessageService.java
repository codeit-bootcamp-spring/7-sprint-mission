package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.FindAllByChannelIdMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryRepository binaryRepository;


    @Override
    @Transactional
    public Message create(CreateMessageRequest request, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        //둘의 uuid가 존재유무판단

        if (!channelRepository.existsById(request.channelId())) {
            throw new NoSuchElementException("채널UUID가없어 :" + request.channelId());
        }
        if (!userRepository.existsById(request.authorId())) {
            throw new NoSuchElementException("유저UUID가 없어 :" + request.authorId());
        }

        List<BinaryContent> binaryContents = makeBinaryContent(binaryContentCreateRequests);


        User author = userRepository.getReferenceById(request.authorId());
        Channel channel = channelRepository.getReferenceById(request.channelId());

        Message message = new Message(
                author,
                channel,
                request.content(),
                binaryContents
        );

        return messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Message find(UUID messageId) {
        return messageRepository
                .findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지UUID가 없어:" + messageId));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);

    }

    @Override
    @Transactional
    public Message update(UUID messageId, UpdateMessageRequest request) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("매시지아이디가 없어 " + messageId));

        message.update(request.newContent());

        return message;
    }

    @Override
    @Transactional
    public void delete(UUID messageId) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지 아이디가 없어: " + messageId));

        List<BinaryContent> attachments = new ArrayList<>(message.getAttachments());

        if (!attachments.isEmpty()) {
            binaryRepository.deleteAll(attachments);
        }
        messageRepository.deleteById(messageId);
    }


    private List<BinaryContent> makeBinaryContent(List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        return binaryContentCreateRequests.stream()
                .map(attachmentRequest -> {
                    byte[] bytes = attachmentRequest.bytes();

                    return new BinaryContent(attachmentRequest.fileName(), (long) bytes.length, attachmentRequest.contentType(), bytes);

                })
                .toList();
    }


}

