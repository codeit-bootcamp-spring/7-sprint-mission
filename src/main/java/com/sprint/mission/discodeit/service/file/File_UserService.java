package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.File_UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class File_UserService implements UserService {
    public static final String FILE_PATH = File_Common.ROOT_PATH + "/File_UserService.ser";
    public File file_UserService = new File(FILE_PATH);
    private File_UserService() {
        File_Common.fileCreate(file_UserService, File_Common.ROOT_PATH);
    }
    private static File_UserService service = new File_UserService();
    static public File_UserService getInstance() { return service; }

    private List<User> userList = new ArrayList<>();
    private File_UserRepository userRepository = new File_UserRepository();

    //===============================
    //========== @Override ==========
    //===============================

    @Override
    public User  creatUser(String name) throws IllegalArgumentException {
        String message = "Creat user: [" + name + "]";

//        List<User> userList = userRepository.getAllUsers();
        for (User user : userList) {
            if (user.getUserName().equals(name)) {
                File_Common.errMessage(message);
                throw new IllegalArgumentException(message);
            }
        }

        // 코드의 흐름이 여기로 왔다는 건, 중복이 발생하지 않았기 때문에 저장해도 되겠다.
        User user = new User(name);
        userList.add(user);
        userRepository.createUser(userList, message);

        return user;
    }

    @Override
    public void getUser(String name) {
        String message = "getUser: [" + name + "]";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));) {

//            Boolean isExist = false;
            List<User> users = (List<User>)ois.readObject();

            for (User user : users) {
                if (user.getUserName().equals(name)) {
//                    isExist = true;
                    File_Common.okMessage(message);
                    return;
                }
            }

            File_Common.errMessage(message);
            return;
        } catch (Exception e) {
            File_Common.errMessage(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAllUsers() {
        for (User user : userList) {
            File_Common.okMessage("getAllUsers : " + user.getUserName());
        }
    }

    @Override
    public void updateUser(String name, String reName) {
        String message = "updateUser: [" + name + "]에서 -> [" + reName + "]으로 변경";

        // service =  update할 user를 찾아야 됨 -> repository한테 userList를 전부 달라 (전체 조회) -> 전달받은 리스트에서 특정 user를 찾기
//        List<User> userList = userRepository.getAllUsers();

        for (User user : userList) {
            if (user.getUserName().equals(name)) {
                int userIndex = userList.indexOf(user);
                user.setUserName(reName);
                userList.set(userIndex, user);

                userRepository.createUser(userList, message);
                return;
            }
        }

        File_Common.errMessage(message);
    }

    @Override
    public void deleteUser(String name) {
        String message = "\uD83C\uDF3C deleteUser: [" + name + "]";

//        List<User> userList = userRepository.getAllUsers();
        for (User user : userList) {
            if (user.getUserName().equals(name)) {
                userList.remove(user);

                File_Common.fileWrite(userList, FILE_PATH, message);
                return;
            }
        }

        File_Common.errMessage(message);
    }
}
