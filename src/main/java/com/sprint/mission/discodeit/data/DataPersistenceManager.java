package com.sprint.mission.discodeit.data;

import com.sprint.mission.discodeit.channel.ChannelRepositoryImpl;
import com.sprint.mission.discodeit.common.repository.BaseRepository;
import com.sprint.mission.discodeit.config.enums.DataKey;
import com.sprint.mission.discodeit.message.channel.ChannelMessageRepositoryImpl;
import com.sprint.mission.discodeit.message.direct.DirectMessageRepositoryImpl;
import com.sprint.mission.discodeit.participation.ParticipationRepositoryImpl;
import com.sprint.mission.discodeit.user.UserRepository;
import com.sprint.mission.discodeit.user.UserRepositoryImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataPersistenceManager {
    /**
     * 데이터가 저장될 기본 폴더 경로입니다.
     */
    public static final String ROOT_PATH = "data/";

    private final List<BaseRepository<?,?>> allRepositories;


    @PostConstruct
    public void loadAllData() {
        File dataDir = new File(ROOT_PATH);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        System.out.println("==================================================");
        System.out.println("✅ [DataPersistenceManager] @PostConstruct: 데이터 로드 시작...");

        for (BaseRepository repository : allRepositories) {
            DataKey key = repository.getDataKey();
            if (key != null) {
                System.out.println(" -> " + key.getName() + " 로드 중...");
                Map data = this.loadData(key);
                if (data != null && !data.isEmpty()) {
                    repository.loadDataMap(data);
                }
            }
        }

        UserRepository userRepository = (UserRepository) allRepositories.stream()
                .filter(repo -> repo instanceof UserRepository)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("UserRepository를 찾을 수 없습니다."));

        System.out.println("✅ [DataPersistenceManager] @PostConstruct: 모든 데이터를 로드했습니다.");
        System.out.println("✅ 로드된 리포지토리 수 : " + allRepositories.size());
        System.out.println("==================================================");

        System.out.println("==================================================");
        System.out.println("✅ [DataPersistenceManager] @PostConstruct: 모든 데이터를 로드했습니다.");
        System.out.println("✅ 유저 수 : " + userRepository.getDataMap().size());
        System.out.println("==================================================");
    }
    @PreDestroy
    public void saveAllData() {
        System.out.println("\n==================================================");
        System.out.println("✅ [DataPersistenceManager] @PreDestroy: 데이터 저장 시작...");

        for (BaseRepository repository : allRepositories) {
            DataKey key = repository.getDataKey();
            if (key != null) {
                this.saveData(key, repository.getDataMap());
            }
        }

        System.out.println("✅ [DataPersistenceManager] @PreDestroy: 모든 데이터를 파일에 저장했습니다.");
        System.out.println("==================================================");
    }


    public Map<?, ?> loadData(DataKey dataKey) {
        String fullPath = ROOT_PATH + dataKey.getDescription();
        File file = new File(fullPath);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            Map<?, ?> data = (Map<?, ?>) ois.readObject();
            return data == null ? new HashMap<>() : new HashMap<>(data);
        } catch (FileNotFoundException e) {
            System.err.println("데이터 파일을 찾을 수 없습니다: " + fullPath);
            return new HashMap<>();
        } catch (IOException e) {
            System.err.println("데이터 로딩 중 오류 발생: " + fullPath);
            throw new RuntimeException("데이터를 불러오는 데 실패했습니다.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("데이터를 이해할 수 없습니다. " + dataKey.getName());
            throw new RuntimeException(e);
        }
    }

    public void saveData(DataKey dataKey, Map<?, ?> data) {
        String fullPath = ROOT_PATH + dataKey.getDescription();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            oos.writeObject(data); // Map 객체를 직렬화하여 파일에 쓴다.
            System.out.println("✅ [" + dataKey.getName() + "] 정보를 성공적으로 저장했습니다.");
        } catch (IOException e) {
            System.err.println("데이터 저장 중 오류 발생: " + fullPath);
            throw new RuntimeException("데이터를 저장하는 데 실패했습니다.", e);
        }
    }


}