package com.sprint.mission.discodeit.event;

import lombok.Getter;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.util.UUID;

@Getter
public class S3UploadFailedEvent {
    UUID binaryContentId;
    SdkClientException sdkClientException;
    String requestId;

    public S3UploadFailedEvent(UUID binaryContentId, SdkClientException sdkClientException, String requestId) {
        this.binaryContentId = binaryContentId;
        this.sdkClientException = sdkClientException;
        this.requestId = requestId;
    }
}
