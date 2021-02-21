import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.lang.System.getenv
import org.gradle.api.*
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.cocoapods.*
import org.jetbrains.kotlin.gradle.dsl.*


plugins {
    kotlin("multiplatform") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.20"
    id("org.jetbrains.kotlin.native.cocoapods") version "1.4.30-RC"
    id("com.android.library")
    id("kotlin-android-extensions")
    `maven-publish`
}

group = System.getenv("GITHUB_REPOSITORY")?.split('/')?.first()?.plus(".me.soumyajitdas") ?: "me.soumyajitdas"
version = System.getenv("GITHUB_REF")?.split('/')?.last() ?: "1.0-development"
val archivesBaseName = "${group}-${version}"
//val Project.xcodeproj get() = property("xcodeproj") as String

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

kotlin {
    android {
        publishLibraryVariants("release")
    }
    ios()
    cocoapods {
        summary = "A Kotlin MPP Cocoapods Template Library"
        homepage = "https://www.github.com/${getenv("GITHUB_REPOSITORY")}"

        //podfile = rootProject.file("$xcodeproj/Podfile")

        ios.deploymentTarget = "13.5"
    }
    sourceSets {
        val coroutinesVersion = "1.4.1-native-mt"
        val ktorVersion = "1.5.0"
        val serializationJsonVersion = "1.0.1"
        val ktorClientJsonVersion = "1.2.2"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"){
                    version {
                        strictly(coroutinesVersion)
                    }
                }
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationJsonVersion")
                implementation ("io.ktor:ktor-client-json:$ktorClientJsonVersion")
                implementation ("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.2.0")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
                implementation ("io.ktor:ktor-client:$ktorVersion")
            }
        }
        val androidTest by getting
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosTest by getting
    }
}

tasks.register<Delete>("cleanPodBuild") {
    arrayOf(buildDir, file("$name.podspec"), file("gen"), file("Pods")).let {
        destroyables.register(it)
        delete(it)
    }
}.also { tasks.named("clean").configure { dependsOn(it) } }

configurePublishing()

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", archivesBaseName)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

fun Project.configurePublishing() {
    getenv("GITHUB_REPOSITORY")?.let {
        publishingExtension {
            repositories {
                maven {
                    name = "github"
                    url = uri("https://maven.pkg.github.com/$it")
                    credentials(PasswordCredentials::class)
                }
            }
        }
    }
}

inline fun KotlinMultiplatformExtension.cocoapods(crossinline block: CocoapodsExtension.() -> Unit) =
    (this as ExtensionAware).extensions.configure(CocoapodsExtension::class) { block() }

inline fun Project.publishingExtension(crossinline block: PublishingExtension.() -> Unit) =
    extensions.configure(PublishingExtension::class) { block() }

val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(buildDir, "xcode-frameworks")
    from({ framework.outputDirectory })
    into(targetDir)
    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText("#!/bin/bash\n"
                + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                + "cd '${rootProject.rootDir}'\n"
                + "./gradlew \$@\n")
        gradlew.setExecutable(true)
    }
}

tasks.getByName("build").dependsOn(packForXcode)