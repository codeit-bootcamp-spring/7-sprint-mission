package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDtoBasic;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.exception.domain.file.FileByteReadFailException;
import com.sprint.mission.discodeit.exception.domain.message.MessageNotExistException;
import com.sprint.mission.discodeit.exception.domain.role.InvalidAccessException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotJoinException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseBasicMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.subTable.MessageAttachment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.service.util.StaticString.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageAttachmentRepository messageAttachmentRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final PageResponseMapper<MessageDto> pageResponseMapper;
    private final PageResponseBasicMapper<MessageDto> pageResponseBasicMapper;
    private final ReadStatusRepository readStatusRepository;

    @Transactional
    public MessageDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments   ) {
        UUID channelId = messageCreateRequestDto.channelId();
        UUID authorId = messageCreateRequestDto.authorId();
        Channel channel2 = channelRepository.findById(channelId).orElseThrow(()->new ChannelNotExistException(channelId));
        User targetUser = userRepository.findById(authorId).orElseThrow(()->new UserNotExistException(authorId));
        if(readStatusRepository.findReadStatusByChannelAndUser(channel2,targetUser)== null) throw(new UserNotJoinException(channelId,authorId));

        Message message;
        message = messageRepository.save(Message.createMessageFactory(
                messageCreateRequestDto.content(),
                targetUser,
                channel2
        ));

        if(attachments!=null) {
            List<MessageAttachment> messageAttachmentList = new ArrayList<>();
            List<BinaryContent> attachmentList = new ArrayList<>();
            attachments.forEach(
                    x-> {
                        try {
                           BinaryContent binaryContent = binaryContentRepository.save(
                                   new BinaryContent(x.getOriginalFilename(),x.getContentType(),x.getSize())
                            );
                           attachmentList.add(binaryContent);
                           binaryContentStorage.put(binaryContent.getId(),x.getBytes());
                            MessageAttachment save = messageAttachmentRepository.save(new MessageAttachment(message, binaryContent));
                            messageAttachmentList.add(save);
                        }

                        catch (IOException e) {
                            throw new FileByteReadFailException(x.getName());
                        }
                    }
            );
            message.setAttachments(attachmentList);
            message.setMessageAttachment(messageAttachmentList);
        }
        log.error("message : {} 일단 에러를 뱉어",message);

        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> readAllMessage(){
        return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
    }
    public void deleteMessage(UUID messageId){
        if (!checkAuthor(messageId)) throw new InvalidAccessException();
        if(!messageRepository.existsById(messageId)) throw new MessageNotExistException(messageId);
        log.warn("delete message : {}",messageId);
        messageRepository.deleteById(messageId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDtoBasic<MessageDto> findallByChannelId(UUID channelId, Pageable pageable) {
        if(!channelRepository.existsById(channelId)) throw new ChannelNotExistException(channelId);
        Page<Message> targetPage = messageRepository.findByChannelId(channelId,pageable);
        if(targetPage.isEmpty()) return pageResponseBasicMapper.fromPage(Page.empty());
        Page<MessageDto> targetPageDto = targetPage.map(messageMapper::toDto);
        return pageResponseBasicMapper.fromPage(targetPageDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MessageDto> findallByChannelIdWithCursor(UUID channelId, String cursor,Pageable pageable) {
        Slice<Message> targetSlice ;

        if(cursor == null) {
          Instant tempTime = Instant.now().minus(Duration.ofDays(2));
          targetSlice = messageRepository.findByChannelIdAndCreatedAtAfter(channelId,tempTime,pageable);
        }
        else{
            targetSlice = messageRepository.findByChannelIdAndCreatedAtAfter(channelId,Instant.parse(cursor),pageable);
        }
            Object next = targetSlice.isEmpty()?Instant.now().minus(Duration.ofDays(2)):
                    targetSlice.getContent().get(targetSlice.getContent().size()-1)
                            .getCreatedAt();
        Slice<MessageDto> targetSliceDto = targetSlice.map(messageMapper::toDto);
        return pageResponseMapper.fromSlice(targetSliceDto,next);
    }

    @Override
    public List<MessageDto> readAllMessageByUserId(UUID userId) {
        return messageRepository.findAll().stream().filter(x->x.getAuthor().getId().equals(userId)).map(messageMapper::toDto).toList();
    }

    @Override
    public void resetMessage() {
        messageRepository.deleteAll();
    }

    @Override
    @Transactional
    public MessageDto patchMessage(MessagePatchRequestDto dto, UUID messageId) {

        if (!checkAuthor(messageId)) throw new InvalidAccessException();
        Message message = messageRepository.findById(messageId).orElseThrow(()->new MessageNotExistException(messageId));
        message.setContent(dto.newContent()==null?message.getContent():dto.newContent());
        messageRepository.save(message);
        log.debug("updated message : {} ",message);
        return messageMapper.toDto(message);
    }

    private boolean checkAuthor(UUID messageId){
        Message message = messageRepository.findById(messageId).orElseThrow(()->new MessageNotExistException(messageId));
        User author = message.getAuthor();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) principal;
        return author.getId().equals(userDetails.getUserDto().id());

    }
}
