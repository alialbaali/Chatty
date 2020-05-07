import com.alialbaali.buildsrc.App
import com.alialbaali.buildsrc.Libraries
import com.alialbaali.buildsrc.Modules

plugins {
    val plugins = com.alialbaali.buildsrc.Plugins
    id(plugins.ANDROID_APPLICATION)
    kotlin(plugins.KOTLIN_ANDROID)
    id(plugins.KOTLIN_ANDROID_EXTENSIONS)
    kotlin(plugins.KOTLIN_KAPT)
    id(plugins.NAVIGATION_SAFE_ARGS)
}

android {
    compileSdkVersion(App.COMPILE_SDK)
    buildToolsVersion(App.BUILD_TOOLS)
    defaultConfig {
        applicationId = App.APP_ID
        minSdkVersion(App.MIN_SDK)
        targetSdkVersion(App.TARGET_SDK)
        versionCode = App.APP_VERSION_CODE
        versionName = App.APP_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(Modules.REPOSITORY))
    implementation(project(Modules.DI))
    implementation(project(Modules.MODEL))
    implementation(project(Modules.RESOURCES))
}
