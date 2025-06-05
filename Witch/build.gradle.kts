// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.diffplug.spotless") version "7.0.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("/*.kt")
            ktlint()
            indentWithSpaces()
            endWithNewline()
        }
        kotlinGradle {
            target("/.gradle.kts")
            ktlint()
            indentWithSpaces()
            endWithNewline()
        }
    }
}