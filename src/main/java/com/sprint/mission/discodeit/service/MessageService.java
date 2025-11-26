package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.mapper.MessageMapper;
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
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final MessageMapper mapper;
    private final BinaryContentService binaryContentService;


    @Transactional
    public MessageDto sendMessage(MessageCreateRequest request, List<MultipartFile> attachments) {

        User user =
                userRepository.findById(request.authorId()).orElseThrow(() -> new NoSuchElementException("해당 유저가 존재하지 않습니다."));

        Channel channel =
                channelRepository.findById(request.channelId()).orElseThrow(() -> new NoSuchElementException("해당 채널이 존재하지 않습니다."));


        Message message = new Message(
                user,
                channel,
                request.content()
        );


        Message save = messageRepository.save(message);

        if (!attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                BinaryContent content = binaryContentService.put(request.authorId(), file);
                MessageAttachment messageAttachment = new MessageAttachment(message, content);
                attachmentRepository.save(messageAttachment);
            }
        }

        return mapper.toDto(message);
    }

    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("수정하고자 하는 메세지를 찾을 수 없습니다."));
        message.setContent(messageUpdateRequest.newContent());
        return mapper.toDto(message);


    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        List<MessageAttachment> list = attachmentRepository.findAllByMessage_Id(messageId);
        attachmentRepository.deleteById(messageId);
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
        messageRepository.delete(message);

        for (MessageAttachment attachment: list) {
            binaryContentService.deleteFile(attachment.getAttachment().getId());
        }

    }


    public List<MessageDto> getAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannel_Id(channelId).stream().map(mapper::toDto).toList();
    }

}
