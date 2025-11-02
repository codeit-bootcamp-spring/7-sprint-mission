package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.PrintUtil;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_Message;
import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_Message;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.InterfaceMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor //!! final 필드나 @NonNull 어노테이션이 붙은 필드에 대한 생성자를 자동으로 생성
public class MessageService implements InterfaceMessageService {
    private final FileMessageRepository messageRepository;
    private final FileChannelRepository channelRepository;
    private final FileUserRepository userRepository;
    private final FileBinaryContentRepository userContentsRepository;

    @Override
    public Res_Message create(Dto_Message dtoMessage, Optional<List<Dto_BinaryContent>> dtoList) {
//        [ ] 선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다.
//        [ ] DTO를 활용해 파라미터를 그룹화합니다.
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

        PrintUtil.okMessage("💌 MessageService.create.message = [" + newMessage.getMessage() + "] 💬");
        return Res_Message.from(newMessage);
    }

    @Override
    public Res_Message find(UUID messageID) {
        Message message = messageRepository.findById(messageID).orElseThrow(() -> new IllegalArgumentException("🚨MessageService.find.messageID = [" + messageID + "] 오류"));
        PrintUtil.okMessage("MessageService.find = [" + message.getMessage() + "]");
        return Res_Message.from(message);
    }

    @Override
    public List<Res_Message> findallByChannleId(UUID channelID) {
//        [ ] 특정 Channel의 Message 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findallByChannelId
        List<Message> messages = messageRepository.findAll().orElseThrow(() -> new NoSuchElementException("🚨findallByChannleId 오류"));
//        for (Message message : messages) {
//            PrintUtil.okMessage("findallByChannleId.channelID = [" + channelID + "][" + message.getMessage() + "]");
//        }

        List<Res_Message> resMessage = messages.stream().filter(msg -> msg.getChannelId().equals(channelID)).map(Res_Message::from).toList();
        resMessage.stream().forEach(message -> PrintUtil.okMessage("findallByChannleId = [" + channelID + "][" + message.message() + "]"));
//        for (Res_Message message : resMessage) {
//            PrintUtil.okMessage("findallByChannleId = [" + channelID + "][" + message.message() + "]");
//        }
        return resMessage;
    }

    @Override
    public Res_Message updateMessage(Dto_MessageUpdate requestDto) {
        // [ ] DTO를 활용해 파라미터를 그룹화합니다.
        // 수정 대상 객체의 id 파라미터, 수정할 값 파라미터
        Message message = messageRepository.findById(requestDto.messageID()).stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("🚨updateMessage.messageID() = [" + requestDto.messageID() + "] 에러"));
        message.updateMessage(requestDto.message());
        messageRepository.save(message);
        PrintUtil.okMessage("updateMessage = [" + message.getMessage() + "]");

        return Res_Message.from(message);
    }

    @Override
    public void deleteMessage(UUID messageID) {
//        [ ] 관련된 도메인도 같이 삭제합니다.
//        첨부파일(BinaryContent)
        Message message = messageRepository.findById(messageID).map(model -> (Message)model).orElseThrow(() -> new IllegalArgumentException("🚨deleteMessage id = [" + messageID.toString() + "] 없는 메세지"));
        messageRepository.deleteById(messageID);
        PrintUtil.okMessage("deleteMessage = [" + message.getMessage() + "]");
    }
}
