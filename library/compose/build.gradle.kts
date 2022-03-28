plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31

        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0"
    }
}

dependencies {
    implementation("androidx.compose.material:material:1.1.0")
    implementation("androidx.compose.runtime:runtime:1.1.1")
    implementation("androidx.compose.ui:ui-tooling:1.1.0")
}
