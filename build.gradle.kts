import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    antlr
}

group = "me.furetur"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-visitor")
}

dependencies {
    antlr("org.antlr:antlr4:4.9.1")

    implementation("guru.zoroark.lixy:lixy-jvm:1849bb1")
    implementation("com.github.Furetur:pratt:0.1.2")
    implementation("de.m3y.kformat:kformat:0.8")

    testImplementation(kotlin("test"))
    testImplementation("io.strikt:strikt-core:0.32.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}