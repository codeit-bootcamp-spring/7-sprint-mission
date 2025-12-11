package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.dto.response.MessageDto;
import com.sprint.mission.discodeit.service.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.mapper.MessagePageResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final MessagePageResponseMapper pageMapper;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentService binaryContentService;


    @Transactional
    public MessageDto sendMessage(MessageCreateRequest request, List<MultipartFile> attachments) {
        log.info("MessageService.sendMessage");
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
        MessageDto dto = mapper.toDto(save);

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                BinaryContent content = binaryContentService.put(request.authorId(), file);
                MessageAttachment messageAttachment = new MessageAttachment(message, content);
                MessageAttachment save1 = attachmentRepository.save(messageAttachment);
                dto.addAttachment(binaryContentMapper.toDto(content));
            }
        }

        return dto;
    }

    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.info("MessageService.updateMessage");
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("수정하고자 하는 메세지를 찾을 수 없습니다."));
        message.updateContent(messageUpdateRequest.newContent());
        return mapper.toDto(message);


    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        log.info("MessageService.deleteMessage");
        List<MessageAttachment> list = attachmentRepository.findAllByMessageId(messageId);
        attachmentRepository.deleteById(messageId);
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("메세지가 없습니다."));
        messageRepository.delete(message);

        for (MessageAttachment attachment : list) {
            binaryContentService.deleteFile(attachment.getAttachment().getId());
        }

    }


    public PageResponse<MessageDto> getAllByChannelId(UUID channelId, Pageable pageable) {
        Page<MessageDto> map = messageRepository.findAllByChannelId(channelId, pageable)
                .map(message -> {
                    MessageDto dto = mapper.toDto(message);
                    List<MessageAttachment> allByMessageId = attachmentRepository.findAllByMessageId(message.getId());
                    for (MessageAttachment messageAttachment : allByMessageId) {
                        BinaryContentDto content = binaryContentMapper.toDto(messageAttachment.getAttachment());
                        dto.addAttachment(content);
                    }
                    return dto;
                });
        return pageMapper.fromPage(map);
    }

}
