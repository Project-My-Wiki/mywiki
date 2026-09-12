# 📖 My Wiki - 당신의 지식 저장소

[![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

**My Wiki**는 단순한 북마크 서비스를 넘어, 집단지성을 이용해 거대하고 효율적인 지식 저장소를 구축하는 것을 목표로 하는 프로젝트입니다.

My Wiki는 여러분이 웹 서핑 중 발견한 유용한 아티클이나 블로그 글을 저장하고, 체계적인 요약 템플릿 기반으로 요약문을 작성해 학습 효과를 극대화할 수 있도록 하며, 요약 복기를 통해 학습한 지식을 온전히
자신의 것으로 만들 수 있도록 돕습니다!

### 지금 바로 이용해보세요!! >>> https://my-wiki.kro.kr

---

## 로컬 실행과 Google 로그인 설정

JDK 21, Node.js, Docker가 필요합니다. Google Cloud Console에서 **웹 애플리케이션** OAuth 클라이언트를 만들고 승인된 리디렉션 URI에 `http://localhost:8080/login/oauth2/code/google`을 등록하세요. 배포 환경에서는 `https://my-wiki.kro.kr/login/oauth2/code/google`을 등록해야 합니다. 이 URI는 Google이 백엔드로 돌려보내는 주소이며, 로그인 완료 후 프론트엔드로 이동하는 `OAUTH2_REDIRECT_URI`와 다릅니다.

```bash
docker compose up -d
export GOOGLE_CLIENT_ID='발급받은 클라이언트 ID'
export GOOGLE_CLIENT_SECRET='발급받은 클라이언트 보안 비밀'
bash ./gradlew bootRun
```

별도 터미널에서 `cd frontend && npm ci && npm start`를 실행한 뒤 `http://localhost:3000`으로 접속하세요. 로컬 프론트엔드는 `frontend/.env.development`의 `REACT_APP_API_BASE_URL=http://localhost:8080`을 사용합니다. Google OAuth 동의 화면이 테스트 모드라면 로그인할 계정을 테스트 사용자로 추가해야 합니다.

로그인 후 `/login?error`로 돌아온다면 백엔드 콘솔이나 `logs/spring.log`에서 `Google OAuth login failed` 메시지와 원인 예외를 확인하세요. Google 클라이언트 ID/보안 비밀, 위 리디렉션 URI, MySQL 연결을 먼저 점검하면 원인을 좁힐 수 있습니다.

---

## 🤔 My Wiki는 어떤 서비스인가요?

My Wiki는 정보의 홍수 속에서 핵심만 명확하게 파악하고, 장기 기억으로 전환할 수 있도록 설계되었습니다.

1. **🎯 습관 형성**: 꾸준한 학습과 기록을 통해 지식을 쌓아가는 습관을 만들어줍니다. '랜덤 글 읽기' 기능으로 어떤 글부터 읽어야 할지 모를 때 좋은 길잡이가 되어줍니다.
2. **✍️ 학습 효과 극대화**: 구조화된 요약 템플릿을 제공하여, 글의 핵심을 파악하고 자신의 생각을 정리하며 학습 효과를 극대화할 수 있습니다.
3. **🧠 장기 기억 전환**: 단순히 읽고 끝나는 것이 아니라, 요약문을 다시 읽어보며 중요한 정보를 리마인드하고 장기 기억으로 전환하는 과정을 지원합니다.

---

## ✨ 주요 기능

- **Google 소셜 로그인**: 복잡한 회원가입 없이 Google 계정으로 간편하게 시작할 수 있습니다. 추후 크롬 확장프로그램을 통해 더욱 쉽게 북마크를 저장할 수 있도록 기능을 제공할 예정이에요!
- **북마크**: 읽고 싶은 웹 아티클을 손쉽게 저장하고 관리합니다.
- **요약 템플릿**: `핵심 파악` - `세부 내용 정리` - `사고 확장`으로 이어지는 체계적인 템플릿으로 깊이 있는 요약을 작성할 수 있습니다.
- **요약문 모아보기**: 작성한 요약문들을 한눈에 보고, 과거에 학습한 내용을 쉽게 복습할 수 있습니다.
- **랜덤 글 추천**: 어떤 글을 읽을지 결정하기 어려우신가요? 저장된 북마크 중 무작위 글을 오픈하여 꾸준한 학습을 유도합니다.
- **모바일 최적화**: 모바일 환경에 최적화된 UI/UX로 모바일과 PC, 언제 어디서든 편안하게 서비스를 이용할 수 있습니다.

---

## 🌊 사용자 흐름 (User Flow)

사용자가 서비스를 어떻게 이용하게 되는지에 대한 흐름도입니다.

```mermaid
graph TD
    subgraph "시작"
        A[서비스 접속] --> B{로그인 상태?};
    end

    subgraph "인증"
        B -- No --> C[로그인 페이지];
        C -- Google 계정으로 로그인 --> D[메인 페이지];
    end

    subgraph "메인"
        B -- Yes --> D;
        D --> E[북마크 등록];
        D --> F[북마크 목록 보기];
        D --> G[요약 목록 보기];
        D --> H[랜덤 글 읽기];
    end

    subgraph "북마크 관리"
        E -- URL 입력 및 저장 --> E_Success[등록 완료];
        F --> F_Item[북마크 카드];
        F_Item -- 클릭 --> I[북마크 상세];
        H -- 북마크 추천 --> I;
    end

    subgraph "학습 및 요약"
        I --> J{어떤 작업을 할까요?};
        J -- 원문 읽기 --> K[새 탭에서 아티클 열기];
        J -- 요약 작성하기 --> L[요약 작성 페이지];
        J -- 읽음/안읽음 처리 --> I;
        L -- 템플릿에 맞춰 작성 및 제출 --> M[요약 상세 페이지];
    end

    subgraph "요약 관리"
        G --> G_Item[요약 카드];
        G_Item -- 클릭 --> M;
        M --> N{어떤 작업을 할까요?};
        N -- 원문 읽기 --> K;
        N -- 수정하기 --> L;
    end
```

---

## 🏗️ 도메인 구조 (Domain Model)

My Wiki 서비스의 핵심 도메인 모델 구조입니다.

```mermaid
erDiagram
    USER {
        Long id PK "사용자 ID"
        String name "이름"
        String email "이메일"
        Role role "권한"
    }

    BOOKMARK {
        Long id PK "북마크 ID"
        Long userId FK "사용자 ID"
        String url "원본 URL"
        String title "제목"
        String description "설명"
        String image "대표 이미지 URL"
        LocalDateTime readAt "읽은 시각"
    }

    SUMMARY {
        Long id PK "요약 ID"
        Long bookmark_id FK "북마크 ID"
        JSON contents "요약 내용"
    }

    USER ||--o{ BOOKMARK: "북마크를 소유"
    BOOKMARK ||--|| SUMMARY: "요약을 가짐 (1:1)"
```

---

## 🛠️ 기술 스택 (Tech Stack)

**Backend**

- Kotlin, Spring Boot, Spring Security (OAuth2)
- JPA / Hibernate, MySQL, Gradle

**Frontend**

- React, TypeScript, Axios
- CSS, HTML

**DevOps**

- Docker, Nginx, GitHub Actions

---

## 👩‍💻 Developer

| <img width="200px" src="https://avatars.githubusercontent.com/u/59721541?v=4"/> |
|---------------------------------------------------------------------------------|
| **UI/FE/BE**                                                                    |
| [🐼 하이현](https://github.com/hyh1016)                                            |
