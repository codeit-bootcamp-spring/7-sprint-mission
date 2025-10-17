package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
//    Message createMessage(String msg);   // 안써!                // 생성// 읽기
    void getAll_Messages(Channel channel);
    void get_Message(Channel channel, UUID messageID);   // 모두 읽기
    void update_Message(Channel channel, UUID messageID, String message);  // 수정
    void delete_Message(Channel channel, UUID messageID);   // 삭제
}
