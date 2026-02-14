plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    android {
        compileSdk = 36
        namespace = "com.zqlq.network"
    }

    sourceSets {
        commonMain.dependencies {
            // Ktor
            api(libs.ktor.client.core)
            api(libs.ktor.client.cio)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.logging)
            // Common module
            api(projects.libs.common)
        }
        androidMain.dependencies {
            api(libs.ktor.client.android)
        }
    }
}
