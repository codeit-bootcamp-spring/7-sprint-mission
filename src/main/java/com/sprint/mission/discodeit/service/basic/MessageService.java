package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.MessageException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.channelNotFoundException;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentCreatedEvent;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import java.util.Map;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.page.PageResponseDto;
import com.sprint.mission.discodeit.repository.jpa.ChannelsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageAttachmentsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.data.domain.Slice;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
//@Transactional(isolation = Isolation.READ_COMMITTED) // 영속성 컨텍스트
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class MessageService implements InterfaceMessageService {
    private final MessagesRepository messageRepository;
    private final ChannelsRepository channelRepository;
    private final UsersRepository userRepository;
    private final MessageAttachmentsRepository messageAttachmentsRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentsRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MessageDto create(MessageCreateRequest dtoMessage, List<MultipartFile> fileList) {
        if (dtoMessage.channelId() == null)
            throw new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION, Map.of("dtoMessage.channelId", "isNull"));

        if (dtoMessage.authorId() == null)
            throw new DiscodeitException(ErrorCode.ILLEAGALARGUEMNTEXCEPTION, Map.of("MessageCreateRequest.authorId", "isNull"));

        Channel channel = channelRepository
            .findById(dtoMessage.channelId())
            .orElseThrow(() -> new channelNotFoundException(dtoMessage.channelId()));

        User user = userRepository
            .findById(dtoMessage.authorId())
            .orElseThrow(() -> new UserNotFoundException(dtoMessage.authorId()));

        Message message = new Message(dtoMessage.content(),
            channel,
            user);

        if (null != fileList && !fileList.isEmpty()) {
            for (MultipartFile file : fileList) {
                BinaryContent binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType());

                BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
                eventPublisher.publishEvent(new BinaryContentCreatedEvent(savedBinaryContent.getId(), file));

                MessageAttachments messageAttachments = new MessageAttachments(
                    message,
                    savedBinaryContent
                );
                message.addAttachment(messageAttachments);
            }
        }

        Message savedMessage = messageRepository.save(message);


        log.info("✅ 💌 MessageService.create.content = [" + message.getContent() + "] 💬");
        return messageMapper.toDto(message);
    }


    @Override
    @Transactional
    @PreAuthorize("@messageAuth.isOwner(#messageID, authentication)")
    public void deleteMessage(UUID messageID) {
      Message message = messageRepository
          .findById(messageID)
          .map(model -> (Message)model)
          .orElseThrow(() -> new NoSuchElementException("🚨Message [" + messageID.toString() + "] 를 찾을 수 없음"));

      messageRepository.deleteById(messageID);
      log.info("✅ deleteMessage = [" + message.getContent() + "]");
    }

    @Override
    @Transactional(readOnly = true)
    public MessageDto find(UUID messageID) {
        Message message = messageRepository
            .findById(messageID)
            .orElseThrow(() -> new MessageException(ErrorCode.MESSAGE_NOT_FOUND, Map.of("messageId", messageID)));

        log.info("✅ MessageService.find = [" + message.getContent() + "]");
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional(readOnly = true)
//        [ ] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId
    public PageResponseDto<MessageDto> findAllByChannelId(UUID channelID, Pageable pageable) { //♨️
        Slice<Message> slice = messageRepository.findByChannelId(channelID, pageable);

        Slice<MessageDto> sliceDto = slice.map(messageMapper::toDto);
        return pageResponseMapper.fromSlice(sliceDto);
    }

    @Override
    @Transactional
    @PreAuthorize("@messageAuth.isOwner(#messageId, authentication)") // SpEL
    public MessageDto updateMessage(UUID messageId, Dto_MessageUpdate requestDto) {
        // [ ] DTO를 활용해 파라미터를 그룹화합니다.
        // 수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        Message message = messageRepository
            .findById(messageId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("🚨Message [" + messageId.toString() + "]를 찾을 수 없음"));

        message.setContent(requestDto.newContent());

        messageRepository.save(message);

        log.info("✅ updateMessage = [" + message.getContent() + "]");

        return messageMapper.toDto(message);
    }
}
