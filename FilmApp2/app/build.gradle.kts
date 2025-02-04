plugins {
    id("com.android.application")
}

android {
    namespace = "xyz.danicostas.filmapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "xyz.danicostas.filmapp"
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
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")

    // Firestore
    implementation("com.google.firebase:firebase-firestore")


    // Dependencias estándar
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Lombok
    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")

}
apply(plugin = "com.google.gms.google-services")


