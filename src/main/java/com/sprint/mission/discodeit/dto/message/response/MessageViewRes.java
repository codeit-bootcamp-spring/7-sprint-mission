package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import java.util.List;
import java.util.UUID;

public record MessageViewRes(
    UUID messageId,
    UUID speakerId,     //화자 ID
    String content,     //메세지 내용
    List<BinaryContentInfoRes> attachmentDatas,   //첨부파일 내용들
    String createAt,       //메세지 작성 시간
    boolean isModified     //메세지 수정 여부
) {

}
