package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.entity.MessageRoomDTO;


import java.util.UUID;

public interface MessageRoomService {

    //메세지 생성
    void addMessageRoom(MessageRoom messageRoom);

    //메세지 제거
    void removeMessageRoom(MessageRoom messageRoom);

    //메세지 가져오기
    MessageRoom getMessageRoom(UUID id);

    //메세지 수정
    void updateMessageRoom(UUID messageId, MessageRoomDTO messageRoomDTO);
}
