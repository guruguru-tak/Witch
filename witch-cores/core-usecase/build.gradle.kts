dependencies {
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-commons"))

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("io.jsonwebtoken:jjwt-api")
    implementation("io.jsonwebtoken:jjwt-impl")
    implementation("io.jsonwebtoken:jjwt-jackson")
}
