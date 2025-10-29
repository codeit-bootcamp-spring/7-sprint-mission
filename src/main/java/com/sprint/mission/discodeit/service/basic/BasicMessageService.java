package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BasicMessageService implements MessageService {
    //싱글톤 구현
    private static BasicMessageService instance;

    private BasicMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public static BasicMessageService getInstance(MessageRepository messageRepository){
        if(instance == null){
            instance = new BasicMessageService(messageRepository);
        }
        return instance;
    }

    //레포지토리
    private final MessageRepository messageRepository;

    //한 유저가 말한 메세지들을 조회
    @Override
    public List<Message> findAllByUser(UUID userId) {
        return messageRepository.findAllByUserId(userId);
    }

    //채널 안의 메세지들을 모두 조회
    @Override
    public List<Message> findAllByChannel(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    //검색한 텍스트가 포함된 메세지를 모두 조회
    @Override
    public List<Message> searchMessagesByContent(String searchText) {
        return messageRepository.findByContentContaining(searchText);
    }

    //메세지 생성
    @Override
    public Message create(Message message) {
        return messageRepository.save(message);
    }
    
    //메세지 수정
    @Override
    public Message update(UUID id, String content) {
        return messageRepository.update(id, content);
    }

    //메세지 삭제
    @Override
    public Message delete(UUID id) {
        return messageRepository.delete(id);
    }
}
