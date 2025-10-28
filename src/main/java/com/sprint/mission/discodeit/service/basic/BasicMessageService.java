package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUsage;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entityElement.MessageElement;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    public Message createMessage(MessageCreateRequestDto messageCreateRequestDto){
        //todo channel id 랑 sender id 관련 안전성 확인, 일단 지금은 안함
        List<Channel> channelList = channelRepository.getAllChannel();

        Message message = messageRepository.saveMessage(Message.builder()
                .content(messageCreateRequestDto.getContent())
                .channelId(messageCreateRequestDto.getChannelId())
                .senderId(messageCreateRequestDto.getSenderId())
                .isMarkDown(messageCreateRequestDto.isMarkDown())
                .attachmentIdList(
                        messageCreateRequestDto.getAttachmentIdList().stream().map(x ->
                                {
                                    BinaryContent binaryContent = BinaryContent.builder()
                                            .binaryContentUsage(BinaryContentUsage.ATTATCHMENT)
                                            .binaryFile(x.getFile())
                                            .build();
                                    return binaryContentRepository.createBinaryContent(binaryContent).getId();
                                }
                                ).collect(Collectors.toCollection(HashSet::new))
                )
                .build());
        Channel channel2 = channelRepository.getChannelById(message.getChannelId()).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        channel2.getMessageIdList().add(message.getId());
        channelRepository.updateChannel(channel2);
        return message;
    }
    public Message readMessage(Message message){
        return messageRepository.getMessage(message).orElseThrow(()->new IllegalArgumentException("Message not found"));
    }
    public List<Message> readAllMessage(){
        return messageRepository.getAllMessage();
    }
    public void deleteMessage(UUID messageId){
        List<BinaryContent> binaryContentList = binaryContentRepository.readAllBinaryContent();
        Message message = messageRepository.getMessageById(messageId).orElseThrow(()->new IllegalArgumentException("Message not found"));
        HashSet<UUID> attatchmentIdList = message.getAttachmentIdList();
        attatchmentIdList.forEach(binaryContentRepository::deleteBinaryContent);
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
    public List<Message> readUpdatedMessage(){
        return messageRepository.getUpdatedMessage();
    }
    @Override
    public List<Message> findallByChannelId(UUID channelId) {
        Channel targetChannel = channelRepository.getChannelById(channelId).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));

        List<Message> messageList = messageRepository.getAllMessage();
        messageList.stream().filter(x->x.getChannelId().equals(channelId)).toList();
        return messageList;
    }


}
