package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

  // User 관련 에러
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "유저를 찾을 수 없습니다."),
  DUPLICATE_USERNAME(HttpStatus.CONFLICT, "U002", "이미 존재하는 유저 이름입니다."),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U003", "이미 존재하는 이메일입니다."),

  // Channel 관련 에러
  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "채널을 찾을 수 없습니다."),
  DUPLICATE_CHANNEL(HttpStatus.CONFLICT, "C002", "이미 존재하는 채널입니다."),
  PRIVATE_CHANNEL_UPDATE(HttpStatus.FORBIDDEN, "C003", "private 채널은 수정할 수 없습니다."),

  // Message 관련 에러
  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "메시지를 찾을 수 없습니다."),

  // Auth 로그인 관련
  LOGIN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A001", "아이디 또는 비밀번호가 일치하지 않습니다."),

  // ReadStatus 관련
  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "읽음 상태를 찾을 수 없습니다."),
  DUPLICATE_READ_STATUS(HttpStatus.CONFLICT, "R002", "이미 읽음 상태입니다."),

  // BinaryContent 관련
  BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "binaryContent 찾을 수 없습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F002", "파일을 찾을 수 없습니다."),
  REPO_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "F003", "저장소 초기화 실패했습니다."),
  FILE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "F004", "파일 저장을 실패했습니다."),
  GET_FILE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "F005", "파일을 가져오는데 실패했습니다.");

  private final HttpStatus status;
  private final String code;
  private final String message;

  ErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
