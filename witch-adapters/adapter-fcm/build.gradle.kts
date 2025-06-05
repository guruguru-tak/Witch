dependencies {

    implementation(project(":witch-cores:core-port"))
    implementation(project(":witch-cores:core-domain"))
    implementation(project(":witch-commons"))

    implementation("org.springframework:spring-context")
    implementation("com.google.firebase:firebase-admin:9.4.3")
}
