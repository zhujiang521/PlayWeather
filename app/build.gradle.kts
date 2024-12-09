plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.zj.weather"

    val sdkVersion = rootProject.extra["sdkVersion"] as Int?
    val minSdkVersion = rootProject.extra["minSdkVersion"] as Int?
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        versionCode = rootProject.extra["versionCode"] as Int?
        versionName = rootProject.extra["versionName"] as String?

        testInstrumentationRunner = rootProject.extra["testInstrumentationRunner"] as String?

        vectorDrawables {
            useSupportLibrary = true
        }
        resourceConfigurations += listOf("en", "zh", "zh-rCN", "zh-rHK", "zh-rTW")
    }

    buildTypes {
        release {
            // 开启混淆
            isMinifyEnabled = true
            // 去除无用资源 与lint不同
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    val javaVersion = rootProject.extra["javaVersion"] as JavaVersion
    val javaVersionName = javaVersion.toString()

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersionName))
        }
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }


    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
        // 或者，如果您想保留一个，可以使用 pickFirst 或 merge
        // pickFirst 'META-INF/LICENSE.md'
        // merge 'META-INF/LICENSE.md'
    }

}

dependencies {

    implementation("${rootProject.extra["coreKtx"] as String?}")
    implementation("${rootProject.extra["appcompat"] as String?}")
    implementation(project(":animate"))
    implementation("androidx.test:monitor:1.7.1")
    androidTestImplementation("org.junit.jupiter:junit-jupiter:5.11.0")

    val composeVersion = rootProject.extra["composeVersion"] as String?
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")

    implementation(project(":network"))


    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 启动动画
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Hilt
    val hiltVersion = rootProject.extra["hiltVersion"] as String?
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")

    val glanceVersion = "1.1.0"
    // For Glance support
    implementation("androidx.glance:glance:$glanceVersion")
    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:$glanceVersion")

    // 地图
    implementation("com.amap.api:map2d:6.0.0")

    testImplementation("${rootProject.extra["junit"] as String?}")
    androidTestImplementation("${rootProject.extra["extJunit"] as String?}")
    androidTestImplementation("${rootProject.extra["espressoCore"] as String?}")
    // Test rules and transitive dependencies:
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    // Needed for createAndroidComposeRule, but not createComposeRule:
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}