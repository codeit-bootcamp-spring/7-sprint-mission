package com.sprint.mission.discodeit.entity.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.service.UserService;

import java.awt.*;
import java.util.*;
import java.util.List;

public class JCFUserService implements UserService {

    //컬렉션 필드  final
    private final List<User> users;

    //싱글톤 클래스내부에서 마지막에 불변 해서 올려놓고
    private static final JCFUserService INSTANCE = new JCFUserService();



    //생성자 초기화 외부에서 못쓰게
    private JCFUserService() {
        this.users = new LinkedList<>();
    }

    //메인에 호출할 유일 인스턴스 메인에서 쓰려고
    public static JCFUserService getInstance() {
        return INSTANCE;
    }



        @Override
    public void create(String userId,String password,String userName,String userNickname) {
       users.add(new User(userId,password, userName, userNickname)) ;

    }

    @Override
    public void read(UUID userId){
        System.out.println("유저찾기");
        users.stream()
                .filter(u -> u.equals(userId))
                .forEach(System.out::println);
    }


    @Override
    public void readAll() {
        System.out.println("유저 리스트");
                 users.stream()
                 .forEach(u -> u.toString() );
    }

    @Override
    public void update(UUID messageId) {

    }

    @Override
    public void delete(UUID messageId) {
        users.stream() .filter(u -> u.equals(messageId))
                        .toList()
                       .forEach(u-> users.remove(u));
    }
}
