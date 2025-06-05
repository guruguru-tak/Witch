dependencies {

    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-cores:core-usecase"))
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-commons"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-json")
}
