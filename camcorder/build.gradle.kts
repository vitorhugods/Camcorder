import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "xyz.schwaab.compose"
version = "1.0-SNAPSHOT"


kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation("com.squareup:gifencoder:0.10.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
                implementation("com.squareup.okio:okio:3.1.0")
            }
        }
        val jvmTest by getting
    }
}

