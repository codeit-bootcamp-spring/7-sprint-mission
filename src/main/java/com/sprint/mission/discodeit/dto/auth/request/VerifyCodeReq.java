package com.sprint.mission.discodeit.dto.auth.request;

public record VerifyCodeReq(
    String email,
    String code
) {

}
