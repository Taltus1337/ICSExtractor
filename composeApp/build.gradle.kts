import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvm("desktop")

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(Dependencies.mpSettings)
                implementation(Dependencies.kotlinxSerializationJSON)
                implementation(Dependencies.excelKT)
                implementation(Dependencies.filePicker)

                implementation(Dependencies.mokoRes)
                implementation(Dependencies.mokoResCompose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
            }
        }

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ICSExtractor"
            version = ProjectConfig.versionName
            description = "Ics-Extractor (XLSX to ICS)"
            copyright = "© 2023 Franz Software by Roland Franz. All rights reserved."
            vendor = "Franz Software"
//            licenseFile.set(project.file("LICENSE.txt"))
            packageVersion = ProjectConfig.versionName

            windows {
                iconFile.set(project.file("src/commonMain/resources/drawables/launcher_icons/DevLogoV2.png"))
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "de.franzsw.extractor"
    multiplatformResourcesClassName = "SharedRes"
    multiplatformResourcesSourceSet = "commonMain"
}