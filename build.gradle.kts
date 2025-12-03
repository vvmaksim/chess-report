plugins {
    alias(deps.plugins.kotlin.jvm)
    id("application")
}

group = "io.github.vvmaksim"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.github.vvmaksim.MainKt"
}

tasks.named<JavaExec>("run") {
    workingDir = file("$projectDir/..")
}
