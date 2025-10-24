package com.sprint.mission.discodeit.entity.vaildator;

import java.util.Optional;

public class VaildationUtils {
    public static void vaildateNotNull(Object value){
        Optional.ofNullable(value)
                .orElseThrow(() -> new IllegalArgumentException("null 값은 입력할 수 없습니다."));
    }
}
