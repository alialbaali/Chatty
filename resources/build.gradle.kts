import com.alialbaali.buildsrc.App
import com.alialbaali.buildsrc.Modules
import com.alialbaali.buildsrc.Libraries

plugins {
    val plugins = com.alialbaali.buildsrc.Plugins
    id(plugins.ANDROID_LIBRARY)
    kotlin(plugins.KOTLIN_ANDROID)
    id(plugins.KOTLIN_ANDROID_EXTENSIONS)
    kotlin(plugins.KOTLIN_KAPT)
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
    api(Libraries.NAVIGATION)
    api(Libraries.NAVIGATION_FRAGMENT)
    api(Libraries.CONSTRAINT_LAYOUT)
    api(Libraries.APP_COMPAT)
    api(Libraries.COORDINATOR_LAYOUT)
    api(Libraries.COROUTINES)
    api(Libraries.COROUTINES_ANDROID)
    api(Libraries.LIFE_CYCLE)
    api(Libraries.VIEW_MODEL)
    api(Libraries.VIEW_MODEL_STATE)
    api(Libraries.RECYCLER_VIEW)
    api(Libraries.SUPPORT)
    api(Libraries.MATERIAL_DESIGN)
    api(Libraries.PROGRESS_BUTTON)
    api(Libraries.GLIDE)
    api(Libraries.WORK_MANAGER)
    kapt(Libraries.GLIDE_COMPILER)
    kapt(Libraries.DATA_BINDING_COMPILER)
}
