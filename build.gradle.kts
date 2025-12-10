plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.compose)
    alias(deps.plugins.compose.plugin)
}

group = "io.github.vvmaksim"
version = "1.0-SNAPSHOT"

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
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(deps.kotlin.logging)
    implementation(deps.logback.classic)
    implementation(deps.chesslib)
    implementation(deps.apache.poi)
    implementation(deps.apache.poi.ooxml)
}
