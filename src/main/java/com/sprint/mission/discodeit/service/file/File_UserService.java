package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class File_UserService implements UserService {
    private static final String FILE_PATH = File_Common.ROOT_PATH + "/File_UserService.txt";
    public File file_UserService = new File(FILE_PATH);
    private List<User> userList = new ArrayList<>();

    private static File_UserService service = new File_UserService();

    private File_UserService() {
        File_Common.fileCreate(file_UserService, File_Common.ROOT_PATH);
    }
    static public File_UserService getInstance() { return service; }


    //===============================
    //========== @Override ==========
    //===============================

    @Override
    public User creatUser(String name) {
        User user = new User(name);
        userList.add(user);

        File_Common.fileWrite(userList, FILE_PATH, "Creat user: [" + name + "]");

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
        String message = "getAllUsers";

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));) {

            List<User> users = (List<User>)ois.readObject();

            for (User user : users) {
                File_Common.okMessage(message + " [" + user + "]");
            }
        } catch (Exception e) {
            File_Common.errMessage(message);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(String name, String reName) {
        String message = "updateUser: [" + name + "]에서 -> [" + reName + "]으로 변경";

        for (User user : userList) {
            if (user.getUserName().equals(name)) {
                int userIndex = userList.indexOf(user);
                user.setUserName(reName);
                userList.set(userIndex, user);

                File_Common.fileWrite(userList, FILE_PATH, message);

                return;
            }
        }

        File_Common.errMessage(message);
    }

    @Override
    public void deleteUser(String name) {
        String message = "\uD83C\uDF3C deleteUser: [" + name + "]";

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
