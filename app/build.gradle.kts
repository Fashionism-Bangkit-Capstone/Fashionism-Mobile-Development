plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.fashionism.fashionismuserapp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.fashionism.fashionismuserapp"
        minSdk = 24
        targetSdk = 37
        versionCode = providers.gradleProperty("VERSION_CODE").get().toInt()
        versionName = providers.gradleProperty("VERSION_NAME").get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            // TODO: replace with staging server URLs, currently pointing at production
            buildConfigField("String", "API_URL_V1", "\"http://34.101.95.192/api/v1/\"")
            buildConfigField("String", "API_URL_V2", "\"http://34.101.42.179/api/v2/\"")
            buildConfigField("String", "API_URL_ML", "\"http://34.101.232.165:8000/\"")
        }
        create("production") {
            dimension = "environment"
            buildConfigField("String", "API_URL_V1", "\"http://34.101.95.192/api/v1/\"")
            buildConfigField("String", "API_URL_V2", "\"http://34.101.42.179/api/v2/\"")
            buildConfigField("String", "API_URL_ML", "\"http://34.101.232.165:8000/\"")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    bundle {
        language {
            enableSplit = false
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.exifinterface)

    implementation(libs.koin.android)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.circleimageview)
    implementation(libs.smoothbottombar)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
