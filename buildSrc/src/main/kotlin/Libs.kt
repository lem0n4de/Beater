object Libs {

    object JakeWharton {
        private const val timber_version = "4.7.1"
        val timber = "com.jakewharton.timber:timber:$timber_version"
        private const val threetenabp_version = "1.2.0"
        val threetenabp = "com.jakewharton.threetenabp:threetenabp:$threetenabp_version"
    }
    object Kotlin {
        private const val version = "1.3.21"
        val stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object Material {
        private const val version = "1.1.0-alpha04"
        const val components = "com.google.android.material:material:$version"
    }

    object Koin {
        private const val version = "2.0.0-beta-1"

        const val viewmodel = "org.koin:koin-android-viewmodel:$version"
        const val testing = "org.koin:koin-test:$version"
    }

    object AndroidX {
        private const val room_version = "2.1.0-alpha06"
        private const val appcompat_version = "1.1.0-alpha02"
        private const val core_version = "1.1.0-alpha04"
        private const val constraint_version = "1.1.3"
        private const val lifecycle_version = "2.1.0-alpha02"
        private const val navigation_version = "2.1.0-alpha02"

        const val room_runtime = "androidx.room:room-runtime:$room_version"
        const val room_compiler = "androidx.room:room-compiler:$room_version"
        const val room_rxjava2 = "androidx.room:room-rxjava2:$room_version"
        const val room_ktx = "androidx.room:room-ktx:$room_version"
        const val room_testing = "androidx.room:room-testing:$room_version"

        const val lifecycle_viewmodel_livedata = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
        const val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
        const val lifecycle_reactive_streams = "androidx.lifecycle:lifecycle-reactivestreams:$lifecycle_version"
        const val lifecycle_testing = "androidx.arch.core:core-testing:$lifecycle_version"

        const val appcompat = "androidx.appcompat:appcompat:$appcompat_version"
        const val core_ktx = "androidx.core:core-ktx:$core_version"
        const val constraint_layout = "androidx.constraintlayout:constraintlayout:$constraint_version"

        const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
        const val navigation_ui = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    }

    object ReactiveX {
        private const val java_version = "2.2.7"
        private const val kotlin_version = "2.3.0"
        private const val android_version = "2.1.1"
        private const val relay_version = "2.1.0"

        const val rxjava2 = "io.reactivex.rxjava2:rxjava:$java_version"
        const val rxkotlin = "io.reactivex.rxjava2:rxkotlin:$kotlin_version"
        const val rxandroid = "io.reactivex.rxjava2:rxandroid:$android_version"
        const val rxrelay = "com.jakewharton.rxrelay2:rxrelay:$relay_version"
    }

    object AndroidXTesting {
        private const val version = "1.1.0"
        private const val rules_version = "1.1.1"
        private const val espresso_version = "3.1.0"
        private const val fragment_version = "1.1.0-alpha04"
        private const val uiautomator_version = "2.2.0"

        const val core = "androidx.test:core:$version"
        const val rules = "androidx.test:rules:$rules_version"
        const val runner = "androidx.test:runner:$rules_version"
        const val espresso_core = "androidx.test.espresso:espresso-core:$espresso_version"
        const val espresso_intents = "androidx.test.espresso:espresso-intents:$espresso_version"
        const val fragment_testing = "androidx.fragment:fragment-testing:$fragment_version"
        const val uiautomator = "androidx.test.uiautomator:uiautomator:$uiautomator_version"
    }

    object Anko {
        private const val version = "0.10.8"
        
        const val all = "org.jetbrains.anko:anko:$version"
        const val commons = "org.jetbrains.anko:anko-commons:$version"
        
        const val layouts_sdk25 = "org.jetbrains.anko:anko-sdk25:$version"
        const val layouts_sdk23 = "org.jetbrains.anko:anko-sdk23:$version"
        const val layouts_appcompat = "org.jetbrains.anko:anko-appcompat-v7:$version"

        const val coroutines = "org.jetbrains.anko:anko-sdk25-coroutines:$version"
        const val coroutines_appcompat = "org.jetbrains.anko:anko-appcompat-v7-coroutines:$version"

        const val constraint_layout = "org.jetbrains.anko:anko-constraint-layout:$version"

    }

    object Junit {
        private const val junit5_version = "5.3.2"
        private const val junit_platform = "1.3.2"
        private const val junit4_version = "4.12"

        val jupiter_api = "org.junit.jupiter:junit-jupiter-api:$junit5_version"
        val jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:$junit5_version"
        val jupiter_params = "org.junit.jupiter:junit-jupiter-params:$junit5_version"

        val junit4 = "junit:junit:$junit4_version"
        val jupiter_vintage = "org.junit.vintage:junit-vintage-engine:$junit5_version"
        val platform = "org.junit.platform:junit-platform-runner:$junit_platform"
    }

    object AndroidJUnit5 {
        private const val version = "0.2.2"

        val instrumentation = "de.mannodermaus.junit5:android-instrumentation-test:$version"
        val runner = "de.mannodermaus.junit5:android-instrumentation-test-runner:$version"
    }

    object Kakao {
        private const val version = "2.0.0"
        val kakao = "com.agoda.kakao:kakao:$version"
    }

    object Spek {
        private const val version = "2.0.0"
        val spek = "org.spekframework.spek2:spek-dsl-jvm:$version"
        val runner = "org.spekframework.spek2:spek-runner-junit5:$version"
    }

    object Kluent {
        private const val version = "1.48"
        val kluent = "org.amshove.kluent:kluent-android:$version"
    }

    object Mockk {
        private const val version = "1.9"
        val mockk = "io.mockk:mockk:$version"
        val mockk_instrumentation = "io.mockk:mockk-android:$version"
    }
}



























