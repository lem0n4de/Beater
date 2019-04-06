import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

tasks.withType(KotlinCompile::class).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    sourceSets?.forEach {
        it.java.srcDir("src/${it.name}/kotlin")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.Kotlin.stdlib_jdk8)
    implementation(Libs.ReactiveX.rxandroid)
    implementation(Libs.ReactiveX.rxkotlin)
    implementation(Libs.ReactiveX.rxjava2)
    // Koin
    api(Libs.Koin.viewmodel)
    // Logging with Timber
    api(Libs.Timber.timber)
    // ReactiveX
    api(Libs.ReactiveX.rxandroid)
    api(Libs.ReactiveX.rxkotlin)
    api(Libs.ReactiveX.rxjava2)

    api(Libs.AndroidX.room_runtime)
    kapt(Libs.AndroidX.room_compiler)
    api(Libs.AndroidX.room_rxjava2)
    api(Libs.AndroidX.room_coroutines)
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}
