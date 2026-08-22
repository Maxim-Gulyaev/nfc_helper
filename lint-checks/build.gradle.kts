import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    // No version: the Kotlin Gradle plugin is already on this build's classpath,
    // so requesting it again with a version fails plugin resolution.
    kotlin("jvm")
    alias(libs.plugins.android.lint)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
        freeCompilerArgs.add("-Xexplicit-backing-fields")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // lint-api is compileOnly so it is not bundled into the published lint jar;
    // tests still need it on the compile classpath (lint-tests only brings
    // com.android.tools.lint:lint at runtime scope).
    compileOnly(libs.android.lint.api)
    testImplementation(libs.android.lint.api)
    testImplementation(libs.junit)
    testImplementation(libs.android.lint.tests)
}
