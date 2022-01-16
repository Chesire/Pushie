plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31
        applicationId = "com.chesire.pushie"
        versionCode = 10
        versionName = "2.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resourceConfigurations.add("en")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":feature:pusher"))
    implementation(project(":feature:settings"))
    implementation(project(":library:common"))
    implementation(project(":library:datasource:pwpush"))
    implementation(project(":library:datastore"))
    implementation(project(":library:resources"))

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("com.chesire.lintrules:lint-gradle:1.2.6")
    implementation("com.chesire.lintrules:lint-xml:1.2.6")
    implementation("com.google.android.material:material:1.5.0")
}
