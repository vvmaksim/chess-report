plugins {
    alias(deps.plugins.kotlin.jvm)
    alias(deps.plugins.compose)
    alias(deps.plugins.compose.plugin)
    id("jacoco")
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
