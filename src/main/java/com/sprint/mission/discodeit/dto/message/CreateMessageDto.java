package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageDto {
    private String content;
    private UUID userId;
    private UUID channelid;
    List<CreateBinaryContentDto> createBinaryContentDtos;
}
