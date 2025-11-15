package com.sprint.mission.discodeit.controller.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * SwaggerDocs 전용 ResponseCode 애노테이션 속성(responseCode)에 바로 사용 가능
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseCode {

  //성공
  public static final String OK = "200";
  public static final String CREATED = "201";
  public static final String NO_CONTENT = "204";

  //실패
  public static final String BAD_REQUEST = "400";
  public static final String UNAUTHORIZED = "401";
  public static final String FORBIDDEN = "403";
  public static final String NOT_FOUND = "404";

  public static final String INTERNAL_ERROR = "500";
}