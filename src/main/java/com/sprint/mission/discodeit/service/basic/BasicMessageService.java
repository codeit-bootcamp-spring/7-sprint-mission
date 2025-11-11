package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entityElement.MessageElement;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.service.util.StaticString.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;


    public MessageReadResponseDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments   ){
        //todo channel id 랑 sender id 관련 안전성 확인, 일단 지금은 안함
        List<Channel> channelList = channelRepository.getAllChannel();
        Channel channel2 = channelRepository.getChannelById(messageCreateRequestDto.getChannelId()).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
//        if(channel2.getJoinUserList().stream().noneMatch(x->x.equals(messageCreateRequestDto.getAuthorId()))) throw new IllegalArgumentException(USER_NOT_EXIST);
        Message message;
        if(!attachments.isEmpty()) {
            System.out.println("attachment list");
            message = messageRepository.saveMessage(Message.builder()
                    .content(messageCreateRequestDto.getContent())
                    .channelId(messageCreateRequestDto.getChannelId())
                    .senderId(messageCreateRequestDto.getAuthorId())
                    .attachmentIdList(
                            attachments.stream().map(x ->
                                    {
                                        BinaryContent binaryContent = null;
                                        try {
                                            binaryContent = BinaryContent.builder()
                                                    .fileName(x.getName())
                                                    .bytes(x.getBytes())
                                                    .contentType(x.getContentType())
                                                    .size(x.getSize())
                                                    .build();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return binaryContentRepository.createBinaryContent(binaryContent).getId();
                                    }
                            ).collect(Collectors.toCollection(HashSet::new))
                    )
                    .build());
        }
        else {
            message = messageRepository.saveMessage(
                    Message.builder()
                            .content(messageCreateRequestDto.getContent())
                    .channelId(messageCreateRequestDto.getChannelId())
                            .attachmentIdList(new HashSet<>())
                    .build()
            );
        }

        channel2.getMessageIdList().add(message.getId());
        channelRepository.updateChannel(channel2);
        return MessageReadResponseDto.from(message);
    }
    public MessageReadResponseDto readMessage(Message message){
        Message message1 = messageRepository.getMessage(message).orElseThrow(()->new IllegalArgumentException("Message not found"));
        return MessageReadResponseDto.from(message1);
    }
    public List<MessageReadResponseDto> readAllMessage(){
        messageRepository.getAllMessage().forEach(x-> System.out.println(x.getContent()+": "+x.getId()));
        return messageRepository.getAllMessage().stream().map(MessageReadResponseDto::from).toList();
    }
    public void deleteMessage(UUID messageId){
        List<BinaryContent> binaryContentList = binaryContentRepository.readAllBinaryContent();
        Message message = messageRepository.getMessageById(messageId).orElseThrow(()->new IllegalArgumentException("Message not found"));
        if(message.getAttachmentIdList()!=null) {
            HashSet<UUID> attatchmentIdList = message.getAttachmentIdList();
            attatchmentIdList.forEach(binaryContentRepository::deleteBinaryContent);
        }
        Channel channel = channelRepository.getChannelById(message.getChannelId()).orElseThrow(() -> new IllegalArgumentException("Channel not found"));
        channel.getMessageIdList().remove(messageId);
        channelRepository.updateChannel(channel);
        messageRepository.deleteMessage(message);

    }
    public <T>void updateMessage(MessageUpdateRequestDto<T> messageUpdateRequestDto){
        MessageElement messageElement = messageUpdateRequestDto.getType();
        Message message = messageRepository.getMessageById(messageUpdateRequestDto.getMessageId()).orElseThrow(()->new IllegalArgumentException("Message not found"));
        BiConsumer<Message ,T> biConsumer = (BiConsumer<Message, T>) messageElement.setter;
        biConsumer.accept(message, messageUpdateRequestDto.getUpdateValue());
        message.updateEntity();
        messageRepository.updateMessage(message);

    }
    public List<MessageReadResponseDto> readUpdatedMessage(){

        return messageRepository.getUpdatedMessage().stream().map(MessageReadResponseDto::from).toList();
    }
    @Override
    public List<MessageReadResponseDto> findallByChannelId(UUID channelId) {
        Channel targetChannel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        List<Message> messageList = messageRepository.getAllMessage();
        return messageList.stream().filter(x -> x.getChannelId().equals(channelId)).map(MessageReadResponseDto::from).toList();
    }

    @Override
    public List<MessageReadResponseDto> readAllMessageByUserId(UUID userId) {
        messageRepository.getAllMessage().forEach(x-> System.out.println(x.getContent()+": "+x.getId()));
        return messageRepository.getAllMessage().stream().filter(x->x.getSenderId().equals(userId)).map(MessageReadResponseDto::from).toList();
    }

    @Override
    public void resetMessage() {
        messageRepository.getAllMessage().forEach(x-> deleteMessage(x.getId()));
    }

    @Override
    public MessageReadResponseDto patchMessage(MessagePatchRequestDto dto, UUID messageId) {
        Message message = messageRepository.getMessageById(messageId).orElseThrow(()->new IllegalArgumentException("Message not found"));
        message.setContent(dto.newContent());
        message.updateEntity();
        messageRepository.updateMessage(message);
        return MessageReadResponseDto.from(message);
    }
}
