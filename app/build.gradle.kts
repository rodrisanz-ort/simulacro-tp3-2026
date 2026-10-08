import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
}

val environmentProperties = Properties().apply {
    val environmentFile = rootProject.file(".env")
    if (environmentFile.exists()) {
        environmentFile.forEachLine { line ->
            val entry = line.trim()
            if (entry.isNotEmpty() && !entry.startsWith("#")) {
                val separator = entry.indexOf('=')
                if (separator > 0) {
                    val key = entry.substring(0, separator).trim()
                    val value = entry.substring(separator + 1).trim().removeSurrounding("\"")
                    setProperty(key, value)
                }
            }
        }
    }
}

android {
    namespace = "com.example.tp3_grupo_1"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.tp3_grupo_1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "API_NINJAS_KEY",
            "\"${environmentProperties.getProperty(
                "API_NINJAS_KEY",
                localProperties.getProperty("API_NINJAS_KEY", "")
            )}\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    val room_version = "2.8.5"
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")
}