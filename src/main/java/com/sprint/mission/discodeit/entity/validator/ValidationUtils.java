package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ValidationUtils {
    public static void validateNotNull(Object value, String fieldName){
        Optional.ofNullable(value)
                .orElseThrow(() -> {
                    log.warn("{} 값은 null이 들어올 수 없습니다.", fieldName);
                    throw new CustomException(ErrorCode.NULL_INPUT_VALUE);
                });
    }
}
