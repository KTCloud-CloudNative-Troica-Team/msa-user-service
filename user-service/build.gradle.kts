plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":user"))

    implementation("com.troica.msa:common:0.3.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    runtimeOnly("org.postgresql:postgresql:42.7.11")
}

springBoot {
    mainClass.set("dev.ktcloud.black.user.service.UserServiceApplicationKt")
}
