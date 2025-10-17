
## 요구사항

### 기본
- [x] 기본 항목 1
- [x] 기본 항목 2

### 심화
- [x] 심화 항목 1
- [x] 심화 항목 2

## 주요 변경사항
- **계층 분리 및 공통화**: Entity, Service, Repository 각 계층별로 공통 기능을 `BaseEntity`, `BaseService`, `BaseRepository` 추상 클래스/인터페이스를 통해 통합 관리합니다.
- **인터페이스 기반 설계**: Service, Repository는 인터페이스로 각 메서드를 선언하고, 메모리 기반 구현체(`JCF*`)를 통해 기능을 구현하여 유연성을 높였습니다.
- **데이터 관리**: 싱글톤 패턴의 `DataPersistenceManager`를 통해 Java 직렬화를 사용하여 데이터를 파일(`.txt`)로 저장/로드합니다. 각 데이터 파일은 `DataKey` enum으로 식별됩니다.
- **복합 키 사용**: `Participation` 엔티티는 `UserId`와 `ChannelId`를 조합한 `ParticipationDualKey` 레코드를 복합 키로 사용합니다.
- **UI 분리**: 콘솔 UI 로직을 `UserView`, `ChannelView`, `ParticipationView`, `MessageView` 등으로 분리하고, 공통 선택 로직은 `SharedView`로 추출하여 관리합니다.

## 스크린샷
![image](이미지url)

## 멘토에게
- Service에는 기능이 존재하지만 실제 테스트 했을 때 추가 기능이 필요해 사용이 어려운 기능이 존재합니다.


