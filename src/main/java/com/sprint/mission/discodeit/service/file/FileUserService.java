package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    //싱글톤 구현
    private final static FileUserService fileUserService = new FileUserService();

    private FileUserService(){}

    public static FileUserService getInstance(){
        return fileUserService;
    }

    //리포지토리
    private final FileUserRepository fileUserRepository = FileUserRepository.getInstance();

    //유저 추가
    @Override
    public User create(String email, String nickname, String password){
        return fileUserRepository.save(new User(email, nickname, password));
    }

    //유저 목록
    @Override
    public List<User> findAll(){
        return fileUserRepository.findAll();
    }

    //이메일 찾기
    @Override
    public User findByEmail(String email) {
        return fileUserRepository.findByEmail(email);
    }

    //닉네임으로 찾기
    @Override
    public List<User> findByNickname(String nickname) {
        return fileUserRepository.findByNickname(nickname);
    }

    //삭제
    @Override
    public User delete(UUID id) {
        return fileUserRepository.delete(id);
    }

    //업데이트
    @Override
    public User update(UUID id, String nickname, String password) {
        return fileUserRepository.update(id,nickname,password);
    }
}
