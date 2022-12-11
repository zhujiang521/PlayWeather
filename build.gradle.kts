import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.compose") version "1.2.1"
}

group = "me.zhujiang"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.datastore:datastore-preferences-core:1.1.0-dev01")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PlayWeather"
            packageVersion = "1.0.0"
            description = "Play Weather App"
            copyright = "© 2022 My Name. All rights reserved."
            vendor = "Lenovo"
            licenseFile.set(project.file("LICENSE.txt"))
            modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")

            linux {
                // a version for all Linux distributables
                packageVersion = "1.0.0"
                // a version only for the deb package
                debPackageVersion = "1.0.0"
                // a version only for the rpm package
                rpmPackageVersion = "1.0.0"
                // 设置图标
                iconFile.set(project.file("launcher/icon.png"))
            }
            macOS {
                // a version for all macOS distributables
                packageVersion = "1.1.0"
                // a version only for the dmg package
                dmgPackageVersion = "1.1.0"
                // a version only for the pkg package
                pkgPackageVersion = "1.1.0"
                // 显示在菜单栏、“关于”菜单项、停靠栏等中的应用程序名称
                dockName = "PlayWeather"

                // a build version for all macOS distributables
                packageBuildVersion = "1.1.0"
                // a build version only for the dmg package
                dmgPackageBuildVersion = "1.1.0"
                // a build version only for the pkg package
                pkgPackageBuildVersion = "1.1.0"
                // 设置图标
                iconFile.set(project.file("launcher/icon.icns"))
            }
            windows {
                // a version for all Windows distributables
                packageVersion = "1.2.0"
                // a version only for the msi package
                msiPackageVersion = "1.2.0"
                // a version only for the exe package
                exePackageVersion = "1.2.0"
                // 设置图标
                iconFile.set(project.file("launcher/icon.ico"))
            }
        }
        buildTypes.release.proguard {
            obfuscate.set(false)
            configurationFiles.from(project.file("proguard-rules.pro"))
        }
    }
}