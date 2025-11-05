package com.sprint.mission.discodeit.common.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    // 1. 포맷터를 미리 상수로 만들어 둔다. (애플리케이션 실행 시 한 번만 생성)
    private static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    /**
     * long 타입의 시간을 "yyyy-MM-dd HH:mm:ss" 형식의 문자열로 변환합니다.
     */
    public static String formatMillis(Instant millis) {
        // 2. 필요할 때마다 미리 만들어 둔 포맷터를 가져다 쓰기만 하면 된다.
        return YYYY_MM_DD_HH_MM_SS.format(millis);
    }
}
