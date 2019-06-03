import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id(Plugins.versions) version Versions.versionsPlugin
    id(Plugins.buildScan) version Versions.buildScanPlugin
}

buildscript {
    repositories {
        google()
        jcenter()

    }

    dependencies {
        classpath(Plugins.androidTools)
        classpath(kotlin(Plugins.kotlin, Versions.kotlin))
    }
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    setTermsOfServiceAgree("yes")
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
}

tasks {
    register("clean", Delete::class) {
        delete(buildDir)
    }
    named<DependencyUpdatesTask>("dependencyUpdates") {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                        .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                        .any { it.matches(candidate.version) }
                    if (rejected) {
                        reject("Release candidate")
                    }
                    if (candidate.group == "com.squareup.okhttp3") {
                        val version = candidate.version.split(".")
                        if (version.component1().toInt() > 3 || version.component2().toInt() > 12)
                            reject("We use okhttp 3.12.*, because okhttp 3.13+ requires API 21")
                    }
                }
            }
        }
        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/dependencyUpdates"
        reportfileName = "report"
    }
}
