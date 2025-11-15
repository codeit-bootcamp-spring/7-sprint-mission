package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INTERNAL_SERVER_ERROR("COMMON_001", HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다."),
    FIELD_ACCESS_FAILED("COMMON_002", HttpStatus.INTERNAL_SERVER_ERROR,
            "클래스의 필드 접근에 실패하였습니다."),

    // File
    DATA_DIRECTORY_CREATE_FAILED("FILE_001", HttpStatus.INTERNAL_SERVER_ERROR,
            "데이터 저장용 디렉토리 생성에 실패하였습니다."),

    BACKUP_DIRECTORY_CREATE_FAILED("FILE_002", HttpStatus.INTERNAL_SERVER_ERROR,
            "백업용 디렉토리 생성에 실패하였습니다."),

    FILE_BACKUP_CREATE_FAILED("FILE_003", HttpStatus.INTERNAL_SERVER_ERROR,
            "백업 파일 생성 중 오류가 발생하였습니다."),

    FILE_BACKUP_RESTORE_FAILED("FILE_004", HttpStatus.INTERNAL_SERVER_ERROR,
            "백업 파일을 복원하는 중 오류가 발생하였습니다."),

    FILE_SAVE_FAILED("FILE_005", HttpStatus.INTERNAL_SERVER_ERROR,
            "데이터 파일 저장에 실패하였습니다."),

    FILE_READ_FAILED("FILE_006", HttpStatus.INTERNAL_SERVER_ERROR,
            "데이터 파일을 읽어오는 중 오류가 발생하였습니다."),

    FILE_DELETE_FAILED("FILE_007", HttpStatus.INTERNAL_SERVER_ERROR,
            "데이터 파일 삭제에 실패하였습니다."),

    FILE_READ_ALL_FAILED("FILE_008", HttpStatus.INTERNAL_SERVER_ERROR,
            "모든 데이터 파일을 읽어오는 중 오류가 발생하였습니다."),

    // Auth
    INVALID_NICKNAME("AUTH_001", HttpStatus.NOT_FOUND,
            "해당 닉네임의 유저가 존재하지 않습니다."),
    INVALID_PASSWORD("AUTH_002", HttpStatus.UNAUTHORIZED,
            "비밀번호가 올바르지 않습니다."),
    SIGNUP_EMAIL_INVALID("USER_003", HttpStatus.BAD_REQUEST,
            "회원가입 이메일 형식이 올바르지 않습니다."),
    SIGNUP_NICKNAME_INVALID("USER_004", HttpStatus.BAD_REQUEST,
            "회원가입 닉네임은 2글자 이상이어야 합니다."),
    SIGNUP_PASSWORD_INVALID("USER_005", HttpStatus.BAD_REQUEST,
            "회원가입 비밀번호는 최소 6글자 이상이어야 합니다."),

    // BinaryContent
    BINARYCONTENT_NOT_FOUNT("BINARYCONTENT_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 바이너리 컨텐츠가 존재하지 않습니다."),
    FILE_CONVERSION_FAILED("BINARYCONTENT_002", HttpStatus.INTERNAL_SERVER_ERROR,
            "파일 변환 중 오류가 발생했습니다."),
    INVALID_FILE_REQUEST("BINARYCONTENT_003", HttpStatus.BAD_REQUEST,
            "잘못된 파일 요청입니다."),

    // Channel
    CHANNEL_NOT_FOUND("CHANNEL_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 채널이 존재하지 않습니다."),
    CHANNEL_PRIVATE_CANNOT_MODIFY("CHANNEL_002", HttpStatus.FORBIDDEN,
            "Private 채널은 수정할 수 없습니다."),
    INVALID_CHANNEL_TYPE("CHANNEL_003", HttpStatus.BAD_REQUEST,
            "유효하지 않은 채널 타입입니다."),

    // Message
    MESSAGE_NOT_FOUND("MESSAGE_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 메세지가 존재하지 않습니다."),

    // ReadStatus
    READSTATUS_NOT_FOUND("READSTATUS_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 읽은 상태가 존재하지 않습니다."),

    // User
    USER_NOT_FOUND("USER_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 유저가 존재하지 않습니다."),
    EMAIL_ALREADY_EXISTS( "USER_002", HttpStatus.CONFLICT,
            "이미 존재하는 이메일로 값을 지정할 수 없습니다."),
    NICKNAME_ALREADY_EXISTS( "USER_003", HttpStatus.CONFLICT,
            "이미 존재하는 닉네임으로 값을 지정할 수 없습니다."),

    // UserStatus
    USERSTATUS_NOT_FOUND("USERSTATUS_001", HttpStatus.NOT_FOUND,
            "해당 UUID를 가진 상태 정보가 존재하지 않습니다."),
    USER_STATUS_MISSING("USERSTATUS_001", HttpStatus.NOT_FOUND,
            "해당 유저의 상태 정보가 존재하지 않습니다.")

    ;

    //field
    private final String code;
    private final HttpStatus status;
    private final String message;
}
