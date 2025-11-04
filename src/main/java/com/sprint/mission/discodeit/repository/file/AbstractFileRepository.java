package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.Repository;

import java.io.*;
import java.util.*;

public abstract class AbstractFileRepository<T, ID> implements Repository<T, ID> {

    protected abstract String getFilePath();
    protected abstract ID getId(T entity);

    public AbstractFileRepository() {}

    protected Map<ID, T> loadData() {
        File file = new File(getFilePath());

        //파일이 없으면 빈 맵 반환
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream load = new FileInputStream(getFilePath());
             ObjectInputStream ois = new ObjectInputStream(load)){

            return (Map<ID, T>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일이 없거나 불러오기 실패");
            return new HashMap<>();
        }
    }

    protected void saveData(Map<ID, T> data) {
        try (FileOutputStream save = new FileOutputStream(getFilePath());
             ObjectOutputStream oos = new ObjectOutputStream(save);)
        {
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("파일 저장 실패");
        }
    }

    @Override
    public T save(T entity) {
        Map<ID, T> data = loadData();
        ID id = getId(entity);
        data.put(id, entity);
        saveData(data);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(loadData().get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(loadData().values());
    }

    @Override
    public void delete(ID id) {
        Map<ID, T> data = loadData();
        data.remove(id);
        saveData(data);
    }
}
