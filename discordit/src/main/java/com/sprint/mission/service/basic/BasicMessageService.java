package com.sprint.mission.service.basic;

import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.Receivable;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.repository.file.FileChannelRepository;
import com.sprint.mission.repository.file.FileMessageRepository;
import com.sprint.mission.repository.file.FileUserRepository;
import com.sprint.mission.service.MessageService;

import java.util.List;

public class BasicMessageService implements MessageService {
    private static final UserRepository userRepository;
    private static final ChannelRepository channelRepository;
    private static final MessageRepository messageRepository;
    private static final BasicMessageService instance;

    static {
        // 1. Repository 인스턴스 생성
        userRepository = FileUserRepository.getInstance();
        channelRepository = FileChannelRepository.getInstance();
        messageRepository = FileMessageRepository.getInstance();

        // 2. 순서대로 초기화
        userRepository.init();
        channelRepository.init(userRepository);
        messageRepository.init(userRepository, channelRepository);

        // 3. Service 인스턴스 생성
        instance = new BasicMessageService();
    }

    private BasicMessageService() {
        // static 블록에서 이미 초기화 완료
    }

    public static BasicMessageService getInstance() {
        return instance;
    }

    @Override
    public void sendMessage(User sender, Receivable receiver, String message) {
        messageRepository.save(new Message<>(sender, receiver, message));
    }

    @Override
    public List<Message<Receivable>> getBySender(User sender) {
        return messageRepository.findBySender(sender);
    }

    @Override
    public <T extends Receivable> List<Message<T>> getByReceiver(T receiver) {
        return messageRepository.findByReceiver(receiver);
    }

    @Override
    public <T extends Receivable> List<Message<T>> getBySenderAndReceiver(User sender, T receiver) {
        return messageRepository.findBySenderAndReceiver(sender, receiver);
    }

    /**
     * 테스트용 메서드: 마지막으로 추가된 메시지를 반환합니다.
     *
     * @return 마지막 메시지
     */
    @Override
    public Message<Receivable> getLastMessage() {
        return messageRepository.getLast();
    }
}
