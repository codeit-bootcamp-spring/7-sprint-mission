package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {
    // ===== 🏗️ Domain Logic (Facade 용)  =====
    //레포지토리
    private final MessageRepository messageRepository;

    //메세지를 id 로 참음
    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElseThrow(()->
                new CustomException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    //한 유저가 말한 메세지들을 조회
    @Override
    public List<Message> findAllByUser(UUID userId) {
        return messageRepository.findAllByUserId(userId);
    }

    //채널 안의 메세지들을 모두 조회
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    //검색한 텍스트가 포함된 메세지를 모두 조회
    @Override
    public List<Message> searchMessagesByContent(String searchText) {
        return messageRepository.findByContentContaining(searchText);
    }

    //채널에서 가장 마지막 메세지를 조회
    @Override
    public Message findLastMessageByChannelId(UUID channelId) {
        Optional<Message> lastMessageOpt = messageRepository.findLastMessageByChannelId(channelId);
        return lastMessageOpt.orElse(null);
    }

    //메세지 생성
    @Override
    public Message create(Message message) {
        return messageRepository.save(message);
    }
    
    //메세지 수정
    @Override
    public void update(UUID id, String content, List<UUID> attachmentIds) {
        if(!messageRepository.existsById(id)){
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        messageRepository.update(id, content, attachmentIds);
    }

    //메세지 삭제
    @Override
    public void delete(UUID id) {
        if(!messageRepository.existsById(id)){
            throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        }
        messageRepository.delete(id);
    }
}
