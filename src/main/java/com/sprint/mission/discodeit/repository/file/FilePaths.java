package com.sprint.mission.discodeit.repository.file;

import java.nio.file.Path;
/**
 * ✅ 모든 파일 저장 경로를 한 곳에 모아둔 클래스
 *  - 하드코딩된 문자열("data/users.dat" 등)을 여러 클래스에 중복 작성하지 않도록 함
 *  - 오타, 경로 변경 등의 사이드이펙트를 방지
 */
public final class FilePaths {
    private FilePaths() {}  // 인스턴스화 방지
    // 각 도메인별 파일 경로 상수 선언
    public static final Path USERS    = Path.of("data/users.dat");
    public static final Path CHANNELS = Path.of("data/channels.dat");
    public static final Path MESSAGES = Path.of("data/messages.dat");
}