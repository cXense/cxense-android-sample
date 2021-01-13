import org.gradle.kotlin.dsl.implementation

plugins {
    id(Plugins.androidApp)
    id(Plugins.kotlinAndroidApp)
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

        buildConfigField("String", "SITEGROUP_ID", """"${Config.cxenseSiteGroupId}"""")
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
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.viewBindingProperty)
    implementation(Libs.cxenseSdk)
}
