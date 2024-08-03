plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "foundation"
            isStatic = true
            binaryOptions["bundleId"] = "com.shared.compose_foundation"
        }
    }
    task("testClasses")
    sourceSets {

        commonMain.dependencies {
            compose.apply {
                api(runtime)
                api(foundation)
                api(material3)
                api(ui)
                api(components.resources)
            }
            api(libs.coil.compose)
//            api(libs.coil.network.ktor)
//            api(libs.coil.kt.coil.compose)
            api(projects.shared.foundation)
            api(libs.file.picker)
//            api(libs.androidx.foundation.v168)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.shared.compose_foundation"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

