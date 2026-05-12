pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "msa-user-service"

include(
    "user",            // 라이브러리 (msa-auth-service가 GitHub Packages로 의존)
    "user-service",    // 배포 가능한 Spring Boot 앱
)
