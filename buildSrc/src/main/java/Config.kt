import org.gradle.api.JavaVersion

object Config {
    // Android config
    const val androidBuildTools = "28.0.3"
    const val androidMinSdk = 15
    const val androidTargetSdk = 28
    const val androidCompileSdk = 28
    const val applicationId = "com.example.cxensesdk"
    const val versionCode = 1
    const val versionName = "1.0"
    val compileSourceVersion = JavaVersion.VERSION_1_8
    val compileTargetVersion = JavaVersion.VERSION_1_8

    const val cxenseSiteId = "PUT_SITE_ID_HERE"
    const val cxenseUser = "PUT_USERNAME_HERE"
    const val cxenseApiKey = "PUT_API_KEY_HERE"
    const val cxensePersistedId = "PUT_PERSISTED_ID_HERE"
}

object Versions {
    const val kotlin = "1.3.31"
    //Plugins
    const val buildScanPlugin = "2.3"
    const val versionsPlugin = "0.21.0"
    const val androidToolsPlugin = "3.4.1"
    // Android libraries
    const val compatLibrary = "1.0.2"
    const val materialLibrary = "1.0.0"

    const val cxenseSdk = "1.6.0"

}

object Plugins {
    const val kotlin = "gradle-plugin"
    const val buildScan = "com.gradle.build-scan"
    const val versions = "com.github.ben-manes.versions"
    const val androidTools = "com.android.tools.build:gradle:${Versions.androidToolsPlugin}"
    const val androidApp = "com.android.application"
    const val kotlinAndroidApp = "kotlin-android"
    const val kotlinAndroidExtApp = "kotlin-android-extensions"
}

object Libs {
    const val kotlinStdlib = "stdlib-jdk7"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.compatLibrary}"
    const val material = "com.google.android.material:material:${Versions.materialLibrary}"
    const val cxenseSdk = "com.cxpublic:cxense-android:${Versions.cxenseSdk}"
}