plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.zqlq.common"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.bundles.coroutines)
    api(libs.kotlinx.serialization.json)
    api(libs.tencent.mmkv)
    api(libs.toaster)
    api(libs.timber)
}
