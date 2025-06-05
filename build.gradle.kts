import com.linecorp.support.project.multi.recipe.configureByLabels
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.asciidoctor.gradle.jvm.AsciidoctorTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.linecorp.build-recipe-plugin") version Versions.lineRecipePlugin
    id("java")
    id("jacoco")
    id("io.spring.dependency-management") version Versions.springDependencyManagementPlugin apply false
    id("org.springframework.boot") version Versions.springBoot apply false
    id("io.freefair.lombok") version Versions.lombokPlugin apply false
    id("com.coditory.integration-test") version Versions.integrationTestPlugin apply false
    id("org.asciidoctor.jvm.convert") version Versions.asciidoctorPlugin apply false
    id ("org.sonarqube") version "4.4.0.3356"
}

allprojects {
    group = "com.ssafy"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "idea")
    apply(plugin = "jacoco")    // Java 플러그인 적용

    // JaCoCo 버전 설정
    jacoco {
        toolVersion = "0.8.11"  // JaCoCo 최신 안정 버전 사용
    }

    val jacocoRootReport = tasks.register<JacocoReport>("jacocoRootReport") {
        description = "Generates an aggregate report from all subprojects"
        group = "verification"

        // 서브프로젝트들의 소스 디렉토리와 클래스 파일 수집
        subprojects.forEach { subproject ->
            subproject.plugins.withType<JacocoPlugin> {
                executionData.from(fileTree(subproject.layout.buildDirectory).include("/jacoco/*.exec"))
                sourceDirectories.from(subproject.files("src/main/java"))
                classDirectories.from(subproject.files("build/classes/java/main"))
            }
        }

        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.required.set(true)
        }
    }

    // 테스트 설정
    tasks.withType<Test> {
        finalizedBy(tasks.withType<JacocoReport>())  // 테스트 후 JaCoCo 리포트 생성
        finalizedBy(jacocoRootReport)
    }

    // 각 모듈의 JaCoCo 리포트 설정
    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)  // XML 리포트 생성
            csv.required.set(false) // CSV 리포트 비활성화
            html.required.set(true) // HTML 리포트 생성
        }
    }
}


configureByLabels("java") {
    apply(plugin = "org.gradle.java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "com.coditory.integration-test")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${Versions.springBoot}")
        }

        dependencies {
            dependency("com.navercorp.fixturemonkey:fixture-monkey-starter:${Versions.fixtureMonkey}")
            dependency("org.mapstruct:mapstruct:${Versions.mapstruct}")
            dependency("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")

            dependencySet("io.jsonwebtoken:${Versions.jwt}") {
                entry("jjwt-api")
                entry("jjwt-impl")
                entry("jjwt-jackson")
            }
        }
    }

    dependencies {
        val implementation by configurations
        val annotationProcessor by configurations

        val testImplementation by configurations
        val testRuntimeOnly by configurations

        val integrationImplementation by configurations
        val integrationRuntimeOnly by configurations

        implementation("org.mapstruct:mapstruct")

        annotationProcessor("org.mapstruct:mapstruct-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")

        integrationImplementation("org.springframework.boot:spring-boot-starter-test")
        integrationImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter")
        integrationRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
}

configureByLabels("boot") {
    apply(plugin = "org.springframework.boot")

    tasks.getByName<Jar>("jar") {
        enabled = false
    }

    tasks.getByName<BootJar>("bootJar") {
        enabled = true
        archiveClassifier.set("boot")
    }
}

configureByLabels("library") {
    apply(plugin = "java-library")

    tasks.getByName<Jar>("jar") {
        enabled = true
    }
}

configureByLabels("restdocs") {
    apply(plugin = "org.asciidoctor.jvm.convert")

    dependencies {
        val testImplementation by configurations
        val asciidoctorExt by configurations.creating

        asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    }

    val snippetsDir = file("build/generated-snippets")

    tasks.named<Test>("test") {
        doFirst {
            delete(snippetsDir)
        }
        outputs.dir(snippetsDir)
        finalizedBy("asciidoctor")
    }

    tasks.register<Copy>("copyDocument") {
        dependsOn("asciidoctor")
        from(file("build/docs/asciidoc"))
        into(file("src/main/resources/static/docs"))
    }

    tasks.named<AsciidoctorTask>("asciidoctor") {
        configurations("asciidoctorExt")
        outputs.dir(file("build/docs"))

        baseDirFollowsSourceFile()

        sources(delegateClosureOf<PatternSet> {
            include("index.adoc")
        })

        finalizedBy("copyDocument")
    }
}

configureByLabels("querydsl") {
    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("com.querydsl:querydsl-bom:${Versions.querydsl}")
        }

        dependencies {
            dependency("com.querydsl:querydsl-core:${Versions.querydsl}")
            dependency("com.querydsl:querydsl-jpa:${Versions.querydsl}")
            dependency("com.querydsl:querydsl-apt:${Versions.querydsl}")
        }
    }

    dependencies {
        val implementation by configurations
        val annotationProcessor by configurations

        implementation("com.querydsl:querydsl-jpa:${Versions.querydsl}:jakarta")
        implementation("com.querydsl:querydsl-core:${Versions.querydsl}")

        annotationProcessor("com.querydsl:querydsl-apt:${Versions.querydsl}:jakarta")
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    }
}
