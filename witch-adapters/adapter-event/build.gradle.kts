dependencies {
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-commons"))

    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework:spring-context")
}
