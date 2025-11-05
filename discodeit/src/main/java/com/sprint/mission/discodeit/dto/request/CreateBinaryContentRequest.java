package com.sprint.mission.discodeit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBinaryContentRequest {
    private String filename;
    private String contentType;
    private byte[] content;
}
