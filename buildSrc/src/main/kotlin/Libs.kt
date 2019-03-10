object Libs {
    object Kotlin {
        private const val version = "1.3.21"
        val stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

    object Material {
        private const val version = "1.1.0-alpha04"

        const val components = "com.google.android.material:material:$version"
    }

    object AndroidX {
        private const val version = "1.1.0"
        private const val rules_version = "1.1.1"
        private const val espresso_version = "3.1.0"
        private const val fragment_version = "1.1.0-alpha04"

        const val core = "androidx.test:core:$version"
        const val rules = "androidx.test:rules:$rules_version"
        const val runner = "androidx.test:runner:$rules_version"
        const val espresso_core = "androidx.test.espresso:espresso-core:$espresso_version"
        const val espresso_intents = "androidx.test.espresso:espresso-intents:$espresso_version"
        const val fragment_testing = "androidx.fragment:fragment-testing:$fragment_version"
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
    }
}



























