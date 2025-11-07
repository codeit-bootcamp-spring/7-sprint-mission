package com.sprint.mission.discodeit.common.config;

import java.time.Duration;

public class OnlineThreshold {
    private OnlineThreshold() {}
    public static final Duration ONLINE_THRESHOLD = Duration.ofMinutes(5);
}
