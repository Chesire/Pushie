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
}

dependencies {
    implementation(project(":library:common"))
    implementation(project(":library:datastore"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.result)
    implementation(libs.okhttp)

    testImplementation(libs.junit)
}
