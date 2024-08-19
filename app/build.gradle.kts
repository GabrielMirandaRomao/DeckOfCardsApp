plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.cardsapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cardsapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // okhttp
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))

    // glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.")

    // koin
    implementation ("io.insert-koin:koin-android:3.1.2")
    implementation ("io.insert-koin:koin-android-compat:3.1.2")

    // RecyclerView Animator
    implementation ("jp.wasabeef:recyclerview-animators:4.0.2")

    // Jetpack Compose
    implementation ("androidx.compose.ui:ui:1.6.8")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.8")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.6.8")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    // coil
    implementation("io.coil-kt:coil-compose:2.7.0")

}