import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.0"
    id("org.jetbrains.compose") version "0.4.0"
}

val appVersion = "1.0.0"

group = "me.omico.genshinimpact.serverswitcher"
version = appVersion

repositories {
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation(compose.desktop.currentOs)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.ui)
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
    test {
        useJUnitPlatform()
    }
}

compose.desktop {
    application {
        mainClass = "$group.AppKt"
        nativeDistributions {
            targetFormats(TargetFormat.AppImage)
            packageName = "genshin-impact-server-switcher"
            packageVersion = appVersion
            windows {
                upgradeUuid = "a6f0174f-5a69-44a9-b0f6-739ab29c1115"
            }
        }
    }
}
