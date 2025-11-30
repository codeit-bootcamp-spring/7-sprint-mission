package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import org.springframework.data.domain.Pageable;
import com.sprint.mission.discodeit.mapper.dto.MessageDto;
import com.sprint.mission.discodeit.page.PageResponseDto;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface InterfaceMessageService {
    MessageDto create(MessageCreateRequest dtoMessage, List<MultipartFile> fileList);
    PageResponseDto<MessageDto> findAllByChannelId(UUID channelID, Pageable pageable);
//    List<Message> getAllMessageInChannel(UUID channelID);
//    List<Message> getAllMessageOfUser(UUID userID);
    MessageDto find(UUID messageID);   // 모두 읽기수정
    MessageDto updateMessage(UUID messageId, Dto_MessageUpdate requestDto);
    void deleteMessage(UUID messageID);   // 삭제
}
