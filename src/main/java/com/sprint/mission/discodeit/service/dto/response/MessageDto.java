package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageDto {

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String Content;
    private UUID channelId;
    private UserDto author;
    private final List<BinaryContentDto> attachments = new ArrayList<>();

    public void addAttachment(BinaryContentDto binaryContentDto){
        this.attachments.add(binaryContentDto);
    }
}
