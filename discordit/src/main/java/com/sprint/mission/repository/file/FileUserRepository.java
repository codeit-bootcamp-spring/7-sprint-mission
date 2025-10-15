package com.sprint.mission.repository.file;

import com.sprint.mission.config.DataPath;
import com.sprint.mission.entity.User;
import com.sprint.mission.exceptions.UserAlreadyExistsException;
import com.sprint.mission.exceptions.UserNotFoundException;
import com.sprint.mission.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = DataPath.FILE_DIR + "/user.sav";
    private static final FileUserRepository instance = new FileUserRepository();
    private static Map<String, User> data; // 유저 id, User객체 (id검색을 빠르게 하기 위함)


    private FileUserRepository(){
        data = new HashMap<>();
        File file = new File(FILE_PATH);

        // 파일이 없으면 디렉토리 생성 및 빈 데이터로 시작
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            List<User> objects = (List<User>) ois.readObject();
            for (User object : objects) {
                data.put(object.getUserId(), object);
            }
        } catch (FileNotFoundException e) {
            // 파일이 없으면 빈 맵으로 시작
            System.out.println("사용자 파일이 없어 새로 생성합니다.");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 파일 로드 중 오류 발생", e);
        }
    }

    public static FileUserRepository getInstance() {
        return instance;
    }

    @Override
    public void save(User user) {
        if(isExsistId(user.getUserId()))
            throw new UserAlreadyExistsException(user.getUserId());
        data.put(user.getUserId(), user);
        write();
    }

    @Override
    public void update(User user) {
        if(!isExsistId(user.getUserId()))
            throw new UserNotFoundException(user.getUserId());
        data.put(user.getUserId(), user);
        write();
    }


    @Override
    public User findById(String id) {
        if(!isExsistId(id))
            throw new UserNotFoundException(id);
        return data.get(id);
    }

    @Override
    public void deleteById(String id) {
        if(!isExsistId(id))
            throw new UserNotFoundException(id);
        data.remove(id);
        write();
    }

    @Override
    public boolean isExsistId(String id) {
        return data.containsKey(id);
    }

    @Override
    public List<User> findByIds(String... ids) {
        return Arrays.stream(ids)
                .filter(this::isExsistId)
                .map(data::get)
                .sorted(Comparator.comparing(User::getUserId))
                .toList();
    }

    @Override
    public List<User> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(User::getUserId))
                .toList();
    }

    private void write(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(List.copyOf(data.values()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
