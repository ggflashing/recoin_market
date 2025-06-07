plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)



    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.recoin_market"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recoin_market"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    testImplementation(libs.junit)
    implementation(libs.androidx.recyclerview)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.lottie)

    implementation(libs.androidx.activity)

    //Navigation components краткий переход между страницами
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    //Lifecycle отслеживание жиненого цикла// фрагмента или активити чтобы разрешить получить данные из LiveData
//Extensions
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Navigation components краткий переход между страницами
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity.ktx)

    //ViewModel применение паттерна MVVM
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Glide загрузка изображения с бэкенда
     implementation(libs.glide)

    // Retrofit Ojkhttp Gson  поучающий данные с бэкэнда
// и его правила поведения там на бэкенде
    implementation (libs.retrofit)
    implementation (libs.okhttp)
    implementation (libs.converter.gson)
    implementation(libs.gson)

    // Interceptor  - возможность получить очередь, сделать запрос на сервер
    implementation(libs.logging.interceptor)

    //Delegates viewBinding позволяет создавать расширенную работу с binding
    implementation (libs.viewbindingpropertydelegate)
    implementation (libs.viewbindingpropertydelegate.noreflection)
    implementation(libs.androidx.databinding.runtime)

    // Применение инструментов платформы  Firebase and  и совместимость библиотек Firebase BoM
    implementation (libs.firebase.core)
    implementation (libs.firebase.auth.v2231)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)

    // круглое изображение
    implementation (libs.circleimageview)

    //PDF viewer
    implementation ("com.github.barteksc:AndroidPdfViewerV1:1.6.0")

    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))

    implementation("com.google.firebase:firebase-analytics")



}