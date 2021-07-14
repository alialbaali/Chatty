plugins {
    id(Plugins.ANDROID_LIBRARY)
    kotlin(Plugins.KOTLIN_ANDROID)
}

android {
    buildToolsVersion = App.BUILD_TOOLS
    compileSdk = App.COMPILE_SDK
    defaultConfig {
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))
    implementation(project(Modules.DATA))
    implementation(project(Modules.LOCAL))
    implementation(project(Modules.REMOTE))
    implementation(Libraries.ROOM)
    implementation(Libraries.KOIN)
}
