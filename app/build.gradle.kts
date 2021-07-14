plugins {
    id(Plugins.ANDROID_APPLICATION)
    kotlin(Plugins.KOTLIN_ANDROID)
    kotlin(Plugins.KOTLIN_KAPT)
    id(Plugins.NAVIGATION_SAFE_ARGS)
}

android {
    buildToolsVersion = App.BUILD_TOOLS
    compileSdk = App.COMPILE_SDK
    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        versionCode = App.VERSION_CODE
        versionName = App.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(Modules.DI))
    implementation(project(Modules.DOMAIN))
    api(Libraries.NAVIGATION)
    api(Libraries.NAVIGATION_FRAGMENT)
    api(Libraries.CONSTRAINT_LAYOUT)
    api(Libraries.APP_COMPAT)
    api(Libraries.COORDINATOR_LAYOUT)
    api(Libraries.COROUTINES)
    api(Libraries.COROUTINES_ANDROID)
    api(Libraries.VIEW_MODEL)
    api(Libraries.VIEW_MODEL_STATE)
    api(Libraries.RECYCLER_VIEW)
    api(Libraries.SUPPORT)
    api(Libraries.MATERIAL_DESIGN)
    api(Libraries.KOIN)
    api(Libraries.GLIDE)
    api(Libraries.WORK_MANAGER)
    implementation("androidx.preference:preference:1.1.1")
    kapt(Libraries.GLIDE_COMPILER)
}
