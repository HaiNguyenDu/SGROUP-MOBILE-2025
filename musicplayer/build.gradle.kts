plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.sgroupmobile2025.musicplayer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.sgroupmobile2025.musicplayer" // Thêm dòng này
        minSdk = 24
        targetSdk = 36 // Nên thêm targetSdk để đồng bộ với compileSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Xóa dòng consumerProguardFiles nếu có vì app không dùng nó
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}
kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}


dependencies {
    implementation(libs.androidx.media)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.fragment.ktx.v171)
    implementation(libs.glide)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v280)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)
    implementation(libs.material.v1110)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}