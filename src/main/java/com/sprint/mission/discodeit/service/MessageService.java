package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentManager;
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

import java.util.HashMap;
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
    private final BinaryContentManager binaryContentManager;


    @Transactional
    public MessageDto sendMessage(MessageCreateRequest request, List<MultipartFile> attachments) {
        log.info("MessageService.sendMessage");
        User user =
                userRepository.findById(request.authorId()).orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, new HashMap<>()));
        Channel channel =
                channelRepository.findById(request.channelId()).orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND, new HashMap<>()));
        Message message = new Message(
                user,
                channel,
                request.content()
        );

        Message save = messageRepository.save(message);
        MessageDto dto = mapper.toDto(save);

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                BinaryContent content = binaryContentManager.saveFileAndMeta(file);
                MessageAttachment messageAttachment = new MessageAttachment(message, content);
                MessageAttachment save1 = attachmentRepository.save(messageAttachment);
                dto.addAttachment(binaryContentMapper.toDto(content));
            }
        }

        return dto;
    }

    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.info("MessageService.updateMessage");
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, new HashMap<>()));
        message.updateContent(messageUpdateRequest.newContent());
        return mapper.toDto(message);


    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        log.info("MessageService.deleteMessage");
        List<MessageAttachment> list = attachmentRepository.findAllWithBinaryContentByMessageId(messageId);
        //여기서 삭제 쿼리는 여러번 나감.
        // 나중에 여러개를 한 번에 삭제해주는 쿼리 + 여러 파일 동시 삭제 메서드를 manager에 만들어주면 될 듯
        for (MessageAttachment attachment : list) {
            binaryContentManager.deleteFile(attachment.getAttachment());
        }
        attachmentRepository.deleteByMessageId(messageId);
        messageRepository.deleteById(messageId);
    }


    public PageResponse<MessageDto> getAllByChannelId(UUID channelId, Pageable pageable) {
        Page<MessageDto> map = messageRepository.findAllByChannelId(channelId, pageable)
                .map(message -> {
                    MessageDto dto = mapper.toDto(message);
//                    List<MessageAttachment> allByMessageId = attachmentRepository.findAllWithBinaryContentByMessageId(message.getId());
//                    for (MessageAttachment messageAttachment : allByMessageId) {
//                        BinaryContentDto content = binaryContentMapper.toDto(messageAttachment.getAttachment());
//                        dto.addAttachment(content);
//                    }
                    return dto;
                });
// 지그 N+1문제 터지고 있음. message를 가져오는 거 까지는 문제가 없지만, 각 메세지 마다 attachmentRepository.findAllWith~ 호출중.
        //

        return pageMapper.fromPage(map);
    }

}
