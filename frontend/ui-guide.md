# UI Guide

## Tech stack

- React
- TypeScript

## 1. Color (Premium & Vibrant)

- **배경 (Background):**
  - 앱 전체 배경은 입체감 있는 Mesh Gradient 또는 은은하고 깊이 있는 색상을 사용합니다.
  - `background-start`: `#EEF2FF` (아주 연한 푸른빛의 스노우 화이트)
  - `background-end`: `#E0E7FF` (부드러운 라벤더 블루)
- **포인트 색상 (Primary Colors - Custom Vivid Purple):**
  - 메인(Primary): `#6324BD` (선명하면서 깊이 있는 매력적인 보라색)
  - 호버(Hover): `#8748E1`
  - 액티브(Active): `#3F0099`
  - 라이트/배경용(Light): `#F3E8FF` (메인 컬러 기반의 옅은 보라색)
- **텍스트 색상 (Typography Colors - Grayscale):**
  - 제목(Heading): `#111827` (가장 짙은 회색)
  - 본문(Body): `#374151` (편안한 짙은 회색)
  - 보조 텍스트(Muted): `#6B7280`

## 2. Typography (Dual Font System)

- **본문 기본 폰트:** `Pretendard` (또는 시스템 폰트 `Inter` 계열) - 탁월한 가독성과 모던한 형태
- **포인트/제목 폰트:** `GMarketSans` - 로고나 대제목(H1, H2)에 제한적으로 사용하여 브랜드 아이덴티티 강조
- **크기 및 두께:**
  - H1(Title): 32px ~ 36px, Bold
  - H2(Subtitle): 24px, Bold
  - 본문(Body): 16px, Regular/Medium
  - 캡션(Caption): 14px, Regular
- **줄 간격(Line Height):** 1.6 (가독성 유지)

## 3. Style & Layout (Glassmorphism & Depth)

- **전체적인 분위기:** 깔끔하고 세련된 미니멀리즘 + 생동감 있는 마이크로 인터랙션
- **레이아웃 구조:** 반응형 중앙 정렬 컨테이너(최대 768px). 모바일과 웹에서 일관된 비율 제공.
- **메인 컨테이너 (Glassmorphism 적용):**
  - 모서리 둥글기(Border Radius): `16px` (기존 8px에서 상향)
  - 배경: `rgba(255, 255, 255, 0.85)` (반투명 화이트)
  - 효과: `backdrop-filter: blur(12px)` (배경 블러 효과)
  - 테두리: `1px solid rgba(255, 255, 255, 0.4)` (유리 질감을 살리는 섬세한 보더)
  - 그림자(Shadow): `box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.05), 0 8px 10px -6px rgba(0, 0, 0, 0.01)` (다중 레이어 섀도우로 더 깊고 부드러운 입체감)
- **네비게이션 & 푸터:** 상단 고정 네비게이션, 하단 간단한 푸터 유지.

## 4. Components

- **버튼 (Button):**
  - 모서리 둥글기: `10px`
  - 상태별 시각적 변화:
    - **기본 상태:** 배경 인디고 퍼플(`#4F46E5`), 글자 `white`, 은은한 그림자 `box-shadow: 0 4px 6px -1px rgba(79, 70, 229, 0.2)`
    - **마우스 호버 (Hover):** 배경이 밝아지며(`#6366F1`) 그림자가 살짝 커지고 위로 떠오르는 효과(Y축 `-2px` 이동)
    - **눌렀을 때 (Active):** 배경이 어두워지고(`#4338CA`) 안으로 살짝 눌리는 효과(Y축 `+1px` 이동), 그림자 감소
- **카드 (Card):**
  - 메인 컨테이너와 유사한 Glassmorphism 또는 깔끔한 화이트 모듈로 구성. 둥근 모서리(`12px`), 모던한 다중 섀도우 적용.

## 5. Spacing

- 여백 시스템은 8px 단위를 따름 (8px, 16px, 24px, 32px 등). 레이아웃을 해치지 않는 일관성 유지.

## 6. Interaction (Micro-animations)

- **부드러운 전환:** 모든 색상, 위치, 그림자 변화에는 `transition: all 0.25s ease-in-out` 효과를 적용.
- 웹 페이지의 정적인 느낌을 탈피하고, 사용자의 작은 동작(Hover, Click)에 즉각적이고 생동감 있게 반응하도록 설계.
