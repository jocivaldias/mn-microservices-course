plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    id("org.jetbrains.kotlin.kapt") version "1.4.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.10" // ALL OPEN FOR MOCKING
    id("org.jetbrains.kotlin.plugin.jpa") version "1.4.21" // NO ARGS FOR ENTITY
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.2.0"
}

version = "0.1"
group = "com.jocivaldias"

val kotlinVersion=project.properties.get("kotlinVersion")
val micronautVersion=project.properties.get("micronautVersion")
repositories {
    mavenCentral()
    jcenter()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.jocivaldias.*")
    }
}

dependencies {
    kapt("io.micronaut.openapi:micronaut-openapi:2.3.0")
    kapt("io.micronaut:micronaut-inject-java:${micronautVersion}")

    implementation("io.micronaut:micronaut-validation")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    // SECURITY
    implementation("io.micronaut.security:micronaut-security-annotations") //Security
    implementation("io.micronaut.security:micronaut-security-jwt") //Security

    // SWAGGER
    implementation("io.swagger.core.v3:swagger-annotations") //Swagger

    // JPA & DATABASE
    annotationProcessor("io.micronaut.data:micronaut-data-processor")// DATA Annotation processor
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")// JPA
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")// Hikari pool
    implementation("io.micronaut.flyway:micronaut-flyway") // flyway
    implementation("org.postgresql:postgresql:42.2.18") //Postgres

    // Testes
    kaptTest("io.micronaut:micronaut-inject-java")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5:2.3.1")
    testImplementation("org.mockito:mockito-core:3.6.28")
    testImplementation("org.mockito:mockito-junit-jupiter:3.6.28")
    testImplementation("io.mockk:mockk:1.10.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

allOpen {
    annotations(
            "io.micronaut.aop.Around"
    )
}

application {
    mainClass.set("com.jocivaldias.Application")
}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

kapt {
    arguments {
        arg("micronaut.openapi.views.spec", "redoc.enabled=true,rapidoc.enabled=true,swagger-ui.enabled=true,swagger-ui.theme=flattop")
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

