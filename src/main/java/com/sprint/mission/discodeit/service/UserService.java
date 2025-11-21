package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * UserService 인터페이스
 * - 사용자 생성, 조회, 업데이트, 삭제 기능 제공
 */
public interface UserService {

    /**
     * 새로운 사용자 생성
     */
    UserResponseDto create(CreateUserRequestDto userRequest, CreateBinaryContentRequestDto profileRequest);

    UserResponseDto find(UUID userId);

    /**
     * 이메일로 사용자 조회
     */
    UserResponseDto findByEmail(String email);

    /**
     * 전화번호로 사용자 조회
     */
    UserResponseDto findByPhoneNum(String phoneNum);

    /**
     * 사용자ID로 사용자 조회
     */
    UserResponseDto findByUsername(String username);

    /**
     * 전체 사용자 목록 조회
     */
    List<UserResponseDto> findAll();

    /**
     * UUID로 사용자 닉네임 조회
     */
    String findNickNameById(UUID userId);

    /**
     * 사용자 정보 업데이트
     */
    UserResponseDto update(UUID userId, UpdateUserRequestDto request, CreateBinaryContentRequestDto profileRequest);

    /**
     * 사용자 삭제
     * @param userId 삭제할 User UUID
     */
    void delete(UUID userId);

    UserResponseDto toDto(User user);

    boolean isPasswordMatch(UUID userId, String password);
}
