import com.chatychaty.buildSrc.*
plugins {
    val plugins = com.chatychaty.buildSrc.Plugins
    id(plugins.ANDROID_APPLICATION)
    kotlin(plugins.KOTLIN_ANDROID)
    id(plugins.KOTLIN_ANDROID_EXTENSIONS)
    kotlin(plugins.KOTLIN_KAPT)
    id(plugins.NAVIGATION_SAFE_ARGS)
    id("kotlin-android")
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
    packagingOptions {
        exclude("META-INF/kotlinx-coroutines-core.kotlin_module")
        exclude("META-INF/rxkotlin.kotlin_module")

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
    api(Libraries.LIFE_CYCLE)
    api(Libraries.VIEW_MODEL)
    api(Libraries.VIEW_MODEL_STATE)
    api(Libraries.RECYCLER_VIEW)
    api(Libraries.SUPPORT)
    api(Libraries.MATERIAL_DESIGN)
    api(Libraries.GLIDE)
    api(Libraries.WORK_MANAGER)
    implementation("androidx.preference:preference:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    kapt(Libraries.GLIDE_COMPILER)
    kapt(Libraries.DATA_BINDING_COMPILER)
}
