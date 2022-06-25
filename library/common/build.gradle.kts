plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines.core)
    kapt(libs.hilt.android.compiler)
}
