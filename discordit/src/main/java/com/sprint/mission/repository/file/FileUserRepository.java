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
    private static final Map<String, User> data = new HashMap<>(); // 유저 id, User객체 (id검색을 빠르게 하기 위함)

    private FileUserRepository(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            List<User> objects = (List<User>) ois.readObject();
            for (User object : objects) {
                data.put(object.getUserId(), object);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileUserRepository getInstance() {
        return instance;
    }

    @Override
    public void save(User user) {
        if(existsById(user.getUserId()))
            throw new UserAlreadyExistsException(user.getUserId());
        data.put(user.getUserId(), user);
        write();
    }

    @Override
    public void update(User user) {
        if(!existsById(user.getUserId()))
            throw new UserNotFoundException(user.getUserId());
        data.put(user.getUserId(), user);
        write();
    }


    @Override
    public User findById(String id) {
        if(existsById(id))
            throw new UserNotFoundException(id);
        return data.get(id);
    }

    @Override
    public void deleteById(String id) {
        if(!existsById(id))
            throw new UserNotFoundException(id);
        data.remove(id);
        write();
    }

    @Override
    public boolean existsById(String id) {
        return data.containsKey(id);
    }

    @Override
    public List<User> findByIds(String... ids) {
        return Arrays.stream(ids)
                .filter(this::existsById)
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
            oos.writeObject(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
