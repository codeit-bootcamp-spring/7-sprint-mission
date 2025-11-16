package com.sprint.mission.discodeit.global.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.UUID;

/*
@UtilityClass 특징
- 1. 상속 방지 : 클래스를 자동으로 final로 만든다
- 2. 인스턴스 생성 방지 : 생성자를 private로 설정
- 3. 모든 필드와 메서드를 static으로 변환 : 객체 없이 사용 가능
- 4. 내부 클래스도 자동 static 처리
 */
@UtilityClass
@Slf4j
public class FileIOHandler {

    // 저장하기
    public <T> void saveToFile(String filePath, Map<UUID, T> store) {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(store);
            System.out.printf("✅ %s 정보가 파일에 저장되었습니다.\n", extractType(filePath));
        } catch (IOException e) {
            log.error("❌ 정보 저장 중 오류 발생: {}", e.getMessage());
        }
    }

    // 불러오기
    public <T> void loadFromFile(String filePath, Map<UUID, T> store) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.printf("ℹ️ 저장된 %s 파일이 없어 저장시 새로 생성합니다.\n", extractType(filePath));
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Map<UUID, T> loaded = (Map<UUID, T>) ois.readObject();
            store.clear();
            store.putAll(loaded);
            System.out.printf("✅ %s 정보를 파일에서 불러왔습니다.\n", extractType(filePath));
        } catch (IOException | ClassNotFoundException e) {
            log.error("❌ 정보 불러오기 중 오류 발생: {}", e.getMessage());
        }
    }

    private String extractType(String path){
        int count = (int) path.chars().filter(c -> c == '/').count();
        return path.split("/")[count].split("\\.")[0];
    }
}
