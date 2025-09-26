package com.sprint.mission.service.jcf;

import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.UserAlreadyExistsException;
import com.sprint.mission.exceptions.UserIdNotFoundException;
import com.sprint.mission.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserService implements UserService {
    private static final Map<String, User> data = new HashMap<>();// 유저 id, User객체 (id검색을 빠르게 하기 위함)

    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public List<User> getUsers(String... ids) {
        return List.of();
    }

    @Override
    public void signIn(User user) {
        if(data.containsKey(user.getUserId()))
            throw new UserAlreadyExistsException(user.getUserId());
        data.put(user.getUserId(), user);
    }

    @Override
    public void deleteUser(String id) {
        validateId(id);
        data.remove(id);
    }

    @Override
    public void updatePasswd(String id, String passwd) {
        validateId(id);
        data.get(id).updatePasswd(passwd);
    }

    @Override
    public void updateBio(String id, String bio) {
        validateId(id);
        data.get(id).updateBio(bio);
    }

    @Override
    public void updateOnlineStatus(String id, User.Status status) {
        validateId(id);
        data.get(id).updateOnlineStatus(status);
    }

    @Override
    public User.Status getOnlineStatus(String id) {
        validateId(id);
        return data.get(id).getOnlineStatus();
    }

    @Override
    public boolean isOnline(String id) {
        validateId(id);
        return data.get(id).getOnlineStatus() == User.Status.ONLINE;
    }

    private static void validateId(String id){
        if (!data.containsKey(id)) {
            throw new UserIdNotFoundException(id);
        }
    }
}
