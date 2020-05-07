buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(com.alialbaali.buildsrc.Libraries.GRADLE)
        classpath(com.alialbaali.buildsrc.Libraries.KOTLIN_GRADLE)
        classpath(com.alialbaali.buildsrc.Libraries.NAVIGATION_SAFE_ARGS)
        "classpath"("com.android.tools.build:gradle:4.0.0-beta05")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}