# mywiki 모바일 애플리케이션 기획서 및 UI 가이드

본 문서는 기존 웹(React) 기반의 mywiki 프론트엔드를 참고하여, 동일한 기능을 수행하는 모바일 애플리케이션(React Native, Flutter, 또는 Native OS) 개발을 위한 기능 명세 및 UI 가이드라인입니다.

---

## 1. 개요 (Overview)
mywiki 모바일 애플리케이션은 사용자가 웹상의 아티클(북마크)을 저장하고, 이를 읽은 뒤 구조화된 템플릿(핵심 파악, 세부 내용 정리 등)을 활용해 요약을 기록하는 '지식 관리 및 요약 서비스'입니다.

---

## 2. 기능 명세서 (Functional Specification)

### 2.1. 인증 및 진입 화면
- **로그인 (Login)**
  - OAuth 2.0 기반의 소셜 로그인 지원
  - 미인증 시 로그인 화면으로 리다이렉트
  - **기능:** 소셜 로그인 버튼 클릭 시 WebView 또는 InAppBrowser를 통한 OAuth 인증 흐름 진행

### 2.2. 메인 탭 (Main Navigation)
웹의 메인 페이지 버튼 구성을 모바일 하단 네비게이션(Bottom Tab Navigation) 구조로 전환하는 것을 권장합니다.
- **홈 탭 (Home):** 환영 인사, 주요 액션 버튼(북마크 등록, 랜덤 글 읽기) 제공
- **북마크 탭 (Bookmarks):** 저장된 북마크 목록
- **요약 탭 (Summaries):** 작성한 요약글 목록
- **내 정보 탭 (Profile):** 로그아웃 및 설정 (추가 제안)

### 2.3. 북마크(Url) 관리 기능
- **북마크 등록 (Add Bookmark)**
  - **입력 폼:** URL 입력 필드 (Paste 기능 연동 등 편의성 고려)
  - **동작:** URL 제출 시 서버에서 메타데이터(Title, Description, Image)를 크롤링하여 저장 후, 결과 카드 미리보기 제공
  - **모바일 특화:** OS 공유 확장 기능(Share Extension/Intent)을 구현하여 웹 브라우저에서 바로 URL을 앱으로 공유해 등록할 수 있도록 구현하면 좋습니다.
- **북마크 목록 (Bookmark List)**
  - **페이징:** 무한 스크롤 (Cursor 기반, 한 번에 10개씩 로드)
  - **필터링:** 모두 보기 / 읽지 않은 것 / 읽은 것 필터 탭 제공
  - **카드 리스트:** 썸네일 이미지, 제목, 설명, 읽은 시간 표기 (예: "오늘 읽음", "3일 전 읽음")
  - **삭제 기능:** 스와이프 투 딜리트(Swipe-to-delete) 또는 휴지통 아이콘 클릭 시 삭제.
    - *예외 처리:* 해당 북마크에 작성된 요약글이 존재할 경우 경고 모달 표시
- **북마크 상세 및 읽기 (Bookmark Detail & Random Read)**
  - **기능:** 원본 북마크 상세 정보 표시 및 원본 링크로 이동 기능(InAppBrowser).
  - 해당 북마크에 대한 요약글 작성/수정 화면으로의 진입점 제공.
  - **랜덤 글 읽기 기능:** 무작위 북마크를 서버에서 받아와 상세 화면으로 띄워주는 기능.

### 2.4. 요약 작성 및 관리 기능
- **요약 템플릿 기반 작성 (Summary Form)**
  - 백엔드(API)에서 내려주는 템플릿 JSON 구조에 따라 동적 UI 생성
  - **STATIC (단일 고정):** '핵심 파악' 등 무조건 렌더링되며 필수 입력해야 하는 텍스트 에어리어
  - **SELECT (단일 선택):** '세부 내용 정리' 등 여러 서브 템플릿(문제-해결형, 왜-무엇을형 등) 중 하나를 Select/Picker로 선택해 입력
  - **MULTI_SELECT (다중 선택):** '사고 확장'과 같이 다중 체크박스로 항목을 추가하면 개별 텍스트 에어리어가 동적으로 생기고, X 버튼으로 개별 제거 가능
  - **방어 로직:** 뒤로가기 제스처 시 작성 중인 내용이 지워지지 않도록 "작성 취소 확인 다이얼로그" 제공
- **요약글 목록 (Summary List)**
  - 사용자가 작성한 모든 요약글 목록 카드 노출 (원문 썸네일 표시)
- **요약글 상세 (Summary Detail)**
  - 작성된 요약글 확인 및 수정/삭제 기능

---

## 3. UI 가이드라인 (UI Guide)

웹의 **Glassmorphism(글래스모피즘)** 및 **생동감 있는 인터랙션** 기반의 Premium 디자인 시스템을 모바일에 맞게 이식합니다.

### 3.1. 색상 팔레트 (Color Palette)
- **배경색 (Background):**
  - 앱 전역 배경: `#EEF2FF` ~ `#E0E7FF` 부드러운 그라데이션 (Mesh Gradient 효과 가능)
- **포인트 색상 (Primary: Custom Vivid Purple):**
  - **메인 포인트 (Primary):** `#6324BD`
  - **활성/눌림 (Active/Pressed):** `#3F0099` (모바일에서는 TouchableHighlight 또는 Ripple 컬러로 사용)
  - **배경용 옅은 톤:** `#F3E8FF`
- **텍스트 색상 (Typography):**
  - 제목 (Heading): `#111827`
  - 본문 (Body): `#374151`
  - 상태/보조 (Muted): `#6B7280`

### 3.2. 타이포그래피 (Typography)
앱 전체의 일관성을 위해 Dual Font System을 운영하며 줄 간격(Line Height)은 `1.6`을 유지하여 가독성을 높입니다.
- **제목군 (Heading):** `GMarketSans` (적용이 어렵다면 시스템 폰트의 Bold Weight 적극 사용)
  - **H1 (대제목):** `32px ~ 36px`, Bold (페이지 진입 인사말 등)
  - **H2 (중제목):** `24px`, Bold (AppBar/Header Title, 섹션 타이틀)
- **본문군 (Body):** `Pretendard` (또는 Apple SD Gothic Neo, Roboto, Inter)
  - **본문 (Body):** `16px`, Regular/Medium
  - **캡션 (Caption):** `14px`, Regular (날짜, 시간, 부가 설명 등)

### 3.3. 여백 시스템 (Spacing)
- 간격(Margin/Padding)은 컴포넌트 간 일관성을 위해 기본적으로 **`8px` 단위**를 따릅니다 (`8px`, `16px`, `24px`, `32px` 등).
- 앱 전체 좌우 공통 마진(Padding): `16px` 번들 사용.

### 3.4. 컴포넌트 및 외곽선 (Components & Border Radius)
기존의 **Glassmorphism(유리 소재 느낌)**을 모바일에 적용할 때는 네이티브 블러(iOS: `UIVisualEffectView`, Android: `RenderEffect`)를 적절히 활용하여 성능을 챙깁니다.

- **모서리 둥글기 (Border Radius):**
  - 메인 컨테이너 / 팝업 / Bottom Sheet: `16px`
  - 카드 (Card) / 북마크 아이템: `12px`
  - 일반 버튼 (Button) / 입력 영역 (Input): `10px`
- **카드(Card) / 리스트 아이템 디자인:**
  - 배경색: 투명도를 준 흰색 `rgba(255, 255, 255, 0.85)`
  - 테두리: 미세한 흰색 보더 `1px solid rgba(255, 255, 255, 0.4)`
  - 그림자(Shadow): 다중 레이어 섀도우를 모바일 파라미터로 이식합니다. 
    - 웹 기준 `box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), ...`를 유사하게 구현.
    - iOS: `shadowColor: '#000', shadowOffset: {width: 0, height: 8}, shadowOpacity: 0.05, shadowRadius: 10`
    - Android: `elevation: 4`
- **버튼 (Button):**
  - 기본 상태: 배경 인디고 퍼플(`#4F46E5`), 텍스트 화이트, 부드러운 그림자. 
  - 상태 변화 (Active): 모바일에서는 Hover가 없으므로 터치(Press) 액션에 대한 시각/촉각 반응을 확실하게 제공합니다. 배경이 한 톤 어두워지고(`#4338CA`), 그림자가 감소하며 전체 버튼이 안으로 살짝 눌리는 효과(Scale Out, 예: 98% 축소 비율 형태)를 적용합니다.

### 3.4. 모바일 특화 인터랙션 (Micro-animations & Haptics)
- **화면 전환:** 부드러운 Slide 또는 Fade 애니메이션
- **제스처 (Gestures):**
  - 북마크 목록: 좌측 혹은 우측으로 스와이프하여 '상태 변경(읽음 처리)' 혹은 '삭제' 기능
  - Bottom Sheet 활용: 요약 템플릿의 SELECT(드롭다운) 영역은 모바일 친화적인 **Bottom Sheet Picker** 로 대체하는 것을 권장.
- **햅틱 피드백 (Haptics):** 북마크 등록 완료, 북마크 삭제, 버튼 토글 등 주요 액션 발생 시 가벼운 햅틱 진동 피드백을 적용하여 입체적인 경험 제공 
