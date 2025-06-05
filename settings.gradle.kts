pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://maven.springframework.org/release")
        }
        maven {
            url = uri("https://maven.restlet.com")
        }
    }
}

rootProject.name = "witch"

include("witch-apps:app-api")
include("witch-apps:kafka-subscriber")

include("witch-adapters:adapter-persistence")
include("witch-adapters:adapter-redis")
include("witch-adapters:adapter-mail")
include("witch-adapters:adapter-generator")
include("witch-adapters:adapter-event")
include("witch-adapters:adapter-security")
include("witch-adapters:adapter-s3")
include("witch-adapters:adapter-kafka-publisher")
include("witch-adapters:adapter-fcm")

include("witch-cores:core-domain")
include("witch-cores:core-port")
include("witch-cores:core-service")
include("witch-cores:core-usecase")

include("witch-commons")

include("witch-tests")
