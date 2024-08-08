plugins {
    war
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
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

    //basic spring
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.modelmapper:modelmapper:3.1.1")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //spring security
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.security:spring-security-test")

    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Kotlin_JDSL
    val jdslVersion = "2.2.1.RELEASE"
    implementation("org.hibernate:hibernate-core:6.2.4.Final") // up to 6
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter-jakarta:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:hibernate-kotlin-jdsl-jakarta:$jdslVersion")

    // https://mvnrepository.com/artifact/com.linecorp.kotlin-jdsl/kotlin-jdsl-reactive-core
    // https://mvnrepository.com/artifact/com.linecorp.kotlin-jdsl/kotlin-jdsl-reactive-core-jakarta
/*
    implementation("com.linecorp.kotlin-jdsl:kotlin-jdsl-reactive-core:$jdslVersion")
    implementation("com.linecorp.kotlin-jdsl:kotlin-jdsl-reactive-core-jakarta:$jdslVersion")

    runtimeOnly("com.linecorp.kotlin-jdsl:kotlin-jdsl:3.5.1")
*/

    //spy 설정
    implementation("p6spy:p6spy:3.9.1")
    implementation("com.github.gavlyukovskiy:datasource-decorator-spring-boot-autoconfigure:1.9.0")

}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
