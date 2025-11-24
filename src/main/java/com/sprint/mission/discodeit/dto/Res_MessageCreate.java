package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;


public record Res_MessageCreate( //all private final
    //@NotBlank(message = "channelID is mandatory")
    UUID channelID,
    //@NotBlank(message = "userID is mandatory")
    UUID userID,
    //@NotBlank(message = "content is mandatory")
    String message
) {
//    public static Res_MessageCreate from(UUID channelID, UUID userID, String message) {
//        return Res_MessageCreate.builder()
//                .channelID(channelID)
//                .channelID(userID)
//                .message(message)
//                .build();
//    }
}
