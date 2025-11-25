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
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseBasicMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.subTable.MessageAttachment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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



    @Transactional
    public MessageDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments   ) throws IOException {
        //todo channel id 랑 sender id 관련 안전성 확인, 일단 지금은 안함
        Channel channel2 = channelRepository.findById(messageCreateRequestDto.getChannelId()).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        User targetUser = userRepository.findById(messageCreateRequestDto.getAuthorId()).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));

        Message message;
        message = messageRepository.save(Message.builder()
                .content(messageCreateRequestDto.getContent())
                .channel(channel2)
                .author(targetUser)
                .build());

        if(attachments!=null) {
            List<MessageAttachment> messageAttachmentList = new ArrayList<>();
            List<BinaryContent> attachmentList = new ArrayList<>();
            attachments.forEach(
                    x-> {
                        try {
                           BinaryContent binaryContent = binaryContentRepository.save(BinaryContent.builder()
                                            .fileName(x.getName())
                                            .contentType(x.getContentType())
                                            .size(x.getSize())
                                    .build()
                            );
                           attachmentList.add(binaryContent);
                           binaryContentStorage.put(binaryContent.getId(),x.getBytes());
                            MessageAttachment save = messageAttachmentRepository.save(new MessageAttachment(message, binaryContent));
                            messageAttachmentList.add(save);
                        }

                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
            message.setAttachments(attachmentList);
            message.setMessageAttachment(messageAttachmentList);
        }

        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    public List<MessageDto> readAllMessage(){
        return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
    }
    public void deleteMessage(UUID messageId){
        messageRepository.deleteById(messageId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDtoBasic<MessageDto> findallByChannelId(UUID channelId, Pageable pageable) {

        Page<Message> targetPage = messageRepository.findByChannelId(channelId,pageable);
        if(targetPage.isEmpty()) return pageResponseBasicMapper.fromPage(Page.empty());
        Message message = targetPage.getContent().get(0);
        MessageDto messageDto = messageMapper.toDto(message);
        Page<MessageDto> targetPageDto = targetPage.map(messageMapper::toDto);
        return pageResponseBasicMapper.fromPage(targetPageDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<MessageDto> findallByChannelIdWithCursor(UUID channelId, String cursor,Pageable pageable) {
        Slice<Message> targetSlice ;
        if(cursor == null) {
          Instant tempTime = Instant.MIN;
          targetSlice = messageRepository.findByChannelIdAndCreatedAtAfter(channelId,tempTime,pageable);
        }
        else{
            targetSlice = messageRepository.findByChannelIdAndCreatedAtAfter(channelId,Instant.parse(cursor),pageable);
        }

        Object next = targetSlice.getContent().get(targetSlice.getContent().size()-1).getCreatedAt();
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
        Message message = messageRepository.findById(messageId).orElseThrow(()->new IllegalArgumentException("Message not found"));
        message.setContent(dto.newContent()==null?message.getContent():dto.newContent());
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }
}
