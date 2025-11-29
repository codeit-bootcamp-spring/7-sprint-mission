package com.sprint.mission.discodeit.facade.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentService binaryContentService;
  private final UserService userService;

  //변환 메소드
  public MessageViewRes mapToView(@NonNull Message message) {
    List<BinaryContentInfoRes> imgs = message.getAttachments().stream()
        .map(BinaryContentInfoRes::from).toList();

    return MessageViewRes.from(
        message,
        imgs
    );
  }
}
