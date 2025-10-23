package com.sprint.mission.discodeit.entity.dto.messageDto;

import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Builder
public record MessageInfoDto(UUID id, String authorName, String content, String createdAt, String updatedAt) {

    public static MessageInfoDto from(Message message) {

        return MessageInfoDto.builder()
                .id(message.getId())
                .authorName(message.getAuthor().getUserName())
                .content(message.getContent())
                .createdAt(changeTime(message.getCreatedAt()))
                .updatedAt(changeTime(message.getUpdatedAt()))
                .build();
    }

    @Override
    public String toString() {

        String editSign = createdAt.equals(updatedAt) ? "" : " (수정됨)";
        return authorName + "   " + createdAt + "\n" + content + editSign;
    }
}
