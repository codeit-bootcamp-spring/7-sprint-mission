package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.util.StaticString.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageAttachmentRepository messageAttachmentRepository;
    private final MessageMapper messageMapper;


    @Transactional
    public MessageDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments   ){
        //todo channel id 랑 sender id 관련 안전성 확인, 일단 지금은 안함
        Channel channel2 = channelRepository.findById(messageCreateRequestDto.getChannelId()).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        User targetUser = userRepository.findById(messageCreateRequestDto.getAuthorId()).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
//        if(channel2.getJoinUserList().stream().noneMatch(x->x.equals(messageCreateRequestDto.getAuthorId()))) throw new IllegalArgumentException(USER_NOT_EXIST);

        List<BinaryContent> binaryContentList = new ArrayList<>();
        Message message;
        if(attachments!=null) {
            attachments.forEach(
                    x-> {
                        try {
                           BinaryContent binaryContent = binaryContentRepository.save(BinaryContent.builder()
                                            .fileName(x.getName())
                                            .contentType(x.getContentType())
                                            .size(x.getSize())
                                            .bytes(x.getBytes())
                                    .build()
                            );
                           binaryContentList.add(binaryContent);
                        }
                        catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
        message = messageRepository.save(Message.builder()
                .content(messageCreateRequestDto.getContent())
                .channel(channel2)
                .author(targetUser)
                .attachments(binaryContentList)
                .build());
        binaryContentList.forEach(x->messageAttachmentRepository.save(new MessageAttachment(message, x)));
        return messageMapper.toDto(message);
    }

    public List<MessageDto> readAllMessage(){
        return messageRepository.findAll().stream().map(messageMapper::toDto).toList();
    }
    public void deleteMessage(UUID messageId){
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageDto> findallByChannelId(UUID channelId) {
        return messageRepository.findAll().stream().filter(x -> x.getChannel().equals(channelId)).map(messageMapper::toDto).toList();
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
