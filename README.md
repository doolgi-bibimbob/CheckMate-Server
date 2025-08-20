# 📘 CheckMate Backend

수학 문제 풀이 학습 플랫폼의 백엔드 서버입니다.  
학생이 풀이를 제출하면 교사가 직접 **필기·텍스트·이미지** 형태로 리뷰할 수 있으며, OpenAI GPT API를 통해 **AI 리뷰**도 받을 수 있습니다.  
애플리케이션 서버와 GPT 서버 간 통신은 **Kafka**로 비동기 처리하며, 운영 데이터베이스는 **AWS RDS PostgreSQL**을 사용합니다.

---

## 🚀 주요 기능
- **학생 제출 관리**: 텍스트/이미지 풀이 업로드 및 조회
- **교사 리뷰**: 점수, 총평, 자유 필기·텍스트 박스·이미지 스티커 형태의 애노테이션 지원
- **AI 리뷰**: Kafka를 통한 GPT 서버 연동, 자동 피드백 제공
- **검색/조회**: JPA + QueryDSL 기반 페이징 및 조건 검색
- **권한 제어**: 학생 / 교사 / 관리자 역할 기반 접근 제어
- **운영 지원**: 헬스체크, 표준 로깅, Swagger(OpenAPI) 문서 제공

---

## 🛠️ 기술 스택
- **Language**: Java 17  
- **Framework**: Spring Boot 3.5.3  
- **Database**: PostgreSQL (AWS RDS)  
- **ORM/Query**: JPA + QueryDSL  
- **Messaging**: Apache Kafka (App ↔ GPT Server)  
- **Docs**: Springdoc OpenAPI (Swagger UI)  
- **Build**: Gradle  

---

## 📂 프로젝트 구조

```text
build/
gradle/
src/
 └── main/
      ├── java/
      │    └── com/
      │         └── seonlim/
      │              └── mathreview/
      │                   ├── advice/
      │                   ├── aop/
      │                   ├── cache/
      │                   ├── configuration/
      │                   ├── controller/
      │                   ├── dto/
      │                   ├── entity/
      │                   ├── exception/
      │                   ├── kafka/
      │                   ├── repository/
      │                   ├── security/
      │                   └── service/
      │                   └── MathreviewApplication.java
      └── resources/
           ├── static/
           ├── templates/
           ├── application.yml
           ├── application-local.yml
           └── application-prod.yml
test/
docs/
 ├── architecture.svg
 └── erd.svg
```

## 🧭 아키텍처 다이어그램

<img src="https://github.com/user-attachments/assets/1855fd2b-6ec7-437f-a945-7127ed5bb266" alt="Architecture" width="600"/>

## 🗺️ ERD

<img src="https://github.com/user-attachments/assets/3d24b690-d186-429d-93c1-d6fdd77e2d02" alt="ERD" width="600"/>
