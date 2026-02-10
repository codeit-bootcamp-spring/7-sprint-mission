package com.sprint.mission.discodeit.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class GeneratedToken {
    private String token;
    private Instant expiration;
}
