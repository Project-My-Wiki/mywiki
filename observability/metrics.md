# Prometheus Metrics 정의

MyWiki 서비스를 모니터링하기 위해 수집할 메트릭 목록입니다.

## 1. 시스템 및 인프라 (Standard)
Spring Boot Actuator가 기본적으로 제공하는 메트릭입니다.

| Metric Name | Description | Type |
|---|---|---|
| `system.cpu.usage` | 전체 시스템 CPU 사용률 | Gauge |
| `process.cpu.usage` | 애플리케이션 프로세스 CPU 사용률 | Gauge |
| `jvm.memory.used` / `committed` / `max` | JVM 메모리 (Heap/Non-heap) 사용량 | Gauge |
| `disk.free` / `total` | 디스크 여유 공간 및 전체 공간 | Gauge |
| `jvm.threads.live` | 현재 활성화된 스레드 수 | Gauge |
| `jvm.gc.pause` | GC 수행 시간 및 횟수 | Summary |

## 2. 애플리케이션 표준 메트릭 (Standard)
Spring Boot Web, JDBC 관련 표준 메트릭입니다.

| Metric Name | Description | Type |
|---|---|---|
| `http.server.requests` | HTTP 요청 처리 시간, 횟수, 상태 코드 | Summary |
| `hikaricp.connections.active` | DB 커넥션 풀 활성 연결 수 | Gauge |
| `hikaricp.connections.idle` | DB 커넥션 풀 유휴 연결 수 | Gauge |
| `hikaricp.connections.pending` | DB 커넥션 획득 대기 수 | Gauge |

## 3. 커스텀 비즈니스 메트릭 (Custom)
비즈니스 로직과 관련된 모니터링을 위해 코드를 수정하여 추가할 메트릭입니다.

| Metric Name | Description | Tags | Type |
|---|---|---|---|
| `mywiki_bookmark_created` | 북마크 생성 횟수 | - | Counter |
| `mywiki_bookmark_read` | 북마크 읽음 상태 변경 횟수 | `status` (read, unread) | Counter |
| `mywiki_summary_created` | 요약 생성 횟수 | - | Counter |
| `mywiki_metadata_fetch_duration` | URL 메타데이터 조회 소요 시간 | `status` (success, failure) | Timer |
| `mywiki_metadata_fetch_errors` | URL 메타데이터 조회 실패 횟수 | - | Counter |

---

# Grafana Dashboard Queries

Grafana에서 대시보드를 구성할 때 사용할 수 있는 PromQL 쿼리 예시입니다.

## System status
- **CPU Usage**: `system_cpu_usage`
- **Heap Memory Used**: `sum(jvm_memory_used_bytes{area="heap"})`
- **Active Threads**: `jvm_threads_live_threads`

## Traffic & Performance
- **HTTP Request Rate (per sec)**: `rate(http_server_requests_seconds_count[1m])`
- **HTTP Error Rate (5xx)**: `rate(http_server_requests_seconds_count{status=~"5.."}[1m])`
- **Average Response Time**: `rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])`
- **DB Active Connections**: `hikaricp_connections_active`

## Business Metrics
- **Total Bookmarks Created**: `sum(increase(mywiki_bookmark_created_total[1h]))` (최근 1시간)
- **Total Summaries Created**: `sum(increase(mywiki_summary_created_total[1h]))` (최근 1시간)
- **Metadata Fetch Latency (Avg)**: `rate(mywiki_metadata_fetch_duration_seconds_sum[5m]) / rate(mywiki_metadata_fetch_duration_seconds_count[5m])`
- **Metadata Fetch Errors**: `increase(mywiki_metadata_fetch_errors_total[1h])`
