import com.alialbaali.buildsrc.Libraries

plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin(Libraries.KOTLIN))

    testImplementation(Libraries.JUNIT)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(kotlin(Libraries.KOTLIN))
    api(Libraries.ROOM)
    api(Libraries.MOSHI)
    api(Libraries.KOIN)
    api(Libraries.LIVE_DATA)
    api(Libraries.TIMBER)
}