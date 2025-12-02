package com.sprint.mission.discodeit.facade.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageFacadeMapper {

  private final BinaryContentService binaryContentService;
  private final UserService userService;

  //변환 메소드
  public MessageViewRes mapToView(@NonNull Message message) {
    List<BinaryContentInfoRes> imgs = message.getAttachments().stream()
        .map(BinaryContentMapper::toResDto).toList();
    return MessageMapper.toResDto(
        message,
        imgs
    );
  }
}
