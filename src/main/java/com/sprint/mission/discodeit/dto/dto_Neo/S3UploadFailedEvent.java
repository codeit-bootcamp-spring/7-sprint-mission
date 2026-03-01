package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class S3UploadFailedEvent {
    UUID requestId;
    String errorMessage;
}
