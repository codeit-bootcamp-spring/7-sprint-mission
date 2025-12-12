package com.sprint.mission.discodeit.exception;

public enum ErrorCode {

    USER_NOT_EXIST("User Not Found"),
    DUPLICATE_USER("Duplicate User"),
    PRIVATE_CHANNEL_UPDATE("Private Channel Update Not Allowed"),
    CHANNEL_NOT_EXIST("Channel Not Exists"),
    USER_NOT_JOIN("User Not Joined"),
    FILE_WRITE_FAIL("FILE WRITE FAIL"),
    FILE_READ_FAIL("FILE READ FAIL"),
    FILE_DIRECTORY_CREATE_FAIL("FILE DIRECTORY CREATE FAIL"),
    FILE_BYTE_READ_FAIL("FILE BYTE READ FAIL"),
    FILE_NOT_EXIST("FILE NOT EXIST"),
    MESSAGE_NOT_EXIST("Message Not Exists"),
    USERSTATUS_NOT_EXIST("USER STATUS NOT EXIST"),
    READSTATUS_NOT_EXIST("READ STATUS NOT EXIST"),
    USERSTATUS_NOT_MATCH("USER STATUS NOT MATCH WITH USER");

    String message;
    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
