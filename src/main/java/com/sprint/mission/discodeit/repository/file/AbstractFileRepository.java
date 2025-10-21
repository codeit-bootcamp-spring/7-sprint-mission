package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.Repository;

import java.io.*;
import java.util.*;

public abstract class AbstractFileRepository<T, ID> implements Repository<T, ID> {

    protected final Map<ID, T> entityMap;

    protected abstract String getFilePath();
    protected abstract ID getId(T entity);

    public AbstractFileRepository() {
        this.entityMap = loadData();
    }

    private Map<ID, T> loadData() {
        String filePath = getFilePath();
        File file = new File(filePath);

        //파일이 없으면 빈 맵 반환
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileInputStream load = new FileInputStream(getFilePath());
             ObjectInputStream ois = new ObjectInputStream(load)){

            return (Map<ID, T>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일이 없거나 불러오기 실패");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData() {
        try (FileOutputStream save = new FileOutputStream(getFilePath());
             ObjectOutputStream oos = new ObjectOutputStream(save);)
        {
            oos.writeObject(this.entityMap);
        } catch (IOException e) {
            System.out.println("파일 저장 실패");
            e.printStackTrace();
        }
    }

    @Override
    public T save(T entity) {
        ID id = getId(entity);
        entityMap.put(id, entity);
        saveData();
        return entity;
    }

    @Override
    public T findById(ID id) {
        return entityMap.get(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entityMap.values());
    }

    @Override
    public void delete(ID id) {
        entityMap.remove(id);
        saveData();
    }
}
