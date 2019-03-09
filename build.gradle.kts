buildscript {
    val kotlin_version = "1.3.21"
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.1")
        classpath(kotlin("gradle-plugin",  version = kotlin_version))
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.4.0.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
