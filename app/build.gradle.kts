import java.io.File
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.unsulliedcode"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.unsulliedcode"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
      compose = true
      aidl = false
      buildConfig = false
      shaders = false
    }

    packaging {
      resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
  val composeBom = platform(libs.androidx.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)

  // Core Android dependencies
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)

  // Arch Components
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)

  // Compose
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.material3)
  // Tooling
  debugImplementation(libs.androidx.compose.ui.tooling)
  // Instrumented tests
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  // Local tests: jUnit, coroutines, Android runner
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation("org.robolectric:robolectric:4.11.1")
  testImplementation("androidx.test:core-ktx:1.5.0")

  // Instrumented tests: jUnit rules and runners
  androidTestImplementation(libs.androidx.test.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
  androidTestImplementation(libs.androidx.test.espresso.core)

  // Navigation
  implementation(libs.androidx.navigation3.ui)
  implementation(libs.androidx.navigation3.runtime)
  implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}

tasks.register("checkDesignTokens") {
    group = "verification"
    description = "Fails the build if raw hex colors or .dp literals are used outside Tokens.kt and AppColors.kt"
    
    // Disable configuration cache for this task
    notCompatibleWithConfigurationCache("Reads files directly")
    
    doLast {
        val srcDir = File(projectDir, "src/main/java")
        var failed = false
        srcDir.walkTopDown().forEach { f ->
            if (f.isFile && f.extension == "kt") {
                val name = f.name
                if (name != "Tokens.kt" && name != "AppColors.kt" && name != "Gradients.kt") {
                    val content = f.readText()
                    val lines = content.lines()
                    lines.forEachIndexed { i, line ->
                        val code = line.substringBefore("//")
                        if (code.contains(".dp") && !code.startsWith("import ")) {
                            System.err.println("Hardcoded .dp literal found in ${f.name}:${i+1}: $line")
                            failed = true
                        }
                        if (Regex("Color\\\\(0x[0-9A-Fa-f]{8}\\\\)").containsMatchIn(code) || code.contains("MaterialTheme.colorScheme") || code.contains("MaterialTheme.typography")) {
                            System.err.println("Hardcoded hex color found in ${f.name}:${i+1}: $line")
                            failed = true
                        }
                    }
                }
            }
        }
        if (failed) {
            throw GradleException("Build failed: Hardcoded tokens detected outside Tokens.kt / AppColors.kt")
        }
    }
}
tasks.named("preBuild").configure {
    dependsOn("checkDesignTokens")
}





