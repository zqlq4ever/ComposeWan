plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        compileSdk = 37
        namespace = "com.zqlq.network"
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.ktor)
            api(projects.libs.common)
        }
        androidMain.dependencies {
            api(libs.ktor.client.android)
        }
    }
}
