plugins {
    kotlin("kapt")
    kotlin("plugin.jpa")
}

dependencies {
    // common-libs v0.3.0 (모노레포의 project(":common"))
    implementation("com.troica.msa:common:0.3.1")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // UserRestController 인터페이스가 REST DTO에 의존 — Web starter 필요
    // 실제 @RestController 어노테이션은 user-service 모듈의 Adapter 클래스에 있음
    implementation("org.springframework.boot:spring-boot-starter-web")

    // R-57: 단위 테스트 — JUnit 5 + AssertJ + Mockito
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Gradle 8.x + JUnit Platform 1.12+ 에서 launcher 명시 필요 (OutputDirectoryProvider)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
