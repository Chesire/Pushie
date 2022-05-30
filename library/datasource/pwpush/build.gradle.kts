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

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.michael-bull.kotlin-result:kotlin-result:1.1.16")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    testImplementation("junit:junit:4.13.2")
}
