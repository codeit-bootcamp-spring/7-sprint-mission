package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.io.ObjectFileStore;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * - 직렬화 파일(Map) 기반의 공통 베이스 레포지토리
 * 파일 기반 Map 저장소의 추상 클래스
 * - 직렬화/역직렬화를 통해 데이터를 파일에 저장 및 로드함
 * ✅ 파일 기반 Map 저장소의 공통 로직을 정의한 추상 클래스
 * - JCF(Map) 기반과 동일한 구조를 가지되, 파일 입출력 기능만 추가됨
 * - 모든 FileRepository가 이를 상속받아 동작
 */
public abstract class AbstractFileMapRepository<ID, T> {

    protected final Path path;  // 저장 파일 경로
    protected final Map<ID, T> data;    // 메모리상 데이터 캐시

    /** 생성자: 파일 경로를 받아서, 기존 데이터가 있으면 로드함 */
    protected AbstractFileMapRepository(Path path) {
        this.path = path;
        this.data = load(); // 시작 시 파일에서 로드 (초기 로드)
    }

    /**
     * 파일에서 Map 데이터를 읽어오는 함수
     * - 존재하지 않으면 새 Map 생성
     * 파일이 없으면 빈 LinkedHashMap 반환
     */
    @SuppressWarnings("unchecked")
    protected Map<ID, T> load() {
        Map<ID, T> loaded = ObjectFileStore.load(path, Map.class);
        return (loaded != null) ? loaded : new LinkedHashMap<>();
    }

    /** 파일을 임시 파일에 먼저 저장한 뒤 move(원자적 치환) — 반쯤 쓰인 파일로 망가지는 일 방지
     *  데이터를 안전하게 파일에 저장하는 함수
     *  - .tmp 임시파일로 먼저 쓰고, ATOMIC_MOVE로 치환 (원자적 업데이트)
     *  - 저장 중 전원 꺼짐 등의 문제에도 파일 손상 방지
     */
    protected void persist() {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            Path tmp = path.resolveSibling(path.getFileName() + ".tmp");
            ObjectFileStore.save(data, tmp);
            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장(원자적 치환) 실패: " + path, e);
        }
    }

}