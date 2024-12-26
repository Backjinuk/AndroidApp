plugins {
    war
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("org.jetbrains.kotlin.kapt") version "1.9.24" // `kapt` 플러그인 추가
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
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

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.modelmapper:modelmapper:3.1.1")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //벨리데이션 라이브러리
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //Kotlin JDSL
//    val jdslVersion = "2.2.1.RELEASE"
//    implementation("org.hibernate:hibernate-core:6.2.4.Final")
//    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:$jdslVersion")
//    implementation("com.linecorp.kotlin-jdsl:hibernate-kotlin-jdsl-jakarta:$jdslVersion")

     val queryDslVersion = "5.0.0"
    implementation("com.querydsl:querydsl-core:$queryDslVersion")
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta") // JPA Jakarta 버전
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // Annotation Processor 설정 (Jakarta 버전 사용 시)
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta") // `kapt`를 사용해 QueryDSL QClass 생성

     // Spy settings
     implementation("p6spy:p6spy:3.9.1")
     implementation("com.github.gavlyukovskiy:datasource-decorator-spring-boot-autoconfigure:1.9.0")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

/**
 * QueryDSL Build Options
 */
//val querydslDir = "src/main/generated" // QueryDSL(QClass) 생성 경로 설정
//
//sourceSets {
//    getByName("main").java.srcDirs(querydslDir)
//}
//
//tasks.withType<JavaCompile> { // JavaCompile 설정 추가 compile시 QueryDSL(QClass) 코드를 생성하도록 설정
//    options.generatedSourceOutputDirectory = file(querydslDir)
//    options.compilerArgs.add("-parameters")
//}


tasks.withType<Test> {
    useJUnitPlatform()
}
