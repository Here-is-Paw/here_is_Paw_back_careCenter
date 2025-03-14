plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ll"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-security")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // PostgreSQL 드라이버 (한 번만 선언하고 버전 명시)
    implementation("org.postgresql:postgresql:42.7.5")

    // Hibernate Spatial 지원
    implementation("org.hibernate.orm:hibernate-spatial:6.6.5.Final")

    // JTS 지리 데이터 지원
    implementation("org.locationtech.jts:jts-core:1.19.0")

    // PostgreSQL JDBC 드라이버의 PostGIS 확장 추가
    implementation("net.postgis:postgis-jdbc:2.5.1")
    implementation("net.postgis:postgis-geometry:2.5.1")

    implementation ("org.springframework.boot:spring-boot-starter-actuator")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
