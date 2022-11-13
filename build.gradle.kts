import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.7.10"
    application
}

group = "ru.hukumister"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ru.hukumister.main.MainKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveVersion.set("")
        archiveClassifier.set("")
        archiveBaseName.set("transaction")

        manifest {
            attributes(mapOf("Main-Class" to "ru.hukumister.main.MainKt"))
        }
    }
}