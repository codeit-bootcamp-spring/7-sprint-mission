package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BinaryContentDto {

    private UUID id;
    private String fileName;  // UUID.toString()
    private String contentType;  // image/png
    private long size;    // 1024

}
