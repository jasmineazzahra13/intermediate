plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.dicoding.intermediate"
    compileSdk = 34

    testOptions {
        animationsDisabled = true
    }

    defaultConfig {
        applicationId = "com.dicoding.intermediate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DB_NAME","\"story_database\"")
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.swiperefreshlayout)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-paging:2.6.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("io.insert-koin:koin-core:3.5.6")
    implementation("io.insert-koin:koin-android:3.5.6")

    implementation("androidx.paging:paging-runtime:3.3.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.navigation:navigation-fragment:2.7.7")

    implementation("com.airbnb.android:lottie:6.4.1")

    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation("com.karumi:dexter:6.2.3")

    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")

    testImplementation("junit:junit:4.13.2")
}