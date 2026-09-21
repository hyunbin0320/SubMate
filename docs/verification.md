# 검증 결과 — 2026-09-21

## 결과

| 확인 항목 | 결과 | 근거 |
|---|---|---|
| Backend 통합 테스트 | 통과 | Gradle test, 총 24건 / 실패 0 / 오류 0 |
| Frontend 프로덕션 빌드 | 통과 | Vite build |
| Frontend 정적 검사 | 통과 | oxlint, 경고/오류 없음 |
| Git 공백 검사 | 통과 | git diff --check |
| 브라우저 결제 | 통과 | 구독 #1 생성, Cinema 12,900원, 완료 화면 |
| 브라우저 해지 | 통과 | 해지 예약, 2026-10-21 00:00 전까지 이용 가능 |
| 브라우저 환불 | 통과 | 요청 → 검토 중 → 관리자 승인 → 완료 |
| 결제/구독 연동 | 통과 | 환불 완료 결제, 구독 이용 종료/이용 불가능 |
| 데이터 격리 | 통과 | 다른 회원 로그인 시 구독 0건 |
| 실패 결제 | 통과 | 실패 안내, 결제 내역 0건 |
| 정상 흐름 브라우저 콘솔 | 통과 | error/warn 목록 없음 |

테스트 구성: BackendApplicationTests 1건, SubscriptionFlowTests 21건,
DemoSecurityTests 2건. 날짜 파라미터 테스트를 포함한 실행 횟수입니다.

검증 경로: React UI → Vite /api 프록시 → Spring Boot Controller/Service → H2 DB → DTO → UI.
자동 만료는 테스트 Clock을 한국 시간 자정 전/후로 이동하여 검증했습니다.

## 검증 중 수정한 문제

- Windows에서 authContext.js와 AuthContext.jsx의 이름이 충돌하던 문제:
  Context 정의를 authState.js로 분리하고 JSX import를 명시.
- demo Security의 CorsConfigurationSource 빈 중복:
  기존 CORS 빈을 Qualifier로 지정.
- loopback Origin POST 차단:
  기존 localhost에 127.0.0.1:5173을 허용하고 회귀 테스트 추가.
- OneDrive build 파일 잠금:
  선택적 submateBuildDir 속성으로 임시 빌드 폴더 사용.
- 샌드박스에서 oxlint 네이티브 모듈 실행 차단:
  승인된 실행 환경에서 lint를 재실행하여 통과 확인.

## 아직 검증하지 않은 영역

실제 MySQL, 실제 JWT, 회원가입/상품 CRUD와의 팀 통합, 모바일 기기별 동작,
외부 배포는 검증 범위에 포함하지 않았습니다.
