package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
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
        User newUser = userRepository.save(req.to());

        //Todo : UserStatus인터페이스 구현 이후 저장 예정 일단 요구사항에 따라 만들기만 함.
        // - 요구사항 : 인터페이스 아직 구현 X, UserStatus 생성
        new UserStatus(newUser.getId());

        return newUser;
    }

    //이메일 중복 검사
    @Override
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복 검사
    @Override
    public boolean existsByNickname(String nickname){
        return userRepository.existsByNickname(nickname);
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
    public List<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id) {
        return userRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, String nickname, String password) {
        return userRepository.update(id,nickname,password);
    }
}
