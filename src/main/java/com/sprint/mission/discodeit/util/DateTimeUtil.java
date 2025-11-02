package com.sprint.mission.discodeit.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Instant instant) {
        if(instant == null){
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
                .withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }
}
