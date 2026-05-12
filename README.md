# msa-user-service

Troica Market Service의 **사용자 도메인** 마이크로서비스.

> Single source of truth: [TROICA_SPEC.md](https://github.com/KTCloud-CloudNative-Troica-Team/msa-argocd-manifest/blob/main/TROICA_SPEC.md)

## 모듈 구조 (dual delivery)

```
msa-user-service/
├── user/           # 라이브러리 (com.troica.msa:user:x.y.z, GitHub Packages publish)
└── user-service/   # Spring Boot 앱 (Docker image, ECR push)
```

- `user/` 라이브러리는 **`msa-auth-service` 가 in-process 의존**으로 사용 (모노레포 auth-service가
  `CreateUserCommand`를 직접 import해서 호출하는 패턴 유지).
- `user-service/` 는 user 도메인 REST API를 외부에 노출하는 별도 deployable.

## Phase 4 작업 요약 (D1/Q2)

- 모노레포 `user/` 모듈 단독 추출 (identification 폐기됨, auth는 별도 `msa-auth-service`).
- 모노레포에는 user-service deployable이 없었으므로 `@SpringBootApplication` + `application.yaml` +
  `UserRestControllerAdapter` 신규 작성.
- `UserRestController` 인터페이스는 그대로 (모노레포에 정의), 본 레포에서 `@RestController` 구현.

## 포트 / 의존성

| 항목 | 값 |
|------|----|
| HTTP / Actuator | 8004 (SPEC §1.4) |
| gRPC | 미노출 (PoC에서는 REST 전용. 향후 service-to-service 필요 시 9004 활성화) |
| PostgreSQL | `user_db` |
| common-libs | `com.troica.msa:common:0.3.0` |

## 빌드 + 실행

```bash
(cd ../msa-common-libs && ./gradlew publishToMavenLocal -Pversion=0.3.0)
./gradlew build -x test

docker build \
  --build-arg GPR_USER=$GITHUB_ACTOR \
  --build-arg GPR_TOKEN=$GITHUB_TOKEN \
  -t msa/user-service:local .

docker run --rm -p 8004:8004 \
  -e SPRING_PROFILES_ACTIVE=dev \
  msa/user-service:local
```

## 릴리스 (user 라이브러리 publish)

```bash
git tag v0.1.0
git push origin v0.1.0
```

`.github/workflows/publish.yml`이 `com.troica.msa:user:0.1.0`을 GitHub Packages로 push.
이후 `msa-auth-service`는 `implementation("com.troica.msa:user:0.1.0")` 로 의존.

## CI

- PR + push: `build-test`
- Push to main + `vars.AWS_DEPLOYMENTS_ENABLED == 'true'`: ECR push + manifest update (Phase 0 후)
- Tag push (`v*`): `user/` 라이브러리 → GitHub Packages publish
