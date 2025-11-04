package com.sprint.mission.discodeit.dto.update;

import java.util.UUID;

public record UpdateUserCommand (
        String username, // 유저 이름
        String nickName, // 유저 닉네임
        String email, // 이메일

        //프로필파일
        byte[] data, // 데이터 (byte)
        String fileName, //파일 이름
        String fileType //파일 타입
) {}

