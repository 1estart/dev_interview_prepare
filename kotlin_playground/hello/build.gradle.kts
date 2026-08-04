plugins {
    kotlin("jvm") version "2.2.0"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass = "MainKt"   // имя файла Main.kt + суффикс Kt
}