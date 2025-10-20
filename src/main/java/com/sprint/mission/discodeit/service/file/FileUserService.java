package com.sprint.mission.discodeit.service.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final List<User> data = new ArrayList<>();
    private static final FileUserService singleton = new FileUserService();

    //로그인을 구현하지 않았으므로 임의로 생성해둔 것이다.
    public static final User loginUser = new User("로그저", "LoUser@naver.com", "ejbd2");

    private FileUserService() {}

    public static FileUserService getInstance() {
        return singleton;
    }


    
    //회원 등록
    @Override
    public User insert(User user) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream("./test.txt");
        data.add(user);
        return user;
    }

    //다건 조회
    @Override
    public List<User> findAll() {
        return List.copyOf(data);
    }
    
    //단건 조회
    @Override
    public User findById(UUID id) {
        return data.stream().filter(
                u -> u.getId().equals(id)).findFirst().orElseThrow(
                        () -> new RuntimeException("해당 ID를 가진 User를 찾을 수 없습니다: " + id)
        );
    }

    //이메일로 조회
    @Override
    public User findByEmail(String email) {
        return data.stream()
                .filter(u -> u.getNickname().equals(email))
                .findFirst().orElseThrow(
                        () -> new RuntimeException("해당 이메일의 User가 없음: "+ email)
        );
    }

    //업데이트
    @Override
    public User update(UUID id,
                             String nickname,
                             String password) {
        User user = findById(id);
        user.setNickname(nickname);
        user.setPassword(password);
        user.setUpdatedAt(System.currentTimeMillis());

        return user;
    }

    //삭제
    @Override
    public User delete(UUID id) {
        User user = findById(id);
        data.remove(user);
        return user;
    }
}
