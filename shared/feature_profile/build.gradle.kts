plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.cashsqldelight)
    kotlin("plugin.serialization")
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
            baseName = "tic_tac_toe"
            isStatic = true
            binaryOptions["bundleId"] = "com.shared.profile"
        }
    }
    task("testClasses")
    sourceSets {
        androidMain.dependencies {
            implementation(libs.cashsqldeligh.android)
        }
        commonMain.dependencies {
            implementation(projects.shared.foundationCompose)
            implementation(projects.shared.featureAuth)
            implementation(libs.file.picker)
            implementation(libs.cashsqldeligh.coroutine)
            implementation(libs.coil.v300alpha01)
        }
        iosMain.dependencies {
            implementation(libs.cashsqldeligh.native)
        }
    }
}

android {
    namespace = "com.shared.profile"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}
sqldelight {
    databases {
        create("Database") {
            packageName.set("com.shared.feature_profile.sql")
        }
    }
}
compose.resources {
    packageOfResClass = "com.shared.cmm.feature_profile.res"
}