import java.util.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

fun String.execute(currentWorkingDir: File): String {
    return providers.exec {
        isIgnoreExitValue = true
        workingDir = currentWorkingDir
        commandLine = split("\\s".toRegex())
    }.standardOutput.asText.get().trim()
}

fun String.executeOrNull(currentWorkingDir: File): String? {
    return runCatching { execute(currentWorkingDir) }.getOrNull()?.takeIf { it.isNotBlank() }
}

android {
    namespace = "org.lyaaz.fuckgram"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.lyaaz.fuckgram"
        minSdk = 26
        targetSdk = 36
        versionCode = "git rev-list HEAD --count"
            .executeOrNull(rootProject.projectDir)?.toIntOrNull() ?: 1
        versionName = "git describe --tag --always"
            .executeOrNull(rootProject.projectDir)?.removePrefix("v") ?: "0.0.0"
    }

    signingConfigs {
        val signingFile = listOf(
            rootProject.file("signing.properties"),
            file("signing.properties")
        ).firstOrNull { it.exists() }
        if (signingFile != null) {
            create("release") {
                val properties = Properties().apply {
                    load(signingFile.reader())
                }
                storeFile = File(properties.getProperty("storeFilePath"))
                storePassword = properties.getProperty("storePassword")
                keyPassword = properties.getProperty("keyPassword")
                keyAlias = properties.getProperty("keyAlias")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.findByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":ui"))

    compileOnly(libs.libxposed.api)
    implementation(libs.libxposed.service)

    implementation(libs.android.material)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
