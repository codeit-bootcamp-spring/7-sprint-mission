package com.sprint.mission.discodeit.dto.auth.response;

public record VerifyCodeRes(
    boolean isVerified,
    String message) {

}
