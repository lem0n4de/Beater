plugins {
    `kotlin-dsl`
}

sourceSets?.forEach {
    it.java.srcDir("src/${it.name}/kotlin")
}

repositories {
    jcenter()
}
