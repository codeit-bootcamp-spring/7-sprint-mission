package com.sprint.mission.discodeit.data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 데이터의 영속성(Persistence)을 관리하는 클래스입니다.
 * 자바 직렬화(Serialization)를 사용하여 Map 객체를 파일에 직접 저장하고 불러옵니다.
 * 이 클래스는 싱글톤(Singleton) 패턴으로 구현되어 프로그램 전체에서 단 하나의 인스턴스만 사용됩니다.
 */
public class DataPersistenceManager {

    /**
     * 데이터가 저장될 기본 폴더 경로입니다.
     */
    public static final String ROOT_PATH = "data/";

    // private static final로 유일한 인스턴스를 클래스 로딩 시점에 생성합니다.
    private static final DataPersistenceManager INSTANCE = new DataPersistenceManager();

    /**
     * private 생성자로 외부에서의 직접적인 인스턴스 생성을 막습니다.
     * 프로그램 시작 시 data 폴더가 없으면 자동으로 생성합니다.
     */
    private DataPersistenceManager() {
        File dataDir = new File(ROOT_PATH);
        if (!dataDir.exists()) {
            dataDir.mkdirs(); // mkdirs()는 상위 폴더까지 모두 생성해줘서 더 안정적입니다.
        }
    }

    /**
     * 프로그램 전체에서 사용될 유일한 DataPersistenceManager 인스턴스를 반환합니다.
     * @return DataPersistenceManager의 싱글톤 인스턴스
     */
    public static DataPersistenceManager getInstance() {
        // 이미 만들어진 유일한 인스턴스를 반환합니다.
        return INSTANCE;
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