import Libs.Anko.coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("de.mannodermaus.android-junit5")
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
        testInstrumentationRunnerArgument("runnerBuilder", "de.mannodermaus.junit5.AndroidJUnit5Builder")
    }
    flavorDimensions("full")
    productFlavors {
        create("experimental") {
            minSdkVersion(26)
            dimension = "full"
        }
        create("normal") {
            dimension = "full"
        }
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
    implementation("androidx.appcompat:appcompat:1.0.0-beta01")
    implementation("androidx.core:core-ktx:1.1.0-alpha04")
    implementation("androidx.constraintlayout:constraintlayout:1.1.2")
    implementation(Libs.Material.components)


    // JUnit 5 unit test
    testImplementation(Libs.Junit.jupiter_api)
    testImplementation(Libs.Junit.junit4)
    testRuntimeOnly(Libs.Junit.jupiter_engine)
    testRuntimeOnly(Libs.Junit.jupiter_vintage)

    val androidTestExperimentalImplementation by configurations
    val androidTestExperimentalRuntimeOnly by configurations
    
    // JUnit 5 integration tests
    androidTestExperimentalImplementation(Libs.Junit.jupiter_api)
    androidTestExperimentalImplementation(Libs.AndroidJUnit5.instrumentation)
    androidTestExperimentalRuntimeOnly(Libs.Junit.jupiter_engine)
    androidTestExperimentalRuntimeOnly(Libs.Junit.platform)
    androidTestExperimentalRuntimeOnly(Libs.AndroidJUnit5.runner)

    // Kakao for easier Espresso usage
    androidTestExperimentalImplementation(Libs.Kakao.kakao)

    // Spek
    testImplementation(Libs.Spek.spek)
    testImplementation(Libs.Spek.runner)
    testImplementation(Libs.Kotlin.reflect)

    // Kluent for fluent assertion
    testImplementation(Libs.Kluent.kluent)
    androidTestImplementation(Libs.Kluent.kluent)

    // Mockk for mocking
    testImplementation(Libs.Mockk.mockk)
    androidTestImplementation(Libs.Mockk.mockk)

    implementation(Libs.Anko.commons)

    androidTestImplementation(Libs.AndroidX.runner)
    androidTestImplementation(Libs.AndroidX.espresso_core)
    androidTestImplementation(Libs.AndroidX.espresso_intents)
    val debugImplementation by configurations
    debugImplementation(Libs.AndroidX.fragment_testing)
}
