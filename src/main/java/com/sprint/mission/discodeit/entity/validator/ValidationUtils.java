package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;

import java.util.Optional;

public class ValidationUtils {
    public static void validateNotNull(Object value){
        Optional.ofNullable(value)
                .orElseThrow(() -> new CustomException(ErrorCode.NULL_INPUT_VALUE));
    }
}
