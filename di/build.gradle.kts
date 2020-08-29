import com.chatychaty.buildSrc.*
plugins {
    val plugins = com.chatychaty.buildSrc.Plugins
    id(plugins.ANDROID_LIBRARY)
    kotlin(plugins.KOTLIN_ANDROID)
    id(plugins.KOTLIN_ANDROID_EXTENSIONS)
}

android {
    compileSdkVersion(App.COMPILE_SDK)
    buildToolsVersion(App.BUILD_TOOLS)
    defaultConfig {
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
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
}
