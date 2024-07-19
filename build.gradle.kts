import com.android.build.gradle.internal.dsl.LintOptions
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${libs.versions.hilt.get()}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.25")
    }
}

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply<KtlintPlugin>()

    afterEvaluate {
        if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
            if (extensions.findByName("LintOptions") == true) {
                extensions.getByType<LintOptions>().apply {
                    isAbortOnError = false
                    isCheckAllWarnings = true
                }
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("detektCheck", Detekt::class) {
    parallel = true
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("$rootDir/detekt.yml"))
    buildUponDefaultConfig = false
}
