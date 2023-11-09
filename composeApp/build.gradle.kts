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
            licenseFile.set(project.file("../LICENSE.txt"))
            packageVersion = ProjectConfig.versionName

            val iconsRoot = project.file("../composeApp/src/commonMain/resources/drawables/launcher_icons")

            macOS {
                iconFile.set(iconsRoot.resolve("DevLogoV2.ico"))
            }
            windows {
                iconFile.set(iconsRoot.resolve("DevLogoV2.ico"))
                menuGroup = "ICS Extractor"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "c522f6c9-8f25-4c04-a7a9-6d86b1a15e0e"
            }
            linux {
                iconFile.set(iconsRoot.resolve("DevLogoV2.png"))
            }


            buildTypes.release.proguard {
//                version.set("7.4.0") // to use java > 18
                isEnabled = false
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "de.franzsw.extractor"
    multiplatformResourcesClassName = "SharedRes"
    multiplatformResourcesSourceSet = "commonMain"
}