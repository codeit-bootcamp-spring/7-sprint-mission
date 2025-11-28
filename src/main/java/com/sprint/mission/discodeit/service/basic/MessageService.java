package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.repository.jpa.ChannelsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageAttachmentsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.service.InterfaceMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class MessageService implements InterfaceMessageService {
    private final MessagesRepository messageRepository;
    private final ChannelsRepository channelRepository;
    private final UsersRepository userRepository;
    private final MessageAttachmentsRepository messageAttachmentsRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto create(MessageCreateRequest dtoMessage, List<MultipartFile> fileList) {
        if (dtoMessage.channelId() == null) {
            throw new NoSuchElementException("🚨 Channel를 찾을 수 없음 :: " + dtoMessage.toString());
        }

        if (dtoMessage.authorId() == null) {
          throw new NoSuchElementException("🚨 User를 찾을 수 없음 :: " + dtoMessage.toString());
        }

        Channel channel = channelRepository
            .findById(dtoMessage.channelId())
            .orElseThrow(() -> new IllegalArgumentException("🚨 noChannelId = [" + dtoMessage.channelId().toString() + "]"));

        User user = userRepository
            .findById(dtoMessage.authorId())
            .orElseThrow(() -> new IllegalArgumentException("🚨 noUserId = [" + dtoMessage.authorId().toString() + "]"));

        List<MessageAttachments> attachments = new ArrayList<>();
        Message newMessage = new Message(dtoMessage.content(),
                                        channel,
                                        user,
                                        attachments);


        List<BinaryContent> dtoList = fileList
            .stream()
            .map(file -> {
                BinaryContent binaryContent = null;
                binaryContent = new BinaryContent(
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType(),
                    null
                );

                MessageAttachments messageAttachments = new MessageAttachments(
                    UUID.randomUUID(),
                    newMessage,
                    binaryContent
                );
                attachments.add(messageAttachments);
                messageAttachmentsRepository.save(messageAttachments);

                // 파일 저장 + DB 저장
                binaryContent.setAttachments(messageAttachments);
                return binaryContentStorage.put(file, binaryContent);
            })
            .toList();

        newMessage.setAttachments(attachments);
        messageRepository.save(newMessage);

        log.info("✅ 💌 MessageService.create.content = [" + newMessage.getContent() + "] 💬");
        return messageMapper.toDto(newMessage);
    }

    @Override
    public void deleteMessage(UUID messageID) {
      Message message = messageRepository
          .findById(messageID)
          .map(model -> (Message)model)
          .orElseThrow(() -> new NoSuchElementException("🚨Message [" + messageID.toString() + "] 를 찾을 수 없음"));

      messageRepository.deleteById(messageID);
      log.info("✅ deleteMessage = [" + message.getContent() + "]");
    }

    @Override
    public MessageDto find(UUID messageID) {
        Message message = messageRepository
            .findById(messageID)
            .orElseThrow(() -> new IllegalArgumentException("🚨MessageService.find.messageID = [" + messageID.toString() + "] 오류"));

        log.info("✅ MessageService.find = [" + message.getContent() + "]");
        return messageMapper.toDto(message);
    }

    @Override
//        [ ] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId
    public List<MessageDto> findAllByChannelId(UUID channelID) { //♨️
        List<Message> messageList = messageRepository.findAll();
        List<MessageDto> resMessage = messageList
            .stream()
            .filter(msg -> msg.getChannel().getId().equals(channelID))
            .map(messageMapper::toDto)
            .peek(message -> log.info("✅ findAllByChannelId = [" + channelID.toString() + "][" + message.content() + "]"))
            .toList();
        return resMessage;
    }

    @Override
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
