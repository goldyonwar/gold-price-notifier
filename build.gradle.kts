plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    application
}

group = "com.goldyonwar"
version = "1.0.0"

repositories {
    mavenCentral()
}

// Declares project dependencies for testing and implementation
dependencies {
    testImplementation(libs.kotlin.test)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.slf4j.simple)
    implementation(libs.mcp.kotlin.sdk)
    implementation(libs.kotlinx.io.core)
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.goldyonwar.MainKt")
}