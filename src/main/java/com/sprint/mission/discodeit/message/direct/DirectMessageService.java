package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.message.direct.dto.DirectMSGRequestDTO;
import com.sprint.mission.discodeit.message.direct.dto.DirectMSGResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DirectMessageService extends BaseService<DirectMessage, UUID> {


    DirectMSGResponseDTO sendMessage(DirectMSGRequestDTO requestDTO);


    List<DirectMSGResponseDTO> getMessagesByReceiver(UUID receiverId);


    List<DirectMSGResponseDTO> getMessagesBySender(UUID senderId);


    List<DirectMSGResponseDTO> getConversation(UUID userOneId, UUID userTwoId);

    void delAllBySenderId(UUID senderId);

    int getUnreadDirectMessageCount(UUID receiverId);
}