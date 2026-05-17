plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":user"))

    implementation("com.troica.msa:common:0.3.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // R-65 (평가 기본 (3)-3 필수): Prometheus text format 의 /actuator/prometheus
    // endpoint 노출. spring-boot-starter-actuator 만으로는 /actuator/metrics (JSON)
    // 만 노출되고 /actuator/prometheus 는 404. 본 의존성 + msa-argocd-manifest
    // PR #120 의 MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE env 와 함께 노출 →
    // ServiceMonitor scrape OK → Grafana Troica dashboard panel data 표시.
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")

    runtimeOnly("org.postgresql:postgresql:42.7.11")
}

springBoot {
    mainClass.set("dev.ktcloud.black.user.service.UserServiceApplicationKt")
}
