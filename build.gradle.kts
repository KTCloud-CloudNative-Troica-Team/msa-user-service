import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    java
    kotlin("jvm") version "2.1.0" apply false
    kotlin("kapt") version "2.1.0" apply false
    kotlin("plugin.spring") version "2.1.0" apply false
    kotlin("plugin.jpa") version "2.1.0" apply false
    id("org.springframework.boot") version "3.5.14" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.troica.msa"
    version = providers.gradleProperty("version").get()

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            name = "GitHubPackagesCommonLibs"
            url = uri("https://maven.pkg.github.com/KTCloud-CloudNative-Troica-Team/msa-common-libs")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN")
                    ?: providers.gradleProperty("gpr.token").orNull
            }
        }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
        jvmToolchain(21)
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.14")
        }
    }

    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-reflect")
        "implementation"("com.fasterxml.jackson.module:jackson-module-kotlin")
    }

    plugins.withId("org.jetbrains.kotlin.kapt") {
        tasks.matching { it.name == "sourcesJar" }.configureEach {
            dependsOn("kaptKotlin")
        }
    }
}

// ===== 라이브러리 publishing — `user` 모듈만 GitHub Packages로 publish =====
// (msa-auth-service가 com.troica.msa:user:x.y.z 로 의존)
project(":user") {
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    extensions.configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }

    extensions.configure<PublishingExtension> {
        publications {
            register<MavenPublication>("library") {
                from(components["java"])
                artifactId = "user"
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/KTCloud-CloudNative-Troica-Team/msa-user-service")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                        ?: providers.gradleProperty("gpr.user").orNull
                    password = System.getenv("GITHUB_TOKEN")
                        ?: providers.gradleProperty("gpr.token").orNull
                }
            }
        }
    }
}
