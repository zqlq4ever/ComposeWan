plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.zqlq.compose"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.libs.common)
    api(libs.blankj.utilcodex)
    api(platform(libs.androidx.compose.bom))
    api(libs.bundles.compose.ui)
    api(libs.bundles.compose.lifecycle)
    api(libs.bundles.compose.navigation)
    api(libs.bundles.koin)
    api(libs.bundles.coil)
    debugApi(libs.bundles.compose.debug)
}
