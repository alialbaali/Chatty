buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(Libraries.ANDROID_GRADLE)
        classpath(Libraries.KOTLIN_GRADLE)
        classpath(Libraries.NAVIGATION_SAFE_ARGS_GRADLE)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("delete", Delete::class.java) {
    delete(rootProject.buildDir)
}
