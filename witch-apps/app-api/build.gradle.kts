dependencies {
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-cores:core-usecase"))
    implementation(project(":witch-cores:core-port")) // 삭제예정
    implementation(project(":witch-commons"))

    runtimeOnly(project(":witch-apps:kafka-subscriber"))

    runtimeOnly(project(":witch-adapters:adapter-persistence"))
    runtimeOnly(project(":witch-adapters:adapter-redis"))
    runtimeOnly(project(":witch-adapters:adapter-generator"))
    runtimeOnly(project(":witch-adapters:adapter-mail"))
    runtimeOnly(project(":witch-adapters:adapter-event"))
    runtimeOnly(project(":witch-adapters:adapter-security"))
    runtimeOnly(project(":witch-adapters:adapter-s3"))
    runtimeOnly(project(":witch-adapters:adapter-kafka-publisher"))
    runtimeOnly(project(":witch-adapters:adapter-fcm"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.data:spring-data-commons")

    implementation("org.flywaydb:flyway-core")

    testImplementation(project(":witch-tests"))
    testImplementation(project(":witch-adapters:adapter-security"))
}

val appMainClassName = "com.ssafy.witch.WitchApiApplication"
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set(appMainClassName)
    archiveClassifier.set("boot")
}