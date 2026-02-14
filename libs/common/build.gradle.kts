plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.zqlq.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // ==================== BOM ====================
    api(platform(libs.androidx.compose.bom))

    // ==================== Google 官方库 ====================
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.compose)
    api(libs.androidx.activity.compose)
    api(libs.androidx.navigation.compose)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.core)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.foundation)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ==================== 第三方库 ====================
    api(libs.coil.compose)
    api(libs.richeditor.compose)
    api(libs.material.dialogs.core)
    api(libs.material.dialogs.datetime)
    api(libs.material.dialogs.color)
    api(libs.sheets.dialogs.core)
    api(libs.sheets.dialogs.calendar)
    api(libs.sheets.dialogs.clock)
    api(libs.lottie.compose)
    api(libs.accompanist.permissions)
    api(libs.toaster)
    api(libs.mmkv)
    api(libs.kotlinx.serialization.json)
    api(libs.timber)
}