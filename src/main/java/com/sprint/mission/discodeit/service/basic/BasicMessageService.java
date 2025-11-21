package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


    public MessageReadResponseDto createMessage(MessageCreateRequestDto messageCreateRequestDto, List<MultipartFile> attachments   ){
        //todo channel id 랑 sender id 관련 안전성 확인, 일단 지금은 안함
        Channel channel2 = channelRepository.findById(messageCreateRequestDto.getChannelId()).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        User targetUser = userRepository.findById(messageCreateRequestDto.getAuthorId()).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
//        if(channel2.getJoinUserList().stream().noneMatch(x->x.equals(messageCreateRequestDto.getAuthorId()))) throw new IllegalArgumentException(USER_NOT_EXIST);
        Message message;

            message = messageRepository.save(Message.builder()
                    .content(messageCreateRequestDto.getContent())
                    .channel(channel2)
                    .user(targetUser)
                    .build());

        return MessageReadResponseDto.from(message);
    }

    public List<MessageReadResponseDto> readAllMessage(){
        return messageRepository.findAll().stream().map(MessageReadResponseDto::from).toList();
    }
    public void deleteMessage(UUID messageId){
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageReadResponseDto> findallByChannelId(UUID channelId) {
        Channel targetChannel = channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException(CHANNEL_NOT_EXIST));
        return messageRepository.findAll().stream().filter(x -> x.getChannel().equals(channelId)).map(MessageReadResponseDto::from).toList();
    }

    @Override
    public List<MessageReadResponseDto> readAllMessageByUserId(UUID userId) {
        return messageRepository.findAll().stream().filter(x->x.getUser().getId().equals(userId)).map(MessageReadResponseDto::from).toList();
    }

    @Override
    public void resetMessage() {
        messageRepository.deleteAll();
    }

    @Override
    public MessageReadResponseDto patchMessage(MessagePatchRequestDto dto, UUID messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(()->new IllegalArgumentException("Message not found"));
        message.setContent(dto.newContent()==null?message.getContent():dto.newContent());
        messageRepository.save(message);
        return MessageReadResponseDto.from(message);
    }
}
