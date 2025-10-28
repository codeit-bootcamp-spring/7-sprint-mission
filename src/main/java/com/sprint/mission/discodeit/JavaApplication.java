package com.sprint.mission.discodeit;

public class JavaApplication {
    public static void main(String[] args) {

        DiscodeitTest discodeitTest = DiscodeitTest.init();
        discodeitTest.run();

        // ===========================================
        // 테스트용 기본 유저 데이터 (테스트/샘플용)
        // ===========================================
        // 이름      | 아이디      | 비밀번호
        // -------------------------------------------1
        // 테스트    | test1234   | test1234
        // 박지훈    | idjihun    | securePass1!
        // 이수빈    | idsubin    | myPassword2@
        // 최하늘    | idhaneul   | skyPass123!
        // 이영희    | idyounghee | pw5678
        // 박민수    | idminsu    | securepw1
        // 최지현    | idjihyun   | mypassword2
        // 오세훈    | idsehun    | hello4321
        // ===========================================
        // 실제 데이터 생성은 TestDataInitializer 클래스에서 수행
        // 기존 File I/O 서비스 사용으로 파일 내 유저 데이터 삭제시 유저 일부가 삭제되었을 수 있다
    }
}
