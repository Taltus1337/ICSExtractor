@echo off
set ICON_DIR=composeApp\commonMain\resources\drawables\launcher_icons
mkdir %ICON_DIR%
set ORIGINAL_ICON=%ICON_DIR%\DevLogoV2.png
copy %1 %ORIGINAL_ICON%

:: Linux
echo 🌀 Creating icon for Linux...
magick convert -resize x128 %ORIGINAL_ICON% %ICON_DIR%\linux.png

:: Windows
echo 🌀 Creating icon for Windows...
magick convert -resize x128 %ORIGINAL_ICON% %ICON_DIR%\windows.ico

:: MacOS
echo 🌀 Creating icon for macOS...
magick convert -resize x128 %ORIGINAL_ICON% %ICON_DIR%\macos.icns

:: Printing code
echo Add this to your build.gradle.kts ⬇️

set OUTPUT=^
val iconsRoot = project.file("composeApp\commonMain\resources\drawables")^

linux {^
  iconFile.set(iconsRoot.resolve("launcher_icons/linux.png"))^
}^

windows {^
  iconFile.set(iconsRoot.resolve("launcher_icons/windows.ico"))^
}^

macOS {^
  iconFile.set(iconsRoot.resolve("launcher_icons/macos.icns"))^
}

echo %OUTPUT%
echo %OUTPUT% | clip
echo Copied to your clipboard
