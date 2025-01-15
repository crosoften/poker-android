plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.safeargsAndroid)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.draccoapp.poker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.draccoapp.poker"
        minSdk = 26
        targetSdk = 34
        versionCode = 14
        versionName = "1.0.0.14"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
    dataBinding {
        enable = true
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.location)
    implementation(libs.socket.io.client)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.coil)
    implementation(libs.navigationFragment)
    implementation(libs.navigationUi)
    implementation(libs.lifecycleViewmodel)
    implementation(libs.lifecycleLivedata)
    implementation(libs.lifecycleRuntime)
    implementation(libs.kotlinxCoroutines)
    implementation(libs.glide)
    implementation(libs.koinAndroid)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.rxjava2Adapter)
    implementation(libs.moshiKotlin)
    implementation(libs.retrofitConverterMoshi)
    ksp(libs.moshiKotlinKsp)
    implementation(libs.pagingRuntimeKtx)
    implementation(libs.pagingCommonKtx)
    implementation(libs.feature.delivery)
    // For Kotlin users, also import the Kotlin extensions library for Play Feature Delivery:
    implementation(libs.feature.delivery.ktx)
    // Paging3
    implementation (libs.androidx.paging.runtime.ktx.v331)
    implementation (libs.androidx.paging.common.ktx.v331)
    implementation (libs.logging.interceptor)
    implementation (libs.play.services.maps)// Ou versão mais recente
}