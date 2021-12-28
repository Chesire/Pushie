import com.android.build.gradle.internal.dsl.LintOptions
import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.KtlintPlugin

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath(kotlin("gradle-plugin", "1.5.0"))
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

allprojects {
    repositories {
        google()
        jcenter()
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
