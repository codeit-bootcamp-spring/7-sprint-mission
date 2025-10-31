package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.response.UserInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {
    //리포지토리
    private final UserRepository userRepository;

    //유저 추가
    @Override
    public User create(UserCreateReq req){
        validateDuplicate(req.email(), req.nickname());
        return userRepository.save(req.to());
    }

    //중복 검사
    private void validateDuplicate(String email, String nickname){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("Email is already registered");
        }
        if(userRepository.existsByNickname(nickname)){
            throw new RuntimeException("Nickname is already registered");
        }
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return userRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id, UUID profileId, UUID userStatusId) {
        return userRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, UserUpdateReq req) {
        validateDuplicate(req.email(), req.nickname());
        return userRepository.update(id,req.email(), req.nickname(),req.password());
    }
}
