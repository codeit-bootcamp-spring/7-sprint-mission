package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class BinaryContentCreatedEvent {
    private UUID binaryContentId;
    private MultipartFile file;
}
