<div align="center">

# ComposeWan

A [WanAndroid](https://www.wanandroid.com/) client built with Kotlin and Jetpack Compose

An open-source learning project for Compose, Navigation 3, Koin, and Ktor. Architecture inspired by [CoolMallKotlin](https://github.com/Joker-x-dev/CoolMallKotlin).

[简体中文](README.md) | English

[📥 Download APK](https://www.pgyer.com/composewan)

<img src="screenshots/QRCode_1400.png" width="180" alt="Download QR code"/>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF?logo=kotlin)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose_BOM-2026.08-4285F4?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![AGP](https://img.shields.io/badge/AGP-9.3.1-3DDC84?logo=android)](https://developer.android.com/build)
[![minSdk](https://img.shields.io/badge/minSdk-24-green)](https://developer.android.com/google/play/requirements/target-sdk)

</div>

## Features

- **Home**: Banner + article list, pull-to-refresh / load-more, article collect
- **Hot**: Search hot keys and frequently used websites
- **System**: Knowledge tree categories, browse articles in child categories
- **Search**: Search articles by keyword
- **Login / Register**: WanAndroid account login with persistent cookies
- **Mine**: User info, collected articles, About, and Settings
- **Settings**: Chinese / English locale, light / dark / follow system, purple / blue / green / orange skins, account management
- **Article detail**: Open links in a built-in WebView

## Screenshots

<div align="center">
<table>
  <tr>
    <td><img src="screenshots/首页.png" width="240" alt="Home"/></td>
    <td><img src="screenshots/热点.png" width="240" alt="Hot"/></td>
    <td><img src="screenshots/体系.png" width="240" alt="System"/></td>
  </tr>
  <tr>
    <td align="center">Home</td>
    <td align="center">Hot</td>
    <td align="center">System</td>
  </tr>
  <tr>
    <td><img src="screenshots/搜索.png" width="240" alt="Search"/></td>
    <td><img src="screenshots/文章详情.png" width="240" alt="Article detail"/></td>
    <td><img src="screenshots/我的.png" width="240" alt="Mine"/></td>
  </tr>
  <tr>
    <td align="center">Search</td>
    <td align="center">Article detail</td>
    <td align="center">Mine</td>
  </tr>
  <tr>
    <td><img src="screenshots/收藏.png" width="240" alt="Collect"/></td>
    <td><img src="screenshots/关于.png" width="240" alt="About"/></td>
    <td><img src="screenshots/换肤.png" width="240" alt="Theme"/></td>
  </tr>
  <tr>
    <td align="center">Collect</td>
    <td align="center">About</td>
    <td align="center">Theme</td>
  </tr>
</table>
</div>

## Tech Stack

| Category | Choice | Version |
| --- | --- | --- |
| Language / Build | Kotlin, AGP, Gradle, Java 21 | Kotlin 2.4.10, AGP 9.3.1, Gradle 9.6.1 |
| UI | Jetpack Compose Material3, Navigation 3 | Compose BOM 2026.08.00, Nav3 1.1.1 |
| Architecture | MVI (UiState / Intent / Event) + UseCase | — |
| DI | Koin | 4.2.2 |
| Network | Ktor Client + Kotlinx Serialization | Ktor 3.5.2 |
| Image | Coil 3 | 3.5.0 |
| Storage | MMKV (cookies, theme, locale prefs) | 2.4.1 |
| Others | Timber, Toaster, Coroutines | — |

SDK: `minSdk 24`, `targetSdk 36`, `compileSdk 37`.

## Architecture

Each feature is layered as **Screen → ViewModel → UseCase → Repository**:

```
┌─────────────┐     Intent      ┌─────────────┐
│   Screen    │ ───────────────►│  ViewModel  │
│  (Compose)  │ ◄───────────────│  UiState    │
└─────────────┘     Event       └──────┬──────┘
                                       │
                                       ▼
                                ┌─────────────┐
                                │   UseCase   │
                                └──────┬──────┘
                                       │
                                       ▼
                                ┌─────────────┐     Ktor      ┌──────────────┐
                                │ WanRepository│ ───────────► │ wanandroid.com│
                                └─────────────┘               └──────────────┘
```

- **ViewModel**: Holds `UiState`, accepts `Intent`, emits one-shot `Event`s via `SharedFlow` (navigation, toast)
- **UseCase**: Encapsulates business logic and maps network beans to UI models
- **WanRepository**: Single entry for WanAndroid APIs; cookies persist in MMKV
- **Navigation**: Navigation 3 with multiple back stacks; each of the four bottom tabs keeps its own stack

## Modules

```
ComposeWan
├── app                 # App entry, screens, ViewModels, navigation
├── libs
│   ├── common          # Data models, MMKV, toast, logging, user session
│   ├── compose         # Theme and shared Compose components
│   └── network         # Ktor wrapper, WanRepository (KMP commonMain)
└── gradle/libs.versions.toml
```

## Requirements

- Android Studio Otter or newer (AGP 9 support required)
- JDK 21
- Android SDK 37

## Run

```bash
git clone https://github.com/zqlq4ever/ComposeWan.git
cd ComposeWan
```

Open the project in Android Studio, sync Gradle, then run `app`. From the command line:

```bash
./gradlew :app:installDebug
```

On Windows, use `gradlew.bat`.

## API

Data comes from the [WanAndroid open API](https://www.wanandroid.com/blog/show/2). Base URL:

```
https://www.wanandroid.com/
```

Login state is kept via cookies, stored in MMKV by `MmkvCookiesStorage`.

## Acknowledgments

- [WanAndroid](https://www.wanandroid.com/) for the public API
- [CoolMallKotlin](https://github.com/Joker-x-dev/CoolMallKotlin) for architecture reference

This project is for learning and discussion only.
