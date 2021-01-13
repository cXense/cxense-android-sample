import org.gradle.api.JavaVersion

object Config {
    // Android config
    const val androidBuildTools = "30.0.3"
    const val androidMinSdk = 15
    const val androidTargetSdk = 30
    const val androidCompileSdk = 30
    const val applicationId = "com.example.cxensesdk"
    const val versionCode = 1
    const val versionName = "1.0"
    val compileSourceVersion = JavaVersion.VERSION_1_8
    val compileTargetVersion = JavaVersion.VERSION_1_8

    const val cxenseSiteGroupId = "PUT_SITEGROUP_ID_HERE"
    const val cxenseSiteId = "PUT_SITE_ID_HERE"
    const val cxenseUser = "PUT_USERNAME_HERE"
    const val cxenseApiKey = "PUT_API_KEY_HERE"
    const val cxensePersistedId = "PUT_PERSISTED_ID_HERE"
}

object Versions {
    const val kotlin = "1.4.21-2"
    //Plugins
    const val versionsPlugin = "0.36.0"
    const val androidToolsPlugin = "4.1.1"
    // Android libraries
    const val compatLibrary = "1.2.0"
    const val materialLibrary = "1.2.1"

    const val cxenseSdk = "2.0.1"
    const val rxJava = "2.2.20"
    const val kotlinCoroutines = "1.4.2"
    const val viewBindingProperty = "1.4.1"
}

object Plugins {
    const val kotlin = "gradle-plugin"
    const val versions = "com.github.ben-manes.versions"
    const val androidTools = "com.android.tools.build:gradle:${Versions.androidToolsPlugin}"
    const val androidLibrary = "com.android.library"
    const val androidApp = "com.android.application"
    const val kotlinAndroidApp = "kotlin-android"
}

object Libs {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.compatLibrary}"
    const val material = "com.google.android.material:material:${Versions.materialLibrary}"
    const val cxenseSdk = "com.cxpublic:cxense-android:${Versions.cxenseSdk}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    const val viewBindingProperty = "com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:${Versions.viewBindingProperty}"
}