package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class BasicUserService implements UserService {

    //컬렉션 필드  final  메서드들은 이 데이터를 가지고 만들꺼다
    //왜 리스트였냐 수업중에 조회는,수정은 리스트
    // 추가(중간정도) , 삭제가 빈번하면 링크라들었다
    //마지막에 컴퓨터 좋아지면서 리스트를 많이쓴다 해서 리스트했다
    private final List<User> users;

    //생성자 초기화 외부에서 못쓰게
    private BasicUserService() {
        this.users = new ArrayList<>();
    }

    //싱글톤 클래스내부에서 마지막에 불변 해서 올려놓고
    private static final BasicUserService INSTANCE = new BasicUserService();


    //메인에 호출할 유일 인스턴스 메인에서 쓰려고
    public static BasicUserService getInstance() {
        return INSTANCE;
    }


    @Override
    public User create(String userId,String password,String userName,String userNickname) {
         User user = new User(userId,password,userName,userNickname);
         users.add(user);
         return user;
    }

    @Override
    public User read(UUID userId) {

        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (user == null) {
            System.out.println("해당 유저가 없습니다: " + userId);
        } else {
            System.out.println(user.toString());
        }
        return user;
    }


    @Override
    public List<User> readAll() {
        System.out.printf("유저 %d명@@@\n",users.size());
         return users;
    }

    @Override
    public User updateName(UUID uuid, String userName) {
        return users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    u.setUserName(userName);
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다" + uuid));
    }

    @Override
    public User updateNickName(UUID uuid, String userNickname) {
        return users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    u.setUserNickname(userNickname);
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
    }

    // 이건 내가 원하는거
    @Override
    public User update(UUID uuid, Consumer<User> updater) {
        System.out.println("수정");
         return users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    updater.accept(u);
                    // 여러개 가능하게
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
    }


    public boolean delete(UUID userId) {
        return users.removeIf(u -> u.getId().equals(userId));
    }

}
