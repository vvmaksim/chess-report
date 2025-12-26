import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.compose)
    alias(deps.plugins.compose.plugin)
    alias(deps.plugins.kotlin.serialization)
    id("jacoco")
}

group = "io.github.vvmaksim"
version = "1.1.3"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    google()
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "io.github.vvmaksim.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb, TargetFormat.AppImage, TargetFormat.Dmg)
            packageName = "Chess Report"
            packageVersion = "1.1.3"
            description = "Chess Report application"
            vendor = "vvmaksim"
            modules("java.naming")

            linux {
                iconFile.set(project.file("src/main/resources/icons/app_icon.png"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/icons/app_icon.png"))
            }
            macOS {
                iconFile.set(project.file("src/main/resources/icons/app_icon.png"))
            }
        }
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(deps.kotlin.logging)
    implementation(deps.logback.classic)
    implementation(deps.chesslib)
    implementation(deps.apache.poi)
    implementation(deps.apache.poi.ooxml)
    implementation(deps.ktor.client.core)
    implementation(deps.ktor.client.cio)
    implementation(deps.log4j.api) // Bridge for Log4j2 API
    implementation(deps.log4j.slf4j.impl) // Bridge for Log4j2 to SLF4J
    implementation(deps.lifecycle.viewmodel)
    implementation(deps.lifecycle.viewmodel.savedstate)
    implementation(deps.lifecycle.viewmodel.compose)
    implementation(deps.kotlinx.coroutines.swing)
    implementation(deps.kotlinx.serialization.json)

    testImplementation(deps.junit.jupiter.api)
    testRuntimeOnly(deps.junit.jupiter.engine)
    testImplementation(deps.junit.jupiter.params)
    testImplementation(deps.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/test/html"))
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}
