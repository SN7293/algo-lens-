plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.algolens"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.algolens"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.core:core-ktx:1.14.0")
    dependencies {
        implementation ("androidx.appcompat:appcompat:1.4.2")
        implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
        implementation ("androidx.recyclerview:recyclerview:1.2.1")


        implementation ("com.google.android.material:material:1.5.0")

        implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")



        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

        implementation ("androidx.room:room-runtime:2.4.0")
        annotationProcessor ("androidx.room:room-compiler:2.4.0")

        implementation ("com.google.code.gson:gson:2.8.8")

        testImplementation ("junit:junit:4.13.2")
        androidTestImplementation ("androidx.test.ext:junit:1.1.3")
        androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    }


}