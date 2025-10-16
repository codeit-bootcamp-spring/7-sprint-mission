package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.LoadService;
import com.sprint.mission.discodeit.service.file.ReadService;
import com.sprint.mission.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class JCFUserRepository implements UserRepository {
    private static final String filename = "users";



    private List<User> users;


    private JCFUserRepository() {
        this.users = new LinkedList<>();

    }


    private static final JCFUserRepository INSTANCE = new JCFUserRepository();



    public static JCFUserRepository getInstance() {
        return INSTANCE;
    }


    @Override
    public User create(String userId, String password, String userName, String userNickname) {
        User user = new User(userId,password,userName,userNickname);
        //목록가지고와서
        users = ReadService.read(filename,User.class);
        //추가하고
        users.add(user);
        //다시저장
        LoadService.load(filename,users);
        return user;
    }

    @Override
    public User read(UUID userId) {
        users = ReadService.read(filename,User.class);
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
        users = ReadService.read(filename,User.class);
        System.out.printf("유저 %d명@@@\n",users.size());
        return users;
    }

    //이게 좀 그런데
    //전부 다 가지고오고 다시 전부 다 넣는게 너무 이상해 ;;
    //특정하나만 가지고 오고 하나만 다시 넣고 싶은데
    @Override
    public User updateName(UUID uuid, String userName) {
        users = ReadService.read(filename,User.class);
        return users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    u.setUserName(userName);
                    LoadService.load(filename,users);
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다" + uuid));
    }

    @Override
    public User updateNickName(UUID uuid, String userNickname) {
        users = ReadService.read(filename,User.class);
        return users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    u.setUserNickname(userNickname);
                    LoadService.load(filename,users);
                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
    }

    // 이건 내가 원하는거
    @Override
    public User update(UUID uuid, Consumer<User> updater) {
       // users = ReadService.read(filename,User.class);
        System.out.println("수정");
        User user = users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst()
                .map(u -> {
                    updater.accept(u);
                    // 여러개 가능하게

                    return u;
                })
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
       // LoadService.load(filename,users);
        return user;
    }

    @Override
    public boolean delete(UUID userId) {
        users = ReadService.read(filename,User.class);
        boolean removed = users.removeIf(u -> u.getId().equals(userId));
        if (removed) {

            LoadService.load(filename, users);
            return true;
        } else {

            System.out.println("고유넘버 없다: " + userId);
            return false;
        }
    }
}
