package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_Message;
import com.sprint.mission.discodeit.repository.InterfaceBinaryContentRepository;
import com.sprint.mission.discodeit.repository.InterfaceChannelRepository;
import com.sprint.mission.discodeit.repository.InterfaceMessageRepository;
import com.sprint.mission.discodeit.repository.InterfaceUserRepository;
import com.sprint.mission.discodeit.service.InterfaceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class MessageService implements InterfaceMessageService {
    private final InterfaceMessageRepository messageRepository;
    private final InterfaceChannelRepository channelRepository;
    private final InterfaceUserRepository userRepository;
    private final InterfaceBinaryContentRepository userContentsRepository;

    @Override
    public Res_Message create(MessageCreateRequest dtoMessage, Optional<List<Dto_BinaryContent>> dtoList) {
        if (dtoMessage.channelId() == null) {
            throw new NoSuchElementException("🚨 Channel를 찾을 수 없음 :: " + dtoMessage.toString());
        }

        if (dtoMessage.authorId() == null) {
          throw new NoSuchElementException("🚨 User를 찾을 수 없음 :: " + dtoMessage.toString());
        }

        List<UUID> attachemntIds = new ArrayList<>();

        if (!dtoList.isEmpty()) {
            for (Dto_BinaryContent dtoBinaryContent : dtoList.get()) {
                BinaryContent binaryContent = new BinaryContent(dtoBinaryContent);
                attachemntIds.add(binaryContent.getId());
                userContentsRepository.save(binaryContent);
            }
        }

        Message newMessage = new Message(dtoMessage, attachemntIds);
        messageRepository.save(newMessage);

        Util.okMessage("💌 MessageService.create.content = [" + newMessage.getMessage() + "] 💬");
        return Res_Message.from(newMessage);
    }

    @Override
    public void deleteMessage(UUID messageID) {
      Message message = messageRepository.findById(messageID).map(model -> (Message)model)
          .orElseThrow(() -> new NoSuchElementException("🚨Message [" + messageID.toString() + "] 를 찾을 수 없음"));

      messageRepository.deleteById(messageID);
      Util.okMessage("deleteMessage = [" + message.getMessage() + "]");
    }

    @Override
    public Res_Message find(UUID messageID) {
        Message message = messageRepository.findById(messageID).orElseThrow(() -> new IllegalArgumentException("🚨MessageService.find.messageID = [" + messageID.toString() + "] 오류"));
        Util.okMessage("MessageService.find = [" + message.getMessage() + "]");
        return Res_Message.from(message);
    }

    @Override
    public List<Res_Message> findAllByChannleId(UUID channelID) {
//        [ ] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId
        List<Message> messages = messageRepository.findAll();
//        for (Message content : messages) {
//            Util.okMessage("findAllByChannleId.channelID = [" + channelID + "][" + content.getMessage() + "]");
//        }

        List<Res_Message> resMessage = messages.stream().filter(msg -> msg.getChannelId().equals(channelID)).map(Res_Message::from).toList();
        resMessage.stream().forEach(message -> Util.okMessage("findAllByChannleId = [" + channelID + "][" + message.content() + "]"));
//        for (Res_Message content : resMessage) {
//            Util.okMessage("findAllByChannleId = [" + channelID + "][" + content.content() + "]");
//        }
        return resMessage;
    }

    @Override
    public Res_Message updateMessage(UUID messageId, Dto_MessageUpdate requestDto) {
        // [ ] DTO를 활용해 파라미터를 그룹화합니다.
        // 수정 대상 객체의 readStatusID 파라미터, 수정할 값 파라미터
        Message message = messageRepository.findById(messageId).stream().findFirst()
                .orElseThrow(() -> new NoSuchElementException("🚨Message [" + messageId.toString() + "]를 찾을 수 없음"));

        message.updateMessage(requestDto.newContent());
        messageRepository.save(message);
        Util.okMessage("updateMessage = [" + message.getMessage() + "]");

        return Res_Message.from(message);
    }
}
