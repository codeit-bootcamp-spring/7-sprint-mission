package com.sprint.mission.discodeit.global.utils;

import java.io.*;
import java.util.Map;
import java.util.UUID;

public class FileIOHandler {

    // 저장하기
    public static <T> void saveToFile(String filePath, Map<UUID, T> store) {
        // FileOutputStream은 기본적으로 덮어쓰기를 진행한다.
        // append 파라미터를 true로 변경해야 이어쓰기가 가능하다.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(store);
            System.out.printf("✅ %s 정보가 파일에 저장되었습니다.\n", extractType(filePath));
        } catch (IOException e) {
            System.out.println("❌ 정보 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 불러오기
    public static <T> void loadFromFile(String filePath, Map<UUID, T> store) {
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
            System.out.println("❌ 정보 불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    private static String extractType(String path){
        int count = (int) path.chars().filter(c -> c == '/').count();
        return path.split("/")[count].split("\\.")[0];
    }
}
