package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.io.ObjectFileStore;

import java.nio.file.Path;
import java.util.*;

/**
 * - 직렬화 파일(Map) 기반의 공통 베이스 레포지토리
 * 파일 기반 Map 저장소의 추상 클래스
 * - 직렬화/역직렬화를 통해 데이터를 파일에 저장 및 로드함
 */
public abstract class AbstractFileMapRepository<ID, T> {

    protected final Path path;
    protected final Map<ID, T> data;

    /** 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함 */
    protected AbstractFileMapRepository(Path path) {
        this.path = path;
        this.data = load(); // 시작 시 파일에서 로드
    }

    /**
     * 파일에서 데이터를 읽어서 Map으로 로드
     * 파일이 없으면 빈 LinkedHashMap 반환
     */
    @SuppressWarnings("unchecked")
    protected Map<ID, T> load() {
        Map<ID, T> loaded = ObjectFileStore.load(path, Map.class);
        return (loaded != null) ? loaded : new LinkedHashMap<>();
    }

    /** 현재 data를 파일로 저장 */
    protected void persist() {
        ObjectFileStore.save(data, path);
    }
}