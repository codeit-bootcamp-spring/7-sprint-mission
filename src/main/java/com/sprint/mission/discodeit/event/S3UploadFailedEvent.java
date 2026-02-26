package com.sprint.mission.discodeit.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import software.amazon.awssdk.core.exception.SdkClientException;

import java.util.UUID;

@Getter
public class S3UploadFailedEvent extends ApplicationEvent {
    UUID binaryContentId;
    SdkClientException sdkClientException;
    String requestId;

    public S3UploadFailedEvent(Object source, UUID binaryContentId, SdkClientException sdkClientException, String requestId) {
        super(source);
        this.binaryContentId = binaryContentId;
        this.sdkClientException = sdkClientException;
        this.requestId = requestId;
    }
}
