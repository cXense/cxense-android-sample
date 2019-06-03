import org.gradle.kotlin.dsl.implementation
import org.jetbrains.kotlin.gradle.internal.CacheImplementation

plugins {
    id(Plugins.androidApp)
    id(Plugins.kotlinAndroidApp)
    id(Plugins.kotlinAndroidExtApp)
}

androidExtensions {
    isExperimental = true
    defaultCacheImplementation = CacheImplementation.SPARSE_ARRAY
}

android {
    compileSdkVersion(Config.androidCompileSdk)
    buildToolsVersion(Config.androidBuildTools)

    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.androidMinSdk)
        targetSdkVersion(Config.androidTargetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName

        buildConfigField("String", "SITE_ID", """"${Config.cxenseSiteId}"""")
        buildConfigField("String", "USERNAME", """"${Config.cxenseUser}"""")
        buildConfigField("String", "API_KEY", """"${Config.cxenseApiKey}"""")
        buildConfigField("String", "PERSISTED_ID", """"${Config.cxensePersistedId}"""")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = Config.compileSourceVersion
        targetCompatibility = Config.compileTargetVersion
    }


}

dependencies {
    implementation(kotlin(Libs.kotlinStdlib, Versions.kotlin))
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.cxenseSdk)
}
