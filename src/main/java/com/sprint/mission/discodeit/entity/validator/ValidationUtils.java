package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.common.NullInputValueException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ValidationUtils {
    public static void validateNotNull(Object value, String fieldName){
        Optional.ofNullable(value)
                .orElseThrow(() -> {
                    throw new NullInputValueException(ErrorCode.NULL_INPUT_VALUE);
                });
    }
}
