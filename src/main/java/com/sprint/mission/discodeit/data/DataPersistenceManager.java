package com.sprint.mission.discodeit.data;

import com.sprint.mission.discodeit.channel.ChannelRepositoryImpl;
import com.sprint.mission.discodeit.message.channel.ChannelMessageRepositoryImpl;
import com.sprint.mission.discodeit.message.direct.DirectMessageRepositoryImpl;
import com.sprint.mission.discodeit.participation.ParticipationRepositoryImpl;
import com.sprint.mission.discodeit.user.UserRepositoryImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataPersistenceManager {
    /**
     * 데이터가 저장될 기본 폴더 경로입니다.
     */
    public static final String ROOT_PATH = "data/";

    private final UserRepositoryImpl userRepository;
    private final ChannelRepositoryImpl channelRepository;
    private final ParticipationRepositoryImpl participationRepository;
    private final ChannelMessageRepositoryImpl channelMessageRepository;
    private final DirectMessageRepositoryImpl directMessageRepository;


    @PostConstruct
    public void loadAllData() {
        File dataDir = new File(ROOT_PATH);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        userRepository.loadDataMap(this.loadData(DataKey.USER));
        channelRepository.loadDataMap(this.loadData(DataKey.CHANNEL));
        participationRepository.loadDataMap(this.loadData(DataKey.PARTICIPATION));
        channelMessageRepository.loadDataMap(this.loadData(DataKey.CHANNEL_MESSAGE));
        directMessageRepository.loadDataMap(this.loadData(DataKey.DIRECT_MESSAGE));

        System.out.println("==================================================");
        System.out.println("✅ [DataPersistenceManager] @PostConstruct: 모든 데이터를 로드했습니다.");
        System.out.println("✅ 유저 수 : " + userRepository.getDataMap().size());
        System.out.println("==================================================");
    }
    @PreDestroy
    public void saveAllData() {
        this.saveData(DataKey.USER, userRepository.getDataMap());
        this.saveData(DataKey.CHANNEL, channelRepository.getDataMap());
        this.saveData(DataKey.PARTICIPATION, participationRepository.getDataMap());
        this.saveData(DataKey.CHANNEL_MESSAGE, channelMessageRepository.getDataMap());
        this.saveData(DataKey.DIRECT_MESSAGE, directMessageRepository.getDataMap());

        System.out.println("\n==================================================");
        System.out.println("✅ [DataPersistenceManager] @PreDestroy: 모든 데이터를 파일에 저장했습니다.");
        System.out.println("==================================================");
    }


    /**
     * 파일로부터 Map 데이터를 불러옵니다. (역직렬화)
     *
     * @param dataKey 파일 정보를 담고 있는 Enum (예: USERS, CHANNELS)
     * @param <ID>    Map의 Key 타입 (제네릭)
     * @param <T>     Map의 Value 타입 (제네릭)
     * @return 파일에서 불러온 데이터 Map. 파일이 없으면 비어있는 HashMap을 반환합니다.
     */
    public <ID, T> Map<ID, T> loadData(DataKey dataKey) {
        String fullPath = ROOT_PATH + dataKey.getDescription();
        File file = new File(fullPath);

        if (!file.exists()) {
            // 파일이 없으면 비어있는 HashMap을 반환합니다.
            return new HashMap<>();
        }

        // try-with-resources 구문을 사용하여 스트림을 자동으로 닫아줍니다.
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // @SuppressWarnings로 안전한 형변환임을 명시합니다.
            @SuppressWarnings("unchecked")
            Map<ID, T> data = (Map<ID, T>) ois.readObject();
            return data == null ? new HashMap<>() : new HashMap<>(data);
        } catch (FileNotFoundException e) {
            // 이 경우는 위에서 file.exists()로 확인했으므로 거의 발생하지 않습니다.
            System.err.println("데이터 파일을 찾을 수 없습니다: " + fullPath);
            return new HashMap<>();
        } catch (IOException e) {
            // 파일이 손상되었거나, 클래스 구조가 변경되었을 때 발생할 수 있습니다.
            System.err.println("데이터 로딩 중 오류 발생: " + fullPath);
            throw new RuntimeException("데이터를 불러오는 데 실패했습니다.", e);
        } catch (ClassNotFoundException e) {
            System.err.println("데이터를 이해할 수 없습니다. " + dataKey.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Map 데이터를 파일에 저장합니다. (직렬화)
     *
     * @param dataKey 저장할 파일 정보를 담고 있는 Enum
     * @param data    저장할 데이터 Map
     * @param <ID>    Map의 Key 타입 (제네릭)
     * @param <T>     Map의 Value 타입 (제네릭)
     */
    public <ID, T> void saveData(DataKey dataKey, Map<ID, T> data) {
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