# 채팅 서비스 프로젝트: 목표 및 요구사항

---

## 🎯 목표

* Git과 GitHub을 통해 프로젝트를 관리할 수 있다.
* 채팅 서비스의 도메인 모델을 설계하고, Java로 구현할 수 있다.
* 인터페이스를 설계하고 구현체를 구현할 수 있다.
* 싱글톤 패턴을 구현할 수 있다.
* Java Collections Framework에 데이터를 생성/수정/삭제할 수 있다.
* Stream API를 통해 JCF의 데이터를 조회할 수 있다.
* **[심화]** 모듈 간 의존 관계를 이해하고 팩토리 패턴을 활용해 의존성을 관리할 수 있다.

---

## 🚀 프로젝트 마일스톤

1.  프로젝트 초기화 (Java, Gradle)
2.  도메인 모델 구현
3.  서비스 인터페이스 설계 및 구현체 구현
4.  각 도메인 모델별 CRUD (JCF/메모리 기반)
5.  의존성 주입

---

## ✅ 기본 요구사항

### 1. 프로젝트 초기화

-   [x] IntelliJ를 통해 다음의 조건으로 Java 프로젝트를 생성합니다.
    -   [x] IntelliJ에서 제공하는 프로젝트 템플릿 중 `Java`를 선택합니다.
    -   [x] 프로젝트의 경로는 스프린트 미션 리포지토리의 경로와 같게 설정합니다.
        * 예: 리포지토리 경로가 `/some/path/1-sprint-mission` 이라면, `Name`은 `1-sprint-mission`, `Location`은 `/some/path`로 설정합니다.
    -   [x] `Create Git Repository` 옵션은 체크하지 않습니다.
    -   [x] Build system은 `Gradle`을 사용하고, Gradle DSL은 `Groovy`를 사용합니다.
    -   [x] `JDK 17`을 선택합니다.
    -   [x] `GroupId`는 `com.sprint.mission`로 설정합니다.
    -   [x] `ArtifactId`는 수정하지 않습니다.
-   [x] `.gitignore`에 IntelliJ 관련 파일(`.idea` 디렉토리)이 형상관리 되지 않도록 추가합니다.
      ```gitignore
      ...
      .idea
      ...
      ```

### 2. 도메인 모델링

-   [ ] 디스코드 서비스를 분석하여 각 도메인 모델에 필요한 정보를 도출하고, Java Class로 구현하세요.
-   [ ] **패키지명**: `com.sprint.mission.discodeit.entity`
-   [ ] **도메인 모델 정의**
    -   [ ] **공통 필드**
        -   `id`: 객체를 식별하기 위한 ID (`UUID` 타입)
        -   `createdAt`, `updatedAt`: 객체 생성 및 수정 시간 (유닉스 타임스탬프, `Long` 타입)
    -   [ ] **`User` 클래스**
    -   [ ] **`Channel` 클래스**
    -   [ ] **`Message` 클래스**
-   [ ] **생성자**
    -   [ ] `id`와 `createdAt`는 생성자 내에서 초기화하세요.
    -   [ ] `id`, `createdAt`, `updatedAt`을 제외한 나머지 필드는 생성자의 파라미터를 통해 초기화하세요.
-   [ ] **메소드**
    -   [ ] 각 필드를 반환하는 Getter 메소드를 정의하세요.
    -   [ ] 필드를 수정하는 `update` 메소드를 정의하세요.

### 3. 서비스 설계 및 구현

-   [ ] 도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
    -   [ ] **인터페이스 패키지명**: `com.sprint.mission.discodeit.service`
    -   [ ] **인터페이스 네이밍 규칙**: `[도메인 모델 이름]Service` (예: `UserService`)
-   [ ] 다음 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
    -   [ ] **클래스 패키지명**: `com.sprint.mission.discodeit.service.jcf`
    -   [ ] **클래스 네이밍 규칙**: `JCF[인터페이스 이름]` (예: `JCFUserService`)
    -   [ ] Java Collections Framework를 활용하여 데이터를 저장할 `final` 필드(`data`)를 선언하고 생성자에서 초기화하세요.
    -   [ ] `data` 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

### 4. 메인 클래스 구현

-   [ ] 메인 메소드가 선언된 `JavaApplication` 클래스를 생성하고, 도메인 별 서비스 구현체를 테스트해보세요.
    -   [ ] 등록
    -   [ ] 조회 (단건, 다건)
    -   [ ] 수정
    -   [ ] 수정된 데이터 조회
    -   [ ] 삭제
    -   [ ] 조회를 통해 삭제되었는지 확인

---

## 💡 심화 요구 사항

### 서비스 간 의존성 주입

-   [ ] 도메인 모델 간 관계를 고려하여 검증 로직을 추가하고, 테스트해보세요.
    -   **힌트**: `Message`를 생성할 때, 연관된 `User`나 `Channel`이 실제로 존재하는지 확인하는 로직을 추가할 수 있습니다.