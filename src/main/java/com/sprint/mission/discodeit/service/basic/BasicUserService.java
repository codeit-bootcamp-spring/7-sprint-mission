package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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
    public User create(String email, String nickname, String password){
        return userRepository.save(new User(email, nickname, password));
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
