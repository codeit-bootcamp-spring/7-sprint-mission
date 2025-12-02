package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  public static MessageViewRes toResDto(Message message,
      List<BinaryContentInfoRes> attachmentDatas) {
    return new MessageViewRes(
        message.getId(),
        message.getSpeaker().getId(),
        message.getContent(),
        attachmentDatas,
        DateTimeUtil.format(message.getCreatedAt()),
        !message.getCreatedAt().equals(message.getUpdatedAt())
    );
  }
}
