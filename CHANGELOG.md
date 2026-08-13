# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Added best-effort web SMS composition through `sms:` URLs. Web cannot report sent or cancelled status, and protocol-handler behavior varies by browser and operating system. [#33](https://github.com/moberwasserlechner/capacitor-sms/issues/33)

### Breaking
- Capacitor 8 is now the only supported Capacitor major.
- iOS distribution uses Swift Package Manager only. CocoaPods consumers must migrate their Capacitor iOS project before upgrading. [#35](https://github.com/moberwasserlechner/capacitor-sms/issues/35)
- Android now requires API 24 and Java 21.
- Renamed `SmsManager.send()` to `SmsManager.compose()` and `SmsSendOptions` to `SmsComposeOptions` to clarify that the plugin opens the platform SMS composer; it does not send messages itself.

### Changed
- Documented why direct SMS sending without user interaction is not supported. [#38](https://github.com/moberwasserlechner/capacitor-sms/issues/38)
- Migrated the Android implementation from Java to Kotlin and added unit tests.
- Modernized TypeScript builds, package exports, package verification, and CI, including packed Swift target validation and an Android Capacitor-host integration build.
- Added maintenance, build, license, download, and release badges to the README.
- Standardized development on pnpm while documenting both pnpm and npm installation.

## [7.0.0] - 2025-07-22

### Breaking
- Capacitor 7 is new minimum peer dependency! [#28](https://github.com/moberwasserlechner/capacitor-sms/issues/28)

## [6.0.0] - 2024-07-25

### Breaking
- Capacitor 6.x is new minimum peer dependency! [#24](https://github.com/moberwasserlechner/capacitor-sms/issues/24)

## [5.0.0] - 2023-09-04

### Breaking
- Capacitor 5.x is new minimum peer dependency!
- Web throws `UNIMPLEMENTED` code instead of `ERR_PLATFORM_NOT_SUPPORTED`.

## [4.0.0] - 2022-09-18

### Breaking
- Capacitor 4.x is new minimum peer dependency!

## [3.0.2] - 2022-05-02

### Fixes
* Error service not found - [#14](https://github.com/moberwasserlechner/capacitor-sms/issues/14)

## [3.0.1] - 2022-04-13

### Fixes
* Readme and Changelog links

## [3.0.0] - 2022-04-13

### Changed

- Upgrade to support Capacitor 3.x - [#12](https://github.com/moberwasserlechner/capacitor-sms/pull/12), [#11](https://github.com/moberwasserlechner/capacitor-sms/issues/11) ... thx [@chrum](https://github.com/chrum)
- Fix thread access - [#13](https://github.com/moberwasserlechner/capacitor-sms/pull/13), [#9](https://github.com/moberwasserlechner/capacitor-sms/issues/9)... thx [@chrum](https://github.com/chrum)

### Breaking
- Capacitor 3.x is new minimum peer dependency!

## [2.0.0] - 2020-04-09

### Breaking
- Capacitor 2.x is new minimum peer dependency!

## [1.0.0] - 2019-06-23

### Breaking
-  Capacitor 1.x is new minimum peer dependency

## [1.0.0-beta.2] - 2019-03-07

### Fixed
- Capacitor version was pinned to beta.16 in podspec file. That prevented the capacitor upgrade to beta.18 by using npx cap sync ios

## [1.0.0-beta.1] - 2019-03-01

### Added
- Send SMS to more than one recipient by opening the native SMS App on Android or iOS

### Breaking
- This plugin will not support Android's background sending features as Google recently change their policy on sending SMS limiting the permission to only a few usecases. See https://play.google.com/about/privacy-security-deception/permissions/ for details.

[Unreleased]: https://github.com/moberwasserlechner/capacitor-sms/compare/7.0.0..develop
[7.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/6.0.0..7.0.0
[6.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/5.0.0..6.0.0
[5.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/4.0.0..5.0.0
[4.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/3.0.2..4.0.0
[3.0.2]: https://github.com/moberwasserlechner/capacitor-sms/compare/3.0.1..3.0.2
[3.0.1]: https://github.com/moberwasserlechner/capacitor-sms/compare/3.0.0..3.0.1
[3.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/2.0.0..3.0.0
[2.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0..2.0.0
[1.0.0]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.2..1.0.0
[1.0.0-beta.2]: https://github.com/moberwasserlechner/capacitor-sms/compare/1.0.0-beta.1..1.0.0-beta.2
[1.0.0-beta.1]: https://github.com/moberwasserlechner/capacitor-sms/releases/tag/1.0.0-beta.1
