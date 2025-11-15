package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.List;
import java.util.UUID;

public record MessageViewRes(
    UUID messageId,
    String speaker,     //화자 닉네임
    String content,     //메세지 내용
    List<BinaryContentInfoRes> attachmentDatas,   //첨부파일 내용들
    String createAt,       //메세지 작성 시간
    boolean isModified     //메세지 수정 여부
) {

  public static MessageViewRes from(
      Message message,
      String speaker,
      List<BinaryContentInfoRes> attachmentDatas) {
    return new MessageViewRes(
        message.getId(),
        speaker,
        message.getContent(),
        attachmentDatas,
        DateTimeUtil.format(message.getCreatedAt()),
        !message.getCreatedAt().equals(message.getUpdatedAt())
    );
  }
}
