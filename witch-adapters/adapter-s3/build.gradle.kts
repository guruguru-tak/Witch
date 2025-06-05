dependencies {
    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))

    implementation("org.springframework:spring-context")
    implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.4")
}
