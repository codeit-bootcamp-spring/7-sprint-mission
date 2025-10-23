package com.sprint.mission.discodeit.io;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.*;

/**
 * 객체를 파일에 저장하고 불러오는 유틸리티 클래스
 * - Java 직렬화(ObjectOuptputStream / ObjectInputStream) 사용
 * - 직렬화/역직렬화를 통해 객체를 파일에 저장 및 로드함
 */
public class ObjectFileStore {

    // 객체를 path에 저장 (상위 디렉토리 없으면 자동 생성)
    public static void save(Object obj, Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path))) {
                oos.writeObject(obj);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("파일 저장 실패: " + path, e);
        }
    }

    // 파일에서 객체를 읽어 반환 (파일이 없으면 null 반환)
    @SuppressWarnings("unchecked")
    public static <T> T load(Path path, Class<T> type) {
        if (!Files.exists(path)) return null;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path))) {
            Object obj = ois.readObject();
            return (T) obj;
        } catch (IOException e) {
            throw new UncheckedIOException("파일 로드 실패: " + path, e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("클래스 로드 실패: " + e.getMessage(), e);
        }
    }
}
