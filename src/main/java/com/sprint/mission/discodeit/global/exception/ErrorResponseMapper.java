package com.sprint.mission.discodeit.global.exception;

import com.sprint.mission.discodeit.global.exception.discodietException.DiscodeitException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ErrorResponseMapper {
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "code", source = "errorCode")
    @Mapping(target = "message", source = "errorCode.message")
    @Mapping(target = "details", source = "details")
    @Mapping(
            target = "exceptionType",
            expression = "java(e.getClass().getSimpleName())"
    )
    @Mapping(
            target = "status",
            expression = "java(e.getErrorCode().getStatus().value())"
    )
    ErrorResponse toErrorResponse(DiscodeitException e);

    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    @Mapping(target = "code", source = "errorCode")
    @Mapping(target = "message", source = "errorCode.message")
    @Mapping(target = "details", source = "details")
    @Mapping(
            target = "exceptionType",
            expression = "java(e.getClass().getSimpleName())"
    )
    @Mapping(
            target = "status",
            expression = "java(errorCode.getStatus().value())"
    )
    ErrorResponse toErrorResponse(
            Exception e,
            ErrorCode errorCode,
            Map<String, Object> details
    );

}
