plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(project(":library:common"))
    implementation(project(":library:compose"))
    implementation(project(":library:datasource:pwpush"))
    implementation(project(":library:datastore"))
    implementation(project(":library:resources"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.liveevent)
    implementation(libs.google.material)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.result)
    implementation(libs.okhttp)
    kapt(libs.hilt.android.compiler)

    testImplementation(libs.junit)
}
