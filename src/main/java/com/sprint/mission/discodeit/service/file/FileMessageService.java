package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    //싱글톤 구현
    private final static FileMessageService fileMessageService = new FileMessageService();
    private FileMessageService(){}
    public static FileMessageService getInstance(){
        return fileMessageService;
    }

    //레포지토리
    private final FileMessageRepository fileMessageRepository = FileMessageRepository.getInstance();

    //한 유저가 말한 메세지들을 조회
    @Override
    public List<Message> findAllByUser(UUID userId) {
        return fileMessageRepository.findAllByUserId(userId);
    }

    //채널 안의 메세지들을 모두 조회
    @Override
    public List<Message> findAllByChannel(UUID channelId) {
        return fileMessageRepository.findAllByChannelId(channelId);
    }

    //검색한 텍스트가 포함된 메세지를 모두 조회
    @Override
    public List<Message> searchMessagesByContent(String searchText) {
        return fileMessageRepository.findByContentContaining(searchText);
    }

    //메세지 생성
    @Override
    public Message create(Message message) {
        return fileMessageRepository.save(message);
    }
    
    //메세지 수정
    @Override
    public Message update(UUID id, String content) {
        return fileMessageRepository.update(id, content);
    }

    //메세지 삭제
    @Override
    public Message delete(UUID id) {
        return fileMessageRepository.delete(id);
    }
}
