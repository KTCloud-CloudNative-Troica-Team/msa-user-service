# msa-user-service

Troica Market Service의 **사용자 도메인** 마이크로서비스. **dual delivery** — service 앱 + `user` 라이브러리 publish (auth-service가 in-process로 사용).

> SPEC + ADR: [msa-argocd-manifest/docs](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest/tree/main/docs)
> 트러블슈팅: [TROUBLESHOOTING.md](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest/blob/main/docs/TROUBLESHOOTING.md)

---

## 빠른 시작 (L2 — 로컬 docker로 끝까지 실행)

### 사전 요구사항

| 항목 | 버전 |
|---|---|
| Java | 21 (Temurin) |
| Docker | 24+ |
| GitHub PAT | `read:packages` (common-libs + user 라이브러리 받기) |

### 1. GH Packages 인증 (1회)

`~/.gradle/gradle.properties`:
```
gpr.user=<github-username>
gpr.token=<PAT-with-read:packages>
```

### 2. PostgreSQL 컨테이너 띄우기

```bash
docker run -d --name pg-user \
  -p 7008:5432 \
  -e POSTGRES_USER=user-service \
  -e POSTGRES_PASSWORD=user-service \
  -e POSTGRES_DB=user_db \
  postgres:18-alpine
```

### 3. 빌드 + 테스트

```bash
./gradlew build
```

### 4. 로컬 실행

```bash
./gradlew :user-service:bootRun --args='--spring.profiles.active=dev'
```

기대:
```
Started UserServiceApplicationKt in X seconds
Tomcat started on port 8004 (http)
```

### 5. 검증

```bash
curl -s http://localhost:8004/healthz | jq
# {"status":"UP",...}
```

### 6. Docker로 실행

```bash
./gradlew :user-service:bootJar
docker build -t msa/user-service:local .

docker run --rm \
  -p 8004:8004 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e USER_DB_HOST=host.docker.internal \
  msa/user-service:local
```

### 7. 정리

```bash
docker rm -f pg-user
```

---

## 모듈 구조 (dual delivery)

```
msa-user-service/
├── user/                # 도메인 라이브러리 — JPA entities, services, ports
│                        # GH Packages에 publish (auth-service가 in-process 의존)
└── user-service/        # Spring Boot 앱 — REST controller + main
```

### Dual delivery 패턴

- `user` 라이브러리: `com.troica.msa:user:0.1.0` (GH Packages `msa-user-service` 레포)
- `user-service`: Docker image (ECR)

**auth-service**가 `user` 라이브러리를 **in-process 의존성**으로 사용 (회원가입 시 user 도메인 호출). 별도 gRPC 통신 불필요 — 두 도메인의 강한 결합 + 트랜잭션 일관성 유지.

---

## 포트

| 프로토콜 | 포트 | 용도 |
|---------|------|------|
| HTTP | 8004 | REST + Actuator |
| gRPC | (미사용) | 향후 service-to-service 호출 시 9004 활성화 |

---

## 환경 변수

| 변수 | 기본값 | 설명 |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | (none) | `dev` / `prod` |
| `USER_DB_HOST` | localhost | PostgreSQL host |
| `USER_DB_PORT` | 7008 | PostgreSQL port |
| `USER_DB_NAME` | user_db | DB name |
| `USER_DB_USERNAME` | user-service | DB user |
| `USER_DB_PASSWORD` | user-service | DB password |
| `JPA_DDL_AUTO` | validate / update (dev) | Hibernate 스키마 정책 |
| `SERVER_PORT` | 8004 | HTTP listen |

---

## 외부 의존성

| 의존 | 용도 | 로컬 실행 시 |
|------|------|-------------|
| PostgreSQL `user_db` | 도메인 데이터 | `postgres:18-alpine` 컨테이너 (위 STEP 2) |
| `com.troica.msa:common:0.3.1` | JPA/QueryDSL config, Base 엔티티 | GH Packages 자동 |

**Kafka, Redis 미사용.**

---

## `user` 라이브러리 publish (별도 워크플로우)

`user-service` 앱 외에 **`user` 라이브러리도 GH Packages에 publish** — auth-service가 그것을 받아서 사용.

### 로컬 publish (개발용)

```bash
./gradlew :user:publishToMavenLocal
```

### Release publish

```bash
# 본 레포의 publish workflow가 tag push 시 트리거 (별도 정책)
# 또는 수동:
GITHUB_ACTOR=<user> GITHUB_TOKEN=<PAT> \
  ./gradlew :user:publish
```

consumer (auth-service)의 `build.gradle.kts`:
```kotlin
implementation("com.troica.msa:user:0.1.0")
```

---

## CI/CD

`.github/workflows/ci.yml`:
- **PR**: `build-test` (호스트에서 `./gradlew build`)
- **Push to main**: build → Docker → Trivy → ECR push → manifest auto-bump PR
  - push-gated step: `vars.AWS_DEPLOYMENTS_ENABLED == 'true'` 일 때만

빌드 시간: ~2-3분 (R-27 (a) 적용).

---

## 트러블슈팅

- **공용/user 라이브러리 다운로드 실패** → `~/.gradle/gradle.properties`의 인증 + PAT `read:packages` 권한 확인
- **`@Configuration class may not be final`** → common-libs 0.3.1+ 사용 ([TROUBLESHOOTING §1.7](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest/blob/main/docs/TROUBLESHOOTING.md#17-kotlin-configuration-class가-final--spring-cglib-proxy-실패-r-38))
- **PostgreSQL connection refused** → 컨테이너 띄움 확인 (`docker ps | grep pg-user`)

---

## 관련 문서

- [msa-argocd-manifest](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest) — `applications/values/user-service/`
- [msa-auth-service](https://github.com/KTCloud-CloudNative-Troica-Team/msa-auth-service) — `user` 라이브러리 consumer
- [msa-common-libs](https://github.com/KTCloud-CloudNative-Troica-Team/msa-common-libs)
- [TROUBLESHOOTING.md](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest/blob/main/docs/TROUBLESHOOTING.md)
