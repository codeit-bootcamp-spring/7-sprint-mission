package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.exception.UserNotFoundException;
import com.sprint.mission.discodeit.message.direct.dto.DirectMSGRequestDTO;
import com.sprint.mission.discodeit.message.direct.dto.DirectMSGResponseDTO;
import com.sprint.mission.discodeit.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DirectMessageServiceImpl extends BaseServiceImpl<DirectMessage, UUID, DirectMessageRepository> implements DirectMessageService {// 사용자 존재 여부 확인을 위해 추가
    private final UserService userService;

    public DirectMessageServiceImpl(DirectMessageRepository directMessageRepository, UserService userService) {
        super(directMessageRepository);
        this.userService = userService;
    }

    @Override
    public DirectMSGResponseDTO sendMessage(DirectMSGRequestDTO requestDTO) {
        UUID senderId = requestDTO.senderId();
        UUID receiverId = requestDTO.receiverId();
        String message = requestDTO.message();
        // 1. 비즈니스 규칙 검증: 보내는 사람과 받는 사람이 실제로 존재하는 사용자인지 확인
        if (!userService.existsByIdNonDel(senderId)) {
            throw new UserNotFoundException(senderId);
        }
        if (!userService.existsByIdNonDel(receiverId)) {
            throw new UserNotFoundException(receiverId);
        }

        // 2. 엔티티 생성 위임
        DirectMessage newDirectMessage = DirectMessage.create(senderId, receiverId, message);

        // 3. 데이터 저장
        save(newDirectMessage);
        return DirectMSGResponseDTO.from(newDirectMessage);
    }

    @Override
    public List<DirectMSGResponseDTO> getMessagesByReceiver(UUID receiverId) {
        return repository.findByReceiverId(receiverId).stream()
                .map(DirectMSGResponseDTO::from)
                .toList();
    }

    @Override
    public List<DirectMSGResponseDTO> getMessagesBySender(UUID senderId) {
        return repository.findBySenderId(senderId).stream()
                .map(DirectMSGResponseDTO::from)
                .toList();
    }

    @Override
    public List<DirectMSGResponseDTO> getConversation(UUID userOneId, UUID userTwoId) {
        return repository.findByParticipants(userOneId, userTwoId).stream()
                .map(DirectMSGResponseDTO::from)
                .toList();
    }

    @Override
    public void delAllBySenderId(UUID senderId) {
        if(repository.findBySenderId(senderId).isEmpty()){
            throw new NoSuchElementException("사용자가 보낸 개인 메새지가 없습니다.");
        }
        repository.deleteAllBySenderId(senderId);
    }

    @Override
    public int getUnreadDirectMessageCount(UUID receiverId) {
        return repository.countNotReadDirectMessage(receiverId);
    }


}