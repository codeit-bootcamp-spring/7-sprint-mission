package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_Message;
import com.sprint.mission.discodeit.entity.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceMessageService  extends BaseInterfaceService {
    Res_Message create(Dto_Message dtoMessage, Optional<List<Dto_BinaryContent>> requestDto);
    List<Res_Message> findAllByChannleId(UUID channelID);
//    List<Message> getAllMessageInChannel(UUID channelID);
//    List<Message> getAllMessageOfUser(UUID userID);
    Res_Message find(UUID messageID);   // 모두 읽기수정
    Res_Message updateMessage(Dto_MessageUpdate requestDto);
    void deleteMessage(UUID messageID);   // 삭제
}
