# 템플 스테이 예약 시스템 : TempLLO

---
## 📋 개요
| 분류       | 내용                                                                                              |
|------------|---------------------------------------------------------------------------------------------------|
| 주제       | 나는 절로 - 실시간 템플 스테이 예약 시스템                                                  |
| 개발 기간  | 2024.12.26 ~ 2025.01.26                                                                            |
| ERD        | [ERD Cloud 링크](https://www.erdcloud.com/d/YBvebiBbk6LjsEBCu)                                  |
| API 명세서 | [Notion 링크](https://www.notion.so/teamsparta/API-28a4059cc855459ab9cd510607efa318)       |
| API 시나리오 명세서 | [Link](https://www.notion.so/teamsparta/739599d6c7d54c3ba42fffcf1b966c2a)                                                                                 |
| 배포 링크  | [Link](http://3.34.141.202:19091) |

<br>

## 팀원 구성
<div align="center">

| 박지혜 `팀장` | 채수원 `부팀장` | 안재희 | 장윤지 | 전혜리 | 
| :------: |  :------: | :------: | :------: | :------: |
|[<img width="96" src="https://github.com/user-attachments/assets/b9e6b450-5d75-4ce1-a703-6a2d21938d15"> <br> @jeeheaG](https://github.com/jeeheaG)|[<image width="102" src="https://github.com/user-attachments/assets/723a80ff-a5ed-4b9f-a621-eab8feb0397c"><br> @soo1e](https://github.com/soo1e)|[<img width="99" src="https://github.com/user-attachments/assets/8cd15873-6e07-4d3c-a3c7-524d1c7603e3"> <br>@jhbreeze](https://github.com/jhbreeze)|[<img width="98" src="https://github.com/user-attachments/assets/8b496f9a-c48f-4ae9-ba30-dc88ce1912b4"> <br>@elliaaa](https://github.com/elliaaa)|[<img width="100" src="https://github.com/user-attachments/assets/8b780871-6d98-4302-baaa-1428ba9ab59a"> <br>@hyeririjeon](https://github.com/hyeririjeon)|
|  `예약` `DevOps` |   `프로모션` `쿠폰` |  `유저` `리뷰` `DevOps` | `사찰` | `프로그램` |

</div>

<br>

## 📌 프로그램 개요
### 프로젝트 소개
- **프로젝트명**: TempLLO
- "TempLLO"는 자연 속에서 편안한 휴식을 제공하는 템플 스테이 플랫폼입니다. 
  기본 템플 스테이 예약 시스템 외에도 이벤트성 소개팅 서비스를 제공하여 특별한 경험을 선사합니다.
  MSA 기반 실시간 예약 시스템을 통해 사용자에게 편리하고 매끄러운 예약 경험을 제공합니다. 

### 프로젝트 목표
- 목표 1: 대량의 예약 요청과 트래픽 폭주 상황에서도 안정성과 효율성을 유지.
- 목표 2: 동시성 제어 및 대규모 트래픽 처리를 중점적으로 구현.
- 목표 3: 성수기 및 이벤트 상황에서도 최적화된 사용자 경험 제공.

<br>

## 🛠️ 인프라 설계도
 ![나는절로_인프라설계도-인프라 설계도_최종 drawio](https://github.com/user-attachments/assets/9e65f1c9-daeb-48e3-b162-8d7c1ba9b8da)


<br>

## ⚙️ 개발환경
- **Backend**: Java 17, Spring Boot
- **Database**: PostgreSQL
- **Search/Indexing**: Elasticsearch
- **CI/CD**: GitHub Actions
- **Streaming**: Kafka
- **Tools**: IntelliJ, Docker

<br>

## 🛠 기술 스택
<a href="버튼을 눌렀을 때 이동할 링크" target="_blank">
<img src="https://img.shields.io/badge/Spring%20boot-6DB33F?style=for-the-badge&logo=Spring%20boot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logoColor=white">
<img src="https://img.shields.io/badge/QueryDSL-0769AD?style=for-the-badge&logo=querydsl&logoColor=white">
<img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white">
<img src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white">
<img src="https://img.shields.io/badge/elasticsearch-%230377CC.svg?style=for-the-badge&logo=elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/Kibana-005571?style=for-the-badge&logo=Kibana&logoColor=white">
<img src="https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka">
<img src="https://img.shields.io/badge/JWT-pink?style=for-the-badge&logo=JSON%20web%20tokens">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white">
<img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
<img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/AWS ECR-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20ECS-FF9900?style=for-the-badge&logo=Amazon%20ECS&logoColor=white">
<img src="https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=Amazon%20RDS&logoColor=white">
<img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white">
</a>
<br/><br/>

<br>

## 📚 프로젝트 협업 규칙 및 컨벤션

<details>
<summary>🌳 협업 규칙</summary>

### **Ground Rules**

- **소통**: 부드럽고 간결하게, 이유를 함께 전달
- **문제 해결**:
    - 1시간 이상 문제 지속 시 팀원과 공유
    - 작업 중 외부 변경 사항 바로 공유
- **명세 관리**:
    - 필요한 API 명세 정확히 작성, 예시 JSON 포함
- **팀 일관성**:
    - 작업 스타일 통일, 하나의 제품처럼 보이도록 유지
- **효율**:
    - 다른 팀원 작업에 영향을 주는 변경 최소화

### **데일리 스크럼**

- **매일 아침 10:30에 진행**
    - 지난 목표 진행상황, 오늘 목표
    - Issue & QnA

### **프로젝트 일정**

- **1주차**: 설계 및 명세 완성, 도메인(엔티티) 구현
- **2~3주차**: 주요 기능 구현 및 테스트
    - 중간 발표 및 피드백 반영
- **4주차**: 리팩토링 및 개선, 발표 준비

</details>


<details>
<summary>🌳 Git 컨벤션</summary>

### **커밋 메시지**

- **형식**: `✨ feat : create user api`
- **태그**:
    - `feat`, `edit`, `add`, `refactor`, `remove`, `comment`, `docs`, `chore` 등
- **Gitmoji 사용**

| 태그          | 설명                   |
|---------------|-----------------------|
| ✨ `feat`      | 새로운 기능 추가        |
| 🔧 `edit`      | 기능 수정 또는 업데이트 |
| ➕ `add`       | 새로운 파일이나 코드 추가 |
| ♻️ `refactor`  | 코드 리팩토링          |
| ❌ `remove`    | 불필요한 코드 또는 파일 삭제 |
| 💬 `comment`   | 주석 추가              |
| 📄 `docs`      | 문서 수정 또는 추가     |
| 🛠️ `chore`     | 빌드 작업 및 패키지 매니저 설정 |

- 가능한 작은 단위로 커밋

### **브랜치 이름 규칙**

- **형식**: `태그/이슈번호/기능명`
    - 예: `feat/01/sign-up`

### **ISSUE/PR 관리**

- **PR 규칙**:
    - 기능 단위로 작성, 너무 많은 양 방지
    - 리뷰 포인트 명확히 설정 (테스트 코드 포함 여부, 스타일 등)
    - PR title: `브랜치명/구현기능`
        - 예: `[Feat/01/sign-up] 로그인 구현`
- **ISSUE 연결**:
    - 기능별 Issue 생성 후 브랜치명과 PR에 이슈 번호 명시

</details>


<details>
<summary>🌳 코드 컨벤션</summary>

### **기본 규칙**

- **Java 버전**: Java 17 사용
- **패키지 구조**: 도메인 중심 설계

    ```
    com.templlo.service.user
       ├── entity        
       ├── dto          
       ├── repository   
       ├── service      
       └── common      
    ```

- 일반적인 변수&클래스명 **Camel Case 사용**
- **중복 코드 제거**: 메서드화, 모듈화, AOP, Interceptor 활용
- **OOP 원칙 준수**:
    - **SOLID**: 변경이 다른 코드에 미치는 영향 최소화, 단일 책임 원칙 고려
    - **Entity → DTO 의존 방지**: 고수준 클래스가 저수준 클래스를 의존하지 않도록 설계
    - **서비스 트랜잭션 단위**: 메서드마다 하나의 트랜잭션 단위로 개발
- **Record 사용**: DTO 대신 `Record` 적극 활용
- **팩토리 메서드 활용**: `toDto()`, `from()` 등
- **어노테이션**: 필요 시에만 사용 (`@Data` 지양, 대신 `@Builder`, `@Getter` 사용)
- **Exception 처리**:
    - 공통 클래스 파일 활용(Basic Exception, Basic Status Code)
    - 도메인 별 세부 Exception 클래스 구현
- **API 설계 규칙**:
    - RESTful 원칙 준수, 리소스를 명확히 표현
    - 하이픈(-)으로 단어 구분
    - `PUT` vs `PATCH`: 전역 vs 부분 변경에 따라 구분
        - **PUT**: 리소스 전체 변경 또는 신규 리소스 생성
        - **PATCH**: 리소스 필드 일부만 변경

### **TEST**

- **.http 파일 작성**: API 별 .http 파일 생성, 형상 관리 및 테스트 시 편리
- **단위 테스트 작성**: 개별 모듈/메서드 검증
    - Controller
    - Service

</details>

<br>


## 🚀 주요 기능
1. **실시간 예약 시스템**:
   - 사용자가 실시간으로 예약 가능 여부를 확인하고 프로그램을 예약.
2. **대기열 관리**:
   - 예약 초과 시 대기열 등록 및 순번에 따라 예약 진행.
3. **선착순 신청권 발급**:
   - 특정 사찰 또는 프로그램에 대한 선착순 신청권 발급.
4. **사용자 관리**:
   - 사용자 후기 및 평가 작성 기능 제공.
5. **사찰 관리**:
   - 사찰 및 프로그램 정보 등록 및 수정 기능 제공.

<br>

## 🧑‍💻 적용 기술

<details>
<summary>🌳 Kafka를 통한 대용량 트래픽 처리</summary>

- **대규모 트래픽 처리**: Kafka를 통한 이벤트 기반 비동기 처리
- **사용자 경험 개선**: 요청이 완료될 때까지 기다릴 필요 없이, 예약 또는 쿠폰 발급 요청 후 결과를 나중에 확인할 수 있도록 처리하여 사용 편의성을 향상
</details>

<details>
<summary>🌳 AOP 기반 분산락을 활용한 데이터 동시성 제어</summary>

- **데이터 일관성 보장**: 분산 환경에서 동일 자원에 대한 동시 접근을 제한
- **트랜잭션 충돌 방지**: 자원에 락을 설정하여 데이터 충돌 방지 및 안정적 처리 가능
- **낙관적 락의 대안**: @Version 기반 낙관적 락의 성능 저하 및 복잡성을 해결
</details>

<details>
<summary>🌳 Elasticsearch (ELK)</summary>

- **실시간 검색 및 분석**: 대규모 데이터에서 빠른 검색과 실시간 로그 분석 제공
- **장애 모니터링**: 서비스 로그 데이터를 수집 및 시각화해 문제를 신속히 파악하고 대응
</details>

<details>
<summary>🌳 Redis 캐시를 활용한 성능 향상</summary>

- **데이터 조회 속도 향상**: 자주 조회되는 데이터를 인메모리 캐싱으로 처리하여 응답 속도 개선
- **시스템 부하 감소**: 캐시 활용으로 데이터베이스 접근을 최소화
</details>

<details>
<summary>🌳 Transactional Outbox Pattern을 적용하여 데이터 일관성 보장</summary>

- **트랜잭션 일관성 보장**: 비동기 통신에서의 데이터베이스와 메시지 브로커 간의 데이터 불일치 문제를 해결
- **재처리 가능**: Outbox 테이블에 이벤트 저장 후 메시지 브로커(Kafka 등)로 전달, 장애 발생 시 안정적 복구 지원
</details>

<details>
<summary>🌳 MSA 환경 Github Actions CI/CD 파이프라인 구성 </summary>

- **CI**: 빌드 및 테스트코드 실행으로 코드 품질 검증
- **CD**: Docker 이미지 빌드 후 ECR에 업로드, 변경된 서비스 ECS에 자동 배포하여 MSA 구조의 서비스별 독립적 배포 환경 구축
- **AWS ECR**: CI 파이프라인과 연계해 서비스별 최신 Docker 이미지 자동 저장 및 버전 관리
- **AWS ECS**: Fargate로 마이크로서비스별 서버리스 컨테이너 환경 구성, 서비스 단위로 독립적 배포 구현
</details>

<details>
<summary>🌳 JWT 다중토큰 발급, 토큰 BlackList 관리</summary>

- **서버 리소스 최적화:** 세션 저장소를 사용하지 않아 서버 부하 감소
- **토큰 위조 방지:** Access Token과 Refresh Token을 활용한 다중 토큰 발급으로 보안성 확보
- **Refresh Token Rotate:** Refresh Token이 사용될 때마다 새롭게 발급하고 이전 토큰 무효화하여 탈취 위험 최소화
- **실시간 토큰 BlackList 관리:** 만료된 토큰 및 로그아웃된 토큰 실시간으로 무효화하여 악의적 접근 차단
</details>

<br>

## 📌 기술적 의사결정

<details>
<summary>1. 예약의 비동기 대기열 위치 고민</summary>

- **후보**:
 1. 예약 신청 시점에 대기열을 만들어 즉시 동기 처리 (동기)
 2. 예약 신청을 비동기 처리 후 추후 결과 확인 (비동기)
- **결정**: 사용자를 앞단에서 기다리게 하기보다는 뒷단에 **비동기** 대기열을 만들어, 사용자가 추후 확인하도록 하여 사용성을 높이기로 결정.
</details>

<details>
<summary>2. Redis 분산 락 적용 방식 고민</summary>

- **후보**:
 1. Redis에 DB 데이터 접근을 위한 키를 두고, 키에 분산 락 적용 → 두 가지 저장소를 사용하며 시간이 더 걸릴 가능성 있음
 2. Redis에 데이터 자체를 보관하고, 데이터에 분산 락 적용 → DB 동기화 전에 데이터 유실 위험이 존재
- **결정**: 2번 방식이 빠를 수 있지만, 대부분 **비동기적으로 처리**되고 있으며, 락이 적용되는 데이터가 **유실 시 복구가 어려운 데이터**라고 판단하여 **1번 방식**을 선택
</details>

<details>
<summary>3. Kafka vs RabbitMQ</summary>

- **후보**:
 1. Kafka: 대용량 데이터 처리와 고속 데이터 스트리밍에 최적화 / 장애 상황에서도 안정적으로 동작 / 확장성
 2. RabbitMQ: 유연한 메시지 라우팅 / Kafka보다는 복잡한 설정으로 확장
- **결정**: 대량의 메시지 전송이 필요한 예약이나 쿠폰 등을 고려하여 고성능 데이터 처리에 적합하며 파티션 기반의 아키텍처인 Kafka를 통해 브로커를 추가함으로써 수평적으로 쉽게 확장 가능한것이 프로젝트에 더 부합할 것으로 판단되어 **Kafka**를 선택
</details>

<details>
<summary>4. Github Actions vs Jenkins</summary>

- **장단점 분석**:

| 기능 | **Github Actions** | **Jenkins** |
|-----------------------|------------------------------------------------------------|------------------------------------------------|
| **초기 설정** | 즉시 사용 가능 | 서버 설치 및 설정 필요 |
| **유지 보수** | 코드와 CI/CD 파이프라인 설정을 동일 저장소에서 관리 | 플러그인 최신 상태 유지 등 추가 관리 필요 |
| **러닝 커브** | 낮음 | 높음 |
| **비용 효율성** | 서버 설치 필요 없음 → 인프라 비용 절감 | 서버 비용 발생 |

- **결정**: 제한된 시간안에 기획/개발/배포까지 마무리해야하기 때문에 학습 부담이 적으며 Github 저장소와 통합이 쉬운 Github Actions 채택
</details>

<br>

## 🛠️ 트러블슈팅
[Wiki](https://github.com/TempLLO/templlo-booking-2025/wiki)에서 트러블 슈팅 파트 부분을 참고해주세요!

<br>

## ✅ 부하 테스트
[Wiki](https://github.com/TempLLO/templlo-booking-2025/wiki)에서 부하 테스트 부분을 참고해주세요!

<br>

추가할 거 있으면 여기에 적어주세요!
1. 성능 개선 : Before & After 수치나 이미지로 강조
2. 기술적으로 도입하게 된 배경 + 고도화한 부분
