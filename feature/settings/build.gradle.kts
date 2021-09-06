plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 30
    defaultConfig {
        minSdk = 21
        targetSdk = 30

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":library:datastore"))
    implementation(project(":library:resources"))

    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("com.chesire.lintrules:lint-gradle:1.2.6")
    implementation("com.chesire.lintrules:lint-xml:1.2.6")
}
