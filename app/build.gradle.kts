import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Carregar propriedades
val properties = Properties()

val localProps = project.rootProject.file("local.properties")
if (localProps.exists()) {
    localProps.inputStream().use {
        properties.load(it)
    }
}

val envFile = project.rootProject.file(".env")
if (envFile.exists()) {
    envFile.inputStream().use {
        properties.load(it)
    }
}

// Obter chave da API do Google Maps
val mapsApiKey = properties.getProperty(
    "MAPS_API_KEY",
    "CHAVE_NAO_CONFIGURADA"
)

android {
    namespace = "br.com.curso.pokemap"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.curso.pokemap"

        minSdk = 24
        targetSdk = 34

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"

        // Chave da API no AndroidManifest
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        // Chave disponível no BuildConfig
        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"$mapsApiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Google Maps
    implementation("com.google.maps.android:maps-compose:4.3.3")

    implementation(
        "com.google.android.gms:play-services-maps:18.2.0"
    )

    implementation(
        "com.google.android.gms:play-services-location:21.1.0"
    )

    // Testes
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )

    androidTestImplementation(
        libs.androidx.ui.test.junit4
    )

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}