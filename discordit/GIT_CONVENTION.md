# Git 컨벤션 가이드

본 문서는 DiscordIt 프로젝트의 Git 사용 규칙을 정의합니다.

(브랜치 전략은 사용하지 않음)

---

## 📋 목차
1. [커밋 메시지 규칙](#커밋-메시지-규칙)
2. [브랜치 전략](#브랜치-전략)
3. [작업 흐름](#작업-흐름)
4. [주의사항](#주의사항)

---

## 커밋 메시지 규칙

### 기본 형식
```
<type>: <subject>

[optional body]

[optional footer]
```

### Type 종류

| Type | 설명 | 예시 |
|------|------|------|
| `feat` | 새로운 기능 추가 | 사용자 프로필 이미지 업로드 기능 |
| `fix` | 버그 수정 | 메시지 전송 시 NullPointerException 해결 |
| `refactor` | 코드 리팩터링 (기능 변경 없음) | UserService 메서드명 개선 |
| `docs` | 문서 수정 | README 설치 가이드 추가 |
| `style` | 코드 포맷팅, 세미콜론 누락 등 | 들여쓰기 및 공백 정리 |
| `test` | 테스트 코드 추가/수정 | UserService 단위 테스트 추가 |
| `chore` | 빌드, 패키지 매니저 설정 등 | Gradle 의존성 버전 업데이트 |
| `design` | UI/UX 디자인 변경 | 채팅창 레이아웃 개선 |
| `comment` | 주석 추가 및 수정 | BaseEntity 클래스 주석 추가 |
| `rename` | 파일/폴더명 변경 | discodeit 패키지명을 discordit으로 수정 |
| `remove` | 파일/코드 삭제 | 사용하지 않는 Util 클래스 제거 |

### Subject 작성 규칙

1. **50자 이내**로 간결하게 작성
2. **한글** 사용 (현재 프로젝트 스타일 유지)
3. **명령형**으로 작성 ("추가", "수정", "제거")
4. **마침표 없이** 작성
5. **구체적으로** 무엇을 했는지 작성

#### ✅ 좋은 예시
```
feat: User 엔티티에 이메일 필드 추가
fix: Channel 삭제 시 관련 메시지도 함께 삭제되도록 수정
refactor: MessageService에서 중복 코드 제거
```

#### ❌ 나쁜 예시
```
feat: 기능 추가.                    // 너무 모호함, 마침표 사용
fix: bug fix                       // 영어 사용, 구체적이지 않음
refactor: 코드 수정했음             // ~했음 형식, 구체적이지 않음
feat: 사용자가 채널에서 메시지를 보낼 수 있도록 하고 메시지 수신자를 설정하는 기능을 추가함  // 너무 김
```

### Body 작성 규칙 (선택사항)

Subject만으로 설명이 부족할 때 작성합니다.

1. Subject와 **한 줄 띄우고** 작성
2. **"무엇을"**과 **"왜"** 변경했는지 설명
3. **"어떻게"**는 코드를 보면 알 수 있으므로 생략 가능
4. 여러 줄 작성 가능

#### 예시
```
feat: Message 엔티티에 읽음 상태 필드 추가

사용자가 메시지를 읽었는지 확인할 수 있는 기능이 필요하여
isRead 필드와 readAt 필드를 추가했습니다.

- isRead: 메시지 읽음 여부 (Boolean)
- readAt: 메시지를 읽은 시간 (Long, 유닉스 타임스탬프)
```

### Footer 작성 규칙 (선택사항)

이슈 트래커 ID를 참조하거나 Breaking Change를 명시할 때 사용합니다.

#### 예시
```
feat: UserService 인터페이스 메서드 시그니처 변경

BREAKING CHANGE: getUser() 메서드가 Optional<User>를 반환하도록 변경
기존 코드에서 null 체크하던 부분을 Optional 처리로 수정 필요

Resolves: #123
```

---

## 브랜치 전략

### 브랜치 종류

#### 1. `main` 브랜치
- 프로젝트의 **메인 브랜치**
- 안정적이고 완성된 코드만 병합
- 직접 커밋 ❌, Pull Request를 통해서만 병합 ✅

#### 2. `feature/<기능명>` 브랜치
- 새로운 기능을 개발할 때 사용
- `main`에서 분기
- 작업 완료 후 `main`으로 병합

**명명 규칙:**
```
feature/user-profile        // 사용자 프로필 기능
feature/message-search      // 메시지 검색 기능
feature/channel-invite      // 채널 초대 기능
```

#### 3. `fix/<버그명>` 브랜치
- 버그를 수정할 때 사용
- `main`에서 분기
- 수정 완료 후 `main`으로 병합

**명명 규칙:**
```
fix/message-null-error      // 메시지 null 에러 수정
fix/user-duplicate          // 사용자 중복 생성 버그 수정
```

#### 4. `refactor/<작업명>` 브랜치
- 코드 리팩터링 작업을 할 때 사용
- 기능 변경 없이 코드 구조 개선

**명명 규칙:**
```
refactor/user-service       // UserService 리팩터링
refactor/entity-structure   // 엔티티 구조 개선
```

#### 5. 개인 브랜치 (예: `김지예`)
- 현재 사용 중인 개인 이름 브랜치
- 개인 작업용으로 자유롭게 사용 가능

---

## 작업 흐름

### 1. 새로운 기능 개발하기

```bash
# 1. main 브랜치로 이동
git checkout main

# 2. 최신 코드 받아오기
git pull origin main

# 3. 새로운 기능 브랜치 생성
git checkout -b feature/message-reply

# 4. 코드 작성 및 커밋
git add .
git commit -m "feat: Message 엔티티에 답장 기능 추가"

# 5. 추가 작업 후 커밋
git add .
git commit -m "feat: MessageService에 답장 메서드 구현"

# 6. 원격 저장소에 푸시
git push origin feature/message-reply

# 7. GitHub에서 Pull Request 생성

# 8. 리뷰 및 병합 완료 후 브랜치 삭제
git checkout main
git pull origin main
git branch -d feature/message-reply
```

### 2. 버그 수정하기

```bash
# 1. main 브랜치에서 수정 브랜치 생성
git checkout main
git checkout -b fix/channel-delete-error

# 2. 버그 수정 및 커밋
git add .
git commit -m "fix: Channel 삭제 시 NullPointerException 해결"

# 3. 푸시 및 PR 생성
git push origin fix/channel-delete-error
```

### 3. 여러 파일을 수정했을 때

```bash
# 1. 수정된 파일 확인
git userStatus

# 2. 관련된 파일끼리 묶어서 커밋
git add src/main/java/com/sprint/mission/entity/User.java
git commit -m "feat: User 엔티티에 상태 메시지 필드 추가"

git add src/main/java/com/sprint/mission/service/UserService.java
git add src/main/java/com/sprint/mission/service/jcf/JCFUserService.java
git commit -m "feat: UserService에 상태 메시지 업데이트 메서드 추가"
```

---

## 주의사항

### ✅ DO (해야 할 것)

1. **커밋은 자주, 작은 단위로**
   ```bash
   # 좋은 예: 기능별로 나누어 커밋
   git commit -m "feat: User 엔티티 생성"
   git commit -m "feat: UserService 인터페이스 정의"
   git commit -m "feat: JCFUserService 구현"
   ```

2. **의미 있는 단위로 커밋**
   - 하나의 커밋은 하나의 목적만 가져야 함
   - "Entity 추가"와 "Service 구현"은 별도 커밋으로 분리

3. **커밋 전에 코드 확인**
   ```bash
   git diff                  # 변경사항 확인
   git userStatus               # 스테이징 상태 확인
   ```

4. **푸시 전에 테스트**
   - 코드가 정상 작동하는지 확인
   - 컴파일 에러가 없는지 확인

### ❌ DON'T (하지 말아야 할 것)

1. **여러 기능을 한 커밋에 담지 않기**
   ```bash
   # 나쁜 예
   git commit -m "feat: User, Channel, Message 엔티티 추가 및 서비스 구현, 버그 수정"
   ```

2. **의미 없는 커밋 메시지 사용하지 않기**
   ```bash
   # 나쁜 예
   git commit -m "수정"
   git commit -m "Update"
   git commit -m "WIP"
   git commit -m "asdf"
   ```

3. **디버깅용 코드 커밋하지 않기**
   ```java
   // 이런 코드는 제거 후 커밋
   System.out.println("디버깅 중...");
   // TODO: 나중에 수정
   ```

4. **민감한 정보 커밋하지 않기**
   - API 키, 비밀번호, 개인정보 등
   - `.gitignore`에 등록하여 방지

5. **main 브랜치에 직접 커밋하지 않기**
   - 항상 feature/fix 브랜치에서 작업
   - Pull Request를 통해 병합

---

## 실전 예시 모음

### 예시 1: Entity 생성
```bash
git add src/main/java/com/sprint/mission/entity/User.java
git commit -m "feat: User 엔티티 클래스 생성

BaseEntity를 상속받아 id, createdAt, updatedAt 필드를 포함하며
username, nickname, profileImage 필드를 추가했습니다."
```

### 예시 2: Service 구현
```bash
git add src/main/java/com/sprint/mission/service/jcf/JCFUserService.java
git commit -m "feat: JCFUserService 클래스 구현

UserService 인터페이스를 구현하여 HashMap 기반의
사용자 CRUD 기능을 제공합니다."
```

### 예시 3: 버그 수정
```bash
git add src/main/java/com/sprint/mission/service/jcf/JCFMessageService.java
git commit -m "fix: Message 조회 시 삭제된 메시지 반환 버그 수정

삭제된 메시지도 조회되는 문제를 해결하기 위해
findById 메서드에 null 체크 로직을 추가했습니다."
```

### 예시 4: 리팩터링
```bash
git add src/main/java/com/sprint/mission/service/UserService.java
git commit -m "refactor: UserService 메서드명 변경

- getUsers() → getAllUsers()로 변경
- findUser() → getUserById()로 변경

메서드 역할을 더 명확히 표현하기 위해 이름을 변경했습니다."
```

### 예시 5: 문서 작성
```bash
git add README.md
git commit -m "docs: README에 프로젝트 실행 방법 추가"
```

### 예시 6: 의존성 추가
```bash
git add build.gradle
git commit -m "chore: Lombok 의존성 추가

Entity 클래스의 보일러플레이트 코드를 줄이기 위해
Lombok 라이브러리를 추가했습니다."
```

---

## 참고 자료

- [Conventional Commits](https://www.conventionalcommits.org/ko/v1.0.0/)
- [Git 브랜치 전략](https://git-scm.com/book/ko/v2/Git-%EB%B8%8C%EB%9E%9C%EC%B9%98-%EB%B8%8C%EB%9E%9C%EC%B9%98-%EC%9B%8C%ED%81%AC%ED%94%8C%EB%A1%9C%EC%9A%B0)

---

**작성일**: 2025-10-01
**버전**: 1.0
