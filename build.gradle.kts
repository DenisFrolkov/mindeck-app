// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.android.lint) apply false
}

buildscript {
    extra.apply {
        set("compileSdk", 36)
        set("targetSdk", 36)
        set("minSdk", 26)
        set("versionCode", 2)
        set("versionName", "1.1.0")
        set("javaVersion", JavaVersion.VERSION_17)
        set("jvmTarget", "17")
        set("jdkVersion", 17)
    }

    dependencies {
        classpath(libs.hilt.android.gradle)
        classpath(libs.spotless.gradle)
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("${layout.buildDirectory.get()}/**/*.kt")

            ktlint("1.5.0")
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint("1.5.0")
        }
    }
}
