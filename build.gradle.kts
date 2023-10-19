plugins {
    kotlin("multiplatform") version Versions.kotlin apply false
    kotlin("plugin.serialization") version Versions.kotlin apply false
    id("org.jetbrains.compose") version Versions.composeMp apply false
}

buildscript {
    dependencies {
        classpath("dev.icerock.moko:resources-generator:${Versions.mokoRes}")
    }
}