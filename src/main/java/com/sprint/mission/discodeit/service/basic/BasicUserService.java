package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    //싱글톤 구현
    private final static BasicUserService basicUserService = new BasicUserService();

    private BasicUserService(){}

    public static BasicUserService getInstance(){
        return basicUserService;
    }

    //리포지토리
    JCFUserRepository jcfUserRepository = JCFUserRepository.getInstance();

    //유저 추가
    @Override
    public User create(String email, String nickname, String password){
        return jcfUserRepository.save(new User(email, nickname, password));
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return jcfUserRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return jcfUserRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public List<User> findByNickname(String nickname) {
        return jcfUserRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id) {
        return jcfUserRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, String nickname, String password) {
        return jcfUserRepository.update(id,nickname,password);
    }
}
