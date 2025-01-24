# 템플 스테이 예약 시스템 : TempLLO

## 📋 개요
| 분류       | 내용                                                                                              |
|------------|---------------------------------------------------------------------------------------------------|
| 주제       | 나는 절로 - 자연과 음식에 취하고 새 인연을 만나는 템플 스테이                                                  |
| 개발 기간  | 2024.12.26 ~ 2025.01.26                                                                            |
| ERD        | [ERD Cloud 링크](https://www.erdcloud.com/d/YBvebiBbk6LjsEBCu)                                  |
| API 명세서 | [Notion 링크](https://www.notion.so/teamsparta/API-28a4059cc855459ab9cd510607efa318)       |
| API 시나리오 명세서 | [Link]()                                                                                 |
| 배포 링크  | [Link]() |

---

## 팀원 구성
<div align="center">

| 박지혜 | 채수원 | 안재희 | 장윤지 | 전혜리 | 
| :------: |  :------: | :------: | :------: | :------: |
|[<img width="96" src="https://github.com/user-attachments/assets/b9e6b450-5d75-4ce1-a703-6a2d21938d15"> <br> @jeeheaG](https://github.com/jeeheaG)|[<image width="102" src="https://github.com/user-attachments/assets/723a80ff-a5ed-4b9f-a621-eab8feb0397c"><br> @soo1e](https://github.com/soo1e)|[<img width="99" src="https://github.com/user-attachments/assets/8cd15873-6e07-4d3c-a3c7-524d1c7603e3"> <br>@jhbreeze](https://github.com/jhbreeze)|[<img width="98" src="https://github.com/user-attachments/assets/8b496f9a-c48f-4ae9-ba30-dc88ce1912b4"> <br>@elliaaa](https://github.com/elliaaa)|[<img width="100" src="https://github.com/user-attachments/assets/8b780871-6d98-4302-baaa-1428ba9ab59a"> <br>@hyeririjeon](https://github.com/hyeririjeon)|
</div>

## 📌 프로그램 개요
### 프로젝트 소개
- **프로젝트명**:TempLLO
- **설명**:"TempLLO"는 자연과 음식에 취하며 템플 스테이의 편안함을 느낄 수 있는 플랫폼이다.
  기본 템플 스테이 예약 시스템 외에도 이벤트성 소개팅 서비스를 통해 색다른 경험을 제공한다.
  대규모 예약 시스템과 동시성 제어를 구현하여 사용자가 원활한 예약 경험을 누릴 수 있도록 설계되었다. 
- **특징**:

### 프로젝트 목표
- 목표 1: 대량의 예약 요청과 트래픽 폭주 상황에서도 안정성과 효율성을 유지.
- 목표 2: 동시성 제어 및 대규모 트래픽 처리를 중점적으로 구현.
- 목표 3: 성수기 및 이벤트 상황에서도 최적화된 사용자 경험 제공.


### 🛠️ 서비스 기능 정의 및 우선 순위
1. **예약 시스템**: 예약 가능한 프로그램 목록 제공 및 실시간 예약 상태 확인.
2. **대기열 관리**: 예약 인원이 초과된 경우 대기자 등록 및 취소 발생 시 대기열 순서에 따라 관리.
3. **선착순 신청권 시스템**: 인기 프로그램 및 사찰에 대한 우선 신청권 발급 및 관리.
4. **사용자 및 프로그램 관리**: 후기 작성 및 평가 기능 포함.
5. **사찰 소개 및 관리**: 사찰 정보 제공 및 관리 기능.

---

## 🛠️ 인프라 설계도
- 설계도 이미지 추가 예정

---

## ⚙️ 개발환경
- **Backend**: Java 17, Spring Boot
- **Database**: PostgreSQL
- **Search/Indexing**: Elasticsearch
- **CI/CD**: GitHub Actions
- **Streaming**: Kafka
- **Monitoring**: 
- **Tools**: IntelliJ, Docker

---

## 🛠 기술 스택 - 수정 필요
<a href="버튼을 눌렀을 때 이동할 링크" target="_blank">
<img src="https://img.shields.io/badge/Spring boot-6DB33F?style=flat-square&logo=Spring boot&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/JUint5-25A162?style=flat-square&logo=JUnit5&logoColor=white">
<br/>
<img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=Amazon EC2&logoColor=white">
<img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=Amazon RDS&logoColor=white">
<img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat-square&logo=Amazon S3&logoColor=white">
</a>
<br/><br/>

* Others
    - Spring Security & JWT
    - Spring Rest Docs & JUnit
    - Hibernate Bean Validator
    - Data JPA
    - Lombok
    - Slf4j


---


## 🔖 커밋 컨벤션
`gitmoji`와 함께 아래 키워드를 사용해 커밋 메시지를 작성한다.

| 태그        | 설명                                     |
|-------------|------------------------------------------|
| ✨ `feat`   | 새로운 기능 추가                          |
| 🔧 `edit`   | 기능 수정 또는 업데이트                   |
| ➕ `add`    | 새로운 파일이나 코드 추가                 |
| ♻️ `refactor` | 코드 리팩토링                             |
| ❌ `remove` | 불필요한 코드 또는 파일 삭제              |
| 💬 `comment`| 주석 추가                                 |
| 📄 `docs`   | 문서 수정 또는 추가                       |
| 🛠️ `chore`  | 빌드 작업 및 패키지 매니저 설정            |


---


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

---

## 🧑‍💻 적용 기술
- 동시성 제어를 위한 Redis 및 분산 락 구현.
- 엘라스틱 서치 어필?
- 성수기 트래픽 관리를 위한 큐 기반 대기열 시스템 설계.
- 로드 밸런싱과 리팩톤 기간에 한 거 어필?
---

## 📌 기술적 의사결정

### 1. **결정: 예약의 비동기 대기열 위치 고민**
   - **후보**:
     1. 예약 신청 시점 (동기)
     2. 예약 신청 접수 후 처리 시점 (비동기)
   - **결정**: 사용자를 앞단에서 기다리게 하기보다는 뒷단에 **비동기** 대기열을 만들어, 사용자가 추후 확인하도록 하여 사용성을 높이기로 결정.

### 2. **결정: Redis 분산 락 적용 방식 고민**
   - **후보**:
     1. Redis에 DB 데이터 접근을 위한 키를 두고, 키에 분산 락 적용  
        → 두 가지 저장소를 사용하며 시간이 더 걸릴 가능성 있음.
     2. Redis에 데이터 자체를 보관하고, 데이터에 분산 락 적용  
        → DB 동기화 전에 데이터 유실 위험이 존재.
   - **결정**: 2번 방식이 빠를 수 있지만, 대부분 **비동기적으로 처리**되고 있으며, 락이 적용되는 데이터가 **유실 시 복구가 어려운 데이터**라고 판단하여 **1번 방식**을 선택.


### 3. **결정: Kafka vs RabbitMQ**
   - **후보**:
     1. Kafka : 대용량 데이터 처리와 고속 데이터 스트리밍에 최적화 / 장애 상황에서도 안정적으로 동작 / 확장성
     2. RabbitMQ : 유연한 메시지 라우팅 / Kafka보다는 복잡한 설정으로 확장
- **결정**: 대량의 메시지 전송이 필요한 예약이나 쿠폰 등을 고려하여 고성능 데이터 처리에 적합하며 파티션 기반의 아키텍처인 Kafka를 통해 브로커를 추가함으로써 수평적으로 쉽게 확장 가능한것이 프로젝트에 더 부합할 것으로 판단되어 **Kafka**를 선택

### 4. **결정: Github Actions vs Jenkins**
  - **장단점 분석**:

| 기능                  | **Github Actions**                                          | **Jenkins**                                     |
|-----------------------|------------------------------------------------------------|------------------------------------------------|
| **초기 설정**         | 즉시 사용 가능                                             | 서버 설치 및 설정 필요                          |
| **유지 보수**         | 코드와 CI/CD 파이프라인 설정을 동일 저장소에서 관리         | 플러그인 최신 상태 유지 등 추가 관리 필요       |
| **러닝 커브**         | 낮음                                                       | 높음                                           |
| **비용 효율성**       | 서버 설치 필요 없음 → 인프라 비용 절감                     | 서버 비용 발생                                  |

   - **결정**: 제한된 시간안에 기획/개발/배포까지 마무리해야하기 때문에 학습 부담이 적으며 Github 저장소와 통합이 쉬운 Github Actions 채택 

---

## 🛠️ 트러블슈팅

### 1. 문제 상황: JPA 낙관적 락(@Version)으로 인한 동시성 제어 문제
- **문제**  
  JPA의 낙관적 락(@Version)을 사용하여 동시성 제어를 구현했으나, 다중 트랜잭션에서 데이터 충돌이 발생.

- **원인 분석**  
  - 낙관적 락은 **분산 환경**에서 높은 트랜잭션 충돌율을 포함한 동시성 제어 방식이 필요.  
  - @Version은 충돌 발생 시 **롤백**과 **재처리**로 성능 저하 발생.  
  - 충돌 후속 처리를 직접 구현해야 하므로 **코드 복잡성 증가**.

- **해결 방법**  
  - @Version 낙관적 락을 제거하고 **Redis 분산 락** 도입.  
  - 분산 환경에서 동시성 제어를 향상하고 데이터 충돌 상황을 안정적으로 처리.



### 2. 문제 상황: Gradle 빌드 실패
- **문제**  
  `chmod: cannot access 'gradlew': No such file or directory` 오류 발생.

- **원인 분석**  
  - 최상위 디렉토리에서 빌드를 시도했으나, **MSA 구조**에서는 각 서비스별로 독립적인 Gradle 프로젝트 구성.  
  - 최상위 디렉토리에 Gradle 설정 파일이 존재하지 않음.

- **해결 방법**  
  - **작업 디렉토리 설정**을 변경하여 각 서비스별 Gradle 환경을 인식하도록 수정.



### 3. 문제 상황: ElasticSearch 관련 Bean 중복 등록 문제
- **문제**  
  ElasticSearch 관련 컴포넌트 스캔 중, `TempleRepository` Bean의 중복 등록 오류 발생.

- **원인 분석**  
  - 서로 다른 구조의 DB를 하나의 도메인 클래스에서 구현하려 시도.  
    - **JPA**: RDBMS 기반, `Entity` 형식.  
    - **ElasticSearch**: NoSQL 기반, `Document` 형식.  

- **해결 방법**  
  - JPA와 ElasticSearch 각각 별도의 **데이터 클래스 구성**.  
  - 서로 독립적으로 동작하도록 리포지토리 분리.


----
추가할 거 있으면 여기에 적어주세요!
1. 인프라 설계도
2. 주요 기능 토글로?
3. 적용 기술 노션 같이 문서화 해서 링크
4. 부하 테스트
5. 기술 스택 뱃지
6. 그라운드 룰 + 협업 방식
7. 성능 개선 : Before & After 수치나 이미지로 강조
8. 기술적으로 도입하게 된 배경 + 고도화한 부분
