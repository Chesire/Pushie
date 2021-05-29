plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)

        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":library:datastore"))
    implementation(project(":library:resources"))

    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("com.chesire.lintrules:lint-gradle:1.2.6")
    implementation("com.chesire.lintrules:lint-xml:1.2.6")
}
