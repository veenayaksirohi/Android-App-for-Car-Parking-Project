import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.dagger.hilt.android")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    // id("io.gitlab.arturbosch.detekt") version "1.23.7" // Temporarily disabled
    id("kotlin-kapt")
}

android {
    namespace = "com.example.carparkingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.carparkingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // The secrets-gradle-plugin automatically makes API keys available as
        // manifestPlaceholders and BuildConfig fields. No manual configuration needed here.
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "DEBUG", "true")
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:5000/\"")
        }
        release {
            buildConfigField("boolean", "DEBUG", "false")
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:5000/\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        disable += "ExpiredTargetSdkVersion"
        abortOnError = false
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kapt {
        correctErrorTypes = true
        useBuildCache = true
        arguments {
            arg("dagger.fastInit", "enabled")
        }
        javacOptions {
            option("-Xmaxerrs", 500)
        }
    }
}

kotlin {
    jvmToolchain(17)
}

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
        force("org.jetbrains.kotlin:kotlin-stdlib-common:1.9.0")
        force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.0")
    }
}

dependencies {
    // Android core libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // Network libraries
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.1.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
}

// detekt {
//     buildUponDefaultConfig = true
//     config.setFrom(files("$projectDir/detekt.yml"))
//     baseline = file("$projectDir/baseline.xml")
//
//     reports {
//         html.required.set(true)
//         xml.required.set(true)
//         txt.required.set(false)
//         sarif.required.set(true)
//     }
// }

ktlint {
    android.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)

    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

// tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
//     jvmTarget = "1.8"
// }
//
// tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
//     jvmTarget = "1.8"
// }

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}
