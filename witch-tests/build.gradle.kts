dependencies {
    implementation(project(":witch-adapters:adapter-security"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-json")

    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    implementation("org.springframework.security:spring-security-test")
}
