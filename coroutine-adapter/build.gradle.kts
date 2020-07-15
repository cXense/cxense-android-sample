plugins {
    id(Plugins.androidLibrary)
}

android {
    buildToolsVersion = Config.androidBuildTools
    compileSdkVersion(Config.androidCompileSdk)

    defaultConfig {
        minSdkVersion(Config.androidMinSdk)
        targetSdkVersion(Config.androidTargetSdk)
    }

    compileOptions {
        sourceCompatibility = Config.compileSourceVersion
        targetCompatibility = Config.compileTargetVersion
    }
}

dependencies {
    implementation(kotlin(Libs.kotlinStdlib, Versions.kotlin))
    implementation(Libs.cxenseSdk)
    implementation(Libs.kotlinCoroutines)
}
