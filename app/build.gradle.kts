import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("de.mannodermaus.android-junit5")
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
        applicationId = "com.lem0n.beater"
        minSdkVersion(24)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    sourceSets?.forEach {
        it.java.srcDir("src/${it.name}/kotlin")
    }
    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
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
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.core_ktx)
    implementation(Libs.AndroidX.constraint_layout)
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation(project(":base"))

    // Koin Dependency Injection
    implementation(Libs.Koin.viewmodel)
    testImplementation(Libs.Koin.testing)
    androidTestImplementation(Libs.Koin.testing)

    // Lifecycle
    implementation(Libs.AndroidX.lifecycle_reactive_streams)

    // ReactiveX
    implementation(Libs.ReactiveX.rxandroid)
    implementation(Libs.ReactiveX.rxkotlin)
    implementation(Libs.ReactiveX.rxjava2)

    // Room
    implementation(Libs.AndroidX.room_runtime)
    kapt(Libs.AndroidX.room_compiler)
    implementation(Libs.AndroidX.room_rxjava2)
    implementation(Libs.AndroidX.room_coroutines)
    testImplementation(Libs.AndroidX.room_testing)
    androidTestImplementation(Libs.AndroidX.room_testing)

    // Material Design
    implementation(Libs.Material.components)

    // JUnit 5 unit test
    testImplementation(Libs.Junit.jupiter_api)
    testImplementation(Libs.Junit.junit4)
    testRuntimeOnly(Libs.Junit.jupiter_engine)
    testRuntimeOnly(Libs.Junit.jupiter_vintage)

    androidTestImplementation(Libs.Junit.junit4)

    // Kakao for easier Espresso usage
    androidTestImplementation(Libs.Kakao.kakao)

    // Spek
    testImplementation(Libs.Spek.spek)
    testImplementation(Libs.Spek.runner)
    testImplementation(Libs.Kotlin.reflect)

    // Kluent for fluent assertion
    testImplementation(Libs.Kluent.kluent)
    androidTestImplementation(Libs.Kluent.kluent)

    // Mockk for mocking
    testImplementation(Libs.Mockk.mockk)
    androidTestImplementation(Libs.Mockk.mockk_instrumentation) {
        exclude(group = "net.bytebuddy")
    }

    implementation(Libs.Anko.commons)

    androidTestImplementation(Libs.AndroidXTesting.runner)
    androidTestImplementation(Libs.AndroidXTesting.espresso_core)
    androidTestImplementation(Libs.AndroidXTesting.espresso_intents)
    androidTestImplementation(Libs.AndroidXTesting.uiautomator)
    val debugImplementation by configurations
    debugImplementation(Libs.AndroidXTesting.fragment_testing)
}
