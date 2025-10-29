package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    //싱글톤 구현
    private final static BasicMessageService basicMessageService = new BasicMessageService();
    private BasicMessageService(){}
    public static BasicMessageService getInstance(){
        return basicMessageService;
    }

    //레포지토리
    JCFMessageRepository jcfMessageRepository = JCFMessageRepository.getInstance();

    //한 유저가 말한 메세지들을 조회
    @Override
    public List<Message> findAllByUser(UUID userId) {
        return jcfMessageRepository.findAllByUserId(userId);
    }

    //채널 안의 메세지들을 모두 조회
    @Override
    public List<Message> findAllByChannel(UUID channelId) {
        return jcfMessageRepository.findAllByChannelId(channelId);
    }

    //검색한 텍스트가 포함된 메세지를 모두 조회
    @Override
    public List<Message> searchMessagesByContent(String searchText) {
        return jcfMessageRepository.findByContentContaining(searchText);
    }

    //메세지 생성
    @Override
    public Message create(Message message) {
        return jcfMessageRepository.save(message);
    }
    
    //메세지 수정
    @Override
    public Message update(UUID id, String content) {
        return jcfMessageRepository.update(id, content);
    }

    //메세지 삭제
    @Override
    public Message delete(UUID id) {
        return jcfMessageRepository.delete(id);
    }
}
