dependencies {
    implementation(project(":witch-cores:core-usecase"))
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-commons"))

    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")

    implementation("org.springframework.security:spring-security-core")

}
