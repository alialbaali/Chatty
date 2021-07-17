object Libraries {
    const val KOTLIN = "stdlib:${Versions.KOTLIN}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.Support.APP_COMPAT}"
    const val CORE = "androidx.core:core-ktx:${Versions.CORE}"
    const val SUPPORT = "androidx.legacy:legacy-support-v4:${Versions.Support.SUPPORT}"

    const val JUNIT = "junit:junit:${Versions.Test.JUNIT}"
    const val TEST_JUNIT = "androidx.test.ext:junit:${Versions.Test.TEST_JUNIT}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.Test.ESPRESSO}"

    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"

    const val MATERIAL_DESIGN = "com.google.android.material:material:${Versions.MATERIAL_DESIGN}"

    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION_FRAGMENT}"
    const val NAVIGATION = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"

    const val COORDINATOR_LAYOUT = "androidx.coordinatorlayout:coordinatorlayout:${Versions.COORDINATOR_LAYOUT}"

    const val ROOM = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_COMMON = "androidx.room:room-common:${Versions.ROOM}"
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"

    const val DATA_STORE = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE}"

    const val LIVE_DATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFE_CYCLE}"

    const val VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFE_CYCLE}"

    const val VIEW_MODEL_STATE = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.VIEW_MODEL_STATE}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"

    const val RETROFIT_MOSHI_CONVERTER = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT_MOSHI_CONVERTER}"

    const val MOSHI = "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}"

    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val COROUTINES_ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES_ANDROID}"

    const val OKHTTP_LOGGER = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"

    const val KOIN = "io.insert-koin:koin-android:${Versions.KOIN}"

    const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val ANDROID_GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val NAVIGATION_SAFE_ARGS_GRADLE = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION_SAFE_ARGS}"

    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:glide:${Versions.GLIDE}"

    const val WORK_MANAGER = "androidx.work:work-runtime-ktx:${Versions.WORK_MANAGER}"
}

private object Versions {
    const val RETROFIT_MOSHI_CONVERTER = "2.7.2"
    const val NAVIGATION_FRAGMENT = "2.3.5"
    const val COORDINATOR_LAYOUT = "1.1.0"
    const val COROUTINES_ANDROID = "1.5.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val VIEW_MODEL_STATE = "2.2.0"
    const val MATERIAL_DESIGN = "1.3.0"
    const val RECYCLER_VIEW = "1.2.0"
    const val NAVIGATION = "2.3.5"
    const val COROUTINES = "1.5.0"
    const val LIFE_CYCLE = "2.3.1"
    const val RETROFIT = "2.9.0"
    const val TIMBER = "4.7.1"
    const val KOTLIN = "1.5.20"
    const val GRADLE = "7.0.0-beta05"
    const val NAVIGATION_SAFE_ARGS = "2.3.5"
    const val OKHTTP = "4.4.0"
    const val MOSHI = "1.12.0"
    const val KOIN = "3.0.2"
    const val CORE = "1.5.0"
    const val ROOM = "2.3.0"
    const val GLIDE = "4.11.0"
    const val WORK_MANAGER = "2.5.0"
    const val DATA_STORE = "1.0.0-beta01"

    object Test {
        const val TEST_JUNIT = "1.1.1"
        const val ESPRESSO = "3.2.0"
        const val JUNIT = "4.12"
    }

    object Support {
        const val APP_COMPAT = "1.3.0"
        const val SUPPORT = "1.0.0"
    }
}

object Modules {
    const val DATA = ":data"
    const val DI = ":di"
    const val LOCAL = ":local"
    const val REMOTE = ":remote"
    const val DOMAIN = ":domain"
}

object App {
    const val APP_ID = "com.chatychaty"
    const val VERSION_NAME = "2.0.0"
    const val NAME = "ChatyChaty"
    const val VERSION_CODE = 6
    const val MIN_SDK = 23
    const val COMPILE_SDK = 30
    const val BUILD_TOOLS = "30.0.2"
    const val TARGET_SDK = COMPILE_SDK
}

object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val KOTLIN_ANDROID = "android"
    const val KOTLIN_KAPT = "kapt"
    const val NAVIGATION_SAFE_ARGS = "androidx.navigation.safeargs.kotlin"
}
