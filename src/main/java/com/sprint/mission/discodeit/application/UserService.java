package com.sprint.mission.discodeit.application;


import com.sprint.mission.discodeit.application.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.application.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.application.dto.response.UserResponseDto;

import java.io.IOException;
import java.util.List;

//아무리 생각해도 서비스 인터페이스는 불필요한 거 같음
//핵사고널 아키텍쳐를 구현하기 위해서 응용 계층이 프리젠테이션 계층을 의존하는 구조를 만들 순 있지만
//어차피 응용 계층을 갈아끼울 이유는 없기 때문에.
//우선은 만들었으니까 냅두고 나중에 지워야겠다.
//우선은 가이드를 조금 따라보는 걸로..
public interface UserService{

    //고도화
    UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException;


    // find & findAll
    UserResponseDto getUser(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException;

    //도메인과 관련된 것 모두 삭제
    void delete(UserRequestDto requestDto);
}
