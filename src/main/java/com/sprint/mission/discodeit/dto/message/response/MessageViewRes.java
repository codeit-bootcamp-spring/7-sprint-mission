package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

import java.util.List;

@Builder
public record MessageViewRes(
        String speaker,     //화자 닉네임
        String content,     //메세지 내용
        List<BinaryContentInfoRes> attachmentDatas,   //첨부파일 내용들
        String createAt,       //메세지 작성 시간
        boolean isModified     //메세지 수정 여부
){
    public static MessageViewRes from(Message message, String speaker, List<BinaryContentInfoRes> attachmentDatas){
        return MessageViewRes.builder()
                .speaker(speaker)
                .content(message.getContent())
                .attachmentDatas(attachmentDatas)
                .createAt(DateTimeUtil.format(message.getCreatedAt()))
                .isModified(!message.getCreatedAt().equals(message.getUpdatedAt()))
                .build();
    }
}
