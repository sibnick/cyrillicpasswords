# Cyrillic Passwords

[![Android Version](https://img.shields.io/badge/Android-5.0%2B-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-M3-purple.svg)](https://developer.android.com/jetpack/compose)

**Cyrillic Passwords** is a lightweight, privacy-focused Android utility designed for users who remember their passwords based on a Russian keyboard layout but need to input them using the QWERTY (EN) layout.

## 🚀 The Problem
Many users create passwords by thinking of a Russian word and typing it while their keyboard is set to English (e.g., typing "пароль" results in "ghjkm"). On mobile devices, switching layouts and remembering these mappings can be tedious. This app solves that by providing a real-time converter.

## ✨ Features
- **Real-time Conversion:** Instantly see the QWERTY equivalent as you type in Cyrillic.
- **Secure Copy:** One-tap copy to clipboard with support for Android 13+ sensitive content flags.
- **Privacy First:** 
    - 🔒 100% Offline: No internet permission requested.
    - 🚫 No Tracking: No analytics, no ads, no third-party SDKs.
    - 🛡️ Local Processing: Everything happens on your device.
- **Modern UI:** Built with Jetpack Compose and Material 3 for a fluid, native experience.
- **Visibility Toggle:** Easily show or hide your password while converting.

## 🛠 Tech Stack
- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Design:** [Material 3](https://m3.material.io/)

## 📦 Building from Source
1. Clone the repository:
   ```bash
   git clone https://github.com/sibnick/cyrillicpasswords.git
   ```
2. Open the project in **Android Studio (Hedgehog or newer)**.
3. Sync Gradle and build the project.
4. To generate a signed release, ensure you have your `keystore.jks` and configure the signing properties in `app/build.gradle`.

## 📜 Privacy Policy
The application does not collect or transmit any data. For more details, see the [Privacy Policy](PRIVACY_POLICY.md).

## 📄 License
This project is licensed under the terms of the [License](license.txt).
