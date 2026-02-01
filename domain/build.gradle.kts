plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = rootProject.extra["javaVersion"] as JavaVersion
    targetCompatibility = rootProject.extra["javaVersion"] as JavaVersion
}

kotlin {
    jvmToolchain(rootProject.extra["jdkVersion"] as Int)
}

dependencies {
    implementation(libs.javax)

    // Coroutines
    implementation(libs.coroutines)
}
