package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Res_Message;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceMessageService {
    MessageDto create(MessageCreateRequest dtoMessage, Optional<List<Dto_BinaryContent>> requestDto);
    List<MessageDto> findAllByChannelId(UUID channelID);
//    List<Message> getAllMessageInChannel(UUID channelID);
//    List<Message> getAllMessageOfUser(UUID userID);
    MessageDto find(UUID messageID);   // 모두 읽기수정
    MessageDto updateMessage(UUID messageId, Dto_MessageUpdate requestDto);
    void deleteMessage(UUID messageID);   // 삭제
}
