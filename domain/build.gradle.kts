plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api(Libraries.ROOM_COMMON)
    api(Libraries.MOSHI)
    api(Libraries.COROUTINES)
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    api(Libraries.TIMBER)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}