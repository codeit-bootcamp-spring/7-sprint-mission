package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageAttachmentRepository attachmentRepository;
    private final MessageMapper mapper;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentManager binaryContentManager;
    private final ApplicationEventPublisher eventPublisher;


    @PreAuthorize("#request.authorId == authentication.principal.userDto.id")
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
                attachmentRepository.save(messageAttachment);
                dto.addAttachment(binaryContentMapper.toDto(content));
            }
        }

        eventPublisher.publishEvent(new MessageCreatedEvent(
                channel.getId(),
                user.getId(),
                user.getUsername(),
                channel.getName(),
                request.content(),
                dto
        ));
        return dto;
    }

    @PreAuthorize("@messageGuard.isAuthor(#messageId, authentication.principal.userDto.id)")
    public MessageDto updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        log.info("MessageService.updateMessage");
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, new HashMap<>()));
        message.updateContent(messageUpdateRequest.newContent());
        return mapper.toDto(message);
    }

    @PreAuthorize("@messageGuard.isAuthor(#messageId, authentication.principal.userDto.id)")
    public void deleteMessage(UUID messageId) {
        log.info("MessageService.deleteMessage");
        List<MessageAttachment> list = attachmentRepository.findAllWithBinaryContentByMessageId(messageId);
        //여기서 삭제 쿼리는 여러번 나감.
        // 나중에 여러개를 한 번에 삭제해주는 쿼리 + 여러 파일 동시 삭제 메서드를 manager에 만들어주면 될 듯
        for (MessageAttachment attachment : list) {
            binaryContentManager.deleteFile(attachment.getAttachment());
        }
        // cascade = CascadeType.REMOVE 때문에 수동으로 삭제를 해주면 오히려 오류가 남
        attachmentRepository.deleteByMessageId(messageId);
        messageRepository.deleteById(messageId);
    }


    @Transactional(readOnly = true)
    public PageResponse<MessageDto> getAllByChannelId(
            UUID channelId,
            Instant cursor,
            int size,
            String sort) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId, size + 1, sort, cursor);

        boolean hasNext = messages.size() > size;
        int endIndex = Math.min(messages.size(), size);
        List<Message> list = messages.subList(0, endIndex);


        List<UUID> messageIds = list.stream()
                .map(BaseEntity::getId)
                .toList();

        Map<UUID, List<BinaryContentDto>> collect = attachmentRepository.findAllWithBinaryContentByMessageIds(messageIds)
                .stream()
                .collect(Collectors.groupingBy(messageAttachment -> messageAttachment.getMessage().getId(),
                        Collectors.mapping(messageAttachment -> binaryContentMapper.toDto(messageAttachment.getAttachment()), Collectors.toList())));

        List<MessageDto> result = list.stream()
                .map(message -> {
                    MessageDto dto = mapper.toDto(message);
                    if (collect.containsKey(message.getId()))
                        dto.setAttachments(collect.get(message.getId()));
                    return dto;
                }).toList();

        MessageDto last = result.isEmpty() ? null : result.get(result.size() - 1);
        Long totalElements = messageRepository.getTotalElementsByChannelId(channelId);
        return new PageResponse<MessageDto>(
                result,
                last,
                result.size(),
                hasNext,
                totalElements);
    }
}
