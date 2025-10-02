package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

/*
    CRUD 순서대로 이렇게 생긴 것이다.

    C: create

    R: read, readAll

    U: updateUsername, updateEmail, deactivate

    D: delete
* */


public interface UserService {

    // User 등록 단계
    User create(String username, String email); // 유저 생성 ( 유저네임, 이메일 )

    // 조회
    User read(UUID id); // 유저 조회 ( UUID id ) = 특정 유저 하나만 조회
    List<User> readAll();   // 모든 유저 조회

    // 수정
    User updateUsername(UUID id,  String newUsername); // 유저네임 수정
    User updateEmail(UUID id, String newEmail); // 이메일 수정
    User deactivate(UUID id); // 유저 비활성화

    // 유저 삭제
    boolean delete(UUID id); // 유저 삭제 ( id )



}
