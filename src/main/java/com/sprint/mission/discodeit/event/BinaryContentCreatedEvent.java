package com.sprint.mission.discodeit.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class BinaryContentCreatedEvent {

    private UUID binaryContentId;
    private String fileName;
    private MultipartFile file;

}
