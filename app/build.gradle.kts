import java.util.Date
import java.text.SimpleDateFormat

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.autoskipads"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.autoskipads"
        minSdk = 31
        targetSdk = 33
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        val mKeyPassWord = "android_box"
        register("debug2") {
            keyAlias = mKeyPassWord
            keyPassword = mKeyPassWord
            storeFile = file("../platform.keystore")
            storePassword = mKeyPassWord
        }
        register("release") {
            keyAlias = mKeyPassWord
            keyPassword = mKeyPassWord
            storeFile = file("../platform.keystore")
            storePassword = mKeyPassWord
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug2")
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    val appName = "AutoSkipAds"
    val buildTime = SimpleDateFormat("yyyyMMdd_HHmm").format(Date())
    applicationVariants.configureEach {
        val buildType = this.buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName =
                    "${appName}_V${defaultConfig.versionName}_${buildType}_${buildTime}.apk"
            }
        }
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}