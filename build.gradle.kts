buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(com.chatychaty.buildSrc.Libraries.GRADLE)
        classpath(com.chatychaty.buildSrc.Libraries.KOTLIN_GRADLE)
        classpath(com.chatychaty.buildSrc.Libraries.NAVIGATION_SAFE_ARGS)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xallow-result-return-type")
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}