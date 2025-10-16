package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final List<User> userStore = new ArrayList<>();
    private final MessageService messageService;
    private final String USER_FILE;

    public FileUserService(MessageService messageService, String USER_FILE) {
        this.messageService = messageService;
        this.USER_FILE = USER_FILE;
        loadUsersFromFile();
    }

    // 저장하기
    private void saveUsersToFile() {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(userStore);
            System.out.println("✅ 사용자 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.out.println("❌ 사용자 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 불러오기
    private void loadUsersFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            System.out.println("ℹ️ 저장된 사용자 파일이 없어 새로 생성합니다.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            List<User> loaded = (List<User>) ois.readObject();
            userStore.clear();
            userStore.addAll(loaded);
            System.out.println("✅ 사용자 정보를 파일에서 불러왔습니다.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ 사용자 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public void createUser(String userName, String nickName, String email, String phoneNum, String userId, String password) {


        User newUser = new User(userName, nickName, email, phoneNum, userId, password);
        userStore.add(newUser);
        saveUsersToFile();
    }

    @Override
    public User getUserByEmail(String email) {
        User target = userStore.stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public User getUserByPhone(String phoneNum) {
        User target = userStore.stream()
                .filter(u -> phoneNum.equals(u.getPhoneNum()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public User getUserByUserId(String userId) {
        User target = userStore.stream()
                .filter(u -> userId.equals(u.getUserId()))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public List<User> getAllUsers() {
        return userStore;
    }

    @Override
    public User login(String userId, String password) {
        User user = userStore.stream()
                .filter(u -> u.getUserId().equals(userId) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        return user;
    }

    @Override
    public String getUserNickName(UUID id) {
        String nickName = userStore.stream()
                .filter(u -> u.getId().equals(id))
//                .filter(u -> u.getId()==id)
                .findFirst()
                .get().getNickName();

        return nickName;
    }

    @Override
    public void updateUser(User user) {
        for(int i = 0; i < userStore.size(); i++){
            if(userStore.get(i).getId().equals(user.getId())){ //UUID 비교
                //변경될 사용자 정보가 비어있는 경우 이전 정보 저장
                if(user.getUserName() == null) user.setUserName(userStore.get(i).getUserName());
                else if(user.getNickName() == null) user.setNickName(userStore.get(i).getNickName());
                else if(user.getEmail() == null) user.setEmail(userStore.get(i).getEmail());
                else if(user.getPhoneNum() == null) user.setPhoneNum(userStore.get(i).getPhoneNum());
                else if(user.getPassword() == null) user.setPassword(userStore.get(i).getPassword());

                //변경된 사용자 정보로 업데이트
                userStore.set(i, user);
                saveUsersToFile();
                break;
            }
        }
    }

    @Override
    public void deleteUser(UUID id) {
        for(int i = 0; i < userStore.size(); i++){
            if(userStore.get(i).getId().equals(id)){
                messageService.deleteMessagesByUser(userStore.get(i)); //삭제될 유저가 보낸 메시지도 삭제
                userStore.remove(i);
                saveUsersToFile();
                break;
            }
        }
    }
}
