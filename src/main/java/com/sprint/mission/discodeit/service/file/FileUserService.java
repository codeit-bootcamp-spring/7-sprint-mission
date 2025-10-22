package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private List<User> data;
    private static final FileUserService singleton = new FileUserService();
    private final String filename = "users";

    //로그인을 구현하지 않았으므로 임의로 생성해둔 것이다.
    public static final User loginUser = new User("로그저", "LoUser@naver.com", "ejbd2");

    public static FileUserService getInstance() {
        return singleton;
    }

    //다건 조회
    @Override
    public List<User> findAll() {
        try(ObjectInputStream is = FileInOutUtil.getInputStream(filename);){
            data = (ArrayList)is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    
    //단건 조회
    @Override
    public User findById(UUID id) {
        data = findAll();
        return data.stream().filter(
                u -> u.getId().equals(id)).findFirst().orElseThrow(
                        () -> new RuntimeException("해당 ID를 가진 User를 찾을 수 없습니다: " + id)
        );
    }

    //이메일로 조회
    @Override
    public User findByEmail(String email) {
        data = findAll();
        return data.stream()
                .filter(u -> u.getNickname().equals(email))
                .findFirst().orElseThrow(
                        () -> new RuntimeException("해당 이메일의 User가 없음: "+ email)
        );
    }

    //회원 등록
    @Override
    public User insert(User user) throws FileNotFoundException {
        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            data.add(user);
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
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

        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    //삭제
    @Override
    public User delete(UUID id) {
        User user = findById(id);
        data.remove(user);

        try(ObjectOutputStream os = FileInOutUtil.getOutputStream(filename);){
            data = findAll();
            os.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
