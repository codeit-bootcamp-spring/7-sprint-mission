package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserCreateReq(String email,
                            String nickname,
                            String password,
                            UUID profileId) {
}
